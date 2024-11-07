package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Home extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Color backgroundColorBtn = new Color(24, 188, 156);

	public Home(MouseListener mouseListener) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 1, 0, 10));
		JButton newGameBtn = new JButton("New game");
		newGameBtn.setActionCommand(newGameBtn.getText());
		JButton optionBtn = new JButton("Option");
		optionBtn.setActionCommand(optionBtn.getText());
		JButton exitBtn = new JButton("Exit");
		exitBtn.setActionCommand(exitBtn.getText());

		newGameBtn.setOpaque(true);
		newGameBtn.setContentAreaFilled(true);
		newGameBtn.setBorderPainted(false);
		newGameBtn.setBackground(backgroundColorBtn);
		newGameBtn.setForeground(Color.WHITE);
		newGameBtn.setFont(new Font("roboto", Font.BOLD, 12));

		optionBtn.setOpaque(true);
		optionBtn.setContentAreaFilled(true);
		optionBtn.setBorderPainted(false);
		// optionBtn.setFocusPainted(false);
		optionBtn.setBackground(backgroundColorBtn);
		optionBtn.setForeground(Color.WHITE);
		optionBtn.setFont(new Font("roboto", Font.BOLD, 12));

		exitBtn.setOpaque(true);
		exitBtn.setContentAreaFilled(true);
		exitBtn.setBorderPainted(false);
		// exitBtn.setFocusPainted(false);
		exitBtn.setBackground(new Color(238, 102, 119));
		exitBtn.setForeground(Color.WHITE);
		exitBtn.setFont(new Font("roboto", Font.BOLD, 12));

		newGameBtn.addMouseListener(mouseListener);
		optionBtn.addMouseListener(mouseListener);
		exitBtn.addMouseListener(mouseListener);
		panel.add(newGameBtn);
		panel.add(optionBtn);
		panel.add(exitBtn);
		panel.setPreferredSize(new Dimension(100, 200));
		panel.setMaximumSize(new Dimension(100, 200));
		this.add(Box.createVerticalGlue());
		this.add(panel);
		this.add(Box.createVerticalGlue());

	}

}
