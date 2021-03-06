package org.spotter.ext.detection.appHiccups.strategies;

import java.util.ArrayList;
import java.util.List;

import org.lpe.common.util.LpeNumericUtils;
import org.lpe.common.util.NumericPairList;
import org.spotter.core.detection.DetectionResultManager;
import org.spotter.ext.detection.appHiccups.IHiccupAnalysisStrategy;
import org.spotter.ext.detection.appHiccups.utils.Hiccup;
import org.spotter.ext.detection.appHiccups.utils.HiccupDetectionConfig;
import org.spotter.ext.detection.utils.AnalysisChartBuilder;
import org.spotter.shared.result.model.SpotterResult;

public class DBSCANStrategy implements IHiccupAnalysisStrategy {

	private static final int numMinNeighbours = 10;

	@Override
	public List<Hiccup> findHiccups(NumericPairList<Long, Double> responsetimeSeries,
			HiccupDetectionConfig hiccupConfig, double perfReqThreshold, double perfReqConfidence,
			DetectionResultManager resultManager, SpotterResult result) {
		List<Hiccup> hiccups = new ArrayList<Hiccup>();
		double keyRange = responsetimeSeries.getKeyMax() - responsetimeSeries.getKeyMin();
		double valueRange = responsetimeSeries.getValueMax() - responsetimeSeries.getValueMin();
		double epsilon = LpeNumericUtils.meanNormalizedDistance(responsetimeSeries, keyRange, valueRange)
				* (double) numMinNeighbours * 0.75;
		List<NumericPairList<Long, Double>> clusters = LpeNumericUtils.dbscanNormalized(responsetimeSeries, epsilon,
				numMinNeighbours, keyRange, valueRange);

		for (NumericPairList<Long, Double> c : clusters) {
			int numViolations = countRequirementViolations(perfReqThreshold, c.getValueList());
			if (((double) numViolations) / ((double) c.size()) > 1.0 - perfReqConfidence) {
				Hiccup hiccup = new Hiccup();
				hiccup.setStartTimestamp(c.getKeyMin());
				hiccup.setEndTimestamp(c.getKeyMax());
				hiccup.setMaxHiccupResponseTime(c.getValueMax());
				hiccups.add(hiccup);
			}

		}

		AnalysisChartBuilder chartBuilder = new AnalysisChartBuilder();
		chartBuilder.startChartWithoutLegend("Clusters", "Experiment Time [ms]", "Response Time [ms]");

		int i = 1;
		for (NumericPairList<Long, Double> c : clusters) {
			chartBuilder.addScatterSeries(c, "Cluster " + i);
			i++;
		}
		chartBuilder.addHorizontalLine(perfReqThreshold, "Performance Requirement");
		resultManager.storeImageChartResource(chartBuilder.build(), "Response Time Clusters", result);
		return hiccups;
	}

	private int countRequirementViolations(double perfReqThreshold, List<Double> responseTimes) {
		int count = 0;
		for (Double rt : responseTimes) {
			if (rt > perfReqThreshold) {
				count++;
			}
		}
		return count;
	}

}
