package edu.rice.biosim.RL.disaster;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.traclabs.biosim.idl.framework.BioModule;

public class CRSAgent extends Agent {

	public CRSAgent(float nnBase) {
		super();

		List<StateFactory.LevelEnum> myLevelSensors = new LinkedList<StateFactory.LevelEnum>();
		List<ActionFactory.ActionEnum> myActuators = new LinkedList<ActionFactory.ActionEnum>();
		List<StateFactory.StatusEnum> myAils = new LinkedList<StateFactory.StatusEnum>();
		List<Rewards.RewardEnum> myRew = new LinkedList<Rewards.RewardEnum>();

		int numInputs;
		int numOutputs;

		myLevelSensors.add(StateFactory.LevelEnum.POT_H2O);
		myLevelSensors.add(StateFactory.LevelEnum.CO2);
		myLevelSensors.add(StateFactory.LevelEnum.H2);

		myActuators.add(ActionFactory.ActionEnum.POWER);

		myAils.add(StateFactory.StatusEnum.DEAD);
		myAils.add(StateFactory.StatusEnum.SUFFOCATING);
		myAils.add(StateFactory.StatusEnum.POISONED);

		myRew.add(Rewards.RewardEnum.CO2_OVER);
		myRew.add(Rewards.RewardEnum.POT_OVER);
		myRew.add(Rewards.RewardEnum.H2_OVER);
		myRew.add(Rewards.RewardEnum.POWER_USED);
		myRew.add(Rewards.RewardEnum.DEATH);
		myRew.add(Rewards.RewardEnum.SUFFOCATE);
		myRew.add(Rewards.RewardEnum.POISON);

		myRewards.setRewardsList(myRew);

		myBioModule = (BioModule) myBioHolder.theCRSModules.get(0);

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
	public boolean loadPolicy(String directory, String filename) {
		// TODO Auto-generated method stub
		try {
			myPolicy = NeuralNet.readFrom(directory
					+ System.getProperty("file.separator") + "crs_" + filename);
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
	public boolean savePolicy(String directory, String filename) {
		// TODO Auto-generated method stub
		try {
			myPolicy.writeTo(directory + System.getProperty("file.separator")
					+ "crs_" + filename);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean saveHistory(int i) {
		myActions.writeActionHistory("crs", i);
		myStates.writeStateHistory("crs", i);
		return false;
	}

	@Override
	public void saveMyRewards(int seed) {
		saveRewards("crs" + seed);

	}

}
