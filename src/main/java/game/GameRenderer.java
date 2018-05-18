package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import game.Game2048.Move;
import game.Game2048.MoveType;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public class GameRenderer {
	private Main2048 parrent;
	private int[] lastNew;
	private float newTileSpeed;
	private float newTileAnimation;
	private float moveTileSpeed;
	private float moveTileAnimation;
	private Map<Move,Move> lastMoves;
	
	public static final int[] BACKGROUND_COLOR = {187, 173, 160};
	public static final int PADDING = 9;
	
	public GameRenderer(Main2048 parrent) {
		this.parrent = parrent;
		lastNew = new int[2];
		newTileSpeed = 0.05f;
		newTileAnimation = 1;
		moveTileAnimation = 1;
		moveTileSpeed = 0.15f;
		lastMoves = new HashMap<Move,Move>(16);
	}
	
	public void draw(Game2048 game, float size, PVector startPos) {
		int[] currLastNew = game.getLast();
		Queue<Move> currLastMove = game.getMoves();
		
		if (currLastNew != null) {
			newTileAnimation = 0.001f;
			lastNew = currLastNew;
		}
		if (currLastMove.size() > 0) {
			lastMoves.clear();
			for (Move m : currLastMove) {
				lastMoves.put(m, m);
			}
			moveTileAnimation = 0.001f;
		}
		PVector tileSize = new PVector(size/game.getCols(), size/game.getRows());
		PVector paddVect = new PVector(PADDING, PADDING);
		PVector currPos = startPos.copy();
		int row = 0;
		
		parrent.fill(BACKGROUND_COLOR[0], BACKGROUND_COLOR[1], BACKGROUND_COLOR[2]);
		parrent.rect(startPos.x - PADDING, startPos.y - PADDING, size + PADDING * 2, size + PADDING * 2, 10);
		ArrayList<Move> moveingTiles = new ArrayList<Move>();
		for (int[] rowArray : game) {
			int col = 0;
			for (int tile : rowArray) {
				if (newTileAnimation < 1 && lastNew != null && row == lastNew[0] && col == lastNew[1]) {
					PVector mid = PVector.add(currPos, PVector.mult(tileSize, 0.5f));
					PVector targetPos = PVector.add(currPos, paddVect);
					PVector direction = PVector.sub(targetPos, mid);
					PVector realPos = direction.mult(newTileAnimation).add(mid);
					displayTile(tile, realPos, (tileSize.x - PADDING * 2), newTileAnimation);
					newTileAnimation += newTileSpeed;
					
				} else if (moveTileAnimation < 1 && lastMoves.containsKey(new Move(null, new int[] {row, col}, null))) {
					Move lastMove = lastMoves.get(new Move(null, new int[] {row, col}, null));
					moveingTiles.add(lastMove);
					displayTile(0, PVector.add(currPos, paddVect), tileSize.x - PADDING * 2, 1);
				} else {
					displayTile(tile, PVector.add(currPos, paddVect), tileSize.x - PADDING * 2, 1);
				}
				currPos.add(tileSize.x, 0);
				col++;
				
			}
			row++;
			currPos.x = startPos.x;
			currPos.add(0, tileSize.y);
		}
		for (Move move : moveingTiles) {
			int val = game.getVal(new PVector(move.to[0], move.to[1]));
			displayMoveingTile(move, val, startPos, tileSize);
		}
		if (moveTileAnimation < 1) {
			moveTileAnimation += moveTileSpeed;
		}
	}
	
	private void displayMoveingTile(Move move, int tile, PVector startPos, PVector tileSize) {
		PVector paddVect = new PVector(PADDING, PADDING);
		PVector start = getScreenPos(move.from, startPos, tileSize.x);
		PVector target = getScreenPos(move.to, startPos, tileSize.x);
		target.sub(start).mult(moveTileAnimation).add(start).add(paddVect);
		displayTile((move.type != MoveType.MERGE) ? tile : tile/2, target, tileSize.x - PADDING * 2, 1);
	}
	
	private PVector getScreenPos(int[] pos, PVector start, float size) {
		return getScreenPos(pos[0], pos[1], start, size);
	}
	
	private PVector getScreenPos(int row, int col, PVector start, float size) {
		return PVector.add(start, new PVector(col * size, row * size));
	}
	
	private void displayTile(int val, PVector pos, float size, float multiplier) {
		parrent.fill(getColor(val));
		parrent.noStroke();
		parrent.rect(pos.x, pos.y, size * multiplier, size * multiplier, 5);
		if (val != 0) {
			parrent.textAlign(PApplet.CENTER, PApplet.CENTER);
			parrent.fill(getFontColor(val));
			parrent.textSize(getFontSize(val, multiplier) );
			parrent.textFont(getFont(val, multiplier));
			parrent.text(val, pos.x + size/2 * multiplier, pos.y + (size/2 - 12) * multiplier);
			//System.out.println((pos.x + size/2) + " " + (pos.y + size/2 - 12 * multiplier));
		}
	}
	
	private int getFontColor(int val) {
		return (val >= 8) ? parrent.color(249, 246, 242) : parrent.color(119, 110, 101);
	}
	
	private PFont getFont(int val, float mult) {
		return parrent.createFont("Helvetica Neue Bold", getFontSize(val, mult));
	}
	
	private int getFontSize(int val, float multiplier) {
		return (val < 1024) ? (int) Math.ceil(80  * multiplier): (int) Math.ceil(60 * multiplier);
	}
	
	private int getColor(int val) {
		switch(val) {
		case 2:
			return parrent.color(238, 228, 218);
		case 4:
			return parrent.color(237, 224, 200);
		case 8:
			return parrent.color(242, 177, 121);
		case 16:
			return parrent.color(245, 149, 99);
		case 32:
			return parrent.color(246, 124, 95);
		case 64:
			return parrent.color(246, 94, 59);
		case 128:
			return parrent.color(237, 207, 114);
		case 256:
			return parrent.color(237, 204, 97);
		case 512:
			return parrent.color(237, 200, 90);
		case 1024:
			return parrent.color(237, 197, 63);
		case 2048:
			return parrent.color(237, 194, 46);
		default:
			return parrent.color(238, 228, 218, 90);
		}
	}
}
