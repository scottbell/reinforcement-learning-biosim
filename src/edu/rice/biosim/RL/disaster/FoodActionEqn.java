/*
 * Created on Apr 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

import java.util.Vector;

/**
 * @author Travis R. Fischer
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FoodActionEqn extends AActionEqn {

	int POWER = 0;

	int BIOMASS = 0;

	int FOOD = 1;

	int FOOD_OVER = 2;

	int FOOD_CAP = 3;

	int DIRTY = 4;

	int DIRTY_OVER = 5;

	int DIRTY_CAP = 6;

	int WASTE = 7;

	int WASTE_OVER = 8;

	int WASTE_CAP = 9;

	float AVE_EDIBLE = 0.42f;

	float AVE_DIRTY_H2O = 0.9f;

	float AVE_WASTE = 0.06f;

	public static AActionEqn Singleton = new FoodActionEqn();

	private FoodActionEqn() {

		STATE_SIZE = 10;
		ACTION_SIZE = 1;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.rice.biosim.RL.disaster.AActionEqn#calcStateChange(java.util.Vector,
	 *      java.util.Vector)
	 */
	@Override
	public Vector<Float> calcStateChange(Vector<Float> state,
			Vector<Float> actions, Object inp) {

		Vector<Float> newState = new Vector<Float>();

		if (actions.size() != ACTION_SIZE) {
			throw new IllegalArgumentException(
					"Action vector wrong arguments. Expected: " + ACTION_SIZE
							+ " received: " + actions.size());
		}

		if (state.size() != STATE_SIZE) {
			throw new IllegalArgumentException(
					"State vector wrong arguments. Expected: " + STATE_SIZE
							+ " received: " + state.size());
		}

		float biomassNeeded = 200f;
		float powerNeeded = 100f;
		float newTotalBiomass;
		float newTotalWaste;
		float newTotalDirty;

		if (powerNeeded > actions.get(POWER)
				|| biomassNeeded > state.get(BIOMASS)) {
			newState = (Vector<Float>) state.clone();
		} else {

			newTotalBiomass = state.get(BIOMASS) + biomassNeeded * AVE_EDIBLE;
			newTotalWaste = state.get(WASTE) + biomassNeeded * AVE_WASTE;
			newTotalDirty = state.get(DIRTY) + biomassNeeded * AVE_DIRTY_H2O;

			newState.add(BIOMASS, state.get(BIOMASS) - biomassNeeded);
			newState.add(FOOD, Math.min(newTotalBiomass, state.get(FOOD_CAP)));
			newState.add(FOOD_OVER, newTotalBiomass - newState.get(FOOD));
			newState.add(WASTE, Math.min(newTotalWaste, state.get(WASTE_CAP)));
			newState.add(WASTE_OVER, newTotalWaste - newState.get(WASTE));
			newState.add(DIRTY, Math.min(newTotalDirty, state.get(DIRTY_CAP)));
			newState.add(DIRTY_OVER, newTotalDirty - newState.get(DIRTY));

		}

		return newState;
	}

}
