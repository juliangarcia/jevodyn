package de.mpg.jevodyn.stochastic.impl;

import junit.framework.TestCase;

import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import de.mpg.jevodyn.stochastic.SimplePopulation;
import de.mpg.jevodyn.utils.Games;

public class GamePayoffCalculatorTest extends TestCase {

	private static Double DELTA = 0.0001;
	
	@Test
	public void testGetPayoff() {
		RealMatrix game = Games.allcTftAlld();
		GamePayoffCalculator payoffCalculator = new GamePayoffCalculator(game);
		int[] populationArray = {0,7,3};
		SimplePopulation population = new SimplePopulationImpl(populationArray);
		double arreglo[] = payoffCalculator.getPayoff(population);
		//Harcoded values
		assertEquals(2.0, arreglo[0], DELTA);
		assertEquals(2.3166666666, arreglo[1], DELTA);
		assertEquals(1.1166666, arreglo[2], DELTA);
	}

}
