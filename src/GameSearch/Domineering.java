package GameSearch;

import javax.swing.*;
import java.util.Enumeration;
import java.util.Vector;

public class Domineering extends GameSearch{
    @Override
    public boolean drawnPosition(Position p) {
        Boolean ret = true;
        DomineeringPosition dp = (DomineeringPosition) p;
        int [][] board = dp.board;
        for (int i=0; i<8; i++) {
            for(int j=0; j<8; j++){
                if(board[i][j] == DomineeringPosition.BLANK){
                    ret = false;
                    break;
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
        for (int i=0; i<8; i++) {
            for(int j=0; j<8; j++){
                if(board[i][j] == DomineeringPosition.BLANK) {
                    if (player) {
                        if (i + 1 < 8 && board[i + 1][j] == DomineeringPosition.BLANK) {
                            break;
                        }
                    } else {
                        if (j + 1 < 8 && board[i][j + 1] == DomineeringPosition.BLANK) {
                            break;
                        }
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public float positionEvaluation(Position p, boolean player) {
        DomineeringPosition dp = (DomineeringPosition) p;
        int [][] board = dp.board;
        int count = 0;
        for (int i=0; i<8; i++) {
            for(int j=0; j<8; j++){
                if(board[i][j] == DomineeringPosition.BLANK){
                    count++;
                }
            }
        }
        count = 64 - count;
        // prefer to block other player
        float base = 1.0f;
        if (wonPosition(p, !player))  {
            return base + (1.0f / count);
        }
        if (wonPosition(p, player))  {
            return -(base + (1.0f / count));
        }
        return count;
    }

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
        for (int i=0; i<8; i++) {
            for(int j=0; j<8; j++){
                if(board[i][j] == DomineeringPosition.BLANK){
                    if(player){
                        if(i+1 < 8 && board[i+1][j] == DomineeringPosition.BLANK){
                            DomineeringPosition pos2 = new DomineeringPosition();
                            for (int k=0; k<8; k++) {
                                for(int l=0; l<8; l++){
                                    pos2.board[k][l] = board[k][l];
                                }
                            }
                            pos2.board[i][j] = DomineeringPosition.HUMAN;
                            pos2.board[i+1][j] = DomineeringPosition.HUMAN;
                            return new Position[]{pos2};
                        }
                        if (j + 1 < 8 && board[i][j + 1] == DomineeringPosition.BLANK) {
                            DomineeringPosition pos2 = new DomineeringPosition();
                            for (int k = 0; k < 8; k++) {
                                for (int l = 0; l < 8; l++) {
                                    pos2.board[k][l] = board[k][l];
                                }
                            }
                            pos2.board[i][j] = DomineeringPosition.PROGRAM;
                            pos2.board[i][j + 1] = DomineeringPosition.PROGRAM;
                            return new Position[]{pos2};
                        }
                    }else {
                        if (i + 1 < 8 && board[i + 1][j] == DomineeringPosition.BLANK) {
                            DomineeringPosition pos2 = new DomineeringPosition();
                            for (int k = 0; k < 8; k++) {
                                for (int l = 0; l < 8; l++) {
                                    pos2.board[k][l] = board[k][l];
                                }
                            }
                            pos2.board[i][j] = DomineeringPosition.PROGRAM;
                            pos2.board[i + 1][j] = DomineeringPosition.PROGRAM;
                            return new Position[]{pos2};
                        }
                        if (j + 1 < 8 && board[i][j + 1] == DomineeringPosition.BLANK) {
                            DomineeringPosition pos2 = new DomineeringPosition();
                            for (int k = 0; k < 8; k++) {
                                for (int l = 0; l < 8; l++) {
                                    pos2.board[k][l] = board[k][l];
                                }
                            }
                            pos2.board[i][j] = DomineeringPosition.PROGRAM;
                            pos2.board[i][j+1] = DomineeringPosition.PROGRAM;
                            return new Position[]{pos2};
                        }
                    }
                }
            }
        }
        return null;
    }

    public Position makeMove(Position p, boolean player, Move move) {
        DomineeringMove m = (DomineeringMove)move;
        DomineeringPosition pos = (DomineeringPosition)p;
        int [][] board = pos.board;
        if(player){
            board[m.row][m.col] = DomineeringPosition.HUMAN;
            board[m.row2][m.col2] = DomineeringPosition.HUMAN;
        }else{
            board[m.row ][m.col] = DomineeringPosition.PROGRAM;
            board[m.row2][m.col2] = DomineeringPosition.PROGRAM;
        }
        return pos;
    }

    @Override
    public boolean reachedMaxDepth(Position p, int depth) {
        boolean ret = false;
        DomineeringPosition dp = (DomineeringPosition) p;
        int [][] board = dp.board;
        if (depth >= 10) return true;
        if (wonPosition(p, false)) ret = true;
        else if (wonPosition(p, true))  ret = true;
        else if (drawnPosition(p)) ret = true;
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
        if (!humanPlayFirst) {
            Vector v = alphaBeta(0, startingPosition, PROGRAM);
            startingPosition = (Position)v.elementAt(1);
        }
        DemoPanel dp = new DemoPanel();
        dp.pack();
        dp.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
                System.out.println("Closed");
            }
        });
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
            String[] userInput = JOptionPane.showInputDialog("Enter your move").split("");
            Move move = createMove((char) (userInput[0].charAt(0)-'a'), Integer.parseInt(userInput[1]),(char) (userInput[2].charAt(0)-'a'), Integer.parseInt(userInput[3]));
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

    public static void main(String [] args) {
        DomineeringPosition dp = new DomineeringPosition();
        Domineering d = new Domineering();
        d.playGame(dp, true);
    }
}
