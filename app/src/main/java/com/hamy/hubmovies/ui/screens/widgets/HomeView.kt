package com.hamy.hubmovies.ui.screens.widgets

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
import com.hamy.hubmovies.data.model.Movies
import com.hamy.hubmovies.data.network.ApiService
import com.hamy.hubmovies.ui.screens.activity.AuthManager
import com.hamy.hubmovies.ui.screens.activity.MainActivityViewModel
import com.hamy.hubmovies.ui.screens.widgets.BottomNavItem.Home
import com.hamy.hubmovies.ui.viewModel.MovieViewModel
import com.hamy.hubmovies.utils.Constants
import com.hamy.hubmovies.utils.Extensions
import com.hamy.hubmovies.utils.Extensions.ExpandableText
import com.hamy.hubmovies.utils.Extensions.FullScreenYouTubePlayer
import com.hamy.hubmovies.utils.Extensions.GenericErrorScreen
import com.hamy.hubmovies.utils.Extensions.ShimmerDescriptionPage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, splashViewModel: MainActivityViewModel) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val innerNavController = rememberNavController()
    var searchText by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = splashViewModel.bottomTabsVisible,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Hub Movies",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        innerNavController.navigate(Home.screenRoute)
                    },
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Profile") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        innerNavController.navigate(BottomNavItem.Profile.screenRoute)
                    },
                    icon = { Icon(Icons.Filled.Person, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        innerNavController.navigate(BottomNavItem.Settings.screenRoute)
                    },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = null) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (splashViewModel.bottomTabsVisible) {
                    TopAppBar(
                        title = {
                            if (isSearching) {
                                TextField(
                                    value = searchText,
                                    onValueChange = { searchText = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text("Search movies...") },
                                    singleLine = true,
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                    keyboardActions = KeyboardActions(onSearch = {
                                        focusManager.clearFocus()
                                    }),
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            searchText = ""
                                            isSearching = false
                                        }) {
                                            Icon(Icons.Filled.Close, contentDescription = "Close Search")
                                        }
                                    }
                                )
                            } else {
                                Text("HubMovie's")
                            }
                        },
                        navigationIcon = {
                            if (!isSearching) {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(Icons.Filled.Menu, contentDescription = "Menu")
                                }
                            }
                        },
                        actions = {
                            if (!isSearching) {
                                IconButton(onClick = { isSearching = true }) {
                                    Icon(Icons.Filled.Search, contentDescription = "Search")
                                }
                            }
                        }
                    )
                }
            },

            bottomBar = {
                if (splashViewModel.bottomTabsVisible) {
                    NavigationBar {
                        val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        listOf(
                            Home, BottomNavItem.Profile, BottomNavItem.Settings
                        ).forEach { item ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        item.icon,
                                        contentDescription = item.title
                                    )
                                },
                                label = { Text(item.title) },
                                selected = currentRoute == item.screenRoute,
                                onClick = {
                                    innerNavController.navigate(item.screenRoute) {
                                        innerNavController.graph.startDestinationRoute?.let { screenRoute ->
                                            popUpTo(screenRoute) {
                                                saveState = true
                                            }
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                })
                        }
                    }
                }
            },
        ) { innerPadding ->
            Navigation(innerNavController, innerPadding, splashViewModel, searchText, navController)
        }
    }
}


