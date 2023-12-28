package GameSearch;

import org.w3c.dom.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Enumeration;
import java.util.Vector;
import java.util.*;

public class DemoPanel extends JFrame {
    public Move move;
    public boolean clicked = false;

    public boolean gameStart = false;
    final int maxCol = 8;
    final int maxRow = 8;
    final int nodeSize = 80;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;

    Node[][] node = new Node[maxCol][maxRow];
    private JComboBox<String> levelComboBox;
    private JComboBox<String> playerComboBox;
    private JComboBox<String> loadGameC;

    public boolean twoPlayer = false;

    public boolean player1turn = true;
    public DomineeringPosition position;
    public boolean loadedgame = false;
    public boolean hint = false;
    public int level;
    public int LoadIndex;
    public boolean isGameFinished=false;

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
        levelComboBox = new JComboBox<>(new String[]{"1", "2"});
        levelComboBox.setSelectedIndex(1); // Set the default selected level

        loadGameC = new JComboBox<>(new String[]{"None","Game 1", "Game 2","Game 3","Game 4"});
        loadGameC.setSelectedIndex(0); // Set the default selected level

        // Create and populate the player combo box
        playerComboBox = new JComboBox<>(new String[]{"P1 vs P2", "P1 vs Computer"});
        playerComboBox.setSelectedIndex(1); // Set the default selected player option

        boolean player = playerComboBox.getSelectedIndex() == 0 ? false : true;

        ImageIcon img= new ImageIcon("img.png");

        // Add components to the button panel
        buttonPanel.setLayout(new FlowLayout());
        // Create and set layout manager
        JPanel mainPanel = new JPanel(new GridLayout(5, 1));
        JPanel beforeZeroRowPanel = new JPanel(new FlowLayout());
        JPanel zeroRowPanel = new JPanel(new FlowLayout());
        JPanel firstRowPanel = new JPanel(new FlowLayout());
        JPanel secondRowPanel = new JPanel(new FlowLayout());
        JPanel thirdRowPanel = new JPanel(new FlowLayout());


        // Use FlowLayout for left-to-right flow
        mainPanel.setBackground(Color.DARK_GRAY);

        zeroRowPanel.add(new JLabel("Select Level:"));
        zeroRowPanel.add(levelComboBox);
        zeroRowPanel.add(new JLabel("Select Game:"));
        zeroRowPanel.add(loadGameC);

        beforeZeroRowPanel.add(new JLabel("Welcome To Domineering Game:"));



        // Add combo boxes to the button panel

        firstRowPanel.add(new JLabel("Select Players:"));
        firstRowPanel.add(playerComboBox);

        secondRowPanel.add(startButton);
        secondRowPanel.add(continueButton);

        thirdRowPanel.add(quitButton);

        mainPanel.add(beforeZeroRowPanel);
        mainPanel.add(zeroRowPanel);
        mainPanel.add(firstRowPanel);
        mainPanel.add(secondRowPanel);
        mainPanel.add(thirdRowPanel);



        this.add(mainPanel);
        this.setVisible(true);



