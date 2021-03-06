package org.daisy.dotify.devtools.gui;

import java.awt.LayoutManager;

import javax.swing.JPanel;

public abstract class MyPanel extends JPanel {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2849680556886257966L;
	private String locale = "";

	public MyPanel() {
		super();
	}

	public MyPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public MyPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public MyPanel(LayoutManager layout) {
		super(layout);
	}
	
	public String getTargetLocale() {
		return locale;
	}
	
	public void setTargetLocale(String locale) {
		this.locale = locale;
		updateResult();
	}

	protected abstract void updateResult();
	
	
}

