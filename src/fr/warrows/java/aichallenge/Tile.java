package fr.warrows.java.aichallenge;

/**
 * Represents a tile of the game map.
 */
public class Tile
{
	private final int row;
	private final int col;
	private TileType type;
	private Tile north, east, west, south;
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
		north = tile;
	}

	public void setSouth(Tile tile)
	{
		south = tile;
	}

	public void setWest(Tile tile)
	{
		west = tile;
	}

	public void setEast(Tile tile)
	{
		east = tile;
	}

	public Tile get(Aim direction)
	{
		switch (direction)
		{
		case EAST:
			return east;
		case NORTH:
			return north;
		case SOUTH:
			return south;
		case WEST:
			return west;
		}
		return null;
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
}
