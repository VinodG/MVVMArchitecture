package com.example.mvvmarchitecture

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.mvvmarchitecture.ui.theme.MVVMArchitectureTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var navHostController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MVVMArchitectureTheme {
                val navHostController = rememberNavController()
                AppNavigation(navHostController)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("TESTING", "onNewIntent:  triggered ${intent?.data?.getQueryParameter("id")}")
        intent?.data?.let {
            navHostController.navigate(it)
        }
    }
}

sealed class Screen(val id: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object Profile : Screen("profile")
}


@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.id) {
        composable(Screen.Splash.id) {
            Button(
                onClick = { navController.navigate(Screen.Login.id) }) {
                Text(
                    "Splash -> Login"
                )
            }
        }
        composable(Screen.Login.id) {
            Button(
                onClick = { navController.navigate(Screen.Home.id) }) {
                Text("Login -> Home")
            }
        }
        composable(Screen.Home.id) {
            Button(onClick = {
                navController.navigate(Screen.Profile.id)
            }
            ) {
                Text("Home -> Profile")
            }
        }
        composable(Screen.Profile.id) {
            Button(onClick = {
                navController.navigate(Screen.Login.id) {
                    launchSingleTop = true
                    popUpTo(Screen.Login.id) {
                        inclusive = false
                    }
                }
            }
            ) {
                Text("Profile -> Login")
            }
        }
    }
}


@Composable
fun ObserverEvents(
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
}