        this.setResizable(false);
        this.setIconImage(img.getImage());
        this.getContentPane().setBackground(Color.BLUE);
        this.setTitle("Welcome to Domineering");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                level=levelComboBox.getSelectedIndex()+1;
                gameStart(level, player);
            }
        });

        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoadIndex=loadGameC.getSelectedIndex();
                loadGameFromFile(LoadIndex);
            }
        });

        quitButton.addActionListener(e -> System.exit(0));


    }
    private void gameStart(int level, boolean player) {
        this.getContentPane().removeAll();

        JPanel gamePanel = new JPanel();
        twoPlayer = playerComboBox.getSelectedIndex() == 0;
        gamePanel.setPreferredSize(new Dimension(screenWidth, screenHeight));
        gamePanel.setLayout(new BorderLayout());

        // Create a panel for the node grid
        JPanel nodeGridPanel = new JPanel(new GridLayout(maxCol, maxRow));
        for (int i = 0; i < maxCol; i++) {
            for (int j = 0; j < maxRow; j++) {
                node[i][j] = new Node(i, j);
                nodeGridPanel.add(node[i][j]);
                addMouseListenerToNode(node[i][j]);
            }
        }

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              saveGridToFile(); // Replace with your desired file name
            }
        });

        // Create a panel for the save button
        JPanel saveButtonPanel = new JPanel();
        saveButtonPanel.add(saveButton);

        // Add the node grid panel to the center and save button panel to the bottom of gamePanel
        gamePanel.add(nodeGridPanel, BorderLayout.CENTER);
        gamePanel.add(saveButtonPanel, BorderLayout.BEFORE_FIRST_LINE);

        this.add(gamePanel);
        JButton hintButton=new JButton("NEED HELP");

        hintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hint=true;
                clicked=true;
                player1turn=false;
            }
        });

        add(hintButton, BorderLayout.LINE_START);


        this.setVisible(true);
        this.pack();
        gameStart = true;



    }
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

    private void saveGridToFile() {
        // Ensure the maximum number of grids to save
        int maxGridsToSave = 4;

        String baseFileName="new";

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

    private void loadGameFromFile(int index) {
        try(BufferedReader reader=new BufferedReader(new java.io.FileReader("new_"+index+".txt"))){
            String line;
            while((line=reader.readLine())!=null){
                String[] parts=line.split("");
                int player;

                if (parts[2].equalsIgnoreCase("-")) {
                     player=-1;
                } else{
                        player = Integer.parseInt(parts[2]);

                }
                int col=Integer.parseInt(parts[1]);
                int row=Integer.parseInt(parts[0]);
                System.out.println(player+" "+row+" "+col);
                if(player==1){
                    node[row][col] = new Node(row, col);
                    node[row][col].setAsCheckedPlayer1();
                }else if(player==-1){
                    node[row][col] = new Node(row, col);
                    node[row][col].setAsCheckedPlayer2();
                }
                position = new DomineeringPosition();
                position.board[row][col]=player;
                loadedgame=true;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void highlightNode(Node currentNode) {
        int col = currentNode.row;
        int row = currentNode.col;
        if(!twoPlayer) {
            if (col + 1 < maxCol && !node[col + 1][row].checked && !node[col][row].checked && !node[col][row + 1].checked) {
                node[row][col].setBackground(Color.ORANGE);
                node[row][col + 1].setBackground(Color.ORANGE);
            }
        } else{
            if(player1turn){
                if (col + 1 < maxCol && !node[col + 1][row].checked && !node[col][row].checked) {
                    node[row][col].setBackground(Color.ORANGE);
                    node[row][col + 1].setBackground(Color.ORANGE);
                }
            }else{
                if (row+1 < maxRow && !node[col][row + 1].checked && !node[col][row].checked) {
                    node[row][col].setBackground(Color.ORANGE);
                    node[row+1][col].setBackground(Color.ORANGE);
                }
            }
        }
    Position p = new DomineeringPosition();

        DomineeringPosition dp = (DomineeringPosition) p;
        dp.board= position.board;
        updatePosition(p);
    }

    private void unhighlightNode(Node currentNode) {
        int col = currentNode.col;
        int row = currentNode.row;
        if(player1turn){
            if (row+1 < maxRow && !node[col][row + 1].checked && !node[col][row].checked) {
                node[col][row].setBackground(Color.WHITE);
                node[col][row+1].setBackground(Color.WHITE);
            }
        }else{
            if (col + 1 < maxCol && !node[col + 1][row].checked && !node[col][row].checked) {
                node[col][row].setBackground(Color.WHITE);
                node[col+1][row].setBackground(Color.WHITE);
            }
        }
    }

    private void handleClick(Node clickedNode) {
        int col = clickedNode.row;
        int row = clickedNode.col;
        if(twoPlayer){
            if(player1turn){
                clickedNode.setAsCheckedPlayer1();
                move = new DomineeringMove((char) (row), col, (char) (row), col + 1);
                clicked=true;
                player1turn = false;
            }else{
                clickedNode.setAsCheckedPlayer2();
                move = new DomineeringMove((char) (row), col, (char) (row + 1), col);
                clicked=true;
                player1turn = true;
            }
        }else{
            clickedNode.setAsCheckedPlayer1();
            move = new DomineeringMove((char) (row), col, (char) (row), col + 1);
            clicked=true;
        }
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
                    }
                }
            }
        }
    }



}
