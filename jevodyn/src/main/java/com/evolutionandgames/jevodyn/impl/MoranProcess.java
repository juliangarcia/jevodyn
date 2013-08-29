package com.evolutionandgames.jevodyn.impl;

import org.apache.commons.math3.linear.RealMatrix;

import com.evolutionandgames.jevodyn.EvolutionaryProcess;
import com.evolutionandgames.jevodyn.PayoffCalculator;
import com.evolutionandgames.jevodyn.SimplePopulation;
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
				fitness[i] = (currentFrequencies[i])
						* (1.0 - intensityOfSelection + intensityOfSelection
								* payoffVector[i]);
			}
			break;
		case EXPONENTIAL:
			for (int i = 0; i < fitness.length; i++) {
				fitness[i] = (currentFrequencies[i])
						* (Math.exp(intensityOfSelection * payoffVector[i]));
			}
			break;
		default:
			throw new IllegalArgumentException("Fitness mapping"
					+ mapping.toString() + "is not implemented ");

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
		if (!this.keepTrackOfTotalPayoff)
			throw new IllegalStateException(
					"Total payoff is being requested, but not kept track of. Please set MoranProcess.KEEP_TRACK_OF_TOTAL_PAYOFF to true ");
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

	public double[] estimateStationaryDistributionSmallMutation(
			int burningTimePerEstimate, int samplesPerEstimate,
			int numberOfEstimates, Long seed) {
		Random.seed(seed);
		int numberOfTypes = this.getPopulation().getNumberOfTypes();
		int populationSize = this.getPopulation().getSize();
		this.setKeepTrackTotalPayoff(false);
		long[] countPerStrategy = new long[numberOfTypes];
		for (int estimate = 0; estimate < numberOfEstimates; estimate++) {
			this.reset(new SimplePopulationImpl(ArrayUtils.randomMonomorphous(numberOfTypes, populationSize)));
			int fixated = this.population.getFixatedType();
			int burningStep = 0;
			int sample = 0;
			// fixated but burning time is not over
			while (burningStep < burningTimePerEstimate) {
				double leavingHomogeneousStateInOneMutationProbability = 1.0 - this.mutationKernel
						.getEntry(fixated, fixated);
				int timeTillLeaving = Random
						.simulateGeometricDistribution(leavingHomogeneousStateInOneMutationProbability);
				burningStep = burningStep + timeTillLeaving;
				
				// introduce mutant and do steps until homogeneous
				double[] distibutionGivenThatIJumpedOut = transformToConditional(mutationKernel.getRow(fixated), fixated);
				int chosenOne = Random.simulateDiscreteDistribution(distibutionGivenThatIJumpedOut);
				this.population.addOneIndividual(chosenOne);
				this.population.removeOneIndividual(fixated);
				// now is not homogeneous any more so we run until fixation (no  mutations)
				fixated = -1;
				if (burningStep >= burningTimePerEstimate){
					break;
				}
				while (fixated == -1 && burningStep < burningTimePerEstimate) {
					this.stepWithoutMutation();
					burningStep++;
					fixated = this.population.getFixatedType();
				}
				// fixation, so I start again
			}
			
			// done with burning but maybe not fixated 
			//then run to fixation without mutation
			fixated = this.population.getFixatedType();
			while (fixated == -1) {
				this.stepWithoutMutation();
				sample++;
				fixated = this.population.getFixatedType();
				for (int i = 0; i < numberOfTypes; i++) {
					countPerStrategy[i] = countPerStrategy[i]
							+ this.getPopulation().getAsArrayOfTypes()[i];
				}
				if (sample>=samplesPerEstimate) break;
			}
			// DONE WITH BURNING and it is FIXATED so sample!
			while (sample< samplesPerEstimate) {
				double leavingHomogeneousStateInOneMutationProbability = 1.0 - this.mutationKernel
						.getEntry(fixated, fixated);
				int timeTillLeaving = Random
						.simulateGeometricDistribution(leavingHomogeneousStateInOneMutationProbability);
				sample = sample + timeTillLeaving;
				//count
				countPerStrategy[fixated] = countPerStrategy[fixated] + populationSize*timeTillLeaving;
				// introduce mutant and do steps until homogeneous
				double[] distibutionGivenThatIJumpedOut = transformToConditional(mutationKernel.getRow(fixated), fixated);
				int chosenOne = Random.simulateDiscreteDistribution(distibutionGivenThatIJumpedOut);
				this.population.addOneIndividual(chosenOne);
				this.population.removeOneIndividual(fixated);
				// now is not homogeneous any more so we run until fixation (no
				// mutations)
				int previousResident = fixated;
				fixated = -1;
				while (fixated == -1) {
					this.stepWithoutMutation();
					sample++;
					fixated = this.population.getFixatedType();
					//count (only two present!)
					countPerStrategy[chosenOne] = countPerStrategy[chosenOne]
							+ this.getPopulation().getAsArrayOfTypes()[chosenOne];
					countPerStrategy[previousResident] = countPerStrategy[previousResident]
							+ this.getPopulation().getAsArrayOfTypes()[previousResident];
					if (sample>=samplesPerEstimate) break;
				}
				// fixation, so I start again
				//unless I am done
				if (sample>=samplesPerEstimate) break;
			}
		}
		return ArrayUtils.normalize(countPerStrategy);
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
