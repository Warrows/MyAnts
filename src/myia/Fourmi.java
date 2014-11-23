package myia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Fourmi implements Comparable<Fourmi>
{
	public static int idIncrementor = 0;
	private Tile tile;
	private Ants ants;
	private Groupe groupe;
	private int id;
	private boolean moved;
	private ArrayList<Tile> chemin;
	private boolean inEscouade;

	public Fourmi(Tile tile, Ants ants, Groupe groupe)
	{
		idIncrementor++;
		id = idIncrementor;
		this.tile = tile;
		this.ants = ants;
		this.groupe = groupe;
		moved = false;
		chemin = new ArrayList<Tile>();
		inEscouade = false;
	}

	public boolean hasBut()
	{
		return !(chemin.isEmpty()&&!inEscouade);
	}

	public Tile getProchain()
	{
		return chemin.remove(0);
	}

	public void resetChemin()
	{
		chemin = new ArrayList<Tile>();
	}

	public void ajouterChemin(Collection<Tile> t)
	{
		chemin.addAll(t);
	}

	public void bouger(Tile obj)
	{
		Aim direction;
		if (obj == null)
			direction = choisirDir();
		else
			direction = choisirDir(obj);

		if (direction != null)
		{
			ants.issueOrder(tile, direction);
			tile = groupe.getTileCarte(ants.getTile(tile, direction));
			if (tile == null)
			{
				groupe.ajouterToCarte(tile);
				tile = ants.getTile(tile, direction);
			}
			groupe.majFourmi(this);
			moved = true;
		}
	}
	

	private Aim choisirDir(Tile but)
	{
		List<Aim> directions = ants.getDirections(tile, but);
		for (Aim direction : directions)
		{
			if (ants.getIlk(tile, direction).isUnoccupied()
					&& !groupe.isOccupied(ants.getTile(tile, direction)))
			{
				return direction;
			}
		}
		return null;
	}

	private Aim choisirDir()
	{
		List<Aim> possibles = new ArrayList<Aim>();
		for (Aim direction : Aim.values())
		{
			if (ants.getIlk(tile, direction).isUnoccupied()
					&& !groupe.isOccupied(groupe.getTileCarte(ants.getTile(tile, direction))))
			{
					possibles.add(direction);
				
			}
		}
		if (!possibles.isEmpty())
			return possibles.get((int) (Math.random() * possibles.size()));
		return null;
	}

	public void setMoved()
	{
		moved = true;
	}
	
	public Tile getBut()
	{
		return chemin.get(chemin.size()-1);
	}
	
	public void setInEscouade (boolean b)
	{
		inEscouade=b;
	}

	public boolean moved()
	{
		return moved;
	}

	public void preparer()
	{
		if (chemin.size() > 0
				&& !groupe.getMiam().contains(chemin.get(chemin.size() - 1)))
			chemin = new ArrayList<Tile>();
		moved = false;
	}

	public Tile getTile()
	{
		return tile;
	}

	@Override
	public int compareTo(Fourmi f)
	{
		return tile.compareTo(f.getTile());
	}

	public int getId()
	{
		return id;
	}
}
