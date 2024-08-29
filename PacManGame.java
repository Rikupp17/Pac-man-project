import java.util.Random;
import java.util.Scanner;

public class PacManGame {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private static final char PACMAN = 'P';
    private static final char GHOST = 'G';
    private static final char DOT = '.';
    private static final char POWER_UP = '*';
    private static final char EMPTY = ' ';

    private char[][] maze;
    private int pacmanX, pacmanY;
    private int score;
    private boolean gameOver;
    private boolean invincible;

    public PacManGame() {
        maze = new char[HEIGHT][WIDTH];
        score = 0;
        gameOver = false;
        invincible = false;
        initializeMaze();
    }

    private void initializeMaze() {
        Random random = new Random();

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                maze[i][j] = DOT;
            }
        }

        // Place Pac-Man in the center
        pacmanX = HEIGHT / 2;
        pacmanY = WIDTH / 2;
        maze[pacmanX][pacmanY] = PACMAN;

        // Place ghosts randomly
        for (int i = 0; i < 3; i++) {
            placeRandom(GHOST);
        }

        // Place power-ups randomly
        for (int i = 0; i < 2; i++) {
            placeRandom(POWER_UP);
        }
    }

    private void placeRandom(char item) {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(HEIGHT);
            y = random.nextInt(WIDTH);
        } while (maze[x][y] != DOT);
        maze[x][y] = item;
    }

    private void printMaze() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Score: " + score);
        System.out.println("Invincible: " + (invincible ? "Yes" : "No"));
    }

    private void movePacMan(char direction) {
        int newX = pacmanX, newY = pacmanY;

        switch (direction) {
            case 'U': newX--; break;
            case 'D': newX++; break;
            case 'L': newY--; break;
            case 'R': newY++; break;
            default: System.out.println("Invalid move!"); return;
        }

        if (newX < 0 || newX >= HEIGHT || newY < 0 || newY >= WIDTH) {
            System.out.println("Cannot move outside the maze!");
            return;
        }

        if (maze[newX][newY] == GHOST) {
            if (invincible) {
                System.out.println("Ghost eaten!");
                maze[newX][newY] = EMPTY;
            } else {
                gameOver = true;
                System.out.println("Game Over! You were caught by a ghost.");
                return;
            }
        } else if (maze[newX][newY] == DOT) {
            score++;
        } else if (maze[newX][newY] == POWER_UP) {
            invincible = true;
            score += 10;
        }

        // Move Pac-Man
        maze[pacmanX][pacmanY] = EMPTY;
        pacmanX = newX;
        pacmanY = newY;
        maze[pacmanX][pacmanY] = PACMAN;
    }

    private void moveGhosts() {
        Random random = new Random();

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (maze[i][j] == GHOST) {
                    int newX = i, newY = j;
                    switch (random.nextInt(4)) {
                        case 0: newX--; break;
                        case 1: newX++; break;
                        case 2: newY--; break;
                        case 3: newY++; break;
                    }

                    if (newX < 0 || newX >= HEIGHT || newY < 0 || newY >= WIDTH || maze[newX][newY] == GHOST) {
                        continue;
                    }

                    if (maze[newX][newY] == PACMAN && !invincible) {
                        gameOver = true;
                        System.out.println("Game Over! You were caught by a ghost.");
                        return;
                    }

                    maze[i][j] = EMPTY;
                    maze[newX][newY] = GHOST;
                }
            }
        }
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (!gameOver) {
            printMaze();
            System.out.println("Move (U/D/L/R): ");
            char move = scanner.next().charAt(0);
            movePacMan(move);
            moveGhosts();
        }

        scanner.close();
    }

    public static void main(String[] args) {
        PacManGame game = new PacManGame();
        game.play();
    }
}