sealed class BottomNavItem(
    val icon: ImageVector, val title: String, val screenRoute: String
) {
    data object Home : BottomNavItem(Icons.Filled.Home, "Latest", "home")
    data object Profile : BottomNavItem(Icons.Filled.Person, "Top Rated", "profile")
    data object Settings : BottomNavItem(Icons.Filled.Settings, "Settings", "settings")
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun Navigation(
    navController: NavHostController,
    innerPadding: PaddingValues,
    splashViewModel: MainActivityViewModel,
    searchQuery: String = "",
    rootNavController: NavController
) {
    NavHost(
        navController = navController,
        startDestination = Home.screenRoute,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Home.screenRoute) { MovieScreen(splashViewModel, navController, searchQuery = searchQuery) }
        composable(BottomNavItem.Profile.screenRoute) {
            ProfileScreen(
                splashViewModel, navController, searchQuery = searchQuery
            )
        }
        composable(BottomNavItem.Settings.screenRoute) { SettingsScreen(LocalContext.current, rootNavController) }
        composable(
            route = "${Constants.MovieDetail}/{movie}",
            arguments = listOf(navArgument("movie") { type = NavType.StringType })
        ) { backStackEntry ->
            val movieJson = backStackEntry.arguments?.getString("movie")
            val movie = Gson().fromJson(movieJson, Movies.Results::class.java)
            DescriptionPage(navController, movie, splashViewModel)
        }
        composable(
            route = "${Constants.PlayVideo}/{videoKey}",
            arguments = listOf(navArgument("videoKey") { type = NavType.StringType })
        ) { backStackEntry ->
            val videoKey = backStackEntry.arguments?.getString("videoKey") ?: ""
            FullScreenYouTubePlayer(videoId = videoKey, onClose = { navController.popBackStack() })
        }
    }
}

