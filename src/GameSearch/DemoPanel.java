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
    public Move move;
    public boolean clicked = false;

    public boolean gameStart = false;
    private boolean player1Turn = true;
    final int maxCol = 8;
    final int maxRow = 8;
    final int nodeSize = 80;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;
    Node[][] node = new Node[maxCol][maxRow];

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
        gameStart = true;
    }
    private void addMouseListenerToNode(Node currentNode) {
        int nodeCol = currentNode.col;
        int nodeRow = currentNode.row;

        if(player1Turn) {
            currentNode.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (nodeCol + 1 < maxCol && !node[nodeCol + 1][nodeRow].checked) {
                        highlightNode(currentNode);
                        highlightNode(node[nodeCol + 1][nodeRow]);
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    unhighlightNode(currentNode);
                    unhighlightNode(node[nodeCol+1][nodeRow]);
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleClick(currentNode);
                    handleClick(node[nodeCol+1][nodeRow]);
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
        // Check if the node to the right exists or not

        if(player1Turn){
            if (col + 1 < maxCol && !node[col + 1][row].checked) {
                node[col + 1][row].setBackground(Color.ORANGE);
            }
        }else{
            if (row + 1 < maxRow && !node[col][row + 1].checked) {
                node[col][row + 1].setBackground(Color.ORANGE);
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
            move = new DomineeringMove((char) (row + 'a'), col, (char) (row + 'a'), col + 1);
            clicked = true;
        } else {
            // Set color for player 2 and the node to the right
            clickedNode.setAsCheckedPlayer2();
        }

        // Switch player turn
    }
}
