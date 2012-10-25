package de.mpg.jevodyn.agentbased;

import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

import de.mpg.jevodyn.agentbased.impl.AgentBasedPopulationImpl;
import de.mpg.jevodyn.agentbased.impl.AgentBasedWrightFisherProcessWithAssortment;
import de.mpg.jevodyn.agentbased.simple.KernelBasedSimpleMutator;
import de.mpg.jevodyn.agentbased.simple.MatrixBasedSimpleAgentPayoffCalculator;
import de.mpg.jevodyn.agentbased.simple.SimplePopulationFactory;
import de.mpg.jevodyn.utils.ArrayUtils;
import de.mpg.jevodyn.utils.Games;
import de.mpg.jevodyn.utils.PayoffToFitnessMapping;
import de.mpg.jevodyn.utils.Random;

public class AgentBasedSimulationTotalPayoffTest {
	
	private static final double DELTA = 1.0;

	@Test
	public void testEstimateFixationProbabilityNeutral() {
		//for neutral selection should spend half time in each state
		Long seed = System.currentTimeMillis();
		Random.seed();
		double intensityOfSelection = 0.0;
		double mutationProbability = 0.01;
		int numberOfTypes = 2;
		double r= 0.0;
		RealMatrix gameMatrix = Games.prionersDilemma();
		int populationSize = 10;
		Mutator mutator = new KernelBasedSimpleMutator(ArrayUtils.uniformMutationKernel(mutationProbability, numberOfTypes));
		AgentBasedPayoffCalculator payoffCalculator = new MatrixBasedSimpleAgentPayoffCalculator(gameMatrix);
		AgentBasedPopulationFactory factory = new SimplePopulationFactory(numberOfTypes, populationSize);
		Agent[] agentArray = ((AgentBasedPopulationImpl)factory.createPopulation()).getAsArrayOfAgents();
		AgentBasedFixedSizePopulation population = new  AgentBasedPopulationImpl(agentArray);
		AgentBasedEvolutionaryProcess wf = new AgentBasedWrightFisherProcessWithAssortment(population, payoffCalculator, 
				PayoffToFitnessMapping.EXPONENTIAL, intensityOfSelection, mutator, r);
		AgentBasedSimulation simulation = new AgentBasedSimulation(wf);
		int burningTimePerEstimate = 100;
		int samplesPerEstimate = 500000;
		int numberOfEstimates = 10;
		int reportEveryTimeSteps = 5;
		double totalPayoff = simulation.estimateTotalPayoff(burningTimePerEstimate, samplesPerEstimate, numberOfEstimates, reportEveryTimeSteps, seed, factory); 
		double expectedPayoff = (gameMatrix.getEntry(0, 0)*0.5 + gameMatrix.getEntry(1, 1)*0.5)*populationSize; 
		//System.out.println(totalPayoff);
		//System.out.println(expectedPayoff);
		Assert.assertEquals(expectedPayoff, totalPayoff, DELTA);
	}

}
