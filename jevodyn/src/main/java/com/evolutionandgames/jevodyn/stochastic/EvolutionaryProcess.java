package com.evolutionandgames.jevodyn.stochastic;


public interface EvolutionaryProcess {
	
	public void step();
	public void stepWithoutMutation();
	public SimplePopulation getPopulation();
	public void reset(SimplePopulation startingPopulation);
	public double getTotalPopulationPayoff();
	public int getTimeStep(); 
	public void setKeepTrackTotalPayoff(boolean keepTrack);
	
}
