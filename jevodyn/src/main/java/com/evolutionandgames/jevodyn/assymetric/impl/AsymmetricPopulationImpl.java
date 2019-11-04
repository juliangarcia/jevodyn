package com.evolutionandgames.jevodyn.assymetric.impl;

import java.util.ArrayList;
import java.util.Arrays;

import com.evolutionandgames.jevodyn.assymetric.AsymmetricPopulation;

public class AsymmetricPopulationImpl implements AsymmetricPopulation {
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(populationArray1);
		result = prime * result + Arrays.hashCode(populationArray2);
		result = prime * result + size1;
		result = prime * result + size2;
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
		AsymmetricPopulationImpl other = (AsymmetricPopulationImpl) obj;
		if (!Arrays.equals(populationArray1, other.populationArray1))
			return false;
		if (!Arrays.equals(populationArray2, other.populationArray2))
			return false;
		if (size1 != other.size1)
			return false;
		if (size2 != other.size2)
			return false;
		return true;
	}

	protected int[]	populationArray1;
	protected int[]	populationArray2;
	protected int size1;
	protected int size2;

	public int[] getAsArrayOfTypes1() {
		return this.populationArray1;
	}
	
	public int[] getAsArrayOfTypes2() {
		return this.populationArray2;
	}

	
	public void addOneIndividual1(int type) {
		this.populationArray1[type]++;
		this.size1++;
	}
	
	public void addOneIndividual2(int type) {
		this.populationArray2[type]++;
		this.size2++;
	}

	public void removeOneIndividual1(int type) {
		populationArray1[type]--;
		size1--;
	}
	
	
	public void removeOneIndividual2(int type) {
		populationArray2[type]--;
		size2--;
	}

	public AsymmetricPopulationImpl(int[] populationArray1, int[] populationArray2) {
		super();
		if (populationArray1.length != populationArray2.length) {
			throw new IllegalArgumentException("The number of types must be the same in both populations");
		}
		
		
		this.populationArray1 = populationArray1.clone();
		this.populationArray2 = populationArray2.clone();
		this.size1 = 0;
		for (int i = 0; i < populationArray1.length; i++) {
			this.size1=this.size1+this.populationArray1[i];
		}
		this.size2 = 0;
		for (int i = 0; i < populationArray2.length; i++) {
			this.size2=this.size2+this.populationArray2[i];
		}
	}
	
	public double[] getAsTypeFrequencies1() {
		double[] frequencies = new double[this.getNumberOfTypes()];
		for (int i = 0; i < frequencies.length; i++) {
			frequencies[i]=  ((float) populationArray1[i]) / this.getSize1();
		}
		return frequencies;
	}
	
	public double[] getAsTypeFrequencies2() {
		double[] frequencies = new double[this.getNumberOfTypes()];
		for (int i = 0; i < frequencies.length; i++) {
			frequencies[i]=  ((float) populationArray2[i]) / this.getSize2();
		}
		return frequencies;
	}

	public int getSize1() {
		return this.size1;
	}
	
	public int getSize2() {
		return this.size2;
	}
	
	public int getSize() {
		return this.size1 + this.size2;
	}

	
	public int getNumberOfTypes() {
		return this.populationArray1.length;
	}

	public int getNumberOfCopies1(int type) {
		return this.populationArray1[type];
	}
	
	public int getNumberOfCopies2(int type) {
		return this.populationArray2[type];
	}

	public int getFixatedType1() {
		for (int i = 0; i < this.populationArray1.length; i++) {
			if(this.populationArray1[i]==this.size1) return i;
			if (this.populationArray1[i] > 0 && this.populationArray1[i] < this.size1) {
				return -1;
			}
		}
		return -1;
	}
	
	public int getFixatedType2() {
		for (int i = 0; i < this.populationArray2.length; i++) {
			if(this.populationArray2[i]==this.size2) return i;
			if (this.populationArray2[i] > 0 && this.populationArray2[i] < this.size2) {
				return -1;
			}
		}
		return -1;
	}

	public ArrayList<Integer> getSupport1() {
		ArrayList<Integer> support = new ArrayList<Integer>();
		for (int i = 0; i < this.populationArray1.length; i++) {
			if(this.populationArray1[i]> 0) support.add(i);
		}
		return support;
	}
	
	public ArrayList<Integer> getSupport2() {
		ArrayList<Integer> support = new ArrayList<Integer>();
		for (int i = 0; i < this.populationArray2.length; i++) {
			if(this.populationArray2[i]> 0) support.add(i);
		}
		return support;
	}
	
	
}
