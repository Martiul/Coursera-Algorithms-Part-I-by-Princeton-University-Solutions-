import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean [] sites;
    private WeightedQuickUnionUF uf;
    private int n;
    private int openSites;

    // Constructor
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();

        uf = new WeightedQuickUnionUF(n*n+2);
        sites = new boolean[n*n+2];

        this.n = n;
        openSites = 0;
        // Should default to false
    }

    // Opens the cell at (row, col)
    public void open(int row, int col) {
        checkSpot(row, col);

        int cur = idxOf(row, col);
        if (!sites[cur]) {
            sites[cur] = true;
            openSites++;

            // Union top/bot with imaginary top and bottom
            if (row == 1) {
                uf.union(cur, 0);
            }
            if (row == n) {
                uf.union(cur, n*n+1);
            }

            // Union open neighbours
            if (row != 1 && isOpen(row-1, col)) {
                assert (cur > n);
                uf.union(cur, cur-n);
            }
            if (row != n && isOpen(row+1, col)) {
                assert(cur+n < n*n);
                uf.union(cur, cur+n);
            }

            if (col != 1 && isOpen(row, col-1)) {
                uf.union(cur, cur-1);
            }
            if (col != n && isOpen(row, col+1)) {
                uf.union(cur, cur+1);
            }
        }
    }

    // Determine if a cell is open
    public boolean isOpen(int row, int col)  {
        checkSpot(row, col);
        return sites[idxOf(row, col)];
    }

    // Determine if a cell is connected to the imaginary top
    public boolean isFull(int row, int col) {
        checkSpot(row,col);
        if (!isOpen(row, col)) {
            return false;
        }
        return uf.connected(idxOf(row, col), 0);
    }

    // Return the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // Determine if the system percolates
    public boolean percolates() {
        return uf.connected(0, n*n+1);
    }

    // Determine if a cell is legal
    private void checkSpot (int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    // Takes in (row, col) and returns the index of the cell within the 1D array
    private int idxOf(int row, int col) {
        return (row-1)*n + (col-1);
    }

    public static void main(String[] args) {
        // test client (optional)
    }  
}