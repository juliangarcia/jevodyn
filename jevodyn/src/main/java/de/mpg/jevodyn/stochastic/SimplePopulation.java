package de.mpg.jevodyn.stochastic;

public interface SimplePopulation {

	/***
	 * Returns an array of integers, where every index corresponds to
	 * the number of types
	 * @return array of int. 
	 */
	public int[] getAsArrayOfTypes();
	public double[] getAsTypeFrequencies();
	public void addOneIndividual(int type);
	public void removeOneIndividual(int type);
	public int getNumberOfCopies(int type);
	public int getSize();
	public int getNumberOfTypes();
	
}
