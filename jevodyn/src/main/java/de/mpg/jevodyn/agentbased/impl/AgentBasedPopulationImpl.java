package de.mpg.jevodyn.agentbased.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.mpg.jevodyn.agentbased.Agent;
import de.mpg.jevodyn.agentbased.AgentBasedFixedSizePopulation;

public class AgentBasedPopulationImpl implements
		AgentBasedFixedSizePopulation {
	
	private Agent[] agentArray;
	private double[] payoffsArray;
	private int size;
	

	public AgentBasedPopulationImpl(Agent[] agentArray) {
		super();
		this.size = agentArray.length;
		this.agentArray = agentArray.clone();
		payoffsArray = new double[this.size];
	}

	public void addOneIndividual(Agent agent, int position) {
		agentArray[position] = agent;
	}

	public int getNumberOfCopies(Agent agent) {
		int ans = 0;
		for (int i = 0; i < agentArray.length; i++) {
			if (agentArray[i].equals(agent)) {
				ans++;
			}
		}
		return ans;
	}

	public int getSize() {
		return this.size;
	}

	public Set<Agent> getSetOfAgents() {
		return new HashSet<Agent>(Arrays.asList(agentArray));
	}

	public Agent getAgent(int index) {
		return agentArray[index];
	}

	public double getPayoffOfAgent(int index) {
		return payoffsArray[index];
	}

	public void setPayoffOfAgent(int index, double payoff) {
		this.payoffsArray[index] = payoff;
	}


}
