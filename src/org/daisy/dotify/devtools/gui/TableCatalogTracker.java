package org.daisy.dotify.devtools.gui;

import org.daisy.braille.utils.api.table.TableCatalogService;
import org.osgi.framework.BundleContext;

public class TableCatalogTracker extends MyTracker<TableCatalogService> {

	public TableCatalogTracker(BundleContext context) {
		super(context, TableCatalogService.class.getName());
	}

}
