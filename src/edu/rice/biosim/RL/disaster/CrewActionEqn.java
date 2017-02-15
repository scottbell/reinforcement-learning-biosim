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
public class CrewActionEqn extends AActionEqn {

	public static AActionEqn Singleton = new CrewActionEqn();

	private CrewActionEqn() {

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

		return newState;
	}

}
