package edu.rice.biosim.RL.disaster;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.traclabs.biosim.client.control.ActionMap;
import com.traclabs.biosim.client.control.StateMap;
import com.traclabs.biosim.client.util.BioHolderInitializer;
import com.traclabs.biosim.idl.actuator.framework.GenericActuator;
import com.traclabs.biosim.idl.framework.MalfunctionIntensity;
import com.traclabs.biosim.idl.framework.MalfunctionLength;
import com.traclabs.biosim.idl.sensor.framework.GenericSensor;
import com.traclabs.biosim.idl.simulation.air.O2Store;
import com.traclabs.biosim.idl.simulation.crew.CrewGroup;
import com.traclabs.biosim.idl.simulation.environment.SimEnvironment;
import com.traclabs.biosim.idl.simulation.framework.Injector;
import com.traclabs.biosim.idl.simulation.power.PowerStore;
import com.traclabs.biosim.idl.simulation.water.DirtyWaterStore;
import com.traclabs.biosim.idl.simulation.water.GreyWaterStore;
import com.traclabs.biosim.idl.simulation.water.PotableWaterStore;

/**
 * @author Theresa Klein
 * @author Scott Bell (modified original code)
 */

public class MixedController extends AController {
	// feedback loop sttuff
	private float levelToKeepO2At = 0.20f;

	private float levelToKeepCO2At = 0.00111f;

	private float crewO2integral = 0f;

	private float crewCO2integral = 0f;

	private final static String TAB = "\t";

	// hand controller stuff;

	private StateMap continuousState;

	private ActionMap myActionMap;

	private Map classifiedState;

	private Map thresholdMap = new TreeMap();

	private SimEnvironment myCrewEnvironment;

	private PotableWaterStore myPotableWaterStore;

	private static int ATMOSPHERIC_PERIOD = 2;

	private static int CORE_PERIOD_MULT = 5;

	public static String[] stateNames = { "dirtywater", "greywater",
			"potablewater", "oxygen" };

	public static String[] actuatorNames = { "OGSpotable", "waterRSdirty",
			"waterRSgrey" };

	private Logger myLogger;

	public static final Integer HIGH = new Integer(0);

	public static final Integer LOW = new Integer(1);

	public static final Integer NORMAL = new Integer(2);

	private GenericSensor myO2AirConcentrationSensor;

	private GenericSensor myCO2AirConcentrationSensor;

	private Injector myO2Injector;

	private Injector myCO2Injector;

	private GenericActuator myO2AirStoreInInjectorAcutator;

	private GenericActuator myO2AirEnvironmentOutInjectorAcutator;

	private GenericActuator myCO2AirStoreInInjectorAcutator;

	private GenericActuator myCO2AirEnvironmentOutInjectorAcutator;

	private float myO2AirStoreInInjectorMax;

	private float myCO2AirStoreInInjectorMax;

	private GenericActuator myBiomassRSPowerActuator;

	private float myBiomassRSPowerRateValue;

	public MixedController() {
		myLogger = Logger.getLogger(this.getClass());
		collectReferences();
		setThresholds();
		continuousState = new StateMap();
		myActionMap = new ActionMap();

		myO2AirStoreInInjectorMax = myO2AirStoreInInjectorAcutator.getMax();

		if (myCO2Injector != null) {
			myCO2AirStoreInInjectorMax = myCO2AirStoreInInjectorAcutator
					.getMax();
		}
	}

	public MixedController(boolean causeMalf, int malfTime) {
		myLogger = Logger.getLogger(this.getClass());
		collectReferences();
		setThresholds();
		continuousState = new StateMap();
		myActionMap = new ActionMap();

		myO2AirStoreInInjectorMax = myO2AirStoreInInjectorAcutator.getMax();

		if (myCO2Injector != null) {
			myCO2AirStoreInInjectorMax = myCO2AirStoreInInjectorAcutator
					.getMax();
		}

		if (causeMalf) {
			myStr.setDisasterTimeTick(malfTime);
			myPowerStore.scheduleMalfunction(MalfunctionIntensity.LOW_MALF,
					MalfunctionLength.PERMANENT_MALF, malfTime);
		}
	}

	public static void main(String[] args) {
		MixedController myController = new MixedController();
		BasicConfigurator.configure();

		myController.setStrFile("mixedcontroller1.csv");
		for (int i = 0; i < 40000; i += 1000) {
			myController.setCurrentBiomassRSPowerInflowRateValue(i);
			myController.runSim();
			myController.resetController();
			myController.changeMalfunction(false, 3000);
		}

	}

