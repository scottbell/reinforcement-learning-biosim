package edu.rice.biosim.RL.disaster;

import java.io.*;

public class SingleTrialResults {

	/**
	 * @param disasterTimeTick
	 *            Tick when disaster occurred
	 */
	private int disasterTimeTick;

	private float levelPotWater;

	private float levelDirtyWater;

	private float levelGreyWater;

	private float levelCO2;

	private float levelO2;

	private float levelH2;

	private float levelPower;

	private float levelFood;

	private float levelBiomass;

	private float levelWaste;

	private float myTotalReward;

	/**
	 * @param numCrewDead
	 *            Counter of the number of crew that died
	 */
	private int numCrewDead;

	/**
	 * @param numCrewStarve
	 *            Counter of the number of times a crewman starved
	 */
	private int numCrewStarve;

	/**
	 * @param numCrewThirst
	 *            Counter of the number of times a crewman thirsted
	 */
	private int numCrewThirst;

	/**
	 * @param numCrewSuffocate
	 *            Counter of the number of times a crewman suffocated
	 */
	private int numCrewSuffocate;

	/**
	 * @param numCrewSick
	 *            Counter of the number of times a crewman got sick
	 */
	private int numCrewSick;

	/**
	 * @param numCrewPoison
	 *            Counter of the number of times a crewman was poisoned
	 */
	private int numCrewPoison;

	/**
	 * @param numPotOver
	 *            Total amount of Potable water that overflowed
	 */
	private float numPotOver;

	/**
	 * @param numDirtyOver
	 *            Total amount of dirty water that overflowed
	 */
	private float numDirtyOver;

	/**
	 * @param numGreyOver
	 *            Total amount of grey water that overflowed
	 */
	private float numGreyOver;

	/**
	 * @param numO2Over
	 *            Total amount of O2 that overflowed
	 */
	private float numO2Over;

	/**
	 * @param numH2Over
	 *            Total amount of H2 that overflowed
	 */
	private float numH2Over;

	/**
	 * @param numCO2Over
	 *            Total amount of CO2 that overflowed
	 */
	private float numCO2Over;

	/**
	 * @param numFoodOver
	 *            Total amount of food that overflowed
	 */
	private float numFoodOver;

	/**
	 * @param numPowerOver
	 *            Total amount of power that overflowed
	 */
	private float numPowerOver;

	private float numBiomassOver;

	private float numWasteOver;

	private int numTicks;

	private float totalPowerUsage;

	private int numPlantsDead;

	public SingleTrialResults() {
		numCrewDead = 0;
		numCrewStarve = 0;
		numCrewThirst = 0;
		numCrewSuffocate = 0;
		numCrewSick = 0;
		numCrewPoison = 0;
		disasterTimeTick = -1;

		numPotOver = 0;
		numDirtyOver = 0;
		numGreyOver = 0;
		numO2Over = 0;
		numH2Over = 0;
		numCO2Over = 0;
		numFoodOver = 0;
		numPowerOver = 0;
		numTicks = 0;
		totalPowerUsage = 0;
		numWasteOver = 0;
		numBiomassOver = 0;
		numPlantsDead = 0;
		myTotalReward = 0;
	}

	public float getNumCO2Over() {
		return numCO2Over;
	}

	public void setNumCO2Over(float numCO2Over) {
		this.numCO2Over = numCO2Over;
	}

	public void incrementCrewDead() {
		numCrewDead++;
	}

	public int getNumCrewDead() {
		return numCrewDead;
	}

	public void setNumCrewDead(int numCrewDead) {
		this.numCrewDead = numCrewDead;
	}

	public int getNumCrewPoison() {
		return numCrewPoison;
	}

	public void incrementNumCrewPoison() {
		numCrewPoison++;
	}

	public void setNumCrewPoison(int numCrewPoison) {
		this.numCrewPoison = numCrewPoison;
	}

	public void incrementNumCrewSick() {
		numCrewSick++;
	}

	public int getNumCrewSick() {
		return numCrewSick;
	}

	public void setNumCrewSick(int numCrewSick) {
		this.numCrewSick = numCrewSick;
	}

	public void incrementNumCrewStarve() {
		numCrewStarve++;
	}

	public int getNumCrewStarve() {
		return numCrewStarve;
	}

	public void setNumCrewStarve(int numCrewStarve) {
		this.numCrewStarve = numCrewStarve;
	}

