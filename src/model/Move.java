package model;

public class Move {
	int row;
	int col;
	boolean isHorizontal;

	public Move(int row, int col, boolean isHorizontal) {
		this.row = row;
		this.col = col;
		this.isHorizontal = isHorizontal;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean isHorizontal() {
		return isHorizontal;
	}

}
