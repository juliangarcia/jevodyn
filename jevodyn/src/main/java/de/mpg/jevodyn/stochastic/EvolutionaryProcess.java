package de.mpg.jevodyn.stochastic;


public interface EvolutionaryProcess {
	
	public void step();
	public void stepWithoutMutation();
	public SimplePopulation getPopulation();
	public PayoffCalculator getPayoffCalculator();
	public void reset(SimplePopulation startingPopulation);
	public double getPopulationPayoff();
	
}
