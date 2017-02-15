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
public class FoodAgent extends Agent {

	public FoodAgent(float baseline) {

		super();

		List<StateFactory.LevelEnum> myLevelSensors = new LinkedList<StateFactory.LevelEnum>();
		List<ActionFactory.ActionEnum> myActuators = new LinkedList<ActionFactory.ActionEnum>();
		List<StateFactory.StatusEnum> myAils = new LinkedList<StateFactory.StatusEnum>();
		List<Rewards.RewardEnum> myRew = new LinkedList<Rewards.RewardEnum>();

		int numInputs;
		int numOutputs;
		myBioModule = (BioModule) myBioHolder.theFoodProcessors.get(0);

		myActuators.add(ActionFactory.ActionEnum.POWER);

		myLevelSensors.add(StateFactory.LevelEnum.BIOMASS);
		myLevelSensors.add(StateFactory.LevelEnum.FOOD);
		myLevelSensors.add(StateFactory.LevelEnum.DIRTY_H2O);
		myLevelSensors.add(StateFactory.LevelEnum.WASTE);

		myAils.add(StateFactory.StatusEnum.STARVING);
		myAils.add(StateFactory.StatusEnum.DEAD);

		myRew.add(Rewards.RewardEnum.BIOMASS_OVER);
		myRew.add(Rewards.RewardEnum.FOOD_OVER);
		myRew.add(Rewards.RewardEnum.DIRTY_OVER);
		myRew.add(Rewards.RewardEnum.POWER_USED);
		myRew.add(Rewards.RewardEnum.DEATH);
		myRew.add(Rewards.RewardEnum.STARVE);

		myRewards.setRewardsList(myRew);

		myStates = StateFactory.Singleton.createStateHistory(myLevelSensors,
				myAils);

		myActions = ActionFactory.Singleton.createActionHistory(myBioModule,
				myActuators);

		numInputs = myLevelSensors.size() + myAils.size();
		numOutputs = myActuators.size();

		myPolicy = new NeuralNet(numInputs, numInputs + numOutputs - 2,
				numOutputs, baseline);
	}

	@Override
	public boolean loadPolicy(String directory, String filename) {
		try {
			myPolicy = NeuralNet
					.readFrom(directory + System.getProperty("file.separator")
							+ "food_" + filename);
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
					+ "food_" + filename);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean saveHistory(int i) {
		myActions.writeActionHistory("food", i);
		myStates.writeStateHistory("food", i);
		return true;
	}

	public void saveMyRewards(int seed) {
		saveRewards("food" + seed);
	}

}
