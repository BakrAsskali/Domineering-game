package GameSearch;

import javax.swing.*;
import java.util.Scanner;

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
                            pos2.board[i][j] = DomineeringPosition.PROGRAM;
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
            board[m.row][m.col] = DomineeringPosition.PROGRAM;
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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter move like a2a3 or a2b2 depending on orientation ou entrez 1 pour une aide: ");
        String move = scanner.nextLine();
        DomineeringMove dm = new DomineeringMove();
        dm.row = (char) (move.charAt(0) - 'a');
        dm.col = Integer.parseInt(move.substring(1, 2));
        dm.row2 = (char) (move.charAt(2) - 'a');
        dm.col2 = Integer.parseInt(move.substring(3, 4));
        return dm;
    }

    public static void main(String [] args) {
        DomineeringPosition dp = new DomineeringPosition();
        Domineering d = new Domineering();

        DemoPanel panel = new DemoPanel();
        panel.pack();

        d.playGame(dp, true);
    }
}
