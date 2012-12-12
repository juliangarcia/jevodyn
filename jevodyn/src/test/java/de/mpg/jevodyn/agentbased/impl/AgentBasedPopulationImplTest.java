package de.mpg.jevodyn.agentbased.impl;

import org.junit.Assert;
import org.junit.Test;

import com.evolutionandgames.jevodyn.agentbased.Agent;
import com.evolutionandgames.jevodyn.agentbased.impl.AgentBasedPopulationImpl;
import com.evolutionandgames.jevodyn.agentbased.simple.AgentSimple;


public class AgentBasedPopulationImplTest {

	@Test
	public void testVarious() {
		// Let us create an array of simple agents for the standard PD
		int[] primitiveArray = { 0, 1, 1, 1, 0 }; // 5 individuals, 3 defect 2
													// cooperate
		Agent[] array = new Agent[5];
		for (int i = 0; i < array.length; i++) {
			array[i] = new AgentSimple(primitiveArray[i]);
		}
		AgentBasedPopulationImpl population = new AgentBasedPopulationImpl(
				array);
		// done

		// changes to the array do not change the population
		array[0] = new AgentSimple(1); // we set agent 0 to strategy 1 in the
										// underlying array
		Assert.assertEquals(0,
				((AgentSimple) population.getAgent(0)).getStrategy()); // but
																		// agent
																		// 0
																		// remains
																		// 0 in
																		// the
																		// population
		// now we really replace agent 0 it with a new agent with straetety 1
		population.addOneIndividual(new AgentSimple(1), 0);
		// new composition [1,1,1,1,0]
		Assert.assertEquals(1,
				((AgentSimple) population.getAgent(0)).getStrategy());
		Assert.assertEquals(1,
				((AgentSimple) population.getAgent(1)).getStrategy());
		Assert.assertEquals(1,
				((AgentSimple) population.getAgent(2)).getStrategy());
		Assert.assertEquals(1,
				((AgentSimple) population.getAgent(3)).getStrategy());
		Assert.assertEquals(0,
				((AgentSimple) population.getAgent(4)).getStrategy());
		// size must be 5
		Assert.assertEquals(5, population.getSize());
		// We get the set corresponding set, it must have 2 elements
		Assert.assertEquals(2, population.getSetOfAgents().size());
		// if we truly ad a new type then the set must contain 3 elements
		population.addOneIndividual(new AgentSimple(2), 2);
		Assert.assertEquals(3, population.getSetOfAgents().size());
		// new composition [1,1,2,1,0]
		Assert.assertEquals(1,
				((AgentSimple) population.getAgent(0)).getStrategy());
		Assert.assertEquals(1,
				((AgentSimple) population.getAgent(1)).getStrategy());
		Assert.assertEquals(2,
				((AgentSimple) population.getAgent(2)).getStrategy());
		Assert.assertEquals(1,
				((AgentSimple) population.getAgent(3)).getStrategy());
		Assert.assertEquals(0,
				((AgentSimple) population.getAgent(4)).getStrategy());
		// check get number of copies
		Assert.assertEquals(3, population.getNumberOfCopies(new AgentSimple(1)));
		Assert.assertEquals(1, population.getNumberOfCopies(new AgentSimple(2)));
		Assert.assertEquals(1, population.getNumberOfCopies(new AgentSimple(0)));
		// by default all the payoffs must be zero.
		Assert.assertEquals(0.0, population.getPayoffOfAgent(0));
		Assert.assertEquals(0.0, population.getPayoffOfAgent(1));
		Assert.assertEquals(0.0, population.getPayoffOfAgent(2));
		Assert.assertEquals(0.0, population.getPayoffOfAgent(3));
		Assert.assertEquals(0.0, population.getPayoffOfAgent(4));
		// unless we change one number
		population.setPayoffOfAgent(0, 666.0);
		Assert.assertEquals(666.0, population.getPayoffOfAgent(0));

	}

	@Test
	public void testToString() {

		// Let us create an array of simple agents for the standard PD
		int[] primitiveArray = { 0, 1, 1, 1, 0 }; // 5 individuals, 3 defect 2
													// cooperate
		Agent[] array = new Agent[5];
		for (int i = 0; i < array.length; i++) {
			array[i] = new AgentSimple(primitiveArray[i]);
		}
		AgentBasedPopulationImpl population = new AgentBasedPopulationImpl(
				array);
		String string = population.toString();
		Assert.assertEquals("Strategy : 1, Count : 3; Strategy : 0, Count : 2", string);

	}

}
