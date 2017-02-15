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
public class OGSActionEqn extends AActionEqn {

	int POWER = 0;

	int WATER = 0;

	int H2_OVER = 1;

	int H2 = 2;

	int H2_CAP = 3;

	int O2 = 4;

	int O2_CAP = 5;

	int O2_OVER = 6;

	public static AActionEqn Singleton = new OGSActionEqn();

	private OGSActionEqn() {
		ACTION_SIZE = 1;

		STATE_SIZE = 7;
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
		float o2Made;
		float h2Made;

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

		float waterNeeded = actions.get(POWER) / 75f * 0.04167f;

		if (state.get(WATER) >= waterNeeded) {
			newState.add(WATER, state.get(WATER) - waterNeeded);
			o2Made = 0.5f * waterNeeded;
			h2Made = waterNeeded;

		} else {

			newState.add(WATER, 0f);

			o2Made = 0.5f * state.get(WATER);
			h2Made = state.get(WATER);
		}

		newState.add(H2, Math.min(state.get(H2) + h2Made, state.get(H2_CAP)));
		newState.add(H2_OVER, state.get(H2) + h2Made - newState.get(H2));
		newState.add(H2_CAP, state.get(H2_CAP));

		newState.add(O2, Math.min(state.get(O2) + o2Made, state.get(O2_CAP)));
		newState.add(O2_OVER, state.get(O2) + o2Made - newState.get(O2));
		newState.add(O2_CAP, state.get(O2_CAP));

		return newState;
	}

}
