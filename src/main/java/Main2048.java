import processing.core.PApplet;

public class Main2048 extends PApplet {
	
	private Game2048 game;
	private GameRenderer renderer;
	
	@Override
	public void settings() {
		size(800,600);

	}
	
	@Override
	public void setup() {
		game = new Game2048();
		System.out.println(game);
		renderer = new GameRenderer(this);
		surface.setResizable(true);
	}
	
	@Override
	public void draw() {
		background(GameRenderer.BACKGROUND_COLOR[0], GameRenderer.BACKGROUND_COLOR[1], GameRenderer.BACKGROUND_COLOR[2]);
		renderer.draw(game);
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
			System.out.println(game);
		}
	}

	public static void main(String[] args) {
		PApplet.main("Main2048");
	}

}
