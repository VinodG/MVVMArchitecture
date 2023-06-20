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
                DeepLinkNavigation(navHostController)
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
fun DeepLinkNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.id) {
        composable(Screen.Splash.id) {
            Button(
                modifier = Modifier.fillMaxWidth(0.5f),
                onClick = { navController.navigate(Screen.Login.id) }) {
                Text(
                    "Splash -> Login"
                )
            }
        }
        composable(Screen.Login.id) {
            ScreenUi(buttonName = "Login -> Home") {
                navController.navigate(Screen.Home.id)
            }
        }
        composable(Screen.Home.id) {
            Button(onClick = {
                navController.navigate(Screen.Profile.id)
            }
            ) {
                Text("Home->Profile")
            }
        }
//        To trigger deeplink adb shell am start -W -a android.intent.action.VIEW -d "vidi://vinod/profile?token=324e324324"
        val uri = "vidi://vinod/profile?token={id}"
        composable(
//            "profile?id={id}",
            Screen.Profile.id,
            deepLinks = listOf(navDeepLink {
                uriPattern = uri
            })
        ) {
            Profile()
        }
    }
}

@Composable
fun Profile( viewModel: DeepVM = hiltViewModel()) {
    Column {
        Text(text = "received value : " + viewModel.vid)
    }
}

@Composable
fun ScreenUi(
    title: String = "",
    buttonName: String,
    viewModel: AnimationVM = hiltViewModel(),
    function: () -> Unit
) {

    ObserverEvents(LocalLifecycleOwner.current) { source, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.isInitVisible.value = true
        }
    }
    EnterAnimation(
        visible = viewModel.isVisible.value.not(),
        initVisible = viewModel.isInitVisible.value
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(color = Color.Red)
        ) {
            Button(onClick = function) {
                Text(buttonName)
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EnterAnimation(
    visible: Boolean = true,
    initVisible: Boolean = false,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(initialOffsetX = {
            it
        }, animationSpec = tween(500)),
        exit = slideOutHorizontally(targetOffsetX = {
            it
        }, animationSpec = tween(500)),
        content = content,
        initiallyVisible = initVisible
    )
}


@HiltViewModel
class AnimationVM @Inject constructor() : ViewModel() {
    val isVisible = mutableStateOf(false)
    val isInitVisible = mutableStateOf(false)
}

@HiltViewModel
class DeepVM @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    val vid: String = savedStateHandle.get<String>("id").orEmpty()
}
