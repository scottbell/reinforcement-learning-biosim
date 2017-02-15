package edu.rice.biosim.RL.disaster;

import java.util.Collection;
import java.util.Map;
import java.util.Vector;

import com.traclabs.biosim.client.util.BioHolder;
import com.traclabs.biosim.client.util.BioHolderInitializer;
import com.traclabs.biosim.idl.framework.BioModule;

public abstract class Agent {

	protected StateHistory myStates;

	protected ActionHistory myActions;

	protected BioModule myBioModule;

	protected Rewards myRewards;

	/**
	 * @param myPolicy
	 *            Policy Pi:S -> A
	 */
	protected NeuralNet myPolicy;

	protected BioHolder myBioHolder;

	public Agent() {
		myBioHolder = BioHolderInitializer.getBioHolder();
		myRewards = new Rewards();
	}

	public void updatePolicy() {
		myPolicy.updateNN(myRewards.setReward());
	}

	public State getCurrentState() {

		return myStates.getCurrentState();
	}

	public float getCurrentPowerUsage() {
		return myActions.getAction(myBioHolder.theBioDriver.getTicks() - 1)
				.getActionValue(ActionFactory.ActionEnum.POWER);
	}

	public void calcAction() {
		State curState = getCurrentState();

		Vector<Float> myInputs = new Vector<Float>();
		Collection<Float> myLevels = curState.getLevelValues().values();
		Collection<Boolean> myStatuses = curState.getAllCrewStatus().values();

		Vector<Float> myActionSet;

		for (Map.Entry<StateFactory.LevelEnum, Float> e : curState
				.getLevelValues().entrySet()) {

			myInputs.add(e.getValue()
					/ StateFactory.getStore(e.getKey()).getInitialCapacity());

		}

		for (Boolean myBool : myStatuses) {
			if (myBool) {
				myInputs.add(1.0f);
			} else {
				myInputs.add(0.0f);
			}
		}

		myActionSet = myPolicy.getValue(myInputs);

		myActions.addAllActions(myBioHolder.theBioDriver.getTicks(),
				myActionSet);

		myActions.setAction(myBioHolder.theBioDriver.getTicks());

	}

	public abstract boolean loadPolicy(String directory, String filename);

	public abstract boolean savePolicy(String directory, String filename);

	public abstract boolean saveHistory(int i);

	public void updateLearningRate() {
		myPolicy.updateAlpha();
	}

	public void saveRewards(String filename) {
		myRewards.saveRewards(filename);

	}

	public abstract void saveMyRewards(int seed);

	public void resetAgent() {
		myRewards.resetRewards();
		myStates.resetStateHistory();
		myActions.resetActionHistory();
	}

	public void calculateIncrementalReward(float amt, Rewards.RewardEnum rew) {
		myRewards.calcRewardNum(amt, rew);
	}

	public void calculateIncrementalReward(boolean bool, Rewards.RewardEnum rew) {
		myRewards.calcRewardBool(bool, rew);
	}
}
