import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.google.common.primitives.Ints;

import processing.core.PVector;

public class Game2048 implements Iterable<int[]>{
	private int[][] grid;
	private int score;
	
	private static final float CHANCE_OF_4 = 0.1f;
	
	public Game2048() {
		this(4,4);
	}
	
	public Game2048(int width, int height) {
		grid = new int[width][height];
		score = 0;
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
	
	private int getVal(PVector pos) {
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
			int[] rowArray = toRotate[row];
			List<Integer> listRow = Arrays.stream(rowArray).boxed().collect(Collectors.toList());
			Collections.reverse(listRow);
			for (int col = 0; col < rowArray.length; col++) {
				res[col][row] = listRow.get(col);
			}
		}
		return res;
	}
	
	public boolean move(int direction) {
		boolean success = false;
		for (int i = 0; i < direction; i++) {
			grid = rotate(grid);
		}
		for (int row = 0; row < grid.length; row++) {
			for (int col =0 ; col < grid[0].length; col++) {
				if (grid[row][col] == 0 ) {
					for(int i = col + 1; i < grid[0].length; i++) {
						if (grid[row][i] != 0) {
							grid[row][col] = grid[row][i];
							grid[row][i] = 0;
							success = true;
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
							}
							break;
						}
					}
				}
			}
		}
		if (success) {
			update();
		}
		
		for (int i = 0; i < 4-direction; i++) {
			grid = rotate(grid);
		}
		
		return success;
	}
	
	private void update() {
		int row, col;
		do {
			row = ThreadLocalRandom.current().nextInt(0, grid.length);
			col = ThreadLocalRandom.current().nextInt(0, grid[0].length);
		} while (grid[row][col] != 0);
		grid[row][col] = (Math.random() > CHANCE_OF_4) ? 2 : 4;
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
			//rowString.deleteCharAt(rowString.length() - 1);
			rowString.append('\n');
			s.append(rowString);
		}
		return s.toString();
	}
	
	public static void main(String[] args) {
		Game2048 g = new Game2048();
		System.out.println(g);
		g.move(0);
		System.out.println(g);
		int a = 10;
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
}
