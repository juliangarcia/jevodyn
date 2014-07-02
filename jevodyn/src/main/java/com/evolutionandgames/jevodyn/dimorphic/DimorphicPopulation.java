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
		this.typeOfMutant = -1;
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
		if (this.typeOfMutant == -1) {
			throw new IllegalStateException("There are no mutants in this population");
		}
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
		this.typeOfMutant = type;
		this.incrementNumberOfMutants();
	}
	
	
	public void decrementNumberOfMutants(){
		numberOfMutants--;
		if(numberOfMutants == 0){
			this.typeOfMutant = -1;
		}
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

	public int getSize() {
		return populationSize;
	}

	@Override
	public String toString() {
		return "DimorphicPopulation [mutants=" + numberOfMutants + "," + "residents =" + getNumberOfResidents()
					+ ", typeOfMutant=" + typeOfMutant + ", typeOfResident="
					+ typeOfResident + ", types=" + numberOfTypes + "]";	
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numberOfMutants;
		result = prime * result + numberOfTypes;
		result = prime * result + populationSize;
		result = prime * result + typeOfMutant;
		result = prime * result + typeOfResident;
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
		DimorphicPopulation other = (DimorphicPopulation) obj;
		if (numberOfMutants != other.numberOfMutants)
			return false;
		if (numberOfTypes != other.numberOfTypes)
			return false;
		if (populationSize != other.populationSize)
			return false;
		if (typeOfMutant != other.typeOfMutant)
			return false;
		if (typeOfResident != other.typeOfResident)
			return false;
		return true;
	}

	private DimorphicPopulation(int numberOfMutants, int typeOfMutant,
			int typeOfResident, int populationSize, int numberOfTypes) {
		super();
		this.numberOfMutants = numberOfMutants;
		this.typeOfMutant = typeOfMutant;
		this.typeOfResident = typeOfResident;
		this.populationSize = populationSize;
		this.numberOfTypes = numberOfTypes;
	}
	
	public DimorphicPopulation copy(){
		return new DimorphicPopulation(this.numberOfMutants, this.typeOfMutant,
				this.typeOfResident, this.populationSize, this.numberOfTypes);
	}

	

}
