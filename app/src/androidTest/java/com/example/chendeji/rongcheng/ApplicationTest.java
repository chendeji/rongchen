package com.example.chendeji.rongcheng;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import com.chendeji.rongchen.common.util.Logger;

import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testGetActivity() {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            for (ActivityManager.RunningTaskInfo info : runningTaskInfos){
                String packageName = info.topActivity.getPackageName();
                int count = info.numActivities;
                Logger.i("chendeji", "packageName:"+packageName);
                Logger.i("chendeji", "activity_count:"+count);
            }
        } else {

        }
    }
}