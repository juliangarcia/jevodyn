package de.mpg.jevodyn.stochastic.impl;

import de.mpg.jevodyn.utils.Random;
import junit.framework.TestCase;

public class SimplePopulationImplTest extends TestCase {

	private static final double DELTA = 0.000001;


	public void testGetAsArrayOfTypes() {
		int[] populationArray = {1,2};
		SimplePopulationImpl mock = new SimplePopulationImpl(populationArray);
		mock.removeOneIndividual(0);
		assertEquals(0, mock.getAsArrayOfTypes()[0]);
		assertEquals(2, mock.getSize());
		mock.addOneIndividual(0);
		assertEquals(1, mock.getAsArrayOfTypes()[0]);
		assertEquals(2, mock.getAsArrayOfTypes()[1]);	
	}
	
	
	public void testInternalArrayDoesNotChange(){
		int strategy0 = 1;
		int strategy1 = 2;
		int strategy2 = 3;
		int[] populationArray = {strategy0,strategy1, strategy2};
		SimplePopulationImpl population = new SimplePopulationImpl(populationArray);
		//now we change the array
		populationArray[0]= 0;
		populationArray[1]= 0;
		populationArray[2]= 0;
		//but the population retains the original values
		assertEquals(strategy0, population.getNumberOfCopies(0));
		assertEquals(strategy1, population.getNumberOfCopies(1));
		assertEquals(strategy2, population.getNumberOfCopies(2));
	}
	
	
	public void testSimpleAccessMethods(){
		long seed = System.currentTimeMillis();
		Random.seed(seed);
		// first we create a random array
		int size = Random.nextInt(11);
		int[] randomArray = new int[size];
		int populationSize = 0;
		for (int i = 0; i < randomArray.length; i++) {
			randomArray[i] = Random.nextInt(11);
			populationSize=populationSize+randomArray[i];
		}
		SimplePopulationImpl populationImpl = new SimplePopulationImpl(randomArray);
		double[] frequencyArray= populationImpl.getAsTypeFrequencies();
		for (int i = 0; i < frequencyArray.length; i++) {
			assertEquals(frequencyArray[i], ((double)populationImpl.getNumberOfCopies(i))/populationImpl.getSize(), DELTA);
		}
		assertEquals(populationImpl.getNumberOfTypes(), size);
	}


	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
}
