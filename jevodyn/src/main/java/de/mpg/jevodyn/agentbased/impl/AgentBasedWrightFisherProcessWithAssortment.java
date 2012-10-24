package de.mpg.jevodyn.agentbased.impl;

import de.mpg.jevodyn.agentbased.Agent;
import de.mpg.jevodyn.agentbased.AgentBasedEvolutionaryProcess;
import de.mpg.jevodyn.agentbased.AgentBasedFixedSizePopulation;
import de.mpg.jevodyn.agentbased.AgentBasedPayoffCalculator;
import de.mpg.jevodyn.agentbased.Mutator;
import de.mpg.jevodyn.utils.ArrayUtils;
import de.mpg.jevodyn.utils.PayoffToFitnessMapping;
import de.mpg.jevodyn.utils.Random;

public class AgentBasedWrightFisherProcessWithAssortment implements
		AgentBasedEvolutionaryProcess {

	public static boolean KEEP_TRACK_OF_TOTAL_PAYOFF = true;
	private int timeStep;
	private AgentBasedFixedSizePopulation population;
	private double totalPopulationPayoff;
	private AgentBasedPayoffCalculator payoffCalculator;
	private PayoffToFitnessMapping mapping;
	private double intensityOfSelection;
	private Mutator mutator;
	private double r;

	public void step() {
		// first we compute payoff
		payoffCalculator.calculatePayoffs(this.population);
		// Map fitness
		double[] fitness = computeFitnessAndTotalPayoff();
		// turn fitness into a probability distribution
		fitness = ArrayUtils.normalize(fitness);
		// create new generation
		createNewGeneration(fitness);
		// apply mutation
		applyMutation();
		timeStep = timeStep + 1;
	}

	public void stepWithoutMutation() {
		// first we compute payoff
		payoffCalculator.calculatePayoffs(this.population);
		// Map fitness
		double[] fitness = computeFitnessAndTotalPayoff();
		// turn fitness into a probability distribution
		fitness = ArrayUtils.normalize(fitness);
		// create new generation
		createNewGeneration(fitness);
		timeStep = timeStep + 1;
	}

	private void applyMutation() {
		//everybody gets mutated, the probability of this happening is responsability of the mutator implementation
		for (int i = 0; i < population.getSize(); i++) {
				Agent thisAgent = this.population.getAgent(i);
				population.addOneIndividual(this.mutator.mutate(thisAgent), i);
		}

	}

	private void createNewGeneration(double[] fitness) {
		for (int i = 0; i < population.getSize(); i++) {
			if (isEven(i)) {
				// if the position is even, we sample the fitness distribution
				// to determine how we occupy it
				Agent fitAgent = this.population.getAgent(Random
						.simulateDiscreteDistribution(fitness));
				population.addOneIndividual(fitAgent, i);
			} else {
				// if the position is odd, we sample...
				// from the distribution with probability 1-r
				if (Random.bernoulliTrial(1.0 - this.r)) {
					Agent fitAgent = this.population.getAgent(Random
							.simulateDiscreteDistribution(fitness));
					population.addOneIndividual(fitAgent, i);
				} else {
					// from parent of the neightbor with probability r
					Agent brother = this.population.getAgent(i - 1);
					population.addOneIndividual(brother, i);
				}
			}
		}
	}

	private boolean isEven(int i) {
		return i % 2 == 0;
	}

	private double[] computeFitnessAndTotalPayoff() {
		this.totalPopulationPayoff = 0.0;
		double[] fitness = new double[this.population.getSize()];
		switch (mapping) {
		case LINEAR:
			for (int i = 0; i < fitness.length; i++) {
				double payoffAgent = population.getPayoffOfAgent(i);
				fitness[i] = 1.0 - intensityOfSelection + intensityOfSelection
						* payoffAgent;
				this.totalPopulationPayoff = this.totalPopulationPayoff
						+ payoffAgent;
			}
			break;
		case EXPONENTIAL:
			for (int i = 0; i < fitness.length; i++) {
				double payoffAgent = population.getPayoffOfAgent(i);
				fitness[i] = Math.exp(intensityOfSelection * payoffAgent);
				this.totalPopulationPayoff = this.totalPopulationPayoff
						+ payoffAgent;
			}
			break;
		default:
			throw new IllegalArgumentException("Fitness mapping"
					+ mapping.toString() + "is not implemented ");
		}
		return fitness;
	}

	public AgentBasedFixedSizePopulation getPopulation() {
		return this.population;
	}

	public void reset(AgentBasedFixedSizePopulation startingPopulation) {
		this.timeStep = 0;
		this.population = startingPopulation;
	}

	public double getTotalPopulationPayoff() {
		return this.totalPopulationPayoff;
	}

	public int getTimeStep() {
		return this.timeStep;
	}

	public AgentBasedWrightFisherProcessWithAssortment(
			AgentBasedFixedSizePopulation population,
			AgentBasedPayoffCalculator payoffCalculator,
			PayoffToFitnessMapping mapping, double intensityOfSelection,
			Mutator mutator, double r) {
		super();
		this.timeStep = 0;
		this.totalPopulationPayoff=0.0;
		this.population = population;
		this.payoffCalculator = payoffCalculator;
		this.mapping = mapping;
		this.intensityOfSelection = intensityOfSelection;
		this.mutator = mutator;
		this.r = r;
		if (population.getSize()%2 !=0) throw new IllegalArgumentException("This class asumes that the population size is even");
	}

}
