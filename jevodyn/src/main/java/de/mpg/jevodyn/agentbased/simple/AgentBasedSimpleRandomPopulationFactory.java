package de.mpg.jevodyn.agentbased.simple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.mpg.jevodyn.agentbased.Agent;
import de.mpg.jevodyn.agentbased.AgentBasedPopulation;
import de.mpg.jevodyn.agentbased.AgentBasedPopulationFactory;
import de.mpg.jevodyn.agentbased.impl.AgentBasedPopulationImpl;
import de.mpg.jevodyn.utils.ArrayUtils;
import de.mpg.jevodyn.utils.Random;

public class AgentBasedSimpleRandomPopulationFactory implements AgentBasedPopulationFactory {
	
	private int numberOfTypes;
	private int populationSize;
	private boolean edge = false;
	private Map<Integer, AgentSimple> cache = new HashMap<Integer, AgentSimple>();  
	

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
			AgentSimple agent = new AgentSimple(strategy);
			cache.put(strategy, agent);
			return agent;
		}		
	}


	private AgentBasedPopulation createEdgePopulation() {
		int[] array = ArrayUtils.randomEdge(numberOfTypes, populationSize);
		ArrayList<Agent> list = new ArrayList<Agent>();
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i]; j++) {
				list.add(getAgent(i));
			}
		}
		Agent[] arrayAgent = list.toArray(new AgentSimple[]{});
		return new AgentBasedPopulationImpl(arrayAgent);
	}


	public AgentBasedSimpleRandomPopulationFactory(int numberOfTypes, int populationSize) {
		super();
		this.numberOfTypes = numberOfTypes;
		this.populationSize = populationSize;
	}
	
	public AgentBasedSimpleRandomPopulationFactory(int numberOfTypes, int populationSize, boolean edge) {
		super();
		this.numberOfTypes = numberOfTypes;
		this.populationSize = populationSize;
		this.edge = edge;
	}



}
