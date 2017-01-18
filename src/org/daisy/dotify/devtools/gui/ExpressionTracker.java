package org.daisy.dotify.devtools.gui;

import org.daisy.dotify.api.obfl.ExpressionFactory;
import org.osgi.framework.BundleContext;

public class ExpressionTracker extends MyTracker<ExpressionFactory> {

	public ExpressionTracker(BundleContext context) {
		super(context, ExpressionFactory.class.getName());
	}

}
