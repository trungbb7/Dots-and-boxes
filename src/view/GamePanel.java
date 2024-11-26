package view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import controller.GameController;
import controller.GameManager;

public class GamePanel extends JPanel {

	private int spacing;
	private int dotSize;

	public GamePanel() {
		this.dotSize = 20;

		this.addMouseListener(new GameController(this));
	}

	@Override
	public void paintComponent(Graphics g) {
		GameManager gameManager = GameManager.getInstance();
		super.paintComponent(g);
		int width = getWidth();
		int height = getHeight();
		int minSpace = Math.min(width, height);
		this.spacing = (minSpace - dotSize * (gameManager.boardSize + 1) - 40) / (gameManager.boardSize + 1);

		// Vẽ các điểm
		for (int i = 0; i <= gameManager.boardSize; i++) {
			for (int j = 0; j <= gameManager.boardSize; j++) {
				int x = spacing + j * (dotSize + spacing);
				int y = spacing + i * (dotSize + spacing);
				g.fillOval(x, y, dotSize, dotSize);
			}
		}
		// Vẽ các đường kẻ ngang
		for (int i = 0; i < gameManager.horizontalLines.length; i++) {
			for (int j = 0; j < gameManager.horizontalLines[0].length; j++) {
				if (gameManager.horizontalLines[i][j] != 0) {
					if (gameManager.horizontalLines[i][j] == GameManager.PLAYER_SIGN)
						g.setColor(Color.RED);
					else
						g.setColor(Color.BLUE);

					int x1 = spacing + j * (dotSize + spacing) + (dotSize / 2);
					int y1 = spacing + i * (dotSize + spacing) + dotSize / 2;
					int x2 = x1 + dotSize + spacing;
					int y2 = y1;
					g.drawLine(x1, y1, x2, y2);
				}
			}
		}
		// Vẽ các đường kẻ doc
		for (int i = 0; i < gameManager.verticalLines.length; i++) {
			for (int j = 0; j < gameManager.verticalLines[0].length; j++) {
				if (gameManager.verticalLines[i][j] != 0) {
					if (gameManager.verticalLines[i][j] == GameManager.PLAYER_SIGN)
						g.setColor(Color.RED);
					else
						g.setColor(Color.BLUE);
					int x1 = spacing + j * (dotSize + spacing) + (dotSize / 2);
					int y1 = spacing + i * (dotSize + spacing) + dotSize / 2;
					int x2 = x1;
					int y2 = y1 + dotSize + spacing;
					g.drawLine(x1, y1, x2, y2);
				}
			}
		}

		// Tô màu các ô vuông
		for (int i = 0; i < gameManager.boardSize; i++) {
			for (int j = 0; j < gameManager.boardSize; j++) {
				if (gameManager.boxes[i][j] != 0) {
					if (gameManager.boxes[i][j] == GameManager.PLAYER) {
						g.setColor(Color.RED); // Màu cho người chơi
					} else {
						g.setColor(Color.BLUE); // Màu cho bot
					}
					int x = spacing + j * (dotSize + spacing);
					int y = spacing + i * (dotSize + spacing);
					int size = spacing / 2;
					g.fillOval(x + dotSize + (spacing / 2) - (size / 2), y + dotSize + (spacing / 2) - (size / 2), size,
							size);
				}
			}
		}
	}

	public int getSpacing() {
		return spacing;
	}

	public int getDotSize() {
		return dotSize;
	}

}
