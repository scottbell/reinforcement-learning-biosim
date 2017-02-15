/*
 * Created on Mar 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

/**
 * @author Travis R. Fischer
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class InputNode implements INetNode {

	/**
	 * @param serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private float input;

	public InputNode() {
		super();
	}

	public InputNode(String name) {
		super();
	}

	public void setValue(float in) {
		input = in;
	}

	public float getValue() {
		return input;
	}

	public void recomputeValue() {
		// TODO Auto-generated method stub

	}

	public void updateWeights(float grad) {
		// TODO Auto-generated method stub

	}

	public void updateAlpha() {
		// TODO Auto-generated method stub

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
		float mexp = (float) Math.exp(-x / 10f);
		// return (exp - mexp) / (exp + mexp);

		return 1 / (1 + mexp);
	}

}
