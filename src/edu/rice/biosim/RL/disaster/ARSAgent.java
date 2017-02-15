/*
 * Created on Mar 5, 2005
 *
 * Filename: ARSAgent.java
 */
package edu.rice.biosim.RL.disaster;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.traclabs.biosim.idl.framework.BioModule;

/**
 * @author Travis R. Fischer
 * @since Mar 5, 2005
 * @version 1.0
 * 
 */
public class ARSAgent extends Agent {

	int VCCR_ACTIONS;

	int OGS_ACTIONS;

	int CRS_ACTIONS;

	StateHistory myVCCRState;

	StateHistory myOGSState;

	StateHistory myCRSState;

	ActionHistory myVCCRActions;

	ActionHistory myOGSActions;

	ActionHistory myCRSActions;

	public ARSAgent(float nnBase) {
		super();

		List<StateFactory.LevelEnum> myVCCRStoreSensors = new LinkedList<StateFactory.LevelEnum>();
		List<ActionFactory.ActionEnum> myVCCRActuators = new LinkedList<ActionFactory.ActionEnum>();

		List<StateFactory.LevelEnum> myCRSStoreSensors = new LinkedList<StateFactory.LevelEnum>();
		List<ActionFactory.ActionEnum> myCRSActuators = new LinkedList<ActionFactory.ActionEnum>();

		List<StateFactory.LevelEnum> myOGSStoreSensors = new LinkedList<StateFactory.LevelEnum>();
		List<ActionFactory.ActionEnum> myOGSActuators = new LinkedList<ActionFactory.ActionEnum>();

		int numInputs;
		int numOutputs;

		myVCCRStoreSensors.add(StateFactory.LevelEnum.CO2);
		myVCCRActuators.add(ActionFactory.ActionEnum.POWER);
		VCCR_ACTIONS = myVCCRActuators.size();

		myCRSStoreSensors.add(StateFactory.LevelEnum.POT_H2O);
		myCRSStoreSensors.add(StateFactory.LevelEnum.CO2);
		myCRSStoreSensors.add(StateFactory.LevelEnum.H2);

		myCRSActuators.add(ActionFactory.ActionEnum.POWER);

		CRS_ACTIONS = myCRSActuators.size();

		myOGSStoreSensors.add(StateFactory.LevelEnum.O2);
		myOGSStoreSensors.add(StateFactory.LevelEnum.H2);
		myOGSStoreSensors.add(StateFactory.LevelEnum.POT_H2O);

		myOGSActuators.add(ActionFactory.ActionEnum.POWER);

		OGS_ACTIONS = myOGSActuators.size();

		BioModule myVCCR = (BioModule) myBioHolder.theVCCRModules.get(0);
		BioModule myOGS = (BioModule) myBioHolder.theOGSModules.get(0);
		BioModule myCRS = (BioModule) myBioHolder.theCRSModules.get(0);

		myVCCRState = StateFactory.Singleton
				.createStateHistory(myVCCRStoreSensors);
		myVCCRActions = ActionFactory.Singleton.createActionHistory(myVCCR,
				myVCCRActuators);

		myCRSState = StateFactory.Singleton
				.createStateHistory(myCRSStoreSensors);
		myCRSActions = ActionFactory.Singleton.createActionHistory(myCRS,
				myCRSActuators);

		myOGSState = StateFactory.Singleton
				.createStateHistory(myOGSStoreSensors);
		myOGSActions = ActionFactory.Singleton.createActionHistory(myOGS,
				myOGSActuators);

		numInputs = myOGSStoreSensors.size() + myCRSStoreSensors.size()
				+ myVCCRStoreSensors.size() + 6;
		numOutputs = myVCCRActuators.size() + myOGSActuators.size()
				+ myCRSActuators.size();

		myPolicy = new NeuralNet(numInputs, numInputs + numOutputs - 2,
				numOutputs, nnBase);
	}

