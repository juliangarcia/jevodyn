package com.evolutionandgames.jevodyn.impl;

import org.apache.commons.math3.linear.RealMatrix;

import com.evolutionandgames.jevodyn.PayoffCalculator;
import com.evolutionandgames.jevodyn.SimplePopulation;
import com.evolutionandgames.jevodyn.utils.ArrayUtils;
import com.evolutionandgames.jevodyn.utils.PayoffToFitnessMapping;
import com.evolutionandgames.jevodyn.utils.Random;

public class MoranProcessFast extends MoranProcess {

	public MoranProcessFast(SimplePopulation population,
			PayoffCalculator payoffCalculator, double mutationProbability,
			double intensityOfSelection) {
		super(population, payoffCalculator, mutationProbability, intensityOfSelection);
	
	}

	public MoranProcessFast(SimplePopulation population,
			PayoffCalculator payoffCalculator, PayoffToFitnessMapping mapping,
			double mutationProbability, double intensityOfSelection) {
		super(population, payoffCalculator, mapping, mutationProbability,
				intensityOfSelection);

	}

	public MoranProcessFast(SimplePopulation population,
			PayoffCalculator payoffCalculator, PayoffToFitnessMapping mapping,
			RealMatrix mutationKernel, double intensityOfSelection) {
		super(population, payoffCalculator, mapping, mutationKernel,
				intensityOfSelection);

	}

	public MoranProcessFast(SimplePopulation population,
			PayoffCalculator payoffCalculator, RealMatrix mutationKernel,
			double intensityOfSelection) {
		super(population, payoffCalculator, mutationKernel, intensityOfSelection);

	}

	/* (non-Javadoc)
	 * @see com.evolutionandgames.jevodyn.impl.MoranProcess#step(boolean)
	 */
	@Override
	protected void step(boolean mutationStep) {
		if (mutationStep && population.getNumberOfTypes() == 1)
		{
			int dies = population.getFixatedType();
			
			double probability_nothing_happens = mutationKernel.getEntry(dies, dies);
			int time = Random.simulateGeometricDistribution(1.0 - probability_nothing_happens);
			this.timeStep = this.timeStep + time;
			// now introduce new mutant
			double[] distribution_src = mutationKernel.getRow(dies);
			double[] dest = new double[distribution_src.length];
			System.arraycopy( distribution_src, 0, dest, 0, distribution_src.length );
			dest[dies] = 0.0;
			dest = ArrayUtils.normalize(dest);
			int chosenOne = Random.simulateDiscreteDistribution(dest);
			this.population.removeOneIndividual(dies);
			this.population.addOneIndividual(chosenOne);
		
			
		}else{
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
			
		
		
		
		
	}
	
	

	


	
	
}
