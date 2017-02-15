/*
 * Created on Mar 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

/**
 * @author Travis R. Fischer
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public interface IValueVisitor {

	Object wrsMethod(Agent anAgent, Object input);

	Object arsMethod(Agent anAgent, Object input);

	Object biomassMethod(Agent anAgent, Object input);

	Object powerMethod(Agent anAgent, Object input);

}
