package de.mpg.jevodyn.agentbased.simple;

import de.mpg.jevodyn.agentbased.Agent;
import de.mpg.jevodyn.agentbased.AgentBasedFixedSizePopulation;
import de.mpg.jevodyn.agentbased.AgentBasedPopulationFactory;
import de.mpg.jevodyn.agentbased.impl.AgentBasedPopulationImpl;
import de.mpg.jevodyn.utils.Random;

public class SimplePopulationFactory implements AgentBasedPopulationFactory {
	
	private int numberOfTypes;
	private int populationSize;
	

	public AgentBasedFixedSizePopulation createPopulation() {
		Agent[] array = new Agent[populationSize];
		for (int i = 0; i < array.length; i++) {
			array[i] = new SimpleAgent(Random.nextInt(this.numberOfTypes));
		}
		return new AgentBasedPopulationImpl(array);
	}


	public SimplePopulationFactory(int numberOfTypes, int populationSize) {
		super();
		this.numberOfTypes = numberOfTypes;
		this.populationSize = populationSize;
	}



}
