package com.evolutionandgames.jevodyn.impl;

import java.util.ArrayList;
import java.util.Arrays;

import com.evolutionandgames.jevodyn.SimplePopulation;

public class SimplePopulationImpl implements SimplePopulation {
	
	protected int[]	populationArray;
	protected int size;

	public int[] getAsArrayOfTypes() {
		return this.populationArray;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(populationArray);
		result = prime * result + size;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimplePopulationImpl other = (SimplePopulationImpl) obj;
		if (!Arrays.equals(populationArray, other.populationArray))
			return false;
		if (size != other.size)
			return false;
		return true;
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
			if (this.populationArray[i] > 0 && this.populationArray[i] < this.size) {
				return -1;
			}
		}
		return -1;
	}

	public ArrayList<Integer> getSupport() {
		ArrayList<Integer> support = new ArrayList<Integer>();
		for (int i = 0; i < this.populationArray.length; i++) {
			if(this.populationArray[i]> 0) support.add(i);
		}
		return support;
	}

}
