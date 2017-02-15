/*
 * Created on Mar 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.traclabs.biosim.client.util.*;
import com.traclabs.biosim.idl.sensor.framework.GenericSensor;
import com.traclabs.biosim.idl.simulation.air.CO2Store;
import com.traclabs.biosim.idl.simulation.air.H2Store;
import com.traclabs.biosim.idl.simulation.air.NitrogenStore;
import com.traclabs.biosim.idl.simulation.air.O2Store;
import com.traclabs.biosim.idl.simulation.crew.CrewGroup;
import com.traclabs.biosim.idl.simulation.crew.CrewPerson;
import com.traclabs.biosim.idl.simulation.food.BiomassStore;
import com.traclabs.biosim.idl.simulation.food.FoodStore;
import com.traclabs.biosim.idl.simulation.power.PowerStore;
import com.traclabs.biosim.idl.simulation.waste.DryWasteStore;
import com.traclabs.biosim.idl.simulation.water.DirtyWaterStore;
import com.traclabs.biosim.idl.simulation.water.GreyWaterStore;
import com.traclabs.biosim.idl.simulation.water.PotableWaterStore;

import edu.rice.biosim.RL.disaster.StateFactory.*;

/**
 * @author Travis R. Fischer
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class StateHistory {

	/**
	 * @param myStateHistory -
	 *            Contains a sequence of integers representing mission time
	 *            ticks mapped to States
	 */
	private Map<Integer, State> myStateHistory;

	/**
	 * @param myAilments -
	 *            List of enumerated tags that indicate crew status ailments
	 *            tracked by this StateHistory
	 */
	private List<StatusEnum> myAilments;

	/**
	 * @param myBioHolder -
	 *            BioHolder associated with this BioSim simulation
	 */
	private BioHolder myBioHolder;

	private List<LevelEnum> myStoreSensors;

	private boolean isFlowSensors;

	private boolean isStoreSensors;

	/**
	 * Default Constructor
	 */
	public StateHistory() {
		myBioHolder = BioHolderInitializer.getBioHolder();
		myStateHistory = new TreeMap<Integer, State>();
	}

	public StateHistory(List<LevelEnum> theStoreSensors,
			List<StatusEnum> theAilments) {

		myBioHolder = BioHolderInitializer.getBioHolder();
		myStateHistory = new TreeMap<Integer, State>();
		isFlowSensors = false;
		isStoreSensors = true;
		myAilments = theAilments;
		myStoreSensors = theStoreSensors;
	}

	public StateHistory(List<LevelEnum> myStoreSensors2) {
		myBioHolder = BioHolderInitializer.getBioHolder();

		myStateHistory = new TreeMap<Integer, State>();
		isFlowSensors = false;
		isStoreSensors = true;
		setDefaultAilments();

		myStoreSensors = myStoreSensors2;

	}

	private void setDefaultAilments() {
		myAilments = new LinkedList<StatusEnum>();

		myAilments.add(StateFactory.StatusEnum.THIRSTY);
		myAilments.add(StateFactory.StatusEnum.DEAD);
		myAilments.add(StateFactory.StatusEnum.POISONED);
		myAilments.add(StateFactory.StatusEnum.SICK);
		myAilments.add(StateFactory.StatusEnum.STARVING);
		myAilments.add(StateFactory.StatusEnum.SUFFOCATING);
	}

	/**
	 * @return Current state of the BioSim simulation
	 */
	public State getCurrentState() {

		State curState = new State();

		StatusEnum tempStatus;

		curState.setMissionTime(myBioHolder.theBioDriver.getTicks());

		if (!myStateHistory.containsKey(curState.getMissionTime())) {

			for (StatusEnum j : myAilments) {

				curState.addCrewStatus(j, checkAilment(j));

			}

			if (isStoreSensors) {
				for (LevelEnum i : myStoreSensors) {
					curState.addLevelValue(i, checkStore(i));
				}
			}

			myStateHistory.put(curState.getMissionTime(), curState);

			return curState;
		}
		return myStateHistory.get(curState.getMissionTime());

	}

	/**
	 * @param missionTime
	 *            Mission tick for the concerned state
	 * @return state from missionTime
	 */
	public State getPreviousState(int missionTime) {

		if (myStateHistory.containsKey(missionTime)) {
			return myStateHistory.get(missionTime);
		}
		throw new IllegalArgumentException("Time step " + missionTime
				+ " has not occurred yet.");
	}

	/**
	 * @param crewStatus
	 *            Particular status ailment concerned with
	 * @return True if status ailment is affecting crew
	 */
	private boolean checkAilment(StatusEnum crewStatus) {

		CrewGroup myCrewGroup = (CrewGroup) myBioHolder.theCrewGroups.get(0);

		switch (crewStatus) {
		case DEAD:
			return myCrewGroup.anyDead();

		default:
			for (int i = 0; i < myCrewGroup.getCrewSize(); i++) {

				CrewPerson bob = myCrewGroup.getCrewPeople()[i];

				switch (crewStatus) {
				case STARVING:
					return bob.isStarving();
				case THIRSTY:
					return bob.isThirsty();
				case SUFFOCATING:
					if (bob.isSuffocating())
						return !bob.isOnBoard();
				case SICK:
					return bob.isSick();
				case POISONED:
					return bob.isPoisoned();
				default:
					break;
				}

			}
			return false;

		}

	}

	private Float checkStore(LevelEnum theStore) {

		GenericSensor myLevelSensor = null;
		Vector<Float> temp = new Vector<Float>();

		CO2Store co2Store = (CO2Store) myBioHolder.theCO2Stores.get(0);
		O2Store o2Store = (O2Store) myBioHolder.theO2Stores.get(0);
		H2Store h2Store = (H2Store) myBioHolder.theH2Stores.get(0);
		NitrogenStore nStore = (NitrogenStore) myBioHolder.theNitrogenStores
				.get(0);
		FoodStore foodStore = (FoodStore) myBioHolder.theFoodStores.get(0);
		BiomassStore bioStore = (BiomassStore) myBioHolder.theBiomassStores
				.get(0);
		PotableWaterStore potStore = (PotableWaterStore) myBioHolder.thePotableWaterStores
				.get(0);
		GreyWaterStore greyStore = (GreyWaterStore) myBioHolder.theGreyWaterStores
				.get(0);
		DirtyWaterStore dirtyStore = (DirtyWaterStore) myBioHolder.theDirtyWaterStores
				.get(0);
		PowerStore powerStore = (PowerStore) myBioHolder.thePowerStores.get(0);
		DryWasteStore wasteStore = (DryWasteStore) myBioHolder.theDryWasteStores
				.get(0);

		switch (theStore) {
		case BIOMASS:
			myLevelSensor = myBioHolder.getSensorAttachedTo(
					myBioHolder.theBiomassStoreLevelSensors, bioStore);
			break;
		case POWER:
			myLevelSensor = myBioHolder.getSensorAttachedTo(
					myBioHolder.thePowerStoreLevelSensors, powerStore);
			break;
		case POT_H2O:
			myLevelSensor = myBioHolder.getSensorAttachedTo(
					myBioHolder.thePotableWaterStoreLevelSensors, potStore);
			break;
		case GREY_H2O:
			myLevelSensor = myBioHolder.getSensorAttachedTo(
					myBioHolder.theGreyWaterStoreLevelSensors, greyStore);
			break;
		case DIRTY_H2O:
			myLevelSensor = myBioHolder.getSensorAttachedTo(
					myBioHolder.theDirtyWaterStoreLevelSensors, dirtyStore);
			break;
		case H2:
			myLevelSensor = myBioHolder.getSensorAttachedTo(
					myBioHolder.theH2StoreLevelSensors, h2Store);
			break;
		case O2:
			myLevelSensor = myBioHolder.getSensorAttachedTo(
					myBioHolder.theO2StoreLevelSensors, o2Store);
			break;
		case CO2:
			myLevelSensor = myBioHolder.getSensorAttachedTo(
					myBioHolder.theCO2StoreLevelSensors, co2Store);
			break;
		case N:
			myLevelSensor = myBioHolder.getSensorAttachedTo(
					myBioHolder.theNitrogenStoreLevelSensors, nStore);
			break;
		case FOOD:
			myLevelSensor = myBioHolder.getSensorAttachedTo(
					myBioHolder.theFoodStoreLevelSensors, foodStore);
			break;
		case WASTE:
			myLevelSensor = myBioHolder.getSensorAttachedTo(
					myBioHolder.theDryWasteStoreLevelSensors, wasteStore);
			break;
		}

		temp.add(0, myLevelSensor.getMax());
		temp.add(1, myLevelSensor.getValue());

		return myLevelSensor.getValue();

	}

	public void writeStateHistory(String s, int i) {
		File file = new File(System.getenv("RL_HOME")
				+ System.getProperty("file.separator") + "data"
				+ System.getProperty("file.separator") + s + "state" + i
				+ ".hist");
		try {
			FileWriter filew = new FileWriter(file, true);

			for (Map.Entry<Integer, State> e : myStateHistory.entrySet()) {
				filew.write(e.getKey() + "," + e.getValue().toString() + "\n");
			}

			filew.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void resetStateHistory() {
		myStateHistory = new TreeMap<Integer, State>();
	}

}
