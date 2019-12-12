package com.kingz.work;

import android.content.Context;
import android.support.annotation.NonNull;

import com.kingz.utils.ZLog;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * author：KingZ
 * date：2019/12/12
 * description：A background task using WorkerManger.
 */

public class FileDownloadWorker extends Worker {
    public FileDownloadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Do the work here--in this case, upload the images.
//        uploadImages()
        ZLog.d("FileDownloadWorker","doWork in :" + Thread.currentThread().getName());
        // Indicate whether the task finished successfully with the Result
        return Result.success();
        // finished successfully via Result.success()
        // failed via Result.failure()
        // needs to be retried at a later time via Result.retry()
    }


}
