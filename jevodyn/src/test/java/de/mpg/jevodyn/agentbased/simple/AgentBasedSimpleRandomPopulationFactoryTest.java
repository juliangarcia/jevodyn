package de.mpg.jevodyn.agentbased.simple;

import org.junit.Assert;
import org.junit.Test;

import de.mpg.jevodyn.agentbased.AgentBasedPopulation;
import de.mpg.jevodyn.utils.Random;

public class AgentBasedSimpleRandomPopulationFactoryTest {

	@Test
	public void testCreatePopulation() {
		Random.seed();
		for (int i = 0; i < 500; i++) {
			int populationSize = 0;
			while (populationSize < 2 || populationSize % 2 != 0) {
				populationSize = Random.nextInt(51);
			}
			boolean edge = Random.nextBoolean();
			int numberOfTypes = 0;
			while (numberOfTypes < 2) {
				numberOfTypes = Random.nextInt(51);
			}
			AgentBasedSimpleRandomPopulationFactory factory = new AgentBasedSimpleRandomPopulationFactory(
					numberOfTypes, populationSize, edge);
			AgentBasedPopulation pop = factory.createPopulation();
			if (edge) {
				// set must be max of size two
				Assert.assertTrue(pop.getSetOfAgents().size()<=2);
			}
			// types is at most numberOfTypes
			Assert.assertTrue(pop.getSetOfAgents().size() <= numberOfTypes);
			Assert.assertEquals(populationSize, pop.getSize());
		}
	}
}
