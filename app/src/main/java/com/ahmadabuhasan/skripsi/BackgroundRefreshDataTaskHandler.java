package com.ahmadabuhasan.skripsi;

import android.os.Handler;

public class BackgroundRefreshDataTaskHandler {
    private static final int DELAY_MS = 5 * 60 * 1000; // 5 minutes delay in milliseconds

    private Handler handler;
    private Runnable task;
    private boolean isRunning;

    public BackgroundRefreshDataTaskHandler() {
        handler = new Handler();
        task = null;
        isRunning = false;
    }

    public void startTask(Runnable runnable) {
        if (!isRunning) {
            task = runnable;
            handler.postDelayed(task, DELAY_MS);
            isRunning = true;
        }
    }

    public void stopTask() {
        if (isRunning && task != null) {
            handler.removeCallbacks(task);
            task = null;
            isRunning = false;
        }
    }
}

