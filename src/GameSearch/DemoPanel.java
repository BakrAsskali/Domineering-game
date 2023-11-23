package GameSearch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DemoPanel extends JFrame {

    private boolean player1Turn = true;
    final int maxCol = 8;
    final int maxRow = 8;
    final int nodeSize = 80;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;
    Node[][] node = new Node[maxCol][maxRow];
    Node currentNode;

    ArrayList<Node> openList=new ArrayList<>();
    ArrayList<Node> playedList=new ArrayList<>();
    int step = 0;

    public DemoPanel() {
        initializeUI();
    }

    private void initializeUI() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start");
        JButton continueButton = new JButton("Continue");
        JButton quitButton = new JButton("Quit");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameStart();
            }
        });

        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Continue button clicked");
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(startButton);
        buttonPanel.add(continueButton);
        buttonPanel.add(quitButton);

        this.add(buttonPanel);
        this.setVisible(true);
    }

    private void gameStart() {
        this.getContentPane().removeAll();
        JPanel gamePanel = new JPanel();

        gamePanel.setPreferredSize(new Dimension(screenWidth,screenHeight));
        gamePanel.setLayout(new GridLayout(maxCol,maxRow));
        ///place nodes
        int col=0;
        int row=0;
        while (col<maxCol && row<maxRow){
            node[col][row]=new Node(col,row);
            gamePanel.add(node[col][row]);
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
        this.add(gamePanel);
        this.setVisible(true);
        this.pack();
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
                @Override
                public void mouseEntered(MouseEvent e) {
                    highlightNode(currentNode);
                    highlightNode(node[nodeCol+1][nodeRow]);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if(!currentNode.checked) {
                        unhighlightNode(currentNode);
                        unhighlightNode(node[nodeCol + 1][nodeRow]);
                    }else {
                        handleClick(currentNode);
                        handleClick(node[nodeCol+1][nodeRow]);
                    }
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleClick(currentNode);
                    handleClick(node[nodeCol+1][nodeRow]);
                    player1Turn=!player1Turn;
                    currentNode.checked=true;

                }
            });
        }
    }

    private void saveGridToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < maxCol; i++) {
                for (int j = 0; j < maxRow; j++) {
                    Node currentNode = node[i][j];
                    writer.write(currentNode.toString(i, j, player1Turn));
                    writer.newLine();
                }
            }
            System.out.println("Grid saved to " + fileName);
        } catch (IOException ex) {
            ex.printStackTrace();
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

    private void handleNodeClick(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int col = x / nodeSize;
        int row = y / nodeSize;

        System.out.println("col: " + col + ", row: " + row);

        if (col < maxCol && row < maxRow) {
            currentNode = node[col][row];
            if (currentNode.checked) {
                System.out.println("This node has been checked");
            } else {
                if (player1Turn) {
                    currentNode.setAsCheckedPlayer1();
                } else {
                    currentNode.setAsCheckedPlayer2();
                }
                player1Turn = !player1Turn;
                step++;
                System.out.println("step: " + step);
            }
        }
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
