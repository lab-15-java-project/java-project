package view;

import javax.swing.*;
import java.awt.*;
import java.io.*;

import controller.ClickController;
import controller.GameController;
import model.ChessColor;
import model.ChessComponent;

public class ChessGameFrame extends JFrame {
    private  int Width;
    private  int Height;
    private  int ChessBoard_size;
    private GameController gameController;

    Chessboard chessboard;
    JLabel label = new JLabel();
    private String str = "Current action player : ";
    public ChessGameFrame(int Width, int Height){
        setTitle("CS102A FinalProject : Chess");
        this.Height = Height;
        this.Width = Width;
        this.ChessBoard_size = Height * 3 / 4;

        setSize(Width, Height);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        JFileChooser jFileChooser = new JFileChooser("./");
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        addChessboard();

        changeCurrentPlayer();

        //button1为加载按钮
        JButton button1 = new JButton("Load Game");
        button1.setLocation(Height + 100, Height / 10 + 220);
        button1.setSize(195, 40);
        button1.setFont(new Font("Times New Roman", Font.BOLD, 20));
        add(button1);

        button1.addActionListener(e -> {
            label.repaint();
            jFileChooser.showOpenDialog(jFileChooser);
            File fi = jFileChooser.getSelectedFile();
            try {
                chessboard.loadGame(fi);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            repaint();
        });

        //button2为开始新游戏按钮
        JButton button2 = new JButton("Start New Game");
        button2.setLocation(Height + 100,Height / 10 + 160);
        button2.setSize(195, 40);
        button2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        add(button2);

        button2.addActionListener(e -> {
            repaint();
            label.repaint();
            chessboard.initiateTheNormalGame();

        });

        //button3为储存按钮
        JButton button3 = new JButton("Store the game");
        button3.setLocation(Height + 100,Height / 10 + 280);
        button3.setSize(195, 40);
        button3.setFont(new Font("Times New Roman", Font.BOLD, 20));
        add(button3);

        button3.addActionListener(e -> {
            jFileChooser.showSaveDialog(jFileChooser);
            File f = jFileChooser.getSelectedFile();
            String fName = f.getName();
            File file = new File(jFileChooser.getCurrentDirectory()+"/"+fName+".txt");
            if (!fName.contains(".txt")){
                file = new File(jFileChooser.getCurrentDirectory(),fName+".txt");
            }
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true));
                if (chessboard.getCurrentColor() == ChessColor.BLACK){
                    bufferedWriter.write("B\n");
                }
                else if (chessboard.getCurrentColor() == ChessColor.WHITE){
                    bufferedWriter.write("W\n");
                }
                else bufferedWriter.write("n\n");
                ChessComponent[][] chessComponents = chessboard.getChessComponents();
                for (ChessComponent[] chessComponent : chessComponents) {
                    for (ChessComponent component : chessComponent) {
                        bufferedWriter.write(component.toString());
                    }
                    bufferedWriter.write("\n");
                }
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //button4为清除按钮
        JButton button4 = new JButton("Clear all");
        button4.setLocation(Height + 100,Height / 10 + 340);
        button4.setSize(195,40);
        button4.setFont(new Font("Times New Roman", Font.BOLD, 20));
        add(button4);

        button4.addActionListener(e ->{
            repaint();
            label.repaint();
            chessboard.initiateEmptyChessboard();
        });
    }

    private void addChessboard() {
        chessboard = new Chessboard(ChessBoard_size, ChessBoard_size);
        gameController = new GameController(chessboard);
        chessboard.setLocation(Height / 10, Height / 10);
        add(chessboard);
    }
    public void changeCurrentPlayer(){
        label.setText(str);
        label.setLocation(Height - 500,Height / 10 - 40);
        label.setSize(300,20);
        label.setFont(new Font("Times New Roman", Font.BOLD, 15));
        add(label);
    }
}
