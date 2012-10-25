package de.mpg.jevodyn.agentbased;

import java.util.Set;

/**
 * Interface for a population in an agent-based process. 
 * @author garcia
 *
 */
public interface AgentBasedPopulation {
	
	/**
	 * Adds a new individual. Since the population size is fixed, an index has to be given of the indidividual to be replaced.
	 * Index runs from 0 to population size minus one. 
	 * @param agent individual to add
	 * @param position index of the individual to replace
	 */
	public void addOneIndividual(Agent agent, int position);
	
	/**
	 * How many copies of a given agent. Couting relies on the equals() method of the agent implementation.
	 * @param agent
	 * @return
	 */
	public int getNumberOfCopies(Agent agent);
	/**
	 * Population size
	 * @return
	 */
	public int getSize();
	/**
	 * A set containing Agents, no repetitions.  If the set size is one the population has fixated.
	 * @return
	 */
	public Set<Agent> getSetOfAgents();
	/**
	 * Get th agent in a given position.
	 * @param index
	 * @return
	 */
	public Agent getAgent(int index);
	/**
	 * Get the payoff of a certain agent.
	 * @param index
	 * @return
	 */
	public double getPayoffOfAgent(int index);
	/**
	 * Set the payoff of an agent in a certain position. 
	 * @param index
	 * @param payoff
	 */
	public void setPayoffOfAgent(int index, double payoff);
	/**
	 * Gives a String representation of the population
	 * @return
	 */
	public String toString();
	/**
	 * Returns the array of agents composing the population
	 * @return
	 */
	public Agent[] getAsArrayOfAgents();
	
	
}
