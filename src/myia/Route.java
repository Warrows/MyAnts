package myia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Represents a route from one tile to another.
 */
public class Route implements Comparable<Route>
{
	private final Fourmi start;

	private final Tile end;

	private Ants ants;

	private ArrayList<Tile> trajet;

	public Route(Fourmi start, Tile end, Ants ants)
	{
		this.start = start;
		this.end = end;
		this.ants = ants;
		this.trajet = createTrajet();
	}

	public Fourmi getStart()
	{
		return start;
	}

	public Tile getEnd()
	{
		return end;
	}

	public int getDistance(Ants ants)
	{
		if (trajet.size()==0)
		{
			return ants.getRows()*ants.getCols();
		}
		return trajet.size();
	}
	
	public int getFlyDistance(Ants ants)
	{
		return ants.getDistance(start.getTile(), end);
	}

	public ArrayList<Tile> getTrajet()
	{
		return trajet;
	}

	private ArrayList<Tile> createTrajet()
	{
		teste = new HashSet<Tile>();
		ArrayList<Node> open = new ArrayList<Node>();
		open.add(new Node(start.getTile(), 0, end, ants));
		ArrayList<Node> close = new ArrayList<Node>();
		while (!open.isEmpty())
		{
			Collections.sort(open);
			Node current = open.get(0);
			if (current.getTile().equals(end))
			{
				ArrayList<Tile> retour = new ArrayList<Tile>();
				if (current.getPrec() == null)
				{
					retour.add(current.getTile());
				}
				while (current.getPrec() != null)
				{
					retour.add(0, current.getTile());
					current = current.getPrec();
				}
				return retour;
			}
			close.add(open.remove(open.indexOf(current)));
			for (Node neighbor : possibles(current))
			{
				if (close.contains(neighbor))
					continue;
				boolean better = false;
				if (!open.contains(neighbor))
				{
					open.add(neighbor);
					better = true;
				} else
				{
					if (current.getG() + 1 < neighbor.getG())
					{
						better = true;
					} else
					{
						better = false;
					}
				}

				if (better)
				{
					neighbor.setPrec(current);
					neighbor.setG(current.getG() + 1);
				}
			}
		}
		return new ArrayList<Tile>();
	}

	private HashSet<Tile> teste;

	private List<Node> possibles(Node current)
	{
		ArrayList<Node> liste = new ArrayList<Node>();
		for (Aim dir : Aim.values())
		{
			Tile but = ants.getTile(current.getTile(), dir);
			if (teste.add(but))
			{
				if (ants.isVisible(but) && !ants.getIlk(but).equals(Ilk.WATER)
						&& !ants.getMyHills().contains(but))
				{
					liste.add(new Node(but, current.getG() + 1, end, ants));
				}
			}
		}
		return liste;
	}

	@Override
	public int compareTo(Route route)
	{
		return getDistance(ants) - route.getDistance(route.getAnts());
	}

	private Ants getAnts()
	{
		return ants;
	}

	@Override
	public int hashCode()
	{
		return start.hashCode() * Ants.MAX_MAP_SIZE * Ants.MAX_MAP_SIZE
				+ end.hashCode();
	}

	@Override
	public boolean equals(Object o)
	{
		boolean result = false;
		if (o instanceof Route)
		{
			Route route = (Route) o;
			result = start.equals(route.start) && end.equals(route.end);
		}
		return result;
	}
}
