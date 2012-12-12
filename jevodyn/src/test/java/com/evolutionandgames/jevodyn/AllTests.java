package com.evolutionandgames.jevodyn;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.evolutionandgames.jevodyn.agentbased.AgentBasedSimulationDistributionTest;
import com.evolutionandgames.jevodyn.agentbased.AgentBasedSimulationFixationTest;
import com.evolutionandgames.jevodyn.agentbased.AgentBasedSimulationTimeSeriesTest;
import com.evolutionandgames.jevodyn.agentbased.AgentBasedSimulationTotalPayoffTest;
import com.evolutionandgames.jevodyn.agentbased.impl.AgentBasedPopulationImplTest;
import com.evolutionandgames.jevodyn.agentbased.impl.AgentBasedWrightFisherProcessWithAssortmentTest;
import com.evolutionandgames.jevodyn.agentbased.impl.AgentMutatorSimpleKernelTest;
import com.evolutionandgames.jevodyn.agentbased.simple.AgentBasedSimplePopulationFactoryTest;
import com.evolutionandgames.jevodyn.agentbased.simple.AgentBasedSimpleRandomPopulationFactoryTest;
import com.evolutionandgames.jevodyn.stochastic.SimulationFixationProbabilityTest;
import com.evolutionandgames.jevodyn.stochastic.SimulationStationaryDistributionTest;
import com.evolutionandgames.jevodyn.stochastic.SimulationTimeSeriesTest;
import com.evolutionandgames.jevodyn.stochastic.SimulationTotalPayoffTest;
import com.evolutionandgames.jevodyn.stochastic.impl.GamePayoffCalculatorTest;
import com.evolutionandgames.jevodyn.stochastic.impl.MoranProcessTest;
import com.evolutionandgames.jevodyn.stochastic.impl.SimplePopulationImplTest;
import com.evolutionandgames.jevodyn.utils.RandomTest;


@RunWith(Suite.class)
@SuiteClasses({GamePayoffCalculatorTest.class,
			   SimplePopulationImplTest.class,
			   MoranProcessTest.class,
			   SimulationFixationProbabilityTest.class,
			   RandomTest.class, 
			   SimulationStationaryDistributionTest.class,
			   SimulationTimeSeriesTest.class,
			   SimulationTotalPayoffTest.class,
			   AgentBasedPopulationImplTest.class,
			   AgentBasedWrightFisherProcessWithAssortmentTest.class,
			   AgentBasedSimulationTimeSeriesTest.class,
			   AgentBasedSimulationFixationTest.class,
			   AgentBasedSimulationDistributionTest.class,
			   AgentBasedSimulationTotalPayoffTest.class,
			   AgentBasedSimplePopulationFactoryTest.class,
			   AgentBasedSimpleRandomPopulationFactoryTest.class,
			   AgentMutatorSimpleKernelTest.class})

public class AllTests {

}
