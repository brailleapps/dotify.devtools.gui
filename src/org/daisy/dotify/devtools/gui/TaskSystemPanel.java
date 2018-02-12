package org.daisy.dotify.devtools.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.daisy.streamline.api.identity.IdentityProviderService;
import org.daisy.streamline.api.media.AnnotatedFile;
import org.daisy.streamline.api.tasks.CompiledTaskSystem;
import org.daisy.streamline.api.tasks.TaskSystem;
import org.daisy.streamline.api.tasks.TaskSystemFactoryMakerService;
import org.daisy.streamline.engine.TaskRunner;

public class TaskSystemPanel extends MyPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8051107255963928066L;
	private final JButton chooseFile;
	private final JFileChooser chooser;
	private final JTextArea outputField;
	private final FactoryContext context;
	private File input;
	
	public TaskSystemPanel(FactoryContext context) {
		super(new BorderLayout());
		this.context = context;
		
		input = null;

		Font f = new Font(null, Font.BOLD, 26);

		outputField = new JTextArea();
		outputField.setFont(f);
		outputField.setEditable(false);
		
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "Supported files", "obfl", "xml", "html", "txt");

	    JButton button = new JButton("Start");
		chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		
		chooseFile = new JButton("Choose file...");
		chooseFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal =chooser.showOpenDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       input = chooser.getSelectedFile();
			       button.setEnabled(true);
			       chooseFile.setText(chooser.getSelectedFile().getName());
			    }
			}});

		button.setEnabled(false);
		button.addActionListener(e->{updateResult();});
		
		JPanel panel = new JPanel();
		panel.add(chooseFile);
		panel.add(button);
		add(panel, BorderLayout.PAGE_START);
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
			TaskSystemFactoryMakerService t = context.getTaskSystemFactoryMakerService();
			IdentityProviderService id = context.getIdentityProviderService();
			if (t == null) {
				outputField.setText("No task system detected");
			} else if (id == null) {
				outputField.setText("No identity provider");
			} else {
				outputField.setText("Converting " + input + "...");
				try {
					new SwingWorker<Void, Void>() {
						File out;

						@Override
						protected Void doInBackground() throws Exception {
							
							AnnotatedFile ai = id.identify(input);
							try {
								//TODO: error handling
								TaskSystem ts = t.newTaskSystem(getFormatString(ai), "pef", getTargetLocale());
								out = new File(input.getParentFile(), input.getName()+".pef");
								//TODO: present, edit and use options
								CompiledTaskSystem cts = ts.compile(Collections.emptyMap());
								TaskRunner tr = new TaskRunner.Builder().build();
								tr.runTasks(ai, out, cts);
							} catch (Exception e) {
								e.printStackTrace();
								throw e;
							}

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
	
	private static String getFormatString(AnnotatedFile f) {
		if (f.getFormatName()!=null) {
			return f.getFormatName();
		} else if (f.getExtension()!=null) {
			return f.getExtension();
		} else if (f.getMediaType()!=null) {
			return f.getMediaType();
		} else {
			return null;
		}
	}

}
