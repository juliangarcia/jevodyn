package de.mpg.jevodyn.agentbased;

import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

import com.evolutionandgames.jevodyn.agentbased.Agent;
import com.evolutionandgames.jevodyn.agentbased.AgentBasedEvolutionaryProcess;
import com.evolutionandgames.jevodyn.agentbased.AgentBasedPayoffCalculator;
import com.evolutionandgames.jevodyn.agentbased.AgentBasedPopulation;
import com.evolutionandgames.jevodyn.agentbased.AgentBasedPopulationFactory;
import com.evolutionandgames.jevodyn.agentbased.AgentBasedSimulation;
import com.evolutionandgames.jevodyn.agentbased.AgentMutator;
import com.evolutionandgames.jevodyn.agentbased.impl.AgentBasedPopulationImpl;
import com.evolutionandgames.jevodyn.agentbased.impl.AgentBasedWrightFisherProcessWithAssortment;
import com.evolutionandgames.jevodyn.agentbased.simple.AgentBasedSimpleRandomPopulationFactory;
import com.evolutionandgames.jevodyn.agentbased.simple.AgentMatrixBasedPayoffCalculator;
import com.evolutionandgames.jevodyn.agentbased.simple.AgentMutatorSimpleKernel;
import com.evolutionandgames.jevodyn.utils.ArrayUtils;
import com.evolutionandgames.jevodyn.utils.Games;
import com.evolutionandgames.jevodyn.utils.PayoffToFitnessMapping;
import com.evolutionandgames.jevodyn.utils.Random;


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
		AgentMutator mutator = new AgentMutatorSimpleKernel(ArrayUtils.uniformMutationKernel(mutationProbability, numberOfTypes));
		AgentBasedPayoffCalculator payoffCalculator = new AgentMatrixBasedPayoffCalculator(gameMatrix);
		AgentBasedPopulationFactory factory = new AgentBasedSimpleRandomPopulationFactory(numberOfTypes, populationSize);
		Agent[] agentArray = ((AgentBasedPopulationImpl)factory.createPopulation()).getAsArrayOfAgents();
		AgentBasedPopulation population = new  AgentBasedPopulationImpl(agentArray);
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
