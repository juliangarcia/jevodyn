package de.mpg.jevodyn.agentbased.simple;

import org.apache.commons.math3.linear.RealMatrix;

import de.mpg.jevodyn.agentbased.Agent;
import de.mpg.jevodyn.agentbased.Mutator;
import de.mpg.jevodyn.utils.Random;

public class KernelBasedSimpleMutator implements Mutator {
	
	private RealMatrix mutationKernel;

	
	
	public Agent mutate(Agent agent) {
		int oldStrategy = ((SimpleAgent)agent).getStrategy();
		int newStrategy = Random.simulateDiscreteDistribution(mutationKernel.getRow(oldStrategy));
		if (oldStrategy!=newStrategy) return new SimpleAgent(newStrategy);
		return agent;
	}



	public KernelBasedSimpleMutator(RealMatrix mutationKernel) {
		super();
		this.mutationKernel = mutationKernel;
	}

	
}
