package de.mpg.jevodyn.agentbased;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import de.mpg.jevodyn.agentbased.impl.AgentBasedPopulationImpl;
import de.mpg.jevodyn.utils.Random;

/**
 * Provides methods for simulating a given AgentBased process. 
 * @author garcia
 *
 */
public class AgentBasedSimulation {

	/**
	 * Process to be simulated
	 */
	private AgentBasedEvolutionaryProcess process;

	public AgentBasedSimulation(AgentBasedEvolutionaryProcess process) {
		super();
		this.process = process;
	}

	/***
	 * Estimate the fixation probability of mutant in a population of incumbent.
	 * @param mutant Agent 
	 * @param incumbent Agent
	 * @param numberOfSamples 
	 * @param seed
	 * @return
	 */
	public double estimateFixationProbability(Agent mutant, Agent incumbent,
			int numberOfSamples, Long seed) {
		Random.seed(seed);
		int populationSize = this.process.getPopulation().getSize();
		int positives = 0;
		Agent[] startingPopulationArray = getStartingArray(mutant, incumbent,
				populationSize);
		for (int i = 0; i < numberOfSamples; i++) {
			this.process.reset(new AgentBasedPopulationImpl(
					startingPopulationArray));
			boolean fixated = false;
			//step until fixation is reached
			while (!fixated) {
				this.process.stepWithoutMutation();
				fixated = this.process.getPopulation().getSetOfAgents().size() == 1;
			}
			//increase positives if it fixated to the mutant type 
			if (this.process.getPopulation().getAgent(0).equals(mutant))
				positives++;
		}
		return ((double) positives) / numberOfSamples;
	}

	/**
	 * Help method, to initialize fixation calculations
	 * @param mutant
	 * @param incumbent
	 * @param populationSize
	 * @return
	 */
	private Agent[] getStartingArray(Agent mutant, Agent incumbent,
			int populationSize) {
		Agent[] ans = new Agent[populationSize];
		ans[0] = mutant;
		for (int i = 1; i < ans.length; i++) {
			ans[i] = incumbent;
		}
		return ans;
	}

	/**
	 * Estimate stationary distribution
	 * @param burningTimePerEstimate for every estimate the chain is left running without taking samples
	 * @param samplesPerEstimate once burningTime is over, we start taking this number of samples
	 * @param numberOfEstimates the process is repeatedas many times as number of estimate requires
	 * @param seed for reproduciblity. 
	 * @param maximumResultSize if the result needs to have at most a certain number of agents
	 * @param factory a class that generates a new starting population for every estimate. 
	 * @return
	 */
	public Map<Agent, Double> estimateStationaryDistribution(
			int burningTimePerEstimate, int samplesPerEstimate,
			int numberOfEstimates, Long seed, int maximumResultSize,
			AgentBasedPopulationFactory factory) {
		Random.seed(seed);
		Multiset<Agent> multiset = HashMultiset.create();
		for (int estimate = 0; estimate < numberOfEstimates; estimate++) {
			process.reset(factory.createPopulation());
			for (int burningStep = 0; burningStep < burningTimePerEstimate; burningStep++) {
				process.step();
			}
			for (int sample = 0; sample < samplesPerEstimate; sample++) {
				process.step();
				for (int i = 0; i < this.process.getPopulation().getSize(); i++) {
					multiset.add(this.process.getPopulation().getAgent(i));
				}
			}
		}
		// build the answer
		double size = (double) multiset.size();
		Map<Agent, Double> ans = new HashMap<Agent, Double>();
		// create a view ordered by count
		Multiset<Agent> ordererdView = Multisets
				.copyHighestCountFirst(multiset);
		int i = 0;
		for (Iterator<Agent> iterator = ordererdView.iterator(); iterator
				.hasNext();) {
			Agent agent = (Agent) iterator.next();
			ans.put(agent, ordererdView.count(agent) / size);
			if (i >= maximumResultSize)
				break;
		}
		return ans;
	}

	/**
	 * Simulates evolution writing the ouput to a file. 
	 * @param numberOfTimeSteps
	 * @param reportEveryTimeSteps
	 * @param seed
	 * @param fileName
	 * @throws IOException
	 */
	public void simulateTimeSeries(int numberOfTimeSteps,
			int reportEveryTimeSteps, Long seed,
			String fileName) throws IOException {
		ICsvListWriter listWriter = null;
		String[] header = this.buildHeader();
		CellProcessor[] processors = this.getProcessors();
		try {
			listWriter = new CsvListWriter(new FileWriter(fileName),
					CsvPreference.STANDARD_PREFERENCE);
			// write the header
			listWriter.writeHeader(header);
			// write the initial zero step content
			listWriter.write(this.currentStateRow(process), processors);
			// repeat for as many steps as requested
			for (int i = 0; i < numberOfTimeSteps; i++) {
				// step
				process.step();
				// if time to repor, report
				if (process.getTimeStep() % reportEveryTimeSteps == 0) {
					listWriter.write(this.currentStateRow(process), processors);
				}
			}
		} finally {
			// close files no matter what
			if (listWriter != null) {
				listWriter.close();
			}
		}

	}

	/**
	 * Helper method to write the csv file
	 * @return
	 */
	private CellProcessor[] getProcessors() {

		CellProcessor timeStepProcessor = new NotNull();
		CellProcessor populationProcessor = new NotNull();
		CellProcessor totalPayoffProcessor = new NotNull();
		final CellProcessor[] processors = { timeStepProcessor,
				totalPayoffProcessor, populationProcessor };
		return processors;
	}

	/**
	 * Helper method to write the csv file
	 * @return
	 */
	private String[] buildHeader() {
		// build the header
		final String[] header = { "timeStep", "totalPayoff", "population" };
		return header;
	}

	/**
	 * Turns the current population into a list to be written in the csv file
	 * @param process
	 * @return
	 */
	private List<Object> currentStateRow(AgentBasedEvolutionaryProcess process) {
		ArrayList<Object> ans = new ArrayList<Object>();
		ans.add(process.getTimeStep());
		ans.add(process.getTotalPopulationPayoff());
		ans.add(process.getPopulation().toString());
		return ans;
	}

	/**
	 * Estimate the total payoff, paramters are similar to those of estimate distribution. Instead of counting the agents, all we care
	 * about here is the population payoff
	 * @param burningTimePerEstimate
	 * @param samplesPerEstimate
	 * @param numberOfEstimates
	 * @param reportEveryTimeSteps
	 * @param seed
	 * @param factory
	 * @return
	 */
	public double estimateTotalPayoff(int burningTimePerEstimate,
			int samplesPerEstimate, int numberOfEstimates,
			int reportEveryTimeSteps, Long seed,
			AgentBasedPopulationFactory factory) {
		Random.seed(seed);
		double payoffSum = 0.0;
		int totalNumberOfSamples = 0;
		for (int estimate = 0; estimate < numberOfEstimates; estimate++) {
			process.reset(factory.createPopulation());
			for (int burningStep = 0; burningStep < burningTimePerEstimate; burningStep++) {
				process.step();
			}
			for (int sample = 0; sample < samplesPerEstimate; sample++) {
				// sample every time?
				process.step();
				if (sample % reportEveryTimeSteps == 0) {
					payoffSum = payoffSum + process.getTotalPopulationPayoff();
					totalNumberOfSamples++;
				}

			}
		}
		return payoffSum / totalNumberOfSamples;
	}
}
