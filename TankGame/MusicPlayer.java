/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;
import java.io.File;


import javax.sound.sampled.*;
/**
 *
 * @author Vimean Sam
 */
public class MusicPlayer {
    Clip music;
    Clip fx;
    AudioInputStream AIS;
    AudioInputStream AIS2;

    public MusicPlayer() {

    }
    public void MusicStart(String song) {
        try {
            AIS = AudioSystem.getAudioInputStream(new File(song).getAbsoluteFile());
            music = AudioSystem.getClip();
            music.open(AIS);
            music.loop((Clip.LOOP_CONTINUOUSLY));
        }
        catch(Exception e) {
            System.err.println("****Unsupported Audio File****");
        }
        music.start();
    }

    public void outPutAudio(String sfx) {
        try {
            AIS2 = AudioSystem.getAudioInputStream(new File(sfx).getAbsoluteFile());
            fx = AudioSystem.getClip();
            fx.open(AIS2);
        }
        catch(Exception e) {
            System.err.println("****Unsupported Audio File****");
        }
        FloatControl gainControl = (FloatControl) fx.getControl(FloatControl.Type.MASTER_GAIN);
        //gainControl.setValue(-10.0f);
        fx.start();
    }
}
