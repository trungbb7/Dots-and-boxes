package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class MatchButtonController extends MouseAdapter {
	
	GameManager gameManager;
	
	public MatchButtonController (GameManager gameManager) {
		this.gameManager = gameManager;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		JButton btn = (JButton) e.getSource();
		String command = btn.getActionCommand();
		switch (command) {
			case "Replay" :
				gameManager.newGame();;
				break;
			case "Home" :
				gameManager.backHome();
				break;
		}

	}

}
