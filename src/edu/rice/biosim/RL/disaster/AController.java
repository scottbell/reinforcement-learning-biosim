package edu.rice.biosim.RL.disaster;

import com.traclabs.biosim.client.util.BioHolder;
import com.traclabs.biosim.client.util.BioHolderInitializer;
import com.traclabs.biosim.idl.framework.BioDriver;
import com.traclabs.biosim.idl.framework.MalfunctionIntensity;
import com.traclabs.biosim.idl.framework.MalfunctionLength;
import com.traclabs.biosim.idl.simulation.crew.CrewGroup;
import com.traclabs.biosim.idl.simulation.crew.CrewPerson;
import com.traclabs.biosim.idl.simulation.food.BiomassRS;
import com.traclabs.biosim.idl.simulation.food.Shelf;
import com.traclabs.biosim.idl.simulation.framework.Store;
import com.traclabs.biosim.idl.simulation.power.PowerPS;

public class AController {

	protected int MAX_MISSION_TICKS = 90 * 24;

	protected Store myPotWaterStore;

	protected Store myGreyWaterStore;

	protected Store myDirtyWaterStore;

	protected Store myO2Store;

	protected Store myH2Store;

	protected Store myCO2Store;

	protected Store myFoodStore;

	protected Store myWasteStore;

	protected Store myBiomassStore;

	protected CrewGroup myCrew;

	protected SingleTrialResults myStr;

	/**
	 * @param myPowerStore
	 *            The PowerStore that will malfunction
	 */
	protected Store myPowerStore;

	/**
	 * @param myBioHolder
	 *            The BioHolder for the current BioSim simulation.
	 */
	protected BioHolder myBioHolder;

	/**
	 * @param myBioDriver
	 *            The BioDriver associated with myBioHolder
	 */
	protected BioDriver myBioDriver;

	protected float incO2Over;

	protected float incCO2Over;

	protected float incH2Over;

	protected float incFoodOver;

	protected float incPowerOver;

	protected float incPotH2OOver;

	protected float incDirtyH2OOver;

	protected float incGreyH2OOver;

	protected float incWasteOver;

	protected float incBiomassOver;

	protected float incLastLevel;

	protected boolean incThirst;

	protected boolean incStarve;

	protected boolean incSick;

	protected boolean incDead;

	protected boolean incSuffocate;

	protected boolean incPoison;

	protected PowerPS myPowerPS;

	protected BiomassRS myBiomassRS;

	protected int myDayOfDoom;

	private String myStrFile;

	protected int incDeadPlants;

	private boolean myCauseFailure;

	public AController() {

		myBioHolder = BioHolderInitializer.getBioHolder();
		myBioDriver = myBioHolder.theBioDriver;

		myPowerStore = (Store) myBioHolder.thePowerStores.get(0);
		myPotWaterStore = (Store) myBioHolder.thePotableWaterStores.get(0);
		myGreyWaterStore = (Store) myBioHolder.theGreyWaterStores.get(0);
		myDirtyWaterStore = (Store) myBioHolder.theDirtyWaterStores.get(0);
		myO2Store = (Store) myBioHolder.theO2Stores.get(0);
		myCO2Store = (Store) myBioHolder.theCO2Stores.get(0);
		myH2Store = (Store) myBioHolder.theH2Stores.get(0);
		myFoodStore = (Store) myBioHolder.theFoodStores.get(0);
		myBiomassStore = (Store) myBioHolder.theBiomassStores.get(0);
		myPowerPS = (PowerPS) myBioHolder.thePowerPSModules.get(0);
		myCrew = (CrewGroup) myBioHolder.theCrewGroups.get(0);
		myBiomassRS = (BiomassRS) myBioHolder.theBiomassRSModules.get(0);
		myWasteStore = (Store) myBioHolder.theDryWasteStores.get(0);
		myStr = new SingleTrialResults();

		incO2Over = 0;
		incCO2Over = 0;
		incH2Over = 0;
		incFoodOver = 0;
		incPowerOver = 0;
		incPotH2OOver = 0;
		incDirtyH2OOver = 0;
		incGreyH2OOver = 0;
		incWasteOver = 0;
		incBiomassOver = 0;
		incDeadPlants = 0;
		incLastLevel = myPowerStore.getCurrentLevel();

		incThirst = false;
		incStarve = false;
		incSick = false;
		incDead = false;
		incSuffocate = false;
		incPoison = false;
	}

	/**
	 * Records interesting data regarding controller performance
	 */
	protected void recordSituation() {

		int dead = 0;
		for (Shelf s : myBiomassRS.getShelves()) {
			if (s.isDead()) {
				dead++;
			}
		}

		incDeadPlants = dead;

		myStr.setNumPotOver(myPotWaterStore.getOverflow());
		myStr.setNumGreyOver(myGreyWaterStore.getOverflow());
		myStr.setNumDirtyOver(myDirtyWaterStore.getOverflow());
		myStr.setNumH2Over(myH2Store.getOverflow());
		myStr.setNumCO2Over(myCO2Store.getOverflow());
		myStr.setNumO2Over(myO2Store.getOverflow());
		myStr.setNumFoodOver(myFoodStore.getOverflow());
		myStr.setNumPowerOver(myPowerStore.getOverflow());
		myStr.setNumBiomassOver(myBiomassStore.getOverflow());
		myStr.setNumWasteOver(myWasteStore.getOverflow());
		myStr.setNumPlantsDead(myStr.getNumPlantsDead() + incDeadPlants);

		float currentPowerUsed;

		currentPowerUsed = myPowerPS.getPowerProduced()
				- (myPowerStore.getCurrentLevel() - incLastLevel);
		incLastLevel = myPowerStore.getCurrentLevel();
		myStr.setTotalPowerUsage(myStr.getTotalPowerUsage() + currentPowerUsed);

	}

