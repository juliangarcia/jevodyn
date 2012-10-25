package de.mpg.jevodyn.agentbased;

/**
 * Interface for agent based evolutionary processes. 
 * @author garcia
 *
 */
public interface AgentBasedEvolutionaryProcess {
	
	/**
	 * one step, changes the population accordingly.
	 */
	public void step();
	/**
	 * Step without innovation, the same agents remain only changing the frequencies. 
	 */
	public void stepWithoutMutation();
	/**
	 * Returns the population on which this process operates. 
	 * @return
	 */
	public AgentBasedPopulation getPopulation();
	/**
	 * Restart the population of the process, and set the timeStep to 0. 
	 * @param startingPopulation
	 */
	public void reset(AgentBasedPopulation startingPopulation);
	/**
	 * Total population payoff.
	 * @return
	 */
	public double getTotalPopulationPayoff();
	/**
	 * Get the current timestep. every step increaseas this number.
	 * @return
	 */
	public int getTimeStep();
	
}
