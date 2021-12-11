package com.toptech;

import android.media.AudioManager;

import java.lang.reflect.InvocationTargetException;

public class AudioManagerEX {
    private static Class aClass = AudioManager.class;


    public static boolean isMasterMute(AudioManager mAudioManager) {
        try {
            return (boolean) aClass.getDeclaredMethod("isMasterMute").invoke(mAudioManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void setMasterMute(AudioManager audioManager,boolean a,int i){
        try {
            aClass.getDeclaredMethod("setMasterMute",String.class,Integer.TYPE).invoke(audioManager,a,i);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
