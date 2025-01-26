package com.hamy.hubmovies.ui.screens.widgets

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.gson.Gson
import com.hamy.hubmovies.R
import com.hamy.hubmovies.data.model.MovieDetail
import com.hamy.hubmovies.data.model.Movies
import com.hamy.hubmovies.data.network.ApiService
import com.hamy.hubmovies.ui.screens.activity.MainActivityViewModel
import com.hamy.hubmovies.ui.screens.widgets.BottomNavItem.Home
import com.hamy.hubmovies.ui.viewModel.MovieViewModel
import com.hamy.hubmovies.utils.Constants
import com.hamy.hubmovies.utils.Extensions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, splashViewModel: MainActivityViewModel) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val navController = rememberNavController()


    Scaffold(
        topBar = {
            if (splashViewModel.bottomTabsVisible) {
                TopAppBar(
                    title = { Text("HubMovie's") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        },

        bottomBar = {
            if (splashViewModel.bottomTabsVisible) {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    listOf(
                        Home,
                        BottomNavItem.Profile,
                        BottomNavItem.Settings
                    ).forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentRoute == item.screenRoute,
                            onClick = {
                                navController.navigate(item.screenRoute) {
                                    navController.graph.startDestinationRoute?.let { screenRoute ->
                                        popUpTo(screenRoute) {
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
            }
        },
    ) { innerPadding ->
        Navigation(navController, innerPadding, splashViewModel)
    }
}


sealed class BottomNavItem(
    val icon: ImageVector,
    val title: String,
    val screenRoute: String
) {
    data object Home : BottomNavItem(Icons.Filled.Home, "Latest", "home")
    data object Profile : BottomNavItem(Icons.Filled.Person, "Top Rated", "profile")
    data object Settings : BottomNavItem(Icons.Filled.Settings, "Settings", "settings")
}

@Composable
fun Navigation(
    navController: NavHostController,
    innerPadding: PaddingValues,
    splashViewModel: MainActivityViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Home.screenRoute,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Home.screenRoute) { MovieScreen(splashViewModel, navController) }
        composable(BottomNavItem.Profile.screenRoute) { ProfileScreen(splashViewModel, navController) }
        composable(BottomNavItem.Settings.screenRoute) { SettingsScreen(LocalContext.current) }
        composable(
            route = "${Constants.MovieDetail}/{movie}",
            arguments = listOf(navArgument("movie") { type = NavType.StringType })
        ) { backStackEntry ->
            val movieJson = backStackEntry.arguments?.getString("movie")
            val movie = Gson().fromJson(movieJson, Movies.Results::class.java)
            DescriptionPage(navController, movie, splashViewModel)
        }
    }
}

@Composable
fun ProfileScreen(
    splashViewModel: MainActivityViewModel,
    navController: NavHostController,
    viewModel: MovieViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getTopRatedMovies()
    }
    viewModel.res.value.apply {
        if (isLoading)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                CircularProgressIndicator()
            }

        if (error.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                Text(text = error)
            }
        }
        if (data.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 items per row
                contentPadding = PaddingValues(16.dp), // Optional padding
                horizontalArrangement = Arrangement.spacedBy(8.dp), // Optional spacing between items horizontally
                verticalArrangement = Arrangement.spacedBy(8.dp) // Optional spacing between items vertically
            ) {
                items(
                    items = data,
                    key = { it.id!! }
                ) { res ->
                    EachRow(res = res, navController = navController, splashViewModel = splashViewModel)
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(context: Context) {
    LazyColumn {
        arrayListOf("Privacy", "About us", "Version 1.0").let { list ->
            items(list.size) { index ->
                Text(
                    text = list[index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Extensions.mToast("Clicked: " + list[index], context)
                        }
                        .padding(16.dp)
                )
                if (index < list.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
fun MovieScreen(
    splashViewModel: MainActivityViewModel,
    navController: NavHostController,
    viewModel: MovieViewModel = hiltViewModel()
) {
    viewModel.res.value.apply {
        if (isLoading)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                CircularProgressIndicator()
            }

        if (error.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                Text(text = error)
            }
        }
        if (data.isNotEmpty()) {
            LazyColumn {
                items(
                    data,
                    key = {
                        it.id!!
                    }
                ) { res ->
                    EachRow(res = res, navController, splashViewModel = splashViewModel)
                }
            }
        }
    }
}

@Composable
fun DescriptionPage(
    navController: NavHostController,
    movie: Movies.Results? = null,
    splashViewModel: MainActivityViewModel,
    viewModel: MovieViewModel = hiltViewModel()
) {
    Extensions.mBackPressHandle(navController, splashViewModel)
    LaunchedEffect(movie?.id.toString()) {
        viewModel.getMovieDetails(movie?.id.toString())
    }
    LaunchedEffect(Unit) {
        viewModel.getTrendingMovies()
    }
    viewModel.movieDetail.value.apply {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                CircularProgressIndicator()
            }
        }
        if (error.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                Text(text = error)
            }
        }
        if (data != null) {
            Box(modifier = Modifier.wrapContentSize()) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(radius = 10.dp),
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("${ApiService.IMAGE_URL}${data?.poster_path}")
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                        }
                    ) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .size(48.dp)
                                .align(TopStart) // Use BoxScope's alignment
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                    Image(
                        modifier = Modifier
                            .width(300.dp)
                            .height(400.dp)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(15.dp)),
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("${ApiService.IMAGE_URL}${data?.poster_path}")
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .crossfade(true)
                                // .transformations(CircleCropTransformation())
                                .build()
                        ),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        style = TextStyle(
                            fontSize = 16.sp
                        ),
                        color = Color.White,
                        text = data?.original_title ?: "",
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        color = Color.White,
                        text = data?.overview ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(modifier = Modifier.wrapContentSize()) {
                            Text(
                                fontSize = 12.sp,
                                color = Color.White,
                                text = "Rating:" + data?.vote_average.toString(),
                            )
                            Text(
                                fontSize = 12.sp,
                                color = Color.White,
                                text = data?.vote_average.toString(),
                            )
                        }
                        Divider(
                            modifier = Modifier
                                .height(25.dp)
                                .width(1.dp),
                            color = Color.Gray // Change color as needed
                        )
                        Column(modifier = Modifier.wrapContentSize()) {
                            Text(
                                fontSize = 12.sp,
                                color = Color.White,
                                text = "Release Date",
                            )
                            Text(
                                fontSize = 12.sp,
                                color = Color.White,
                                text = data?.release_date.toString(),
                            )
                        }
                        Divider(
                            modifier = Modifier
                                .height(25.dp)
                                .width(1.dp),
                            color = Color.Gray // Change color as needed
                        )
                        Column(modifier = Modifier.wrapContentSize()) {
                            Text(
                                fontSize = 12.sp,
                                color = Color.White,
                                text = "Status",
                            )
                            Text(
                                fontSize = 12.sp,
                                color = Color.White,
                                text = data?.status.toString(),
                            )
                        }
                        Divider(
                            modifier = Modifier
                                .height(25.dp)
                                .width(1.dp),
                            color = Color.Gray // Change color as needed
                        )
                        Column(modifier = Modifier.wrapContentSize()) {
                            Text(
                                fontSize = 12.sp,
                                color = Color.White,
                                text = "Adult",
                            )
                            Text(
                                fontSize = 12.sp,
                                color = Color.White,
                                text = data?.adult.toString(),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(10.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Yellow),
                            textAlign = TextAlign.Start,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            text = " Trending ",
                        )
                    }

                    // trending movies
                    viewModel.trendingMovies.value.apply {
                        if (isLoading) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                                CircularProgressIndicator()
                            }
                        }
                        if (error.isNotEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                                Text(text = error)
                            }
                        }
                        if (data != null) {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                // .background(Color.Black.copy(alpha = 0.5F)),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                items(data.results?.toList() ?: emptyList()) { movie ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color.Black.copy(alpha = 0.5F))
                                            .width(100.dp)
                                            .height(250.dp)
                                    ) {
                                        Image(
                                            modifier = Modifier.fillMaxSize(),
                                            painter = rememberAsyncImagePainter(
                                                model = ImageRequest.Builder(LocalContext.current)
                                                    .data("${ApiService.IMAGE_URL}${movie?.poster_path}")
                                                    .placeholder(R.drawable.ic_launcher_foreground)
                                                    .crossfade(true)
                                                    .build()
                                            ),
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop
                                        )
                                        Text(
                                            textAlign = TextAlign.Center,
                                            text = movie?.original_title ?: "",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .align(Alignment.BottomCenter)
                                                .background(Color.Black.copy(alpha = 0.6f)),
                                            color = Color.White,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}

@Composable
private fun EachRow(
    res: Movies.Results, navController: NavHostController,
    splashViewModel: MainActivityViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                splashViewModel.bottomTabsVisible = false
                val movieJson = Uri.encode(Gson().toJson(res))
                navController.navigate("${Constants.MovieDetail}/$movieJson")
            }
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


