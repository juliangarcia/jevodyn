package com.evolutionandgames.jevodyn.agentbased.simple;

import com.evolutionandgames.jevodyn.agentbased.Agent;

public class AgentSimple implements Agent {
	
	public int getStrategy() {
		return strategy;
	}

	public void setStrategy(int strategy) {
		this.strategy = strategy;
	}

	private int strategy;

	public AgentSimple(int strategy) {
		super();
		this.strategy = strategy;
	}

	@Override
	public String toString() {
		return Integer.toString(strategy);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + strategy;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AgentSimple other = (AgentSimple) obj;
		if (strategy != other.strategy)
			return false;
		return true;
	}
	
	
	
	

}
