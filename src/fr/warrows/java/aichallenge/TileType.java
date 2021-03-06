package fr.warrows.java.aichallenge;
/**
 * Represents type of tile on the game map.
 */
public enum TileType {
    /** Water tile. */
    WATER,
    
    /** Food tile. */
    FOOD,
    
    /** Land tile. */
    LAND,
    
    /** Dead ant tile. */
    DEAD,
    
    /** My ant tile. */
    MY_ANT,
    
    /** Enemy ant tile. */
    ENEMY_ANT,
    
    MY_HILL,
    
    ENEMY_HILL;
    
    /**
     * Checks if this type of tile is passable, which means it is not a water tile.
     * 
     * @return <code>true</code> if this is not a water tile, <code>false</code> otherwise
     */
    public boolean isPassable() {
        return ordinal() > WATER.ordinal() || this == MY_ANT || this == MY_HILL;
    }
    
    /**
     * Checks if this type of tile is unoccupied, which means it is a land tile or a dead ant tile.
     * 
     * @return <code>true</code> if this is a land tile or a dead ant tile, <code>false</code>
     *         otherwise
     */
    public boolean isUnoccupied() {
        return this == LAND || this == DEAD;
    }
}
