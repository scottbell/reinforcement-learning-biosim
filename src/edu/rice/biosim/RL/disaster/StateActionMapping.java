package edu.rice.biosim.RL.disaster;

import java.util.*;

public class StateActionMapping extends AActionEqn {

	AActionEqn crsAction = CRSActionEqn.Singleton;

	AActionEqn ogsAction = OGSActionEqn.Singleton;

	AActionEqn plantAction = PlantActionEqn.Singleton;

	AActionEqn vccrAction = VCCRActionEqn.Singleton;

	AActionEqn wrsAction = WRSActionEqn.Singleton;

	AActionEqn wasteAction = WasteActionEqn.Singleton;

	AActionEqn foodAction = FoodActionEqn.Singleton;

	int POWER_VCCR = 0;

	int POWER_OGS = 1;

	int POWER_PLANT = 2;

	int POWER_CRS = 3;

	int POWER_WRS = 4;

	int POWER_WASTE = 5;

	int POWER_FOOD = 6;

	int POT = 0;

	int POT_CAP = 1;

	int POT_OVER = 2;

	int DIRTY = 3;

	int DIRTY_CAP = 4;

	int DIRTY_OVER = 5;

	int GREY = 6;

	int GREY_CAP = 7;

	int GREY_OVER = 8;

	int H2 = 9;

	int H2_CAP = 10;

	int H2_OVER = 11;

	int CO2 = 12;

	int CO2_CAP = 13;

	int CO2_OVER = 14;

	int BIOMASS = 15;

	int BIOMASS_CAP = 16;

	int BIOMASS_OVER = 17;

	int FOOD = 18;

	int FOOD_CAP = 19;

	int FOOD_OVER = 20;

	int WASTE = 21;

	int WASTE_CAP = 22;

	int WASTE_OVER = 23;

	int O2 = 24;

	int O2_CAP = 25;

	int O2_OVER = 26;

	int TOTAL_PLANT_AREA = 27;

	int PLANT_WATER_DIFFERENCE = 28;

	int PLANT_POWER_DIFFERENCE = 29;

	int AIR = 30;

	int AIR_CO2_CONCENTRATION = 31;

	int WASTE_PRODUCTION_RATE = 32;

	Random r;

	Vector<Float> newState = new Vector<Float>();

	public StateActionMapping() {

		ACTION_SIZE = 7;
		STATE_SIZE = 33;

	}

	@Override
	public Vector<Float> calcStateChange(Vector<Float> state,
			Vector<Float> actions, Object inp) {

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

		newState = (Vector<Float>) state.clone();

		calcModulesInRandomOrder(actions);

		return newState;
	}

	private void calcModulesInRandomOrder(Vector<Float> a) {

		List<Integer> list = createRandomList(7);

		int temp;

		while (!list.isEmpty()) {
			temp = list.remove(0);

			switch (temp) {
			case 0:
				doCRSAction(a.get(POWER_CRS));
				break;
			case 1:
				doOGSAction(a.get(POWER_OGS));
				break;
			case 2:
				doFoodAction(a.get(POWER_FOOD));
				break;
			case 3:
				doPlantAction(a.get(POWER_PLANT));
				break;
			case 4:
				doVCCRAction(a.get(POWER_VCCR));
				break;
			case 5:
				doWRSAction(a.get(POWER_WRS));
				break;
			case 6:
				doWasteAction(a.get(POWER_WASTE));
				break;
			}
		}

	}

	private List<Integer> createRandomList(int n) {

		List<Integer> temp = new LinkedList<Integer>();
		for (int i = 0; i < n; i++) {
			temp.add(r.nextInt(i + 1), i);
		}

		return temp;
	}

	private void doCRSAction(float power) {

		Vector<Float> myState = new Vector<Float>();
		Vector<Float> myAction = new Vector<Float>();
		Vector<Float> myFinalState;
		myAction.add(power);

		myState.add(newState.get(CO2));
		myState.add(newState.get(H2));
		myState.add(newState.get(POT));
		myState.add(newState.get(POT_CAP));
		myState.add(newState.get(POT_OVER));

		myFinalState = CRSActionEqn.Singleton.calcStateChange(myState,
				myAction, null);

		newState.set(CO2, myFinalState.get(0));
		newState.set(H2, myFinalState.get(1));
		newState.set(POT, myFinalState.get(2));
		newState.set(POT_CAP, myFinalState.get(3));
		newState.set(POT_OVER, myFinalState.get(4));
	}

	private void doOGSAction(float power) {

		Vector<Float> myState = new Vector<Float>();
		Vector<Float> myAction = new Vector<Float>();
		Vector<Float> myFinalState;
		myAction.add(power);

		myState.add(newState.get(POT));
		myState.add(newState.get(H2_OVER));
		myState.add(newState.get(H2));
		myState.add(newState.get(H2_CAP));
		myState.add(newState.get(O2));
		myState.add(newState.get(O2_CAP));
		myState.add(newState.get(O2_OVER));

		myFinalState = OGSActionEqn.Singleton.calcStateChange(myState,
				myAction, null);

		newState.set(POT, myFinalState.get(0));
		newState.set(H2_OVER, myFinalState.get(1));
		newState.set(H2, myFinalState.get(2));
		newState.set(H2_CAP, myFinalState.get(3));
		newState.set(O2, myFinalState.get(4));
		newState.set(O2_CAP, myFinalState.get(5));
		newState.set(O2_OVER, myFinalState.get(6));
	}