	@Override
	public boolean savePolicy(String directory, String filename) {
		// TODO Auto-generated method stub

		try {
			myPolicy.writeTo(directory + System.getProperty("file.separator")
					+ "ars_" + filename);
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
					+ System.getProperty("file.separator") + "ars_" + filename);
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

	public void calcAction() {
		State curVCCRState = myVCCRState.getCurrentState();
		State curCRSState = myCRSState.getCurrentState();
		State curOGSState = myOGSState.getCurrentState();
		int it = 0;

		Vector<Float> myInputs = new Vector<Float>();

		Collection<Float> myVCCRLevels = curVCCRState.getLevelValues().values();
		Collection<Float> myCRSLevels = curCRSState.getLevelValues().values();
		Collection<Float> myOGSLevels = curOGSState.getLevelValues().values();

		Collection<Boolean> myStatuses = curVCCRState.getAllCrewStatus()
				.values();

		Vector<Float> myActionSet;

		// System.out.println("myVCCRLevel size: " + myVCCRLevels.size());

		for (Map.Entry<StateFactory.LevelEnum, Float> e : curVCCRState
				.getLevelValues().entrySet()) {

			myInputs.add(e.getValue()
					/ StateFactory.getStore(e.getKey()).getInitialCapacity());

		}

		// System.out.println("myCRSLevels: " + myCRSLevels.size());
		for (Map.Entry<StateFactory.LevelEnum, Float> e : curCRSState
				.getLevelValues().entrySet()) {

			myInputs.add(e.getValue()
					/ StateFactory.getStore(e.getKey()).getInitialCapacity());

		}

		for (Map.Entry<StateFactory.LevelEnum, Float> e : curOGSState
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

		// System.out.println("Input size: " + myInputs.size());
		// System.out.println("Inputs: " + myInputs.toString());

		myActionSet = myPolicy.getValue(myInputs);

		// System.out.println("Set actions for VCCR");
		myVCCRActions.addAllActions(myBioHolder.theBioDriver.getTicks(),
				myActionSet.subList(0, VCCR_ACTIONS));
		// System.out.println("Set actions for CRS");
		myCRSActions.addAllActions(myBioHolder.theBioDriver.getTicks(),
				myActionSet.subList(VCCR_ACTIONS, VCCR_ACTIONS + CRS_ACTIONS));
		// System.out.println("Set actions for OGS");
		myOGSActions.addAllActions(myBioHolder.theBioDriver.getTicks(),
				myActionSet.subList(VCCR_ACTIONS + CRS_ACTIONS, myActionSet
						.size()));

		myVCCRActions.setAction(myBioHolder.theBioDriver.getTicks());
		myCRSActions.setAction(myBioHolder.theBioDriver.getTicks());
		myOGSActions.setAction(myBioHolder.theBioDriver.getTicks());

	}

	@Override
	public boolean saveHistory(int i) {
		myVCCRState.writeStateHistory("vccr", i);
		myVCCRActions.writeActionHistory("vccr", i);
		myOGSState.writeStateHistory("ogs", i);
		myOGSActions.writeActionHistory("ogs", i);
		myCRSState.writeStateHistory("crs", i);
		myCRSActions.writeActionHistory("crs", i);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rice.biosim.RL.disaster.Agent#getCurrentPowerUsage()
	 */
	public float getCurrentPowerUsage() {
		return myVCCRActions.getAction(myBioHolder.theBioDriver.getTicks() - 1)
				.getActionValue(ActionFactory.ActionEnum.POWER)
				+ myCRSActions.getAction(
						myBioHolder.theBioDriver.getTicks() - 1)
						.getActionValue(ActionFactory.ActionEnum.POWER)
				+ myOGSActions.getAction(
						myBioHolder.theBioDriver.getTicks() - 1)
						.getActionValue(ActionFactory.ActionEnum.POWER);
	}

	public void saveMyRewards(int seed) {
		saveRewards("ars" + seed);
	}

	@Override
	public void resetAgent() {
		myRewards.resetRewards();
		myVCCRState.resetStateHistory();
		myCRSState.resetStateHistory();
		myOGSState.resetStateHistory();

		myVCCRActions.resetActionHistory();
		myOGSActions.resetActionHistory();
		myCRSActions.resetActionHistory();
	}

}
