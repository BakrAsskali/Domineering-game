package GameSearch;

public class DomineeringPosition extends Position{
    final static public int BLANK = 0;
    final static public int HUMAN = 1;
    final static public int PROGRAM = -1;

    int [][] board = new int[8][8];

    public DomineeringPosition() {
        for (int i=0; i<8; i++) {
            for(int j=0; j<8; j++){
                board[i][j] = BLANK;
            }
        }
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
