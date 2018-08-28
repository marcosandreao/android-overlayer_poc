package br.com.uarini.poc.overlay.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import br.com.uarini.poc.overlay.R;

public class OverLayerService extends Service implements View.OnTouchListener {

    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private ImageView imgHead;

    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private GestureDetector mTapDetector;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.mTapDetector = new GestureDetector(this, this.gestureListener);

        this.windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        this.imgHead = new ImageView(this);
        this.imgHead.setImageResource(R.mipmap.ic_launcher);

        int layoutFlag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE;
        }

        this.params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        this.params.gravity = Gravity.TOP | Gravity.LEFT;
        this.params.x = 0;
        this.params.y = 100;
        this.imgHead.setOnTouchListener(this);
        this.windowManager.addView(this.imgHead, this.params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.imgHead != null) {
            this.windowManager.removeView(this.imgHead);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (this.mTapDetector.onTouchEvent(event)) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.initialX = params.x;
                this.initialY = params.y;
                this.initialTouchX = event.getRawX();
                this.initialTouchY = event.getRawY();
                return true;
            case MotionEvent.ACTION_UP:
                return true;
            case MotionEvent.ACTION_MOVE:
                this.params.x = initialX + (int) (event.getRawX() - this.initialTouchX);
                this.params.y = initialY + (int) (event.getRawY() - this.initialTouchY);
                this.windowManager.updateViewLayout(this.imgHead, this.params);
                return true;
        }
        return false;
    }

    final GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i(OverLayerService.class.getSimpleName(), "onSingleTapConfirmed");
            return true;
        }
    };
}