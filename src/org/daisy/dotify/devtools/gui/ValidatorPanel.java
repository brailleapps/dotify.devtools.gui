package org.daisy.dotify.devtools.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.daisy.braille.utils.pef.PEFBook;
import org.daisy.streamline.api.validity.ValidationReport;
import org.daisy.streamline.api.validity.Validator;
import org.daisy.streamline.api.validity.ValidatorFactoryMakerService;
import org.daisy.streamline.api.validity.ValidatorMessage;
import org.xml.sax.SAXException;

public class ValidatorPanel extends MyPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8051107255963928066L;
	private final JButton chooseFile;
	private final JFileChooser chooser;
	private final JTextArea outputField;
	private final FactoryContext context;
	private File input;
	
	public ValidatorPanel(FactoryContext context) {
		super(new BorderLayout());
		this.context = context;
		
		input = null;

		Font f = new Font(null, Font.BOLD, 26);

		outputField = new JTextArea();
		outputField.setFont(f);
		outputField.setEditable(false);
		
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "PEF-files", "pef");

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
		ValidatorFactoryMakerService t = context.getValidatorFactoryService();
		if (t == null) {
			outputField.setText("No formatter detected");
		} else if (input==null) {
			outputField.setText("No file selected.");
			chooseFile.setText("Choose file...");
		} else if (!input.isFile()) { 
			outputField.setText("File doesn't exist " + input);
		} else {
			try {
				Validator e = t.newValidator("application/x-pef+xml");
				ValidationReport valid = e.validate(input.toURI().toURL());
				outputField.setText("Done! " + input+"\n");
				for (ValidatorMessage line : valid.getMessages()) {
					outputField.append(line+"\n");
				}
				if (valid.isValid()) {
					outputField.append("\n --- Info ---\n");
					try {
						PEFBook p = PEFBook.load(input.toURI());
						outputField.append(p.toString().replaceAll("\\s+", " ").replaceAll(", ", ", \n"));
					} catch (XPathExpressionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParserConfigurationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SAXException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} catch (FileNotFoundException e1) {
				outputField.setText(e1.toString());
			} catch (MalformedURLException e) {
				e.printStackTrace();
				outputField.setText(e.toString());
			} catch (IOException e) {
				e.printStackTrace();
				outputField.setText(e.toString());
			}
		}
	}

}
