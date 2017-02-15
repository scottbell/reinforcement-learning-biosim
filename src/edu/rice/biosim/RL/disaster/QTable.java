/*
 * Created on Mar 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

import java.util.*;

/**
 * @author Travis R. Fischer
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class QTable {

	/**
	 * @param myQTable -
	 *            Maps State and Action pairs to a real q-value.
	 */
	private Map<State, Map<Action, Float>> myQTable;

	/**
	 * Default constructor. Initializes QTable.
	 */
	public QTable() {
		myQTable = new TreeMap<State, Map<Action, Float>>();
	}

	/**
	 * @param myState
	 *            State of a Qval
	 * @param myAction
	 *            Action of a Qval
	 * @return The value from the Qtable for this state/action pair
	 */
	public float getQValue(State myState, Action myAction) {
		return myQTable.get(myState).get(myAction);
	}

	/**
	 * @param myState
	 *            State of current Qval
	 * @param myAction
	 *            Action of current Qval
	 * @param val
	 *            Value derived from the state/action pair
	 */
	public void addQValue(State myState, Action myAction, float val) {
		Map<Action, Float> tempMap;
		// State already exists, then Map<Action,Float> already exists.
		if (myQTable.containsKey(myState)) {
			tempMap = myQTable.remove(myState);
		} else {
			tempMap = new TreeMap<Action, Float>();
		}

		tempMap.put(myAction, val);
		myQTable.put(myState, tempMap);
	}

}
