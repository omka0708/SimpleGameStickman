package com.example.stickman;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

public class Sprite {
    private ArrayList<Bitmap> bitmaps;
    private double frame_time;
    private double time_for_current_frame;
    private int current_bitmap;
    private int bitmap_width;
    private int bitmap_height;
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private int padding = 15; //15

    Sprite(double x, double y, double velocityX, double velocityY, Bitmap first_bitmap, int bitmap_width, int bitmap_height){
        this.x = x;
        this.y = y;

        this.velocityX = velocityX;
        this.velocityY = velocityY;

        this.bitmaps = new ArrayList<>();
        this.bitmaps.add(first_bitmap);
        this.bitmap_width = bitmap_width;
        this.bitmap_height = bitmap_height;

        this.time_for_current_frame = 0.0;
        this.frame_time = 25;
        this.current_bitmap = 0;
    }

    public boolean frames_is_empty(){
        return bitmaps.isEmpty();
    }

    double getX() {
        return x;
    }

    void setX(double x) {
        this.x = x;
    }

    double getY() {
        return y;
    }

    void setY(double y) {
        this.y = y;
    }

    int getPadding() {
        return padding;
    }

    void setPadding(int padding) {
        this.padding = padding;
    }

    public double getVelocityX() {
        return velocityX;
    }

    void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    double getVelocityY() {
        return velocityY;
    }

    void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    Bitmap get_current_bitmap(){
        return bitmaps.get(current_bitmap);
    }

    int get_bitmap_width(){
        return bitmap_width;
    }

    int get_bitmap_height(){
        return bitmap_height;
    }

    void set_current_bitmap(int current_bitmap){
        this.current_bitmap = current_bitmap;
    }

    void add_frame(Bitmap bitmap){
        bitmaps.add(bitmap);
    }

    public void remove_frame_by_id(int id){
        bitmaps.remove(id);
    }

    void remove_all_frames(){
        bitmaps.clear();
    }

    void update(int ms){
        time_for_current_frame += ms;
        if(time_for_current_frame >= frame_time){
            current_bitmap = (current_bitmap + 1) % bitmaps.size();
            time_for_current_frame = time_for_current_frame - frame_time;
        }
        x += velocityX * ms / 1000.0;
        y += velocityY * ms / 1000.0;
    }

    void draw(Canvas canvas){
        Rect destination = new Rect((int) x, (int) y, (int)(x + bitmap_width), (int)(y + bitmap_height));
        canvas.drawBitmap(get_current_bitmap(), null, destination,  new Paint());
    }

    Rect get_bounding_box_rect(){
        return new Rect((int)x + padding,
                (int) y + padding,
                (int)(x + bitmap_width - padding),
                (int)(y + bitmap_height - padding));
    }

    Rect get_external_box_rect(){
        return new Rect((int)x, (int)y,
                (int)x + get_bitmap_width(),
                (int)y + get_bitmap_height());
    }

    boolean intersect(Sprite sprite){
        return get_bounding_box_rect().intersect(sprite.get_bounding_box_rect());
    }
}
