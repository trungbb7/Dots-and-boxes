package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.MatchButtonController;

public class Match extends JPanel {

	GamePanel gamePanel;
	JPanel top;

	public Match() {
		setLayout(new BorderLayout());
		gamePanel = new GamePanel();
		this.add(gamePanel, BorderLayout.CENTER);
	}

	public void addTop() {
		GameManager gameManager = GameManager.getInstance();
		JPanel top = new Top();
		this.add(top, BorderLayout.NORTH);
		gameManager.frame.setVisible(false);
		gameManager.frame.setVisible(true);
	}

	private class Top extends JPanel {

		private Top() {
			GameManager gameManager = GameManager.getInstance();
			this.setLayout(new FlowLayout(FlowLayout.CENTER));
			MouseListener btnController = new MatchButtonController(gameManager);
			JButton homeBtn = new JButton("Home");
			homeBtn.setActionCommand("Home");
			homeBtn.addMouseListener(btnController);
			JButton replayBtn = new JButton("Replay");
			replayBtn.setActionCommand("Replay");
			replayBtn.addMouseListener(btnController);
			this.add(homeBtn);
			this.add(replayBtn);
		}
	}
}
