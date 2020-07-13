/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;

public class Solver {

    private static final class SearchNodeComparator implements Comparator<SearchNode> {
        public int compare(SearchNode sn1, SearchNode sn2) {
            return sn1.priority - sn2.priority;
        }
    }

    private final class SearchNode {
        private final Board board;
        private final SearchNode parent;
        private final int moves;
        private final int priority;


        SearchNode(Board board, SearchNode parent, int moves) {
            this.board = board;
            this.parent = parent;
            this.moves = moves;
            this.priority = moves + board.manhattan();
        }

    }

    private final class SolverStepper {

        private MinPQ<SearchNode> pq = new MinPQ<>(new SearchNodeComparator());
        private SearchNode min;

        private SolverStepper(Board initial) {
            SearchNode initialSearchNode = new SearchNode(initial, null, 0);
            pq.insert(initialSearchNode);
            min = initialSearchNode;
        }

        private Board next() {
            if (min.board.isGoal()) {
                return min.board;
            }

            min = pq.delMin();

            for (Board nb : min.board.neighbors()) {
                if (min.parent != null && min.parent.board.equals(nb)) {
                    continue;
                }

                SearchNode childSN = new SearchNode(nb, min, min.moves + 1);

                pq.insert(childSN);
            }

            return min.board;
        }

        private ArrayList<Board> solution() {
            SearchNode sn = min;
            if (!sn.board.isGoal()) {
                return null;
            }

            ArrayList<Board> reversedSolution = new ArrayList<>();
            while (sn != null) {
                reversedSolution.add(sn.board);
                sn = sn.parent;
            }

            ArrayList<Board> s = new ArrayList<>();
            for (int i = reversedSolution.size() - 1; i >= 0; i--) {
                s.add(reversedSolution.get(i));
            }

            return s;
        }

    }

    private boolean isSolvable;
    private int moves;
    private ArrayList<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        if (initial.isGoal()) {
            isSolvable = true;
            moves = 0;
            solution = new ArrayList<>();
            solution.add(initial);

            return;
        }

        SolverStepper original = new SolverStepper(initial);
        SolverStepper twin = new SolverStepper(initial.twin());

        while (true) {
            Board nextOriginal = original.next();
            Board nextTwin = twin.next();

            if (nextOriginal.isGoal() || nextTwin.isGoal()) {
                break;
            }
        }

        isSolvable = original.next().isGoal();
        moves = isSolvable ? original.solution().size() - 1 : -1;
        solution = isSolvable ? original.solution() : null;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

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
