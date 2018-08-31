package org.daisy.dotify.devtools.gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

import org.daisy.braille.utils.api.factory.FactoryProperties;
import org.daisy.braille.utils.api.table.BrailleConverter;
import org.daisy.braille.utils.api.table.TableCatalogService;
import org.daisy.dotify.api.translator.BrailleTranslator;
import org.daisy.dotify.api.translator.BrailleTranslatorFactory;
import org.daisy.dotify.api.translator.BrailleTranslatorFactoryMakerService;
import org.daisy.dotify.api.translator.BrailleTranslatorResult;
import org.daisy.dotify.api.translator.Translatable;
import org.daisy.dotify.api.translator.TranslationException;
import org.daisy.dotify.api.translator.TranslatorConfigurationException;


public class TranslatorDemo extends MyPanel {
	private static final String USER_PREFS_KEY_TABLE = "tableIdentifier";
	static final String DEFAULT_LIMIT = "30";
	final JScrollPane textPanelScroll;
	final JTextArea textPanel;
	final JScrollPane braillePanelScroll;
	final JTextArea braillePanel;
	final JScrollPane braille2PanelScroll;
	final JTextArea braille2Panel;
	final JComboBox<TableWrapper> tableSelect;
	final JCheckBox hyphenate;
	final JCheckBox translate;
	final JTextField limitBox;
	final Font odtFont;
	final Font sysFont;
	private String convId;
	private int limit;
	private volatile boolean updateNeeded;
	private final Preferences userPrefs;
	private final FactoryContext context;
	private final ActionListener listener;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3237476094594239863L;

	public TranslatorDemo(FactoryContext context) {
		this.textPanel = new JTextArea(5, 40);
		this.textPanelScroll = new JScrollPane(textPanel);
		this.braillePanel = new JTextArea(10, 40);
		this.braillePanelScroll = new JScrollPane(braillePanel);
		this.braille2Panel = new JTextArea(10, 40);
		this.braille2PanelScroll = new JScrollPane(braille2Panel);
		this.tableSelect = new JComboBox<>();
		this.hyphenate = new JCheckBox("Hyphenate");
		this.translate = new JCheckBox("As text");
		this.userPrefs = Preferences.userNodeForPackage(TranslatorDemo.class);
		this.context = context;
        this.listener = new TableSelectActionListener();

		this.limitBox = new JTextField(3);
		this.limit = 30;
		sysFont = new Font(Font.MONOSPACED, Font.PLAIN, 18);
		try {
			InputStream fs = this.getClass().getResourceAsStream("resource-files/odt2braille6.ttf");
			odtFont = Font.createFont(Font.TRUETYPE_FONT, fs);
			fs.close();
		} catch (Exception e) {
			throw new RuntimeException("Font init failed.", e);
		}
		
		try {
			initComponents();
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateNeeded = false;
    	new Thread(new Updater()).start();
	}

    private void initComponents() throws FontFormatException, IOException {
    	tableSelect.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				updateTableList();
			}
		});
        
        textPanel.setLineWrap(true);
        braillePanel.setFont(sysFont);
        

        braille2Panel.setFont(odtFont.deriveFont(24f));

		hyphenate.addActionListener(new HyphenateActionListener());
		hyphenate.setSelected(true);
		//hyphenate.setBackground(Color.BLACK);
		//hyphenate.setForeground(Color.WHITE);
		
		translate.addActionListener(new TranslateActionListener());
		translate.setSelected(false);

		limitBox.addActionListener(new LimitActionListener());
		limitBox.setText(DEFAULT_LIMIT);

        KeyListener kl = new InputKeyListener();
        textPanel.addKeyListener(kl);
        
        braille2Panel.setEditable(false);
		//Color c = new Color(250, 250, 250);
        //braille2Panel.setBackground(c);
        braillePanel.setEditable(false);
        //braillePanel.setBackground(c);

		javax.swing.GroupLayout mainLayout = new javax.swing.GroupLayout(this);
		setLayout(mainLayout);
		//setBackground(Color.BLACK);
        mainLayout.setAutoCreateGaps(true);
        mainLayout.setAutoCreateContainerGaps(true);
        JLabel textLabel = new JLabel("Input");
        //textLabel.setForeground(Color.GRAY);
		JLabel brailleLabel = new JLabel("using table");
        //brailleLabel.setForeground(Color.WHITE);
        
        JLabel braille2Label = new JLabel("Braille");
        //braille2Label.setForeground(Color.GRAY);
        
    	JLabel limitLabel;
		limitLabel = new JLabel("Columns");
		//limitLabel.setForeground(Color.WHITE);
        GroupLayout.SequentialGroup h2Group = mainLayout.createSequentialGroup();
        h2Group.addGroup(mainLayout.createParallelGroup()
        		.addComponent(textLabel)
        		.addComponent(textPanelScroll)
        		.addGroup(
        				mainLayout.createSequentialGroup()
        				.addComponent(hyphenate)
        				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED,
                     GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(limitLabel)
        				.addComponent(limitBox)
        			)
        		.addComponent(braille2Label)
        		.addComponent(braille2PanelScroll)
        		.addGroup(
        				mainLayout.createSequentialGroup()
        				.addComponent(translate)
        				.addComponent(brailleLabel)
        				.addComponent(tableSelect)
        			)
        		);
        mainLayout.setHorizontalGroup(h2Group);
        
        GroupLayout.SequentialGroup v2Group = mainLayout.createSequentialGroup();
        v2Group
    	.addComponent(textLabel)
    	.addComponent(textPanelScroll)
    	.addGroup(
    			mainLayout.createParallelGroup(Alignment.CENTER, false)
    			.addComponent(hyphenate)
    			.addComponent(limitLabel)
    			.addComponent(limitBox)
    		)
    	.addComponent(braille2Label)
    	.addComponent(braille2PanelScroll)
    	.addGroup(
mainLayout.createParallelGroup(Alignment.CENTER, false)
    			.addComponent(translate)
    			.addComponent(brailleLabel)
    			.addComponent(tableSelect)
);
        mainLayout.setVerticalGroup(v2Group);
        
    }
    
