package myia;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Handles system input stream parsing.
 */
public abstract class AbstractSystemInputParser extends AbstractSystemInputReader {
    private static final String READY = "ready";
    
    private static final String GO = "go";
    
    private static final char COMMENT_CHAR = '#';
    
    private final List<String> input = new ArrayList<String>();
    
    private enum SetupToken {
        LOADTIME, TURNTIME, ROWS, COLS, TURNS, VIEWRADIUS2, ATTACKRADIUS2, SPAWNRADIUS2;
        
        private static final Pattern PATTERN = compilePattern(SetupToken.class);
    }
    
    private enum UpdateToken {
        W, A, F, D, H;
        
        private static final Pattern PATTERN = compilePattern(UpdateToken.class);
    }
    
    private static Pattern compilePattern(Class<? extends Enum> clazz) {
        StringBuilder builder = new StringBuilder("(");
        for (Enum enumConstant : clazz.getEnumConstants()) {
            if (enumConstant.ordinal() > 0) {
                builder.append("|");
            }
            builder.append(enumConstant.name());
        }
        builder.append(")");
        return Pattern.compile(builder.toString());
    }
    
    /**
     * Collects lines read from system input stream until a keyword appears and then parses them.
     */
    @Override
    public void processLine(String line) {
        if (line.equals(READY)) {
            parseSetup(input);
            doTurn();
            finishTurn();
            input.clear();
        } else if (line.equals(GO)) {
            parseUpdate(input);
            doTurn();
            finishTurn();
            input.clear();
        } else if (!line.isEmpty()) {
            input.add(line);
        }
    }
    
    /**
     * Parses the setup information from system input stream.
     * 
     * @param input setup information
     */
    public void parseSetup(List<String> input) {
        int loadTime = 0;
        int turnTime = 0;
        int rows = 0;
        int cols = 0;
        int turns = 0;
        int viewRadius2 = 0;
        int attackRadius2 = 0;
        int spawnRadius2 = 0;
        for (String line : input) {
            line = removeComment(line);
            if (line.isEmpty()) {
                continue;
            }
            StringTokenizer scanner = new StringTokenizer(line);
            if (!scanner.hasMoreTokens()) {
                continue;
            }
            String token = scanner.nextToken().toUpperCase();
            if (!SetupToken.PATTERN.matcher(token).matches()) {
                continue;
            }
            SetupToken setupToken = SetupToken.valueOf(token);
            switch (setupToken) {
                case LOADTIME:
                    loadTime = Integer.parseInt(scanner.nextToken());
                break;
                case TURNTIME:
                    turnTime = Integer.parseInt(scanner.nextToken());
                break;
                case ROWS:
                    rows = Integer.parseInt(scanner.nextToken());
                break;
                case COLS:
                    cols = Integer.parseInt(scanner.nextToken());
                break;
                case TURNS:
                    turns = Integer.parseInt(scanner.nextToken());
                break;
                case VIEWRADIUS2:
                    viewRadius2 = Integer.parseInt(scanner.nextToken());
                break;
                case ATTACKRADIUS2:
                    attackRadius2 = Integer.parseInt(scanner.nextToken());
                break;
                case SPAWNRADIUS2:
                    spawnRadius2 = Integer.parseInt(scanner.nextToken());
                break;
            }
        }
        setup(loadTime, turnTime, rows, cols, turns, viewRadius2, attackRadius2, spawnRadius2);
    }
    
    /**
     * Parses the update information from system input stream.
     * 
     * @param input update information
     */
    public void parseUpdate(List<String> input) {
        beforeUpdate();
        for (String line : input) {
            line = removeComment(line);
            if (line.isEmpty()) {
                continue;
            }
            StringTokenizer scanner = new StringTokenizer(line);
            if (!scanner.hasMoreTokens()) {
                continue;
            }
            String token = scanner.nextToken().toUpperCase();
            if (!UpdateToken.PATTERN.matcher(token).matches()) {
                continue;
            }
            UpdateToken updateToken = UpdateToken.valueOf(token);
            int row = Integer.parseInt(scanner.nextToken());
            int col = Integer.parseInt(scanner.nextToken());
            switch (updateToken) {
                case W:
                    addWater(row, col);
                break;
                case A:
                    if (scanner.hasMoreTokens()) {
                        addAnt(row, col, Integer.parseInt(scanner.nextToken()));
                    }
                break;
                case F:
                    addFood(row, col);
                break;
                case D:
                    if (scanner.hasMoreTokens()) {
                        removeAnt(row, col, Integer.parseInt(scanner.nextToken()));
                    }
                break;
                case H:
                    if (scanner.hasMoreTokens()) {
                        addHill(row, col, Integer.parseInt(scanner.nextToken()));
                    }
                break;
            }
        }
        afterUpdate();
    }
    
    /**
     * Sets up the game state.
     * 
     * @param loadTime timeout for initializing and setting up the bot on turn 0
     * @param turnTime timeout for a single game turn, starting with turn 1
     * @param rows game map height
     * @param cols game map width
     * @param turns maximum number of turns the game will be played
     * @param viewRadius2 squared view radius of each ant
     * @param attackRadius2 squared attack radius of each ant
     * @param spawnRadius2 squared spawn radius of each ant
     */
    public abstract void setup(int loadTime, int turnTime, int rows, int cols, int turns,
            int viewRadius2, int attackRadius2, int spawnRadius2);
    
    /**
     * Enables performing actions which should take place prior to updating the game state, like
     * clearing old game data.
     */
    public abstract void beforeUpdate();
    
    /**
     * Adds new water tile.
     * 
     * @param row row index
     * @param col column index
     */
    public abstract void addWater(int row, int col);
    
    /**
     * Adds new ant tile.
     * 
     * @param row row index
     * @param col column index
     * @param owner player id
     */
    public abstract void addAnt(int row, int col, int owner);
    
    /**
     * Adds new food tile.
     * 
     * @param row row index
     * @param col column index
     */
    public abstract void addFood(int row, int col);
    
    /**
     * Removes dead ant tile.
     * 
     * @param row row index
     * @param col column index
     * @param owner player id
     */
    public abstract void removeAnt(int row, int col, int owner);
    
    /**
     * Adds new hill tile.
     *
     * @param row row index
     * @param col column index
     * @param owner player id
     */
    public abstract void addHill(int row, int col, int owner);
    
    /**
     * Enables performing actions which should take place just after the game state has been
     * updated.
     */
    public abstract void afterUpdate();
    
    /**
     * Subclasses are supposed to use this method to process the game state and send orders.
     */
    public abstract void doTurn();
    
    /**
     * Finishes turn.
     */
    public void finishTurn() {
        System.out.println("go");
        System.out.flush();
    }
    
    private String removeComment(String line) {
        int commentCharIndex = line.indexOf(COMMENT_CHAR);
        String lineWithoutComment;
        if (commentCharIndex >= 0) {
            lineWithoutComment = line.substring(0, commentCharIndex).trim();
        } else {
            lineWithoutComment = line;
        }
        return lineWithoutComment;
    }
}
