package GameSearch;

import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

public class Domineering extends GameSearch{


    @Override
    public boolean drawnPosition(Position p) {
        Boolean ret = false;
        DomineeringPosition dp = (DomineeringPosition) p;
        int [][] board = dp.board;
        if(wonPosition(p, true) && wonPosition(p, false)) return false;
        for (int i=0; i<8; i++) {
            for(int j=0; j<8; j++){
                if(board[i][j] == DomineeringPosition.BLANK){
                    if (i + 1 < 8 && board[i + 1][j] == DomineeringPosition.BLANK) {
                        return false;
                    }
                    if (j + 1 < 8 && board[i][j + 1] == DomineeringPosition.BLANK) {
                        return false;
                    }
                }
            }
        }
        return ret;
    }







    @Override
    public boolean wonPosition(Position p, boolean player) {
        boolean ret = false;
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
            if (place2 > 0) return false;
            return true;
        } else {
            if (place1 > 0) return false;
            return true;

        }
    }


    @Override
    public float positionEvaluation(Position p, boolean player) {
        DomineeringPosition dp = (DomineeringPosition) p;
        int[][] board = dp.board;
        float base = 1.0f;
        int countPlayer1 = 0;
        int countPlayer2=0;
        float result;


        countPlayer1=numberOfHorizontalPos(board);
        countPlayer2=numberOfVerticalPos(board);

        result=countPlayer1-countPlayer2;
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
        if (depth >= 1) return true;
        return ret;
    }

    @Override
    public Move createMove() {
        return null;
    }


    @Override
    public Move createMove(char row, int col, char row2, int col2) {
        DomineeringMove dm = new DomineeringMove();
        dm.row = row;
        dm.col = col;
        dm.row2 = row2;
        dm.col2 = col2;
        return dm;
    }


    @Override
    public void playGame(Position startingPosition, boolean humanPlayFirst){
        DemoPanel dp = new DemoPanel();
        dp.pack();
        if (!humanPlayFirst) {
            Vector v = alphaBeta(0, startingPosition, PROGRAM);
            startingPosition = (Position)v.elementAt(1);
        }

        while(!dp.gameStart){
            try {
                Thread.sleep(100); // Adjust the sleep time as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

            dp.updatePosition(startingPosition);
            while(!dp.clicked){
                try {
                    Thread.sleep(100); // Adjust the sleep time as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            dp.clicked = false;
            Move move = dp.move;

            startingPosition = makeMove(startingPosition, HUMAN, move);
            dp.updatePosition(startingPosition);
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


    public Position getHintPos(Position position, boolean player){

        if(player){
            Vector v = alphaBeta(0, position, HUMAN);
            position = (Position)v.elementAt(1);


            position = (Position)v.elementAt(1);
        }else{

            Vector v = alphaBeta(0, position, PROGRAM);
            Position p = (Position)v.elementAt(1);
            position = (Position)v.elementAt(1);

        }

        //return the best move
        return  position;
    }

    private DomineeringMove showHintMove(Position p, boolean player){
        Color blankColor=Color.WHITE;
        Color hintColor=Color.GREEN;

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

//        for (int i=0; i<8; i++) {
//            dp.board[i][0]=-1;
//        }
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