	public void incrementNumCrewSuffocate() {
		numCrewSuffocate++;
	}

	public int getNumCrewSuffocate() {
		return numCrewSuffocate;
	}

	public void setNumCrewSuffocate(int numCrewSuffocate) {
		this.numCrewSuffocate = numCrewSuffocate;
	}

	public void incrementNumCrewThirst() {
		numCrewThirst++;
	}

	public int getNumCrewThirst() {
		return numCrewThirst;
	}

	public void setNumCrewThirst(int numCrewThirst) {
		this.numCrewThirst = numCrewThirst;
	}

	public float getNumDirtyOver() {
		return numDirtyOver;
	}

	public void setNumDirtyOver(float numDirtyOver) {
		this.numDirtyOver = numDirtyOver;
	}

	public float getNumFoodOver() {
		return numFoodOver;
	}

	public void setNumFoodOver(float numFoodOver) {
		this.numFoodOver = numFoodOver;
	}

	public float getNumGreyOver() {
		return numGreyOver;
	}

	public void setNumGreyOver(float numGreyOver) {
		this.numGreyOver = numGreyOver;
	}

	public float getNumH2Over() {
		return numH2Over;
	}

	public void setNumH2Over(float numH2Over) {
		this.numH2Over = numH2Over;
	}

	public float getNumO2Over() {
		return numO2Over;
	}

	public void setNumO2Over(float numO2Over) {
		this.numO2Over = numO2Over;
	}

	public float getNumPotOver() {
		return numPotOver;
	}

	public void setNumPotOver(float numPotOver) {
		this.numPotOver = numPotOver;
	}

	public float getNumPowerOver() {
		return numPowerOver;
	}

	public void setNumPowerOver(float numPowerOver) {
		this.numPowerOver = numPowerOver;
	}

	public int getNumTicks() {
		return numTicks;
	}

	public void setNumTicks(int numTicks) {
		this.numTicks = numTicks;
	}

	public float getTotalPowerUsage() {
		return totalPowerUsage;
	}

	public void setTotalPowerUsage(float totalPowerUsage) {
		this.totalPowerUsage = totalPowerUsage;
	}

	protected void writeToFile(String filename) {
		boolean addHeader = false;
		try {
			System.out
					.println("Attempting to write to file:[" + filename + "]");
			File file = new File(System.getenv("RL_HOME")
					+ System.getProperty("file.separator") + "data"
					+ System.getProperty("file.separator") + filename);
			if (!file.exists()) {
				file.createNewFile();
				addHeader = true;
			}
			System.out.println(file.getAbsolutePath());
			FileWriter filew = new FileWriter(file, true);

			if (addHeader) {
				filew
						.append("NumTicks,DisasterTimeTick,TotalReward,TotalPowerUsed,NumPotOverflow,NumGreyOverflow,NumDirtyOverflow,NumH2Overflow,NumCO2Overflow,NumO2Overflow,NumFoodOverflow,NumBiomassOverflow,NumWasteOverflow,");
				filew
						.append("NumCrewDead,NumCrewThirsty,NumCrewSuffocate,NumCrewStarve,NumCrewPoison,NumCrewSick,PotWaterLevel,DirtyWaterLevel,GreyWaterLevel,H2Level,CO2Level,O2Level,FoodLevel,PowerLevel,WasteLevel,BiomassLevel,NumPlantsDead\n");
			}

			filew.append(numTicks + ",");
			filew.append(disasterTimeTick + ",");
			filew.append(myTotalReward + ",");
			filew.append(totalPowerUsage + ",");
			filew.append(numPotOver + ",");
			filew.append(numGreyOver + ",");
			filew.append(numDirtyOver + ",");
			filew.append(numH2Over + ",");
			filew.append(numCO2Over + ",");
			filew.append(numO2Over + ",");
			filew.append(numFoodOver + ",");
			filew.append(numBiomassOver + ",");
			filew.append(numWasteOver + ",");
			filew.append(numCrewDead + ",");
			filew.append(numCrewThirst + ",");
			filew.append(numCrewSuffocate + ",");
			filew.append(numCrewStarve + ",");
			filew.append(numCrewPoison + ",");
			filew.append(numCrewSick + ",");
			filew.append(levelPotWater + ",");
			filew.append(levelDirtyWater + ",");
			filew.append(levelGreyWater + ",");
			filew.append(levelH2 + ",");
			filew.append(levelCO2 + ",");
			filew.append(levelO2 + ",");
			filew.append(levelFood + ",");
			filew.append(levelPower + ",");
			filew.append(levelWaste + ",");
			filew.append(levelBiomass + ",");
			filew.append(numPlantsDead + ",");
			filew.append("\n");
			filew.flush();
			filew.close();
			System.out.println("File exists: " + file.exists());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Write failed");
			e.printStackTrace();

		}

	}

