import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Puzzle {
    private Board board;
    private static PriorityQueue<Board> queue = new PriorityQueue<>((a, b) -> {
        if(a.getF() == b.getF()) {
            return 0;
        }
        else if(a.getF() > b.getF()) {
            return 1;
        }
        else {
            return -1;
        }
    });
    private static HashMap<Board, Board> closed = new HashMap<>();

    public Puzzle(String state) {
        board = new Board(state);
    }

    public Board solvePuzzle() {
        board.setG(0);
        board.setH(board.goalOffset());
        queue.offer(board);
        Board finalBoard = null;

        while(!queue.isEmpty()) {
            Board current = queue.poll();

            if(current.goalOffset() == 0) {
                finalBoard = current;
                break;
            }

            ArrayList<Board> children = current.getValidChildren();
            for(Board child : children) {
                boolean addToQueue = true;

                if(queue.contains(child)) {
                    Board duplicateBoard = queue.remove();
                    if(duplicateBoard.getF() < child.getF()) {
                        queue.offer(duplicateBoard);
                        addToQueue = false;
                    }
                }
                else if(closed.containsKey(child)) {
                    Board duplicateBoard = closed.get(child);
                    if(duplicateBoard.getF() < child.getF()) {
                        addToQueue = false;
                    }
                }

                if(addToQueue) {
                    queue.offer(child);
                }
            }
            closed.put(current, current);
        }
        return finalBoard;
    }

    public static void main(String[] args) {
        Puzzle p = new Puzzle("312 6b4 785");
        Board solution = p.solvePuzzle();
        solution.printBoard();
        System.out.println();

        Board backtrace = solution;
        while(backtrace.getParent() != null) {
            backtrace.getParent().printBoard();
            System.out.println();
            backtrace = backtrace.getParent();
        }
    }
}
