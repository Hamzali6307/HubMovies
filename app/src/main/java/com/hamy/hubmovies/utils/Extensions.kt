package com.hamy.hubmovies.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.hamy.hubmovies.ui.screens.activity.MainActivityViewModel
import com.hamy.hubmovies.utils.Constants.MyLOGS

object Extensions {
    fun mLogs(data :String) {
        Log.e(MyLOGS,data)
    }
    fun mToast (data:String, context:Context){
        Toast.makeText(context,data,Toast.LENGTH_SHORT).show()
    }

    @Composable
    fun mBackPressHandle(
        navController: NavHostController,
        splashViewModel: MainActivityViewModel
    ){
        var lastClickTime by remember { mutableLongStateOf(0L) }
        val debounceInterval = 500L // Interval in milliseconds
        BackHandler {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > debounceInterval) {
                lastClickTime = currentTime
                splashViewModel.bottomTabsVisible = true // Show bottom tabs
                navController.popBackStack()
            }
        }
    }
}