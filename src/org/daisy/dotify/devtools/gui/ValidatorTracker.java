package org.daisy.dotify.devtools.gui;

import org.daisy.streamline.api.validity.ValidatorFactoryMakerService;
import org.osgi.framework.BundleContext;

public class ValidatorTracker extends MyTracker<ValidatorFactoryMakerService> {

	public ValidatorTracker(BundleContext context) {
		super(context, ValidatorFactoryMakerService.class.getName());
	}

}
