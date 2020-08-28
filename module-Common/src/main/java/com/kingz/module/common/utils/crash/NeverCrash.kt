/*
     The MIT License (MIT)
     Copyright (c) 2017 Jenly Yu
     https://github.com/jenly1314
     Permission is hereby granted, free of charge, to any person obtaining
     a copy of this software and associated documentation files
     (the "Software"), to deal in the Software without restriction, including
     without limitation the rights to use, copy, modify, merge, publish,
     distribute, sublicense, and/or sell copies of the Software, and to permit
     persons to whom the Software is furnished to do so, subject to the
     following conditions:
     The above copyright notice and this permission notice shall be included
     in all copies or substantial portions of the Software.
     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
     FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
     DEALINGS IN THE SOFTWARE.
 */
package com.kingz.module.common.utils.crash

import android.os.Handler
import android.os.Looper

class NeverCrash private constructor() {

    interface CrashHandler {
        fun uncaughtException(thread: Thread?, throwable: Throwable?)
    }

    private var mCrashHandler: CrashHandler? = null
    private fun setCrashHandler(crashHandler: CrashHandler) {
        mCrashHandler = crashHandler
        Handler(Looper.getMainLooper()).post {
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Throwable) {
                    if (mCrashHandler != null) { //捕获异常处理
                        mCrashHandler?.uncaughtException(
                            Looper.getMainLooper().thread
                            ,e
                        )
                    }
                }
            }
        }
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            if (mCrashHandler != null) { //捕获异常处理
                mCrashHandler?.uncaughtException(thread, throwable)
            }
        }
    }

    companion object {
        private var mInstance: NeverCrash? = null
        private val instance: NeverCrash?
            private get() {
                if (mInstance == null) {
                    synchronized(NeverCrash::class.java) {
                        if (mInstance == null) {
                            mInstance = NeverCrash()
                        }
                    }
                }
                return mInstance
            }

        fun init(crashHandler: CrashHandler) {
            instance?.setCrashHandler(crashHandler)
        }
    }
}