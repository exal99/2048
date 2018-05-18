package game;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;


import processing.core.PVector;

public class Game2048 implements Iterable<int[]>{
	private int[][] grid;
	private int score;
	private Random gen;
	
	private int[] lastNew;
	private Queue<Move> lastMoves;
	
	private static final float CHANCE_OF_4 = 0.1f;
	
	public Game2048(Random rand) {
		this(4,4, rand);
	}
	
	public Game2048(int width, int height, Random rand) {
		grid = new int[width][height];
		score = 0;
		gen = rand;
		lastNew = null;
		lastMoves = new ArrayDeque<Move>();
		update();
	}
	
	public int getScore() {
		return score;
	}
	
	private boolean isValidPos(int row, int col) {
		return 0 <= row && row < grid.length && 0 <= col && col < grid[0].length;
	}
	
	private boolean isValidPos(PVector p) {
		return isValidPos((int) p.x, (int) p.y);
	}
	
	public int getVal(PVector pos) {
		return grid[(int) pos.x][(int) pos.y];
	}
	
	public boolean canMove() {
		PVector[] vects = new PVector[] {new PVector(-1,0), new PVector(1,0), new PVector(0,-1), new PVector(0,1)};
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[0].length; col++) {
				PVector pos = new PVector(row, col);
				for (PVector vect : vects) {
					PVector newPos = pos.add(vect);
					if (isValidPos(pos.add(vect))) {
						if (getVal(newPos) == 0 || getVal(newPos) == getVal(pos)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private int[][] rotate(int[][] toRotate) {
		int[][] res = new int[toRotate[0].length][toRotate.length];
		for (int row = 0; row < toRotate.length; row++) {
			for (int col = 0; col < toRotate[0].length; col++) {
				res[col][row] = toRotate[row][toRotate[0].length - col - 1];
			}
		}
		return res;
	}
	
	private int[] rotatePos(int row, int col, int numTimes) {
		if (numTimes == 0) {
			return new int[] {row, col};
		} else {
			return rotatePos(grid[0].length - col - 1, row, numTimes - 1);
		}
		
	}
	
	public boolean move(int direction) {
		boolean success = false;
		for (int i = 0; i < direction; i++) {
			grid = rotate(grid);
		}
		for (int row = 0; row < grid.length; row++) {
			for (int col =0 ; col < grid[0].length; col++) {
				ArrayList<Move> moves = new ArrayList<Move>();
				if (grid[row][col] == 0 ) {
					for(int i = col + 1; i < grid[0].length; i++) {
						if (grid[row][i] != 0) {
							grid[row][col] = grid[row][i];
							grid[row][i] = 0;
							success = true;
							moves.add(new Move(rotatePos(row, i, 4-direction), rotatePos(row, col, 4 - direction), MoveType.MOVE));
							break;
						}
					}
					for(int i = col + 1; i < grid[0].length; i++) {
						if (grid[row][i] != 0) {
							if (grid[row][i] == grid[row][col]) {
								grid[row][col] += grid[row][i];
								grid[row][i] = 0;
								score += grid[row][col];
								success = true;
								Move last = moves.get(0);
								last.type = MoveType.MERGE;
								moves.add(new Move(rotatePos(row, i, 4-direction), rotatePos(row, col, 4 - direction), MoveType.MERGE));
							}
							break;
						}
					}
				} else {
					for(int i = col + 1; i < grid[0].length; i++) {
						if (grid[row][i] != 0) {
							if (grid[row][i] == grid[row][col]) {
								grid[row][col] += grid[row][i];
								grid[row][i] = 0;
								score += grid[row][col];
								success = true;
								moves.add(new Move(rotatePos(row, i, 4-direction), rotatePos(row, col, 4 - direction), MoveType.MERGE));
							}
							break;
						}
					}
				}
				lastMoves.addAll(moves);
			}
		}
		
		for (int i = 0; i < 4-direction; i++) {
			grid = rotate(grid);
		}
		
		
		if (success) {
			update();
		}
		
		
		
		return success;
	}
	
	private int randInt(int low, int hight) {
		return gen.nextInt(hight - low) + low;
	}
	
	private void update() {
		int row, col;
		do {
			row = randInt(0, grid.length);
			col = randInt(0, grid[0].length);
		} while (grid[row][col] != 0);
		grid[row][col] = (Math.random() > CHANCE_OF_4) ? 2 : 4;
		lastNew = new int[]{row, col};
	}
	
	public int[] getLast() {
		int[] res = lastNew;
		lastNew = null;
		return res;
	}
	
	public Queue<Move> getMoves() {
		Queue<Move> res = new ArrayDeque<Move>();
		while (lastMoves.size() > 0) {
			res.add(lastMoves.poll());
		}
		return res;
	}
	
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		int padding = 3;
		for (int[] row : grid) {
			StringBuilder rowString = new StringBuilder();
			for (int i : row) {
				rowString.append(String.format("%1$" + padding + "s", i) + "|");
			}
			rowString.append('\n');
			for(int i = 0; i < (padding + 1) * grid[0].length; i++) {
				rowString.append('-');
			}
			rowString.append('\n');
			s.append(rowString);
		}
		return s.toString();
	}
	
	public int getRows() {
		return grid.length;
	}
	
	public int getCols() {
		return grid[0].length;
	}

	@Override
	public Iterator<int[]> iterator() {
		return new GameIterator();
	}
	
	private class GameIterator implements Iterator<int[]> {
		private int row;
		public GameIterator() {
			row = 0;
		}
		
		public boolean hasNext() {
			return row < grid.length;
		}
		
		public int[] next() {
			return grid[row++];
		}
	}
	
	public static class Move {
		public int[] from;
		public int[] to;
		public MoveType type;
		
		public Move(int[] fr, int[] to, MoveType ty) {
			from = fr;
			this.to = to;
			type = ty;
		}
		
		public boolean equals(Object obj) {
			if (obj instanceof Move) {
				Move real = (Move) obj;
				return real.to[0] == to[0] && real.to[1] == to[1];
			} else {
				return false;
			}
		}
		
		public int hashCode() {
			return to[0] << 15 | to[1];
		}
		
	}
	
	public enum MoveType {
		MERGE, MOVE;
	}
}
