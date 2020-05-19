package com.kingz.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.zeke.kangaroo.utils.ZLog;

/**
 * author：KingZ
 * date：2019/12/12
 * description：A background task using WorkerManger.
 */

public class FileDownloadWorker extends Worker {
    public static final String KEY_ADDR_URI = "";
    private String addURL = "";
    public FileDownloadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        addURL = getInputData().getString(KEY_ADDR_URI);

        ZLog.d("FileDownloadWorker","doWork in :" + Thread.currentThread().getName());
        // Indicate whether the task finished successfully with the Result

        // Create the output of the work
        Data outputData = new Data.Builder()
                .putString("result", "Work finished!")
                .build();
        return Result.success(outputData);
        // finished successfully via Result.success()
        // failed via Result.failure()
        // needs to be retried at a later time via Result.retry()
    }
}
