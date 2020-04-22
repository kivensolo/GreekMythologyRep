package com.zeke.ktx.modules.aac

import android.os.Bundle
import android.view.View
import com.kingz.customdemo.R
import com.zeke.kangaroo.utils.ZLog
import com.zeke.ktx.base.BaseActivity
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * author: King.Z <br>
 * date:  2020/4/12 20:39 <br>
 * description: 协程练习 <br>
 */
class CoroutineTestActivity:BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.xiecheng_layout)
    }

    fun apiConcurrentRequest(view:View){
        apiConcurrentRequest()
    }

    fun apiLinkRequest(view:View){
        apiLinkRequest()
    }


    /**
     * 并发同时请求多个网络异步请求   使用 async 和 await 配合使用
     * Rxjava中可以使用Observable.zip
     *
     * CoroutineScope.async {} 和
     */
    private fun apiConcurrentRequest(){
        // 创建协程代码
        // 默认创建 Dispatchers.Default 线程组,此处指明Main线程
        GlobalScope.launch(Dispatchers.Main) {

            /** measureTimeMillis返回给定的block代码的执行时间*/
            val time = measureTimeMillis {
                // 开启io线程  withContext 是谁的Context ? CoroutineContext，协程上下文，
                val sum = withContext(Dispatchers.IO) {
                    // withContext 是真正切线程的
                    val asyncA = async { ioCode1000() }
                    val asyncB = async { ioCode1500() }
                    asyncA.await() + asyncB.await()
                }
                ZLog.d("两个方法返回值的和：${sum}")
            }
            uiCode1(time) //自动切回主线程
        }

        ZLog.d("--------apiConcurrent start-------->")
    }

    /**
     * 模拟链式调用
     * getDetailInfo的参数是 getUserInfo的 结果
     */
    private fun apiLinkRequest(){
        // 创建协程代码
        GlobalScope.launch {
            /** measureTimeMillis返回给定的block代码的执行时间*/
            val time = measureTimeMillis {
                val sum = withContext(Dispatchers.IO) {
                    val resultA = ioCode1000()
                    val resultB = ioCode1500(resultA)
                    resultA + resultB
                }
                ZLog.d("两个方法返回值的和：${sum}")
            }
            ZLog.d("执行耗时(API_1 + API_2)：${time}")
            uiCode2()
        }
        ZLog.d("--------apiLinkRequest start--------")
    }


    /**
     * 多个接口同时跑，依次到达，全部到达即为Ready.
     */
    private fun BarrierAllApiReadyRequest(){

    }


    /**
     * 可挂起方法1
     * 模拟获取用户信息
     */
    private suspend fun ioCode1000():Int{
        ZLog.d("api_1000()")
        delay(1000)
        return 1
    }


    /**
     * 可挂起方法2
     * 模拟获取详情信息
     */
    private suspend fun ioCode1500(int:Int = 0): Int {
        ZLog.d("api_1500()")
        delay(1500)
        return 2+int
    }

    private fun  uiCode1(time:Long){
        ZLog.d("执行耗时(Max(API_1,API_2))：${time}")
        print("Coroutines Camp ui1 ${Thread.currentThread().name}")
    }

    private fun  uiCode2(){
        print("Coroutines Camp ui2 ${Thread.currentThread().name}")
    }
}