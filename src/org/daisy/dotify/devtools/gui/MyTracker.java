package org.daisy.dotify.devtools.gui;

import java.util.HashSet;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class MyTracker<T> extends ServiceTracker<T, T> {
	private final HashSet<T> list;
	
	public MyTracker(BundleContext context, String name) {
		super(context, name, null);
		this.list = new HashSet<T>();
	}
	
	@Override
	public T addingService(ServiceReference<T> reference) {
		T f = context.getService(reference);
		list.add(f);
		return super.addingService(reference);
	}
	
	public T get() {
		if (list.size()>0) {
			return list.iterator().next();
		} else {
			return null;
		}
	}
	
	public int size() {
		return list.size();
	}

	@Override
	public void removedService(ServiceReference<T> reference, T service) {
		list.remove(service);
		super.removedService(reference, service);
	}

}
