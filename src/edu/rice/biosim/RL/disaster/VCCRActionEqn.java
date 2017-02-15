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
public class VCCRActionEqn extends AActionEqn {

	int POWER = 0;

	int CO2 = 0;

	int CO2_CAP = 1;

	int CO2_OVER = 2;

	int AIR = 3;

	int CO2_CON = 4;

	public static AActionEqn Singleton = new VCCRActionEqn();

	private VCCRActionEqn() {

		STATE_SIZE = 5;
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

		float airNeeded = actions.get(POWER) / 25.625f * 1.2125f;

		float co2Removed = airNeeded * state.get(CO2_CON);

		newState.add(CO2, Math.min(state.get(CO2) + co2Removed, state
				.get(CO2_CAP)));
		newState.add(CO2_OVER, state.get(CO2) + co2Removed - newState.get(CO2));
		newState.add(AIR, state.get(AIR) - co2Removed);
		newState.add(CO2_CON, state.get(CO2_CON)
				* (1 - airNeeded / state.get(AIR)));
		newState.add(CO2_CAP, state.get(CO2_CAP));
		return newState;
	}

}
