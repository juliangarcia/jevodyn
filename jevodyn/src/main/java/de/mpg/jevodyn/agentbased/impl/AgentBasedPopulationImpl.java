package de.mpg.jevodyn.agentbased.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import de.mpg.jevodyn.agentbased.Agent;
import de.mpg.jevodyn.agentbased.AgentBasedFixedSizePopulation;

public class AgentBasedPopulationImpl implements AgentBasedFixedSizePopulation {

	public static final boolean EXTENSIVE_TO_STRING = false;
	private Agent[] agentArray;

	private double[] payoffsArray;
	private int size;

	public AgentBasedPopulationImpl(Agent[] agentArray) {
		super();
		this.size = agentArray.length;
		this.agentArray = agentArray.clone();
		payoffsArray = new double[this.size];
	}

	public Agent[] getAsArrayOfAgents() {
		return agentArray;
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

	private String frequenciesToString() {
		// first get an ordered multiset from the
		Multiset<Agent> multiset = Multisets.copyHighestCountFirst(HashMultiset
				.create(Arrays.asList(this.agentArray)));
		List<String> stringView = new ArrayList<String>();
		for (Iterator<Agent> iterator = multiset.elementSet().iterator(); iterator
				.hasNext();) {
			Agent agent = (Agent) iterator.next();
			stringView.add("Strategy : " + agent.toString() + ", Count : "
					+ multiset.count(agent));
		}
		Joiner joiner = Joiner.on("; ").skipNulls();
		return joiner.join(stringView);
	}

	@Override
	public String toString() {
		if (EXTENSIVE_TO_STRING)
			return "AgentBasedPopulationImpl [agentArray="
					+ Arrays.toString(agentArray) + ", payoffsArray="
					+ Arrays.toString(payoffsArray) + ", size=" + size + "]";
		return frequenciesToString();
	}

}
