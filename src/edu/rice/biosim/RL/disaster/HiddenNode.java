/*
 * Created on Mar 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

import java.util.Random;
import java.util.Vector;

/**
 * @author Travis R. Fischer
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class HiddenNode implements INetNode {

	/**
	 * @param serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private static float INITIAL_ALPHA = 0.999f;

	private float k = 1f;

	private float alpha = 0.999f;

	/**
	 * @param weights
	 *            The vector of weights for this node
	 */
	private Vector<Float> weights;

	/**
	 * @param r
	 *            Used to generate random weights
	 */
	private Random r;

	/**
	 * @param parents
	 *            The INetNodes that give input to this node.
	 */
	private Vector<INetNode> parents;

	/**
	 * @param value
	 *            The calculated value of this node
	 */
	private float value;

	private float baseline;

	/**
	 * Default Constructor. Initializes weight vector
	 */
	public HiddenNode() {
		weights = new Vector<Float>();
		r = new Random();
	}

	public HiddenNode(Vector<INetNode> p, float base) {
		weights = new Vector<Float>();
		parents = p;
		r = new Random();
		setRandomWeights(parents.size());
		baseline = base;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rice.biosim.RL.disaster.INetNode#getValue()
	 */
	public float getValue() {

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rice.biosim.RL.disaster.INetNode#recomputeValue()
	 */
	public void recomputeValue() {
		value = sigmoid(getSum());

	}

	public float getSum() {

		float sum = 0;

		for (int i = 0; i < weights.size(); i++) {
			sum += weights.get(i) * parents.get(i).getValue();
		}

		return sum;
	}

	/**
	 * Implements the sigmoid function to provide the non-linearity to this
	 * function. Simply returns
	 * 
	 * 1 / (1 + e^-x)
	 * 
	 * @param x
	 *            The value to apply the function to
	 * @return The result
	 */
	public float sigmoid(float x) {
		float exp = (float) Math.exp(x);
		float mexp = (float) Math.exp(-k * x);
		// return (exp - mexp) / (exp + mexp);

		return 1 / (1 + mexp);
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

	public void setValue(float val) {
	}

	public void updateWeights(float reward) {

		float delta = alpha * (reward - baseline) * value * (1 - value);
		float w = 0;

		// System.out.println("reward: " + reward);
		// System.out.println("alpha: " + alpha);
		// System.out.println("baseline: " + baseline);
		// System.out.println("delta: " + delta);
		for (int i = 0; i < weights.size(); i++) {
			w = weights.get(i);

			// System.out.println("w before: " + w);
			// System.out.println("w+delta: " + (w+delta));
			w = w + delta;
			// System.out.println("w after: " + w);
			weights.set(i, w);
		}

	}

	public void updateAlpha() {
		alpha = alpha * INITIAL_ALPHA;
	}

	public boolean equals(Object o) {
		if (o instanceof HiddenNode) {

			return weights.equals(((HiddenNode) o).weights);

		}

		return false;
	}

	public String toString() {

		String s = "";

		s += "( ";

		for (Float f : weights) {
			s = s + f + " ";
		}

		s += ")";

		return s;

	}

	public float getValueOfParent(int i) {
		return parents.get(i).getValue();
	}

}
