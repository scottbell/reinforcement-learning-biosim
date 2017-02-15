package edu.rice.biosim.RL.disaster;

public class RewardCalculator {

	private float REWARD_DEATH = -1;

	private float REWARD_STARVE = -0.04f;

	private float REWARD_SUFFOCATE = -0.02f;

	private float REWARD_THIRST = -0.02f;

	private float REWARD_POISON = -0.02f;

	private float REWARD_SICK = -0.02f;

	private float REWARD_O2_OVER = -16f * (float) Math.pow(10, -4);

	private float REWARD_CO2_OVER = -4f * (float) Math.pow(10, -4);

	private float REWARD_H2_OVER = -4f * (float) Math.pow(10, -4);

	private float REWARD_POT_OVER = -8f * (float) Math.pow(10, -4)
			* (1000f / 18.015f);

	private float REWARD_DIRTY_OVER = -8f * (float) Math.pow(10, -4)
			* (1000f / 18.015f);

	private float REWARD_GREY_OVER = -8f * (float) Math.pow(10, -4)
			* (1000f / 18.015f);

	private float REWARD_FOOD_OVER = -5f * (float) Math.pow(10, -4);

	private float REWARD_BIOMASS_OVER = -5f * (float) Math.pow(10, -4);

	private float REWARD_WASTE_OVER = -5f * (float) Math.pow(10, -4);

	private float REWARD_POWER_USED = -1f * (float) Math.pow(10, -9);

	private float REWARD_NO_STARVE = +0.002f;

	private float REWARD_NO_SUFFOCATE = +0.002f;

	private float REWARD_NO_THIRST = +0.002f;

	private float REWARD_NO_POISON = +0.002f;

	private float REWARD_NO_SICK = +0.002f;

	private float REWARD_NO_OVER = +0.002f;

	private float REWARD_CROP_DEATH = -0.02f;

	private float REWARD_NO_CROP_DEATH = +0.002f;

	private float REWARD_COMPLETE = 1f;

}
