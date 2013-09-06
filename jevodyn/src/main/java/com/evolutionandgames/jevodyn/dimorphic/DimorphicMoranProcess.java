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
        double fitnessResident = this.population.getNumberOfResidents()*mapFitness(payoffs[0], this.mapping);
        double fitnessMutant = this.population.getNumberOfMutants()*mapFitness(payoffs[1], this.mapping);
        boolean mutantBirth = Random.bernoulliTrial(fitnessMutant/(fitnessResident+fitnessMutant));
        boolean mutantDeath = Random.bernoulliTrial(this.population.getMutantFrequency());
        //(true true) and (false false ) cancel out and nothing happens 
        if (mutantBirth && !mutantDeath){
        	//true false
        	this.population.incrementNumberOfMutants();
        }else if (!mutantBirth && mutantDeath) {
        	//false true
        	this.population.decrementNumberOfMutants();
		}
        this.timeStep++;
    }
    
    
    
    

	private double mapFitness(double payoffValue, PayoffToFitnessMapping fitnessMapping) {
		switch (fitnessMapping) {
		case LINEAR:
			return 1.0 - this.intensityOfSelection + this.intensityOfSelection * payoffValue;
		case EXPONENTIAL:
			return Math.exp(intensityOfSelection * payoffValue);
		}
		throw new IllegalArgumentException("Inexistent payoff mapping");
	}

	public DimorphicMoranProcess(int timeStep, DimorphicPopulation population,
			PayoffToFitnessMapping mapping, double intensityOfSelection,
			double mutationProbability) {
		super();
		this.timeStep = timeStep;
		this.population = population;
		this.mapping = mapping;
		this.intensityOfSelection = intensityOfSelection;
		this.mutationProbability = mutationProbability;
		this.mutationKernel = ArrayUtils.uniformMutationKernel(
                this.mutationProbability, this.population.getNumberOfTypes());
        
	}

	public DimorphicMoranProcess(int timeStep, DimorphicPopulation population,
			PayoffToFitnessMapping mapping, double intensityOfSelection,
			RealMatrix mutationKernel) {
		super();
		this.timeStep = timeStep;
		this.population = population;
		this.mapping = mapping;
		this.intensityOfSelection = intensityOfSelection;
		this.mutationKernel = mutationKernel;
	}
	
	
	
}
