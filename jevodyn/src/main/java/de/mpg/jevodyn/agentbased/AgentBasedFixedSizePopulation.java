package de.mpg.jevodyn.agentbased;

import java.util.Set;

public interface AgentBasedFixedSizePopulation {
	
	public void addOneIndividual(Agent agent, int position);
	public int getNumberOfCopies(Agent agent);
	public int getSize();
	public Set<Agent> getSetOfAgents();
	public Agent getAgent(int index);
	public double getPayoffOfAgent(int index);
	public void setPayoffOfAgent(int index, double payoff);
	public String toString();
	
	
}
