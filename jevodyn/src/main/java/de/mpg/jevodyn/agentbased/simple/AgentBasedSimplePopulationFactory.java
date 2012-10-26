package de.mpg.jevodyn.agentbased.simple;

import de.mpg.jevodyn.agentbased.Agent;
import de.mpg.jevodyn.agentbased.AgentBasedPopulation;
import de.mpg.jevodyn.agentbased.AgentBasedPopulationFactory;
import de.mpg.jevodyn.agentbased.impl.AgentBasedPopulationImpl;



/***
 * AgentBasedPopulation factory, creates a simple population with given copies of the same Agent
 * @author garcia
 *
 */
public class AgentBasedSimplePopulationFactory implements
		AgentBasedPopulationFactory {
	
	private int populationSize;
	private Agent sampleAgent;
	
	

	@Override
	public AgentBasedPopulation createPopulation() {
		Agent[] agentArray = new AgentSimple[this.populationSize];
		for (int i = 0; i < agentArray.length; i++) {
			agentArray[i] = this.sampleAgent;
		}
		return new AgentBasedPopulationImpl(agentArray);
	}


	/***
	 * Initializes this factory
	 * @param populationSize 
	 * @param sampleAgent
	 */
	public AgentBasedSimplePopulationFactory(int populationSize,
			Agent sampleAgent) {
		super();
		this.populationSize = populationSize;
		this.sampleAgent = sampleAgent;
	}

}
