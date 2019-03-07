package com.evolutionandgames.jevodyn.assymetric;


public interface AsymetricEvolutionaryProcess {
	
	public void step();
	public void stepWithoutMutation();
	public AsymmetricPopulation getPopulation();
	public void reset(AsymmetricPopulation startingPopulation);
	public double getTotalPopulationPayoff();
	public int getTimeStep(); 
	public void setKeepTrackTotalPayoff(boolean keepTrack);
	
}
