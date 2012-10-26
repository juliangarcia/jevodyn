package de.mpg.jevodyn.agentbased;

import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

import de.mpg.jevodyn.agentbased.impl.AgentBasedPopulationImpl;
import de.mpg.jevodyn.agentbased.impl.AgentBasedWrightFisherProcessWithAssortment;
import de.mpg.jevodyn.agentbased.simple.AgentMutatorSimpleKernel;
import de.mpg.jevodyn.agentbased.simple.AgentMatrixBasedPayoffCalculator;
import de.mpg.jevodyn.agentbased.simple.AgentSimple;
import de.mpg.jevodyn.agentbased.simple.AgentBasedSimpleRandomPopulationFactory;
import de.mpg.jevodyn.utils.ArrayUtils;
import de.mpg.jevodyn.utils.Games;
import de.mpg.jevodyn.utils.PayoffToFitnessMapping;
import de.mpg.jevodyn.utils.Random;

public class AgentBasedSimulationFixationTest {
	
	private static final double DELTA = 0.01;

	@Test
	public void testEstimateFixationProbabilityNeutral() {
		Long seed = System.currentTimeMillis();
		Random.seed();
		double intensityOfSelection = 0.0;
		double mutationProbability = 0.01;
		int numberOfTypes = 2;
		double r= 0.0;
		RealMatrix gameMatrix = Games.prionersDilemma();
		int populationSize = 10;
		AgentMutator mutator = new AgentMutatorSimpleKernel(ArrayUtils.uniformMutationKernel(mutationProbability, numberOfTypes));
		AgentBasedPayoffCalculator payoffCalculator = new AgentMatrixBasedPayoffCalculator(gameMatrix);
		AgentBasedPopulationFactory factory = new AgentBasedSimpleRandomPopulationFactory(numberOfTypes, populationSize);
		Agent[] agentArray = ((AgentBasedPopulationImpl)factory.createPopulation()).getAsArrayOfAgents();
		AgentBasedPopulation population = new  AgentBasedPopulationImpl(agentArray);
		AgentBasedEvolutionaryProcess wf = new AgentBasedWrightFisherProcessWithAssortment(population, payoffCalculator, 
				PayoffToFitnessMapping.EXPONENTIAL, intensityOfSelection, mutator, r);
		AgentBasedSimulation simulation = new AgentBasedSimulation(wf);
		int numberOfSamples = 500000;
		double fixation = simulation.estimateFixationProbability(new AgentSimple(1), new AgentSimple(0), numberOfSamples, seed);
		Assert.assertEquals(1.0/populationSize, fixation, DELTA);
	}

}
