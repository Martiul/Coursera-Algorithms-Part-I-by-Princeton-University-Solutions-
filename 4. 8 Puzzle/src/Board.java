import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

public final class Board {

    private final int n;
    private final int [][] grid;

    // Construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length;
        grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            grid[i] = blocks[i].clone();
        }
    }

    // Return the board's dimension
    public int dimension() {
        return n;
    }

    // Determine the number of blocks out of place
    public int hamming() {
        int re = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != 0 && grid[i][j] != i*n+j+1) {
                    re++;
                }
            }
        }
        return re;
    }

    // Determine the sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int re = 0;
        int yVal, xVal;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != 0) {
                    yVal = i - (grid[i][j]-1) / n;
                    xVal = j - (grid[i][j]-1) % n;
                    re += (yVal < 0) ? -yVal : yVal;
                    re += (xVal < 0) ? -xVal : xVal;
                }
            }
        }
        return re;
    }

    // Determine if this board is at its goal state
    public boolean isGoal() {
        return (hamming() == 0);
    }

    // Return a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int [][] newGrid = new int [n][n];
        for (int i = 0; i < n; i++) {
            newGrid[i] = grid[i].clone();
        }

        int tmp, rowNum = 0;
        // Just switch from the upper four squares
        if (newGrid[0][0] == 0 || newGrid[0][1] == 0) {
            rowNum = 1;
        }
        tmp = newGrid[rowNum][0];
        newGrid[rowNum][0] = newGrid[rowNum][1];
        newGrid[rowNum][1] = tmp;

        return new Board(newGrid);
    }

    // Determine if this Board and Board y are the same
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }

        Board that = (Board) y;
        if (grid.length != that.grid.length ||
                grid[0].length != that.grid[0].length) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != that.grid[i][j]) return false;
            }
        }
        return true;
    }

    // Find all boards that be made from the current board
    // Return a class that iterates through Boards
    public Iterable<Board> neighbors() {
        int [][] tempBlocks = new int[n][n];
        for (int i = 0; i < n; i++) tempBlocks[i] = grid[i].clone();

        Stack<Board> re = new Stack<Board>();

        // Find the blank space and move it around
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                if (grid[i][j] == 0) {
                    // Swap the blank space with adjacent squares
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {

                            // If only dy or dx is 0
                            if ((dy != 0 || dx != 0) &&
                                    (dy == 0 || dx == 0) &&
                                    i+dy >= 0 &&
                                    i+dy < grid.length &&
                                    j+dx >= 0 &&
                                    j+dx < grid.length) {

                                tempBlocks[i][j] = tempBlocks[i+dy][j+dx];
                                tempBlocks[i+dy][j+dx] = 0;

                                re.push(new Board(tempBlocks));

                                tempBlocks[i+dy][j+dx] = tempBlocks[i][j];
                                tempBlocks[i][j] = 0;
                            }
                        }
                    }
                    return re;  // Only one blank space, so we are done
                }
            }
        }
        return re;
    }

    // String representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        int n = grid.length;
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", grid[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // Unit tests (not graded)
    public static void main(String[] args) {
        int [][] arr = {{8, 2, 3}, {4, 5, 6}, {7, 1, 0}};
        Board b = new Board(arr);
        StdOut.println("=== SOURCE ===");
        StdOut.println(b.toString());

        Iterable<Board> neighbours = b.neighbors();
        for (Board n : neighbours) {
            StdOut.println(n.toString());
        }
    }
}