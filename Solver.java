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

    private static final class BoardComparator implements Comparator<Board> {
        public int compare(Board board, Board t1) {
            return board.manhattan() - t1.manhattan();
        }
    }

    private final class SolverStepper {

        private Board current;
        private ArrayList<Board> solution = new ArrayList<>();

        private SolverStepper(Board initial) {
            current = initial;
            solution.add(current);
        }

        private Board next() {
            if (current.isGoal()) {
                return current;
            }

            MinPQ<Board> pq = new MinPQ<>(new BoardComparator());
            for (Board nb : current.neighbors()) {
                if (solution.size() > 1 && solution.get(solution.size() - 2).equals(nb)) {
                    continue;
                }
                pq.insert(nb);
            }

            current = pq.delMin();

            solution.add(current);

            return current;
        }

        private ArrayList<Board> solution() {
            return solution;
        }

        private Board current() {
            return current;
        }
    }

    private boolean isSolvable;
    private int moves;
    private ArrayList<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
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

        isSolvable = original.current().isGoal();
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