	private void collectReferences() {
		myBioHolder = BioHolderInitializer.getBioHolder();
		myBioDriver = myBioHolder.theBioDriver;
		myBioDriver.setRunTillN(2160);
		myCrew = (CrewGroup) myBioHolder.theCrewGroups.get(0);

		myO2Injector = (Injector) myBioHolder.theInjectors.get(0);

		if (myBioHolder.theInjectors.size() >= 3) {
			myCO2Injector = (Injector) myBioHolder.theInjectors.get(2);
		}

		myDirtyWaterStore = (DirtyWaterStore) myBioHolder.theDirtyWaterStores
				.get(0);
		myPotableWaterStore = (PotableWaterStore) myBioHolder.thePotableWaterStores
				.get(0);
		myGreyWaterStore = (GreyWaterStore) myBioHolder.theGreyWaterStores
				.get(0);

		myO2Store = (O2Store) myBioHolder.theO2Stores.get(0);

		myCrewEnvironment = (SimEnvironment) myBioHolder.theSimEnvironments
				.get(0);
		myPowerStore = (PowerStore) myBioHolder.thePowerStores.get(0);

		myO2AirStoreInInjectorAcutator = (GenericActuator) (myBioHolder
				.getActuatorAttachedTo(
						myBioHolder.theO2AirStoreInFlowRateActuators,
						myO2Injector));

		myO2AirEnvironmentOutInjectorAcutator = (GenericActuator) (myBioHolder
				.getActuatorAttachedTo(
						myBioHolder.theO2AirEnvironmentOutFlowRateActuators,
						myO2Injector));

		myO2AirConcentrationSensor = (GenericSensor) (myBioHolder
				.getSensorAttachedTo(myBioHolder.theO2AirConcentrationSensors,
						myCrewEnvironment));

		if (myCO2Injector != null) {
			myCO2AirStoreInInjectorAcutator = (GenericActuator) (myBioHolder
					.getActuatorAttachedTo(
							myBioHolder.theCO2AirStoreInFlowRateActuators,
							myCO2Injector));

			myCO2AirEnvironmentOutInjectorAcutator = (GenericActuator) (myBioHolder
					.getActuatorAttachedTo(
							myBioHolder.theCO2AirEnvironmentOutFlowRateActuators,
							myCO2Injector));

			myCO2AirConcentrationSensor = (GenericSensor) (myBioHolder
					.getSensorAttachedTo(
							myBioHolder.theCO2AirConcentrationSensors,
							myCrewEnvironment));
		}


		myBiomassRSPowerActuator = (GenericActuator) myBioHolder
				.getActuatorAttachedTo(myBioHolder.thePowerInFlowRateActuators,
						myBiomassRS);
	}

	public void runSim() {
		myBioDriver.setPauseSimulation(true);
		myBioDriver.startSimulation();
		myLogger.info("Controller starting run");
		while (!myBioDriver.isDone()) {
			stepSim();
			recordIncrementalStatus();
		}
		myLogger.info("Controller ended on tick " + myBioDriver.getTicks());
		myLogger.info("Run completed. Recording situation.");
		recordFinalSituation();
		writeStrFile();
	}

	public void stepSim() {
		if (((myBioDriver.getTicks()) % (CORE_PERIOD_MULT * ATMOSPHERIC_PERIOD)) == 0) {
			myLogger.debug(myBioDriver.getTicks() + "");
			continuousState.updateState();
			classifiedState = classifyState(continuousState);
			myActionMap.performAction(classifiedState);
		}
		doO2Injector();
		setBiomassRSPowerInFlowRateValue();
		if (myCO2Injector != null)
			doCO2Injector();
		// advancing the sim 1 tick
		myBioDriver.advanceOneTick();
	}

