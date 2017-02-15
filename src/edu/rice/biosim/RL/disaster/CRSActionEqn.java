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
public class CRSActionEqn extends AActionEqn {

	int POWER = 0;

	int CO2 = 0;

	int H2 = 1;

	int WATER = 2;

	int WATER_CAP = 3;

	int WATER_OVER = 4;

	public static AActionEqn Singleton = new CRSActionEqn();

	private CRSActionEqn() {
		ACTION_SIZE = 1;

		STATE_SIZE = 5;
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

		float co2Needed = actions.get(POWER) * 100;
		float h2Needed = co2Needed * 4;
		float co2Used;
		float h2Used;
		float waterMade = 0f;

		if (state.get(CO2) >= co2Needed) {
			if (state.get(H2) >= h2Needed) {

				co2Used = co2Needed;
				h2Used = h2Needed;

			} else {
				// enough co2, not enough h2

				h2Used = state.get(H2);
				co2Used = h2Used / 4f;

			}
		} else if (state.get(H2) >= h2Needed) {
			co2Used = state.get(CO2);
			h2Used = co2Used * 4f;

		} else {
			co2Used = Math.min(state.get(CO2), state.get(H2) / 4f);
			h2Used = co2Used * 4f;
		}

		waterMade = 2 * co2Used;

		newState.add(CO2, state.get(CO2) - co2Used);
		newState.add(H2, state.get(H2) - h2Used);
		newState.add(WATER, Math.min(state.get(WATER) + waterMade, state
				.get(WATER_CAP)));
		newState.add(WATER_OVER, state.get(WATER) + waterMade
				- newState.get(WATER));
		newState.add(WATER_CAP, state.get(WATER_CAP));

		return newState;
	}

}
