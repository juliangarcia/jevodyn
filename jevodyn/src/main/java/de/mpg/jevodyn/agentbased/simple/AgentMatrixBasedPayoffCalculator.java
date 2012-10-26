package de.mpg.jevodyn.agentbased.simple;

import org.apache.commons.math3.linear.RealMatrix;

import de.mpg.jevodyn.agentbased.AgentBasedPopulation;
import de.mpg.jevodyn.agentbased.AgentBasedPayoffCalculator;

public class AgentMatrixBasedPayoffCalculator implements
		AgentBasedPayoffCalculator {

	private RealMatrix gameMatrix;
	
	
	public AgentMatrixBasedPayoffCalculator(RealMatrix gameMatrix) {
		super();
		this.gameMatrix = gameMatrix;
	}


	public void calculatePayoffs(AgentBasedPopulation population) {
		if (population.getSize()%2 !=0) throw new IllegalArgumentException("This class asumes that the population size is even");
		for (int i = 0; i < population.getSize()-1; i=i+2) {
			//i is focal, we set the fitness of i and i+1
			int focal = ((AgentSimple)population.getAgent(i)).getStrategy();
			int other = ((AgentSimple)population.getAgent(i+1)).getStrategy();
			population.setPayoffOfAgent(i, gameMatrix.getEntry(focal, other));
			population.setPayoffOfAgent(i+1, gameMatrix.getEntry(other, focal));
		}
	}

}
