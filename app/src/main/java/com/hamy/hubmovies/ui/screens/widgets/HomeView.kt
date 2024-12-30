package com.hamy.hubmovies.ui.screens.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.hamy.hubmovies.R
import com.hamy.hubmovies.data.model.Movies
import com.hamy.hubmovies.data.network.ApiService
import com.hamy.hubmovies.ui.screens.widgets.BottomNavItem.Home
import com.hamy.hubmovies.ui.viewModel.MovieViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val navHostController = rememberNavController()
    val items = listOf(
        Home,
        BottomNavItem.Profile,
        BottomNavItem.Settings
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My App") },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch { drawerState.open() }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navHostController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { item->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.screen_route,
                        onClick = {
                            navHostController.navigate(item.screen_route) {
                                navHostController.graph.startDestinationRoute?.let { screen_route ->
                                    popUpTo(screen_route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
    ) { innerPadding ->
        Navigation(navHostController, innerPadding)
    }
}


sealed class BottomNavItem(
    val icon: ImageVector,
    val title: String,
    val screen_route: String
) {
    object Home : BottomNavItem(Icons.Filled.Home, "Home", "home")
    object Profile : BottomNavItem(Icons.Filled.Person, "Profile", "profile")
    object Settings : BottomNavItem(Icons.Filled.Settings, "Settings", "settings")
}

@Composable
fun Navigation(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(navController = navController, startDestination = Home.screen_route, modifier = Modifier.padding(innerPadding)) {
        composable(Home.screen_route) { MovieScreen() }
        composable(BottomNavItem.Profile.screen_route) { ProfileScreen() }
        composable(BottomNavItem.Settings.screen_route) { SettingsScreen() }
    }
}

@Composable
fun HomeScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Home Screen")
    }
}

@Composable
fun ProfileScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Profile Screen")
    }
}

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Settings Screen")
    }
}

@Composable
fun MovieScreen(
    viewModel: MovieViewModel = hiltViewModel()
) {
    val res = viewModel.res.value
    if (res.isLoading)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
            CircularProgressIndicator()
        }

    if (res.error.isNotEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
            Text(text = res.error)
        }
    }

    if (res.data.isNotEmpty()) {
        LazyColumn {
            items(
                res.data,
                key =  {
                    it.id!!
                }
            ) { res ->
                EachRow(res = res)
            }
        }
    }
}
@Composable
private fun MovieDetailView(){

}
@Composable
private fun EachRow(
    res: Movies.Results
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${ApiService.IMAGE_URL}${res.poster_path}")
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .crossfade(true)
                        .transformations(CircleCropTransformation())
                        .build()
                ),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .padding(10.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(CenterVertically)
            ) {
                Text(
                    text = res.original_title!!, style = TextStyle(
                        fontSize = 16.sp
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = res.overview!!, style = TextStyle(
                        fontSize = 12.sp
                    ),
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}


