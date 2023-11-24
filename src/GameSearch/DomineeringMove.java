package GameSearch;

public class DomineeringMove extends Move{
    public char row;
    public int col;
    public char row2;
    public int col2;

    @Override
    public String toString() {
        return ""+row+col+row2+col2;
    }

    public DomineeringMove(char row, int col, char row2, int col2){
        this.row = row;
        this.col = col;
        this.row2 = row2;
        this.col2 = col2;
    }

    public DomineeringMove(){
        this.row = 0;
        this.col = 0;
        this.row2 = 0;
        this.col2 = 0;
    }
}
