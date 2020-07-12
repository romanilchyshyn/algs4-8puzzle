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

    private boolean isSolvable;
    private int moves;
    private ArrayList<Board> solution;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial.isGoal()) {
            isSolvable = true;
            moves = 0;
            solution = new ArrayList<>();

            return;
        }

        MinPQ<Board> pqOriginal = new MinPQ<>(new Comparator<Board>() {
            public int compare(Board board, Board t1) {
                return (board.hamming() + board.manhattan()) - (t1.hamming() + t1.manhattan());
            }
        });

        MinPQ<Board> pqTwin = new MinPQ<>(new Comparator<Board>() {
            public int compare(Board board, Board t1) {
                return (board.hamming() + board.manhattan()) - (t1.hamming() + t1.manhattan());
            }
        });

        ArrayList<Board> visitedOriginal = new ArrayList<>();
        ArrayList<Board> visitedTwin = new ArrayList<>();

        Board currentOriginal = initial;
        Board currentTwin = initial.twin();

        visitedOriginal.add(currentOriginal);
        visitedTwin.add(currentTwin);

        while (!currentOriginal.isGoal() || !currentTwin.isGoal()) {
            for (Board nb : currentOriginal.neighbors()) {
                if (visitedOriginal.contains(nb)) {
                    continue;
                }
                pqOriginal.insert(nb);
            }
            currentOriginal = pqOriginal.delMin();
            visitedOriginal.add(currentOriginal);

            // -

            for (Board nb : currentTwin.neighbors()) {
                if (visitedTwin.contains(nb)) {
                    continue;
                }
                pqTwin.insert(nb);
            }
            currentTwin = pqTwin.delMin();
            visitedTwin.add(currentTwin);
        }

        isSolvable = currentOriginal.isGoal();
        moves = isSolvable ? visitedOriginal.size() - 1 : -1;
        solution = isSolvable ? visitedOriginal : null;
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