	public void setThresholds() {
		// sets up the threshold map variable
		int dirtyWaterHighLevel = (int) myDirtyWaterStore.getCurrentCapacity();
		int dirtyWaterLowLevel = dirtyWaterHighLevel / 3;
		int greyWaterHighLevel = (int) myGreyWaterStore.getCurrentCapacity();
		int greyWaterLowLevel = greyWaterHighLevel / 3;
		int potableWaterHighLevel = (int) myPotableWaterStore
				.getCurrentCapacity();
		int potableWaterLowLevel = potableWaterHighLevel / 3;
		int O2StoreHighLevel = (int) myO2Store.getCurrentCapacity();
		int O2StoreLowLevel = O2StoreHighLevel / 3;

		Map dirtyWaterSubMap = new TreeMap();
		dirtyWaterSubMap.put(LOW, new Integer(dirtyWaterLowLevel));
		dirtyWaterSubMap.put(HIGH, new Integer(dirtyWaterHighLevel));
		thresholdMap.put("dirtywater", dirtyWaterSubMap);

		Map greyWaterSubMap = new TreeMap();
		greyWaterSubMap.put(LOW, new Integer(greyWaterLowLevel));
		greyWaterSubMap.put(HIGH, new Integer(greyWaterHighLevel));
		thresholdMap.put("greywater", greyWaterSubMap);

		Map oxygenSubMap = new TreeMap();
		oxygenSubMap.put(LOW, new Integer(O2StoreLowLevel));
		oxygenSubMap.put(HIGH, new Integer(O2StoreHighLevel));
		thresholdMap.put("oxygen", oxygenSubMap);

		Map potableWaterSubMap = new TreeMap();
		potableWaterSubMap.put(LOW, new Integer(potableWaterLowLevel));
		potableWaterSubMap.put(HIGH, new Integer(potableWaterHighLevel));
		thresholdMap.put("potablewater", potableWaterSubMap);
	}

	public Map classifyState(StateMap instate) {
		Map state = new TreeMap();

		Map thisSet;
		StringBuffer fileoutput;

		fileoutput = new StringBuffer(myBioDriver.getTicks());
		fileoutput.append(TAB);

		for (int i = 0; i < stateNames.length; i++) {

			thisSet = (Map) thresholdMap.get(stateNames[i]);
			fileoutput.append(instate.getStateValue(stateNames[i]));
			fileoutput.append(TAB);
			if (instate.getStateValue(stateNames[i]) < ((Integer) thisSet
					.get(LOW)).intValue())
				state.put(stateNames[i], LOW);
			else if (instate.getStateValue(stateNames[i]) > ((Integer) thisSet
					.get(HIGH)).intValue())
				state.put(stateNames[i], HIGH);
			else
				state.put(stateNames[i], NORMAL);
		}
		return state;
	}

	private void doO2Injector() {
		float crewAirPressure = myCrewEnvironment.getTotalPressure();
		// crew O2 feedback control
		float crewO2p = 100f;
		float crewO2i = 5f;
		float crewO2 = myO2AirConcentrationSensor.getValue();
		float delta = levelToKeepO2At - crewO2;
		crewO2integral += delta;
		float signal = (delta * crewO2p + crewO2i * crewO2integral);
		float valueToSet = Math.min(myO2AirStoreInInjectorMax, signal);
		myLogger.debug("setting O2 injector to " + valueToSet);
		valueToSet = Math.min(myO2AirStoreInInjectorMax, signal);
		myO2AirStoreInInjectorAcutator.setValue(valueToSet);
		myO2AirEnvironmentOutInjectorAcutator.setValue(valueToSet);
	}

	private void doCO2Injector() {
		float crewAirPressure = myCrewEnvironment.getTotalPressure();
		// crew O2 feedback control
		float crewCO2p = 100f;
		float crewCO2i = 5f;
		float crewCO2 = myCO2AirConcentrationSensor.getValue();
		float delta = levelToKeepCO2At - crewCO2;
		crewCO2integral += delta;
		float signal = (delta * crewCO2p + crewCO2i * crewCO2integral);
		float valueToSet = Math.min(myCO2AirStoreInInjectorMax, signal);
		myLogger.debug("setting CO2 injector to " + valueToSet);
		valueToSet = Math.min(myCO2AirStoreInInjectorMax, signal);
		myCO2AirStoreInInjectorAcutator.setValue(valueToSet);
		myCO2AirEnvironmentOutInjectorAcutator.setValue(valueToSet);
	}
	/**
	 * @param pO2AirStoreInInjectorMax
	 *            The myO2AirStoreInInjectorMax to set.
	 */
	public void setO2AirStoreInInjectorMax(float pO2AirStoreInInjectorMax) {
		myO2AirStoreInInjectorMax = pO2AirStoreInInjectorMax;
	}


	/**
	 * @param pO2AirStoreInInjectorMax
	 *            The myO2AirStoreInInjectorMax to set.
	 */
	public void setCO2AirStoreInInjectorMax(float pCO2AirStoreInInjectorMax) {
		myCO2AirStoreInInjectorMax = pCO2AirStoreInInjectorMax;
	}

	public void setBiomassRSPowerInFlowRateValue() {
		myBiomassRSPowerActuator.setValue(myBiomassRSPowerRateValue);
	}

	public void setCurrentBiomassRSPowerInflowRateValue(
			float pBiomassInflowRateVal) {
		myBiomassRSPowerRateValue = pBiomassInflowRateVal;
	}

	public void run() {

	}

}
