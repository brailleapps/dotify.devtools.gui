package org.daisy.dotify.devtools.gui;

import org.daisy.streamline.api.identity.IdentityProviderService;
import org.osgi.framework.BundleContext;

public class IdentityProviderTracker extends MyTracker<IdentityProviderService> {

	public IdentityProviderTracker(BundleContext context) {
		super(context, IdentityProviderService.class.getName());
	}

}
