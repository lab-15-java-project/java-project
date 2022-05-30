package view;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class StartFrame extends JFrame{

    public StartFrame(int Width, int Height){
        setTitle("CS102A FinalProject : Chess");

        setSize(Width, Height);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        ImageIcon image = new ImageIcon("./1.jpg");
        JLabel imgLabel = new JLabel(image);
        imgLabel.setLocation(0, 0);
        imgLabel.setSize(Width,Height);
        add(imgLabel);

        JButton jButton = new JButton("Start!");
        jButton.setLocation(400,315);
        jButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        jButton.setSize(200,50);
        add(jButton);

        jButton.addActionListener(e -> {
            try {
                new Sound("10010.wav",1);
            } catch (InterruptedException | IOException | LineUnavailableException | UnsupportedAudioFileException malformedURLException) {
                malformedURLException.printStackTrace();
            }
            ChessGameFrame chessGameFrame = new ChessGameFrame(Width,Height);
            chessGameFrame.setVisible(true);
            this.setVisible(false);
        });
    }
}
