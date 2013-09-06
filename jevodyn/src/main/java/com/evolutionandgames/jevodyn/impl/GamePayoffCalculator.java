package com.evolutionandgames.jevodyn.impl;

import org.apache.commons.math3.linear.RealMatrix;

import com.evolutionandgames.jevodyn.PayoffCalculator;
import com.evolutionandgames.jevodyn.SimplePopulation;
import com.evolutionandgames.jevodyn.dimorphic.DimorphicPayoffCalculator;
import com.evolutionandgames.jevodyn.dimorphic.DimorphicPopulation;


public class GamePayoffCalculator implements PayoffCalculator, DimorphicPayoffCalculator {

	RealMatrix gameMatrix;
	
	public GamePayoffCalculator(RealMatrix gameMatrix) {
		super();
		this.gameMatrix = gameMatrix;
	}

	public RealMatrix getGameMatrix() {
		return gameMatrix;
	}

	public double[] getPayoff(SimplePopulation population) {
		double factorNumberOfInteractions= 1.0/(population.getSize() -1.0);
		double[] answer= new double[population.getNumberOfTypes()];
		for (int i = 0; i < gameMatrix.getRowDimension(); i++) {
			double iStrategyPayoff = 0.0;
			for (int j = 0; j < gameMatrix.getColumnDimension(); j++) {
				if (i!=j) {
					iStrategyPayoff = iStrategyPayoff + population.getNumberOfCopies(j)*gameMatrix.getEntry(i, j);
				}else{
					iStrategyPayoff = iStrategyPayoff + (population.getNumberOfCopies(j)-1)*gameMatrix.getEntry(i, j);
				}
			}
			answer[i]=iStrategyPayoff*factorNumberOfInteractions;
		}
		return answer;
	}

	public double[] payoff(DimorphicPopulation population) {
		//fitness of the resident
		int residentType = population.getTypeOfResident();
		int mutantType = population.getTypeOfMutant();
		int numberOfResidents = population.getNumberOfResidents();
		int numberOfMutants = population.getNumberOfMutants();
		double payoffResident = (numberOfResidents-1.0)*gameMatrix.getEntry(residentType, residentType)  + numberOfMutants*gameMatrix.getEntry(residentType, mutantType);
		double payoffMutant = (numberOfResidents)*gameMatrix.getEntry(mutantType, residentType)  + (numberOfMutants-1.0)*gameMatrix.getEntry(mutantType, mutantType);
		payoffResident = (1.0/(population.getSize()-1))* payoffResident;
		payoffMutant = (1.0/(population.getSize()-1))* payoffMutant;
		double[] ans = {payoffResident, payoffMutant};
		return ans;
	}

}
