package edu.rice.biosim.RL.disaster;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GraphDataGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int x_index;
		int x_index2;
		int y_index;
		try {
			NeuralNet theNN = NeuralNet.readFrom(System.getenv("RL_HOME")
					+ System.getProperty("file.separator") + "policies"
					+ System.getProperty("file.separator") + "ars_49.nn");

			FileWriter myFW = new FileWriter(System.getenv("RL_HOME")
					+ System.getProperty("file.separator") + "data"
					+ System.getProperty("file.separator")
					+ "crs_co2Level_starve.csv");
			FileWriter myFW2 = new FileWriter(System.getenv("RL_HOME")
					+ System.getProperty("file.separator") + "data"
					+ System.getProperty("file.separator") + "crs_Matrix.csv");
			Vector<Float> inputs = new Vector<Float>();

			inputs.add(0f);
			x_index2 = 0;
			inputs.add(5000f);
			inputs.add(9000f);
			inputs.add(0f);
			x_index = 3;
			inputs.add(500f);
			inputs.add(9000f);
			inputs.add(5000f);
			inputs.add(0f);
			y_index = 7;
			inputs.add(0f);
			inputs.add(0f);
			inputs.add(0f);
			inputs.add(0f);
			inputs.add(0f);

			for (float x_val = 0; x_val <= 2000f; x_val += 10) {
				inputs.set(x_index, x_val);
				inputs.set(x_index2, x_val);
				for (float y_val = 0; y_val < 2f; y_val++) {

					inputs.set(y_index, y_val);
					myFW.write(x_val + "," + y_val + ","
							+ theNN.getValue(inputs).get(1) + "\n");
				}
			}

			myFW.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
