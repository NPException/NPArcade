package de.npe.mcmods.nparcade.client.debug;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DebugWindow extends JFrame {

	private final DateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS zzz");

	private JTextArea content;
	private ArrayDeque<String> messages = new ArrayDeque<>();

	public DebugWindow(String title) {
		super(title);
		setAutoRequestFocus(false);

		setLayout(new BorderLayout());

		content = new JTextArea(30, 160);
		content.setFont(Font.decode(Font.MONOSPACED));
		content.setEditable(false);
		add(new JScrollPane(content), BorderLayout.CENTER);

		// custom close behaviour
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	public void addMessage(final String message) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				boolean makeVisible = !isDisplayable();
				if (makeVisible) {
					pack();
					setFocusableWindowState(false);
					setVisible(true);
					setFocusableWindowState(true);
				}
				content.append(dateFormatter.format(new Date()) + ": " + message + "\n");
			}
		});
	}
}
