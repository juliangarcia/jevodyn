package com.evolutionandgames.jevodyn.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import com.evolutionandgames.jevodyn.PayoffCalculator;
import com.evolutionandgames.jevodyn.SimplePopulation;
import com.evolutionandgames.jevodyn.utils.ArrayUtils;
import com.evolutionandgames.jevodyn.utils.PayoffToFitnessMapping;
import com.evolutionandgames.jevodyn.utils.Random;


public class MoranProcessTest {
	
	private static final double DELTA = 0.000001;

	private class EveryBodyGetsOnePayoffCalculator implements PayoffCalculator {

		public double[] getPayoff(SimplePopulation population) {
			double[] ans = new double[population.getNumberOfTypes()];
			for (int i = 0; i < ans.length; i++) {
				ans[i] = 1.0;
			}
			return ans;
		}

	}
	
	
	private class EveryBodyGetsRandomPayoffCalculator implements PayoffCalculator {

		public double[] getPayoff(SimplePopulation population) {
			double[] ans = new double[population.getNumberOfTypes()];
			int[] arrayPop = population.getAsArrayOfTypes();
			for (int i = 0; i < ans.length; i++) {
				//if(arrayPop[i] >0){
					ans[i] = Random.nextInt(100);
				//}
			}
			//some guys will get a negative payoff
			int n = Random.nextInt(population.getNumberOfTypes());
			for (int i = 0; i < n; i++) {
				ans[i] = -ans[i];
			}
			
			return ans;
		}

	}

	public void testStepIsInvariableInPopulationSize() {
		Random.seed();
		for (int j = 0; j < 5; j++) {
			int numberOfTypes = 2;
			int maximumNumberOfCopiesPerType = 10;
			SimplePopulationImpl population = new SimplePopulationImpl(ArrayUtils.randomArray(numberOfTypes, maximumNumberOfCopiesPerType));
			int populationSize = population.getSize();
			MoranProcess mp = new MoranProcess(population, new EveryBodyGetsOnePayoffCalculator(), Random.nextDouble(), Random.nextDouble());
			mp.setKeepTrackTotalPayoff(false);
			for (int i = 0; i < 1000; i++) {
				mp.step();
				assertEquals(mp.getPopulation().getSize(), populationSize);
			}
		}
	}
	
	

	public void testTotalPayoff() {
		Random.seed();
		for (int j = 0; j < 5; j++) {
			int numberOfTypes = 3;
			int maximumNumberOfCopiesPerType = 10;
			SimplePopulationImpl population = new SimplePopulationImpl(ArrayUtils.randomArray(numberOfTypes, maximumNumberOfCopiesPerType));
			int populationSize = population.getSize();
			MoranProcess mp = new MoranProcess(population, new EveryBodyGetsOnePayoffCalculator(), PayoffToFitnessMapping.LINEAR,Random.nextDouble(), Random.nextDouble());
			mp.setKeepTrackTotalPayoff(true);
			for (int i = 0; i < 1000; i++) {
				mp.step();
				Assert.assertEquals((double)populationSize, mp.getTotalPopulationPayoff(), DELTA);
			}
		}
	}
	
	 
	public void testIfATypeIsNotThereItNeverShowsUp() {
		Random.seed();
		for (int j = 0; j < 10; j++) {
			int numberOfTypes = 5;
			int maximumNumberOfCopiesPerType = 10;
			int[] array = ArrayUtils.randomArray(numberOfTypes, maximumNumberOfCopiesPerType);
			int zeroType = Random.nextInt(numberOfTypes);
			array[zeroType] = 0;
			SimplePopulationImpl population = new SimplePopulationImpl(array);
			MoranProcess mp = new MoranProcess(population, new EveryBodyGetsOnePayoffCalculator(), PayoffToFitnessMapping.LINEAR,Random.nextDouble(), Random.nextDouble());
			mp.setKeepTrackTotalPayoff(false);
			for (int i = 0; i < 100000; i++) {
				mp.stepWithoutMutation();
				assertEquals(0, mp.getPopulation().getNumberOfCopies(zeroType));
			}
		}
	}
	
	
	@Test
	public void testNeverNegative() {
		Random.seed();
		Long seed = (long) Random.nextInt(99999);
		Random.seed(seed);
		int numberOfTypes = 4;
		int maximumNumberOfCopiesPerType = 10;
		for (int j = 0; j < 10; j++) {
			int[] array = new int[numberOfTypes];
			for (int i = 0; i < array.length; i++) {
				while(array[i]== 0){
					array[i] = Random.nextInt(maximumNumberOfCopiesPerType);
				}
				
			}
			
			SimplePopulationImpl population = new SimplePopulationImpl(array);
			MoranProcess mp = new MoranProcess(population, new EveryBodyGetsRandomPayoffCalculator(), PayoffToFitnessMapping.EXPONENTIAL, Random.nextDouble(), Random.nextDouble());
			mp.setKeepTrackTotalPayoff(false);
			for (int i = 0; i < 100000; i++) {
				mp.step();
				Assert.assertTrue(mp.getPopulation().getAsArrayOfTypes()[0] >= 0);
				Assert.assertTrue(mp.getPopulation().getAsArrayOfTypes()[1] >= 0);
				Assert.assertTrue(mp.getPopulation().getAsArrayOfTypes()[2] >= 0);
				Assert.assertTrue(mp.getPopulation().getAsArrayOfTypes()[3] >= 0);
			}
		}
	}



}
