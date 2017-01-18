package org.daisy.dotify.devtools.gui;

import org.daisy.braille.api.embosser.EmbosserCatalogService;
import org.daisy.braille.api.paper.PaperCatalogService;
import org.daisy.braille.api.table.TableCatalogService;
import org.daisy.braille.api.validator.ValidatorFactoryService;
import org.daisy.dotify.api.engine.FormatterEngineFactoryService;
import org.daisy.dotify.api.hyphenator.HyphenatorFactoryMakerService;
import org.daisy.dotify.api.identity.IdentityProviderService;
import org.daisy.dotify.api.tasks.TaskSystemFactoryMakerService;
import org.daisy.dotify.api.text.Integer2TextFactoryMakerService;
import org.daisy.dotify.api.translator.BrailleTranslatorFactoryMakerService;
import org.daisy.dotify.api.writer.PagedMediaWriterFactoryMakerService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class OsgiFactoryContext implements FactoryContext {
	private TranslatorTracker tracker;
	private TableCatalogTracker tctracker;
	private PaperCatalogTracker pctracker;
	private EmbosserCatalogTracker ectracker;
	private ValidatorTracker vtracker;
	private Int2TextTracker itracker;
	private HyphTracker htracker;
	private FormatterTracker ftracker;
	private TaskSystemTracker tstracker;
	private IdentityProviderTracker idtracker;
	private WriterTracker wtracker;
	private BundleContext context;

	public void openTracking(BundleContext context) {
		tracker = new TranslatorTracker(context);
		tracker.open();
		tctracker = new TableCatalogTracker(context);
		tctracker.open();
		pctracker = new PaperCatalogTracker(context);
		pctracker.open();
		ectracker = new EmbosserCatalogTracker(context);
		ectracker.open();
		vtracker = new ValidatorTracker(context);
		vtracker.open();
		itracker = new Int2TextTracker(context);
		itracker.open();
		htracker = new HyphTracker(context);
		htracker.open();
		ftracker = new FormatterTracker(context);
		ftracker.open();
		tstracker = new TaskSystemTracker(context);
		tstracker.open();
		idtracker = new IdentityProviderTracker(context);
		idtracker.open();
		wtracker = new WriterTracker(context);
		wtracker.open();
		this.context = context;
	}

	public void closeTracking() {
		tracker.close();
		tctracker.close();
		pctracker.close();
		ectracker.close();
		vtracker.close();
		itracker.close();
		htracker.close();
		ftracker.close();
		tstracker.close();
		idtracker.close();
		wtracker.close();
	}

	@Override
	public EmbosserCatalogService getEmbosserCatalogService() {
		return ectracker.get();
	}

	@Override
	public PaperCatalogService getPaperCatalogService() {
		return pctracker.get();
	}

	@Override
	public TableCatalogService getTableCatalogService() {
		return tctracker.get();
	}

	@Override
	public BrailleTranslatorFactoryMakerService getBrailleTranslatorFactoryMakerService() {
		return tracker.get();
	}

	@Override
	public ValidatorFactoryService getValidatorFactoryService() {
		return vtracker.get();
	}

	@Override
	public Integer2TextFactoryMakerService getInteger2TextFactoryMakerService() {
		return itracker.get();
	}

	@Override
	public HyphenatorFactoryMakerService getHyphenatorFactoryMakerService() {
		return htracker.get();
	}

	@Override
	public FormatterEngineFactoryService getFormatterEngineFactoryService() {
		return ftracker.get();
	}
	
	@Override
	public TaskSystemFactoryMakerService getTaskSystemFactoryMakerService() {
		return tstracker.get();
	}
	

	@Override
	public IdentityProviderService getIdentityProviderService() {
		return idtracker.get();
	}

	@Override
	public PagedMediaWriterFactoryMakerService getPagedMediaWriterFactoryService() {
		return wtracker.get();
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
