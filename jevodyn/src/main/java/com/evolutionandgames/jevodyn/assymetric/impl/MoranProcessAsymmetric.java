package com.evolutionandgames.jevodyn.assymetric.impl;

import org.apache.commons.math3.linear.RealMatrix;

import com.evolutionandgames.jevodyn.assymetric.AsymetricEvolutionaryProcess;
import com.evolutionandgames.jevodyn.assymetric.AsymmetricPayoffCalculator;
import com.evolutionandgames.jevodyn.assymetric.AsymmetricPopulation;
import com.evolutionandgames.jevodyn.utils.ArrayUtils;
import com.evolutionandgames.jevodyn.utils.PayoffToFitnessMapping;
import com.evolutionandgames.jevodyn.utils.Random;

public class MoranProcessAsymmetric implements AsymetricEvolutionaryProcess {

	protected boolean keepTrackOfTotalPayoff = true;

	public boolean isKeepTrackOfTotalPayoff() {
		return keepTrackOfTotalPayoff;
	}

	protected int timeStep;
	protected AsymmetricPopulation population;
	protected double totalPopulationPayoff;
	protected AsymmetricPayoffCalculator payoffCalculator;
	protected PayoffToFitnessMapping mapping;
	protected double intensityOfSelection;
	protected RealMatrix mutationKernel;
	protected double mutationProbability;

	protected void step(boolean mutationStep) {
		double[] currentFrequencies1 = this.population.getAsTypeFrequencies1();
		double[] currentFrequencies2 = this.population.getAsTypeFrequencies2();
		
		double[] payoffVector1 = this.payoffCalculator
				.getPayoff1(this.population);
		
		double[] payoffVector2 = this.payoffCalculator
				.getPayoff2(this.population);
		
		
		
		if (keepTrackOfTotalPayoff) {
			this.totalPopulationPayoff = ArrayUtils.dot(payoffVector1,
					this.population.getAsArrayOfTypes1()) + ArrayUtils.dot(payoffVector2,
							this.population.getAsArrayOfTypes2());
		}
		
		
		
		double[] fitness1 = new double[population.getNumberOfTypes()];
		double[] fitness2 = new double[population.getNumberOfTypes()];
		
		
		// Map fitness
		
		double totalFitness1 = 0;
		double totalFitness2 = 0;
		
		switch (mapping) {
		case LINEAR:
			for (int i = 0; i < fitness1.length; i++) {
				fitness1[i] = (currentFrequencies1[i])
						* (1.0 - intensityOfSelection + intensityOfSelection
								* payoffVector1[i]);
				totalFitness1 += fitness1[i]; 
			}
			for (int i = 0; i < fitness2.length; i++) {
				fitness2[i] = (currentFrequencies2[i])
						* (1.0 - intensityOfSelection + intensityOfSelection
								* payoffVector2[i]);
				totalFitness2 += fitness2[i];
			}
			
			break;
		case EXPONENTIAL:
			for (int i = 0; i < fitness1.length; i++) {
				fitness1[i] = (currentFrequencies1[i])
						* (Math.exp(intensityOfSelection * payoffVector1[i]));
				totalFitness1 += fitness1[i];
			}
			for (int i = 0; i < fitness2.length; i++) {
				fitness2[i] = (currentFrequencies2[i])
						* (Math.exp(intensityOfSelection * payoffVector2[i]));
				totalFitness2 += fitness2[i];
			}
			break;
		default:
			throw new IllegalArgumentException("Fitness mapping"
					+ mapping.toString() + "is not implemented ");

		}
		
		// Choose which population will reproduce
		boolean population1Reproduces = Random.bernoulliTrial(totalFitness1/(totalFitness1+totalFitness2));
		
		if (population1Reproduces) {
			fitness1 = ArrayUtils.normalize(fitness1);
			int chosenOne = Random.simulateDiscreteDistribution(fitness1);
			if (mutationStep) {
				chosenOne = Random.simulateDiscreteDistribution(mutationKernel
						.getRow(chosenOne));
			}
			int dies = Random.simulateDiscreteDistribution(currentFrequencies1);
			this.population.removeOneIndividual1(dies);
			this.population.addOneIndividual1(chosenOne);
			
			
		} else {
			fitness2 = ArrayUtils.normalize(fitness2);
			int chosenOne = Random.simulateDiscreteDistribution(fitness2);
			if (mutationStep) {
				chosenOne = Random.simulateDiscreteDistribution(mutationKernel
						.getRow(chosenOne));
			}
			int dies = Random.simulateDiscreteDistribution(currentFrequencies2);
			this.population.removeOneIndividual2(dies);
			this.population.addOneIndividual2(chosenOne);
		}
		
		//
		
		this.timeStep++;
	}

	public void step() {
		this.step(true);
	}

	public void stepWithoutMutation() {
		this.step(false);
	}

	public double getTotalPopulationPayoff() {
		if (!this.keepTrackOfTotalPayoff)
			throw new IllegalStateException(
					"Total payoff is being requested, but not kept track of. Please set MoranProcess.KEEP_TRACK_OF_TOTAL_PAYOFF to true ");
		return this.totalPopulationPayoff;
	}

	public MoranProcessAsymmetric(AsymmetricPopulation population,
			AsymmetricPayoffCalculator payoffCalculator, PayoffToFitnessMapping mapping,
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

	public MoranProcessAsymmetric(AsymmetricPopulation population,
			AsymmetricPayoffCalculator payoffCalculator, RealMatrix mutationKernel,
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

	public MoranProcessAsymmetric(AsymmetricPopulation population,
			AsymmetricPayoffCalculator payoffCalculator, PayoffToFitnessMapping mapping,
			double mutationProbability, double intensityOfSelection) {
		super();
		this.timeStep = 0;
		this.population = population;
		this.payoffCalculator = payoffCalculator;
		this.mapping = mapping;
		this.intensityOfSelection = intensityOfSelection;
		this.mutationProbability = mutationProbability;
		this.mutationKernel = ArrayUtils.uniformMutationKernel(
				this.mutationProbability, this.population
						.getNumberOfTypes());
		this.totalPopulationPayoff = -1.0;
	}

	public MoranProcessAsymmetric(AsymmetricPopulation population,
			AsymmetricPayoffCalculator payoffCalculator, double mutationProbability,
			double intensityOfSelection) {
		super();
		this.timeStep = 0;
		this.population = population;
		this.payoffCalculator = payoffCalculator;
		this.mapping = PayoffToFitnessMapping.EXPONENTIAL;
		this.intensityOfSelection = intensityOfSelection;
		this.mutationProbability = mutationProbability;
		this.mutationKernel = ArrayUtils.uniformMutationKernel(
				this.mutationProbability, this.population
						.getNumberOfTypes());
		this.totalPopulationPayoff = -1.0;
	}

	public AsymmetricPopulation getPopulation() {
		return population;
	}

	public void reset(AsymmetricPopulation startingPopulation) {
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
