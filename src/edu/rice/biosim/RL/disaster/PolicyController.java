package edu.rice.biosim.RL.disaster;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.traclabs.biosim.idl.framework.*;

/**
 * @author Travis R. Fischer
 *
 * Used to control our policy driven reinforcment learners.
 */

/**
 * @author Travis
 * 
 */
public class PolicyController extends AController {

	private int neuralNetSeed = 0;

	private float REWARD_DEATH = -1;

	private float REWARD_STARVE = -0.4f;

	private float REWARD_SUFFOCATE = -0.2f;

	private float REWARD_THIRST = -0.2f;

	private float REWARD_POISON = -0.2f;

	private float REWARD_SICK = -0.2f;

	private float REWARD_O2_OVER = -16f * (float) Math.pow(10, -4);

	private float REWARD_CO2_OVER = -4f * (float) Math.pow(10, -4);

	private float REWARD_H2_OVER = -4f * (float) Math.pow(10, -4);

	private float REWARD_POT_OVER = -8f * (float) Math.pow(10, -4)
			* (1000f / 18.015f);

	private float REWARD_DIRTY_OVER = -8f * (float) Math.pow(10, -4)
			* (1000f / 18.015f);

	private float REWARD_GREY_OVER = -8f * (float) Math.pow(10, -4)
			* (1000f / 18.015f);

	private float REWARD_FOOD_OVER = -5f * (float) Math.pow(10, -4);

	private float REWARD_BIOMASS_OVER = -5f * (float) Math.pow(10, -4);

	private float REWARD_WASTE_OVER = -5f * (float) Math.pow(10, -4);

	private float REWARD_POWER_USED = -1f * (float) Math.pow(10, -6);

	private float REWARD_NO_STARVE = +0.02f;

	private float REWARD_NO_SUFFOCATE = +0.02f;

	private float REWARD_NO_THIRST = +0.02f;

	private float REWARD_NO_POISON = +0.02f;

	private float REWARD_NO_SICK = +0.02f;

	private float REWARD_NO_OVER = +0.02f;

	private float REWARD_CROP_DEATH = -0.2f;

	private float REWARD_NO_CROP_DEATH = +0.02f;

	private float REWARD_COMPLETE = 1f;

	private float NN_BASELINE = REWARD_NO_STARVE + REWARD_NO_SUFFOCATE
			+ REWARD_NO_THIRST + REWARD_NO_SICK + REWARD_NO_OVER * 9
			+ REWARD_NO_CROP_DEATH;

	private String policyDirectory;

	/**
	 * @param wrsAgent
	 *            The Agent controlling the WRS
	 */
	private Agent wrsAgent;

	/**
	 * @param plantAgent
	 *            The Agent controlling the Biomass chamber
	 */
	private Agent plantAgent;

	private Agent ogsAgent;

	private Agent vccrAgent;

	private Agent crsAgent;

	/**
	 * @param failureTick
	 *            Tick where failure will occur
	 */
	private int failureTick;

	/**
	 * @param causeFailure
	 *            Boolean indicating if power shortage will occur
	 */
	private boolean causeFailure;

	private Random r;

	private File outFile;

	private FileWriter fw;

	private PrintWriter pw;

	private Logger myLogger;

	private DecimalFormat numFormat;

	/**
	 * Default constructor. Initializes all paramenters
	 */
	public PolicyController() {
		super();
		myLogger = Logger.getLogger(this.getClass());
		myLogger.setLevel(Level.INFO);
		numFormat = new DecimalFormat("#,##0.0;(#)");
		if (myLogger.isDebugEnabled()) {
			try {
				outFile = new File("policycontroller-output.txt");
				fw = new FileWriter(outFile);
			} catch (IOException e) {
			}
			pw = new PrintWriter(fw, true);
		}
		r = new Random();

		wrsAgent = new WRSAgent(NN_BASELINE);
		crsAgent = new CRSAgent(NN_BASELINE);
		ogsAgent = new OGSAgent(NN_BASELINE);
		vccrAgent = new VCCRAgent(NN_BASELINE);
		plantAgent = new FixedPlantAgent(1000);

	}