	public int getDisasterTimeTick() {
		return disasterTimeTick;
	}

	public void setDisasterTimeTick(int diasterTimeTick) {
		this.disasterTimeTick = diasterTimeTick;
	}

	/**
	 * @return Returns the levelCO2.
	 */
	public float getLevelCO2() {
		return levelCO2;
	}

	/**
	 * @param levelCO2
	 *            The levelCO2 to set.
	 */
	public void setLevelCO2(float levelCO2) {
		this.levelCO2 = levelCO2;
	}

	/**
	 * @return Returns the levelDirtyWater.
	 */
	public float getLevelDirtyWater() {
		return levelDirtyWater;
	}

	/**
	 * @param levelDirtyWater
	 *            The levelDirtyWater to set.
	 */
	public void setLevelDirtyWater(float levelDirtyWater) {
		this.levelDirtyWater = levelDirtyWater;
	}

	/**
	 * @return Returns the levelFood.
	 */
	public float getLevelFood() {
		return levelFood;
	}

	/**
	 * @param levelFood
	 *            The levelFood to set.
	 */
	public void setLevelFood(float levelFood) {
		this.levelFood = levelFood;
	}

	/**
	 * @return Returns the levelGreyWater.
	 */
	public float getLevelGreyWater() {
		return levelGreyWater;
	}

	/**
	 * @param levelGreyWater
	 *            The levelGreyWater to set.
	 */
	public void setLevelGreyWater(float levelGreyWater) {
		this.levelGreyWater = levelGreyWater;
	}

	/**
	 * @return Returns the levelH2.
	 */
	public float getLevelH2() {
		return levelH2;
	}

	/**
	 * @param levelH2
	 *            The levelH2 to set.
	 */
	public void setLevelH2(float levelH2) {
		this.levelH2 = levelH2;
	}

	/**
	 * @return Returns the levelO2.
	 */
	public float getLevelO2() {
		return levelO2;
	}

	/**
	 * @param levelO2
	 *            The levelO2 to set.
	 */
	public void setLevelO2(float levelO2) {
		this.levelO2 = levelO2;
	}

	/**
	 * @return Returns the levelPotWater.
	 */
	public float getLevelPotWater() {
		return levelPotWater;
	}

	/**
	 * @param levelPotWater
	 *            The levelPotWater to set.
	 */
	public void setLevelPotWater(float levelPotWater) {
		this.levelPotWater = levelPotWater;
	}

	/**
	 * @return Returns the levelPower.
	 */
	public float getLevelPower() {
		return levelPower;
	}

	/**
	 * @param levelPower
	 *            The levelPower to set.
	 */
	public void setLevelPower(float levelPower) {
		this.levelPower = levelPower;
	}

	public void setNumBiomassOver(float overflow) {
		this.numBiomassOver = overflow;

	}

	public void setNumWasteOver(float overflow) {
		// TODO Auto-generated method stub
		this.numWasteOver = overflow;
	}

	public void setLevelWaste(float currentLevel) {
		// TODO Auto-generated method stub
		this.levelWaste = currentLevel;
	}

	public void setLevelBiomass(float currentLevel) {
		// TODO Auto-generated method stub
		this.levelBiomass = currentLevel;
	}

	public float getNumBiomassOver() {
		// TODO Auto-generated method stub
		return numBiomassOver;
	}

	public float getNumWasteOver() {
		// TODO Auto-generated method stub
		return numWasteOver;
	}

	public void setNumPlantsDead(int incDeadPlants) {
		// TODO Auto-generated method stub
		this.numPlantsDead = incDeadPlants;
	}

	public int getNumPlantsDead() {
		return numPlantsDead;
	}

	public void updateTotalReward(float totalReward) {
		// TODO Auto-generated method stub

		myTotalReward = myTotalReward + totalReward;

	}

	public float getTotalReward() {
		return myTotalReward;
	}

}
