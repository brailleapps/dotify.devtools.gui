package org.daisy.dotify.devtools.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainFrame extends JFrame {
	private final Logger logger = Logger.getLogger(MainFrame.class.getCanonicalName());
	/**
	 * 
	 */
	private static final long serialVersionUID = -5599508953697168226L;
	private JTextField loc;
	private ArrayList<MyPanel> panels;
	
	final JTabbedPane pane;
	final TranslatorPanel panel3;
	final TranslatorDemo panel3b;
	final EmbossPanel panel6;
	
	public MainFrame(String title, final FactoryContext context) {
		//getContentPane().set
		super(title);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				context.stop();
				System.exit(0);
			}
		});
		setLayout(new BorderLayout());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth()*0.5;
		double height = screenSize.getHeight()*0.7;
		setPreferredSize(new Dimension(Math.max(600, (int)width), Math.max(400, (int)height)));

		panels = new ArrayList<MyPanel>();
		
		Int2TextPanel panel1 = new Int2TextPanel(context);
		HyphPanel panel2 = new HyphPanel(context);
		panel3 = new TranslatorPanel(context);
		panel3b = new TranslatorDemo(context);
		FormatterPanel panel4 = new FormatterPanel(context);
		ValidatorPanel panel5 = new ValidatorPanel(context);
		panel6 = new EmbossPanel(context);
		
		loc = new JTextField();
		loc.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				updateLocale();
			}

		});
		pane = new JTabbedPane();
		JComponent panel11 = new CodePointUtility();
		pane.addTab("Code Points", null, panel11, "Convert characters to codepoints");
		pane.setMnemonicAt(0, KeyEvent.VK_1);
		
		addPanel("Numbers", pane, panel1);
		addPanel("Hyphenation", pane, panel2);
		addPanel("Translation", pane, panel3);
		addPanel("Translation2", pane, panel3b);
		addPanel("Formatter", pane, panel4);
		addPanel("Validator", pane, panel5);
		addPanel("Emboss", pane, panel6);
		
		pane.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				updateSelectedPane();
			}
		});
		addWindowFocusListener(new WindowFocusListener() {
			
			@Override
			public void windowLostFocus(WindowEvent arg0) {
				logger.finer("Window focus lost.");
				
			}
			
			@Override
			public void windowGainedFocus(WindowEvent arg0) {
				logger.finer("Window focus gained.");
				updateSelectedPane();
				
			}
		});
		
		JPanel north = new JPanel(new BorderLayout());
		north.add(new JLabel("Locale:"), BorderLayout.WEST);
		north.add(loc, BorderLayout.CENTER);
		add(north, BorderLayout.NORTH);
		add(pane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				try {
					for (int i=0; i<100 && !context.isStarted(); i++) {
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				return null;
			}

			@Override
			protected void done() {
				super.done();
				if (context.isStarted() && "".equals(loc.getText())) {
					loc.setText(Locale.getDefault().toLanguageTag());
					updateLocale();
				}
			}
		}.execute();
	}
	
	private void updateSelectedPane() {
		if (pane.getSelectedComponent()==panel6) {
			panel6.updateLists();
		} else if (pane.getSelectedComponent()==panel3) {
			panel3.updateTableList();
		} else if (pane.getSelectedComponent()==panel3b) {
			panel3b.updateTableList();
		}
	}
	
	private void addPanel(String desc, JComponent panel, MyPanel p) {
		panel.add(p, desc);
		panels.add(p);
	}
	
	private void updateLocale() {
		for (MyPanel p : panels) {
			p.setTargetLocale(loc.getText());
		}
	}

}
