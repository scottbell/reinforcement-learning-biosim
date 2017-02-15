/*
 * Created on Mar 5, 2005
 *
 * Filename: WRSAgent.java
 */
package edu.rice.biosim.RL.disaster;

import com.traclabs.biosim.idl.framework.BioModule;

import java.io.IOException;
import java.util.*;

/**
 * @author Travis R. Fischer
 * @since Mar 5, 2005
 * @version 1.0
 * 
 */
public class WRSAgent extends Agent {

	public WRSAgent(float nnBase) {
		super();

		List<StateFactory.LevelEnum> myLevelSensors = new LinkedList<StateFactory.LevelEnum>();
		List<ActionFactory.ActionEnum> myActuators = new LinkedList<ActionFactory.ActionEnum>();
		List<StateFactory.StatusEnum> myAils = new LinkedList<StateFactory.StatusEnum>();

		int numInputs;
		int numOutputs;

		myLevelSensors.add(StateFactory.LevelEnum.POT_H2O);
		myLevelSensors.add(StateFactory.LevelEnum.GREY_H2O);
		myLevelSensors.add(StateFactory.LevelEnum.DIRTY_H2O);

		myAils.add(StateFactory.StatusEnum.DEAD);
		myAils.add(StateFactory.StatusEnum.THIRSTY);

		myActuators.add(ActionFactory.ActionEnum.POWER);

		myBioModule = (BioModule) myBioHolder.theWaterRSModules.get(0);

		myStates = StateFactory.Singleton.createStateHistory(myLevelSensors,
				myAils);

		myActions = ActionFactory.Singleton.createActionHistory(myBioModule,
				myActuators);

		numInputs = myLevelSensors.size() + myAils.size();
		numOutputs = myActuators.size();

		myPolicy = new NeuralNet(numInputs, numInputs + numOutputs - 2,
				numOutputs, nnBase);
	}

	@Override
	public boolean savePolicy(String directory, String filename) {
		try {
			myPolicy.writeTo(directory + System.getProperty("file.separator")
					+ "wrs_" + filename);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean loadPolicy(String directory, String filename) {

		try {
			myPolicy = NeuralNet.readFrom(directory
					+ System.getProperty("file.separator") + "wrs_" + filename);
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
		myActions.writeActionHistory("wrs", i);
		myStates.writeStateHistory("wrs", i);
		return false;
	}

	public void saveMyRewards(int seed) {
		saveRewards("wrs" + seed);
	}
}
