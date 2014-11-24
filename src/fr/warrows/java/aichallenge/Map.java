package fr.warrows.java.aichallenge;

import java.util.Collection;
import java.util.HashSet;

public class Map
{
	private Tile[][] tiles;
	private final int nbCols, nbRows;

	Map(int nbRows, int nbCols)
	{
		this.nbCols = nbCols;
		this.nbRows = nbRows;
		tiles = new Tile[nbRows][nbCols];
		for (int row = 0; row < nbRows; row++)
			for (int col = 0; col < nbCols; col++)
			{
				tiles[row][col] = new Tile(row, col);
			}
		for (int row = 0; row < nbRows; row++)
			for (int col = 0; col < nbCols; col++)
			{
				tiles[row][col].setNorth(tiles[(nbRows + row - 1) % nbRows][col]);
				tiles[row][col].setSouth(tiles[(row + 1) % nbRows][col]);
				tiles[row][col].setWest(tiles[row][(nbCols + col - 1) % nbCols]);
				tiles[row][col].setEast(tiles[row][(col + 1) % nbCols]);
			}
	}

	public int getNbRows()
	{
		return nbRows;
	}

	public int getNbCols()
	{
		return nbCols;
	}

	public void clear()
	{
		clearFood();
		for (int row = 0; row < nbRows; row++)
			for (int col = 0; col < nbCols; col++)
			{
				tiles[row][col].unreserve();
				switch (tiles[row][col].getType())
				{
				case DEAD:
				case ENEMY_ANT:
				case MY_ANT:
					tiles[row][col].setType(TileType.LAND);
					break;
				case FOOD:
				case LAND:
				case WATER:
				case ENEMY_HILL:
				case MY_HILL:
					break;
				}
			}
	}

	private void clearFood()
	{
		for (int row = 0; row < nbRows; row++)
			for (int col = 0; col < nbCols; col++)
				if (tiles[row][col].getType() == TileType.FOOD)
					for (Aim direction : Aim.values())
						if (tiles[row][col].get(direction).isAnt())
							tiles[row][col].setType(TileType.LAND);
	}

	public void setAnt(int row, int col, int owner)
	{
		if (owner == 0)
			tiles[row][col].setType(TileType.MY_ANT);
		else
			tiles[row][col].setType(TileType.ENEMY_ANT);
	}

	public void setWater(int row, int col)
	{
		tiles[row][col].setType(TileType.WATER);
	}

	public void setFood(int row, int col)
	{
		tiles[row][col].setType(TileType.FOOD);
	}

	public void setDead(int row, int col)
	{
		tiles[row][col].setType(TileType.DEAD);
	}

	public void setHill(int row, int col, int owner)
	{
		if (owner == 0)
			tiles[row][col].setType(TileType.MY_HILL);
		else
			tiles[row][col].setType(TileType.ENEMY_HILL);
	}

	public Collection<Tile> getMyAnts()
	{
		HashSet<Tile> ants = new HashSet<Tile>();
		for (int row = 0; row < nbRows; row++)
			for (int col = 0; col < nbCols; col++)
				if (tiles[row][col].getType() == TileType.MY_ANT)
					ants.add(tiles[row][col]);
		return ants;
	}

	public Tile getTile(Tile tile, Aim direction)
	{
		return tile.get(direction);
	}
}
