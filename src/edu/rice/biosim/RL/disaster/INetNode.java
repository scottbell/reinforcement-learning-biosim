package edu.rice.biosim.RL.disaster;

import java.io.Serializable;

/*
 * Created on Mar 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Travis R. Fischer
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public interface INetNode extends Serializable {

	/**
	 * @return Value of this network node
	 */
	public float getValue();

	/**
	 * Recalculate value of this node
	 */
	public void recomputeValue();

	public void setValue(float val);

	public void updateWeights(float r);

	public void updateAlpha();

}
