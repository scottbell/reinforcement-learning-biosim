package edu.rice.biosim.RL.disaster;

import java.util.*;

public class Action {

	/**
	 * @param myActions -
	 *            Mapping of actuators to value that the actuator should be set
	 *            to
	 */
	private Map<ActionFactory.ActionEnum, Float> myActions;

	/**
	 * @param myMissionTime -
	 *            Time the action was taken
	 */
	private int myMissionTime;

	/**
	 * Default constructor. Initializes mapping
	 */
	public Action() {

		myActions = new TreeMap<ActionFactory.ActionEnum, Float>();
	}

	/**
	 * @param myActuator
	 *            The flow rate actuator to be set
	 * @param myValue
	 *            The value to set the flow rate to
	 */
	public void addActionPair(ActionFactory.ActionEnum myActuator, float myValue) {
		myActions.put(myActuator, myValue);
	}

	/**
	 * @param myAction
	 *            Actuator tracked in this Action set
	 * @return value to set actuator to
	 */
	public float getActionValue(ActionFactory.ActionEnum myActuator) {
		return myActions.get(myActuator);
	}

	public Map<ActionFactory.ActionEnum, Float> getActions() {
		return myActions;
	}

	/**
	 * @param missionTime
	 *            Sets mission time of action
	 */
	public void setMissionTime(int missionTime) {
		myMissionTime = missionTime;
	}

	/**
	 * @return Mission time when action taken
	 */
	public int getMissionTime() {
		return myMissionTime;
	}

	public boolean equals(Object obj) {

		if (obj instanceof Action) {
			return (myMissionTime == ((Action) obj).getMissionTime())
					&& myActions.equals(((Action) obj).getActions());

		}
		return false;
	}

	public String toString() {
		String temp = "";

		for (Map.Entry<ActionFactory.ActionEnum, Float> e : myActions
				.entrySet()) {
			temp += e.getKey() + "," + e.getValue() + ",";
		}

		return temp;

	}
}