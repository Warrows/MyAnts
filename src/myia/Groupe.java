package myia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Groupe
{
	private HashMap<Fourmi, Integer> fourmis;
	private ArrayList<Fourmi> fourmisToMaj;
	private Ants ants;
	private ArrayList<Tile> miam;
	private ArrayList<Tile> miamChoisi;
	private ArrayList<Tile> carte;
	private int tour;
	private ArrayList<Fourmi> exploratrices;
	private ArrayList<Tile> cible;

	public Groupe(Ants ants)
	{
		fourmis = new HashMap<Fourmi, Integer>();
		fourmisToMaj = new ArrayList<Fourmi>();
		this.ants = ants;
		miam = new ArrayList<Tile>();
		carte = new ArrayList<Tile>();
		carte.addAll(ants.getMyHills());
		miamChoisi = new ArrayList<Tile>();
		exploratrices = new ArrayList<Fourmi>();
		cible = new ArrayList<Tile>();
		tour = 0;
	}

	public void add(Fourmi f)
	{
		if (!fourmis.containsKey(f.getId()))
		{
			fourmis.put(f, f.getId());
		}
	}

	public void majFourmi(Fourmi f)
	{
		fourmisToMaj.add(f);
	}

	public void majFourmis()
	{
		for (Fourmi f : fourmisToMaj)
		{
			fourmis.remove(f);
			fourmis.put(f, f.getId());
		}
		fourmisToMaj = new ArrayList<Fourmi>();
	}

	public boolean isOccupied(Tile t)
	{
		if (ants.getMyHills().contains(t))
			return true;
		for (Fourmi f : fourmis.keySet())
		{
			if (f.getTile().equals(t))
				return true;
		}
		return false;
	}

	public Fourmi getFourmiByTile(Tile t)
	{
		for (Fourmi f : fourmis.keySet())
		{
			if (t.equals(f.getTile()))
				return f;
		}
		return null;
	}

	private Set<Fourmi> fourmisDispos(boolean dispo)
	{
		Set<Fourmi> set = new HashSet<Fourmi>();
		for (Fourmi f : fourmis.keySet())
		{
			if (!f.moved() && dispo && !f.hasBut())
				set.add(f);
			if (f.moved() && !dispo || !dispo && f.hasBut())
				set.add(f);
		}
		return set;
	}

	private Set<Fourmi> fourmisDispos(Tile t, int i)
	{
		Set<Fourmi> set = new HashSet<Fourmi>();
		for (Fourmi f : fourmis.keySet())
		{
			if (!f.moved()
					&& !f.hasBut()
					&& ants.getDistance(f.getTile(), t) < i
							* ants.getViewRadius2())
				set.add(f);
		}
		return set;
	}

	public ArrayList<Tile> getCarte()
	{
		return carte;
	}

	public Tile getTileCarte(Tile t)
	{
		for (Tile t2 : carte)
			if (t.equals(t2))
			{
				return t2;
			}
		carte.add(t);
		return t;
	}

	public void preparerTour()
	{
		cible.addAll(ants.getEnemyHills());
		ArrayList<Tile> toRemove = new ArrayList<Tile>();
		for (Tile t : cible)
		{
			if (ants.isVisible(t) && !ants.getEnemyHills().contains(t))
				toRemove.add(t);
		}
		cible.removeAll(toRemove);
		nettoyerMorts();
		for (Fourmi f : fourmis.keySet())
		{
			f.preparer();
		}
		preparerManger();
	}

	public void ajouterToCarte(Tile t)
	{
		if (!carte.contains(t))
			carte.add(t);
	}

	public void preparerManger()
	{
		ArrayList<Tile> aRetirerMiam = new ArrayList<Tile>();
		for (Tile t : miam)
		{
			if (!ants.getIlk(t).equals(Ilk.FOOD))
			{
				aRetirerMiam.add(t);
				if (miamChoisi.contains(t))
					miamChoisi.removeAll(aRetirerMiam);
				for (Fourmi f : fourmisDispos(false))
				{
					if (f.getBut().equals(t))
						f.resetChemin();
				}
			}
		}
		miam.removeAll(aRetirerMiam);
		for (Tile t : ants.getFoodTiles())
			if (!miam.contains(t))
				miam.add(t);
	}

	private void nettoyerMorts()
	{
		ArrayList<Fourmi> fourmisToDel = new ArrayList<Fourmi>();
		for (Fourmi f : fourmis.keySet())
		{
			if (!ants.getIlk(f.getTile()).equals(Ilk.MY_ANT))
				fourmisToDel.add(f);
		}
		HashMap<Fourmi, Integer> temp = new HashMap<Fourmi, Integer>();
		for (Fourmi f : fourmis.keySet())
		{
			if (!fourmisToDel.contains(f))
				temp.put(f, f.getId());
			fourmis = temp;
		}
	}

	public ArrayList<Tile> getMiam()
	{
		return miam;
	}

	private void ajouterRecolteuse(Route choisie, Tile t)
	{
		choisie.getStart().ajouterChemin(choisie.getTrajet());
		t.getFourmis().add(choisie.getStart());
	}

	public void bougerFourmis()
	{
		if (tour < 30)
		{
			commencer();
		} else
		{
			manger();
			explorer();
			attaquer();
		}

		for (Fourmi f : fourmisDispos(false))
		{
			f.bouger(f.getProchain());
		}
		for (Fourmi f : fourmisDispos(true))
		{
			f.bouger(null);
		}
		majFourmis();
	}

	private void attaquer()
	{
		if (!cible.isEmpty())
		{
			for (Tile t : cible)
			{
				for (Fourmi f : fourmisDispos(t, 2))
				{
					ajouterRecolteuse(new Route(f, t, ants), t);
				}
			}
		}
	}

	private void explorer()
	{
		ArrayList<Fourmi> al = new ArrayList<Fourmi>();
		al.addAll(fourmisDispos(true));
		while (exploratrices.size() < fourmis.size() / 10
				&& exploratrices.size() < 15)
		{
			exploratrices.add(al.remove(0));
		}

		// for (Fourmi f : exploratrices())
	}

	private void manger()
	{
		Collections.shuffle(miam);
		for (Tile t : miam)
		{
			if (t.getFourmis().size() < 3)
			{
				ArrayList<Route> trajets = new ArrayList<Route>();
				for (Fourmi f : fourmisDispos(t, 2))
				{
					trajets.add(new Route(f, t, ants));
				}
				Collections.sort(trajets);
				if (!trajets.isEmpty())
				{
					ajouterRecolteuse(trajets.get(0), t);
				}
			}
		}
	}

	private void commencer()
	{
		{
			for (Fourmi f : fourmisDispos(true))
			{
				ArrayList<Route> routes = new ArrayList<Route>();
				for (Tile t : miam)
				{
					if (!miamChoisi.contains(t))
					{
						routes.add(new Route(f, t, ants));
					}
				}
				Collections.sort(routes);
				if (!routes.isEmpty())
				{
					miamChoisi.add(routes.get(0).getEnd());
					ajouterRecolteuse(routes.get(0), routes.get(0).getEnd());
				}
			}
		}
	}

	public void tourPlus()
	{
		this.tour++;
	}
}
