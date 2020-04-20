package com.zeke.ktx.api

import android.content.Context
import android.content.res.AssetManager
import android.os.AsyncTask
import android.util.JsonReader
import android.widget.Toast
import com.google.android.exoplayer2.ParserException
import com.google.android.exoplayer2.util.Util
import com.kingz.config.SampleGroup
import com.zeke.kangaroo.utils.ZLog
import com.zeke.ktx.api.callback.IDataResponse
import com.zeke.ktx.modules.player.entity.DemoGroup
import com.zeke.ktx.modules.player.entity.DemoSample
import org.jetbrains.annotations.NotNull
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/**
 * author：KingZ
 * date：2020/2/15
 * description：Demo样例数据的数据提供实现类，
 * 负责Demo数据配置文件解析和数据返回
 */
class AndroidDemoProvider constructor() : DataApiService<MutableList<DemoGroup>> {
    companion object {
        private val TAG = AndroidDemoProvider::class.java.simpleName
        private lateinit var mCallBack: IDataResponse<MutableList<DemoGroup>>

    }

    /**
     * 获取Demo展示数据信息
     * @param context Context
     * @param callback DataApiService.IDataResponse
     */
    override fun requestData(context: Context,@NotNull callback: IDataResponse<MutableList<DemoGroup>>) {
        mCallBack = callback
        val assetManager = context.assets
        val uris: Array<String>
        val uriList = ArrayList<String>()
        checkMainList(assetManager, uriList, context)
        // 显式指定数组的长度，数组元素全部被初始化为null。
        // 相当于Java数组的动态初始化。
        // uris = arrayOfNulls<String?>(uriList.size)
        uris = uriList.toTypedArray()
        // 同java  Arrays.sort(uris);
        uris.sort()

        // AsyncTask 异步加载列表数据
        SampleListLoader(context).execute(uris[0])
    }

    /**
     * 检查首页数据列表
     */
    private fun checkMainList(assetManager: AssetManager, uriList: ArrayList<String>, context: Context) {
        try {
            for (asset in assetManager.list("")) {
                if (asset.endsWith(".mainlist.json")) {
                    //uriList.add("asset:///" + asset);
                    uriList.add(asset)
                }
            }
        } catch (e: IOException) {
            Toast.makeText(context,
                    "One or more lists failed to load !!!",
                    Toast.LENGTH_LONG)
                    .show()
        }
    }

    class SampleListLoader(var ctx:Context)
        : AsyncTask<String, Void, MutableList<DemoGroup>>() {
        private var sawError: Boolean = false

        override fun doInBackground(vararg parms: String?): MutableList<DemoGroup> {
            val demoGroup = ArrayList<DemoGroup>()

            for (uri in parms) {
                try {
                    val inputStream = ctx.assets.open(uri)
                    readSampleGroups(JsonReader(InputStreamReader(inputStream,
                            "UTF-8")), demoGroup)
                } catch (e: Exception) {
                    ZLog.e(TAG, "Error loading sample list: $uri", e)
                    sawError = true
                }

            }
            return demoGroup
        }

        // 等同于Java的 throws IOException
        @Throws(IOException::class)
        private fun readSampleGroups(
                reader: JsonReader,
                groups: MutableList<DemoGroup>) {
            reader.beginArray()
            while (reader.hasNext()) {
                readSampleGroup(reader, groups)
            }
            reader.endArray()
        }

        @Throws(IOException::class)
        private fun readSampleGroup(
                reader: JsonReader,
                groups: MutableList<DemoGroup>) {
            var groupName = ""
            var groupDesc = ""
            val samples = ArrayList<DemoSample>()

            reader.beginObject()
            while (reader.hasNext()) {
                val name = reader.nextName()
                // switch-case
                when (name) {
                    "name" -> groupName = reader.nextString()
                    "samples" -> {
                        reader.beginArray()
                        while (reader.hasNext()) {
                            samples.add(readEntry(reader))
                        }
                        reader.endArray()
                    }
                    "desc" -> groupDesc = reader.nextString()
                    else -> throw ParserException("Unsupported name: $name")
                }
            }
            reader.endObject()

            val group = getGroup(groupName,groupDesc, groups)
            group.samples.addAll(samples)
        }

        @Throws(IOException::class)
        private fun readEntry(reader: JsonReader): DemoSample {
            var sampleName: String? = null
            var clazzName: String? = null

            reader.beginObject()
            while (reader.hasNext()) {
                val name = reader.nextName()
                when (name) {
                    "name" -> sampleName = reader.nextString()
                    "class" -> clazzName = reader.nextString()
                    else -> throw ParserException("Unsupported attribute name: $name")
                }
            }
            reader.endObject()
            return DemoSample(sampleName, clazzName)
        }


        /**
         * 根据分组名称获取到改组的Demo数据的分组数据
         */
        private fun getGroup(groupName: String,
                             groupDesc: String,
                             groups: MutableList<DemoGroup>): DemoGroup {
            for (i in groups.indices) {
                if (Util.areEqual(groupName, groups[i].title)) {
                    return groups[i]
                }
            }
            val group = DemoGroup(groupName,groupDesc)
            groups.add(group)
            return group
        }


        override fun onPostExecute(result: MutableList<DemoGroup>) {
            super.onPostExecute(result)
            mCallBack.onSuccess(result)
        }

    }

    private fun onSampleGroups(ctx:Context,
                               groups: List<SampleGroup>,
                               sawError: Boolean) {
        if (sawError) {
            Toast.makeText(ctx, "One or more page lists failed to load.", Toast.LENGTH_LONG)
                    .show()
        }
//        expandAdapter.setSampleGroups(groups)
    }
}