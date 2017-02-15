/*
 * Created on Apr 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

import java.io.IOException;
import java.util.*;

/**
 * @author Travis R. Fischer
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NNChecker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			NeuralNet firstNN = NeuralNet.readFrom(System.getenv("RL_HOME")
					+ System.getProperty("file.separator") + "policies"
					+ System.getProperty("file.separator") + "wrs_-1.nn");
			NeuralNet lastNN = NeuralNet.readFrom(System.getenv("RL_HOME")
					+ System.getProperty("file.separator") + "policies"
					+ System.getProperty("file.separator") + "wrs_0.nn");
			NeuralNet thirdNN = NeuralNet.readFrom(System.getenv("RL_HOME")
					+ System.getProperty("file.separator") + "policies"
					+ System.getProperty("file.separator") + "wrs_10.nn");
			System.out.println("Are equal 1? " + firstNN.equals(lastNN));
			System.out.println("Are equal 2? " + firstNN.equals(thirdNN));
			System.out.println("Are equal 3? " + lastNN.equals(thirdNN));

			System.out.println("firstNN: " + firstNN.toString() + "\n");
			System.out.println("lastNN: " + lastNN.toString() + "\n");
			System.out.println("thirdNN: " + thirdNN.toString() + "\n");

			Vector<Float> inputs = new Vector<Float>();

			inputs.add(1f);
			inputs.add(0.574846f);
			inputs.add(1f);
			inputs.add(0.91971f);
			inputs.add(1f);
			inputs.add(0.501094f);
			inputs.add(0f);
			inputs.add(0f);
			inputs.add(0f);
			inputs.add(0f);
			inputs.add(0f);
			inputs.add(0f);

			System.out.println("output: " + thirdNN.getValue(inputs));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
