package myia;

public class Node implements Comparable<Node>
{
	private Tile tile;
	private int g;
	private int h;
	private Node prec;

	public Node(Tile tile, int g, Tile but, Ants ants)
	{
		this.tile = tile;
		this.g = g;
		this.h = ants.getDistance(tile,but);
		this.prec = null;
	}
	
	public Tile getTile()
	{
		return tile;
	}
	
	public int getF()
	{
		return g+h;
	}
	
	public int getH()
	{
		return h;
	}
	
	public void setG(int g)
	{
		this.g=g;
	}
	
	public int getG()
	{
		return g;
	}

	public int compareTo(Node o)
	{
		return getH()-o.getH();
	}

	public void setPrec(Node prec)
	{
		this.prec = prec;
	}

	public Node getPrec()
	{
		return prec;
	}
	
	public boolean equals(Node node)
	{
		return tile.equals(node.getTile());
	}
}
