package GameSearch;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Node extends JButton implements ActionListener {
    Node upper;
    int col;
    int row;

    boolean checked;
    public Node(int col,int row){
        this.col=col;
        this.row=row;
        setBackground(Color.white);
        setForeground(Color.BLACK);
        addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        setBackground(Color.orange);
    }
    public void setAsCheckedPlayer2(){
        setBackground(Color.yellow);
        setForeground(Color.BLACK);
        checked=true;
    }
    public void setAsCheckedPlayer1(){
        setBackground(Color.black);
        setForeground(Color.BLACK);
        checked=true;
    }

    public String toString(int col,int row, boolean player1Turn){
        String s="";
        s=s+col;
        s=s+row;
        if(player1Turn){
            s=s+"O";
        }
        else{
            s=s+"X";
        }
        return s;
    }
}