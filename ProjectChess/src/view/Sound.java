package view;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Sound{
    private static Clip ac,bc;

    public Sound(String a,int t) throws IOException, InterruptedException, LineUnavailableException, UnsupportedAudioFileException {
        ac = AudioSystem.getClip();
        File file = new File("./"+a);
        ac.open(AudioSystem.getAudioInputStream(file));
        bc = AudioSystem.getClip();
        File file1 = new File("./"+a);
        bc.open(AudioSystem.getAudioInputStream(file1));
        if (t==3){
            playBM();
        }
        else playM();
    }

    public void playBM(){
        ac.loop(-1);
    }

    public void playM(){
        bc.start();
    }
}
