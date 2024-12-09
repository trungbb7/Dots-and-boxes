package model;

import java.util.ArrayList;
import java.util.List;

public class AIPlayer {



	private int level;

	public AIPlayer(int level) {
		this.level = level;
	}

	public Move makeMove(int[][] horizontalLines, int[][] verticalLines, int[][] boxes) {

		MoveWithScore mwc = minimax(horizontalLines, verticalLines, true, this.level, null,
				Integer.MIN_VALUE, Integer.MAX_VALUE);
		// System.out.println("row: " + mwc.row + " - col: " + mwc.col + " - isHorizontal: " + mwc.isHorizontal
		// 		+ " - score: " + mwc.score);
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
			Move currentMove, int alpha, int beta) {
		List<Move> availableMoves = availableMoves(horizontalLines, verticalLines);
		if (depth <= 0 || availableMoves.isEmpty()) {
			int score = evaluate(horizontalLines, verticalLines, currentMove, isMax);
			return new MoveWithScore(0, 0, false, score);
		}

		// Sắp xếp nước đi để tối ưu alpha-beta pruning
		sortMoves(availableMoves, horizontalLines, verticalLines);
		
		Move rsMove = new Move(0, 0, false);
		int rsScore;
		
		if (isMax) {
			rsScore = Integer.MIN_VALUE;
			for (Move move : availableMoves) {   
				move(move, horizontalLines, verticalLines);
				int score = 0;
				int count = countNewSquare(horizontalLines, verticalLines, move);
				if(count > 0) {
					score += (depth + 1) * count * 100000000;
					// Nếu có thể tạo ô, tiếp tục lượt của mình
					score += minimax(horizontalLines, verticalLines, true, depth - 1, move, alpha, beta).score;
				} else {
					score += minimax(horizontalLines, verticalLines, false, depth - 1, move, alpha, beta).score;
				}
				
				if (score > rsScore) {
					rsScore = score;
					rsMove = move;
				}
				reMove(move, horizontalLines, verticalLines);
				
				alpha = Math.max(alpha, rsScore);
				if (beta <= alpha) {
					break; 
				}
			}
		} else {
			// Ngược lại cho min
			rsScore = Integer.MAX_VALUE;
			for (Move move : availableMoves) {
				move(move, horizontalLines, verticalLines);
				int score = 0;
				int count = countNewSquare(horizontalLines, verticalLines, move);
				if(count > 0) {
					score -= (depth + 1) * count * 100000000;
					// Nếu có thể tạo ô, tiếp tục lượt của đối thủ
					score += minimax(horizontalLines, verticalLines, false, depth - 1, move, alpha, beta).score;
				} else {
					score += minimax(horizontalLines, verticalLines, true, depth - 1, move, alpha, beta).score;
				}
				
				if (score < rsScore) {
					rsScore = score;
					rsMove = move;
				}
				reMove(move, horizontalLines, verticalLines);
				
				beta = Math.min(beta, rsScore);
				if (beta <= alpha) {
					break; 
				}
			}
		}

		return new MoveWithScore(rsMove.row, rsMove.col, rsMove.isHorizontal, rsScore);
	}

	// Sắp xếp nước đi
	private void sortMoves(List<Move> moves, int[][] horizontalLines, int[][] verticalLines) {
		moves.sort((m1, m2) -> {
			int score1 = getQuickMoveScore(m1, horizontalLines, verticalLines);
			int score2 = getQuickMoveScore(m2, horizontalLines, verticalLines);
			return Integer.compare(score2, score1); 
		});
	}

	// Đánh giá nhanh một nước đi
	private int getQuickMoveScore(Move move, int[][] horizontalLines, int[][] verticalLines) {
		int score = 0;
		
		// Ưu tiên cao nhất cho nước đi tạo được ô
		move(move, horizontalLines, verticalLines);
		int squares = countNewSquare(horizontalLines, verticalLines, move);
		reMove(move, horizontalLines, verticalLines);
		if (squares > 0) {
			return 1000 * squares;
		}
		
		// Ưu tiên nước đi ở góc và cạnh
		if (move.isHorizontal) {
			if (move.row == 0 || move.row == horizontalLines.length - 1) score += 10;
			if (move.col == 0 || move.col == horizontalLines[0].length - 1) score += 10;
		} else {
			if (move.col == 0 || move.col == verticalLines[0].length - 1) score += 10;
			if (move.row == 0 || move.row == verticalLines.length - 1) score += 10;
		}
		
		return score;
	}

	private int countNewSquare(int[][] horizontalLines, int[][] verticalLines, Move move) {
		int result = 0;
		if (move != null) {
			boolean isHorizontal = move.isHorizontal();
			int row = move.getRow();
			int col = move.getCol();
			if (isHorizontal) {

				if (row == 0) {
					if (horizontalLines[row + 1][col] != 0 && (verticalLines[row][col] != 0)
							&& (verticalLines[row][col + 1] != 0)) {
						result++;
					}
				} else if (row == horizontalLines.length - 1) {
					if (horizontalLines[row - 1][col] != 0 && (verticalLines[row - 1][col] != 0)
							&& (verticalLines[row - 1][col + 1] != 0)) {
						result++;
					}
				} else {
					if ((horizontalLines[row - 1][col] != 0) && (verticalLines[row - 1][col] != 0)
							&& (verticalLines[row - 1][col + 1] != 0)) {
						result++;
					}
					if ((horizontalLines[row + 1][col] != 0) && (verticalLines[row][col] != 0)
							&& (verticalLines[row][col + 1] != 0)) {
						result++;
					}
				}

			} else {

				if (col == 0) {
					if ((verticalLines[row][col + 1] != 0) && (horizontalLines[row][col] != 0)
							&& (horizontalLines[row + 1][col] != 0)) {
						result++;
					}
				} else if (col == verticalLines[0].length - 1) {
					if ((verticalLines[row][col - 1] != 0) && (horizontalLines[row][col - 1] != 0)
							&& (horizontalLines[row + 1][col - 1] != 0)) {
						result++;
						;
					}
				} else {
					if ((verticalLines[row][col + 1] != 0) && (horizontalLines[row][col] != 0)
							&& (horizontalLines[row + 1][col] != 0)) {
						result++;
					}
					if ((verticalLines[row][col - 1] != 0) && (horizontalLines[row][col - 1] != 0)
							&& (horizontalLines[row + 1][col - 1] != 0)) {
						result++;
					}
				}
			}

		}
		return result;
	}

	private int[] analyzeSquare(int[][] horizontalLines, int[][] verticalLines) {
		int[] result = new int[5];
		for (int i = 0; i < verticalLines.length; i++) {
			for (int j = 0; j < verticalLines.length; j++) {
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
		// System.out.println("isMax: " + isMax);
		int result = 0;
		if (isMax) {

			result -= countNewSquare(horizontalLines, verticalLines, currentMove) * 10000000;

			int score = 1;
			int sign = -1;
			int[] analysSquare = analyzeSquare(horizontalLines, verticalLines);
			for (int i = 0; i < 4; i++) {
				result += sign * analysSquare[i] * score;
				score *= 10;
				sign *= -1;
			}

		} else {
			result += countNewSquare(horizontalLines, verticalLines, currentMove) * 10000000;
			int score = 1;
			int sign = 1;
			int[] analysSquare = analyzeSquare(horizontalLines, verticalLines);
			for (int i = 0; i < 4; i++) {
				result += sign * analysSquare[i] * score;
				score *= 10;
				sign *= -1;
			}

		}

		return result;
	}

}