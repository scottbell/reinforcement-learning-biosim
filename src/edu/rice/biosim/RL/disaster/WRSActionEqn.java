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
public class WRSActionEqn extends AActionEqn {

	int POWER = 0;

	int POTABLE = 0;

	int GREY = 1;

	int DIRTY = 2;

	int POT_CAP = 3;

	int OVER = 4;

	public static AActionEqn Singleton = new WRSActionEqn();

	private WRSActionEqn() {
		ACTION_SIZE = 1;
		STATE_SIZE = 3;
	}

	/*
	 * (non-Javadoc) state should contain 5 elements: PotWaterStore level,
	 * DirtyWaterStore level, GreyWaterStoreLevel, PotableWaterCapacity and
	 * Potable Water Overflow action should be 1 element with Power given to
	 * system. Returns three element state same as input
	 * 
	 * @see edu.rice.biosim.RL.disaster.IActionEqn#calcStateChange(java.util.Vector,
	 *      java.util.Vector)
	 */
	public Vector<Float> calcStateChange(Vector<Float> state,
			Vector<Float> actions, Object inp) {

		Vector<Float> newState = new Vector<Float>();
		float temp;

		if (actions.size() != ACTION_SIZE) {
			throw new IllegalArgumentException(
					"Action vector wrong arguments. Expected: " + ACTION_SIZE
							+ " received: " + actions.size());
		}

		float totalWaterNeeded = (actions.get(POWER) / 1540f) * 4.26f;

		if (state.size() != STATE_SIZE) {
			throw new IllegalArgumentException(
					"State vector wrong arguments. Expected: " + STATE_SIZE
							+ " received: " + state.size());
		}

		if (state.get(DIRTY) >= totalWaterNeeded) {
			newState.add(DIRTY, state.get(DIRTY) - totalWaterNeeded);
			newState.add(GREY, state.get(GREY));
			newState.add(POTABLE, Math.min(state.get(POTABLE)
					+ totalWaterNeeded, state.get(POT_CAP)));
			newState.add(OVER, state.get(POTABLE) + totalWaterNeeded
					- newState.get(POTABLE));

		} else if (state.get(DIRTY) + state.get(GREY) >= totalWaterNeeded) {
			newState.add(DIRTY, 0f);
			newState.add(GREY, state.get(GREY) + totalWaterNeeded
					- state.get(DIRTY));
			newState.add(POTABLE, Math.min(state.get(POTABLE)
					+ totalWaterNeeded, state.get(POT_CAP)));
			newState.add(OVER, state.get(POTABLE) + totalWaterNeeded
					- newState.get(POTABLE));
		} else {
			newState.add(DIRTY, 0f);
			newState.add(GREY, 0f);
			temp = state.get(DIRTY) + state.get(GREY);
			newState.add(POTABLE, Math.min(state.get(POTABLE) + temp, state
					.get(POT_CAP)));
			newState.add(OVER, state.get(POTABLE) + temp
					- newState.get(POTABLE));
		}

		newState.add(POT_CAP, state.get(POT_CAP));

		return newState;
	}

}
