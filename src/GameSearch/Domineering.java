package GameSearch;

import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

public class Domineering extends GameSearch{

    @Override
    public boolean wonPosition(Position p, boolean player) {
        DomineeringPosition dp = (DomineeringPosition) p;
        int [][] board = dp.board;
        int place1=0;
        int place2=0;
        for (int i=0; i<8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == DomineeringPosition.BLANK) {
                    if (player) {
                        if (i + 1 < 8 && board[i + 1][j] == DomineeringPosition.BLANK) {
                            place2++;
                            break;
                        }

                    } else {
                        if (j + 1 < 8 && board[i][j + 1] == DomineeringPosition.BLANK) {
                            place1++;
                            break;
                        }
                    }
                }

            }

        }
        if (player) {
            return place2 <= 0;
        } else {
            return place1 <= 0;

        }
    }

    public int degre(Position p) {
        DomineeringPosition dp = (DomineeringPosition) p;
        int[][] board = dp.board;
        int horizontalMoves = numberOfHorizontalPos(board);
        int verticalMoves = numberOfVerticalPos(board);
        return horizontalMoves + verticalMoves;
       }
    @Override
    public float positionEvaluation(Position p, boolean player) {
        DomineeringPosition dp = (DomineeringPosition) p;
        int[][] board = dp.board;
        int countPlayer1;
        int countPlayer2;
        float result;


        countPlayer1=numberOfHorizontalPos(board);
        countPlayer2=numberOfVerticalPos(board);
        float degreValue = degre(p);
        result=countPlayer1-countPlayer2;
        result += degreValue;

        if(player){
            return result;
        }else {
            return -result;
        }
    }
    public int numberOfVerticalPos(int[][] board){
        int ret = 0;
        for (int i=0; i<8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == DomineeringPosition.BLANK) {
                    if (i + 1 < 8 && board[i+1][j] == DomineeringPosition.BLANK) {
                        ret++;
                    }
                }
            }
        }
        return ret;
    }
    public int numberOfHorizontalPos(int[][] board){
        int ret = 0;
        for (int i=0; i<8; i++) {
            for(int j=0; j<8; j++){
                if(board[i][j] == DomineeringPosition.BLANK) {
                    if (j+1 < 8 && board[i][j+1] == DomineeringPosition.BLANK) {
                        ret++;
                    }
                }
            }
        }
        return ret;
    }

    //useless
    @Override
    public void printPosition(Position p) {
        DomineeringPosition dp = (DomineeringPosition) p;
        int [][] board = dp.board;
        for (int i=0; i<8; i++) {
            for(int j=0; j<8; j++){
                if(board[i][j] == DomineeringPosition.PROGRAM){
                    System.out.print("X");
                }else if(board[i][j] == DomineeringPosition.HUMAN){
                    System.out.print("O");
                }else{
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    @Override
    public Position[] possibleMoves(Position p, boolean player) {
        DomineeringPosition dp = (DomineeringPosition) p;
        int [][] board = dp.board;
        ArrayList<Position> ret = new ArrayList<>();
        Position[] ret2 ;
        for (int i=0; i<8; i++) {
            for(int j=0; j<8; j++){
                if(board[i][j] == DomineeringPosition.BLANK){
                    if(player) {
                            if (j + 1 < 8 && board[i][j + 1] == DomineeringPosition.BLANK) {
                                DomineeringPosition pos2 = new DomineeringPosition();
                                for(int k = 0; k < 8; k++) System.arraycopy(board[k], 0, pos2.board[k], 0, 8);
                                pos2.board[i][j] = DomineeringPosition.HUMAN;
                                pos2.board[i][j+1] = DomineeringPosition.HUMAN;
                                ret.add(pos2);
                            }
                        }else {
                        if (i + 1 < 8 && board[i + 1][j] == DomineeringPosition.BLANK) {
                            DomineeringPosition pos2 = new DomineeringPosition();
                            for(int k = 0; k < 8; k++) System.arraycopy(board[k], 0, pos2.board[k], 0, 8);
                            pos2.board[i][j] = DomineeringPosition.PROGRAM;
                            pos2.board[i+1][j] = DomineeringPosition.PROGRAM;
                            ret.add(pos2);

                        }
                    }
                    }
                }
            }

        ret2 = new Position[ret.size()];
        ret.toArray(ret2);
        return ret2;
    }

    public Position makeMove(Position p, boolean player, Move move) {
        DomineeringMove m = (DomineeringMove)move;
        DomineeringPosition pos = (DomineeringPosition)p;
        int [][] board = pos.board;


        if(player){
            board[m.row][m.col] = DomineeringPosition.HUMAN;
            board[m.row2][m.col2] = DomineeringPosition.HUMAN;
        }else{
            board[m.row][m.col] = DomineeringPosition.PROGRAM;
            board[m.row2][m.col2] = DomineeringPosition.PROGRAM;
        }
        return pos;
    }

    @Override
    public boolean reachedMaxDepth(Position p, int depth) {
        boolean ret = false;
        if (depth >= 15) return true;
        return ret;
    }

    @Override
    public Move createMove() {
        return null;
    }
    public Vector maxValue(int depth, Position p, boolean player, float alpha, float beta) {
        Vector v = new Vector(2);
        if (wonPosition(p, PROGRAM)) {
            v.addElement(1000000.0f);
            v.addElement(null);
            return v;
        }
        if (wonPosition(p, HUMAN)) {
            v.addElement(-1000000.0f);
            v.addElement(null);
            return v;
        }
        if (reachedMaxDepth(p, depth)) {
            float value = positionEvaluation(p, player);
            v.addElement(value);
            v.addElement(null);
            return v;
        }
        float bestValue = -1000000.0f;
        Position [] moves = possibleMoves(p, player);
        Position bestPosition = null;
        for (int i=0; i<moves.length; i++) {
            Vector v2 = minValue(depth+1, moves[i], !player, alpha, beta);
            float value = (Float) v2.elementAt(0);
            if (value > bestValue) {
                bestValue = value;
                bestPosition = moves[i];
            }
            if (value > alpha) alpha = value;
            if (alpha >= beta) break;
        }
        v.addElement(bestValue);
        v.addElement(bestPosition);
        return v;
    }

    public Vector minValue(int depth, Position p, boolean player, float alpha, float beta) {
        Vector v = new Vector(2);
        if (wonPosition(p, PROGRAM)) {
            v.addElement(1000000.0f);
            v.addElement(null);
            return v;
        }
        if (wonPosition(p, HUMAN)) {
            v.addElement(new Float(-1000000.0f));
            v.addElement(null);
            return v;
        }
        if (reachedMaxDepth(p, depth)) {
            float value = positionEvaluation(p, player);
            v.addElement(value);
            v.addElement(null);
            return v;
        }
        float bestValue = 1000000.0f;
        Position [] moves = possibleMoves(p, player);
        Position bestPosition = null;
        for (int i=0; i<moves.length; i++) {
            Vector v2 = maxValue(depth+1, moves[i], !player, alpha, beta);
            float value = (Float) v2.elementAt(0);
            if (value < bestValue) {
                bestValue = value;
                bestPosition = moves[i];
            }
            if (value < beta) beta = value;
            if (alpha >= beta) break;
        }
        v.addElement(bestValue);
        v.addElement(bestPosition);
        return v;
    }

    @Override
    public void playGame(Position startingPosition, boolean humanPlayFirst){
        DemoPanel dp = new DemoPanel();
        dp.pack();
        if(dp.loadedgame){
            startingPosition = dp.position;
            dp.updatePosition(startingPosition);
            dp.loadedgame=false;
        }
        if (!humanPlayFirst) {
            Vector v = alphaBeta(1, startingPosition, PROGRAM);
            startingPosition = (Position)v.elementAt(1);
        }

        while(!dp.gameStart){
            try {
                Thread.sleep(100); // Adjust the sleep time as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!dp.twoPlayer) {
            while (true) {
                dp.hint=false;
                dp.updatePosition(startingPosition);
                printPosition(startingPosition);
                if (wonPosition(startingPosition, PROGRAM)) {
                    System.out.println("Program won");
                    break;
                }
                if (wonPosition(startingPosition, HUMAN)) {
                    System.out.println("Human won");
                    break;
                }
                while (!dp.clicked) {
                    try {
                        Thread.sleep(100); // Adjust the sleep time as needed
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                dp.clicked = false;
                Move move = dp.move;
                boolean hint = dp.hint;

                if (hint){
                    DomineeringMove dm = showHintMove(startingPosition, true);
                    makeHighlightedMove(dm,startingPosition);
                    dp.updatePosition(startingPosition);
                    dp.hint = false;

                    continue;
                }

                startingPosition = makeMove(startingPosition, HUMAN, move);
                dp.updatePosition(startingPosition);
                printPosition(startingPosition);
                if (wonPosition(startingPosition, HUMAN)) {

                    System.out.println("Human won");

                    break;
                }
                Vector v;
                if (dp.level==2){
                    v = alphaBeta(10, startingPosition, PROGRAM);
                }
                else {
                    v = maxValue(10, startingPosition, PROGRAM, -1000000.0f, 1000000.0f);
                }
                Enumeration enum2 = v.elements();
                while (enum2.hasMoreElements()) {
                    System.out.println(" next element: " + enum2.nextElement());
                }
                startingPosition = (Position) v.elementAt(1);
            }
        } else {
            while (true) {
                dp.hint=false;
                dp.updatePosition(startingPosition);
                printPosition(startingPosition);
                if (wonPosition(startingPosition, PROGRAM)) {
                    System.out.println("Program won");
                    break;
                }
                if (wonPosition(startingPosition, HUMAN)) {
                    System.out.println("Human won");
                    break;
                }

                while (!dp.clicked && !dp.hint) {
                    try {
                        Thread.sleep(100); // Adjust the sleep time as needed
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                dp.clicked = false;
                Move move = dp.move;
                boolean hint = dp.hint;

                if (hint){
                    DomineeringMove dm = showHintMove(startingPosition, true);
                    makeHighlightedMove(dm,startingPosition);
                    dp.updatePosition(startingPosition);
                    dp.hint = false;


                    continue;
                }

                startingPosition = makeMove(startingPosition, HUMAN, move);
                dp.updatePosition(startingPosition);
                printPosition(startingPosition);
                if (wonPosition(startingPosition, HUMAN)) {
                    System.out.println("player 1 won");
                    break;
                }
                dp.updatePosition(startingPosition);
                while (!dp.clicked) {
                    try {
                        Thread.sleep(100); // Adjust the sleep time as needed
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                dp.clicked = false;
                move = dp.move;

                startingPosition = makeMovePlayer2(startingPosition, PROGRAM, move);
                dp.updatePosition(startingPosition);
                printPosition(startingPosition);
                if (wonPosition(startingPosition, PROGRAM)) {
                    System.out.println("player 2 won");
                    break;
                }
            }
      }
    }

    private void makeHighlightedMove(DomineeringMove m, Position p){
        DomineeringPosition pos = (DomineeringPosition)p;

        pos.board[m.row][m.col] = DomineeringPosition.Hint;
        pos.board[m.row2][m.col2] = DomineeringPosition.Hint;

    }

    private Position makeMovePlayer2(Position startingPosition, boolean player, Move move) {
        DomineeringMove m = (DomineeringMove)move;
        DomineeringPosition pos = (DomineeringPosition)startingPosition;
        int [][] board = pos.board;

        if(player){
            board[m.row][m.col] = DomineeringPosition.HUMAN;
            board[m.row2][m.col2] = DomineeringPosition.HUMAN;
        }else{
            board[m.row][m.col] = DomineeringPosition.PROGRAM;
            board[m.row2][m.col2] = DomineeringPosition.PROGRAM;
        }
        return pos;
    }

    public Position getHintPos(Position position, boolean player){
        if(player){
            Vector v = maxValue(5, position, HUMAN, 1000000.0f, -1000000.0f);
            position = (Position)v.elementAt(1);
        }else{

            Vector v = minValue(10, position, PROGRAM, 1000000.0f, -1000000.0f);
            position = (Position)v.elementAt(1);

        }

        //return the best move
        return  position;
    }

    private DomineeringMove showHintMove(Position p, boolean player){

        DomineeringPosition dp = (DomineeringPosition) p;
        DomineeringPosition hintPos =(DomineeringPosition) getHintPos(p, player);

        int [][] board1 = dp.board;
        int [][] board2 = hintPos.board;
        DomineeringMove dm=new DomineeringMove(-1,-1,-1,-1);
        //find the difference between two boards


        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board1[row][col] != board2[row][col]) {
                    if(dm.col==-1 && dm.row==-1){
                        dm.col = col;
                        dm.row = row;
                    }else {
                        dm.col2 = col;
                        dm.row2 = row;
                    }

                }
            }
        }


        //highlight the hint
        return dm;

    }



    public static void main(String [] args) {
//
        DomineeringPosition dp = new DomineeringPosition();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(dp.board[i][j] + "\t");
            }
            System.out.println(); // Move to the next line after each row
        }



        Domineering d = new Domineering();

        d.playGame(dp, true);
    }
}
