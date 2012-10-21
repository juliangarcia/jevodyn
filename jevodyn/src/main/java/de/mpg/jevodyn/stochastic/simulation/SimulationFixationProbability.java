package de.mpg.jevodyn.stochastic.simulation;

import de.mpg.jevodyn.stochastic.EvolutionaryProcess;
import de.mpg.jevodyn.stochastic.impl.SimplePopulationImpl;
import de.mpg.jevodyn.utils.Random;

public class SimulationFixationProbability {
	
	private EvolutionaryProcess process;
	
	public double estimate(int indexOfTheMutant, int indexOfTheIncumbent, int numberOfTypes, int populationSize, int numberOfSamples, Long seed){
	Random.seed(seed);
	int positives = 0;
	int[] startingPopulationArray = getStartingArray(indexOfTheMutant, indexOfTheIncumbent, numberOfTypes, populationSize);
	for (int i = 0; i < numberOfSamples; i++) {
		this.process.reset(new SimplePopulationImpl(startingPopulationArray));
		int fixatedOn = -1;
		while(fixatedOn ==-1){
			this.process.stepWithoutMutation();
			fixatedOn = this.process.getPopulation().getFixatedType();
		}
		if (fixatedOn==indexOfTheMutant) positives++;
	}
	return ((double)positives)/numberOfSamples;
	}

	public SimulationFixationProbability(EvolutionaryProcess process) {
		super();
		this.process = process;
	}

	private static int[] getStartingArray(int indexOfTheMutant,
			int indexOfTheIncumbent, int numberOfTypes,
			int populationSize) {
		int[] array = new int[numberOfTypes];
		array[indexOfTheIncumbent] = populationSize-1;
		array[indexOfTheMutant] = 1;
		return array;
	}
	
	
	
	

}
