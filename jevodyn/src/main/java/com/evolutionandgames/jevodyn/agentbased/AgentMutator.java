package com.evolutionandgames.jevodyn.agentbased;

/**
 * How mutations are coded
 * @author garcia
 *
 */
public interface AgentMutator {
	
	//in case of returning an agent different than the one being returned, a new copy must be created!!
	/**
	 * All agents are mutated, so if there is randomness they have to be handled by the implementation. 
	 * @param agent
	 * @return Agent
	 */
	public Agent mutate(Agent agent);
}