	/**
	 * Overloaded constructor. Allows setting of power failure.
	 * 
	 * @param fail
	 *            Boolean indicating if failure will occur
	 * @param ticks
	 *            Tick on which power failure will occur
	 */
	public PolicyController(boolean fail, int ticks) {
		super();
		myLogger = Logger.getLogger(this.getClass());
		myLogger.setLevel(Level.INFO);
		numFormat = new DecimalFormat("#,##0.0;(#)");
		if (myLogger.isDebugEnabled()) {
			try {
				outFile = new File("policycontroller-output.txt");
				fw = new FileWriter(outFile);
			} catch (IOException e) {
			}
			pw = new PrintWriter(fw, true);
		}

		wrsAgent = new WRSAgent(NN_BASELINE);
		crsAgent = new CRSAgent(NN_BASELINE);
		ogsAgent = new OGSAgent(NN_BASELINE);
		vccrAgent = new VCCRAgent(NN_BASELINE);
		plantAgent = new FixedPlantAgent(1000);

		r = new Random();

		// Minor malfunction scheduled. PowerStore capacity permanently reduced
		// by 25%
		if (fail) {
			myPowerStore.scheduleMalfunction(MalfunctionIntensity.LOW_MALF,
					MalfunctionLength.PERMANENT_MALF, ticks);
			myStr.setDisasterTimeTick(ticks);
		}

	}

	public void loadPolicies(String directory, int seed) {
		policyDirectory = directory;
		if (seed > -1) {
			wrsAgent.loadPolicy(directory, seed + ".nn");
			crsAgent.loadPolicy(directory, seed + ".nn");
			vccrAgent.loadPolicy(directory, seed + ".nn");
			ogsAgent.loadPolicy(directory, seed + ".nn");
			plantAgent.loadPolicy(directory, seed + ".nn");

		}
		savePolicies(seed + ".nn");
		neuralNetSeed = seed + 1;
	}

	/**
	 * Public interface to run simulation with Policy controllers
	 */
	public void runSim() {
		myBioDriver.setPauseSimulation(true);
		myBioDriver.startSimulation();
		myLogger.info("Controller starting run");

		while (!myBioDriver.isDone()) {

			stepSim();
			recordIncrementalStatus();
			calcIncrementalRewards();
		}
		myLogger.info("Run completed. Recording situation.");
		recordFinalSituation();
		calcFinalRewards();
		writeStrFile();
		savePolicies(neuralNetSeed + ".nn");
		saveHistories(neuralNetSeed);
		saveRewards(neuralNetSeed);
		neuralNetSeed++;
	}

	private void saveRewards(int neuralNetSeed2) {
		// TODO Auto-generated method stub
		wrsAgent.saveMyRewards(neuralNetSeed2);

		crsAgent.saveMyRewards(neuralNetSeed2);
		vccrAgent.saveMyRewards(neuralNetSeed2);
		ogsAgent.saveMyRewards(neuralNetSeed2);
		plantAgent.saveMyRewards(neuralNetSeed2);

	}

	private void saveHistories(int neuralNetSeed2) {
		// TODO Auto-generated method stub
		wrsAgent.saveHistory(neuralNetSeed2);

		crsAgent.saveHistory(neuralNetSeed2);
		vccrAgent.saveHistory(neuralNetSeed2);
		ogsAgent.saveHistory(neuralNetSeed2);
		plantAgent.saveHistory(neuralNetSeed2);

	}

	private void calcFinalRewards() {

		float reward = 0;
		float powerReward = 0;
		float arsReward = 0;
		float wrsReward = 0;
		float plantReward = 0;

		if (myStr.getNumTicks() == MAX_MISSION_TICKS) {
			reward += REWARD_COMPLETE;
		}

		/*
		 * crsAgent.updatePolicy(reward); ogsAgent.updatePolicy(reward);
		 * vccrAgent.updatePolicy(reward); wrsAgent.updatePolicy(reward);
		 * 
		 * plantAgent.updatePolicy(reward);
		 */
	}

