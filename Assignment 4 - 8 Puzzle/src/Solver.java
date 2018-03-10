import edu.princeton.cs.algs4.*;

public class Solver {

    private boolean solvable;
    private SearchNode root;        // Root node of solution

    // Find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));
        pq.insert(new SearchNode(initial.twin(), 0, null));

        while (!pq.isEmpty()) {
            SearchNode cur = pq.delMin();

            if (cur.isGoal()) {

                // Determine if this is the initial board (solvable)
                SearchNode tmp = cur;
                while (tmp.predecessor != null) {
                    tmp = tmp.predecessor;
                }
                solvable = tmp.board.equals(initial);
                root = cur;
                break;
            }

            Iterable<Board> neighbours = cur.neighbours();
            SearchNode pred = cur.predecessor;
            Board predBoard = (pred == null) ? null : pred.board;

            for (Board b : neighbours) {
                // Add non-predecessor neighbours to pq
                if (!b.equals(predBoard)) {
                    pq.insert(new SearchNode(b, cur.moves+1, cur));
                }
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solvable) return root.moves;
        else return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) return null;
        Stack<Board> re = new Stack<Board>();
        SearchNode tmp = root;
        while (tmp != null) {
            re.push(tmp.board);
            tmp = tmp.predecessor;
        }
        return re;
    }


    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final int priority;           // Manhattan
        private final SearchNode predecessor;

        public SearchNode(Board board, int moves, SearchNode predecessor) {
            this.board = board;
            this.moves = moves;
            this.predecessor = predecessor;
            this.priority = board.manhattan() + this.moves;
        }

        public int compareTo(SearchNode that) {
            if (this.priority < that.priority) return -1;
            else if (this.priority > that.priority) return 1;
            else return 0;
        }

        public Iterable<Board> neighbours() {
            return board.neighbors();
        }

        public boolean isGoal() {
            return moves == priority;
        }

    }

    public static void main(String[] args) {

        // create initial board from file
//        In in = new In(args[0]);
//        int n = in.readInt();
        int n = StdIn.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
//                blocks[i][j] = in.readInt();
                blocks[i][j] = StdIn.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}