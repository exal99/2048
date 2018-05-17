package game;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public class GameRenderer {
	private Main2048 parrent;
	private int[] lastNew;
	private float animationSpeed;
	private float animationLevel;
	
	public static final int[] BACKGROUND_COLOR = {187, 173, 160};
	
	public GameRenderer(Main2048 parrent) {
		this.parrent = parrent;
		lastNew = new int[2];
		animationSpeed = 0.05f;
		animationLevel = 1;
	}
	
	
	public void draw(Game2048 game) {
		float size = Math.min(parrent.width, parrent.height);
		int[] currLastNew = game.getLast();
		if (currLastNew != null) {
			animationLevel = 0.001f;
			lastNew = currLastNew;
		} 
		int padding = 8;
		PVector tileSize = new PVector(size/game.getCols(), size/game.getRows());
		PVector startPos = (parrent.width < parrent.height) ? new PVector(0, (parrent.height - size)/2) : new PVector((parrent.width - size) / 2, 0);
		PVector paddVect = new PVector(padding, padding);
		PVector currPos = startPos.copy();
		int row = 0;
		for (int[] rowArray : game) {
			int col = 0;
			for (int tile : rowArray) {
				if (animationLevel < 1 && lastNew != null && row == lastNew[0] && col == lastNew[1]) {
					PVector mid = PVector.add(currPos, PVector.mult(tileSize, 0.5f));
					PVector targetPos = PVector.add(currPos, paddVect);
					PVector direction = PVector.sub(targetPos, mid);
					PVector realPos = direction.mult(animationLevel).add(mid);
					displayTile(tile, realPos, (tileSize.x - padding * 2), animationLevel);
					animationLevel += animationSpeed;
					
				} else {
					displayTile(tile, PVector.add(currPos, paddVect), tileSize.x - padding * 2, 1);
				}
				currPos.add(tileSize.x, 0);
				col++;
			}
			row++;
			currPos.x = startPos.x;
			currPos.add(0, tileSize.y);
		}
	}
	
	private void displayTile(int val, PVector pos, float size, float multiplier) {
		parrent.fill(getColor(val));
		parrent.noStroke();
		parrent.rect(pos.x, pos.y, size * multiplier, size * multiplier);
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
