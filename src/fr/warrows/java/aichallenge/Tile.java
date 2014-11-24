package fr.warrows.java.aichallenge;

import java.util.LinkedList;

/**
 * Represents a tile of the game map.
 */
public class Tile implements Comparable<Tile>
{
	private final int row;
	private final int col;
	private TileType type;
	private Tile[] neighbours;
	private boolean reserved;

	/**
	 * Creates new {@link Tile} object.
	 * 
	 * @param row
	 *            row index
	 * @param col
	 *            column index
	 */
	public Tile(int row, int col)
	{
		this.row = row;
		this.col = col;
		this.type = TileType.LAND;
		this.reserved = false;
		this.neighbours = new Tile[4]; // N S W E
	}

	public TileType getType()
	{
		return type;
	}

	/**
	 * Returns row index.
	 * 
	 * @return row index
	 */
	public int getRow()
	{
		return row;
	}

	/**
	 * Returns column index.
	 * 
	 * @return column index
	 */
	public int getCol()
	{
		return col;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Tile && ((Tile) o).col == this.col
				&& ((Tile) o).row == this.row)
			return true;
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return row + " " + col;
	}

	public void setNorth(Tile tile)
	{
		neighbours[Aim.NORTH.ordinal()] = tile;
	}

	public void setSouth(Tile tile)
	{
		neighbours[Aim.SOUTH.ordinal()] = tile;
	}

	public void setWest(Tile tile)
	{
		neighbours[Aim.WEST.ordinal()] = tile;
	}

	public void setEast(Tile tile)
	{
		neighbours[Aim.EAST.ordinal()] = tile;
	}

	public Tile get(Aim direction)
	{
		return neighbours[direction.ordinal()];
	}

	public boolean isPassable()
	{
		if (reserved || !type.isPassable())
			return false;
		return true;
	}

	public void setType(TileType type)
	{
		this.type = type;
	}

	public void unreserve()
	{
		reserved = false;
	}

	public void reserve()
	{
		reserved = true;
	}

	public boolean isAnt()
	{
		return type == TileType.MY_ANT || type == TileType.ENEMY_ANT;
	}

	public Tile getClosestFood() throws NoFoodFoundException
	{
		LinkedList<Tile> toTest = new LinkedList<Tile>();
		LinkedList<Tile> tested = new LinkedList<Tile>();
		toTest.add(this);

		while (!toTest.isEmpty())
		{
			if (toTest.getFirst().type == TileType.FOOD)
				return toTest.getFirst();

			for (Tile t : toTest.getFirst().neighbours)
				if (!tested.contains(t) && !toTest.contains(t))
					toTest.add(t);

			tested.add(toTest.getFirst());
			toTest.remove(toTest.getFirst());
		}
		throw new NoFoodFoundException();
	}

	@Override
	public int compareTo(Tile o)
	{
		return hashCode() - o.hashCode();
	}

	@Override
	public int hashCode()
	{
		return row * GameDataHandler.MAX_MAP_SIZE + col;
	}
}
