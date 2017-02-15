/**
 * 
 */
package edu.rice.biosim.RL.disaster;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.traclabs.biosim.idl.framework.BioModule;

/**
 * @author travis
 * 
 */
public class OGSAgent extends Agent {

	public OGSAgent(float nnBase) {

		super();

		List<StateFactory.LevelEnum> myLevelSensors = new LinkedList<StateFactory.LevelEnum>();
		List<ActionFactory.ActionEnum> myActuators = new LinkedList<ActionFactory.ActionEnum>();
		List<StateFactory.StatusEnum> myAils = new LinkedList<StateFactory.StatusEnum>();
		List<Rewards.RewardEnum> myRew = new LinkedList<Rewards.RewardEnum>();

		int numInputs;
		int numOutputs;

		myLevelSensors.add(StateFactory.LevelEnum.O2);
		myLevelSensors.add(StateFactory.LevelEnum.H2);
		myLevelSensors.add(StateFactory.LevelEnum.POT_H2O);

		myActuators.add(ActionFactory.ActionEnum.POWER);

		myAils.add(StateFactory.StatusEnum.DEAD);
		myAils.add(StateFactory.StatusEnum.SUFFOCATING);
		myAils.add(StateFactory.StatusEnum.POISONED);

		myRew.add(Rewards.RewardEnum.O2_OVER);
		myRew.add(Rewards.RewardEnum.POT_OVER);
		myRew.add(Rewards.RewardEnum.H2_OVER);
		myRew.add(Rewards.RewardEnum.POWER_USED);
		myRew.add(Rewards.RewardEnum.DEATH);
		myRew.add(Rewards.RewardEnum.SUFFOCATE);
		myRew.add(Rewards.RewardEnum.POISON);

		myRewards.setRewardsList(myRew);

		myBioModule = (BioModule) myBioHolder.theOGSModules.get(0);

		myStates = StateFactory.Singleton.createStateHistory(myLevelSensors,
				myAils);
		myActions = ActionFactory.Singleton.createActionHistory(myBioModule,
				myActuators);

		numInputs = myLevelSensors.size() + myAils.size();
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
					+ System.getProperty("file.separator") + "ogs_" + filename);
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
					+ "ogs_" + filename);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rice.biosim.RL.disaster.Agent#saveHistory(int)
	 */
	@Override
	public boolean saveHistory(int i) {
		myActions.writeActionHistory("ogs", i);
		myStates.writeStateHistory("ogs", i);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rice.biosim.RL.disaster.Agent#saveMyRewards(int)
	 */
	@Override
	public void saveMyRewards(int seed) {
		saveRewards("ogs" + seed);

	}

}
