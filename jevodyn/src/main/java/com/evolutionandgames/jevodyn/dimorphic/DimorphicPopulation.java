package com.evolutionandgames.jevodyn.dimorphic;

public class DimorphicPopulation {
	
	private int numberOfMutants;
	private int typeOfMutant;
	private int typeOfResident;
	
	private final int populationSize;
	private final int numberOfTypes;
	
	public DimorphicPopulation(int populationSize, int typeOfResident,
			int numberOfTypes) {
		super();
		this.populationSize = populationSize;
		this.typeOfResident = typeOfResident;
		this.numberOfTypes = numberOfTypes;
		this.numberOfMutants = 0;
	}

	public int getNumberOfMutants() {
		return numberOfMutants;
	}
	
	public double getMutantFrequency() {
		return (double)numberOfMutants/this.populationSize;
	}
	
	public int getNumberOfResidents(){
		return this.populationSize - this.numberOfMutants;
	}
	
	public double getResidentFrequency() {
		return (double)numberOfMutants/this.populationSize;
	}

	public void incrementNumberOfMutants(){
		numberOfMutants++;
		if (numberOfMutants == populationSize) {
			this.numberOfMutants = 0;
			this.typeOfResident = typeOfMutant;
			this.typeOfMutant = -1;
		}else if(numberOfMutants > populationSize){
			throw new IllegalStateException("This is not possible");
		}		
	}
	
	public boolean isFixated(){
		return numberOfMutants == 0;
	}
	
	public void introduceNewMutant(int type){
		if (!isFixated() || type > this.numberOfTypes){
			throw new IllegalStateException("A new mutant has to be introduced when the population is monomorphous, new type has to be smaller than number of types");
		}
		this.incrementNumberOfMutants();
		this.typeOfMutant = type;
	}
	
	
	public void decrementNumberOfMutants(){
		numberOfMutants--;
		if(numberOfMutants < 0){
			throw new IllegalStateException("This is not possible");
		}		
	}
	

	public int getTypeOfMutant() {
		return typeOfMutant;
	}

	
	public int getTypeOfResident() {
		return typeOfResident;
	}

	public int getNumberOfTypes() {
		return numberOfTypes;
	}

	

}
