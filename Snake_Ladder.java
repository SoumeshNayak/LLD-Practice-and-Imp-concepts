import java.util.*;

interface Obstacle {
    int apply(int pos);
}

class Snake implements Obstacle {
    private int s;
    private int e;

    Snake(int s, int e) {
        this.s = s;
        this.e = e;
    }

    public int apply(int pos) {
        if (pos == s) {
            System.out.println("Bitten by snake! Down to " + e);
            return e;
        }
        return pos;
    }
}

class Ladder implements Obstacle {
    private int s;
    private int e;

    Ladder(int s, int e) {
        this.s = s;
        this.e = e;
    }

    public int apply(int pos) {
        if (pos == s) {
            System.out.println("Climbed ladder! Up to " + e);
            return e;
        }
        return pos;
    }
}

class ObstacleFactory {
    public static Obstacle createObstacle(String type, int s, int e) {
        if (type.equalsIgnoreCase("snake")) {
            return new Snake(s, e);
        } else if (type.equalsIgnoreCase("ladder")) {
            return new Ladder(s, e);
        } else {
            throw new IllegalArgumentException("Invalid type!");
        }
    }
}

class Player {
    String name;
    int pos;

    Player(String name) {
        this.name = name;
        this.pos = 0;
    }
}

class Dice {
    private Random rand = new Random();

    public int roll() {
        return rand.nextInt(6) + 1;
    }
}

class Board {
    int size;
    Map<Integer, Obstacle> obstacles;

    Board(int size) {
        this.size = size;
        this.obstacles = new HashMap<>();
    }

    public void addObstacle(Obstacle ob, int s) {
        obstacles.put(s, ob);
    }

    public int checkObstacle(int pos) {
        if (obstacles.containsKey(pos)) {
            return obstacles.get(pos).apply(pos);
        }
        return pos;
    }
}

class Game {
    private Board board;
    private Queue<Player> players;
    private Dice dice;

    public Game(int boardSize, List<Player> playersList) {
        this.board = new Board(boardSize);
        this.players = new LinkedList<>(playersList);
        this.dice = new Dice();
    }

    public void addObstacle(String type, int s, int e) {
        Obstacle obs = ObstacleFactory.createObstacle(type, s, e);
        board.addObstacle(obs, s);
    }

    public void startGame() {
        while (true) {
            Player p = players.poll();

            int diceValue = dice.roll();
            System.out.println(p.name + " rolled " + diceValue);

            int newpos = p.pos + diceValue;

            if (newpos > board.size) {
                players.offer(p);
                continue;
            }

            newpos = board.checkObstacle(newpos);
            p.pos = newpos;

            System.out.println(p.name + " moved to " + newpos);

            if (newpos == board.size) {
                System.out.println(p.name + " wins!");
                break;
            }

            players.offer(p);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        int boardSize = 100;

        List<Player> players = new ArrayList<>();
        players.add(new Player("A"));
        players.add(new Player("B"));

        Game game = new Game(boardSize, players);

        game.addObstacle("snake", 99, 10);
        game.addObstacle("snake", 70, 50);
        game.addObstacle("ladder", 3, 22);
        game.addObstacle("ladder", 5, 40);

        game.startGame();
    }
}
