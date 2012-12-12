package com.evolutionandgames.jevodyn.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.evolutionandgames.jevodyn.PayoffCalculator;
import com.evolutionandgames.jevodyn.SimplePopulation;
import com.evolutionandgames.jevodyn.impl.MoranProcess;
import com.evolutionandgames.jevodyn.impl.SimplePopulationImpl;
import com.evolutionandgames.jevodyn.utils.ArrayUtils;
import com.evolutionandgames.jevodyn.utils.PayoffToFitnessMapping;
import com.evolutionandgames.jevodyn.utils.Random;


public class MoranProcessTest {

	private class EveryBodyGetsOnePayoffCalculator implements PayoffCalculator {

		public double[] getPayoff(SimplePopulation population) {
			double[] ans = new double[population.getNumberOfTypes()];
			for (int i = 0; i < ans.length; i++) {
				ans[i] = 1.0;
			}
			return ans;
		}

	}

	@Test
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
	
	

	@Test
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
				assertEquals((double)populationSize, mp.getTotalPopulationPayoff());
			}
		}
	}
	
	 
	@Test
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



}