	protected void recordFinalSituation() {

		recordSituation();

		myStr.setNumTicks(myBioDriver.getTicks());

		// totalPowerProduced + initialLevel = all power available
		// powerInStore + powerOverflow = power not used
		// powerAvailable - powerNotUsed = totalPowerUsage
		myStr.setTotalPowerUsage(myStr.getTotalPowerUsage()
				+ myPowerStore.getInitialLevel()
				- myPowerStore.getCurrentLevel() - myPowerStore.getOverflow());

		myStr.setLevelCO2(myCO2Store.getCurrentLevel());
		myStr.setLevelDirtyWater(myDirtyWaterStore.getCurrentLevel());
		myStr.setLevelFood(myFoodStore.getCurrentLevel());
		myStr.setLevelGreyWater(myGreyWaterStore.getCurrentLevel());
		myStr.setLevelH2(myH2Store.getCurrentLevel());
		myStr.setLevelO2(myO2Store.getCurrentLevel());
		myStr.setLevelPotWater(myPotWaterStore.getCurrentLevel());
		myStr.setLevelPower(myPowerStore.getCurrentLevel());
		myStr.setLevelWaste(myWasteStore.getCurrentLevel());
		myStr.setLevelBiomass(myBiomassStore.getCurrentLevel());

	}

	protected void recordIncrementalStatus() {

		int dead = 0;

		incO2Over = myO2Store.getOverflow() - myStr.getNumO2Over();
		incCO2Over = myCO2Store.getOverflow() - myStr.getNumCO2Over();
		incH2Over = myH2Store.getOverflow() - myStr.getNumH2Over();
		incFoodOver = myFoodStore.getOverflow() - myStr.getNumFoodOver();
		incPowerOver = myPowerStore.getOverflow() - myStr.getNumPowerOver();
		incPotH2OOver = myPotWaterStore.getOverflow() - myStr.getNumPotOver();
		incDirtyH2OOver = myDirtyWaterStore.getOverflow()
				- myStr.getNumDirtyOver();
		incGreyH2OOver = myGreyWaterStore.getOverflow()
				- myStr.getNumGreyOver();
		incBiomassOver = myBiomassStore.getOverflow()
				- myStr.getNumBiomassOver();
		incWasteOver = myWasteStore.getOverflow() - myStr.getNumWasteOver();

		recordSituation();
		recordCrewStatus();
	}

	protected void recordCrewStatus() {

		incThirst = false;
		incStarve = false;
		incSick = false;
		incDead = false;
		incSuffocate = false;
		incPoison = false;

		for (CrewPerson bob : myCrew.getCrewPeople()) {
			if (bob.isDead()) {
				incDead = incDead || bob.isDead();
				myStr.incrementCrewDead();
			}
			if (bob.isPoisoned()) {
				incPoison = incPoison || bob.isPoisoned();
				myStr.incrementNumCrewPoison();
			}
			if (bob.isSick()) {
				incSick = incSick || bob.isSick();
				myStr.incrementNumCrewSick();
			}
			if (bob.isStarving()) {
				incStarve = incStarve || bob.isStarving();
				myStr.incrementNumCrewStarve();
			}
			if (bob.isSuffocating() && bob.isOnBoard()) {
				incSuffocate = incSuffocate || bob.isSuffocating();
				myStr.incrementNumCrewSuffocate();
			}
			if (bob.isThirsty()) {
				incThirst = incThirst || bob.isThirsty();
				myStr.incrementNumCrewThirst();
			}
		}

		// myStr.setTotalPowerUsage(myStr.getTotalPowerUsage()+
		// myPowerPS.getPowerProduced());
	}

	public SingleTrialResults getStr() {
		return myStr;
	}

	public void setStr(SingleTrialResults str) {
		this.myStr = str;
	}

	public void setStrFile(String s) {
		myStrFile = s;
	}

	public void writeStrFile() {

		myStr.writeToFile(myStrFile);
	}

	public void changeMalfunction(boolean fail, int ticks) {

		myDayOfDoom = ticks;
		myCauseFailure = fail;
		if (fail) {
			myPowerStore.scheduleMalfunction(MalfunctionIntensity.SEVERE_MALF,
					MalfunctionLength.PERMANENT_MALF, ticks);
		} else {
			myPowerStore.clearAllMalfunctions();
		}
	}

	public void resetController() {

		myBioHolder.theBioDriver.reset();
		resetDisaster();

		resetSTR();
	}

	protected void resetDisaster() {
		if (myCauseFailure) {
			myPowerStore.clearAllMalfunctions();
			myPowerStore.scheduleMalfunction(MalfunctionIntensity.LOW_MALF,
					MalfunctionLength.PERMANENT_MALF, myDayOfDoom);
		} else {
			myPowerStore.clearAllMalfunctions();
		}
	}

	public void resetSTR() {
		myStr = new SingleTrialResults();

		incO2Over = 0;
		incCO2Over = 0;
		incH2Over = 0;
		incFoodOver = 0;
		incPowerOver = 0;
		incPotH2OOver = 0;
		incDirtyH2OOver = 0;
		incGreyH2OOver = 0;
		incWasteOver = 0;
		incBiomassOver = 0;
		incDeadPlants = 0;
		incLastLevel = myPowerStore.getCurrentLevel();
		myStr.setDisasterTimeTick(myDayOfDoom);
	}

}
