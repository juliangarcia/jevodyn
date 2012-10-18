package de.mpg.jevodyn.stochastic.impl;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import de.mpg.jevodyn.stochastic.EvolutionaryProcess;
import de.mpg.jevodyn.stochastic.PayoffCalculator;
import de.mpg.jevodyn.stochastic.SimplePopulation;
import de.mpg.jevodyn.utils.ArrayUtils;
import de.mpg.jevodyn.utils.PayoffToFitnessMapping;
import de.mpg.jevodyn.utils.Random;

public class MoranProcess implements EvolutionaryProcess {

	private int timeStep;
	private SimplePopulation population;
	private SummaryStatistics averagePayoff;
	private PayoffCalculator payoffCalculator;
	private PayoffToFitnessMapping mapping;
	private double intensityOfSelection;
	private RealMatrix mutationKernel;
	private double mutationProbability;
	

	protected void step(boolean mutationStep) {
		double[] currentFrequencies = this.population.getAsTypeFrequencies();
		double[] payoffVector = this.payoffCalculator.getPayoff(this.population);
		this.averagePayoff.addValue(StatUtils.sum(payoffVector));
		double[] fitness = new double[population.getNumberOfTypes()];
		//Map fitness
		switch (mapping) {
		case LINEAR:
			for (int i = 0; i < fitness.length; i++) {
				fitness[i]= 1.0 - intensityOfSelection + intensityOfSelection*payoffVector[i];
			}
			break;
		case EXPONENTIAL:
			for (int i = 0; i < fitness.length; i++) {
				fitness[i]= Math.exp(intensityOfSelection*payoffVector[i]);
			}
			break;
		}
		fitness = ArrayUtils.normalize(fitness);
		int chosenOne = Random.simulateDiscreteDistribution(fitness);
		if (mutationStep){
			chosenOne = Random.simulateDiscreteDistribution(mutationKernel.getRow(chosenOne));
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
	
	

	public MoranProcess(SimplePopulation population, PayoffCalculator payoffCalculator, PayoffToFitnessMapping mapping, RealMatrix mutationKernel, double intensityOfSelection) {
		super();
		this.timeStep = 0;
		this.population = population;
		this.payoffCalculator = payoffCalculator;
		this.mapping = mapping;
		this.intensityOfSelection = intensityOfSelection;
		this.mutationKernel = mutationKernel;
		this.mutationProbability = -1.0;
		this.averagePayoff = new SummaryStatistics();
	}
	
	
	public MoranProcess(SimplePopulation population, PayoffCalculator payoffCalculator, PayoffToFitnessMapping mapping, double mutationProbability, double intensityOfSelection) {
		super();
		this.timeStep = 0;
		this.population = population;
		this.payoffCalculator = payoffCalculator;
		this.mapping = mapping;
		this.intensityOfSelection = intensityOfSelection;
		this.mutationProbability = mutationProbability;
		this.mutationKernel = ArrayUtils.uniformMutationKernel(this.mutationProbability, this.getPopulation().getNumberOfTypes());
		this.averagePayoff = new SummaryStatistics();
	}
	
	 

	public SimplePopulation getPopulation() {
		return population;
	}

	public PayoffCalculator getPayoffCalculator() {
		return payoffCalculator;
	}

	public void reset(SimplePopulation startingPopulation) {
		this.population = startingPopulation;
		this.timeStep=0;
		this.averagePayoff.clear();
	}

	public int getTimeStep() {
		return timeStep;
	}

	public PayoffToFitnessMapping getMapping() {
		return mapping;
	}
	
	public double getIntensityOfSelection() {
		return intensityOfSelection;
	}

	

}