@Composable
fun ProfileScreen(
    splashViewModel: MainActivityViewModel,
    navController: NavHostController,
    viewModel: MovieViewModel = hiltViewModel(),
    searchQuery: String = ""
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var pageNumber by remember { mutableIntStateOf(1) }
    var isLoadingMore by remember { mutableStateOf(false) }
    var isLastPage by remember { mutableStateOf(false) }
    var items by remember { mutableStateOf(emptyList<Movies.Results>()) }
    val state = rememberLazyGridState()
    val shouldStartPaginate = remember {
        derivedStateOf {
            (isLoadingMore || isLastPage).not() && (state.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -9) >= (state.layoutInfo.totalItemsCount - 5)
        }
    }

    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value) {
            isLoadingMore = true
            viewModel.getTopRatedMovies(pageNumber = (pageNumber + 1))
        }
    }
    LaunchedEffect(key1 = viewModel.topRatedMovies.value) {
        viewModel.topRatedMovies.value.apply {
            if (isLoading) {
                isLoadingMore = true
            }
            if (error.isNotEmpty()) {
                isLoadingMore = false
            }
            if (data.isNotEmpty()) {
                isLoadingMore = false
                if (data.size < 20) {
                    isLastPage = true
                    scope.launch {
                        snackbarHostState.showSnackbar("This is the last page data")
                    }
                }
                pageNumber++
                // Fix duplicate keys by using distinctBy
                items = (items + data).distinctBy { it.id }
            }
        }
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.getTopRatedMovies(pageNumber = pageNumber)
    }

    val filteredItems = if (searchQuery.isEmpty()) {
        items
    } else {
        items.filter { it.original_title?.contains(searchQuery, ignoreCase = true) == true }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (filteredItems.isNotEmpty()) {
            LazyVerticalGrid(
                state = state,
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = filteredItems, key = { it.id!! }) { res ->
                    Box(
                        modifier = Modifier
                            .clickable {
                                splashViewModel.bottomTabsVisible = false
                                val movieJson = Uri.encode(Gson().toJson(res))
                                navController.navigate("${Constants.MovieDetail}/$movieJson")
                            }
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Black.copy(alpha = 0.5F))
                            .width(100.dp)
                            .height(250.dp)) {
                        Image(
                            modifier = Modifier.fillMaxSize(), painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("${ApiService.IMAGE_URL}${res?.poster_path}")
                                    .placeholder(R.drawable.ic_loading).crossfade(true).build()
                            ), contentDescription = "", contentScale = ContentScale.Crop
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = res?.original_title ?: "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .background(Color.Black.copy(alpha = 0.6f)),
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
                if (isLoadingMore && searchQuery.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (viewModel.topRatedMovies.value.isLoading) {
                    CircularProgressIndicator()
                } else if (viewModel.res.value.error.isNotEmpty()) {
                    Text(text = viewModel.topRatedMovies.value.error)
                } else if (searchQuery.isNotEmpty()) {
                    Text(text = "No movies found for \"$searchQuery\"")
                }
            }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun SettingsScreen(context: Context, rootNavController: NavController) {
    val authManager = remember { AuthManager() }
    val coroutineScope = rememberCoroutineScope()

    LazyColumn {
        arrayListOf("Privacy", "About us", "Version 1.0", "Logout").let { list ->
            items(list.size) { index ->
                Text(
                    text = list[index], modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            coroutineScope.launch {
                                if (list[index] == "Logout") {
                                    authManager.logout()
                                    rootNavController.navigate(Constants.Login) {
                                        popUpTo(Constants.MainPage) { inclusive = true }
                                    }
                                }
                            }
                            Extensions.mToast("Clicked: " + list[index], context)
                        }
                        .padding(16.dp))
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
    viewModel: MovieViewModel = hiltViewModel(),
    searchQuery: String = ""
) {
    var pageNumber by remember { mutableStateOf(1) }
    var isLoadingMore by remember { mutableStateOf(false) }
    var showGenericError by remember { mutableStateOf(false) }
    // New state to hold all the loaded data
    var allMovies by remember { mutableStateOf<List<Movies.Results>>(emptyList()) }
    val listState = rememberLazyListState()
    val shouldStartPaginate = remember {
        derivedStateOf {
            (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -9) >= (listState.layoutInfo.totalItemsCount - 4)
        }
    }
    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value && !isLoadingMore && searchQuery.isEmpty()) {
            isLoadingMore = true
            pageNumber++
            viewModel.getMovies(pageNumber = pageNumber)
            isLoadingMore = false
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getMovies(pageNumber = pageNumber)
    }

    val filteredMovies = if (searchQuery.isEmpty()) {
        allMovies
    } else {
        allMovies.filter { it.original_title?.contains(searchQuery, ignoreCase = true) == true }
    }

    viewModel.res.value.apply {
        when {
            isLoading && pageNumber == 1 -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                    CircularProgressIndicator()
                }
            }

            error.isNotEmpty() -> {
                showGenericError = true
            }

            else -> {
                // Append new data to the existing list and fix duplicate keys
                if (data.isNotEmpty()) {
                    allMovies = (allMovies + data).distinctBy { it.id }
                }
                showGenericError = false
                
                if (filteredMovies.isNotEmpty()) {
                    LazyColumn(state = listState) {
                        items(
                            filteredMovies, key = {
                                it.id!!
                            }) { res ->
                            EachRow(res = res, navController, splashViewModel = splashViewModel)
                        }
                        if (isLoadingMore && searchQuery.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                        if (searchQuery.isNotEmpty()) {
                            Text(text = "No movies found for \"$searchQuery\"")
                        }
                    }
                }
            }
        }
        if (showGenericError) {
            GenericErrorScreen(onRetry = {
                pageNumber = 1
                allMovies = emptyList()
                viewModel.getMovies(pageNumber = pageNumber)
                showGenericError = false
            })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
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
                ShimmerDescriptionPage()
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
                            // .placeholder(R.drawable.ic_loading)
                            .crossfade(true).build()
                    ),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {

                                }) {
                            IconButton(
                                onClick = { 
                                    splashViewModel.bottomTabsVisible = true
                                    navController.popBackStack() 
                                },
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
                                    // .placeholder(R.drawable.ic_loading)
                                    .crossfade(true)
                                    // .transformations(CircleCropTransformation())
                                    .build()
                            ),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            style = TextStyle(fontSize = 16.sp),
                            color = Color.White,
                            text = data?.original_title ?: "",
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ExpandableText(data?.overview ?: "")

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

                        var showVideoPlayer by remember { mutableStateOf(false) }
                        var videoId by remember { mutableStateOf(0) }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            horizontal = 16.dp, vertical = 8.dp
                                        ), // optional padding for row
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Trending",
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier
                                            .background(
                                                Color.Yellow, shape = CircleShape
                                            )
                                            .clickable {
                                                showVideoPlayer = true
                                                videoId = data.id ?: 0
                                            }
                                            .padding(
                                                horizontal = 12.dp, vertical = 6.dp
                                            ) // background padding
                                    )
                                    Text(
                                        text = "Watch Now",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier
                                            .background(
                                                Color.Red, shape = CircleShape
                                            )
                                            .clickable {
                                                showVideoPlayer = true
                                                videoId = data.id ?: 0
                                            }
                                            .padding(
                                                horizontal = 12.dp, vertical = 6.dp
                                            ) // background padding
                                    )
                                }


                                Spacer(modifier = Modifier.width(4.dp)) // Add some space between text and icon
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp) // Adjust icon size as needed
                                )
                                if (showVideoPlayer) {
                                    LoadVideo(navController, viewModel = viewModel, data.id ?: 0)
                                }
                            }
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
                                        .defaultMinSize(100.dp, 200.dp)
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
                                                .height(200.dp)
                                        ) {
                                            Image(
                                                modifier = Modifier.fillMaxSize(),
                                                painter = rememberAsyncImagePainter(
                                                    model = ImageRequest.Builder(LocalContext.current)
                                                        .data("${ApiService.IMAGE_URL}${movie?.poster_path}")
                                                        //.placeholder(R.drawable.ic_loading)
                                                        .crossfade(true).build()
                                                ),
                                                contentDescription = "",
                                                contentScale = ContentScale.Crop
                                            )
                                            Text(
                                                maxLines = 2,
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
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun LoadVideo(navController: NavHostController, viewModel: MovieViewModel, id: Int) {
    var videoId by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showPlayer by remember { mutableStateOf(true) }
    LaunchedEffect(id) {
        viewModel.getVideoLink(id)
    }
    viewModel.movieLink.value.apply {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Center
            ) {
                CircularProgressIndicator()
            }
        }
        if (error.isNotEmpty()) {
            scope.launch {
                snackbarHostState.showSnackbar("Video Link not found")
            }
        }
        if (data != null) {
            videoId = data.results?.firstOrNull()?.key ?: ""
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Video Link not found")
            }
        }
    }
    when {
        videoId.isNotEmpty() && showPlayer -> {
            navController.navigate("${Constants.PlayVideo}/${videoId}")
            showPlayer = false

        }
    }
}

