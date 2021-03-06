package com.evolutionandgames.jevodyn;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.evolutionandgames.jevodyn.impl.SimplePopulationImpl;
import com.evolutionandgames.jevodyn.utils.ArrayUtils;
import com.evolutionandgames.jevodyn.utils.Random;


public class Simulation {

	EvolutionaryProcess process;

	public Simulation(EvolutionaryProcess process) {
		super();
		this.process = process;
	}

	public double estimateFixationProbability(int indexOfTheMutant, int indexOfTheIncumbent, int numberOfSamples,
			Long seed) {
		
		int numberOfTypes = this.process.getPopulation().getNumberOfTypes(); 
		int populationSize = this.process.getPopulation().getSize();
		Random.seed(seed);
		int positives = 0;
		int[] startingPopulationArray = getStartingArray(indexOfTheMutant,
				indexOfTheIncumbent, numberOfTypes, populationSize);
		for (int i = 0; i < numberOfSamples; i++) {
			this.process
					.reset(new SimplePopulationImpl(startingPopulationArray));
			int fixatedOn = -1;
			while (fixatedOn == -1) {
				this.process.stepWithoutMutation();
				fixatedOn = this.process.getPopulation().getFixatedType();
			}
			if (fixatedOn == indexOfTheMutant)
				positives++;
		}
		return ((double) positives) / numberOfSamples;
	}

	private static int[] getStartingArray(int indexOfTheMutant,
			int indexOfTheIncumbent, int numberOfTypes, int populationSize) {
		int[] array = new int[numberOfTypes];
		array[indexOfTheIncumbent] = populationSize - 1;
		array[indexOfTheMutant] = 1;
		return array;
	}

	public double[] estimateStationaryDistribution(int burningTimePerEstimate,
			int samplesPerEstimate, int numberOfEstimates, Long seed) {
		Random.seed(seed);
		int numberOfTypes = this.process.getPopulation().getNumberOfTypes();
		int populationSize = this.process.getPopulation().getSize();
		process.setKeepTrackTotalPayoff(false);
		long[] countPerStrategy = new long[numberOfTypes];
		for (int estimate = 0; estimate < numberOfEstimates; estimate++) {
			process.reset(new SimplePopulationImpl(ArrayUtils.centroid(numberOfTypes, populationSize)));
			for (int burningStep = 0; burningStep < burningTimePerEstimate; burningStep++) {
				process.step();
			}
			for (int sample = 0; sample < samplesPerEstimate; sample++) {
				
				int[] previousPopArray = Arrays.copyOf(this.process.getPopulation().getAsArrayOfTypes(), this.process.getPopulation().getAsArrayOfTypes().length);
				int t_before = process.getTimeStep();
				process.step();
				int t_after = process.getTimeStep();
				int[] arrayOfTypes = process.getPopulation().getAsArrayOfTypes();
				for (int i = 0; i < numberOfTypes; i++) {
					countPerStrategy[i] = countPerStrategy[i] + (t_after - t_before -1 )* previousPopArray[i] + 
							+ arrayOfTypes[i];
				}
			}
		}
		return ArrayUtils.normalize(countPerStrategy);
	}

	public void simulateTimeSeries(int numberOfTimeSteps,
			int reportEveryTimeSteps, Long seed, boolean includeTotalPayoff, String fileName)
			throws IOException {
		Random.seed(seed);
		process.setKeepTrackTotalPayoff(includeTotalPayoff);
		ICsvListWriter listWriter = null;
		String[] header = this.buildHeader(process.getPopulation(),
				includeTotalPayoff);
		CellProcessor[] processors = this.getProcessors(process.getPopulation(),
				includeTotalPayoff);
		try {
			listWriter = new CsvListWriter(new FileWriter(fileName),
					CsvPreference.STANDARD_PREFERENCE);
			// write the header
			listWriter.writeHeader(header);
			// write the initial zero step content
			listWriter.write(this.currentStateRow(process, includeTotalPayoff),
					processors);
			// repeat for as many steps as requested
			for (int i = 0; i < numberOfTimeSteps; i++) {
				// step
				process.step();
				// if time to repor, report
				if (process.getTimeStep() % reportEveryTimeSteps == 0) {
					listWriter.write(
							this.currentStateRow(process, includeTotalPayoff),
							processors);
				}
			}

		} finally {
			// close files no matter what
			if (listWriter != null) {
				listWriter.close();
			}
		}

	}

	private List<Object> currentStateRow(EvolutionaryProcess process,
			boolean includeTotalPayoff) {
		ArrayList<Object> ans = new ArrayList<Object>();
		ans.add(process.getTimeStep());
		for (int i = 0; i < process.getPopulation().getNumberOfTypes(); i++) {
			ans.add(process.getPopulation().getNumberOfCopies(i));
		}
		if (includeTotalPayoff)
			ans.add(process.getTotalPopulationPayoff());
		return ans;
	}

	private String[] buildHeader(SimplePopulation startingPopulation,
			boolean includeTotalPayoff) {
		// build the header
		int headerSize = getHeaderSize(startingPopulation, includeTotalPayoff);
		final String[] header = new String[headerSize];
		header[0] = "timeStep";
		for (int i = 0; i < startingPopulation.getNumberOfTypes(); i++) {
			header[i + 1] = String.valueOf(i);
		}
		if (includeTotalPayoff) {
			header[headerSize - 1] = "totalPayoff";
		}
		return header;
	}

	private CellProcessor[] getProcessors(SimplePopulation startingPopulation,
			boolean includeTotalPayoff) {
		int headerSize = getHeaderSize(startingPopulation, includeTotalPayoff);
		final CellProcessor[] processors = new CellProcessor[headerSize];
		processors[0] = new NotNull();
		for (int i = 0; i < startingPopulation.getNumberOfTypes(); i++) {
			processors[i + 1] = new NotNull();
		}
		if (includeTotalPayoff) {
			processors[headerSize - 1] = new NotNull();
		}
		return processors;
	}

	private int getHeaderSize(SimplePopulation startingPopulation,
			boolean includeTotalPayoff) {
		int headerSize = startingPopulation.getNumberOfTypes() + 1;
		if (includeTotalPayoff) {
			headerSize++;
		}
		return headerSize;
	}

	public double estimateTotalPayoff(int burningTimePerEstimate,
			int samplesPerEstimate, int numberOfEstimates,
			int reportEveryTimeSteps, Long seed) {
		Random.seed(seed);
		int numberOfTypes = this.process.getPopulation().getNumberOfTypes();
		int populationSize = this.process.getPopulation().getSize();
		process.setKeepTrackTotalPayoff(true);
		double payoffSum = 0.0;
		int totalNumberOfSamples = 0;
		for (int estimate = 0; estimate < numberOfEstimates; estimate++) {
			process.reset(new SimplePopulationImpl(ArrayUtils.centroid(numberOfTypes, populationSize)));
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
