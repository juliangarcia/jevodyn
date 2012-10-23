package de.mpg.jevodyn.agentbased;


public interface AgentBasedEvolutionaryProcess {
	
	public void step();
	public void stepWithoutMutation();
	public AgentBasedFixedSizePopulation getPopulation();
	public void reset(AgentBasedFixedSizePopulation startingPopulation);
	public double getTotalPopulationPayoff();
	public int getTimeStep();
	public void setKeepTrackTotalPayoff(boolean keepTrack);

}
