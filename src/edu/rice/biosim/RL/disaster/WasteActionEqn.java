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
public class WasteActionEqn extends AActionEqn {

	int POWER = 0;

	int O2_LEVEL = 0;

	int MYPRODUCTIONRATE = 1;

	int CO2 = 2;

	int CO2_CAP = 3;

	int CO2_OVER = 4;

	int WASTE = 5;

	public static AActionEqn Singleton = new WasteActionEqn();

	private WasteActionEqn() {
		ACTION_SIZE = 1;
		STATE_SIZE = 6;
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

		if (actions.get(POWER) >= 100) {

			float o2Used = Math.min(10f, state.get(O2_LEVEL));
			float co2Made = o2Used * state.get(MYPRODUCTIONRATE);

			newState.add(O2_LEVEL, state.get(O2_LEVEL) - o2Used);
			newState.add(CO2, Math.min(state.get(CO2) + co2Made, state
					.get(CO2_CAP)));
			newState
					.add(CO2_OVER, state.get(CO2) + co2Made - newState.get(CO2));
			newState.add(WASTE, Math.max(state.get(WASTE) - 10f, 0f));
			newState.add(MYPRODUCTIONRATE, state.get(MYPRODUCTIONRATE));
			newState.add(CO2_CAP, state.get(CO2_CAP));
		} else {
			newState = (Vector<Float>) state.clone();
		}

		return newState;
	}

}
