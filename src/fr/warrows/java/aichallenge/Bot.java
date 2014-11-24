package fr.warrows.java.aichallenge;

/**
 * Provides basic game state handling.
 */
public abstract class Bot extends AbstractSystemInputParser
{
	private GameDataHandler gameDataHandler;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setup(int loadTime, int turnTime, int rows, int cols,
			int turns, int viewRadius2, int attackRadius2, int spawnRadius2)
	{
		setAnts(new GameDataHandler(loadTime, turnTime, rows, cols, turns, viewRadius2,
				attackRadius2, spawnRadius2));
	}

	/**
	 * Returns game state information.
	 * 
	 * @return game state information
	 */
	public GameDataHandler getAnts()
	{
		return gameDataHandler;
	}

	/**
	 * Sets game state information.
	 * 
	 * @param gameDataHandler
	 *            game state information to be set
	 */
	protected void setAnts(GameDataHandler gameDataHandler)
	{
		this.gameDataHandler = gameDataHandler;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeUpdate()
	{
		gameDataHandler.setTurnStartTime(System.currentTimeMillis());
		gameDataHandler.getMap().clear();
		gameDataHandler.getOrders().clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addWater(int row, int col)
	{
		gameDataHandler.getMap().setWater(row, col);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAnt(int row, int col, int owner)
	{
		gameDataHandler.getMap().setAnt(row, col, owner);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addFood(int row, int col)
	{
		gameDataHandler.getMap().setFood(row, col);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAnt(int row, int col, int owner)
	{
		gameDataHandler.getMap().setDead(row, col);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addHill(int row, int col, int owner)
	{
		gameDataHandler.getMap().setHill(row, col, owner);
	}
}
