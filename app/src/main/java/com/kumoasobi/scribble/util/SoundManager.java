package com.kumoasobi.scribble.util;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {

    private static Clip bgmClip, gameClip, quitClip, loadClip, senrenClip, instClip, cancelClip, changeClip, selectClip, startClip, successClip, decideClip;

    public static void init() {
        bgmClip   = loadClip("/assets/sound/yuzu_title_music.wav");
        gameClip   = loadClip("/assets/sound/yuzu_game_music.wav");
        senrenClip = loadClip("/assets/sound/yuzu_title_senren.wav");
        quitClip = loadClip("/assets/sound/yuzu_title_button_quit.wav");
        loadClip = loadClip("/assets/sound/yuzu_title_button_select_save.wav");
        instClip = loadClip("/assets/sound/yuzu_title_button_instruction.wav");
        decideClip = loadClip("/assets/sound/yuzu_button_decide.wav");
        cancelClip = loadClip("/assets/sound/yuzu_button_cancel.wav");
        changeClip = loadClip("/assets/sound/yuzu_button_change.wav");
        selectClip = loadClip("/assets/sound/yuzu_button_select.wav");
        startClip = loadClip("/assets/sound/yuzu_button_start.wav");
        successClip = loadClip("/assets/sound/yuzu_button_success.wav");
    }

    public static void playMenuBGM() {
        if (bgmClip == null) return;
        bgmClip.stop();
        bgmClip.setFramePosition(0);
        bgmClip.loop(Clip.LOOP_CONTINUOUSLY); 
    }

    public static void stopMenuBGM() {
        if (bgmClip != null) bgmClip.stop();
    }
    
    public static void playGameBGM() {
        if (gameClip == null) return;
        gameClip.stop();
        gameClip.setFramePosition(0);
        gameClip.loop(Clip.LOOP_CONTINUOUSLY); 
    }

    public static void stopGameBGM() {
        if (gameClip != null) gameClip.stop();
    }

    public static void playQuit() {
        if (quitClip == null) return;
        quitClip.stop();
        quitClip.setFramePosition(0); 
        quitClip.start();
    }
    public static void playLoad() {
        if (loadClip == null) return;
        loadClip.stop();
        loadClip.setFramePosition(0); 
        loadClip.start();
    }
    public static void playSenren() {
        if (senrenClip == null) return;
        senrenClip.stop();
        senrenClip.setFramePosition(0); 
        senrenClip.start();
    }
    public static void playInst() {
        if (instClip == null) return;
        instClip.stop();
        instClip.setFramePosition(0); 
        instClip.start();
    }

    public static void playDecide() {
        if (decideClip == null) return;
        decideClip.stop();
        decideClip.setFramePosition(0);
        decideClip.start();
    }

    public static void playCancel() {
        if (cancelClip == null) return;
        cancelClip.stop();
        cancelClip.setFramePosition(0);
        cancelClip.start();
    }

    public static void playChange() {
        if (changeClip == null) return;
        changeClip.stop();
        changeClip.setFramePosition(0);
        changeClip.start();
    }

    public static void playSelect() {
        if (selectClip == null) return;
        selectClip.stop();
        selectClip.setFramePosition(0);
        selectClip.start();
    }

    public static void playStart() {
        if (startClip == null) return;
        startClip.stop();
        startClip.setFramePosition(0);
        startClip.start();
    }

    public static void playSuccess() {
        if (successClip == null) return;
        successClip.stop();
        successClip.setFramePosition(0);
        successClip.start();
    }

    public static void shutdown() {
        if (bgmClip  != null) bgmClip.close();
        if (gameClip != null) gameClip.close();
        if (quitClip != null) quitClip.close();
        if (loadClip != null) loadClip.close();
        if (senrenClip != null) senrenClip.close();
        if (instClip != null) instClip.close();
        if (decideClip != null) decideClip.close();
        if (cancelClip != null) cancelClip.close();
        if (changeClip != null) changeClip.close();
        if (selectClip != null) selectClip.close();
        if (startClip != null) startClip.close();
        if (successClip != null) successClip.close();
    }

    private static Clip loadClip(String resourcePath) {
        try {
            URL url = SoundManager.class.getResource(resourcePath);
            if (url == null) return null;
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Failed to load sound: " + resourcePath);
            return null;
        }
    }
}