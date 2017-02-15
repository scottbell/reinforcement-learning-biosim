/*
 * Created on Mar 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

import java.io.*;
import java.util.*;
import com.traclabs.biosim.idl.actuator.framework.GenericActuator;
import edu.rice.biosim.RL.disaster.ActionFactory.ActionEnum;

/**
 * @author Travis R. Fischer
 * 
 */
public class ActionHistory {

	/**
	 * @param myActionHistory -
	 *            A mapping of actions to integer mission time ticks
	 */
	private Map<Integer, Action> myActionHistory;

	private Map<ActionEnum, GenericActuator> myActuators;

	private List<ActionEnum> myActionEnums;

	public ActionHistory(Map<ActionEnum, GenericActuator> theActuators) {
		myActuators = theActuators;
		myActionHistory = new TreeMap<Integer, Action>();
	}

	public ActionHistory(List<ActionEnum> aeList,
			Map<ActionEnum, GenericActuator> theActuators) {
		myActuators = theActuators;
		myActionEnums = aeList;
		myActionHistory = new TreeMap<Integer, Action>();
	}

	public GenericActuator getActuator(ActionEnum myActuator) {
		return myActuators.get(myActuator);
	}

	public void setActuator(ActionEnum myActuator, Float value) {

		if (value < 0 || value.isNaN()) {
			getActuator(myActuator).setValue(getActuator(myActuator).getMin());
		} else if (value.isInfinite()) {
			getActuator(myActuator).setValue(getActuator(myActuator).getMax());
		} else {
			getActuator(myActuator).setValue(value);
		}
	}

	public void addAction(Action curAction) {
		myActionHistory.put(curAction.getMissionTime(), curAction);
	}

	public void addAllActions(int missionTime, List<Float> curActionSet) {

		// System.out.println("ActionEnum list: " + myActionEnums.toString());

		Action tempAction = new Action();
		tempAction.setMissionTime(missionTime);

		if (curActionSet.size() != myActionEnums.size()) {
			throw new IllegalArgumentException(
					"Improper action size given. Expected: "
							+ myActionEnums.size() + " Received: "
							+ curActionSet.size());
		}
		for (int i = 0; i < curActionSet.size(); i++) {
			tempAction.addActionPair(myActionEnums.get(i), getActuator(
					myActionEnums.get(i)).getMax()
					* curActionSet.get(i));
		}

		myActionHistory.put(tempAction.getMissionTime(), tempAction);

	}

	public Action getAction(int time) {
		return myActionHistory.get(time);

	}

	public void setAction(int time) {

		Action theAction = getAction(time);

		for (Map.Entry<ActionEnum, Float> e : theAction.getActions().entrySet()) {

			setActuator(e.getKey(), e.getValue());

		}

	}

	public void writeActionHistory(String s, int i) {
		File file = new File(System.getenv("RL_HOME")
				+ System.getProperty("file.separator") + "data"
				+ System.getProperty("file.separator") + s + "action" + i
				+ ".hist");
		try {
			FileWriter filew = new FileWriter(file, true);

			for (Map.Entry<Integer, Action> e : myActionHistory.entrySet()) {
				filew.write(e.getKey() + "," + e.getValue().toString() + "\n");
			}

			filew.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void resetActionHistory() {
		myActionHistory = new TreeMap<Integer, Action>();
	}

}
