package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import controller.GameManager;

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

		MoveWithScore mwc = minimax(horizontalLines, verticalLines, true, this.level, null);
		return new Move(mwc.row, mwc.col, mwc.isHorizontal);
	}

	private List<Move> availableMoves(int[][] horizontalLines, int[][] verticalLines) {
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

	private MoveWithScore minimax(int[][] horizontalLines, int[][] verticalLines, boolean isMax, int depth,
			Move currentMove) {
		List<Move> availableMoves = availableMoves(horizontalLines, verticalLines);
		if (depth <= 0 || availableMoves.isEmpty()) {
			int score = evaluate(horizontalLines, verticalLines, currentMove, isMax);
			return new MoveWithScore(0, 0, false, score);
		}
		Move rsMove = new Move(0, 0, false);
		int rsScore;
		if (isMax) {
			rsScore = Integer.MIN_VALUE;
			for (Move move : availableMoves) {
				move(move, horizontalLines, verticalLines);
				int score = minimax(horizontalLines, verticalLines, !isMax, depth - 1, move).score;
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
				int score = minimax(horizontalLines, verticalLines, !isMax, depth - 1, move).score;
				if (score < rsScore) {
					rsScore = score;
					rsMove = move;
				}
				reMove(move, horizontalLines, verticalLines);
			}
		}
		return new MoveWithScore(rsMove.row, rsMove.col, rsMove.isHorizontal, rsScore);
	}

	private boolean hasNewSquare(int[][] horizontalLines, int[][] verticalLines, Move move) {
		if(move != null) {
			boolean isHorizontal = move.isHorizontal();
			int row = move.getRow();
			int col = move.getCol();
			if (isHorizontal) {

				if (row > 0 && (horizontalLines[row - 1][col] != 0) && (verticalLines[row - 1][col] != 0)
						&& (verticalLines[row - 1][col + 1] != 0)) {
					return true;
				}

				if ((row < verticalLines.length) && (horizontalLines[row + 1][col] != 0) && (verticalLines[row][col] != 0)
						&& (verticalLines[row][col + 1] != 0)) {
					return true;
				}
			} else {

				if ((col > 0) && (verticalLines[row][col - 1] != 0) && (horizontalLines[row][col - 1] != 0)
						&& (horizontalLines[row + 1][col - 1] != 0)) {
					return true;
				}

				if ((col < verticalLines.length) && (verticalLines[row][col + 1] != 0) && (horizontalLines[row][col] != 0)
						&& (horizontalLines[row + 1][col] != 0)) {
					return true;
				}
			}

		}
		return false;
	}

	private int[] analysisSquare(int[][] horizontalLines, int[][] verticalLines) {
		int[] result = new int[5];
		for (int i = 0; i < verticalLines.length; i++) {
			for (int j = 0; j < verticalLines.length - 1; j++) {
				int score = 0;
				if (horizontalLines[i][j] != 0)
					score++;
				if (horizontalLines[i + 1][j] != 0)
					score++;
				if (verticalLines[i][j] != 0)
					score++;
				if (verticalLines[i][j + 1] != 0)
					score++;
				result[score]++;

			}
		}

		return result;
	}

	private int evaluate(int[][] horizontalLines, int[][] verticalLines, Move currentMove, boolean isMax) {
		int result = 0;
		if (isMax) {
			if (hasNewSquare(horizontalLines, verticalLines, currentMove)) {
				return -10000000;
			} else {
				int score = 1;
				int sign = -1;
				int[] analysSquare = analysisSquare(horizontalLines, verticalLines);
				for (int i = 0; i < 4; i++) {
					result += sign * analysSquare[i] * score;
					score *= 10;
					sign *= -1;
				}
			}

		} else {
			if (hasNewSquare(horizontalLines, verticalLines, currentMove)) {
				return 10000000;
			} else {
				int score = 1;
				int sign = 1;
				int[] analysSquare = analysisSquare(horizontalLines, verticalLines);
				for (int i = 0; i < 4; i++) {
					result += sign * analysSquare[i] * score;
					score *= 10;
					sign *= -1;
				}
			}
		}

		return result;
	}

}