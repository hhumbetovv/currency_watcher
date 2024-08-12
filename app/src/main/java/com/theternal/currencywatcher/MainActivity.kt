package com.theternal.currencywatcher

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.theternal.core.managers.WorkerManager
import com.theternal.currencywatcher.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), WorkerManager {

    private var binding: ActivityMainBinding? = null
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        enableEdgeToEdge()

        setContentView(binding!!.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }

    private fun initViews() {
        navController = (supportFragmentManager.findFragmentById(
            R.id.activityContainerView
        ) as NavHostFragment).navController

        binding?.bottomNavigationView?.setupWithNavController(navController!!)
    }

    override fun startWatcherWorker() {
        val constraints = Constraints.Builder().setRequiredNetworkType(
            NetworkType.CONNECTED
        ).build()

        val watcherWork = PeriodicWorkRequestBuilder<WatcherWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(constraints).setBackoffCriteria(
            BackoffPolicy.LINEAR,
            15, TimeUnit.MINUTES
        ).build()

        val workManagerInstance = WorkManager.getInstance(this)

        workManagerInstance.enqueueUniquePeriodicWork(
            WatcherWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            watcherWork
        )
    }
}