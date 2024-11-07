package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer {

	public static final int EASY_LEVEL = 1;
	public static final int MEDIUM_LEVEL = 2;
	public static final int HARD_LEVEL = 3;

	// Thuộc tính và phương thức cho AI
	private int level;

	public AIPlayer(int level) {
		this.level = level;
	}

	public Move makeMove(int[][] horizontalLines, int[][] verticalLines, int[][] boxes) {

		MoveWithScore mwc = minimax(horizontalLines, verticalLines, boxes, true, this.level);
		return new Move(mwc.row, mwc.col, mwc.isHorizontal);
	}

	private List<Move> availableMopves(int[][] horizontalLines, int[][] verticalLines) {
		List<Move> result = new ArrayList<Move>();
		for (int i = 0; i < horizontalLines.length; i++) {
			for (int j = 0; j < horizontalLines[0].length; j++) {
				if (horizontalLines[i][j] == 0) {
					result.add(new Move(i, j, true));
				}
			}
		}
		for (int i = 0; i < verticalLines.length; i++) {
			for (int j = 0; j < verticalLines[0].length; j++) {
				if (verticalLines[i][j] == 0) {
					result.add(new Move(i, j, false));
				}
			}
		}
		return result;
	}

	private void move(Move move, int[][] horizontalLines, int[][] verticalLines) {
		if (move.isHorizontal) {
			horizontalLines[move.row][move.col] = 1;
		} else {
			verticalLines[move.row][move.col] = 1;
		}
	}

	private void reMove(Move move, int[][] horizontalLines, int[][] verticalLines) {
		if (move.isHorizontal) {
			horizontalLines[move.row][move.col] = 0;
		} else {
			verticalLines[move.row][move.col] = 0;
		}
	}

	private MoveWithScore minimax(int[][] horizontalLines, int[][] verticalLines, int[][] boxes, boolean isMax,
			int depth) {
		List<Move> availableMoves = availableMopves(horizontalLines, verticalLines);
		if (depth <= 0 || isGameOver(boxes)) {
			int score = evaluate(horizontalLines, verticalLines);
			return new MoveWithScore(-1, -1, false, score);
		}
		Move rsMove = new Move(0, 0, false);
		int rsScore;
		if (isMax) {
			rsScore = Integer.MIN_VALUE;
			for (Move move : availableMoves) {
				move(move, horizontalLines, verticalLines);
				int score = minimax(horizontalLines, verticalLines, boxes, !isMax, depth - 1).score;
				if (score > rsScore) {
					rsScore = score;
					rsMove = move;
				}
				reMove(move, horizontalLines, verticalLines);
			}
		} else {
			rsScore = Integer.MAX_VALUE;
			for (Move move : availableMoves) {
				move(move, horizontalLines, verticalLines);
				int score = minimax(horizontalLines, verticalLines, boxes, !isMax, depth - 1).score;
				if (score < rsScore) {
					rsScore = score;
					rsMove = move;
				}
				reMove(move, horizontalLines, verticalLines);
			}
		}
		return new MoveWithScore(rsMove.row, rsMove.col, rsMove.isHorizontal, rsScore);
	}

	private boolean isGameOver(int[][] boxes) {
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[0].length; j++) {
				if (boxes[i][j] == 0)
					return false;
			}
		}
		return true;
	}

	private int evaluate(int[][] horizontalLines, int[][] verticalLines) {
		Random rd = new Random();
		return rd.nextInt(100);
	}

}