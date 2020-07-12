/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class Board {

    private final int[][] tiles;
    private final int dimension;
    private int hamming = -1;
    private int manhattan = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.dimension = tiles.length;

        this.tiles = new int[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension());
        for (int i = 0; i < dimension; i++) {
            sb.append('\n');
            for (int j = 0; j < dimension; j++) {
                sb.append(String.format("%2d", tiles[i][j]));
                if (j != dimension - 1) {
                    sb.append(' ');
                }
            }
        }

        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        if (-1 < hamming) {
            return hamming;
        } else {
            hamming = 0;

            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    int tile = tiles[i][j];
                    if (tile == 0) {
                        continue;
                    }
                    if (tile != goalTileAtPosition(i, j)) {
                        hamming++;
                    }
                }
            }

            return hamming;
        }
    }

    private int goalTileAtPosition(int i, int j) {
        if (i + 1 == dimension && j + 1 == dimension) {
            return 0;
        } else {
            return i * dimension + j + 1;
        }
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (-1 < manhattan) {
            return manhattan;
        } else {
            manhattan = 0;

            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    int tile = tiles[i][j];
                    if (tile == 0) {
                        continue;
                    }

                    int tileGoalI = (tile - 1) / dimension;
                    int tileGoalJ = (tile - 1) % dimension;

                    int m = Math.abs(tileGoalI - i) + Math.abs(tileGoalJ - j);

                    manhattan += m;
                }
            }

            return manhattan;
        }
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }

        Board that = (Board) y;
        if (this.dimension() != that.dimension()) {
            return false;
        }

        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> result = new ArrayList<Board>();

        int zeroI = 0;
        int zeroJ = 0;

        int[][] neighborTiles = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                neighborTiles[i][j] = tiles[i][j];

                if (tiles[i][j] == 0) {
                    zeroI = i;
                    zeroJ = j;
                }
            }
        }

        if (zeroI + 1 < dimension) {
            Board.exch(neighborTiles, zeroI, zeroJ, zeroI + 1, zeroJ);
            result.add(new Board(neighborTiles));
            Board.exch(neighborTiles, zeroI + 1, zeroJ, zeroI, zeroJ);
        }
        if (0 <= zeroI - 1) {
            Board.exch(neighborTiles, zeroI, zeroJ, zeroI - 1, zeroJ);
            result.add(new Board(neighborTiles));
            Board.exch(neighborTiles, zeroI - 1, zeroJ, zeroI, zeroJ);
        }
        if (zeroJ + 1 < dimension) {
            Board.exch(neighborTiles, zeroI, zeroJ, zeroI, zeroJ + 1);
            result.add(new Board(neighborTiles));
            Board.exch(neighborTiles, zeroI, zeroJ + 1, zeroI, zeroJ);
        }
        if (0 <= zeroJ - 1) {
            Board.exch(neighborTiles, zeroI, zeroJ, zeroI, zeroJ - 1);
            result.add(new Board(neighborTiles));
            Board.exch(neighborTiles, zeroI, zeroJ - 1, zeroI, zeroJ);
        }

        return result;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinTiles = new int[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                twinTiles[i][j] = tiles[i][j];
            }
        }

        int i1 = StdRandom.uniform(dimension);
        int j1 = StdRandom.uniform(dimension);
        int i2 = StdRandom.uniform(dimension);
        int j2 = StdRandom.uniform(dimension);

        Board.exch(twinTiles, i1, j1, i2, j2);

        return new Board(twinTiles);
    }

    private static void exch(int [][] a, int i1, int j1, int i2, int j2) {
        int tmp = a[i1][j1];
        a[i1][j1] = a[i2][j2];
        a[i2][j2] = tmp;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // int[][] a1 = new int[][]{{1, 2, 3}, {4, 0, 6}, {7, 8, 5}};
        // Board b1 = new Board(a1);
        //
        // int[][] a2 = new int[][]{{1, 2, 3}, {4, 0, 6}, {7, 8, 5}};
        // Board b2 = new Board(a2);
        //
        // int[][] a3 = new int[][]{{0, 2, 3}, {4, 1, 6}, {7, 8, 5}};
        // Board b3 = new Board(a3);
        //
        // int[][] a4 = new int[][]{
        //         {1, 2, 3, 4},
        //         {5, 6, 7, 8},
        //         {9, 10, 11, 12},
        //         {13, 14, 15, 0}
        // };
        // Board b4 = new Board(a4);

        // System.out.println(b1);
        // System.out.println(b2);
        // System.out.println(b3);
        // System.out.println(b4);

        // System.out.println(b1.equals(b1));
        // System.out.println(b1.equals(b2));
        // System.out.println(b1.equals(b3));
        // System.out.println(b1.equals(b4));
        // System.out.println(b4.hamming());
        // System.out.println(b4.manhattan());
        // System.out.println(b4.isGoal());

        int[][] test1 = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board bTest1 = new Board(test1);

        System.out.println(bTest1);
        System.out.println(bTest1.hamming());
        System.out.println(bTest1.manhattan());


    }

}