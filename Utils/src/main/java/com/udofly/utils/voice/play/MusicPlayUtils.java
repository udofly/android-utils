package com.udofly.utils.voice.play;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;


import java.io.IOException;


/**
 * Describe: 本地音频播放
 * Created by udofly on 2021/12/24.
 */
public class MusicPlayUtils {

    private static MusicPlayUtils     mInstance;
    private static ManagedMediaPlayer mediaPlayer;

    /**
     * 单例
     */
    public static MusicPlayUtils getInstance() {

        if (mInstance == null) {
            mInstance = new MusicPlayUtils();

            mediaPlayer = new ManagedMediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        return mInstance;
    }

    /**
     * 播放本地音乐
     * MusicPlayUtils.getInstance().playLocalMusic(mContext, "fapai2_3.mp3"); //资产目录中
     * @param context
     * @param fileName
     */
    public void playLocalMusic(Context context, String fileName) {
        if (mediaPlayer.isPlaying()) {
            return;
        }
        try {
            //播放 assets/a2.mp3 音乐文件
            AssetFileDescriptor fd = context.getAssets().openFd(fileName);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 停止播放
     */
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * 播放回调
     */
    public interface Callback {
        void onStart();

        void onStop();
    }

}































