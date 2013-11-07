package org.daisy.dotify.devtools.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import org.daisy.dotify.api.text.Integer2TextConfigurationException;
import org.daisy.dotify.api.text.Integer2TextFactoryMakerService;
import org.daisy.dotify.api.text.IntegerOutOfRange;
import org.osgi.framework.BundleContext;

public class Int2TextPanel extends MyPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8051107255963928066L;
	private final JTextField textField;
	private final JTextField outputField;

	private Int2TextTracker tracker;
	
	public Int2TextPanel() {
		super(new GridLayout(2, 1));

		Font f = new Font(null, Font.BOLD, 26);

		outputField = new JTextField();
		outputField.setFont(f);
		outputField.setEditable(false);

		textField = new JTextField();
		textField.setFont(f);

		textField.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				updateResult();
			}

		});

		add(textField);
		add(outputField);

		setPreferredSize(new Dimension(500, 400));
	}

	protected void updateResult() {
		if (getTargetLocale()==null || getTargetLocale().equals("")) {
			outputField.setText("No locale selected");
		} else {
			try {
				Integer2TextFactoryMakerService t = tracker.get();
				if (t == null) {
					outputField.setText("No conversion");
				} else {
					if (textField.getText().equals("")) {
						outputField.setText("");
					} else {
						try {
							outputField.setText(t.newInteger2Text(getTargetLocale()).intToText(Integer.parseInt(textField.getText())));
						} catch (NumberFormatException e1) {
							outputField.setText("Not a valid integer");
						} catch (IntegerOutOfRange e1) {
							outputField.setText("Value not in supported range.");
						}
					}
				}
			} catch (Integer2TextConfigurationException e1) {
				outputField.setText("Locale not supported: " + getTargetLocale());
			}
		}
	}

	public void openTracking(BundleContext context) {
		tracker = new Int2TextTracker(context);
		tracker.open();
	}

	public void closeTracking() {
		tracker.close();
	}

}
