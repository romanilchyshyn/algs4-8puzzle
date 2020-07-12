/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Board {

    private int[][] tiles;
    private int dimension;

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
                sb.append(' ');
                sb.append(' ');
                sb.append(tiles[i][j]);
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
        int result = 0;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int tile = tiles[i][j];
                if (tile == 0) {
                    continue;
                }
                if (tile != goalTileAtPosition(i, j)) {
                    result++;
                }
            }
        }

        return result;
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
        int result = 0;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int tile = tiles[i][j];
                if (tile == 0) {
                    continue;
                }

                int tileGoalI = (tile - 1) / dimension;
                int tileGoalJ = (tile - 1) % dimension;

                int m = Math.abs(tileGoalI - i) + Math.abs(tileGoalJ - j);

                result += m;
            }
        }

        return result;
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
        throw new UnsupportedOperationException();
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        throw new UnsupportedOperationException();
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

        int[][] test = new int[][]{{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board bTest = new Board(test);

        System.out.println(bTest);
        System.out.println(bTest.hamming());
        System.out.println(bTest.manhattan());

    }

}