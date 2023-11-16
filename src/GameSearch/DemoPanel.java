package GameSearch;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class DemoPanel extends JPanel {
    private boolean player1Turn = true; //  player 1 starts

    final int maxCol=10;
    final int maxRow=10;
    final int nodeSize=80;
    final int screenWidth=nodeSize*maxCol;
    final int screenHeight=nodeSize*maxRow;
    Node[][] node=new Node[maxCol][maxRow];
    Node currentNode;
    ArrayList<Node> openList=new ArrayList<>();
    ArrayList<Node> playedList=new ArrayList<>();
    int step=0;
    public DemoPanel() {
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.GRAY);
        this.setLayout(new GridLayout(maxRow,maxCol));
        this.setFocusable(true);
        ///place nodes
        int col=0;
        int row=0;
        while (col<maxCol && row<maxRow){
            node[col][row]=new Node(col,row);
            this.add(node[col][row]);
            col++;
            if(col==maxCol){
                col=0;
                row++;
            }
        }
        for (int i = 0; i < maxCol; i++) {
            for (int j = 0; j < maxRow; j++) {
                addMouseListenerToNode( node[i][j] );
            }
        }
    }
    private void addMouseListenerToNode(Node currentNode) {
        int nodeCol = currentNode.col;
        int nodeRow = currentNode.row;

        if(player1Turn) {
            currentNode.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    highlightNode(currentNode);
                    highlightNode(node[nodeCol][nodeRow - 1]);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    unhighlightNode(currentNode);
                    unhighlightNode(node[nodeCol][nodeRow-1]);
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleClick(currentNode);
                    handleClick(node[nodeCol][nodeRow - 1]);
                    player1Turn=!player1Turn;
                }
            });
                    }else {
            currentNode.addMouseListener(new MouseAdapter() {
                boolean isClicked=false;
                @Override
                public void mouseEntered(MouseEvent e) {
                    highlightNode(currentNode);
                    highlightNode(node[nodeCol+1][nodeRow]);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if(!isClicked) {
                        unhighlightNode(currentNode);
                        unhighlightNode(node[nodeCol + 1][nodeRow]);
                    }
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleClick(currentNode);
                    handleClick(node[nodeCol+1][nodeRow]);
                    player1Turn=!player1Turn;
                    isClicked=true;
                }
            });
        }

    }

    private void highlightNode(Node currentNode) {
        int col = currentNode.col;
        int row = currentNode.row;
        // Check if the node above exists and is not checked

       if(player1Turn){
            if (row > 0 && !playedList.contains(node[col][row - 1])) {
                currentNode.setBackground(Color.LIGHT_GRAY);
            }
        }else {
           if (row > 0 && !playedList.contains(node[col+1][row ])) {
               currentNode.setBackground(Color.LIGHT_GRAY);
           }
       }

    }
    private void unhighlightNode(Node currentNode) {

        currentNode.setBackground(Color.WHITE);
    }
    private void handleClick(Node clickedNode) {
        int col = clickedNode.col;
        int row = clickedNode.row;

        if (player1Turn) {
            // Set color for player 1 and the node above

                 clickedNode.setAsCheckedPlayer1();
        } else {
            // Set color for player 2 and the node to the right
               clickedNode.setAsCheckedPlayer2();
         }

        // Switch player turn
    }
}