package com.example.stickman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.SurfaceHolder;

import java.util.ArrayList;

public class DrawThread extends Thread {

    private int points = 0;

    private SurfaceHolder surfaceHolder;
    private volatile boolean running = true;

    private int towardPointX;
    private int towardPointY;

    boolean pause = false;
    boolean is_died = false;
    boolean main_menu = true;
    boolean draw_sprites = false;

    Sprite stickman;
    Sprite heart;
    Sprite rocket;
    double inaccuracy;
    int inaccuracy_param;
    int rocket_speed;
    int lives = 3;

    private Paint paint;

    int width = 0;
    int height = 0;

    String mode = "flying";
    boolean long_click_enabled = false;
    private final int timer_interval = 50;

    private Bitmap background;
    private Bitmap pause_icon;
    ArrayList<Bitmap> stickman_frames;
    ArrayList<Bitmap> rocket_frames;

    DrawThread(Context context, SurfaceHolder surfaceHolder){
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        pause_icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause);

        this.surfaceHolder = surfaceHolder;

        stickman_frames = new ArrayList<>();
        stickman_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.p_1)); // 0
        stickman_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.p_2)); // 1
        stickman_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.p_3)); // 2
        stickman_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.p_4)); // 3
        stickman_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.p_5)); // 4
        stickman_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.p_6)); // 5
        stickman_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.p_7)); // 6
        stickman_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.p_8)); // 7
        stickman_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.p_9)); // 8
        stickman_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.p_10)); // 9
        stickman_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.p_11)); // 10
        stickman_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.p_12)); // 11
        stickman_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.p_jump)); // 12
        stickman_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.p_jumpd)); // 13

        rocket_frames = new ArrayList<>();
        rocket_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket_1)); // 0
        rocket_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket_2)); // 1
        rocket_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket_3)); // 2
        rocket_frames.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket_4)); // 3

        stickman = new Sprite(200, 0 , 0, 0, stickman_frames.get(8), 100, 100);
        stickman.add_frame(stickman_frames.get(9));
        stickman.add_frame(stickman_frames.get(10));
        stickman.add_frame(stickman_frames.get(11));

        rocket = new Sprite(2500, 300, -300, 0, rocket_frames.get(0), 90, 30);
        rocket.add_frame(rocket_frames.get(1));
        rocket.add_frame(rocket_frames.get(2));
        rocket.add_frame(rocket_frames.get(3));

        heart = new Sprite(-100, -100, 0, 0,
                BitmapFactory.decodeResource(context.getResources(), R.drawable.heart), 40, 40);

        paint = new Paint();
        MyTimer timer = new MyTimer();
        timer.start();
    }

    void requestStop(){
        running = false;
    }

    @Override
    public void run() {
        while(running){
            Canvas canvas = surfaceHolder.lockCanvas();
            if(canvas != null){
                try{
                    width = canvas.getWidth();
                    height = canvas.getHeight();
                    canvas.drawBitmap(Bitmap.createScaledBitmap(background, canvas.getWidth(), canvas.getHeight(), true), 0, 0 , paint);

                    paint.setTextSize(55.0f);
                    paint.setColor(Color.BLACK);

                    if(main_menu){
                        canvas.drawARGB(150, 211, 211, 211);
                        paint.setColor(Color.BLACK);
                        paint.setStrokeWidth(5);

                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawRect(width / 2 - 110, height / 2 - 150, width / 2 + 75, height / 2 - 85, paint);
//                        canvas.drawRect(width / 2 - 155, height / 2 - 50, width / 2 + 110, height / 2 + 15, paint);
                        canvas.drawRect(width / 2 - 90, height / 2 + 50, width / 2 + 50, height / 2 + 115, paint);


                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(Color.rgb(211, 211, 211));
                        canvas.drawRect(width / 2 - 110, height / 2 - 150, width / 2 + 75, height / 2 - 85, paint);
//                        canvas.drawRect(width / 2 - 155, height / 2 - 50, width / 2 + 110, height / 2 + 15, paint);
                        canvas.drawRect(width / 2 - 90, height / 2 + 50, width / 2 + 50, height / 2 + 115, paint);

                        paint.setColor(Color.BLACK);
                        canvas.drawText("START", width / 2 - 100, height / 2 - 100, paint);
//                        canvas.drawText("SETTINGS", width / 2 - 150, height / 2, paint);
                        canvas.drawText("QUIT", width / 2 - 80, height / 2 + 100, paint);
                    }
                    else {
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawText("points: " + points, width - 500, 70, paint);
                        canvas.drawBitmap(heart.get_current_bitmap(), null, new Rect(width - 500, 80, width - 450, 130), paint);
                        canvas.drawText(": " + lives, width - 440,  120, paint);
                        canvas.drawBitmap(pause_icon, null, new Rect(0, 0, 100 ,100), paint);

                        stickman.draw(canvas);
                        rocket.draw(canvas);
                        heart.draw(canvas);

                        if(draw_sprites){
                            //отрисовка спрайтов
                            draw_sprites(canvas, stickman);
                            draw_sprites(canvas, rocket);
                            draw_sprites(canvas, heart);
                            rocket.setPadding(5);
                            heart.setPadding(0);
                        }

                        //дорожка
                        paint.setStyle(Paint.Style.FILL_AND_STROKE);
                        paint.setColor(Color.rgb(255, 228, 181));
                        canvas.drawRect(0, height - stickman.get_bitmap_height(), width, height, paint);

                        rocket_speed = 200 + points * 20;
                        if (rocket_speed >= 800)
                            rocket_speed = 800;
                        //параметр, задающий правильное значение переменной inaccuracy
                        inaccuracy_param = 100 - points * 5;
                        if (inaccuracy_param <= 0)
                            inaccuracy_param = 0;

                        //выравнивание ракеты
                        if (rocket.getY() + rocket.get_bitmap_height() > height - stickman.get_bitmap_height() + 10)
                            rocket.setVelocityY(-rocket_speed);
                        else if (rocket.getY() + rocket.get_bitmap_height() < 0)
                            rocket.setVelocityY(rocket_speed);

                        //выравнивание сердца
                        if (heart.getX() + heart.get_bitmap_width() < 0)
                            teleport_heart();


                        if (rocket.getX() > stickman.getX() + stickman.getPadding() + inaccuracy_param) {
                            inaccuracy = Math.abs(stickman.getX() - rocket.getX()) * Math.random() + inaccuracy_param;
                            if (rocket.getY() < stickman.getY() + stickman.getPadding() - inaccuracy)
                                rocket.setVelocityY(rocket_speed);
                            if (rocket.getY() > stickman.getY() + stickman.getPadding() + inaccuracy)
                                rocket.setVelocityY(-rocket_speed);
                        } else {
                            rocket.setVelocityX(-rocket_speed);
                        }

                        //если столкнется
                        if (rocket.intersect(stickman)) {
                            teleport_enemy();
                            lives--;
                        }

                        //если пролетит мимо
                        if (rocket.getX() < 0) {
                            teleport_enemy();
                            points++;
                        }

                        if(heart.intersect(stickman)){
                            teleport_heart();
                            lives++;
                        }

                        if (lives == 0)
                            is_died = true;

                        switch (mode) {
                            case "flying":
                                long_click_enabled = false;
                                stickman.setVelocityY(300.0);

                                //переход на бег
                                if (stickman.getY() + stickman.get_bitmap_height() > height - stickman.get_bitmap_height()) {
                                    go_to_run();
                                }

                                break;
                            case "running":
                                long_click_enabled = true;
                                break;
                            case "jumping":
                                long_click_enabled = false;
                                if (stickman.getY() < height - 2 * stickman.getY() && stickman.getVelocityY() < 0) {
                                    stickman.remove_all_frames();
                                    stickman.add_frame(stickman_frames.get(13)); //прыжок вниз
                                    stickman.setVelocityY(1000);
                                }

                                //переход на бег
                                if (stickman.getY() + stickman.get_bitmap_height() > height - stickman.get_bitmap_height()) {
                                    go_to_run();
                                }
                                break;
                        }
                        //отрисовка паузы
                        if (is_died) {
                            canvas.drawARGB(150, 211, 211, 211);
                            paint.setColor(Color.BLACK);
                            paint.setStyle(Paint.Style.FILL);
                            canvas.drawBitmap(pause_icon, null, new Rect(0, 0, 100, 100), paint);
                            canvas.drawText("DIED! Tap for go to main menu", width / 2 - 300, height / 2, paint);
                        }
                        if (pause) {
                            canvas.drawARGB(150, 211, 211, 211);
                            paint.setStyle(Paint.Style.FILL);
                            paint.setColor(Color.BLACK);
                            canvas.drawBitmap(pause_icon, null, new Rect(0, 0, 100, 100), paint);
                            canvas.drawText("PAUSE", width / 2 - 100, height / 2, paint);
                        }
                    }
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
    private void draw_sprites(Canvas canvas, Sprite sprite){
        //рисуем внешний спрайт
        Rect sprite1 = sprite.get_external_box_rect();
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(sprite1, paint);

        //рисуем внутренний спрайт
        Rect sprite2 = sprite.get_bounding_box_rect();
        paint.setStrokeWidth(2);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(sprite2, paint);
    }

    private void go_to_run(){
        mode = "running";
        stickman.remove_all_frames();
        stickman.setY(height - 2*stickman.get_bitmap_height());
        stickman.setVelocityY(0.0);
        for(int i = 0; i < 8; i++)
            stickman.add_frame(stickman_frames.get(i));
    }
    private void update(){
        if(!pause && !is_died && !main_menu) {
            stickman.update(timer_interval);
            rocket.update(timer_interval);
            heart.update(timer_interval);
        }
    }
    void teleport_enemy(){
        rocket.setX(width + Math.random()*500);
        rocket.setY(Math.random()*height/2);
        rocket.setVelocityX(-rocket_speed);
    }
    void teleport_heart(){
        heart.setX(width + 6000 + Math.random()*4000);
        heart.setY(height - (stickman.get_bitmap_height() + 50 + Math.random()*2*stickman.get_bitmap_height()));
        heart.setVelocityX(-300);
    }
    void set_toward_points(int x, int y){
        towardPointX = x;
        towardPointY = y;
    }

    class MyTimer extends CountDownTimer {
        MyTimer(){
            super(Integer.MAX_VALUE, timer_interval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            update();
        }

        @Override
        public void onFinish() {}
    }
}
