package com.evolutionandgames.jevodyn.dimorphic;

import org.apache.commons.math3.linear.RealMatrix;

import com.evolutionandgames.jevodyn.utils.ArrayUtils;
import com.evolutionandgames.jevodyn.utils.PayoffToFitnessMapping;
import com.evolutionandgames.jevodyn.utils.Random;

public class DimorphicMoranProcess {

	protected int timeStep;
	protected DimorphicPopulation population;
	protected PayoffToFitnessMapping mapping;
	protected double intensityOfSelection;
	protected double mutationProbability;
	protected RealMatrix mutationKernel;
	protected DimorphicPayoffCalculator payoffCalculator;

	public void step() {
		double[] payoffs = this.payoffCalculator.payoff(this.population);
		double fitnessResident = this.population.getNumberOfResidents()
				* mapFitness(payoffs[0], this.mapping);
		double fitnessMutant = this.population.getNumberOfMutants()
				* mapFitness(payoffs[1], this.mapping);
		double p = fitnessMutant / (fitnessResident + fitnessMutant);// mutantSelectedProbability
		double q = this.population.getMutantFrequency();//mutantForDeathProbability
		
		double probabilityMutantIncrement = p*(1.0-q);
		double probabilityMutantDecrement = q*(1.0-p);
		
		double random = Random.nextDouble();
		if (random < probabilityMutantIncrement) {
			this.population.incrementNumberOfMutants();
		}else if (random < probabilityMutantIncrement + probabilityMutantDecrement) {
			this.population.decrementNumberOfMutants();
		}
		this.timeStep++;
	}

	private double mapFitness(double payoffValue,
			PayoffToFitnessMapping fitnessMapping) {
		switch (fitnessMapping) {
		case LINEAR:
			return 1.0 - this.intensityOfSelection + this.intensityOfSelection
					* payoffValue;
		case EXPONENTIAL:
			return Math.exp(intensityOfSelection * payoffValue);
		}
		throw new IllegalArgumentException("Inexistent payoff mapping");
	}

	public DimorphicMoranProcess(DimorphicPopulation population,
			PayoffToFitnessMapping mapping, double intensityOfSelection,
			double mutationProbability,
			DimorphicPayoffCalculator payoffCalculator) {
		super();
		this.timeStep = 0;
		this.population = population;
		this.mapping = mapping;
		this.intensityOfSelection = intensityOfSelection;
		this.mutationProbability = mutationProbability;
		this.mutationKernel = ArrayUtils.uniformMutationKernel(
				this.mutationProbability, this.population.getNumberOfTypes());
		this.payoffCalculator = payoffCalculator;

	}

	public DimorphicMoranProcess(DimorphicPopulation population,
			PayoffToFitnessMapping mapping, double intensityOfSelection,
			RealMatrix mutationKernel,
			DimorphicPayoffCalculator payoffCalculator) {
		super();
		this.timeStep = 0;
		this.population = population;
		this.mapping = mapping;
		this.intensityOfSelection = intensityOfSelection;
		this.mutationKernel = mutationKernel;
		this.payoffCalculator = payoffCalculator;
	}

	public double[] estimateStationaryDistributionSmallMutation(
			int burningTimePerEstimate, int samplesPerEstimate,
			int numberOfEstimates, Long seed) {
		Random.seed(seed);
		int numberOfTypes = this.population.getNumberOfTypes();
		int populationSize = this.population.getSize();
		long[] countPerStrategy = new long[numberOfTypes];
		for (int estimate = 0; estimate < numberOfEstimates; estimate++) {
			// for every new esimate start at a random type
			int typeOfResident = Random.nextInt(numberOfTypes);
			this.population = new DimorphicPopulation(populationSize,
					typeOfResident, numberOfTypes);
			this.timeStep = 0;
			// burning time
			this.burn(burningTimePerEstimate);
			int samples = 0;
			// run to fixation and count
			while (samples < samplesPerEstimate) {
				while (!this.population.isFixated()) {
					countPerStrategy[this.population.getTypeOfResident()] = countPerStrategy[this.population
							.getTypeOfResident()]
							+ this.population.getNumberOfResidents();
					countPerStrategy[this.population.getTypeOfMutant()] = countPerStrategy[this.population
							.getTypeOfMutant()]
							+ this.population.getNumberOfMutants();
					this.step();
					samples++;
				}
				if (samples > samplesPerEstimate)
					break;
				// here we are fixated
				int residentType = this.population.getTypeOfResident();
				double escapeProbability = 1.0 - this.mutationKernel.getEntry(
						residentType, residentType);
				int escapeTime = Random
						.simulateGeometricDistribution(escapeProbability);
				samples = samples + escapeTime;
				countPerStrategy[this.population.getTypeOfResident()] = countPerStrategy[this.population
						.getTypeOfResident()]
						+ escapeTime
						* this.population.getSize();
				// add the mutant
				double[] distibutionGivenThatIJumpedOut = transformToConditional(
						mutationKernel.getRow(residentType), residentType);
				int mutantType = Random
						.simulateDiscreteDistribution(distibutionGivenThatIJumpedOut);
				this.population.introduceNewMutant(mutantType);
			}
		}
		return ArrayUtils.normalize(countPerStrategy);
	}

	private void burn(int burningTimePerEstimate) {
		int burningStep = 0;
		if (!this.population.isFixated())
			throw new IllegalStateException(
					"This method assumes a fixated population");
		while (burningStep < burningTimePerEstimate) {
			// compute and add time to escape
			int residentType = this.population.getTypeOfResident();
			double escapeProbability = 1.0 - this.mutationKernel.getEntry(
					residentType, residentType);
			int escapeTime = Random
					.simulateGeometricDistribution(escapeProbability);
			burningStep = burningStep + escapeTime;
			// add the mutant
			double[] distibutionGivenThatIJumpedOut = transformToConditional(
					mutationKernel.getRow(residentType), residentType);
			int mutantType = Random
					.simulateDiscreteDistribution(distibutionGivenThatIJumpedOut);
			this.population.introduceNewMutant(mutantType);
			// if burning time is not over, run to fixation or until burning
			// time is over
			while (!this.population.isFixated()
					&& burningStep < burningTimePerEstimate) {
				this.step();
				burningStep++;
			}
		}
	}

	private double[] transformToConditional(double[] row, int focalIndex) {
		double weight = 1.0 - row[focalIndex];
		double ans[] = new double[row.length];
		ans[focalIndex] = 0.0;
		for (int i = 0; i < ans.length; i++) {
			if (i != focalIndex) {
				ans[i] = row[i] / weight;
			}
		}
		return ans;
	}

}
