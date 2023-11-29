package GameSearch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class DemoPanel extends JFrame {
    public Move move;
    public boolean clicked = false;

    public boolean gameStart = false;
    final int maxCol = 8;
    final int maxRow = 8;
    final int nodeSize = 80;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;
    public boolean hint=false;

    Node[][] node = new Node[maxCol][maxRow];
    private JComboBox<String> levelComboBox;
    private JComboBox<String> playerComboBox;

    public DemoPanel() {
        initializeUI();
    }

    private void initializeUI() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout()); // Use FlowLayout for left-to-right flow


        JButton startButton = new JButton("Start");
        JButton continueButton = new JButton("Continue");
        JButton quitButton = new JButton("Quit");


        startButton.setVerticalAlignment(JLabel.CENTER);
        startButton.setHorizontalAlignment(JLabel.CENTER);




        // Create and populate the level combo box
        levelComboBox = new JComboBox<>(new String[]{"1", "2", "3"});
        levelComboBox.setSelectedIndex(1); // Set the default selected level

        // Create and populate the player combo box
        playerComboBox = new JComboBox<>(new String[]{"P1 vs P2", "P1 vs Computer"});
        playerComboBox.setSelectedIndex(1); // Set the default selected player option

        boolean player = playerComboBox.getSelectedIndex() == 0 ? false : true;
        int level=levelComboBox.getSelectedIndex()+1;

        ImageIcon img= new ImageIcon("img.png");

        // Add components to the button panel
        buttonPanel.add(startButton);
        buttonPanel.add(continueButton);
        buttonPanel.add(quitButton);



        this.setResizable(false);
        this.setIconImage(img.getImage());
        this.getContentPane().setBackground(Color.BLUE);
        this.setTitle("Welcome to Domineering");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameStart(level, player);
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
        // Add combo boxes to the button panel
        buttonPanel.add(new JLabel("Select Level:"));
        buttonPanel.add(levelComboBox);
        buttonPanel.add(new JLabel("Select Players:"));
        buttonPanel.add(playerComboBox);


        this.add(buttonPanel);
        this.setVisible(true);
    }

    private void gameStart(int level,boolean player) {
        this.getContentPane().removeAll();
        JPanel gamePanel = new JPanel();

        gamePanel.setPreferredSize(new Dimension(screenWidth,screenHeight));
        gamePanel.setLayout(new GridLayout(maxCol,maxRow));
        for (int i = 0; i < maxCol; i++) {
            for (int j = 0; j < maxRow; j++) {
                node[i][j] = new Node(i,j);
                gamePanel.add(node[i][j]);
            }
        }
        
        for (int i = 0; i < maxCol; i++) {
            for (int j = 0; j < maxRow; j++) {
                addMouseListenerToNode( node[i][j] );
            }
        }
        this.add(gamePanel);
        JButton button=new JButton("Save The game");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGridToFile("grid");

            }
        });
        JButton hintButton=new JButton("NEED HELP");

        hintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                hint=true;
                clicked=true;
            }
        });

        add(button, BorderLayout.BEFORE_FIRST_LINE);
        add(hintButton, BorderLayout.LINE_START);


        this.setVisible(true);
        this.pack();
        gameStart = true;



    }

//    public void showHint( Node[][] node, boolean player){
//        int[][] board = new int[8][8];
//        for (int i=0; i<8; i++) {
//            for(int j=0; j<8; j++){
//                if(node[i][j].checked){
//                    board[i][j]=DomineeringPosition.HUMAN;
//                }
//                else{
//                    board[i][j]=DomineeringPosition.BLANK;
//                }
//            }
//        }
//
//
//        Vector v = alphaBeta(1, new DomineeringPosition(), player);
//        Move move = (Move) v.elementAt(1);
//        int row = move.row1;
//        int col = move.col1;
//        node[row][col].setBackground(Color.GREEN);
//        node[row][col+1].setBackground(Color.GREEN);
//    }

    private void addMouseListenerToNode(Node currentNode) {
        int nodeCol = currentNode.col;
        int nodeRow = currentNode.row;

            currentNode.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (nodeCol + 1 < maxCol && nodeRow+1< maxRow && !node[nodeCol + 1][nodeRow].checked && !node[nodeCol][nodeRow].checked) {
                        highlightNode(currentNode);
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if (nodeCol + 1 < maxCol && nodeRow+1< maxRow && !node[nodeCol + 1][nodeRow].checked && !node[nodeCol][nodeRow].checked) {
                        unhighlightNode(currentNode);
                    }
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                        handleClick(currentNode);

                }
            });

    }

    private void saveGridToFile(String baseFileName) {
        // Ensure the maximum number of grids to save
        int maxGridsToSave = 4;

        for (int fileIndex = 1; fileIndex <= maxGridsToSave; fileIndex++) {
            String fileName = baseFileName + "_" + fileIndex + ".txt";

            // Check if the file is empty
            if (isFileEmpty(fileName)) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                    // Save the grid to the empty file
                    saveGridContent(writer);
                    System.out.println("Grid saved to " + fileName);
                    return;  // Exit the loop if the grid is saved
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        System.out.println("No empty file found to save the grid.");
    }

    private boolean isFileEmpty(String fileName) {
        File file = new File(fileName);
        return file.length() == 0;
    }

    private void saveGridContent(BufferedWriter writer) throws IOException {
        for (int i = 0; i < maxCol; i++) {
            for (int j = 0; j < maxRow; j++) {
                Node currentNode = node[i][j];
                String nodeString = currentNode.toString(i, j);
                writer.write(nodeString);
                writer.newLine();
            }
        }
    }



    private void highlightNode(Node currentNode) {
        int col = currentNode.row;
        int row = currentNode.col;
            if (col + 1 < maxCol && !node[col + 1][row].checked && !node[col][row].checked) {
                node[row][col].setBackground(Color.ORANGE);
                node[row][col+1].setBackground(Color.ORANGE);
            }
    }

    private void unhighlightNode(Node currentNode) {
        int col = currentNode.col;
        int row = currentNode.row;
            if (col + 1 < maxCol && !node[col + 1][row].checked && !node[col][row].checked) {
                node[col][row].setBackground(Color.WHITE);
                node[col][row+1].setBackground(Color.WHITE);
            }

    }

    private void handleClick(Node clickedNode) {
        int col = clickedNode.row;
        int row = clickedNode.col;

            // Set color for player 1 and the node above
            clickedNode.setAsCheckedPlayer1();
            move = new DomineeringMove((char) (row), col, (char) (row), col + 1);

        clicked = true;
    }

    public void updatePosition(Position p){
        DomineeringPosition dp = (DomineeringPosition) p;
        int [][] board = dp.board;
        for (int i=0; i<8; i++) {
            for(int j=0; j<8; j++){
                if(board[i][j]==DomineeringPosition.HUMAN){
                    node[i][j].setAsCheckedPlayer1();
                }
                if(board[i][j]==DomineeringPosition.PROGRAM){
                    node[i][j].setAsCheckedPlayer2();
                }
                if (board[i][j]==DomineeringPosition.BLANK){
                    node[i][j].setBackground(Color.WHITE);
                }
                if(board[i][j]==DomineeringPosition.Hint){
                    if(hint){
                        node[i][j].setBackground(Color.GREEN);
                    }                }
            }
        }
    }


}
