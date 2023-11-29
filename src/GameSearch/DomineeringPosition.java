package GameSearch;

public class DomineeringPosition extends Position{
    final static public int BLANK = 0;
    final static public int HUMAN = 1;
    final static public int PROGRAM = -1;
    final static public int Hint = 2;

    int col=8;
    int row=8;


    public int [][] board = new int[col][row];

    DomineeringPosition(int col, int row){
        this.col= col;
        this.row = row;

        for (int i=0; i<col; i++) {
            for(int j=0; j<row; j++){
                board[i][j] = BLANK;
            }
        }
        board=new int[this.col][this.row];
    }
    DomineeringPosition(int [][] board){
        this.col= col;
        this.row = row;
        this.board = board;
    }
    DomineeringPosition(){
        for (int i=0; i<col; i++) {
            for(int j=0; j<row; j++){
                board[i][j] = BLANK;
            }
        }
        board=new int[this.col][this.row];
    }
    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        for (int i=0; i<8; i++) {
            for(int j=0; j<8; j++){
                sb.append(""+board[i][j]+",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
