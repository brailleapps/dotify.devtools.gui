package org.daisy.dotify.devtools.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.daisy.dotify.api.engine.FormatterEngine;
import org.daisy.dotify.api.engine.FormatterEngineFactoryService;
import org.daisy.dotify.api.translator.TranslatorMode;
import org.daisy.dotify.api.translator.TranslatorType;
import org.daisy.dotify.api.writer.MediaTypes;
import org.daisy.dotify.api.writer.PagedMediaWriter;
import org.daisy.dotify.api.writer.PagedMediaWriterFactoryMakerService;

public class FormatterPanel extends MyPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8051107255963928066L;
	private final JButton chooseFile;
	private final JFileChooser chooser;
	private final JTextArea outputField;
	private final FactoryContext context;
	private File input;
	
	public FormatterPanel(FactoryContext context) {
		super(new BorderLayout());
		this.context = context;
		
		input = null;

		Font f = new Font(null, Font.BOLD, 26);

		outputField = new JTextArea();
		outputField.setFont(f);
		outputField.setEditable(false);
		
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "OBFL-files", "obfl");

		chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		
		chooseFile = new JButton("Choose file...");
		chooseFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal =chooser.showOpenDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       input = chooser.getSelectedFile();
			       chooseFile.setText(chooser.getSelectedFile().getName());
			       updateResult();
			    }
			}});

		add(chooseFile, BorderLayout.PAGE_START);
		add(new JScrollPane(outputField), BorderLayout.CENTER);

		setPreferredSize(new Dimension(500, 400));
	}

	@Override
	protected void updateResult() {
		if (input==null) {
			outputField.setText("No file selected.");
			chooseFile.setText("Choose file...");
		} else if (getTargetLocale()==null || getTargetLocale().equals("")) {
			outputField.setText("No locale selected");
		} else if (!input.isFile()) { 
			outputField.setText("File doesn't exist " + input);
		} else {
			FormatterEngineFactoryService t = context.getFormatterEngineFactoryService();
			PagedMediaWriterFactoryMakerService w = context.getPagedMediaWriterFactoryService();
			if (t == null) {
				outputField.setText("No formatter detected");
			} else if (w == null) {
				outputField.setText("No writer detected");
			} else {
				outputField.setText("Converting " + input + "...");
				try {
					new SwingWorker<Void, Void>() {
						File out;

						@Override
						protected Void doInBackground() throws Exception {
							PagedMediaWriter pw = w.newPagedMediaWriter(MediaTypes.PEF_MEDIA_TYPE);
							FormatterEngine e = t.newFormatterEngine(getTargetLocale(), TranslatorMode.withType(TranslatorType.UNCONTRACTED).toString(), pw);
							out = new File(input.getParentFile(), input.getName()+".pef");
						
							e.convert(new FileInputStream(input), new FileOutputStream(out));
							return null;
						}

						@Override
						protected void done() {
							outputField.setText("Done! " + out);
							super.done();
						}

					}.execute();
				} catch (Exception e1) {
					e1.printStackTrace();
					outputField.setText(e1.toString());
				}
			}
		}
	}

}
