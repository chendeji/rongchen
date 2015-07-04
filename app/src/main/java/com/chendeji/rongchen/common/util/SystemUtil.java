package com.chendeji.rongchen.common.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.chendeji.rongchen.R;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

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

    public static void showExistDialog(final Context context) {
        Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                ((Activity)context).finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };

        builder.title(context.getString(R.string.exist))
                .positiveAction(context.getString(R.string.positive))
                .negativeAction(context.getString(R.string.negative));
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(((AppCompatActivity)context).getSupportFragmentManager(), null);
    }

}
