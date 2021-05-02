package gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class MyProgressBar {
	private JFrame frameBar;
	private JProgressBar bar;

	public MyProgressBar(int size) {
		frameBar = new JFrame();
		frameBar.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		JPanel p = new JPanel();

		bar = new JProgressBar(0, size);
		bar.setValue(0);
		bar.setStringPainted(true);

		p.add(bar);
		frameBar.getContentPane().add(p);
		frameBar.pack();

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frameBar.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frameBar.getHeight()) / 2);

		frameBar.setLocation(x, y);
		frameBar.setVisible(true);
	}

	public void updateProgressBar() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				bar.setValue(bar.getValue() + 1);
			}
		});
	}

	public void closeProgressBar() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				frameBar.dispose();
			}
		});
	}
	
	public boolean isActive() {
		return frameBar.isDisplayable();
	}
}
