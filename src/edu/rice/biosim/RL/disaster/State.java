/*
 * Created on Mar 15, 2005
 *
 * Filename: AState.java
 */
package edu.rice.biosim.RL.disaster;

import java.util.*;

import edu.rice.biosim.RL.disaster.StateFactory.*;

/**
 * @author Travis R. Fischer
 * @author Christy Beatty
 * @since Mar 15, 2005
 * @version 1.0
 * 
 */
public class State {

	/**
	 * @param crewStatus -
	 *            Boolean value concerning the status ailment of the StatusEnum
	 */
	private Map<StatusEnum, Boolean> crewStatus;

	/**
	 * @param myMissionTime -
	 *            Mission Time when above data recorded
	 */
	private int myMissionTime;

	/**
	 * @param levelVals -
	 *            Mapping of level sensor to a pair of values, the current level
	 *            and current capacity of the store associated with this level
	 *            sensor.
	 */
	private Map<LevelEnum, Float> levelVals;

	/**
	 * @param finalMissionTime -
	 *            Mission lasts for fixed length
	 */
	private int finalMissionTime;

	/**
	 * Default constructor. Initiliazes maps
	 */
	public State() {

		crewStatus = new TreeMap<StatusEnum, Boolean>();
		levelVals = new TreeMap<LevelEnum, Float>();
		myMissionTime = -1;

		finalMissionTime = 90 * 24;
	}

	/**
	 * @return True if current mission time exceeded final mission time.
	 */
	public boolean isMissionComplete() {
		return (getMissionTime() >= finalMissionTime);
	}

	/**
	 * @return final mission time minus current mission time
	 */
	public int getRemainingMissionTime() {
		return finalMissionTime - getMissionTime();
	}

	/**
	 * @param tempStatus
	 *            Status ailment concerned with this pair
	 * @param b
	 *            Boolean value indicating if this status ailment afflicted the
	 *            crew
	 */
	public void addCrewStatus(StatusEnum tempStatus, boolean b) {
		crewStatus.put(tempStatus, b);

	}

	public void addLevelValue(LevelEnum levelE, Float level) {
		Vector<Float> temp = new Vector<Float>();

		temp.add(0, level);
		// temp.add(1,cap);
		levelVals.put(levelE, level);
	}

	// public void addLevelValue(LevelEnum levelE,Vector<Float> myValues) {
	// levelVals.put(levelE,myValues);
	// }

	/**
	 * @param ticks
	 *            Mission time for this state
	 */
	public void setMissionTime(int ticks) {
		myMissionTime = ticks;

	}

	/**
	 * @return mission time of this state
	 */
	public int getMissionTime() {
		return myMissionTime;
	}

	/**
	 * @param theStatus
	 *            Status ailment of this state
	 * @return boolean mapped to this enumeratied datatype
	 */
	public boolean getCrewStatus(StatusEnum theStatus) {
		return crewStatus.get(theStatus);
	}

	public float getStoreLevel(LevelEnum theStore) {
		return levelVals.get(theStore);
	}

	// public float getStoreCapacity(LevelEnum theStore) {
	// return levelVals.get(theStore).get(1);
	// }

	public Map<StatusEnum, Boolean> getAllCrewStatus() {
		return crewStatus;
	}

	public Map<LevelEnum, Float> getLevelValues() {
		return levelVals;
	}

	public boolean equals(Object obj) {

		if (obj instanceof State) {
			return crewStatus.equals(((State) obj).crewStatus)
					&& levelVals.equals(((State) obj).levelVals); // &&
																	// (myMissionTime
																	// ==
																	// ((State)obj).getMissionTime());
		}

		return false;
	}

	public String toString() {
		String temp = "";

		if (!levelVals.isEmpty()) {
			for (Map.Entry<StateFactory.LevelEnum, Float> e : levelVals
					.entrySet()) {
				temp += e.getKey() + "," + e.getValue();
			}
		}

		if (!crewStatus.isEmpty()) {
			for (Map.Entry<StateFactory.StatusEnum, Boolean> e : crewStatus
					.entrySet()) {
				temp += e.getKey() + "," + e.getValue() + ",";
			}
		}

		return temp;

	}

}