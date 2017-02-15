package edu.rice.biosim.RL.disaster;

public interface Percept {
	public boolean isBumping();

	public int getTime();

	public int[][] getMap();

	public int[][] getRoverLocations();

	public Action getLastAction();

	public int getRoverY();

	public int getRoverX();
}
