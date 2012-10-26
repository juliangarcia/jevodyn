package de.mpg.jevodyn.agentbased.simple;

import org.junit.Assert;
import org.junit.Test;

import de.mpg.jevodyn.agentbased.AgentBasedPopulation;
import de.mpg.jevodyn.utils.Random;

public class AgentBasedSimplePopulationFactoryTest {

	@Test
	public void testCreatePopulation() {
		Random.seed();
		for (int i = 0; i < 10; i++) {
			int populationSize = 0;
			while (populationSize < 2 || populationSize % 2 != 0) {
				populationSize = Random.nextInt(51);
			}
			AgentSimple simple = new AgentSimple(Random.nextInt(10));
			AgentBasedSimplePopulationFactory factory = new AgentBasedSimplePopulationFactory(
					populationSize, simple);
			AgentBasedPopulation pop = factory.createPopulation();
			Assert.assertEquals(populationSize, pop.getSize());
			Assert.assertEquals(1, pop.getSetOfAgents().size());
			Assert.assertTrue(pop.getSetOfAgents().contains(simple));
		}
	}

}
