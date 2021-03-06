/**
 * Copyright 2014 SAP AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.spotter.ext.detection.ramp;

import org.lpe.common.config.ConfigParameterDescription;
import org.lpe.common.util.LpeSupportedTypes;
import org.spotter.core.detection.AbstractDetectionExtension;
import org.spotter.core.detection.IDetectionController;

/**
 * The ramp antipattern detection extension.
 * 
 * @author Alexander Wert
 */
public class RampExtension extends AbstractDetectionExtension {

	private static final String EXTENSION_DESCRIPTION = "The ramp occurs when processing time increases as the system is used.";

	protected static final String KEY_WARMUP_PHASE_DURATION = "warmupPhaseDuration";
	protected static final String KEY_REQUIRED_SIGNIFICANT_STEPS = "numRequiredSignificantSteps";
	protected static final String KEY_REQUIRED_SIGNIFICANCE_LEVEL = "requiredSignificanceLevel";
	protected static final String KEY_CPU_UTILIZATION_THRESHOLD = "maxCpuUtilization";
	protected static final String KEY_EXPERIMENT_STEPS = "numExperiments";
	
	protected static final int STIMULATION_PHASE_DURATION_DEFAULT = 30; //[Sec]
	protected static final int EXPERIMENT_STEPS_DEFAULT = 3;
	protected static final double REQUIRED_SIGNIFICANCE_LEVEL_DEFAULT = 0.05; //[0-1] (percentage)
	protected static final int REQUIRED_SIGNIFICANT_STEPS_DEFAULT = 2;
	protected static final double MAX_CPU_UTILIZATION_DEFAULT = 0.9; //[0-1] (percentage)
	
	private static final int STIMULATION_PHASE_MIN_DURATION = 10; //[Sec]
	
	@Override
	public String getName() {
		return "Ramp";
	}
	
	private ConfigParameterDescription createStimulationPhaseDurationParameter() {
		ConfigParameterDescription parameter = new ConfigParameterDescription(KEY_WARMUP_PHASE_DURATION, LpeSupportedTypes.Integer);
		parameter.setDefaultValue(String.valueOf(STIMULATION_PHASE_DURATION_DEFAULT));
		parameter.setRange(String.valueOf(STIMULATION_PHASE_MIN_DURATION), String.valueOf(Integer.MAX_VALUE));
		parameter.setDescription("The duration of the stimulation phase.");
		return parameter;
	}
	
	private ConfigParameterDescription createNumExperimentsParameter() {
		ConfigParameterDescription parameter = new ConfigParameterDescription(KEY_EXPERIMENT_STEPS, LpeSupportedTypes.Integer);
		parameter.setDefaultValue(String.valueOf(EXPERIMENT_STEPS_DEFAULT));
		parameter.setRange(String.valueOf(2), String.valueOf(Integer.MAX_VALUE));
		parameter.setDescription("Number of experiments to execute with "
				+ "different number of users between 1 and max number of users.");
		return parameter;
	}
	
	private ConfigParameterDescription createRequiredSignificanceLevelParameter() {
		ConfigParameterDescription parameter = new ConfigParameterDescription(KEY_REQUIRED_SIGNIFICANCE_LEVEL, LpeSupportedTypes.Double);
		parameter.setDefaultValue(String.valueOf(REQUIRED_SIGNIFICANCE_LEVEL_DEFAULT));
		parameter.setRange(String.valueOf(0.0), String.valueOf(1.0));
		parameter.setDescription("This parameter defines the confidence level to be reached "
				+ "in order to recognize a significant difference when comparing "
				+ "two response time samples with the t-test.");
		return parameter;
	}
	
	private ConfigParameterDescription createRequiredSignificantStepsParameter() {
		ConfigParameterDescription parameter = new ConfigParameterDescription(KEY_REQUIRED_SIGNIFICANT_STEPS, LpeSupportedTypes.Integer);
		parameter.setDefaultValue(String.valueOf(REQUIRED_SIGNIFICANT_STEPS_DEFAULT));
		parameter.setRange(String.valueOf(2), String.valueOf(Integer.MAX_VALUE));
		parameter.setDescription("This parameter specifies the number of steps between experiments "
				+ "required to show a significant increase in order to detect a Ramp.");
		return parameter;
	}
	
	private ConfigParameterDescription createCpuThresholdParameter() {
		ConfigParameterDescription parameter = new ConfigParameterDescription(KEY_CPU_UTILIZATION_THRESHOLD, LpeSupportedTypes.Double);
		parameter.setDefaultValue(String.valueOf(MAX_CPU_UTILIZATION_DEFAULT));
		parameter.setRange(String.valueOf(0.0), String.valueOf(1.0));
		parameter.setDescription("Defines the CPU utilization threshold, when a system is considered as overutilized.");
		return parameter;
	}

	@Override
	public IDetectionController createExtensionArtifact() {
		return new RampDetectionController(this);
	}

	@Override
	protected void initializeConfigurationParameters() {
		addConfigParameter(ConfigParameterDescription.createExtensionDescription(EXTENSION_DESCRIPTION));
		addConfigParameter(createStimulationPhaseDurationParameter());
		addConfigParameter(createNumExperimentsParameter());
		addConfigParameter(createRequiredSignificanceLevelParameter());
		addConfigParameter(createRequiredSignificantStepsParameter());
		addConfigParameter(createCpuThresholdParameter());
	}
}
