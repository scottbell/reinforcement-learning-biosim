package edu.rice.biosim.RL.disaster;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.traclabs.biosim.client.util.BioHolder;
import com.traclabs.biosim.client.util.BioHolderInitializer;

public class RLMain {

	private String DEFAULT_DIRECTORY = System.getenv("RL_HOME")
			+ System.getProperty("file.separator") + "policies";

	private PolicyController myPolicyController;

	private HandController myHandController;

	private MixedController myMixedController;

	private int dayOfDoom;

	private final boolean causeFailure = true;

	private BioHolder myBioHolder;

	private Logger myLogger;

	private Random rand;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String xmlFileLocation = getXMLfromArgs(args);
		RLMain rl = new RLMain();
		BasicConfigurator.configure();
		boolean runMixed = false;
		rl.initialize(xmlFileLocation);

		if (runMixed) {
			rl.runMixedController();
		} else {

			for (int i = 0; i < 50; i++) {
				rl.run();
				rl.updateMalfunction();
			}

		}
	}

	public RLMain() {
		Properties logProps = new Properties();
		logProps.setProperty("log4j.rootLogger", "INFO, rootAppender");
		logProps.setProperty("log4j.appender.rootAppender",
				"org.apache.log4j.ConsoleAppender");
		logProps.setProperty("log4j.appender.rootAppender.layout",
				"org.apache.log4j.PatternLayout");
		logProps.setProperty(
				"log4j.appender.rootAppender.layout.ConversionPattern",
				"%5p [%c] - %m%n");
		/*
		 * logProps.setProperty(
		 * "log4j.logger.com.traclabs.biosim.client.control.HandController",
		 * "DEBUG");
		 */
		PropertyConfigurator.configure(logProps);
		myLogger = Logger.getLogger(this.getClass());
	}

	public void initialize(String xmlLocation) {

		rand = new Random();
		BioHolderInitializer.setFile(xmlLocation);
		myBioHolder = BioHolderInitializer.getBioHolder();
		myBioHolder.theBioDriver.setRunTillN(90 * 24);
		
		//create new directory if not there
		File directory = new File(DEFAULT_DIRECTORY);
		if (!directory.exists())
			directory.mkdir();

		// dayOfDoom = rand.nextInt(2952) + 48;
		dayOfDoom = 3000;

		myPolicyController = new PolicyController(causeFailure, dayOfDoom);
		myHandController = new HandController(causeFailure, dayOfDoom);
		myMixedController = new MixedController(causeFailure, dayOfDoom);

		myPolicyController.changeMalfunction(causeFailure, dayOfDoom);
		myHandController.changeMalfunction(causeFailure, dayOfDoom);

		myMixedController.changeMalfunction(causeFailure, dayOfDoom);
		myMixedController.setStrFile("mixedcontroller1.csv");

		myPolicyController.setStrFile("policycontroller26.csv");
		myHandController.setStrFile("handcontroller26.csv");

		myPolicyController.loadPolicies(DEFAULT_DIRECTORY, -1);

	}

	public void run() {
		int runs[] = { 5, 5 };
		myLogger.info("Running main");
		while (runs[0] > 0 || runs[1] > 0) {
			if (runs[0] > 0) {
				myHandController.runSim();
				myHandController.resetController();
				runs[0]--;
			} else {
				myPolicyController.runSim();
				myPolicyController.resetController();
				runs[1]--;
			}

		}

	}

	public void updateMalfunction() {
		// dayOfDoom = rand.nextInt(2952) + 48;
		dayOfDoom = 3000;
		myHandController.changeMalfunction(causeFailure, dayOfDoom);
		myPolicyController.changeMalfunction(causeFailure, dayOfDoom);
		myMixedController.changeMalfunction(causeFailure, dayOfDoom);

	}

	public void runMixedController() {
		myMixedController.setStrFile("mixedcontroller1.csv");
		for (int i = 0; i < 40000; i += 1000) {
			myMixedController.setCurrentBiomassRSPowerInflowRateValue(i);
			myMixedController.runSim();
			myMixedController.resetController();
			myMixedController.changeMalfunction(causeFailure, dayOfDoom);
		}
	}
	
    /**
     * Grabs xml parameter from an array of string
     * 
     * @param myArgs
     *            an array of strings to parse for the parameter,
     *            "-xml". Used for setting xml init of this instance of the
     *            server. example, java RLMain -xml=/home/bob/init.xml
     */
    private static String getXMLfromArgs(String[] myArgs) {
    	String xmlLocation = null;
        for (int i = 0; i < myArgs.length; i++) {
            if (myArgs[i].startsWith("-xml="))
                xmlLocation = myArgs[i].split("=")[1];
        }
        return xmlLocation;
    }

}