@Composable
private fun EachRow(
    res: Movies.Results, navController: NavHostController, splashViewModel: MainActivityViewModel
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                splashViewModel.bottomTabsVisible = false
                val movieJson = Uri.encode(Gson().toJson(res))
                navController.navigate("${Constants.MovieDetail}/$movieJson")
            }
            .padding(3.dp), elevation = CardDefaults.cardElevation(
        defaultElevation = 2.dp
    ), colors = CardDefaults.cardColors(
        containerColor = Color.White
    ), shape = RoundedCornerShape(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min) // Ensures the row fits its content
        ) {
            Image(
                contentScale = ContentScale.Crop,
                contentDescription = "",
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight(), // Fills the height of the row
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${ApiService.IMAGE_URL}${res.poster_path}")
                        .placeholder(R.drawable.ic_loading).crossfade(true)
                        // .transformations(CircleCropTransformation())
                        .build()
                )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(CenterVertically)
            ) {
                Text(
                    color = Color.Black,
                    text = res.original_title!!, style = TextStyle(
                        fontSize = 16.sp
                    ), textAlign = TextAlign.Justify
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    var isExpanded by remember { mutableStateOf(false) }
                    var isTextOverflowing by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),

                            text = res.overview!!.trim(),
                            color = Color.Black,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                            overflow = TextOverflow.Ellipsis,
                            onTextLayout = { result ->
                                if (!isExpanded) {
                                    isTextOverflowing = result.hasVisualOverflow
                                }
                            }
                        )
                    }


                    if (isTextOverflowing) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = if (isExpanded) "Read Less" else "Read More",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier
                                    .background(Color.Gray, shape = CircleShape)
                                    .clickable { isExpanded = !isExpanded }
                                    .padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}
