package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;

import view.Option;

public class ApplyButtonController extends MouseAdapter {

	Option option;

	public ApplyButtonController(Option option) {
		this.option = option;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		GameManager gameManager = GameManager.getInstance();
		applyHandler();
		gameManager.backHome();
		super.mouseClicked(e);
	}

	private void applyHandler() {
		try {
			GameManager gameManager = GameManager.getInstance();
			String levelConfig = option.getLevelBG().getSelection().getActionCommand();
			String typeConfig = option.getTypeBG().getSelection().getActionCommand();
			gameManager.boardSize = Integer.parseInt(typeConfig);
			FileWriter fileWriter = new FileWriter("src\\config");
			fileWriter.write("level:" + levelConfig + "\ntype:" + typeConfig);
			gameManager.aiLevel = Integer.parseInt(levelConfig);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
