package de.mpg.jevodyn.stochastic.simulation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import de.mpg.jevodyn.stochastic.EvolutionaryProcess;
import de.mpg.jevodyn.stochastic.SimplePopulation;
import de.mpg.jevodyn.utils.Random;

public class SimulationTimeSeries {

	EvolutionaryProcess process;

	public SimulationTimeSeries(EvolutionaryProcess process) {
		super();
		this.process = process;
	}

	public void simulateTimeSeries(int numberOfTimeSteps,
			int reportEveryTimeSteps, SimplePopulation startingPopulation,
			Long seed, boolean includeTotalPayoff, String fileName) throws IOException {
		Random.seed(seed);
		process.setKeepTrackTotalPayoff(includeTotalPayoff);
		process.reset(startingPopulation);
		ICsvListWriter listWriter = null;
		String[] header = this.buildHeader(startingPopulation, includeTotalPayoff);
		CellProcessor[] processors = this.getProcessors(startingPopulation, includeTotalPayoff);
		try {
			listWriter = new CsvListWriter(new FileWriter(fileName),CsvPreference.STANDARD_PREFERENCE);
			// write the header
			listWriter.writeHeader(header);
			listWriter.write(this.currentStateRow(process, includeTotalPayoff), processors);
			for (int i = 0; i < numberOfTimeSteps; i++) {
				process.step();
				if (process.getTimeStep()%reportEveryTimeSteps == 0 ) {
					listWriter.write(this.currentStateRow(process, includeTotalPayoff), processors);
				}
			}
		
		} finally {
			if (listWriter != null) {
				listWriter.close();
			}
		}

	}

	private List<Object> currentStateRow(EvolutionaryProcess process, boolean includeTotalPayoff) {
		ArrayList<Object> ans = new ArrayList<Object>();
		ans.add(process.getTimeStep());
		for (int i = 0; i < process.getPopulation().getNumberOfTypes(); i++) {
			ans.add(process.getPopulation().getNumberOfCopies(i));
		}
		if(includeTotalPayoff) ans.add(process.getTotalPopulationPayoff());
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
	
	private CellProcessor[] getProcessors(SimplePopulation startingPopulation,boolean includeTotalPayoff) {
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

	private int getHeaderSize(SimplePopulation startingPopulation, boolean includeTotalPayoff) {
		int headerSize = startingPopulation.getNumberOfTypes() + 1;
		if (includeTotalPayoff) {
			headerSize++;
		}
		return headerSize;
	}

	
	
	

}
