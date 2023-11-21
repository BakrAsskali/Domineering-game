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

public class DemoPanel extends JFrame {
    private boolean player1Turn = true;
    final int maxCol = 8;
    final int maxRow = 8;
    final int nodeSize = 80;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;
    Node[][] node = new Node[maxCol][maxRow];
    Node currentNode;
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
        this.repaint();

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(maxCol, maxRow));

        for (int i = 0; i < maxCol; i++) {
            for (int j = 0; j < maxRow; j++) {
                node[i][j] = new Node(i, j);
                gamePanel.add(node[i][j]);
            }
        }

        // Add two buttons in the middle of the grid
        JButton saveButton = new JButton("Save");
        JButton quitButton = new JButton("Quit");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(saveButton);
        buttonPanel.add(quitButton);

        // Add components to the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
        this.setVisible(true);
        this.pack();

        // Add your button action listeners here
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGridToFile("grid.txt");
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleNodeClick(e);
            }
        });
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
}
