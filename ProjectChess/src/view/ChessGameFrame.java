package view;

import controller.GameController;
import model.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ChessGameFrame extends JFrame {
    private boolean back = false;
    Chessboard chessboard;
    JPanel backPanel;
    JLabel jLabel1;
    JButton button1,button2,button3,button4,button5;
    private boolean isCastlingBlack =false;
    private boolean isCastlingWhite =false;

    public ChessGameFrame(int Width, int Height){
        setTitle("CS102A FinalProject : Chess");

        setSize(Width, Height);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(new view.ChessGameFrame.Layout());

        JFileChooser jFileChooser = new JFileChooser("./");
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        ImageIcon image = new ImageIcon("./1.jpg");// 这是背景图片 .png .jpg .gif 等格式的图片都可以

        backPanel = new JPanel();// 将背景图放在"标签"里。
        backPanel.setLayout(new Layout());
        jLabel1 = new JLabel(image);
        jLabel1.setOpaque(false);
        backPanel.add(jLabel1,0);
        add(backPanel,0);

        //button1为加载按钮
        button1 = new JButton("Load Game");
        backPanel.add(button1,0);
        button1.setFont(new Font("Times New Roman", Font.BOLD, 20));

        button1.addActionListener(e -> {
            try {
                new Sound("10010.wav",1);
            } catch (InterruptedException | IOException | LineUnavailableException | UnsupportedAudioFileException malformedURLException) {
                malformedURLException.printStackTrace();
            }
            jFileChooser.showOpenDialog(jFileChooser);
            File fi = jFileChooser.getSelectedFile();
            String fiName = fi.getName();
            if (!fiName.contains(".txt")){
                JOptionPane.showMessageDialog(null,"并非txt文件！");
            }
            else try {
                chessboard.loadGame(fi);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            chessboard.repaint();
        });

        //button2为开始新游戏按钮
        button2 = new JButton("Start New Game");
        button2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        backPanel.add(button2,0);

        button2.addActionListener(e -> {
            try {
                new Sound("10010.wav",1);
            } catch (InterruptedException | IOException | LineUnavailableException | UnsupportedAudioFileException malformedURLException) {
                malformedURLException.printStackTrace();
            }
            int optionDialog= JOptionPane.showOptionDialog(null, "Who comes first?", "Start", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"BLACK", "WHITE"}, "WHITE");
            chessboard.setCurrentColor(optionDialog==0?ChessColor.BLACK:ChessColor.WHITE);
            repaint();
            chessboard.initiateTheNormalGame();
            JButton button6 = new JButton("Castling");
            button6.setLocation(Width*7/11-10 ,Height / 10 +400+10);
            button6.setSize(Width/4, Height/17);
            button6.setFont(new Font("Times New Roman", Font.BOLD, 20));
            backPanel.add(button6,0);
            isCastlingWhite=false;
            isCastlingBlack=false;
            button6.addActionListener(c -> {
                try {
                    new Sound("10010.wav",1);
                } catch (IOException | InterruptedException | LineUnavailableException | UnsupportedAudioFileException ioException) {
                    ioException.printStackTrace();
                }
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
                    if (chessboard.getCheckMating()){
                        JOptionPane.showMessageDialog(null,"Check!","situation",JOptionPane.WARNING_MESSAGE );
                    }}
            });
        });

        //button3为储存按钮
        button3 = new JButton("Store the game");
        button3.setFont(new Font("Times New Roman", Font.BOLD, 20));
        backPanel.add(button3,0);

        button3.addActionListener(e -> {
            try {
                new Sound("10010.wav",1);
            } catch (InterruptedException | IOException | LineUnavailableException | UnsupportedAudioFileException malformedURLException) {
                malformedURLException.printStackTrace();
            }
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
        button4 = new JButton("Clear all");
        button4.setFont(new Font("Times New Roman", Font.BOLD, 20));
        backPanel.add(button4,0);

        button4.addActionListener(e ->{
            try {
                new Sound("10010.wav",1);
            } catch (InterruptedException | IOException | LineUnavailableException | UnsupportedAudioFileException malformedURLException) {
                malformedURLException.printStackTrace();
            }
            chessboard.initiateEmptyChessboard();
            chessboard.repaint();
        });

        //button5为更换皮肤图片
        button5 = new JButton("Change the background");
        button5.setFont(new Font("Times New Roman", Font.BOLD, 20));
        backPanel.add(button5,0);

        ImageIcon image3 = new ImageIcon("./2.jpg");

        button5.addActionListener(e -> {
            try {
                new Sound("10010.wav",1);
            } catch (InterruptedException | IOException | LineUnavailableException | UnsupportedAudioFileException malformedURLException) {
                malformedURLException.printStackTrace();
            }
            if (!back){
                jLabel1.setIcon(image3);
                back = true;
            }
            else {
                jLabel1.setIcon(image);
                back = false;
            }
            backPanel.add(button1,0);
            backPanel.add(button2,0);
            backPanel.add(button3,0);
            backPanel.add(button4,0);
            backPanel.add(button5,0);
            backPanel.repaint();
        });
    }

    class Layout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {

        }

        @Override
        public void removeLayoutComponent(Component comp) {

        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return null;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return null;
        }

        @Override
        public void layoutContainer(Container parent) {
            int w = parent.getWidth();
            int h = parent.getHeight();
            backPanel.setLocation(0, 0);
            backPanel.setSize(w,h);
            jLabel1.setLocation(0,0);
            jLabel1.setSize(w,h);
            button1.setLocation(w *7/11, h / 10 + h*11/34);
            button1.setSize(w / 4, h/17);
            button2.setLocation(w * 7 / 11,h / 10 + h*4/17);
            button2.setSize(w / 4, h/17);
            button3.setLocation(w * 7 / 11,h / 10 + h*7/17);
            button3.setSize(w / 4, h/17);
            button4.setLocation(w * 7 / 11,h / 10 + h/2);
            button4.setSize(w / 4, h/17);
            button5.setLocation(w * 7 / 11,h / 10 + h*10/17);
            button5.setSize(w / 4, h/17);
            chessboard = new Chessboard(h*3/4,h*3/4);
            GameController gameController = new GameController(chessboard);
            chessboard = new Chessboard(h*3/4,h*3/4);
            chessboard.setVisible(true);
            chessboard.setSize(w/2,h*3/4);
            chessboard.setLocation(h/10,h/10);
            backPanel.add(chessboard,0);
        }
    }

    public void paint(Graphics g){
        super.paint(g);
        if (chessboard.getCurrentColor() == ChessColor.BLACK){
            g.setColor(Color.BLACK);
            g.fillRect(450, 0, 100, 80);
            repaint();
        }
        else if (chessboard.getCurrentColor() == ChessColor.WHITE){
            g.setColor(Color.WHITE);
            g.fillRect(450, 0, 100, 80);
            repaint();
        }
    }
}

