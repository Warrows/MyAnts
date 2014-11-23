package myia;

import java.io.IOException;

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

	private static Groupe groupe = null;

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
		if (!(groupe == null))
		{
			groupe.tourPlus();
		}
		Ants ants = getAnts();
		if (groupe == null)
			groupe = new Groupe(ants);
		for (Tile newAnt : ants.getMyHills())
		{
			if (ants.getIlk(newAnt).equals(Ilk.MY_ANT)
					&& groupe.getFourmiByTile(newAnt) == null)
				groupe.add(new Fourmi(newAnt, ants, groupe));
		}
		groupe.preparerTour();
		groupe.bougerFourmis();
	}
}
