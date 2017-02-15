/*
 * Created on Mar 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

/**
 * @author Travis R. Fischer
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 * 
 * This is an abstract implementation of a neural net that is used for the
 * Disaster Management policies
 */
public class NeuralNet implements Serializable {

	/**
	 * @param serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private Vector<INetNode> inputNodes;

	private Vector<INetNode> hiddenNodes;

	private Vector<INetNode> outputNodes;

	private float alpha;
	

	public NeuralNet(int inputSize, int hiddenSize, int outputSize, float base) {
		inputNodes = new Vector<INetNode>();
		for (int i = 0; i < inputSize; i++) {
			inputNodes.add(new InputNode());
		}

		hiddenNodes = new Vector<INetNode>();
		for (int j = 0; j < hiddenSize; j++) {
			hiddenNodes.add(new HiddenNode(inputNodes, base));
		}

		outputNodes = new Vector<INetNode>();
		for (int k = 0; k < outputSize; k++) {
			outputNodes.add(new HiddenNode(hiddenNodes, base));
		}

	}

	/**
	 * @param inputs
	 *            Inputs given to the NN
	 * @return Values after evaluating NN with inputs
	 */
	public Vector<Float> getValue(Vector<Float> inputs) {

		Vector<Float> myAns = new Vector<Float>();

		if (inputs.size() != inputNodes.size()) {
			throw new IllegalArgumentException(
					"Improper inputs given to Neural Net. Expected: "
							+ inputNodes.size() + " Received: " + inputs.size());
		}

		for (int i = 0; i < inputs.size(); i++) {
			inputNodes.get(i).setValue(inputs.get(i));
		}

		for (int j = 0; j < hiddenNodes.size(); j++) {
			hiddenNodes.get(j).recomputeValue();
		}

		for (int k = 0; k < outputNodes.size(); k++) {
			outputNodes.get(k).recomputeValue();
			myAns.add(k, outputNodes.get(k).getValue());
		}

		return myAns;
	}

	/**
	 * Used for back-propagation of the neural net to adjust the weights of the
	 * system.
	 * 
	 * @param reward
	 *            The calculated "error" of the network (based on reward
	 *            decisions after evaluating the neural net).
	 */
	public void updateNN(float reward) {

		// adjust weights of output layer
		for (int i = 0; i < outputNodes.size(); i++) {

			outputNodes.get(i).updateWeights(reward);
		}

		for (INetNode j : hiddenNodes) {

			j.updateWeights(reward);
		}

	}

	/**
	 * Method which writes this network to the given file
	 * 
	 * @param file
	 *            The file to write to
	 */
public void writeTo(String filename) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				filename));
		oos.writeObject(this);
		oos.flush();
		oos.close();
	}
	/**
	 * Method which reads and returns a network from the given file
	 * 
	 * @param filename
	 *            The file to read from
	 */
	public static NeuralNet readFrom(String filename) throws IOException,
			ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				filename));
		NeuralNet net = (NeuralNet) ois.readObject();
		ois.close();

		return net;
	}

	public void updateAlpha() {
		for (INetNode hidden : hiddenNodes) {
			hidden.updateAlpha();
		}

		for (INetNode output : outputNodes) {
			output.updateAlpha();
		}
	}

	public boolean equals(Object o) {

		boolean answer = false;

		if (o instanceof NeuralNet) {

			return hiddenNodes.equals(((NeuralNet) o).hiddenNodes)
					&& outputNodes.equals(((NeuralNet) o).outputNodes);
		}
		return false;
	}

	public String toString() {
		String s = "[";

		for (INetNode hidden : hiddenNodes) {
			s += ((HiddenNode) hidden).toString();
		}

		s += ";";

		for (INetNode output : outputNodes) {
			s += ((HiddenNode) output).toString();
		}

		s += "]";

		return s;
	}

}
