package de.mpg.jevodyn.agentbased.impl;

import org.junit.Assert;
import org.junit.Test;

import com.evolutionandgames.jevodyn.agentbased.Agent;
import com.evolutionandgames.jevodyn.agentbased.AgentBasedPayoffCalculator;
import com.evolutionandgames.jevodyn.agentbased.AgentBasedPopulation;
import com.evolutionandgames.jevodyn.agentbased.AgentMutator;
import com.evolutionandgames.jevodyn.agentbased.impl.AgentBasedPopulationImpl;
import com.evolutionandgames.jevodyn.agentbased.impl.AgentBasedWrightFisherProcessWithAssortment;
import com.evolutionandgames.jevodyn.agentbased.simple.AgentBasedSimpleRandomPopulationFactory;
import com.evolutionandgames.jevodyn.agentbased.simple.AgentMatrixBasedPayoffCalculator;
import com.evolutionandgames.jevodyn.agentbased.simple.AgentMutatorSimpleKernel;
import com.evolutionandgames.jevodyn.agentbased.simple.AgentSimple;
import com.evolutionandgames.jevodyn.utils.ArrayUtils;
import com.evolutionandgames.jevodyn.utils.Games;
import com.evolutionandgames.jevodyn.utils.PayoffToFitnessMapping;
import com.evolutionandgames.jevodyn.utils.Random;


public class AgentBasedWrightFisherProcessWithAssortmentTest {

	private class EveryBodyGetsOnePayoffCalculator implements
			AgentBasedPayoffCalculator {
		public void calculatePayoffs(AgentBasedPopulation population) {
			for (int i = 0; i < population.getSize(); i++) {
				population.setPayoffOfAgent(i, 1.0);
			}
		}
	}



	private static final double DELTA = 0.05;

	@Test
	public void testStepIsInvariableInPopulationSize() {
		// size does not vary as evolution progresses
		Random.seed(null);
		double intensityOfSelection = 1.0;
		double mutationProbablity = 0.1;
		double r = 0.0;
		// we repeat it 5 times with random numberOftypes and popSize
		for (int repetitions = 0; repetitions < 5; repetitions++) {
			int numberOfTypes = 0;
			while (numberOfTypes < 2) {
				numberOfTypes = Random.nextInt(10);
			}
			int populationSize = 0;
			while (populationSize < 1 || populationSize%2!=0) {
				populationSize = Random.nextInt(20);
			}
			// everybody is a one.
			Agent agent1 = new AgentSimple(1);
			Agent[] agentArray = new Agent[populationSize];
			for (int i = 0; i < agentArray.length; i++) {
				agentArray[i] = agent1;
			}
			AgentBasedPopulationImpl population = new AgentBasedPopulationImpl(
					agentArray);
			// now fitness calculator
			AgentBasedPayoffCalculator payoffCalculator = new EveryBodyGetsOnePayoffCalculator();
			// and mutator
			AgentMutator mutator = new AgentMutatorSimpleKernel(
					ArrayUtils.uniformMutationKernel(mutationProbablity,
							numberOfTypes));
			// the process itself
			AgentBasedWrightFisherProcessWithAssortment wf = new AgentBasedWrightFisherProcessWithAssortment(
					population, payoffCalculator,
					PayoffToFitnessMapping.EXPONENTIAL, intensityOfSelection,
					mutator, r);
			for (int i = 0; i < 10000; i++) {
				wf.step();
				Assert.assertEquals(populationSize, wf.getPopulation()
						.getSize());
			}
		}
	}

	@Test
	public void testTotalPayoff() {
		// since everybody gets one total payoff must be equal to popSize
		Random.seed(null);
		double intensityOfSelection = 1.0;
		double mutationProbablity = 0.1;
		double r = 0.0;
		// we repeat it 5 times with random numberOftypes and popSize
		for (int repetitions = 0; repetitions < 5; repetitions++) {
			int numberOfTypes = 0;
			while (numberOfTypes < 2) {
				numberOfTypes = Random.nextInt(10);
			}
			int populationSize = 0;
			while (populationSize < 1 || populationSize%2!=0) {
				populationSize = Random.nextInt(20);
			}
			// everybody is a one.
			Agent agent1 = new AgentSimple(1);
			Agent[] agentArray = new Agent[populationSize];
			for (int i = 0; i < agentArray.length; i++) {
				agentArray[i] = agent1;
			}
			AgentBasedPopulationImpl population = new AgentBasedPopulationImpl(
					agentArray);
			// now fitness calculator
			AgentBasedPayoffCalculator payoffCalculator = new EveryBodyGetsOnePayoffCalculator();
			// and mutator
			AgentMutator mutator = new AgentMutatorSimpleKernel(
					ArrayUtils.uniformMutationKernel(mutationProbablity,
							numberOfTypes));
			// the process itself
			AgentBasedWrightFisherProcessWithAssortment wf = new AgentBasedWrightFisherProcessWithAssortment(
					population, payoffCalculator,
					PayoffToFitnessMapping.EXPONENTIAL, intensityOfSelection,
					mutator, r);
			for (int i = 0; i < 10000; i++) {
				wf.step();
				Assert.assertEquals(1.0 * populationSize,
						wf.getTotalPopulationPayoff());
			}
		}
	}