/*
	TableCatalogService tc = context.getTableCatalogService();
	Collection<FactoryProperties> list = tc.list();
	int currentTable = 0;
	String def = userPrefs.get(USER_PREFS_KEY_TABLE, "");

	int i = 0;
	for (FactoryProperties t : list) {
		tableSelect.addItem(new TableWrapper(t));
		if (t.getIdentifier().equals(def)) {
			currentTable = i;
		}
		i++;
	}
	tableSelect.addActionListener(new TableSelectActionListener());

	tableSelect.setSelectedIndex(currentTable);
    */
	void updateTableList() {
		tableSelect.removeActionListener(listener);
		tableSelect.removeAllItems();
		TableCatalogService tt = context.getTableCatalogService();
		if (tt != null) {
			String def = userPrefs.get(USER_PREFS_KEY_TABLE, "");
			ArrayList<FactoryProperties> sorted = new ArrayList<FactoryProperties>(tt.list());
			Collections.sort(sorted, FactoryProperties.newComparatorBuilder().build());
			int currentTable = 0;
			int i = 0;
			for (FactoryProperties p : sorted) {
				tableSelect.addItem(new TableWrapper(p));
				if (p.getIdentifier().equals(def)) {
					currentTable = i;
					convId = p.getIdentifier();
				}
				i++;
			}
			try {
				tableSelect.setSelectedIndex(currentTable);
			} catch (IllegalArgumentException e) {
			}
		}
		tableSelect.addActionListener(listener);
	}
    
    public void updateLimit() {
    	boolean failed = false;
    	try {
    		limit = Integer.parseInt(limitBox.getText());
        	if (limit<5 || limit>50) {
        		failed = true;
    		}
    	} catch (NumberFormatException e) {
    		failed = true;
    	}
    	if (failed) {
    		JOptionPane.showMessageDialog(null, "Enter a number between 5 and 50", "Error", JOptionPane.ERROR_MESSAGE);
    		limitBox.setText(DEFAULT_LIMIT);
    		limit = Integer.parseInt(limitBox.getText());
    	}
		updateTranslation();
    }
    
    public void updateHyphenating() {
    	updateTranslation();
    }
    
	public void updateTranslating() {
		braille2PanelScroll.setViewportView(translate.isSelected() ? braillePanel : braille2Panel);
	}

    public void updateConverter() {
    	convId = ((TableWrapper)tableSelect.getSelectedItem()).t.getIdentifier();
    	updateTranslation();
    }
    
    public void updateTranslation() {
    	updateNeeded = true;
    }
    
    @Override
    protected void updateResult() {
		String loc = getTargetLocale();
		if (loc==null || loc.equals("")) {
			braillePanel.setText("No locale selected");
		} else {
			if (textPanel == null) {
				return;
			}
			StringBuilder sb = new StringBuilder();
			StringBuilder sb2 = new StringBuilder();
	
			BrailleTranslatorFactoryMakerService ts = context.getBrailleTranslatorFactoryMakerService();
			if (ts==null) {
				sb.append("No conversion");
			} else if (!textPanel.getText().equals("")) {
				BrailleTranslator t;
				try {
					t = ts.newTranslator(loc, BrailleTranslatorFactory.MODE_UNCONTRACTED);
					BrailleConverter conv = context.newBrailleConverter(convId);
					BrailleTranslatorResult btr = t.translate(Translatable.text(textPanel.getText()).hyphenate(hyphenate.isSelected()).build());
					while (btr.hasNext()) {
						String braille = btr.nextTranslatedRow(limit, true);
						sb2.append(braille+"\n");
						if (conv!=null) {
							try {
								sb.append(conv.toText(braille)+"\n");
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							}
						}
					}
				} catch (TranslatorConfigurationException e) {
					sb.append("Specification not supported.");
				} catch (TranslationException e) {
					sb.append("Failed to translate.");
				}
				/*
				if (((TableWrapper)tableSelect.getSelectedItem()).t.getIdentifier().equals("org_daisy.EmbosserTableProvider.TableType.UNICODE_BRAILLE")) {
			        braillePanel.setFont(odtFont.deriveFont(24f));
				} else {
					braillePanel.setFont(sysFont);
				}*/
			}
			braille2Panel.setText(sb2.toString());
			braillePanel.setText(sb.toString());	
			updateNeeded = false;
		}
    }
    
    private class Updater implements Runnable {
    	private volatile boolean stop;
    	Updater() {
    		stop = false;
    	}

		@Override
		public void run() {
			while (!stop) {
				if (updateNeeded) {
					updateResult();
				} else {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		}
    }
    
    private class TableWrapper {
    	private final FactoryProperties t;
    	public TableWrapper(FactoryProperties t) {
    		this.t = t;
    	}
		@Override
		public String toString() {
			return t.getDisplayName();
		}
    	
    }
    
    public class TableSelectActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JComboBox) {
				JComboBox<?> box = (JComboBox<?>) e.getSource();
				Object o = box.getSelectedItem();
				if (o instanceof TableWrapper) {
					TableWrapper tr = (TableWrapper) box.getSelectedItem();
					userPrefs.put(USER_PREFS_KEY_TABLE, tr.t.getIdentifier());
					try {
						userPrefs.sync();
					} catch (BackingStoreException e1) {
					}
				}
			}
			updateConverter();
		}
    	
    }
    
    public class HyphenateActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			updateHyphenating();
		}
    	
    }
    
	public class TranslateActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			updateTranslating();
		}
	}

    public class LimitActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			updateLimit();
		}
    	
    }
    
    public class InputKeyListener implements KeyListener {

    	public InputKeyListener() { }

		@Override
		public void keyPressed(KeyEvent e) { }

		@Override
		public void keyReleased(KeyEvent e) { updateTranslation(); }

		@Override
		public void keyTyped(KeyEvent e) { }
    	
    }
    
}
