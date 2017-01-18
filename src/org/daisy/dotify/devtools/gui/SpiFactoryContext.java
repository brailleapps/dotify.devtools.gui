package org.daisy.dotify.devtools.gui;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

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

public class SpiFactoryContext implements FactoryContext {
	private static final Logger logger = Logger.getLogger(SpiFactoryContext.class.getCanonicalName());
	private final Map<Class<?>, Object> services;
	
	public SpiFactoryContext() {
		this.services = new HashMap<>();
	}
	
	private void putService(Object service) {
		services.put(service.getClass(), service);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getService(Class<T> clazz) {
		return (T)services.get(clazz);
	}
	
	private <T> T getService(Class<T> clazz, Creator<T> creator) {
		T ret = getService(clazz);
		if (ret==null) {
			ret = creator.create();
			putService(ret);
		}
		return ret;
	}
	
	private static interface Creator<T> {
		public T create();
	}

	@Override
	public EmbosserCatalogService getEmbosserCatalogService() {
		return getService(
				EmbosserCatalogService.class,
				()->invokeStatic("org.daisy.braille.consumer.embosser.EmbosserCatalog", "newInstance"));
	}

	@Override
	public PaperCatalogService getPaperCatalogService() {
		return getService(
				PaperCatalogService.class,
				()->invokeStatic("org.daisy.braille.consumer.paper.PaperCatalog", "newInstance"));
	}

	@Override
	public TableCatalogService getTableCatalogService() {
		return getService(
				TableCatalogService.class,
				()->invokeStatic("org.daisy.braille.consumer.table.TableCatalog", "newInstance"));
	}

	@Override
	public BrailleTranslatorFactoryMakerService getBrailleTranslatorFactoryMakerService() {
		return getService(
				BrailleTranslatorFactoryMakerService.class,
				()->invokeStatic("org.daisy.dotify.consumer.translator.BrailleTranslatorFactoryMaker", "newInstance"));
	}

	@Override
	public ValidatorFactoryService getValidatorFactoryService() {
		return getService(
				ValidatorFactoryService.class,
				()->invokeStatic("org.daisy.braille.consumer.validator.ValidatorFactory", "newInstance"));
	}

	@Override
	public Integer2TextFactoryMakerService getInteger2TextFactoryMakerService() {
		return getService(
				Integer2TextFactoryMakerService.class,
				()->invokeStatic("org.daisy.dotify.consumer.text.Integer2TextFactoryMaker", "newInstance"));
	}

	@Override
	public HyphenatorFactoryMakerService getHyphenatorFactoryMakerService() {
		return getService(
				HyphenatorFactoryMakerService.class,
				()->invokeStatic("org.daisy.dotify.consumer.hyphenator.HyphenatorFactoryMaker", "newInstance"));
	}

	@Override
	public FormatterEngineFactoryService getFormatterEngineFactoryService() {
		return getService(
				FormatterEngineFactoryService.class,
				()->{
					FormatterEngineFactoryService ret = ServiceLoader.load(FormatterEngineFactoryService.class).iterator().next();
					ret.setCreatedWithSPI();
					return ret;
			});
	}

	@Override
	public TaskSystemFactoryMakerService getTaskSystemFactoryMakerService() {
		return getService(
				TaskSystemFactoryMakerService.class,
				()->invokeStatic("org.daisy.dotify.consumer.tasks.TaskSystemFactoryMaker", "newInstance"));
	}

	@Override
	public IdentityProviderService getIdentityProviderService() {
		return getService(
				IdentityProviderService.class,
				()->invokeStatic("org.daisy.dotify.consumer.identity.IdentityProvider", "newInstance"));
	}

	@Override
	public PagedMediaWriterFactoryMakerService getPagedMediaWriterFactoryService() {
		return getService(
				PagedMediaWriterFactoryMakerService.class, 
				()->invokeStatic("org.daisy.dotify.consumer.writer.PagedMediaWriterFactoryMaker", "newInstance"));
	}

	@SuppressWarnings("unchecked")
	private static <T> T invokeStatic(String clazz, String method) {
		T instance = null;
		try {
			Class<?> cls = Class.forName(clazz);
			Method m = cls.getMethod(method);
			instance = (T)m.invoke(null);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to create instance.", e);
		}
		return instance;
	}

	@Override
	public void stop() {
	}

	@Override
	public boolean isStarted() {
		return true;
	}
}