	@Test
	public void testIfATypeIsNotThereItNeveShowsUp() {
		// since everybody gets one total payoff must be equal to popSize
		Random.seed(null);
		double intensityOfSelection = 1.0;
		double mutationProbablity = 0.1;
		double r = 0.0;
		// we repeat it 5 times with random numberOftypes and popSize
		for (int repetitions = 0; repetitions < 5; repetitions++) {
			int numberOfTypes = 0;
			while (numberOfTypes < 2) {
				numberOfTypes = Random.nextInt(10);
			}
			int populationSize = 0;
			while (populationSize < 1 || populationSize%2!=0) {
				populationSize = Random.nextInt(20);
			}
			// everybody is a random type but never type 0.
			Agent[] agentArray = new Agent[populationSize];
			for (int i = 0; i < agentArray.length; i++) {
				int type = Random.nextInt(numberOfTypes);
				if (type == 0)
					type = 1;
				agentArray[i] = new AgentSimple(type);
			}
			AgentBasedPopulationImpl population = new AgentBasedPopulationImpl(
					agentArray);
			// now fitness calculator
			AgentBasedPayoffCalculator payoffCalculator = new EveryBodyGetsOnePayoffCalculator();
			// and mutator
			AgentMutator mutator = new AgentMutatorSimpleKernel(
					ArrayUtils.uniformMutationKernel(mutationProbablity,
							numberOfTypes));
			// the process itself
			AgentBasedWrightFisherProcessWithAssortment wf = new AgentBasedWrightFisherProcessWithAssortment(
					population, payoffCalculator,
					PayoffToFitnessMapping.EXPONENTIAL, intensityOfSelection,
					mutator, r);
			Agent agent0 = new AgentSimple(0);
			for (int i = 0; i < 10000; i++) {
				wf.stepWithoutMutation();
				// agent zero never shows up
				Assert.assertEquals(0,
						wf.getPopulation().getNumberOfCopies(agent0));
			}
		}

	}

	private class SimpleAdvantage implements AgentBasedPayoffCalculator {
		private double fitnessAdv = 1.0;
		private double fitnessOther = 0.0;
		private AgentSimple advantageous;

		public void calculatePayoffs(AgentBasedPopulation population) {
			for (int i = 0; i < population.getSize(); i++) {
				if (population.getAgent(i).equals(advantageous)) {
					population.setPayoffOfAgent(i, fitnessAdv);
				} else {
					population.setPayoffOfAgent(i, fitnessOther);
				}
			}
		}

		public SimpleAdvantage(int advantageousType) {
			super();
			advantageous = new AgentSimple(advantageousType);
		}
	}

	@Test
	public void testStrongSelection() {
		// since everybody gets one total payoff must be equal to popSize
		Random.seed(null);
		double intensityOfSelection = 10.0;
		double mutationProbablity = 0.1;
		double r = 0.0;
		int numberOfTypes = 2;

		// we repeat it 5 times with random numberOftypes and popSize
		for (int repetitions = 0; repetitions < 10; repetitions++) {
			int populationSize = 0;
			// pop size is random between 10 and 20
			while (populationSize < 10 || populationSize%2!=0) {
				populationSize = Random.nextInt(20);
			}
			// everybody is a random type
			Agent[] agentArray = new Agent[populationSize];
			for (int i = 0; i < agentArray.length; i++) {
				int type = Random.nextInt(numberOfTypes);
				agentArray[i] = new AgentSimple(type);
			}
			AgentBasedPopulationImpl population = new AgentBasedPopulationImpl(
					agentArray);
			// now fitness calculator
			int advantageousType = Random.nextInt(numberOfTypes);
			AgentBasedPayoffCalculator payoffCalculator = new SimpleAdvantage(
					advantageousType);
			// and mutator
			AgentMutator mutator = new AgentMutatorSimpleKernel(
					ArrayUtils.uniformMutationKernel(mutationProbablity,
							numberOfTypes));
			// the process itself
			AgentBasedWrightFisherProcessWithAssortment wf = new AgentBasedWrightFisherProcessWithAssortment(
					population, payoffCalculator,
					PayoffToFitnessMapping.EXPONENTIAL, intensityOfSelection,
					mutator, r);
			Agent shouldFixateInto = new AgentSimple(advantageousType);
			// go to fixation
			boolean stop = false;
			while (!stop) {
				wf.stepWithoutMutation();
				stop = wf.getPopulation().getSetOfAgents().size() == 1;
			}
			Assert.assertEquals(wf.getPopulation().getAgent(0),
					shouldFixateInto);
		}
	}

