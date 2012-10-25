package de.mpg.jevodyn.agentbased;

/**
 * This interface defines how the payoff should be specified. Given a population,
 * the fitness values should be set inside after the method has concluded. 
 * @author garcia
 *
 */
public interface AgentBasedPayoffCalculator {
	
	void calculatePayoffs(AgentBasedPopulation population);
}
