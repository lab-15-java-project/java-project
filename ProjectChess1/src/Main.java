import view.Sound;
import view.StartFrame;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {
        SwingUtilities.invokeLater(() -> {
            StartFrame mainFrame = new StartFrame(1000, 680);
            mainFrame.setVisible(true);
        });
        new Sound("./2541486610.wav",3);
    }
}
