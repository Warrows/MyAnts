package fr.warrows.java.aichallenge;
import java.io.IOException;

/**
 * Starter bot implementation.
 */
public class MyBot extends Bot {
    /**
     * Main method executed by the game engine for starting the bot.
     * 
     * @param args command line arguments
     * 
     * @throws IOException if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        new MyBot().readSystemInput();
    }
    
    /**
     * For every ant check every direction in fixed order (N, E, S, W) and move it if the tile is
     * passable.
     */
    @Override
    public void doTurn() {
        GameDataHandler gameDataHandler = getAnts();
        for (Tile myAnt : gameDataHandler.getMyAnts()) {
            for (Aim direction : Aim.values()) {
                if (myAnt.get(direction).isPassable()) {
                    gameDataHandler.issueOrder(myAnt, direction);
                    break;
                }
            }
        }
    }

	@Override
	public void afterUpdate()
	{
	}
}
