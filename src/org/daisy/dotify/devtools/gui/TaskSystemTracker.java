package org.daisy.dotify.devtools.gui;

import org.daisy.dotify.api.tasks.TaskSystemFactoryMakerService;
import org.osgi.framework.BundleContext;

public class TaskSystemTracker extends MyTracker<TaskSystemFactoryMakerService> {

	public TaskSystemTracker(BundleContext context) {
		super(context, TaskSystemFactoryMakerService.class.getName());
	}

}
