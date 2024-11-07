package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import view.GameManager;

public class HomeController extends MouseAdapter {

	public HomeController() {
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		GameManager gameManager = GameManager.getInstance();
		JButton btn = (JButton) e.getSource();
		String command = btn.getActionCommand();
		switch (command) {
			case "New game" :
				gameManager.newGame();
				break;
			case "Option" :
				gameManager.goOption();
				break;
			case "Exit" :
				System.exit(0);
				break;
		}
		super.mouseClicked(e);
	}
}