	private void doFoodAction(float power) {

		Vector<Float> myState = new Vector<Float>();
		Vector<Float> myAction = new Vector<Float>();
		Vector<Float> myFinalState;
		myAction.add(power);

		myState.add(newState.get(BIOMASS));
		myState.add(newState.get(FOOD));
		myState.add(newState.get(FOOD_OVER));
		myState.add(newState.get(FOOD_CAP));
		myState.add(newState.get(DIRTY));
		myState.add(newState.get(DIRTY_OVER));
		myState.add(newState.get(DIRTY_CAP));
		myState.add(newState.get(WASTE));
		myState.add(newState.get(WASTE_OVER));
		myState.add(newState.get(WASTE_CAP));

		myFinalState = FoodActionEqn.Singleton.calcStateChange(myState,
				myAction, null);

		newState.set(BIOMASS, myFinalState.get(0));
		newState.set(FOOD, myFinalState.get(1));
		newState.set(FOOD_OVER, myFinalState.get(2));
		newState.set(FOOD_CAP, myFinalState.get(3));
		newState.set(DIRTY, myFinalState.get(4));
		newState.set(DIRTY_OVER, myFinalState.get(5));
		newState.set(DIRTY_CAP, myFinalState.get(6));
		newState.set(WASTE, myFinalState.get(7));
		newState.set(WASTE_OVER, myFinalState.get(8));
		newState.set(WASTE_CAP, myFinalState.get(9));
	}

	private void doPlantAction(float power) {

		Vector<Float> myState = new Vector<Float>();
		Vector<Float> myAction = new Vector<Float>();
		Vector<Float> myFinalState;
		myAction.add(power);

		myState.add(newState.get(GREY));
		myState.add(newState.get(TOTAL_PLANT_AREA));
		myState.add(newState.get(PLANT_WATER_DIFFERENCE));
		myState.add(newState.get(PLANT_POWER_DIFFERENCE));

		myFinalState = PlantActionEqn.Singleton.calcStateChange(myState,
				myAction, null);

		newState.set(GREY, myFinalState.get(0));
		newState.set(TOTAL_PLANT_AREA, myFinalState.get(1));
		newState.set(PLANT_WATER_DIFFERENCE, myFinalState.get(2));
		newState.set(PLANT_POWER_DIFFERENCE, myFinalState.get(3));
	}

	private void doVCCRAction(float power) {

		Vector<Float> myState = new Vector<Float>();
		Vector<Float> myAction = new Vector<Float>();
		Vector<Float> myFinalState;
		myAction.add(power);

		myState.add(newState.get(CO2));
		myState.add(newState.get(CO2_CAP));
		myState.add(newState.get(CO2_OVER));
		myState.add(newState.get(AIR));
		myState.add(newState.get(AIR_CO2_CONCENTRATION));

		myFinalState = VCCRActionEqn.Singleton.calcStateChange(myState,
				myAction, null);

		newState.set(CO2, myFinalState.get(0));
		newState.set(CO2_CAP, myFinalState.get(1));
		newState.set(CO2_OVER, myFinalState.get(2));
		newState.set(AIR, myFinalState.get(3));
		newState.set(AIR_CO2_CONCENTRATION, myFinalState.get(4));
	}

	private void doWRSAction(float power) {

		Vector<Float> myState = new Vector<Float>();
		Vector<Float> myAction = new Vector<Float>();
		Vector<Float> myFinalState;
		myAction.add(power);

		myState.add(newState.get(POT));
		myState.add(newState.get(GREY));
		myState.add(newState.get(DIRTY));
		myState.add(newState.get(POT_CAP));
		myState.add(newState.get(POT_OVER));

		myFinalState = WRSActionEqn.Singleton.calcStateChange(myState,
				myAction, null);

		newState.set(POT, myFinalState.get(0));
		newState.set(GREY, myFinalState.get(1));
		newState.set(DIRTY, myFinalState.get(2));
		newState.set(POT_CAP, myFinalState.get(3));
		newState.set(POT_OVER, myFinalState.get(4));
	}

	private void doWasteAction(float power) {
		Vector<Float> myState = new Vector<Float>();
		Vector<Float> myAction = new Vector<Float>();
		Vector<Float> myFinalState;
		myAction.add(power);

		myState.add(newState.get(O2));
		myState.add(newState.get(WASTE_PRODUCTION_RATE));
		myState.add(newState.get(CO2));
		myState.add(newState.get(CO2_CAP));
		myState.add(newState.get(CO2_OVER));
		myState.add(newState.get(WASTE));

		myFinalState = WasteActionEqn.Singleton.calcStateChange(myState,
				myAction, null);

		newState.set(O2, myFinalState.get(0));
		newState.set(WASTE_PRODUCTION_RATE, myFinalState.get(1));
		newState.set(CO2, myFinalState.get(2));
		newState.set(CO2_CAP, myFinalState.get(3));
		newState.set(CO2_OVER, myFinalState.get(4));
		newState.set(WASTE, myFinalState.get(5));
	}

}
