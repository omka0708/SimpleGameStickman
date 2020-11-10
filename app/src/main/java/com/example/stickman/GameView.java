package com.example.stickman;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private DrawThread drawThread;

    public GameView(Context context){
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getContext(),getHolder());
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.requestStop();
        boolean retry = true;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                //
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        drawThread.set_toward_points((int)event.getX(),(int)event.getY());
        float x = event.getX();
        float y = event.getY();
        if(drawThread.main_menu){
            //start
            if((int)x > drawThread.width / 2 - 110 && (int)x < drawThread.width / 2 + 75 &&
                    (int)y > drawThread.height / 2 - 150  && (int)y < drawThread.height / 2 - 85){
                drawThread.main_menu = false;
            }
            //quit
            else if((int)x > drawThread.width / 2 - 90 && (int)x < drawThread.width / 2 + 50 &&
                    (int)y > drawThread.height / 2 + 50  && (int)y < drawThread.height / 2 + 115){
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
        if(drawThread.is_died){
            surfaceDestroyed(getHolder());
            surfaceCreated(getHolder());
        }
        if(x < 100 && y < 100){
            drawThread.pause = !drawThread.pause;
        }
        else if(drawThread.long_click_enabled){
            drawThread.mode = "jumping";
            drawThread.stickman.remove_all_frames();
            drawThread.stickman.add_frame(drawThread.stickman_frames.get(12)); //p_jump
            drawThread.stickman.set_current_bitmap(0);
            drawThread.stickman.setVelocityY(-900);
        }
        return false;
    }
}
