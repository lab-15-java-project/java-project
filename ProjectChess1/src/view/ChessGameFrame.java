package view;

import javax.swing.*;
import java.awt.*;
import java.io.*;

import controller.ClickController;
import controller.GameController;
import model.*;

public class ChessGameFrame extends JFrame {
    private  int Width;
    private  int Height;
    private  int ChessBoard_size;
    private GameController gameController;
    private boolean isCastlingBlack =false;
    private boolean isCastlingWhite =false;

    Chessboard chessboard;
    private ClickController clickController=new ClickController(this);

    JLabel label = new JLabel();
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
        chessboard.setX(Width);
        chessboard.setY(Height);
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
            int optionDialog= JOptionPane.showOptionDialog(null, "Who comes first?", "Start", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"BLACK", "WHITE"}, "WHITE");
            chessboard.setCurrentColor(optionDialog==0?ChessColor.BLACK:ChessColor.WHITE);
            changeCurrentPlayer("Current action player : "+chessboard.getCurrentColor());
            repaint();
            label.repaint();
            chessboard.initiateTheNormalGame();
            JButton button5 = new JButton("Castling");
            button5.setLocation(Height ,Height / 10 );
            button5.setSize(195, 40);
            button5.setFont(new Font("Times New Roman", Font.BOLD, 20));
            add(button5);
            isCastlingWhite=false;
            isCastlingBlack=false;
            button5.addActionListener(c -> {
                if (chessboard.getCheckMating()){
                    JOptionPane.showMessageDialog(null,"Is Checking!","Error!",JOptionPane.WARNING_MESSAGE);
                }
                else{
                    boolean isAttacked =false;
                    boolean isDone=false;
                    if (chessboard.getCurrentColor()==ChessColor.BLACK){
                        if (isCastlingBlack){
                            JOptionPane.showMessageDialog(null,"You have operated it!","Error!",JOptionPane.WARNING_MESSAGE);
                        }
                        else {
                            int option= JOptionPane.showOptionDialog(null, "Castling?", "Castling", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Castle Kingside", "Castle Queenside","Cancel"}, "Cancel");
                            if (option==0){
                                if (chessboard.getChess(0,7).getMove()!=0||chessboard.getChess(0,4).getMove()!=0){
                                    JOptionPane.showMessageDialog(null,"You have moved the chess!","Error!",JOptionPane.WARNING_MESSAGE);
                                }
                                else {
                                    if (!(chessboard.getChess(0,6) instanceof EmptySlotComponent&&chessboard.getChess(0,5) instanceof EmptySlotComponent)){
                                        JOptionPane.showMessageDialog(null,"There is something blocking!","Error!",JOptionPane.WARNING_MESSAGE);
                                    }
                                    else {
                                        for (int i=0;i<chessboard.getArrayList().size();i++){
                                            if (chessboard.getArrayList().get(i).getChessColor()==ChessColor.BLACK){
                                                continue;
                                            }
                                            for (int j=0;j<chessboard.getArrayList().get(i).getList().size();j++){
                                                if (chessboard.getArrayList().get(i).getList().get(j).getX()==0&&chessboard.getArrayList().get(i).getList().get(j).getY()==5){
                                                    isAttacked=true;
                                                    break;
                                                }
                                                if (chessboard.getArrayList().get(i).getList().get(j).getX()==0&&chessboard.getArrayList().get(i).getList().get(j).getY()==6){
                                                    isAttacked=true;
                                                    break;
                                                }
                                            }
                                            if (isAttacked){
                                                JOptionPane.showMessageDialog(null,"The King will be attacked!","Error!",JOptionPane.WARNING_MESSAGE);
                                                break;}
                                        }
                                        if (!isAttacked){
                                            chessboard.swapChessComponents(chessboard.getChess(0,4),chessboard.getChess(0,6));
                                            chessboard.swapChessComponents(chessboard.getChess(0,7),chessboard.getChess(0,5));
                                            isCastlingBlack=true;
                                            chessboard.repaint();
                                            chessboard.swapColor();
                                            isDone=true;
                                        }
                                    }
                                }
                            }
                            else if (option==1){
                                if (chessboard.getChess(0,0).getMove()!=0||chessboard.getChess(0,4).getMove()!=0){
                                    JOptionPane.showMessageDialog(null,"You have moved the chess!","Error!",JOptionPane.WARNING_MESSAGE);
                                }
                                else {
                                    if (!(chessboard.getChess(0,2) instanceof EmptySlotComponent&&chessboard.getChess(0,3) instanceof EmptySlotComponent&&chessboard.getChess(0,1) instanceof EmptySlotComponent)){
                                        JOptionPane.showMessageDialog(null,"There is something blocking!","Error!",JOptionPane.WARNING_MESSAGE);
                                    }
                                    else {
                                        for (int i=0;i<chessboard.getArrayList().size();i++){
                                            if (chessboard.getArrayList().get(i).getChessColor()==ChessColor.BLACK){
                                                continue;
                                            }
                                            for (int j=0;j<chessboard.getArrayList().get(i).getList().size();j++){
                                                if (chessboard.getArrayList().get(i).getList().get(j).getX()==0&&chessboard.getArrayList().get(i).getList().get(j).getY()==3){
                                                    isAttacked=true;
                                                    break;
                                                }
                                                if (chessboard.getArrayList().get(i).getList().get(j).getX()==0&&chessboard.getArrayList().get(i).getList().get(j).getY()==2){
                                                    isAttacked=true;
                                                    break;
                                                }
                                            }
                                            if (isAttacked){
                                                JOptionPane.showMessageDialog(null,"The King will be attacked!","Error!",JOptionPane.WARNING_MESSAGE);
                                                break;}
                                        }
                                        if (!isAttacked){
                                            chessboard.swapChessComponents(chessboard.getChess(0,4),chessboard.getChess(0,2));
                                            chessboard.swapChessComponents(chessboard.getChess(0,0),chessboard.getChess(0,3));
                                            isCastlingBlack=true;
                                            chessboard.repaint();
                                            chessboard.swapColor();
                                            isDone=true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (chessboard.getCurrentColor()==ChessColor.WHITE&&!isDone){
                        if (isCastlingWhite){
                            JOptionPane.showMessageDialog(null,"You have operated it!","Error!",JOptionPane.WARNING_MESSAGE);
                        }
                        else {
                            int option= JOptionPane.showOptionDialog(null, "Castling?", "Castling", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Castle Kingside", "Castle Queenside","Cancel"}, "Cancel");
                            if (option==0){
                                if (chessboard.getChess(7,7).getMove()!=0||chessboard.getChess(7,4).getMove()!=0){
                                    JOptionPane.showMessageDialog(null,"You have moved the chess!","Error!",JOptionPane.WARNING_MESSAGE);
                                }
                                else {
                                    if (!(chessboard.getChess(7,6) instanceof EmptySlotComponent&&chessboard.getChess(7,5) instanceof EmptySlotComponent)){
                                        JOptionPane.showMessageDialog(null,"There is something blocking!","Error!",JOptionPane.WARNING_MESSAGE);
                                    }
                                    else {
                                        for (int i=0;i<chessboard.getArrayList().size();i++){
                                            if (chessboard.getArrayList().get(i).getChessColor()==ChessColor.WHITE){
                                                continue;
                                            }
                                            for (int j=0;j<chessboard.getArrayList().get(i).getList().size();j++){
                                                if (chessboard.getArrayList().get(i).getList().get(j).getX()==7&&chessboard.getArrayList().get(i).getList().get(j).getY()==5){
                                                    isAttacked=true;
                                                    break;
                                                }
                                                if (chessboard.getArrayList().get(i).getList().get(j).getX()==7&&chessboard.getArrayList().get(i).getList().get(j).getY()==6){
                                                    isAttacked=true;
                                                    break;
                                                }
                                            }
                                            if (isAttacked){
                                                JOptionPane.showMessageDialog(null,"The King will be attacked!","Error!",JOptionPane.WARNING_MESSAGE);
                                                break;}
                                        }
                                        if (!isAttacked){
                                            chessboard.swapChessComponents(chessboard.getChess(7,4),chessboard.getChess(7,6));
                                            chessboard.swapChessComponents(chessboard.getChess(7,7),chessboard.getChess(7,5));
                                            isCastlingWhite=true;
                                            chessboard.repaint();
                                            chessboard.swapColor();
                                        }
                                    }
                                }
                            }
                            else if (option==1){
                                if (chessboard.getChess(7,0).getMove()!=0||chessboard.getChess(7,4).getMove()!=0){
                                    JOptionPane.showMessageDialog(null,"You have moved the chess!","Error!",JOptionPane.WARNING_MESSAGE);
                                }
                                else {
                                    if (!(chessboard.getChess(7,2) instanceof EmptySlotComponent&&chessboard.getChess(7,3) instanceof EmptySlotComponent&&chessboard.getChess(7,1) instanceof EmptySlotComponent)){
                                        JOptionPane.showMessageDialog(null,"There is something blocking!","Error!",JOptionPane.WARNING_MESSAGE);
                                    }
                                    else {
                                        for (int i=0;i<chessboard.getArrayList().size();i++){
                                            if (chessboard.getArrayList().get(i).getChessColor()==ChessColor.WHITE){
                                                continue;
                                            }
                                            for (int j=0;j<chessboard.getArrayList().get(i).getList().size();j++){
                                                if (chessboard.getArrayList().get(i).getList().get(j).getX()==7&&chessboard.getArrayList().get(i).getList().get(j).getY()==3){
                                                    isAttacked=true;
                                                    break;
                                                }
                                                if (chessboard.getArrayList().get(i).getList().get(j).getX()==7&&chessboard.getArrayList().get(i).getList().get(j).getY()==2){
                                                    isAttacked=true;
                                                    break;
                                                }
                                            }
                                            if (isAttacked){
                                                JOptionPane.showMessageDialog(null,"The King will be attacked!","Error!",JOptionPane.WARNING_MESSAGE);
                                                break;}
                                        }
                                        if (!isAttacked){
                                            chessboard.swapChessComponents(chessboard.getChess(7,4),chessboard.getChess(7,2));
                                            chessboard.swapChessComponents(chessboard.getChess(7,0),chessboard.getChess(7,3));
                                            isCastlingWhite=true;
                                            chessboard.repaint();
                                            chessboard.swapColor();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
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
        gameController = new GameController(chessboard,this);
        chessboard.setLocation(Height / 10, Height / 10);
        add(chessboard);
    }
    public void changeCurrentPlayer(String str){
        label.setText(str);
        label.setLocation(Height - 500,Height / 10 - 40);
        label.setSize(300,20);
        label.setFont(new Font("Times New Roman", Font.BOLD, 15));
        add(label);
        repaint();
    }

}