	@Test
	public void testFulLAsortment() {

		// neighbours should be the same strategy
		Random.seed(null);
		double intensityOfSelection = 0.5;
		double mutationProbablity = 0.1;
		double r = 1.0;
		int numberOfTypes = 2;

		// we repeat it 5 times with random numberOftypes and popSize
		for (int repetitions = 0; repetitions < 10; repetitions++) {
			int populationSize = 0;
			// pop size is random between 10 and 20
			while (populationSize < 10 || populationSize%2!=0) {
				populationSize = Random.nextInt(20);
			}
			// everybody is a random type
			Agent[] agentArray = new Agent[populationSize];
			for (int i = 0; i < agentArray.length; i++) {
				int type = Random.nextInt(numberOfTypes);
				agentArray[i] = new AgentSimple(type);
			}
			AgentBasedPopulationImpl population = new AgentBasedPopulationImpl(
					agentArray);
			AgentBasedPayoffCalculator payoffCalculator = new AgentMatrixBasedPayoffCalculator(
					Games.hawkDoveGame());
			// and mutator
			AgentMutator mutator = new AgentMutatorSimpleKernel(
					ArrayUtils.uniformMutationKernel(mutationProbablity,
							numberOfTypes));
			// the process itself
			AgentBasedWrightFisherProcessWithAssortment wf = new AgentBasedWrightFisherProcessWithAssortment(
					population, payoffCalculator,
					PayoffToFitnessMapping.EXPONENTIAL, intensityOfSelection,
					mutator, r);
			while(wf.getTimeStep() < 10000 || wf.getPopulation().getSetOfAgents().size() > 1) {
				wf.stepWithoutMutation();
				for (int j = 0; j < wf.getPopulation().getSize() - 1; j = j + 2) {
					Assert.assertEquals(wf.getPopulation().getAgent(j), wf.getPopulation().getAgent(j + 1));
				}
			}

		}
	}
	
	
	@Test
	public void testAsortment() {

		// neighbours should be the same strategy
		Random.seed(null);
		double intensityOfSelection = 1.0;
		double mutationProbablity = 0.1;
		double r = 0.2;
		int numberOfTypes = 2;

		// we repeat it 5 times with random numberOftypes and popSize
		for (int repetitions = 0; repetitions < 1; repetitions++) {
			int populationSize = 100000;
			// everybody is a random type
			AgentBasedPopulation population = new AgentBasedSimpleRandomPopulationFactory(2, populationSize).createPopulation();
			AgentBasedPayoffCalculator payoffCalculator = new AgentMatrixBasedPayoffCalculator(Games.prionersDilemma(2.0, 0.0, 3.0, 1.0));
			// and mutator
			AgentMutator mutator = new AgentMutatorSimpleKernel(ArrayUtils.uniformMutationKernelWithSelfMutation(mutationProbablity,numberOfTypes));
			// the process itself
			AgentBasedWrightFisherProcessWithAssortment wf = new AgentBasedWrightFisherProcessWithAssortment(population, payoffCalculator,PayoffToFitnessMapping.LINEAR, intensityOfSelection,mutator, r);
			wf.stepWithoutMutation();
				int aaPairings = 0;
				int abPairings = 0;
				int bbPairings = 0;
				for (int j = 0; j < wf.getPopulation().getSize() - 1; j = j + 2) {
					if(wf.getPopulation().getAgent(j).equals(new AgentSimple(0)) && wf.getPopulation().getAgent(j + 1).equals(new AgentSimple(0))){
						aaPairings = aaPairings+1;	
					}
					if(wf.getPopulation().getAgent(j).equals(new AgentSimple(0)) && wf.getPopulation().getAgent(j + 1).equals(new AgentSimple(1))){
						abPairings = abPairings+1;	
					}
					if(wf.getPopulation().getAgent(j).equals(new AgentSimple(1)) && wf.getPopulation().getAgent(j + 1).equals(new AgentSimple(0))){
						abPairings = abPairings+1;	
					}
					if(wf.getPopulation().getAgent(j).equals(new AgentSimple(1)) && wf.getPopulation().getAgent(j + 1).equals(new AgentSimple(1))){
						bbPairings = bbPairings+1;	
					}
				}
				double conditionalProbability1 = (2.0*aaPairings)/(2.0*aaPairings + (double)abPairings);
				double conditionalProbability2 = ((double)abPairings)/(2.0*bbPairings + (double)abPairings);
				Assert.assertEquals(r, conditionalProbability1-conditionalProbability2, DELTA);

		}
	}
	

}
