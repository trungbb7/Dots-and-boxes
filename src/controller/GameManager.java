package controller;

import java.awt.CardLayout;
import java.awt.Container;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.AIPlayer;
import model.Move;
import view.GamePanel;
import view.Home;
import view.Match;
import view.Option;

public class GameManager {
	private static GameManager uniqueInstance;

	public static final int PLAYER = 1;
	public static final int BOT = -1;
	public static final int PLAYER_SIGN = 1;
	public static final int BOT_SIGN = -1;

	public int[][] horizontalLines;
	public int[][] verticalLines;
	public int[][] boxes;
	public int boardSize = 5;
	public boolean isFinish;

	private GamePanel gamePanel;
	private AIPlayer aiPlayer;
	private int currentPlayer;
	private int playerScore;
	private int aiScore;
	public int aiLevel;

	public JFrame frame;

	public CardLayout card;
	private Container container;
	private JPanel home;
	private Match match;
	private JPanel option;

	private Move lastBotMove;
	public boolean isHighlightingLastMove = false;

	private GameManager() {
		loadConfig();
		init();


	}

	public static GameManager getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new GameManager();
		}

		return uniqueInstance;
	}

	private void init() {
		frame = new JFrame("Dots and Boxes");

		container = frame.getContentPane();
		card = new CardLayout();
		container.setLayout(card);
		match = new Match();
		home = new Home(new HomeController());
		option = new Option();

		container.add("home", home);
		container.add("option", option);
		container.add(match);

		frame.setSize(820, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void loadConfig() {
		File file = new File("src\\config");
		Scanner sc;
		try {
			sc = new Scanner(file);
			int count = 1;
			while (sc.hasNextLine()) {
				if (count == 1) {
					String level = sc.nextLine().split(":")[1];
					
					this.aiLevel = Integer.parseInt(level);
//					if (level.equals("easy"))
//						this.aiLevel = AIPlayer.EASY_LEVEL;
//					else if (level.equals("medium"))
//						this.aiLevel = AIPlayer.MEDIUM_LEVEL;
////						this.aiLevel = 1;
//					else
//						this.aiLevel = AIPlayer.HARD_LEVEL;
				} else if (count == 2) {
					this.boardSize = Integer.parseInt(sc.nextLine().split(":")[1]);
				}
				count++;
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void newGame() {
		this.card.removeLayoutComponent(this.match);
		this.match = new Match();
		this.gamePanel = match.gamePanel;
		this.aiPlayer = new AIPlayer(this.aiLevel);
		this.currentPlayer = PLAYER; // Người chơi đi trước
		this.playerScore = 0;
		this.aiScore = 0;
		this.horizontalLines = new int[boardSize + 1][boardSize];
		this.verticalLines = new int[boardSize][boardSize + 1];
		this.boxes = new int[boardSize][boardSize];
		this.isFinish = false;

		this.container.add("match", this.match);
		this.card.show(container, "match");
	}

	public void backHome() {
		this.card.show(container, "home");
	}

	public void goOption() {
		this.card.show(container, "option");
	}

	public Move getLastBotMove() {
		return lastBotMove;
	}

	public void updateGameState(Move playerMove) {
		// Cập nhật trạng thái trò chơi (đường kẻ, ô vuông, điểm số)
		if (!isExist(playerMove)) {
			if (currentPlayer == PLAYER) {
				if (playerMove.isHorizontal()) {
					horizontalLines[playerMove.getRow()][playerMove.getCol()] = PLAYER_SIGN;
				} else {
					verticalLines[playerMove.getRow()][playerMove.getCol()] = PLAYER_SIGN;
				}
				gamePanel.repaint();
				if (!checkAndMarkBox(playerMove, PLAYER_SIGN)) {
					currentPlayer = BOT;
					checkFinished();

						Move botMove;
						do {
							if(isFinish) {
								return;
							}
							botMove = aiPlayer.makeMove(horizontalLines, verticalLines, boxes);
							isHighlightingLastMove = true;
							lastBotMove = botMove;
							if (botMove.isHorizontal()) {
								horizontalLines[botMove.getRow()][botMove.getCol()] = BOT_SIGN;
							} else {
								verticalLines[botMove.getRow()][botMove.getCol()] = BOT_SIGN;
							}
							// gamePanel.repaint();
							try {
								gamePanel.paintImmediately(gamePanel.getBounds());
								// timer.restart();
								Thread.sleep(500); // Độ trễ 300ms giữa các nước đi
								isHighlightingLastMove = false;
								gamePanel.repaint();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							checkFinished();
						} while (checkAndMarkBox(botMove, BOT_SIGN));
						// Chuyển lượt chơi
						currentPlayer = PLAYER;
					}

				}
			checkFinished();

			}

	}

	private boolean checkAndMarkBox(Move move, int sign) {
		boolean result = false;
		boolean isHorizontal = move.isHorizontal();
		int row = move.getRow();
		int col = move.getCol();
		// Kiểm tra xem đường kẻ vừa vẽ có tạo thành ô vuông nào không
		if (isHorizontal) {

			if (row > 0 && (horizontalLines[row - 1][col] != 0) && (verticalLines[row - 1][col] != 0)
					&& (verticalLines[row - 1][col + 1] != 0)) {
				if (sign == PLAYER_SIGN)
					playerScore++;
				else if (sign == BOT_SIGN)
					aiScore++;
				result = true;
				boxes[row - 1][col] = sign;
			}

			if ((row < boardSize) && (horizontalLines[row + 1][col] != 0) && (verticalLines[row][col] != 0)
					&& (verticalLines[row][col + 1] != 0)) {
				if (sign == PLAYER_SIGN)
					playerScore++;
				else if (sign == BOT_SIGN)
					aiScore++;
				result = true;
				boxes[row][col] = sign;
			}
		} else {

			if ((col > 0) && (verticalLines[row][col - 1] != 0) && (horizontalLines[row][col - 1] != 0)
					&& (horizontalLines[row + 1][col - 1] != 0)) {
				if (sign == PLAYER_SIGN)
					playerScore++;
				else if (sign == BOT_SIGN)
					aiScore++;
				result = true;
				boxes[row][col - 1] = sign;
			}

			if ((col < boardSize) && (verticalLines[row][col + 1] != 0) && (horizontalLines[row][col] != 0)
					&& (horizontalLines[row + 1][col] != 0)) {
				if (sign == PLAYER_SIGN)
					playerScore++;
				else if (sign == BOT_SIGN)
					aiScore++;
				result = true;
				boxes[row][col] = sign;
			}
		}
		return result;
	}

	private boolean isGameOver() {
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes.length; j++) {
				if (boxes[i][j] == 0)
					return false;
			}
		}
		this.isFinish = true;
		return true;
	}

	private void checkFinished() {
		if (isGameOver()) {
			String[] options = { "Close", "Home", "Replay" };
			int selection = 0;
			if (playerScore > aiScore) {
				selection = JOptionPane.showOptionDialog(null, "You won\nYou: " + playerScore + " - BOT: " + aiScore,
						"Congratulations!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
						options[0]);

			} else if (playerScore < aiScore) {
				selection = JOptionPane.showOptionDialog(null, "You lost\nYou: " + playerScore + " - BOT: " + aiScore,
						"Opp", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			} else {
				selection = JOptionPane.showOptionDialog(null, "Draw\nYou: " + playerScore + " - BOT: " + aiScore,
						"...", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				System.out.println("selection: " + selection);
			}
			if (selection == 1) {
				backHome();
			} else if (selection == 2) {
				newGame();
			} else if (selection == 0) {
				match.addTop();
			}
		}
	}

	private boolean isExist(Move move) {
		if (move.isHorizontal()) {
			if (horizontalLines[move.getRow()][move.getCol()] != 0)
				return true;
		} else {
			if (verticalLines[move.getRow()][move.getCol()] != 0)
				return true;
		}

		return false;
	}

}
