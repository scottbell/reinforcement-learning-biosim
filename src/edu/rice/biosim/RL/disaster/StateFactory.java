/*
 * Created on Mar 15, 2005
 *
 * Filename: AState.java
 */
package edu.rice.biosim.RL.disaster;

import java.util.List;

import com.traclabs.biosim.client.util.BioHolder;
import com.traclabs.biosim.client.util.BioHolderInitializer;
import com.traclabs.biosim.idl.simulation.air.CO2Store;
import com.traclabs.biosim.idl.simulation.air.H2Store;
import com.traclabs.biosim.idl.simulation.air.NitrogenStore;
import com.traclabs.biosim.idl.simulation.air.O2Store;
import com.traclabs.biosim.idl.simulation.food.BiomassStore;
import com.traclabs.biosim.idl.simulation.food.FoodStore;
import com.traclabs.biosim.idl.simulation.framework.Store;
import com.traclabs.biosim.idl.simulation.power.PowerStore;
import com.traclabs.biosim.idl.simulation.waste.DryWasteStore;
import com.traclabs.biosim.idl.simulation.water.DirtyWaterStore;
import com.traclabs.biosim.idl.simulation.water.GreyWaterStore;
import com.traclabs.biosim.idl.simulation.water.PotableWaterStore;

/**
 * @author Travis R. Fischer
 * @author Christy Beatty
 * @since Mar 15, 2005
 * @version 1.0
 * 
 */
public class StateFactory {

	/**
	 * @param myBioHolder -
	 *            BioHolder used in this BioSim simulation
	 */
	private static BioHolder myBioHolder;

	private CO2Store co2Store;

	private O2Store o2Store;

	private H2Store h2Store;

	private NitrogenStore nStore;

	private FoodStore foodStore;

	private BiomassStore bioStore;

	private PotableWaterStore potStore;

	private GreyWaterStore greyStore;

	private DirtyWaterStore dirtyStore;

	private PowerStore powerStore;

	public enum StatusEnum {
		STARVING, THIRSTY, DEAD, POISONED, SICK, SUFFOCATING
	}

	public enum LevelEnum {
		POWER, POT_H2O, GREY_H2O, DIRTY_H2O, H2, O2, CO2, N, FOOD, BIOMASS, WASTE
	}

	/**
	 * @param Singleton -
	 *            Uses singleton pattern
	 */
	public static StateFactory Singleton = new StateFactory();

	/**
	 * Default constructor. Initializes list of all sensors of interest for this
	 * simulation
	 */
	public StateFactory() {

		myBioHolder = BioHolderInitializer.getBioHolder();

	}

	public StateHistory createStateHistory(List<LevelEnum> myStoreSensors,
			List<StatusEnum> myAilments) {
		return new StateHistory(myStoreSensors, myAilments);
	}

	public StateHistory createStateHistory(List<LevelEnum> myStoreSensors) {
		return new StateHistory(myStoreSensors);
	}

	public static Store getStore(LevelEnum theStore) {

		CO2Store co2Store = (CO2Store) myBioHolder.theCO2Stores.get(0);
		O2Store o2Store = (O2Store) myBioHolder.theO2Stores.get(0);
		H2Store h2Store = (H2Store) myBioHolder.theH2Stores.get(0);
		NitrogenStore nStore = (NitrogenStore) myBioHolder.theNitrogenStores
				.get(0);
		FoodStore foodStore = (FoodStore) myBioHolder.theFoodStores.get(0);
		BiomassStore bioStore = (BiomassStore) myBioHolder.theBiomassStores
				.get(0);
		PotableWaterStore potStore = (PotableWaterStore) myBioHolder.thePotableWaterStores
				.get(0);
		GreyWaterStore greyStore = (GreyWaterStore) myBioHolder.theGreyWaterStores
				.get(0);
		DirtyWaterStore dirtyStore = (DirtyWaterStore) myBioHolder.theDirtyWaterStores
				.get(0);
		PowerStore powerStore = (PowerStore) myBioHolder.thePowerStores.get(0);
		DryWasteStore wasteStore = (DryWasteStore) myBioHolder.theDryWasteStores
				.get(0);

		switch (theStore) {
		case BIOMASS:
			return bioStore;

		case POWER:
			return powerStore;

		case POT_H2O:
			return potStore;

		case GREY_H2O:
			return greyStore;

		case DIRTY_H2O:
			return dirtyStore;

		case H2:
			return h2Store;

		case O2:
			return o2Store;

		case CO2:
			return co2Store;

		case N:
			return nStore;

		case FOOD:
			return foodStore;

		case WASTE:
			return wasteStore;

		}
		return null;

	}

}