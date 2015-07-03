package com.chendeji.rongchen.common.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by chendeji on 2/7/15.
 */
public class SystemUtil {

    public static boolean isLastActivityInTask(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            for (ActivityManager.RunningTaskInfo info : runningTaskInfos) {
                String packageName = info.topActivity.getPackageName();
                if (context.getPackageName().equalsIgnoreCase(packageName)) {
                    int count = info.numActivities;
                    if (count == 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
