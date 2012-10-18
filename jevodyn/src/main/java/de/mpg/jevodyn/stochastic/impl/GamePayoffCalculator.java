package de.mpg.jevodyn.stochastic.impl;

import org.apache.commons.math3.linear.RealMatrix;

import de.mpg.jevodyn.stochastic.PayoffCalculator;
import de.mpg.jevodyn.stochastic.SimplePopulation;

public class GamePayoffCalculator implements PayoffCalculator {

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

}
