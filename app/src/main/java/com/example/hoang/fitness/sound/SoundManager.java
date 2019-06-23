package com.example.hoang.fitness.sound;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;

import com.example.hoang.fitness.R;

public class SoundManager {
    Context context;
    public MediaPlayer begin;               //ok
    public MediaPlayer congratulations;     //ok
    public MediaPlayer countdown;           //ok
    public MediaPlayer ding;                //ok
    public MediaPlayer get_ready;           //ok
    public MediaPlayer halfwaythere;        //ok
    public MediaPlayer nextStep;            //ok
    public MediaPlayer one;                 //ok
    public MediaPlayer rest;                //na
    public MediaPlayer switchSide;          //na
    public MediaPlayer three;               //ok
    public MediaPlayer two;                 //ok
    public boolean isPlaying = false;

    public SoundManager(Context context) {
        this.context = context;
    }

    public void setup() {
        get_ready = MediaPlayer.create(context, R.raw.sys_getready);
        nextStep = MediaPlayer.create(context, R.raw.sys_nextstep);
        begin = MediaPlayer.create(context, R.raw.sys_begin);
        rest = MediaPlayer.create(context, R.raw.rest);
        congratulations = MediaPlayer.create(context, R.raw.sys_congratulations);
        one = MediaPlayer.create(context, R.raw.sys_countdown1);
        two = MediaPlayer.create(context, R.raw.sys_countdown2);
        three = MediaPlayer.create(context, R.raw.sys_countdown3);
        countdown = MediaPlayer.create(context, R.raw.sys_countdown);
        halfwaythere = MediaPlayer.create(context, R.raw.sys_halfwaythere);
        switchSide = MediaPlayer.create(context, R.raw.switch_side);
        ding = MediaPlayer.create(context, R.raw.sys_ding);
    }

    public void start(MediaPlayer mp) {
        isPlaying = true;
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 500);
                isPlaying = false;
            }
        });
    }

    public void releaseAll() {
        if (get_ready.isPlaying()) get_ready.stop();
        if (nextStep.isPlaying()) nextStep.stop();
        if (begin.isPlaying()) begin.stop();
        if (rest.isPlaying()) rest.stop();
        if (congratulations.isPlaying()) congratulations.stop();
        if (one.isPlaying()) one.stop();
        if (two.isPlaying()) two.stop();
        if (three.isPlaying()) three.stop();
        if (countdown.isPlaying()) countdown.stop();
        if (halfwaythere.isPlaying()) halfwaythere.stop();
        if (switchSide.isPlaying()) switchSide.stop();
        if (ding.isPlaying()) ding.stop();
        get_ready.release();
        nextStep.release();
        begin.release();
        rest.release();
        congratulations.release();
        one.release();
        two.release();
        three.release();
        countdown.release();
        halfwaythere.release();
        switchSide.release();
        ding.release();
        nullAll();
    }

    public void nullAll() {
        get_ready = null;
        nextStep = null;
        begin = null;
        rest = null;
        congratulations = null;
        one = null;
        two = null;
        three = null;
        countdown = null;
        halfwaythere = null;
        switchSide = null;
        ding = null;
    }
}