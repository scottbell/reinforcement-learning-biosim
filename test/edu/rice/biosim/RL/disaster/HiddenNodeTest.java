/*
 * Created on Apr 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

import java.util.Vector;

import junit.framework.*;

public class HiddenNodeTest extends TestCase {

    public void testUpdateWeights() {
        
        Vector<INetNode> inputs = new Vector<INetNode>();
        
        inputs.add(new InputNode());
        
        HiddenNode hn = new HiddenNode(inputs,0.3f);
		
		System.out.println(hn.toString());
		
		inputs.get(0).setValue(2000.0f);
		hn.recomputeValue();
		
		
        System.out.println("parent value: " + hn.getValueOfParent(0));
		System.out.println("my value: " + hn.getValue());
		System.out.println("my Sum: " + hn.getSum());
        hn.updateWeights(-2.15648f);
        
        System.out.println(hn.toString());
    }
    
    public void testGetSum() {
        
        Vector<INetNode> inputs = new Vector<INetNode>();
        
        inputs.add(new InputNode());
        
        HiddenNode hn = new HiddenNode(inputs,3.0f);
        
        inputs.get(0).setValue(200f);
        Assert.assertEquals(0f,hn.getSum());
    }
    
    public void testSigmoid() {
        
		Vector<INetNode> inputs = new Vector<INetNode>();
        
        inputs.add(new InputNode());
        
        HiddenNode hn = new HiddenNode(inputs,3.0f);
        
        inputs.get(0).setValue(200f);
        
        
        Assert.assertEquals(0,hn.sigmoid(hn.getSum()));
        
        
    }
	
	public void testSigmoidExtensive() {
		
		Vector<INetNode> inputs = new Vector<INetNode>();
        
        inputs.add(new InputNode());
        
        HiddenNode hn = new HiddenNode(inputs,3.0f);
        
        inputs.get(0).setValue(200f);
        
        
        Assert.assertEquals(0.5f,hn.sigmoid(10));
		Assert.assertEquals(0.5f,hn.sigmoid(20));
		Assert.assertEquals(0.5f,hn.sigmoid(30));
		Assert.assertEquals(0.5f,hn.sigmoid(40));
		Assert.assertEquals(0.5f,hn.sigmoid(50));
		Assert.assertEquals(0.5f,hn.sigmoid(60));
		Assert.assertEquals(0.5f,hn.sigmoid(70));
		Assert.assertEquals(0.5f,hn.sigmoid(80));
		Assert.assertEquals(0.5f,hn.sigmoid(90));
		Assert.assertEquals(0.5f,hn.sigmoid(100));
		Assert.assertEquals(0.5f,hn.sigmoid(150));
		Assert.assertEquals(0.5f,hn.sigmoid(200));
	}
	
	public void testGetValue() {
		
		Vector<INetNode> inputs = new Vector<INetNode>();
        
        inputs.add(new InputNode());
        
        HiddenNode hn = new HiddenNode(inputs,3.0f);
        
        inputs.get(0).setValue(200f);
        
		hn.recomputeValue();
        
        Assert.assertEquals(0,hn.getValue());
        
     
	}

}
