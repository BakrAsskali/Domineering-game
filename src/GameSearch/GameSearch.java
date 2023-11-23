package GameSearch;

import java.util.Enumeration;
import java.util.Vector;

public abstract class GameSearch {

    public static final boolean DEBUG = false;

    /*
     * Note: the abstract Position also needs to be
     *       subclassed to write a new game program.
     */
    /*
     * Note: the abstract class Move also needs to be subclassed.
     *       
     */

    public static boolean PROGRAM = false;
    public static boolean HUMAN = true;

    /**
     *  Notes:  PROGRAM false -1,  HUMAN true 1
     */

    /*
     * Abstract methods:
     */

    public abstract boolean drawnPosition(Position p);
    public abstract boolean wonPosition(Position p, boolean player);
    public abstract float positionEvaluation(Position p, boolean player);
    public abstract void printPosition(Position p);
    public abstract Position [] possibleMoves(Position p, boolean player);
    public abstract Position makeMove(Position p, boolean player, Move move);
    public abstract boolean reachedMaxDepth(Position p, int depth);
    public abstract Move createMove();

    /*
     * Search utility methods:
     */

    protected Vector alphaBeta(int depth, Position p, boolean player) {
        Vector v = maxValue(depth, p, player, 1000000.0f, -1000000.0f);
        //System.out.println("^^ v(0): " + v.elementAt(0) + ", v(1): " + v.elementAt(1));
        return v;
    }

    protected Vector maxValue(int depth, Position p, boolean player, float alpha, float beta){
        if(reachedMaxDepth(p, depth)){
            Vector v = new Vector();
            v.addElement(new Float(positionEvaluation(p, player)));
            v.addElement(p);
            return v;
        }
        Vector v = new Vector();
        v.addElement(new Float(-1000000.0f));
        v.addElement(p);
        Position [] moves = possibleMoves(p, player);
        for (int i=0; i<moves.length; i++) {
            Vector v2 = minValue(depth+1, moves[i], !player, alpha, beta);
            Float eval = (Float)v.elementAt(0);
            Float eval2 = (Float)v2.elementAt(0);
            if (eval2.floatValue() > eval.floatValue()) {
                v.setElementAt(eval2, 0);
                v.setElementAt(moves[i], 1);
            }
            if (eval2.floatValue() >= beta) {
                return v;
            }
            if (eval2.floatValue() > alpha) {
                alpha = eval2.floatValue();
            }
        }
        return v;
    }

        protected Vector minValue(int depth, Position p, boolean player, float alpha, float beta){
        if(reachedMaxDepth(p, depth)){
            Vector v = new Vector();
            v.addElement(new Float(positionEvaluation(p, player)));
            v.addElement(p);
            return v;
        }
        Vector v = new Vector();
        v.addElement(new Float(1000000.0f));
        v.addElement(p);
        Position [] moves = possibleMoves(p, player);
        for (int i=0; i<moves.length; i++) {
            Vector v2 = maxValue(depth+1, moves[i], !player, alpha, beta);
            Float eval = (Float)v.elementAt(0);
            Float eval2 = (Float)v2.elementAt(0);
            if (eval2.floatValue() < eval.floatValue()) {
                v.setElementAt(eval2, 0);
                v.setElementAt(moves[i], 1);
            }
            if (eval2.floatValue() <= alpha) {
                return v;
            }
            if (eval2.floatValue() < beta) {
                beta = eval2.floatValue();
            }
        }
        return v;
    }

    public void playGame(Position startingPosition, boolean humanPlayFirst) {
        if (!humanPlayFirst) {
            Vector v = alphaBeta(0, startingPosition, PROGRAM);
            startingPosition = (Position)v.elementAt(1);
        }
        while (true) {
            
            printPosition(startingPosition);
            if (wonPosition(startingPosition, PROGRAM)) {
                System.out.println("Program won");
                break;
            }
            if (wonPosition(startingPosition, HUMAN)) {
                System.out.println("Human won");
                break;
            }
            if (drawnPosition(startingPosition)) {
                System.out.println("Drawn game");
                break;
            }
            System.out.print("\nYour move:");
            Move move = createMove();
            startingPosition = makeMove(startingPosition, HUMAN, move);

            printPosition(startingPosition);

             if (wonPosition(startingPosition, HUMAN)) {
                System.out.println("Human won");
                break;
            }
             
            Vector v = alphaBeta(0, startingPosition, PROGRAM);
            
            Enumeration enum2 = v.elements();
            while (enum2.hasMoreElements()) {
                System.out.println(" next element: " + enum2.nextElement());
            }
                     
            startingPosition = (Position)v.elementAt(1);  
           if(startingPosition ==null){
               System.out.println("Drawn game");
               break;
           }
        }
    }

    public abstract Move createMove(char row, int col, char row2, int col2);
}
