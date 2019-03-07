package com.evolutionandgames.jevodyn.assymetric;

import java.util.ArrayList;

public interface AsymmetricPopulation {

	/***
	 * Returns an array of integers, where every index corresponds to
	 * the number of types
	 * @return array of int. 
	 */
	public int[] getAsArrayOfTypes1();
	public int[] getAsArrayOfTypes2();
	public double[] getAsTypeFrequencies1();
	public double[] getAsTypeFrequencies2();
	public void addOneIndividual1(int type);
	public void addOneIndividual2(int type);
	public void removeOneIndividual1(int type);
	public void removeOneIndividual2(int type);
	public int getNumberOfCopies1(int type);
	public int getNumberOfCopies2(int type);
	public int getSize();
	public int getSize1();
	public int getSize2();
	public int getNumberOfTypes();
	public int getFixatedType1();
	public int getFixatedType2();
	public ArrayList<Integer> getSupport1();
	public ArrayList<Integer> getSupport2();
	
}
