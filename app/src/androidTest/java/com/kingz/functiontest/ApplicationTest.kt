package com.kingz.functiontest

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kingz.database.DatabaseApplication
import com.kingz.database.dao.SongDao
import com.zeke.kangaroo.utils.ZLog
import com.zeke.test.BaseInstrumentedTest
import org.junit.Test
import org.junit.runner.RunWith

/**
 * author：ZekeWang
 * date：2021/1/15
 * description：
 */
@RunWith(AndroidJUnit4::class)
class ApplicationTest : BaseInstrumentedTest(){

    @Test
    fun testFindMethod(){
        val app = DatabaseApplication.getInstance()
        val declaredMethod =
            DatabaseApplication::class.java.getDeclaredMethod("getSongDao")
        declaredMethod.isAccessible = true
        val songDao = declaredMethod.invoke(app)
        if(songDao is SongDao){
            ZLog.d("get sondao")
        }
    }
}