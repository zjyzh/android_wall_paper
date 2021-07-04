package com.example.little_wallpaper.ui.middle;

import android.app.Activity;

//多线程运行，背景线程，这个跟普通的多线程的区别就是
// 它有回调消息，可以通知activity线程说运行结束了
public abstract class BackgroundTask {
    private Activity activity;
    public BackgroundTask(Activity activity) {
        this.activity = activity;
    }
    private void startBackground() {
        new Thread(() -> {
            doInBackground();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    onPostExecute();
                }
            });
        }).start();
    }
    public void execute(){
        startBackground();
    }
    public abstract void doInBackground();
    public abstract void onPostExecute();
}
