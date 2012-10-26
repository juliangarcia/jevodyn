package de.mpg.jevodyn.agentbased.simple;

import org.apache.commons.math3.linear.RealMatrix;

import de.mpg.jevodyn.agentbased.Agent;
import de.mpg.jevodyn.agentbased.AgentMutator;
import de.mpg.jevodyn.utils.Random;

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
