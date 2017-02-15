/*
 * Created on Mar 28, 2005
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
 * 
 * The nodes of a NeuralNet.
 */
public class ANetNode {

	/**
	 * @param weights
	 *            The internal weights of the node
	 */
	private Vector<Float> weights;

	/**
	 * @param inputs
	 *            The inputs to this node
	 */
	private Vector<Float> inputs;

	/**
	 * @param r
	 *            Allows for random weight generation.
	 */
	private Random r;

	/**
	 * Default constructor. Initializes all fields.
	 */
	public ANetNode() {
		weights = new Vector<Float>();
		inputs = new Vector<Float>();
		r = new Random();
	}

	/**
	 * @param w
	 *            The weights for this neural net node.
	 */
	public void setWeights(Vector<Float> w) {

		weights = new Vector<Float>(w);

	}

	/**
	 * @param val
	 *            The weight value that all weights will be set to.
	 * @param size
	 *            How many weights should be in node.
	 */
	public void setWeights(float val, int size) {

		weights.setSize(size);

		for (int i = 0; i < weights.size(); i++) {
			weights.set(i, val);
		}

	}

	/**
	 * @param size
	 *            The number of weights in the node.
	 */
	public void setRandomWeights(int size) {
		weights.setSize(size);

		for (int i = 0; i < weights.size(); i++) {
			weights.set(i, r.nextFloat());
		}
	}

	/**
	 * @param num
	 *            The index of the input
	 * @param val
	 *            The value of the input
	 */
	public void setInputs(int num, float val) {
		inputs.set(num, val);
	}

	/**
	 * @return
	 */
	public float getValue() {
		float ans = 0;

		for (int i = 0; i < inputs.size(); i++) {
			ans += weights.get(i) * inputs.get(i);
		}

		return ans;
	}

}
