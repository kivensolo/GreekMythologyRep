package com.zeke.play.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.kingz.module.common.base.BaseActivity
import com.zeke.module_player.R
import com.zeke.play.fragment.ijk.SampleMediaListFragment

/**
 * author：ZekeWang
 * date：2021/5/12
 * description：IJK播放器样例列表页面
 */
class IJKSampleMediaActivity : BaseActivity() {
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_with_toolbar)

        setSupportActionBar(findViewById(R.id.toolbar))

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // TODO: show explanation
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
            }
        }

        val sampleMediaListFragment =
            SampleMediaListFragment()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.body, sampleMediaListFragment)
        transaction.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the task you need to do.
                } else {
                    // permission denied, boo!
                    // Disable the functionality that depends on this permission.
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_sample, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
    //            SettingsActivity.intentTo(this)
                return true
            }
            R.id.action_recent -> {
    //            RecentMediaActivity.intentTo(this)
            }
            R.id.action_sample -> {
    //            SampleMediaActivity.intentTo(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val show = super.onPrepareOptionsMenu(menu)
        if (!show) return show

        val item = menu?.findItem(R.id.action_recent)
        item?.isVisible = false
        return true
    }

}