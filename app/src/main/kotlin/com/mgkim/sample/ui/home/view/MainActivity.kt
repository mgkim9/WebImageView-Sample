package com.mgkim.sample.ui.home.view

import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.mgkim.sample.R
import com.mgkim.sample.di.module.GlideApp
import com.mgkim.sample.ui.base.BaseViewModel
import com.mgkim.sample.ui.base.view.BaseActivity
import com.mgkim.sample.ui.home.viewmodel.MainViewModel
import com.mgkim.libs.webimageview.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    lateinit var viewModel: MainViewModel
    override fun getViewModel(): BaseViewModel? = viewModel
    override fun getContentView(): Int = R.layout.activity_main

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun initView() {
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            navController.graph, drawer_layout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
    }

    override fun dataBinding() {
//        viewModel.loading(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.item_remove_cache ->{
                clearMemoryCache()
            }
            R.id.item_remove_cache_file -> {
                clearDiskCache()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Memory cache clear
    private fun clearMemoryCache() {
        NetManager.cacheClear()
        GlideApp.get(this@MainActivity).clearMemory()
        Toast.makeText(this@MainActivity, "item_remove_cache", Toast.LENGTH_LONG).show()
    }

    // Disk cache clear
    private fun clearDiskCache() {
        // io 동작이므로 RequestLocal를 이용하여 비동기 처리
        RequestLocal<Void?>().setDoInBackground {
            // 수행할 local 작업
            NetManager.diskCacheClear()
            GlideApp.get(this@MainActivity).clearDiskCache()
            null
        }.setReceiver { isSuccess, obj ->
            // request 결과
            Toast.makeText(this@MainActivity, "item_remove_cache_file finish", Toast.LENGTH_LONG).show()
        }.useHandler() //결과(onResult)를 mainThread에서 수행 함
            .addReq()   // Request 시작
    }
}