	private void calcIncrementalRewards() {

		float reward = 0;
		float powerReward = 0;
		float arsReward = 0;
		float wrsReward = 0;
		float plantReward = 0;

		float totalReward = 0;

		// mission failure reward
		wrsAgent.calculateIncrementalReward(incDead, Rewards.RewardEnum.DEATH);
		crsAgent.calculateIncrementalReward(incDead, Rewards.RewardEnum.DEATH);
		ogsAgent.calculateIncrementalReward(incDead, Rewards.RewardEnum.DEATH);
		vccrAgent.calculateIncrementalReward(incDead, Rewards.RewardEnum.DEATH);
		plantAgent
				.calculateIncrementalReward(incDead, Rewards.RewardEnum.DEATH);

		// crew starvation reward
		wrsAgent.calculateIncrementalReward(incStarve,
				Rewards.RewardEnum.STARVE);
		crsAgent.calculateIncrementalReward(incStarve,
				Rewards.RewardEnum.STARVE);
		ogsAgent.calculateIncrementalReward(incStarve,
				Rewards.RewardEnum.STARVE);
		vccrAgent.calculateIncrementalReward(incStarve,
				Rewards.RewardEnum.STARVE);
		plantAgent.calculateIncrementalReward(incStarve,
				Rewards.RewardEnum.STARVE);

		// crew poison reward
		wrsAgent.calculateIncrementalReward(incPoison,
				Rewards.RewardEnum.POISON);
		crsAgent.calculateIncrementalReward(incPoison,
				Rewards.RewardEnum.POISON);
		ogsAgent.calculateIncrementalReward(incPoison,
				Rewards.RewardEnum.POISON);
		vccrAgent.calculateIncrementalReward(incPoison,
				Rewards.RewardEnum.POISON);
		plantAgent.calculateIncrementalReward(incPoison,
				Rewards.RewardEnum.POISON);

		// crew sick reward
		wrsAgent.calculateIncrementalReward(incSick, Rewards.RewardEnum.SICK);
		crsAgent.calculateIncrementalReward(incSick, Rewards.RewardEnum.SICK);
		ogsAgent.calculateIncrementalReward(incSick, Rewards.RewardEnum.SICK);
		vccrAgent.calculateIncrementalReward(incSick, Rewards.RewardEnum.SICK);
		plantAgent.calculateIncrementalReward(incSick, Rewards.RewardEnum.SICK);

		// crew suffocate reward
		wrsAgent.calculateIncrementalReward(incSuffocate,
				Rewards.RewardEnum.SUFFOCATE);
		crsAgent.calculateIncrementalReward(incSuffocate,
				Rewards.RewardEnum.SUFFOCATE);
		ogsAgent.calculateIncrementalReward(incSuffocate,
				Rewards.RewardEnum.SUFFOCATE);
		vccrAgent.calculateIncrementalReward(incSuffocate,
				Rewards.RewardEnum.SUFFOCATE);
		plantAgent.calculateIncrementalReward(incSuffocate,
				Rewards.RewardEnum.SUFFOCATE);

		// crew thirst reward
		wrsAgent.calculateIncrementalReward(incThirst,
				Rewards.RewardEnum.THIRST);
		crsAgent.calculateIncrementalReward(incThirst,
				Rewards.RewardEnum.THIRST);
		ogsAgent.calculateIncrementalReward(incThirst,
				Rewards.RewardEnum.THIRST);
		vccrAgent.calculateIncrementalReward(incThirst,
				Rewards.RewardEnum.THIRST);
		plantAgent.calculateIncrementalReward(incThirst,
				Rewards.RewardEnum.THIRST);

		// potable h2o overflow
		wrsAgent.calculateIncrementalReward(incPotH2OOver,
				Rewards.RewardEnum.POT_OVER);
		crsAgent.calculateIncrementalReward(incPotH2OOver,
				Rewards.RewardEnum.POT_OVER);
		ogsAgent.calculateIncrementalReward(incPotH2OOver,
				Rewards.RewardEnum.POT_OVER);
		vccrAgent.calculateIncrementalReward(incPotH2OOver,
				Rewards.RewardEnum.POT_OVER);
		plantAgent.calculateIncrementalReward(incPotH2OOver,
				Rewards.RewardEnum.POT_OVER);

		// o2 overflow
		wrsAgent.calculateIncrementalReward(incO2Over,
				Rewards.RewardEnum.O2_OVER);
		crsAgent.calculateIncrementalReward(incO2Over,
				Rewards.RewardEnum.O2_OVER);
		ogsAgent.calculateIncrementalReward(incO2Over,
				Rewards.RewardEnum.O2_OVER);
		vccrAgent.calculateIncrementalReward(incO2Over,
				Rewards.RewardEnum.O2_OVER);
		plantAgent.calculateIncrementalReward(incO2Over,
				Rewards.RewardEnum.O2_OVER);

		// dirty h2o overflow
		wrsAgent.calculateIncrementalReward(incDirtyH2OOver,
				Rewards.RewardEnum.DIRTY_OVER);
		crsAgent.calculateIncrementalReward(incDirtyH2OOver,
				Rewards.RewardEnum.DIRTY_OVER);
		ogsAgent.calculateIncrementalReward(incDirtyH2OOver,
				Rewards.RewardEnum.DIRTY_OVER);
		vccrAgent.calculateIncrementalReward(incDirtyH2OOver,
				Rewards.RewardEnum.DIRTY_OVER);
		plantAgent.calculateIncrementalReward(incDirtyH2OOver,
				Rewards.RewardEnum.DIRTY_OVER);

		// o2 overflow
		wrsAgent.calculateIncrementalReward(incCO2Over,
				Rewards.RewardEnum.CO2_OVER);
		crsAgent.calculateIncrementalReward(incCO2Over,
				Rewards.RewardEnum.CO2_OVER);
		ogsAgent.calculateIncrementalReward(incCO2Over,
				Rewards.RewardEnum.CO2_OVER);
		vccrAgent.calculateIncrementalReward(incCO2Over,
				Rewards.RewardEnum.CO2_OVER);
		plantAgent.calculateIncrementalReward(incCO2Over,
				Rewards.RewardEnum.CO2_OVER);

		// grey h2o overflow
		wrsAgent.calculateIncrementalReward(incGreyH2OOver,
				Rewards.RewardEnum.GREY_OVER);
		crsAgent.calculateIncrementalReward(incGreyH2OOver,
				Rewards.RewardEnum.GREY_OVER);
		ogsAgent.calculateIncrementalReward(incGreyH2OOver,
				Rewards.RewardEnum.GREY_OVER);
		vccrAgent.calculateIncrementalReward(incGreyH2OOver,
				Rewards.RewardEnum.GREY_OVER);
		plantAgent.calculateIncrementalReward(incGreyH2OOver,
				Rewards.RewardEnum.GREY_OVER);

		// biomass overflow
		wrsAgent.calculateIncrementalReward(incBiomassOver,
				Rewards.RewardEnum.BIOMASS_OVER);
		crsAgent.calculateIncrementalReward(incBiomassOver,
				Rewards.RewardEnum.BIOMASS_OVER);
		ogsAgent.calculateIncrementalReward(incBiomassOver,
				Rewards.RewardEnum.BIOMASS_OVER);
		vccrAgent.calculateIncrementalReward(incBiomassOver,
				Rewards.RewardEnum.BIOMASS_OVER);
		plantAgent.calculateIncrementalReward(incBiomassOver,
				Rewards.RewardEnum.BIOMASS_OVER);

		// dead plants overflow
		wrsAgent.calculateIncrementalReward(incDeadPlants,
				Rewards.RewardEnum.CROP_DEATH);
		crsAgent.calculateIncrementalReward(incDeadPlants,
				Rewards.RewardEnum.CROP_DEATH);
		ogsAgent.calculateIncrementalReward(incDeadPlants,
				Rewards.RewardEnum.CROP_DEATH);
		vccrAgent.calculateIncrementalReward(incDeadPlants,
				Rewards.RewardEnum.CROP_DEATH);
		plantAgent.calculateIncrementalReward(incDeadPlants,
				Rewards.RewardEnum.CROP_DEATH);

		// biomass overflow
		wrsAgent.calculateIncrementalReward(wrsAgent.getCurrentPowerUsage(),
				Rewards.RewardEnum.POWER_USED);
		crsAgent.calculateIncrementalReward(crsAgent.getCurrentPowerUsage(),
				Rewards.RewardEnum.POWER_USED);
		ogsAgent.calculateIncrementalReward(ogsAgent.getCurrentPowerUsage(),
				Rewards.RewardEnum.POWER_USED);
		vccrAgent.calculateIncrementalReward(vccrAgent.getCurrentPowerUsage(),
				Rewards.RewardEnum.POWER_USED);
		plantAgent.calculateIncrementalReward(
				plantAgent.getCurrentPowerUsage(),
				Rewards.RewardEnum.POWER_USED);

		totalReward = wrsAgent.myRewards.getTempReward()
				+ crsAgent.myRewards.getTempReward()
				+ vccrAgent.myRewards.getTempReward()
				+ plantAgent.myRewards.getTempReward()
				+ ogsAgent.myRewards.getTempReward();

		myStr.updateTotalReward(totalReward);
		wrsAgent.updatePolicy();
		crsAgent.updatePolicy();
		ogsAgent.updatePolicy();
		vccrAgent.updatePolicy();
		plantAgent.updatePolicy();

	}

