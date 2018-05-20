package game;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public class Main2048 extends PApplet {
	
	private Game2048 game;
	private GameRenderer renderer;
	
	private int gameWidth, gameHeight;
	
	private static int[] BACKGROUND_COLOR = {250, 248, 239};
	
	@Override
	public void settings() {
		gameWidth = gameHeight = 630;
		size(gameWidth + GameRenderer.PADDING, 850);
		

	}
	
	@Override
	public void setup() {
		game = new Game2048(new Random());
		renderer = new GameRenderer(this);
		
	}
	
	@Override
	public void draw() {
		background(BACKGROUND_COLOR[0], BACKGROUND_COLOR[1], BACKGROUND_COLOR[2]);
		renderer.draw(game, gameHeight, new PVector((width- gameWidth)/2, height - gameHeight - GameRenderer.PADDING));
		
		textAlign(LEFT, TOP);
		fill(119, 110, 101);
		textFont(getFont(100));
		text("2048", 10, getTextYPos() - 30);
		
		PVector[] rectSize = getScoreRect();
		
		fill(GameRenderer.BACKGROUND_COLOR[0], GameRenderer.BACKGROUND_COLOR[1], GameRenderer.BACKGROUND_COLOR[2]);
		rect(rectSize[0].x, rectSize[0].y, rectSize[1].x, rectSize[1].y, 10);
		
		textFont(getFont(25));
		fill(255);
		textAlign(CENTER, TOP);
		text("SCORE", rectSize[0].x + rectSize[1].x / 2, rectSize[0].y);
		text(game.getScore(), rectSize[0].x + rectSize[1].x / 2, rectSize[0].y + rectSize[1].y / 2);
		
	}
	
	private PVector[] getScoreRect() {
		int rectWidth = 200;
		int padding = 10;
		return new PVector[] {new PVector(width - rectWidth - padding, getTextYPos() + padding),
							  new PVector(rectWidth, (textDescent() + textAscent())/2, 10)};
	}
	
	private float getTextYPos() {
		float textHeight = textDescent() + textAscent();
		return (height - gameHeight - textHeight - GameRenderer.PADDING * 2) /2.0f;
	}
	
	private PFont getFont(int size) {
		return createFont("Helvetica Neue Bold", size);
	}
	
	@Override
	public void keyPressed() {
		if (key == CODED) {
			switch (keyCode) {
			case LEFT:
				game.move(0);
				break;
			case UP:
				game.move(1);
				break;
			case RIGHT:
				game.move(2);
				break;
			case DOWN:
				game.move(3);
				break;
			}
		}
	}

	public static void main(String[] args) {
		PApplet.main("game.Main2048");
	}

}
