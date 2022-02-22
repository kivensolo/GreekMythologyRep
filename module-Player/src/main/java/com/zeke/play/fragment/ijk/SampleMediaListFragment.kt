/*
 * Copyright (C) 2015 Bilibili
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zeke.play.fragment.ijk

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.kingz.module.common.base.BaseFragment
import com.zeke.module_player.R

/**
 * IJK demo列表页
 */
class SampleMediaListFragment : BaseFragment() {
    private var mFileListView: ListView? = null
    private var mAdapter: SampleMediaAdapter? = null
    override fun initViews() {
        super.initViews()
        mFileListView = rootView?.findViewById(R.id.file_list_view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity: Activity? = activity
        mAdapter = SampleMediaAdapter(activity)
        mFileListView?.adapter = mAdapter
        mFileListView?.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val item = mAdapter?.getItem(position)
                val name = item?.mName
                val url = item?.mUrl
                // 跳转Video页面
//                VideoActivity.intentTo(activity, url, name);
            }
        mAdapter?.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8","bipbop basic master playlist")
        mAdapter?.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear1/prog_index.m3u8","bipbop basic 400x300 @ 232 kbps")
        mAdapter?.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear2/prog_index.m3u8","bipbop basic 640x480 @ 650 kbps")
        mAdapter?.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear3/prog_index.m3u8","bipbop basic 640x480 @ 1 Mbps")
        mAdapter?.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear4/prog_index.m3u8","bipbop basic 960x720 @ 2 Mbps")
        mAdapter?.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear0/prog_index.m3u8","bipbop basic 22.050Hz stereo @ 40 kbps")
        mAdapter?.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8","bipbop advanced master playlist")
        mAdapter?.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear1/prog_index.m3u8","bipbop advanced 416x234 @ 265 kbps")
        mAdapter?.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear2/prog_index.m3u8","bipbop advanced 640x360 @ 580 kbps")
        mAdapter?.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear3/prog_index.m3u8","bipbop advanced 960x540 @ 910 kbps")
        mAdapter?.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear4/prog_index.m3u8","bipbop advanced 1289x720 @ 1 Mbps")
        mAdapter?.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear5/prog_index.m3u8","bipbop advanced 1920x1080 @ 2 Mbps")
        mAdapter?.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear0/prog_index.m3u8","bipbop advanced 22.050Hz stereo @ 40 kbps")
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_file_list
    }

    internal inner class SampleMediaItem(var mUrl: String, var mName: String)

    internal inner class SampleMediaAdapter(context: Context?) :
        ArrayAdapter<SampleMediaItem?>(
            context,
            android.R.layout.simple_list_item_2
        ) {
        fun addItem(url: String, name: String) {
            add(SampleMediaItem(url, name))
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            var view = convertView
            if (view == null) {
                val inflater = LayoutInflater.from(parent.context)
                view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
            }
            var viewHolder = view?.tag
            if (viewHolder == null) {
                viewHolder = ViewHolder()
                viewHolder.mNameTextView = view?.findViewById<View>(android.R.id.text1) as TextView
                viewHolder.mUrlTextView = view?.findViewById<View>(android.R.id.text2) as TextView
            }
            val item = getItem(position)
            viewHolder = (viewHolder as ViewHolder)
            viewHolder.mNameTextView?.text = item?.mName
            viewHolder.mUrlTextView?.text = item?.mUrl
            return view
        }

        internal inner class ViewHolder {
            var mNameTextView: TextView? = null
            var mUrlTextView: TextView? = null
        }
    }

    companion object {
        fun newInstance(): SampleMediaListFragment {
            return SampleMediaListFragment()
        }
    }
}