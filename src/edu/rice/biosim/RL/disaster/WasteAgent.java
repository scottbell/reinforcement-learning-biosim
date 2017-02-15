/*
 * Created on Apr 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.traclabs.biosim.idl.framework.BioModule;

/**
 * @author Travis R. Fischer
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class WasteAgent extends Agent {

	public WasteAgent(float nnBase) {
		super();

		List<StateFactory.LevelEnum> myLevelSensors = new LinkedList<StateFactory.LevelEnum>();
		List<ActionFactory.ActionEnum> myActuators = new LinkedList<ActionFactory.ActionEnum>();
		int numInputs;
		int numOutputs;

		myActuators.add(ActionFactory.ActionEnum.POWER);

		myLevelSensors.add(StateFactory.LevelEnum.WASTE);
		myLevelSensors.add(StateFactory.LevelEnum.CO2);
		myLevelSensors.add(StateFactory.LevelEnum.O2);

		myBioModule = (BioModule) myBioHolder.theIncinerators.get(0);

		myStates = StateFactory.Singleton.createStateHistory(myLevelSensors);

		myActions = ActionFactory.Singleton.createActionHistory(myBioModule,
				myActuators);

		numInputs = myLevelSensors.size() * 2 + 6;
		numOutputs = myActuators.size();

		myPolicy = new NeuralNet(numInputs, numInputs + numOutputs - 2,
				numOutputs, nnBase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rice.biosim.RL.disaster.Agent#loadPolicy(java.lang.String,
	 *      java.lang.String)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rice.biosim.RL.disaster.Agent#savePolicy(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean savePolicy(String directory, String filename) {
		try {
			myPolicy.writeTo(directory + System.getProperty("file.separator")
					+ "waste_" + filename);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveHistory(int i) {
		myActions.writeActionHistory("waste", i);
		myStates.writeStateHistory("waste", i);
		return true;
	}

	public void saveMyRewards(int seed) {
		saveRewards("waste" + seed);
	}
}
