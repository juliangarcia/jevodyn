package com.evolutionandgames.jevodyn.stochastic.impl;

import org.apache.commons.math3.linear.RealMatrix;

import com.evolutionandgames.jevodyn.stochastic.EvolutionaryProcess;
import com.evolutionandgames.jevodyn.stochastic.PayoffCalculator;
import com.evolutionandgames.jevodyn.stochastic.SimplePopulation;
import com.evolutionandgames.jevodyn.utils.ArrayUtils;
import com.evolutionandgames.jevodyn.utils.PayoffToFitnessMapping;
import com.evolutionandgames.jevodyn.utils.Random;


public class MoranProcess implements EvolutionaryProcess {

	private boolean keepTrackOfTotalPayoff = true;
	public boolean isKeepTrackOfTotalPayoff() {
		return keepTrackOfTotalPayoff;
	}

	private int timeStep;
	private SimplePopulation population;
	private double totalPopulationPayoff;
	private PayoffCalculator payoffCalculator;
	private PayoffToFitnessMapping mapping;
	private double intensityOfSelection;
	private RealMatrix mutationKernel;
	private double mutationProbability;

	protected void step(boolean mutationStep) {
		double[] currentFrequencies = this.population.getAsTypeFrequencies();
		double[] payoffVector = this.payoffCalculator
				.getPayoff(this.population);
		if (keepTrackOfTotalPayoff) {
			this.totalPopulationPayoff = ArrayUtils.dot(payoffVector,
					this.population.getAsArrayOfTypes());
		}
		double[] fitness = new double[population.getNumberOfTypes()];
		// Map fitness
		switch (mapping) {
		case LINEAR:
			for (int i = 0; i < fitness.length; i++) {
				fitness[i] = (currentFrequencies[i])*(1.0 - intensityOfSelection + intensityOfSelection
						* payoffVector[i]);
			}
			break;
		case EXPONENTIAL:
			for (int i = 0; i < fitness.length; i++) {
				fitness[i] = (currentFrequencies[i])*(Math.exp(intensityOfSelection * payoffVector[i]));
			}
			break;
		default:
			throw new IllegalArgumentException("Fitness mapping" + mapping.toString() + "is not implemented ");
			
		}
		fitness = ArrayUtils.normalize(fitness);
		int chosenOne = Random.simulateDiscreteDistribution(fitness);
		if (mutationStep) {
			chosenOne = Random.simulateDiscreteDistribution(mutationKernel
					.getRow(chosenOne));
		}
		int dies = Random.simulateDiscreteDistribution(currentFrequencies);
		this.population.removeOneIndividual(dies);
		this.population.addOneIndividual(chosenOne);
		this.timeStep++;
	}

	public void step() {
		this.step(true);
	} 

	public void stepWithoutMutation() {
		this.step(false);
	}

	public double getTotalPopulationPayoff() {
		if(!this.keepTrackOfTotalPayoff) throw new IllegalStateException("Total payoff is being requested, but not kept track of. Please set MoranProcess.KEEP_TRACK_OF_TOTAL_PAYOFF to true ");
		return this.totalPopulationPayoff;
	}

	public MoranProcess(SimplePopulation population,
			PayoffCalculator payoffCalculator, PayoffToFitnessMapping mapping,
			RealMatrix mutationKernel, double intensityOfSelection) {
		super();
		this.timeStep = 0;
		this.population = population;
		this.payoffCalculator = payoffCalculator;
		this.mapping = mapping;
		this.intensityOfSelection = intensityOfSelection;
		this.mutationKernel = mutationKernel;
		this.mutationProbability = -1.0;
		this.totalPopulationPayoff = -1.0;
	}

	public MoranProcess(SimplePopulation population,
			PayoffCalculator payoffCalculator, RealMatrix mutationKernel,
			double intensityOfSelection) {
		super();
		this.timeStep = 0;
		this.population = population;
		this.payoffCalculator = payoffCalculator;
		this.mapping = PayoffToFitnessMapping.EXPONENTIAL;
		this.intensityOfSelection = intensityOfSelection;
		this.mutationKernel = mutationKernel;
		this.mutationProbability = -1.0;
		this.totalPopulationPayoff = -1.0;
	}

	public MoranProcess(SimplePopulation population,
			PayoffCalculator payoffCalculator, PayoffToFitnessMapping mapping,
			double mutationProbability, double intensityOfSelection) {
		super();
		this.timeStep = 0;
		this.population = population;
		this.payoffCalculator = payoffCalculator;
		this.mapping = mapping;
		this.intensityOfSelection = intensityOfSelection;
		this.mutationProbability = mutationProbability;
		this.mutationKernel = ArrayUtils.uniformMutationKernel(
				this.mutationProbability, this.getPopulation()
						.getNumberOfTypes());
		this.totalPopulationPayoff = -1.0;
	}

	public MoranProcess(SimplePopulation population,
			PayoffCalculator payoffCalculator, double mutationProbability,
			double intensityOfSelection) {
		super();
		this.timeStep = 0;
		this.population = population;
		this.payoffCalculator = payoffCalculator;
		this.mapping = PayoffToFitnessMapping.EXPONENTIAL;
		this.intensityOfSelection = intensityOfSelection;
		this.mutationProbability = mutationProbability;
		this.mutationKernel = ArrayUtils.uniformMutationKernel(
				this.mutationProbability, this.getPopulation()
						.getNumberOfTypes());
		this.totalPopulationPayoff = -1.0;
	}

	public SimplePopulation getPopulation() {
		return population;
	}

	

	public void reset(SimplePopulation startingPopulation) {
		this.population = startingPopulation;
		this.timeStep = 0;
		this.totalPopulationPayoff = -1.0;
	}

	public int getTimeStep() {
		return timeStep;
	}

	public void setKeepTrackTotalPayoff(boolean keepTrack) {
		this.keepTrackOfTotalPayoff = keepTrack;
	}

	

}
