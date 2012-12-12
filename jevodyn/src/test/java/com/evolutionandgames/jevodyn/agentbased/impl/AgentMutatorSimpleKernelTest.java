package com.evolutionandgames.jevodyn.agentbased.impl;

import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

import com.evolutionandgames.jevodyn.agentbased.Agent;
import com.evolutionandgames.jevodyn.agentbased.simple.AgentMutatorSimpleKernel;
import com.evolutionandgames.jevodyn.agentbased.simple.AgentSimple;
import com.evolutionandgames.jevodyn.utils.ArrayUtils;
import com.evolutionandgames.jevodyn.utils.Random;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;


public class AgentMutatorSimpleKernelTest {

	private static final double DELTA = 0.05;

	//@Test
	public void testNoMutation() {
		Random.seed();
		double mutationProbability = 0.0;
		AgentMutatorSimpleKernel mutator = new AgentMutatorSimpleKernel(
				ArrayUtils.uniformMutationKernel(mutationProbability, 5));
		for (int i = 0; i < 20; i++) {
			Agent startingAgent = new AgentSimple(Random.nextInt(5));
			// for mutation rate zero, the result is always the same.
			for (int j = 0; j < 50; j++) {
				Assert.assertEquals(startingAgent,
						mutator.mutate(startingAgent));
			}
		}
	}

	//@Test
	public void testFullMutation() {
		Random.seed();
		double mutationProbability = 1.0;
		AgentMutatorSimpleKernel mutator = new AgentMutatorSimpleKernel(
				ArrayUtils.uniformMutationKernel(mutationProbability, 5));
		for (int i = 0; i < 20; i++) {
			Agent startingAgent = new AgentSimple(Random.nextInt(5));
			// for mutation rate zero, the result is always the same.
			for (int j = 0; j < 50; j++) {
				Assert.assertNotSame(startingAgent,
						mutator.mutate(startingAgent));
			}
		}
	}

	@Test
	public void testFullKernelMutation() {
		Random.seed();
		double mutationProbability = 0.3;
		int numberOfTypes = 4;
		int trials = 20000;
		RealMatrix kernel = ArrayUtils.uniformMutationKernel(
				mutationProbability, numberOfTypes);
		AgentMutatorSimpleKernel mutator = new AgentMutatorSimpleKernel(kernel);
		for (int i = 0; i < 20; i++) {
			Agent startingAgent = new AgentSimple(Random.nextInt(numberOfTypes));
			// for mutation rate zero, the result is always the same.
			Multiset<Agent> multiset = HashMultiset.create();
			for (int j = 0; j < trials; j++) {
				multiset.add(mutator.mutate(startingAgent));
			}
			// test frequencies
			int startingStrategyIndex = ((AgentSimple) startingAgent)
					.getStrategy();
			for (int j = 0; j < numberOfTypes; j++) {
				double estimate = multiset.count(new AgentSimple(j))
						/ (double) trials;
				Assert.assertEquals(kernel.getEntry(startingStrategyIndex, j),
						estimate, DELTA);
			}

		}
	}

}
