/*
 * Created on Apr 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.rice.biosim.RL.disaster;

import java.io.IOException;

import junit.framework.*;
import java.util.*;

public class NeuralNetTest extends TestCase {

    public void testGetValue() {
    }

    public void testUpdateNN() {
        
        NeuralNet nn1 = new NeuralNet(1,1,1,3.0f);
        try {
            nn1.writeTo(System.getenv("RL_HOME")+System.getProperty("file.separator")+"test1.nn");
            
            Vector<Float> myInput = new Vector<Float>();
            Vector<Float> myOutput = new Vector<Float>();
            myInput.add(200f);
            
            
            myOutput = nn1.getValue(myInput);
            
            System.out.println("myOutput: " + myOutput.toString());
            
            
            nn1.updateNN(-2000);
            
            NeuralNet nn2 = NeuralNet.readFrom(System.getenv("RL_HOME")+System.getProperty("file.separator")+"test1.nn");
            Assert.assertEquals(nn1,nn2);
            
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public void testUpdateAlpha() {
    }

}
