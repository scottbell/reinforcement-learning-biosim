/*
 * Created on Mar 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

import java.util.*;

/**
 * @author Travis R. Fischer
 * 
 */
public class ValueFunctionVisitor implements IValueVisitor {

	public static IValueVisitor Singleton = new ValueFunctionVisitor();

	public Object wrsMethod(Agent anAgent, Object input) {
		Float[] myWeights = { (float) .94, (float) .02, (float) .02,
				(float) .02 };
		return compute(anAgent, input, myWeights);

	}

	private Float compute(Agent anAgent, Object input, Float[] myWeights) {
		float ans = 0;
		List<Float> allValues = help(anAgent, input);

		// if (allValues.size() < myWeights.length) throw new
		// Exception("ValueFunctionVisitor help did not return enough values");
		for (int i = 0; i < myWeights.length; i++) {
			ans = ans + myWeights[i] * allValues.get(i);
		}
		return ans;
	}

	private List<Float> help(Agent anAgent, Object input) {
		// TODO:: WRITE ME
		// Needs to return following array:
		// wrs value
		// ars value
		// biomass value
		// power value
		return null;

	}

	public Object arsMethod(Agent anAgent, Object input) {
		Float[] myWeights = { (float) .02, (float) .94, (float) .02,
				(float) .02 };
		return compute(anAgent, input, myWeights);

	}

	public Object biomassMethod(Agent anAgent, Object input) {
		Float[] myWeights = { (float) .02, (float) .02, (float) .94,
				(float) .02 };
		return compute(anAgent, input, myWeights);

	}

	public Object powerMethod(Agent anAgent, Object input) {
		Float[] myWeights = { (float) .02, (float) .02, (float) .02,
				(float) .94 };
		return compute(anAgent, input, myWeights);

	}

}
