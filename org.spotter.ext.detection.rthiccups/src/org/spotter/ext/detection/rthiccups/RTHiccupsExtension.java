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
package org.spotter.ext.detection.rthiccups;

import org.lpe.common.config.ConfigParameterDescription;
import org.spotter.core.detection.AbstractDetectionExtension;
import org.spotter.core.detection.IDetectionController;
import org.spotter.ext.detection.hiccup.utils.HiccupDetectionConfig;

/**
 * Responsetime hiccups detection extension.
 * 
 * @author Alexander Wert
 * 
 */
public class RTHiccupsExtension extends AbstractDetectionExtension {

	// TODO: please provide a description
	private static final String EXTENSION_DESCRIPTION = "no description";

	@Override
	public String getName() {
		return "RTHiccups";
	}

	@Override
	public IDetectionController createExtensionArtifact() {
		return new RTHiccupsDetectionController(this);
	}

	@Override
	protected void initializeConfigurationParameters() {
		for (ConfigParameterDescription cpd : HiccupDetectionConfig.getConfigurationParameters()) {
			addConfigParameter(cpd);
		}
		addConfigParameter(ConfigParameterDescription.createExtensionDescription(EXTENSION_DESCRIPTION));
	}
}
