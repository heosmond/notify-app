package com.example.notify_app

import ViewNoteScreen
import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.notify_app.ui.theme.NotifyappTheme
import com.example.notify_app.data.NotesDatabase
import com.example.notify_app.ui.customTheme.SpotifyTheme
import com.example.notify_app.ui.screens.AddNotesScreen
import com.example.notify_app.ui.screens.NotesScreen
import com.example.notify_app.ui.screens.SearchTrackScreen
import com.example.notify_app.viewmodel.NotesViewModel


class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            NotesDatabase::class.java,
            "notes.db"
        ).build()
    }

    private val viewModel by viewModels<NotesViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(db.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        // Fetch the Spotify access token during initialization
        viewModel.getAccessToken()

        // Observe the access token for debugging
        viewModel.accessToken.observe(this) { token ->
            if (token != null) {
                //SUCCESS!! I can see the token when I run the program :)
                println("Access Token fetched successfully: $token")
            } else {
                println("Failed to fetch Access Token")
            }
        }

        setContent {
            SpotifyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val state = viewModel.state.collectAsState()
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "NotesScreen") {
                        composable("NotesScreen") {
                            NotesScreen(
                                state = state.value,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }
                        composable("SearchTrackScreen") {
                            SearchTrackScreen(
                                state = state.value,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }
                        composable(
                            route = "ViewNoteScreen/{noteId}",
                            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val noteId = backStackEntry.arguments?.getInt("noteId")
                            val note = state.value.notes.find { it.id == noteId }
                            noteId?.let {
                                ViewNoteScreen(
                                    navController = navController,
                                    selectedNoteId = noteId,
                                    state = state.value,
//                                    onEvent = viewModel::onEvent
                                )
                            }
                        }
                        composable("AddNotesScreen") {
                            AddNotesScreen(
                                state = state.value,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotifyappTheme {

    }
}