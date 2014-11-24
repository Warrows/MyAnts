package fr.warrows.java.aichallenge;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Starter bot implementation.
 */
public class MyBot extends Bot
{
	/**
	 * Main method executed by the game engine for starting the bot.
	 * 
	 * @param args
	 *            command line arguments
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static void main(String[] args) throws IOException
	{
		new MyBot().readSystemInput();
	}

	/**
	 * For every ant check every direction in fixed order (N, E, S, W) and move
	 * it if the tile is passable.
	 */
	@Override
	public void doTurn()
	{
		GameDataHandler gameDataHandler = getAnts();
		Collection<Tile> myAnts = gameDataHandler.getMyAnts();
		Collection<Tile> movedAnts = new HashSet<Tile>();
		for (Tile myAnt : myAnts)
		{
			Tile food;
			try
			{
				food = myAnt.getClosestFood();
				for (Aim direction : gameDataHandler.getDirections(myAnt, food))
					if (myAnt.get(direction).isPassable())
					{
						gameDataHandler.issueOrder(myAnt, gameDataHandler
								.getDirections(myAnt, food).get(0));
						movedAnts.add(myAnt);
					}
			} catch (NoFoodFoundException e)
			{
				break;
			}
		}
		myAnts.removeAll(movedAnts);
		for (Tile myAnt : myAnts)
		{
			for (Aim direction : Aim.values())
			{
				if (myAnt.get(direction).isPassable())
				{
					gameDataHandler.issueOrder(myAnt, direction);
				}
			}
		}
	}

	@Override
	public void afterUpdate()
	{
	}
}
