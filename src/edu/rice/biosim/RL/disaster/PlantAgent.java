/*
 * Created on Mar 5, 2005
 *
 * Filename: PlantAgent.java
 */
package edu.rice.biosim.RL.disaster;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.traclabs.biosim.idl.framework.BioModule;

/**
 * @author Travis R. Fischer
 * @since Mar 5, 2005
 * @version 1.0
 * 
 */
public class PlantAgent extends Agent {

	public PlantAgent(float nnBase) {
		super();

		List<StateFactory.LevelEnum> myStoreSensors = new LinkedList<StateFactory.LevelEnum>();
		List<StateFactory.StatusEnum> myAils = new LinkedList<StateFactory.StatusEnum>();
		List<ActionFactory.ActionEnum> myActuators = new LinkedList<ActionFactory.ActionEnum>();
		List<Rewards.RewardEnum> myRew = new LinkedList<Rewards.RewardEnum>();

		int numInput;
		int numOutput;

		myStoreSensors.add(StateFactory.LevelEnum.GREY_H2O);
		myStoreSensors.add(StateFactory.LevelEnum.BIOMASS);

		myAils.add(StateFactory.StatusEnum.DEAD);
		myAils.add(StateFactory.StatusEnum.STARVING);

		myActuators.add(ActionFactory.ActionEnum.POWER);

		myRew.add(Rewards.RewardEnum.GREY_OVER);
		myRew.add(Rewards.RewardEnum.BIOMASS_OVER);
		myRew.add(Rewards.RewardEnum.POWER_USED);
		myRew.add(Rewards.RewardEnum.DEATH);
		myRew.add(Rewards.RewardEnum.STARVE);

		myRewards.setRewardsList(myRew);

		myBioModule = (BioModule) myBioHolder.theBiomassRSModules.get(0);

		myStates = StateFactory.Singleton.createStateHistory(myStoreSensors);

		myActions = ActionFactory.Singleton.createActionHistory(myBioModule,
				myActuators);

		numInput = myStoreSensors.size() * 2 + 6;

		numOutput = myActuators.size();

		myPolicy = new NeuralNet(numInput, numInput + numOutput - 2, numOutput,
				nnBase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rice.biosim.RL.disaster.Agent#strategy(edu.rice.biosim.RL.disaster.Percept)
	 */
	public Action strategy(Percept p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean savePolicy(String directory, String filename) {
		try {
			myPolicy.writeTo(directory + System.getProperty("file.separator")
					+ "plant_" + filename);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean loadPolicy(String directory, String filename) {
		// TODO Auto-generated method stub

		try {
			myPolicy = NeuralNet.readFrom(directory
					+ System.getProperty("file.separator") + "plant_"
					+ filename);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean saveHistory(int i) {
		myStates.writeStateHistory("plant", i);
		myActions.writeActionHistory("plant", i);
		return true;
	}

	public void saveMyRewards(int seed) {
		saveRewards("plant" + seed);
	}

}
