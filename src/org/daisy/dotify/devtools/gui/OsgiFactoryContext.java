package org.daisy.dotify.devtools.gui;

import java.util.HashMap;
import java.util.Map;

import org.daisy.dotify.api.embosser.EmbosserCatalogService;
import org.daisy.dotify.api.paper.PaperCatalogService;
import org.daisy.dotify.api.table.TableCatalogService;
import org.daisy.dotify.api.engine.FormatterEngineFactoryService;
import org.daisy.dotify.api.hyphenator.HyphenatorFactoryMakerService;
import org.daisy.dotify.api.obfl.ExpressionFactory;
import org.daisy.dotify.api.text.Integer2TextFactoryMakerService;
import org.daisy.dotify.api.translator.BrailleTranslatorFactoryMakerService;
import org.daisy.dotify.api.writer.PagedMediaWriterFactoryMakerService;
import org.daisy.streamline.api.identity.IdentityProviderService;
import org.daisy.streamline.api.tasks.TaskSystemFactoryMakerService;
import org.daisy.streamline.api.validity.ValidatorFactoryMakerService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class OsgiFactoryContext implements FactoryContext {
	private final Map<Class<?>, MyTracker<?>> trackers;
	private BundleContext context;
	
	public OsgiFactoryContext() {
		this.trackers = new HashMap<>();
	}

	public void openTracking(BundleContext context) {
		putTracker(new TranslatorTracker(context));
		putTracker(new TableCatalogTracker(context));
		putTracker(new PaperCatalogTracker(context));
		putTracker(new EmbosserCatalogTracker(context));
		putTracker(new ValidatorTracker(context));
		putTracker(new Int2TextTracker(context));
		putTracker(new HyphTracker(context));
		putTracker(new ExpressionTracker(context));
		putTracker(new FormatterTracker(context));
		putTracker(new TaskSystemTracker(context));
		putTracker(new IdentityProviderTracker(context));
		putTracker(new WriterTracker(context));
		trackers.values().stream().forEach(e->e.open());
		this.context = context;
	}

	public void closeTracking() {
		trackers.values().stream().forEach(e->e.close());
	}
	
	private void putTracker(MyTracker<?> tracker) {
		trackers.put(tracker.getClass(), tracker);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getTracker(Class<T> clazz) {
		return (T)trackers.get(clazz);
	}

	@Override
	public EmbosserCatalogService getEmbosserCatalogService() {
		return getTracker(EmbosserCatalogTracker.class).get();
	}

	@Override
	public PaperCatalogService getPaperCatalogService() {
		return getTracker(PaperCatalogTracker.class).get();
	}

	@Override
	public TableCatalogService getTableCatalogService() {
		return getTracker(TableCatalogTracker.class).get();
	}

	@Override
	public BrailleTranslatorFactoryMakerService getBrailleTranslatorFactoryMakerService() {
		return getTracker(TranslatorTracker.class).get();
	}

	@Override
	public ValidatorFactoryMakerService getValidatorFactoryService() {
		return getTracker(ValidatorTracker.class).get();
	}

	@Override
	public Integer2TextFactoryMakerService getInteger2TextFactoryMakerService() {
		return getTracker(Int2TextTracker.class).get();
	}

	@Override
	public HyphenatorFactoryMakerService getHyphenatorFactoryMakerService() {
		return getTracker(HyphTracker.class).get();
	}

	@Override
	public ExpressionFactory getExpressionFactory() {
		return getTracker(ExpressionTracker.class).get();
	}

	@Override
	public FormatterEngineFactoryService getFormatterEngineFactoryService() {
		return getTracker(FormatterTracker.class).get();
	}
	
	@Override
	public TaskSystemFactoryMakerService getTaskSystemFactoryMakerService() {
		return getTracker(TaskSystemTracker.class).get();
	}
	

	@Override
	public IdentityProviderService getIdentityProviderService() {
		return getTracker(IdentityProviderTracker.class).get();
	}

	@Override
	public PagedMediaWriterFactoryMakerService getPagedMediaWriterFactoryService() {
		return getTracker(WriterTracker.class).get();
	}

	@Override
	public void stop() {
		if (context!=null) {
			try {
				context.getBundle().stop();
			} catch (BundleException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean isStarted() {
		return context!=null && context.getBundle().getState()==Bundle.ACTIVE;
	}

}
