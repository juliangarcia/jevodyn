package com.evolutionandgames.jevodyn.impl;

import com.evolutionandgames.jevodyn.SimplePopulation;

public class SimplePopulationImpl implements SimplePopulation {
	
	protected int[]	populationArray;
	protected int size;

	public int[] getAsArrayOfTypes() {
		return this.populationArray;
	}

	public void addOneIndividual(int type) {
		this.populationArray[type]++;
		this.size++;
	}

	public void removeOneIndividual(int type) {
		populationArray[type]--;
		size--;
	}

	public SimplePopulationImpl(int[] populationArray) {
		super();
		this.populationArray = populationArray.clone();
		this.size = 0;
		for (int i = 0; i < populationArray.length; i++) {
			this.size=this.size+this.populationArray[i];
		}
	}
	
	public double[] getAsTypeFrequencies() {
		double[] frequencies = new double[this.getNumberOfTypes()];
		for (int i = 0; i < frequencies.length; i++) {
			frequencies[i]=  ((float) populationArray[i]) / this.getSize();
		}
		return frequencies;
	}

	public int getSize() {
		return this.size;
	}

	public int getNumberOfTypes() {
		return this.populationArray.length;
	}

	public int getNumberOfCopies(int type) {
		return this.populationArray[type];
	}

	public int getFixatedType() {
		for (int i = 0; i < this.populationArray.length; i++) {
			if(this.populationArray[i]==this.size) return i;
		}
		return -1;
	}

}
