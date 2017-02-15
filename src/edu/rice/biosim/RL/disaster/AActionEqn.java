/*
 * Created on Apr 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

import java.util.*;

/**
 * @author Travis R. Fischer
 * 
 * Used as interface for the model mapping of BioSim.
 */
public abstract class AActionEqn {

	int ACTION_SIZE;

	int STATE_SIZE;

	public abstract Vector<Float> calcStateChange(Vector<Float> state,
			Vector<Float> actions, Object inp);

}
