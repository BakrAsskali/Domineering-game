package GameSearch;

public class Domineering extends GameSearch{
    @Override
    public boolean drawnPosition(Position p) {
        return false;
    }

    @Override
    public boolean wonPosition(Position p, boolean player) {
        return false;
    }

    @Override
    public float positionEvaluation(Position p, boolean player) {
        DomineeringPosition dp = (DomineeringPosition) p;
        int [][] board = dp.board;
        int score = 0;
        for (int i=0; i<8; i++) {
            for(int j=0; j<8; j++){
                if(board[i][j] == DomineeringPosition.PROGRAM){
                    score++;
                }else if(board[i][j] == DomineeringPosition.HUMAN){
                    score--;
                }
            }
        }
        return score;
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
        return new Position[0];
    }

    public Position makeMove(Position p, boolean player, Move move) {
        return null;
    }

    @Override
    public boolean reachedMaxDepth(Position p, int depth) {
        return false;
    }

    @Override
    public Move createMove() {
        return null;
    }
}
