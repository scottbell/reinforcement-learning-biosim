/*
 * Created on Mar 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

import java.util.*;

import com.traclabs.biosim.client.util.*;
import com.traclabs.biosim.idl.actuator.framework.GenericActuator;
import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.simulation.framework.Store;

/**
 * @author Travis R. Fischer
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ActionFactory {

	/**
	 * @param myActuators -
	 *            The actuators used
	 */
	private Vector<List> myActuators;

	private static BioHolder myBioHolder;

	public static ActionFactory Singleton = new ActionFactory();

	public enum ActionEnum {
		POWER;

		GenericActuator getActuator(BioModule theModule) {
			List myActuatorList = null;
			switch (this) {
			case POWER:
				myActuatorList = myBioHolder.thePowerInFlowRateActuators;
				break;

			}
			return myBioHolder.getActuatorAttachedTo(myActuatorList, theModule);
		}
	}

	public ActionFactory() {

		myActuators = new Vector<List>();

		myBioHolder = BioHolderInitializer.getBioHolder();

		myActuators.add(myBioHolder.thePowerInFlowRateActuators);

	}

	public ActionHistory createActionHistory(BioModule theBioModule,
			List<ActionEnum> theActuatorEnums) {

		Map<ActionEnum, GenericActuator> theActuators = new TreeMap<ActionEnum, GenericActuator>();

		for (ActionEnum a : theActuatorEnums) {
			theActuators.put(a, a.getActuator(theBioModule));
		}

		return new ActionHistory(theActuatorEnums, theActuators);
	}

	public float getActionStoreCapacity(ActionEnum ae) {
		switch (ae) {
		case POWER:
			return ((Store) myBioHolder.thePowerStores.get(0))
					.getCurrentCapacity();
		}
		return 0;
	}

}
