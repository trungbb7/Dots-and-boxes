package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.Move;
import view.GameManager;
import view.GamePanel;

public class GameController extends MouseAdapter {

	GamePanel gamePanel;

	public GameController(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		GameManager gameManager = GameManager.getInstance();
		int spacing = gamePanel.getSpacing();
		int dotSize = gamePanel.getDotSize();

		int row;
		int col;
		boolean isHorizontal;

		int x = e.getX();
		int y = e.getY();
		
		// Tìm đường kẻ gần nhất với vị trí click chuột
		int lineX = findNearestLine(x);
		int lineY = findNearestLine(y);

		int distanceX = Math.abs(x - (spacing + lineX * (dotSize + spacing)) - dotSize / 2);
		int distanceY = Math.abs(y - (spacing + lineY * (dotSize + spacing)) - dotSize / 2);

		if (lineX != -1 || lineY != -1) {
			boolean isHorizonal = distanceY < distanceX;
			if (isHorizonal) {
				int cIndex = Math.min(x / (dotSize + spacing) - 1, gameManager.horizontalLines[0].length - 1);
				cIndex = cIndex >= 0 ? cIndex : 0;

				row = lineY;
				col = cIndex;
				isHorizontal = true;
			} else {
				int rIndex = Math.min(y / (dotSize + spacing) - 1, gameManager.verticalLines.length - 1);
				rIndex = rIndex >= 0 ? rIndex : 0;
				row = rIndex;
				col = lineX;
				isHorizontal = false;
			}
			gameManager.updateGameState(new Move(row, col, isHorizontal));
		}

	}

	private int findNearestLine(int coordinate) {
		GameManager gameManager = GameManager.getInstance();
		int spacing = gamePanel.getSpacing();
		int dotSize = gamePanel.getDotSize();
		int nearestLine = -1;
		double minDistance = Double.MAX_VALUE;

		for (int i = 0; i <= gameManager.boardSize; i++) {
			int lineCoordinate = spacing + i * (dotSize + spacing);
			double distance = Math.abs(coordinate - lineCoordinate);
			if (distance < minDistance) {
				minDistance = distance;
				nearestLine = i;
			}
		}
		// Kiểm tra xem khoảng cách có đủ gần để vẽ đường kẻ không
		if (minDistance > dotSize) {
			return -1;
		}
		return nearestLine;
	}

}
