package org.daisy.dotify.devtools.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.daisy.dotify.api.text.Integer2TextConfigurationException;
import org.daisy.dotify.api.text.Integer2TextFactoryMakerService;
import org.daisy.dotify.api.text.IntegerOutOfRange;

public class Int2TextPanel extends MyPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8051107255963928066L;
	private final JTextField textField;
	private final JTextArea outputField;
	private final FactoryContext context;
	
	public Int2TextPanel(FactoryContext context) {
		super(new BorderLayout());
		this.context = context;

		Font f = new Font(null, Font.BOLD, 26);

		outputField = new JTextArea();
		outputField.setFont(f);
		outputField.setEditable(false);

		textField = new JTextField();
		textField.setFont(f);

		textField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				updateResult();
			}

		});

		add(textField, BorderLayout.PAGE_START);
		add(new JScrollPane(outputField), BorderLayout.CENTER);

		setPreferredSize(new Dimension(500, 400));
	}

	@Override
	protected void updateResult() {
		if (textField.getText().equals("")) {
			outputField.setText("");
		} else if (getTargetLocale()==null || getTargetLocale().equals("")) {
			outputField.setText("No locale selected");
		} else {
			Integer2TextFactoryMakerService t = context.getInteger2TextFactoryMakerService();
			if (t == null) {
				outputField.setText("No conversion");
			} else {
				try {
					try {
						outputField.setText(t.newInteger2Text(getTargetLocale()).intToText(Integer.parseInt(textField.getText())));
					} catch (Integer2TextConfigurationException e1) {
						outputField.setText("Locale not supported: " + getTargetLocale() + "\n");
						outputField.append("Supported values:\n");
						for (String s : t.listLocales()) {
							outputField.append(s+"\n");
						}
					}
				} catch (NumberFormatException e1) {
					outputField.setText("Not a valid integer");
				} catch (IntegerOutOfRange e1) {
					outputField.setText("Value not in supported range.");
				}
			}
		}
	}

}
