package com.evolutionandgames.jevodyn.agentbased.simple;

import org.apache.commons.math3.linear.RealMatrix;

import com.evolutionandgames.jevodyn.agentbased.Agent;
import com.evolutionandgames.jevodyn.agentbased.AgentMutator;
import com.evolutionandgames.jevodyn.utils.Random;


public class AgentMutatorSimpleKernel implements AgentMutator {
	
	private RealMatrix mutationKernel;

	
	
	public Agent mutate(Agent agent) {
		int oldStrategy = ((AgentSimple)agent).getStrategy();
		int newStrategy = Random.simulateDiscreteDistribution(mutationKernel.getRow(oldStrategy));
		if (oldStrategy!=newStrategy) return new AgentSimple(newStrategy);
		return agent;
	}



	public AgentMutatorSimpleKernel(RealMatrix mutationKernel) {
		super();
		this.mutationKernel = mutationKernel;
	}

	
}
