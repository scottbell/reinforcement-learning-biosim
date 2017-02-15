/*
 * Created on Apr 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

import java.util.Vector;

/**
 * @author Travis R. Fischer
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PlantActionEqn extends AActionEqn {

	public static AActionEqn Singleton = new PlantActionEqn();

	private int POWER = 0;

	private int H2O = 0;

	private int PLANTAREA = 1;

	private int WATEROFF = 2;

	private int POWEROFF = 3;

	private float WATERPERSQUAREMETER = 50f;

	private float POWERPERSQUAREMETER = 1520f;

	private PlantActionEqn() {

		STATE_SIZE = 4;
		ACTION_SIZE = 1;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rice.biosim.RL.disaster.AActionEqn#calcStateChange(java.util.Vector,
	 *      java.util.Vector)
	 */
	@Override
	public Vector<Float> calcStateChange(Vector<Float> state,
			Vector<Float> actions, Object inp) {

		Vector<Float> newState = new Vector<Float>();

		float powerNeeded = 0f;
		float waterNeeded = 0f;
		float waterUsed = 0f;
		float biomassHarvested = 0f;

		if (actions.size() != ACTION_SIZE) {
			throw new IllegalArgumentException(
					"Action vector wrong arguments. Expected: " + ACTION_SIZE
							+ " received: " + actions.size());
		}

		if (state.size() != STATE_SIZE) {
			throw new IllegalArgumentException(
					"State vector wrong arguments. Expected: " + STATE_SIZE
							+ " received: " + state.size());
		}

		powerNeeded = state.get(PLANTAREA) * POWERPERSQUAREMETER;
		waterNeeded = state.get(PLANTAREA) * WATERPERSQUAREMETER;

		waterUsed = Math.min(state.get(H2O), waterNeeded);

		newState.add(H2O, state.get(H2O) - waterUsed);
		newState.add(WATEROFF, waterNeeded - waterUsed);
		newState.add(POWEROFF, powerNeeded - actions.get(POWER));
		newState.add(PLANTAREA, state.get(PLANTAREA));

		return newState;

	}

}
