/**
 * 
 */
package edu.rice.biosim.RL.disaster;

import com.traclabs.biosim.idl.actuator.framework.GenericActuator;
import com.traclabs.biosim.idl.framework.*;

/**
 * @author Travis
 * 
 */
public class FixedPlantAgent extends Agent {

	private float myWattage;

	public FixedPlantAgent(float wattage) {
		myWattage = wattage;
	}

	@Override
	public void calcAction() {
		BioModule myBiomassRS = (BioModule) myBioHolder.theBiomassRSModules
				.get(0);
		GenericActuator myPowerInFlow = myBioHolder.getActuatorAttachedTo(
				myBioHolder.thePowerInFlowRateActuators, myBiomassRS);

		myPowerInFlow.setValue(myWattage);
	}

	@Override
	public float getCurrentPowerUsage() {
		return myWattage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rice.biosim.RL.disaster.Agent#loadPolicy(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean loadPolicy(String directory, String filename) {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rice.biosim.RL.disaster.Agent#savePolicy(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean savePolicy(String directory, String filename) {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rice.biosim.RL.disaster.Agent#saveHistory(int)
	 */
	@Override
	public boolean saveHistory(int i) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rice.biosim.RL.disaster.Agent#saveMyRewards(int)
	 */
	@Override
	public void saveMyRewards(int seed) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLearningRate() {
	}

	@Override
	public void saveRewards(String filename) {
	}

	@Override
	public void updatePolicy() {
	}

	@Override
	public void resetAgent() {
	}

	@Override
	public void calculateIncrementalReward(float amt, Rewards.RewardEnum rew) {

	}

	@Override
	public void calculateIncrementalReward(boolean bool, Rewards.RewardEnum rew) {

	}
}