	/**
	 * Steps through one mission tick. Each agent evaluates state and makes
	 * command decision for its BioModule. These actions are implemented and the
	 * mission time is advanced.
	 */
	private void stepSim() {

		List<Integer> list = createRandomList(5);

		int temp;

		while (!list.isEmpty()) {
			temp = list.remove(0);

			switch (temp) {
			case 0:
				wrsAgent.calcAction();
				break;
			case 1:
				crsAgent.calcAction();
				break;
			case 2:
				plantAgent.calcAction();
				break;
			case 3:
				vccrAgent.calcAction();
				break;
			case 4:
				ogsAgent.calcAction();
				break;

			}
		}

		// advancing the sim 1 tick
		myBioDriver.advanceOneTick();
	}

	public List<Integer> createRandomList(int n) {

		List<Integer> temp = new LinkedList<Integer>();
		for (int i = 0; i < n; i++) {
			temp.add(r.nextInt(i + 1), i);
		}

		return temp;
	}

	public void savePolicies(String filename) {
		wrsAgent.savePolicy(policyDirectory, filename);
		crsAgent.savePolicy(policyDirectory, filename);
		ogsAgent.savePolicy(policyDirectory, filename);
		vccrAgent.savePolicy(policyDirectory, filename);
		plantAgent.savePolicy(policyDirectory, filename);

		updateLearningRates();
	}

	private void updateLearningRates() {
		wrsAgent.updateLearningRate();
		crsAgent.updateLearningRate();
		ogsAgent.updateLearningRate();
		vccrAgent.updateLearningRate();
		plantAgent.updateLearningRate();

	}

	@Override
	public void resetController() {
		wrsAgent.resetAgent();
		crsAgent.resetAgent();
		ogsAgent.resetAgent();
		vccrAgent.resetAgent();
		plantAgent.resetAgent();

		myBioHolder.theBioDriver.reset();
		resetDisaster();

		resetSTR();
	}

}
