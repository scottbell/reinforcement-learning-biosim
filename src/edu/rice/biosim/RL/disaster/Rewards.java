package edu.rice.biosim.RL.disaster;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Rewards {

	/**
	 * Enumerated data type of information tracked for reward purposes
	 */
	public enum RewardEnum {

		STARVE(-0.4f, +0.02f), SUFFOCATE(-0.2f, +0.02f), THIRST(-0.2f, +0.02f), POISON(
				-0.2f, +0.02f), SICK(-0.2f, +0.02f), O2_OVER(-16f
				* (float) Math.pow(10, -4), +0.02f), CO2_OVER(-4f
				* (float) Math.pow(10, -4), +0.02f), H2_OVER(-4f
				* (float) Math.pow(10, -4), +0.02f), POT_OVER(-8f
				* (float) Math.pow(10, -4) * (1000f / 18.015f), +0.02f), DIRTY_OVER(
				-8f * (float) Math.pow(10, -4) * (1000f / 18.015f), +0.02f), GREY_OVER(
				-8f * (float) Math.pow(10, -4) * (1000f / 18.015f), +0.02f), FOOD_OVER(
				-5f * (float) Math.pow(10, -4), +0.02f), BIOMASS_OVER(-5f
				* (float) Math.pow(10, -4), +0.02f), WASTE_OVER(-5f
				* (float) Math.pow(10, -4), +0.02f), POWER_USED(-1f
				* (float) Math.pow(10, -6), 0), CROP_DEATH(-0.2f, +0.02f), DEATH(
				-1f, 0f), MISSION(+1F, +1F);

		/**
		 * @param negative
		 *            Negative reward if reward state true
		 */
		private final float negative;

		/**
		 * @param positive
		 *            Positive reward if reward state true
		 */
		private final float positive;

		RewardEnum(float neg, float pos) {
			this.negative = neg;
			this.positive = pos;

		}

		public float punish() {
			return this.negative;
		}

		public float praise() {
			return this.positive;
		}
	}

	/**
	 * @param myRewardsList
	 *            The rewards that this Reward calculator would be concerned
	 *            with.
	 */
	private List<RewardEnum> myRewardsList;

	/**
	 * @param myRewards
	 *            Vector of rewards already assigned. Vector index corresponds
	 *            to time tick assignment made
	 */
	private Vector<Float> myRewards;

	/**
	 * @param tempReward
	 *            Temporary variable containing the partially calculated reward
	 */
	private float tempReward;

	public Rewards() {
		tempReward = 0;
		myRewards = new Vector<Float>();
	}

	public Rewards(List<RewardEnum> rewards) {

		myRewardsList = rewards;
		tempReward = 0;
		myRewards = new Vector<Float>();

	}

	/**
	 * @param rew
	 *            Rewards to be used by Agent
	 */
	public void setRewardsList(List<RewardEnum> rew) {
		myRewardsList = rew;
	}

	/**
	 * Checks a given RewardEnum to the reward list. If in list, adds to
	 * temporary rewards total.
	 * 
	 * @param rewardAmount
	 *            Amount of resource used/overflowed
	 * @param rew
	 *            Reward to be given
	 */
	public void calcRewardNum(float rewardAmount, RewardEnum rew) {
		for (RewardEnum e : myRewardsList) {
			if (e.equals(rew)) {
				tempReward += rewardAmount * e.punish();

				if (rewardAmount == 0) {
					tempReward += e.praise();
				}
			}
		}
	}

	/**
	 * Checks a given RewardEnum to the reward list. If in list, adds temporary
	 * rewards total.
	 * 
	 * @param rewardState
	 *            Indicates if ailment exists
	 * @param rew
	 *            Ailment reward calced for.
	 */
	public void calcRewardBool(boolean rewardState, RewardEnum rew) {
		for (RewardEnum e : myRewardsList) {
			if (e.equals(rew)) {
				if (rewardState) {
					tempReward += e.punish();
				} else {
					tempReward += e.praise();
				}
			}
		}
	}

	/**
	 * Permanently stores reward and returns it for policy update.
	 * 
	 * @return the reward assigned
	 */
	public float setReward() {
		float f = tempReward;
		tempReward = 0;
		myRewards.add(f);
		return f;
	}

	/**
	 * Writes a reward list to a file.
	 * 
	 * @param filename
	 *            The file to save reward list to
	 */
	public void saveRewards(String filename) {
		try {
			FileWriter rewardFW = new FileWriter(System.getenv("RL_HOME")
					+ System.getProperty("file.separator") + "data"
					+ System.getProperty("file.separator") + filename + ".rwd");
			for (Float f : myRewards) {
				rewardFW.write(f + "\n");
			}
			rewardFW.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void resetRewards() {
		tempReward = 0;
		myRewards = new Vector<Float>();
	}

	/**
	 * @return tempReward
	 */
	public float getTempReward() {
		return tempReward;
	}

}
