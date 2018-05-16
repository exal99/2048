import processing.core.PVector;

public class GameRenderer {
	private Main2048 parrent;
	
	public static final int[] BACKGROUND_COLOR = {187, 173, 160};
	
	public GameRenderer(Main2048 parrent) {
		this.parrent = parrent;
	}
	
	
	public void draw(Game2048 game) {
		float size = Math.min(parrent.width, parrent.height);
		int padding =7;
		PVector startPos = (parrent.width < parrent.height) ? new PVector(0, (parrent.height - size)/2) : new PVector((parrent.width - size) / 2, 0);
		PVector tileSize = new PVector(size/game.getCols(), size/game.getRows());
		PVector paddVect = new PVector(padding, padding);
		PVector currPos = startPos.copy();
		for (int[] row : game) {
			for (int tile : row) {
				displayTile(tile, PVector.add(currPos, paddVect), tileSize.x - padding * 2);
				currPos.add(tileSize.x, 0);
			}
			currPos.x = startPos.x;
			currPos.add(0, tileSize.y);
		}
	}
	
	private void displayTile(int val, PVector pos, float size) {
		parrent.fill(getColor(val));
		parrent.noStroke();
		parrent.rect(pos.x, pos.y, size, size);
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
