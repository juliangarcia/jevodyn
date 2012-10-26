package de.mpg.jevodyn.agentbased.simple;

import java.util.HashMap;
import java.util.Map;

import de.mpg.jevodyn.agentbased.Agent;
import de.mpg.jevodyn.agentbased.AgentBasedPopulation;
import de.mpg.jevodyn.agentbased.AgentBasedPopulationFactory;
import de.mpg.jevodyn.agentbased.impl.AgentBasedPopulationImpl;
import de.mpg.jevodyn.utils.ArrayUtils;
import de.mpg.jevodyn.utils.Random;

public class SimplePopulationFactory implements AgentBasedPopulationFactory {
	
	private int numberOfTypes;
	private int populationSize;
	private boolean edge = false;
	private Map<Integer, SimpleAgent> cache = new HashMap<Integer, SimpleAgent>();  
	

	public AgentBasedPopulation createPopulation() {
		if(edge) return createEdgePopulation();
		return createRandomPopulation();
	}


	private AgentBasedPopulation createRandomPopulation() {
		Agent[] array = new Agent[populationSize];
		for (int i = 0; i < array.length; i++) {
			int strategy = Random.nextInt(this.numberOfTypes);
			array[i] = getAgent(strategy);
		}
		return new AgentBasedPopulationImpl(array);
	}


	private Agent getAgent(int strategy) {
		// check if it is in the map, otherwise create it and put it in the map
		if (cache.containsKey(strategy)){
			return cache.get(strategy);
		}else{
			SimpleAgent agent = new SimpleAgent(strategy);
			cache.put(strategy, agent);
			return agent;
		}		
	}


	private AgentBasedPopulation createEdgePopulation() {
		int[] array = ArrayUtils.randomEdge(numberOfTypes, populationSize);
		Agent[] arrayAgent = new Agent[array.length];
		for (int i = 0; i < array.length; i++) {
			arrayAgent[i] = getAgent(array[i]);
		}
		return new AgentBasedPopulationImpl(arrayAgent);
	}


	public SimplePopulationFactory(int numberOfTypes, int populationSize) {
		super();
		this.numberOfTypes = numberOfTypes;
		this.populationSize = populationSize;
	}
	
	public SimplePopulationFactory(int numberOfTypes, int populationSize, boolean edge) {
		super();
		this.numberOfTypes = numberOfTypes;
		this.populationSize = populationSize;
		this.edge = edge;
	}



}
