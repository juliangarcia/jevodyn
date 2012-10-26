package de.mpg.jevodyn.agentbased.impl;

import org.junit.Assert;
import org.junit.Test;

import de.mpg.jevodyn.agentbased.Agent;
import de.mpg.jevodyn.agentbased.AgentBasedPopulation;
import de.mpg.jevodyn.agentbased.AgentBasedPayoffCalculator;
import de.mpg.jevodyn.agentbased.AgentMutator;
import de.mpg.jevodyn.agentbased.simple.AgentMutatorSimpleKernel;
import de.mpg.jevodyn.agentbased.simple.AgentMatrixBasedPayoffCalculator;
import de.mpg.jevodyn.agentbased.simple.AgentSimple;
import de.mpg.jevodyn.utils.ArrayUtils;
import de.mpg.jevodyn.utils.Games;
import de.mpg.jevodyn.utils.PayoffToFitnessMapping;
import de.mpg.jevodyn.utils.Random;

public class AgentBasedWrightFisherProcessWithAssortmentTest {

	private class EveryBodyGetsOnePayoffCalculator implements
			AgentBasedPayoffCalculator {
		public void calculatePayoffs(AgentBasedPopulation population) {
			for (int i = 0; i < population.getSize(); i++) {
				population.setPayoffOfAgent(i, 1.0);
			}
		}
	}

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

}
