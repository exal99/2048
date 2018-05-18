package game;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PVector;

public class Main2048 extends PApplet {
	
	private Game2048 game;
	private GameRenderer renderer;
	
	private int gameWidth, gameHeight;
	
	private static int[] BACKGROUND_COLOR = {250, 248, 239};
	
	@Override
	public void settings() {
		gameWidth = gameHeight = 630;
		size(gameWidth + GameRenderer.PADDING, 900);

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
