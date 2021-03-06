package org.daisy.dotify.devtools.gui;

import java.util.Collection;

import org.daisy.dotify.api.embosser.EmbosserCatalogService;
import org.daisy.dotify.api.factory.FactoryProperties;
import org.daisy.dotify.api.paper.PaperCatalogService;
import org.daisy.dotify.api.table.BrailleConverter;
import org.daisy.dotify.api.table.Table;
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

public interface FactoryContext {

	public EmbosserCatalogService getEmbosserCatalogService();
	public PaperCatalogService getPaperCatalogService();
	public TableCatalogService getTableCatalogService();
	public BrailleTranslatorFactoryMakerService getBrailleTranslatorFactoryMakerService();
	public ValidatorFactoryMakerService getValidatorFactoryService();
	public Integer2TextFactoryMakerService getInteger2TextFactoryMakerService();
	public HyphenatorFactoryMakerService getHyphenatorFactoryMakerService();
	public ExpressionFactory getExpressionFactory();
	public FormatterEngineFactoryService getFormatterEngineFactoryService();
	public TaskSystemFactoryMakerService getTaskSystemFactoryMakerService();
	public IdentityProviderService getIdentityProviderService();
	public PagedMediaWriterFactoryMakerService getPagedMediaWriterFactoryService();
	public boolean isStarted();
	public void stop();
	
	
	default BrailleConverter newBrailleConverter(String identifier) {
		TableCatalogService tt = getTableCatalogService();
		if (tt != null) {
			if (identifier==null) {
				Collection<FactoryProperties> fp = tt.list();
				if (fp.size()>0) {
					identifier = fp.iterator().next().getIdentifier();									
				}
			}
			if (identifier!=null) {
				Table ttt = tt.newTable(identifier);
				return ttt.newBrailleConverter();
			}							
		}
		return null;
	}

}
