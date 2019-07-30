package com.example.application;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class MyService extends Service {
    private static final String TAG = "MyService";
    private String mPackName;
    private ActivityManager mActivityManager;

    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread thread = new HandlerThread("name");
        new Handler(thread.getLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String o = (String) msg.obj;
            }
        };
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String process = getProcessName();
        mPackName = getPackageName();
        boolean isRun = isRunningProcess(mActivityManager, mPackName);
//        Log.i(TAG, String.format("onCreate: %s %s pid=%d uid=%d isRun=%s", mPackName, process,Process.myPid(), Process.myUid(), isRun));
        if (!isRun) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(mPackName);
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 获取当前进程名称     *     * @return
     */
    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 进程是否存活     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean isRunningProcess(ActivityManager manager, String processName) {
        Log.i("11111111111", "222222222");
        if (manager == null) {
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    show();

                }
            }, 1000);*/
            return false;
        }
        List<ActivityManager.RunningAppProcessInfo> runnings = manager.getRunningAppProcesses();
        if (runnings != null) {
            for (ActivityManager.RunningAppProcessInfo info : runnings) {
                if (TextUtils.equals(info.processName, processName)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            show();

                        }
                    }, 0);
                    return true;
                }
            }
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void show() {
        Intent intent = new Intent(this, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);

        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(getApplicationContext(), createNotificationChannel(this));
        Notification notification = notificationCompatBuilder
                // Title for API <16 (4.0 and below) devices.
                .setContentTitle("标题")
                // Content for API <24 (7.0 and below) devices.
                .setContentText("内容")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(),
                        R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setCategory(Notification.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
//        NotificationManagerCompat.from(getApplicationContext()).notify(1, notification);
        startForeground(1, notification);
    }

//这块Android9.0分类的比较完整，你创建多个这样的东西，你可以在设置里边显示那个或者第几个

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("111111111111", "/////////////");
    }

    public static String createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channelId";
            CharSequence channelName = "channelName";
            String channelDescription ="channelDescription";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            // 设置描述 最长30字符
            notificationChannel.setDescription(channelDescription);
            // 该渠道的通知是否使用震动
            notificationChannel.enableVibration(true);
            // 设置显示模式
            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            return null;
        }
    }

}
