package com.evolutionandgames.jevodyn.assymetric;

public interface AsymmetricPayoffCalculator {
	
	double[] getPayoff1(AsymmetricPopulation population);
	double[] getPayoff2(AsymmetricPopulation population);

}
