package com;

import android.app.ActivityOptions;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.Util;
import com.kingz.config.DemoSample;
import com.kingz.config.Sample;
import com.kingz.config.SampleGroup;
import com.kingz.customdemo.R;
import com.zeke.home.adapter.DemoFragmentExpandableListAdapter;
import com.zeke.kangaroo.utils.ZLog;
import com.zeke.kangaroo.view.animation.AnimatedExpandableListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author: KingZ
 * Data: 2015年10月4日下午11:40:26
 * Description: Demo首页
 * 老版本首页  已标记过时
 */
@Deprecated
public class MainActivity extends ExpandableListActivity implements OnItemClickListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private DemoFragmentExpandableListAdapter expandAdapter;
    private AnimatedExpandableListView listView;


    static {
        System.loadLibrary("testNative-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_all_demo);
        listView = (AnimatedExpandableListView) findViewById(android.R.id.list);
        expandAdapter = new DemoFragmentExpandableListAdapter(this);
        listView.setAdapter(expandAdapter);
        listView.setOnGroupClickListener(new ImpOnGroupClickListener());
//		getExpandableListView().setIndicatorBounds(UITools.SCREEN_WIDTH - 60, UITools.SCREEN_WIDTH - 27);

        String fromJNI = stringFromJNI();
        ZLog.d(TAG,"fromJNI = " + fromJNI);
        String[] uris;
        ArrayList<String> uriList = new ArrayList<>();
        AssetManager assetManager = getAssets();
        try {
            for (String asset : assetManager.list("")) {
                if (asset.endsWith(".mainlist.json")) {
//                    uriList.add("asset:///" + asset);
                    uriList.add(asset);
                }
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "One or more lists failed to load", Toast.LENGTH_LONG)
                    .show();
        }
        uris = new String[uriList.size()];
        uriList.toArray(uris);
        Arrays.sort(uris);

        //AsyncTask 异步加载列表数据
        SampleListLoader loaderTask = new SampleListLoader();
        loaderTask.execute(uris);
    }


    private class ImpOnGroupClickListener implements ExpandableListView.OnGroupClickListener {

        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            // We call collapseGroupWithAnimation(int)/expandGroupWithAnimation(int) to animate group
            // expansion/collapse.
            if (listView.isGroupExpanded(groupPosition)) {
                listView.collapseGroupWithAnimation(groupPosition);
            } else {
                listView.expandGroupWithAnimation(groupPosition);
            }
            return true;
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		ActivityOptions opts = ActivityOptions.makeCustomAnimation(this,R.anim.fade,R.anim.hold);
//		ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
        Sample data = (Sample) expandAdapter.getChild(groupPosition, childPosition);
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        Intent intent = data.buildIntent(this);
        if(intent == null){
            Toast.makeText(getApplicationContext(),
                    "Target page resolve failed.", Toast.LENGTH_LONG)
                    .show();
        }else{
            startActivity(intent,opts.toBundle());
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        //FpsTools.finish();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // 法2： 使用 Intent 应用保活
        // 模拟Home按键发送，使back按键触发的时候，不让系统finish当前Activity
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);

        // 法2： 使用 Instrumentation
        //try {
        //    Instrumentation inst = new Instrumentation();
        //    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_HOME);
        //} catch (Exception e) {
        //    Log.e("Exception when sendPointerSync", e.toString());
        //}
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}

    private final class SampleListLoader extends AsyncTask<String, Void, List<SampleGroup>> {
        private boolean sawError;

        @Override
        protected List<SampleGroup> doInBackground(String... uris) {
            List<SampleGroup> result = new ArrayList<>();

            for (String uri : uris) {
                try {
                    InputStream inputStream = getResources().getAssets().open(uri);
                    readSampleGroups(new JsonReader(new InputStreamReader(inputStream, "UTF-8")), result);
                } catch (Exception e) {
                    ZLog.e(TAG, "Error loading sample list: " + uri, e);
                    sawError = true;
                }
            }
            return result;
        }


        @Override
        protected void onPostExecute(List<SampleGroup> result) {
            onSampleGroups(result, sawError);
        }

        private void readSampleGroups(
                JsonReader reader, List<SampleGroup> groups) throws IOException {
            reader.beginArray();
            while (reader.hasNext()) {
                readSampleGroup(reader, groups);
            }
            reader.endArray();
        }

        private void readSampleGroup(JsonReader reader, List<SampleGroup> groups) throws IOException {
            String groupName = "";
            ArrayList<Sample> samples = new ArrayList<>();

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "name":
                        groupName = reader.nextString();
                        break;
                    case "samples":
                        reader.beginArray();
                        while (reader.hasNext()) {
                            samples.add(readEntry(reader));
                        }
                        reader.endArray();
                        break;
                    case "desc":
                        reader.nextString();
                        break;
                    default:
                        throw new ParserException("Unsupported name: " + name);
                }
            }
            reader.endObject();

            SampleGroup group = getGroup(groupName, groups);
            group.samples.addAll(samples);
        }

        private Sample readEntry(JsonReader reader) throws IOException {
            String sampleName = null;
            String clazzName = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "name":
                        sampleName = reader.nextString();
                        break;
                    case "class":
                        clazzName = reader.nextString();
                        break;
                    default:
                        throw new ParserException("Unsupported attribute name: " + name);
                }
            }
            reader.endObject();
            return new DemoSample(sampleName, clazzName);
        }


        private SampleGroup getGroup(String groupName, List<SampleGroup> groups) {
            for (int i = 0; i < groups.size(); i++) {
                if (Util.areEqual(groupName, groups.get(i).title)) {
                    return groups.get(i);
                }
            }
            SampleGroup group = new SampleGroup(groupName);
            groups.add(group);
            return group;
        }
    }

    private void onSampleGroups(final List<SampleGroup> groups, boolean sawError) {
        if (sawError) {
            Toast.makeText(getApplicationContext(), "One or more page lists failed to load.", Toast.LENGTH_LONG)
                    .show();
        }
//        expandAdapter.setSampleGroups(groups);
    }


    public native String stringFromJNI();
}
