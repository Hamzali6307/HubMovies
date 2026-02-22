
package com.hamy.hubmovies.ui.screens.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.hamy.hubmovies.R
import com.hamy.hubmovies.ui.screens.widgets.MainScreen
import com.hamy.hubmovies.ui.theme.HubMoviesTheme
import com.hamy.hubmovies.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class MainActivity : ComponentActivity() {
    private val splashViewModel: MainActivityViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = ContextCompat.getColor(this, R.color.btn_back)
        setContent {
            val navController = rememberNavController()
            installSplashScreen().apply {
                setKeepOnScreenCondition { splashViewModel.isLoading.value }
            }
            HubMoviesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray) // Optional fallback background color
                    ) {
                        val image: Painter =
                            painterResource(id = R.drawable.back) // Reference your drawable image here
                        Image(
                            painter = image,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().blur(radius = 10.dp),
                            contentScale = ContentScale.Crop // Scales the image to cover the box area
                        )
                    }
                    AppNavigation(navController,splashViewModel)
                }
            }
        }
    }
}

@Composable
fun SignUpScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var showEmailError by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(false) }
    var showConfirmPasswordError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val authManager = remember { AuthManager() }
    val context = LocalContext.current

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(padding)
                .background(Color.LightGray) // Optional fallback background color
        ) {
            val image: Painter =
                painterResource(id = R.drawable.back) // Reference your drawable image here
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().blur(radius = 10.dp),
                contentScale = ContentScale.Crop // Scales the image to cover the box area
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Circular Image with Border",
                modifier = Modifier
                    .size(100.dp) // Adjust the size as needed
                    .clip(CircleShape)
                    .border(2.dp, Color.Black) // Add a black border of 2 dp
            )
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp, 0.dp, 10.dp),
                text = "SignUp",
                fontSize = TextUnit(30f, TextUnitType.Sp),
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(FocusRequester())
                    .padding(30.dp, 0.dp, 30.dp, 10.dp),

                singleLine = true,
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                isError = showEmailError
            )
            if (showEmailError) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 0.dp, 0.dp, 0.dp),
                    text = "Invalid email address",
                    color = Color.Red,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(FocusRequester())
                    .padding(30.dp, 0.dp, 30.dp, 10.dp),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Lock else Icons.Outlined.Lock,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = showPasswordError
            )
            if (showPasswordError) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 0.dp, 0.dp, 0.dp),
                    text = "Password must be at least 6 characters",
                    color = Color.Red,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(FocusRequester())
                    .padding(30.dp, 0.dp, 30.dp, 10.dp),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Lock else Icons.Outlined.Lock,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = showConfirmPasswordError
            )
            if (showConfirmPasswordError) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 0.dp, 0.dp, 0.dp),
                    text = "Passwords do not match",
                    color = Color.Red,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp, 30.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.btn_back),
                    contentColor = Color.White,
                ),
                enabled = !isLoading,
                onClick = {
                    showEmailError = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    showPasswordError = password.length < 6
                    showConfirmPasswordError = password != confirmPassword
                    if (!showEmailError && !showPasswordError && !showConfirmPasswordError) {
                        coroutineScope.launch {
                            isLoading = true
                            try {
                                val successful = authManager.createUser(email, password)
                                if (successful) {
                                    navController.navigate(Constants.MainPage) {
                                        popUpTo(Constants.Login) { inclusive = true }
                                        popUpTo(Constants.Signup) { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(context, "Sign up failed", Toast.LENGTH_SHORT).show()
                                }
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                }) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Sign Up")
                }
            }
            Text(
                modifier = Modifier.clickable {
                    navController.navigate(Constants.Login) {
                        popUpTo(Constants.Login) { inclusive = true }
                    }
                },
                text = "Already Have Account!",
                color = Color.White,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
@Composable
fun LoginScreen(
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showEmailError by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val authManager = remember { AuthManager() }
    val context = LocalContext.current

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(padding)
                .background(Color.LightGray) // Optional fallback background color
        ) {
            val image: Painter =
                painterResource(id = R.drawable.back) // Reference your drawable image here
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(radius = 10.dp),
                painter = image,
                contentDescription = null,
                contentScale = ContentScale.Crop // Scales the image to cover the box area
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Circular Image with Border",
                modifier = Modifier
                    .size(100.dp) // Adjust the size as needed
                    .clip(RectangleShape)
                    .border(2.dp, Color.Black) // Add a black border of 2 dp
            )
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp, 0.dp, 10.dp),
                text = "Login",
                fontSize = TextUnit(30f, TextUnitType.Sp),
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )

            OutlinedTextField(
                modifier = Modifier
                    .focusRequester(FocusRequester())
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp, 30.dp, 10.dp),
                value = email,
                singleLine = true,
                onValueChange = { email = it },
                label = { Text("Email") },
                isError = showEmailError
            )
            if (showEmailError) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 0.dp, 0.dp, 0.dp),
                    text = "Invalid email address",
                    color = Color.Red,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier
                    .focusRequester(FocusRequester())
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp, 30.dp, 10.dp),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Lock else Icons.Outlined.Lock,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                value = password,
                singleLine = true,
                onValueChange = { password = it },
                label = { Text("Password") },
                isError = showPasswordError
            )
            if (showPasswordError) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 0.dp, 0.dp, 0.dp),
                    text = "Password cannot be empty",
                    color = Color.Red,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp, 0.dp, 30.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.btn_back),
                    contentColor = Color.White
                ),
                enabled = !isLoading,
                onClick = {
                    showEmailError = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    showPasswordError = password.isBlank()
                    if (!showEmailError && !showPasswordError) {
                        coroutineScope.launch {
                            isLoading = true
                            try {
                                val successful = authManager.signIn(email, password)
                                if (successful) {
                                    navController.navigate(Constants.MainPage) {
                                        popUpTo(Constants.Login) { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                                }
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                }) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Login")
                }
            }
            Text(
                modifier = Modifier.clickable {
                    navController.navigate(Constants.Signup) {
                        popUpTo(Constants.Signup) { inclusive = true }
                    }
                },
                text = "Register Account!",
                color = Color.White,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, splashViewModel: MainActivityViewModel) {
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) Constants.MainPage else Constants.Login

    navController.apply {
        NavHost(navController = this, startDestination = startDestination) {
            composable(Constants.Login) { LoginScreen(this@apply) }  // Start with LoginScreen
            composable(Constants.Signup) { SignUpScreen(this@apply) }  // Start with LoginScreen
            composable(Constants.MainPage) { MainScreen(this@apply,splashViewModel) }  // Navigate to MainScreen
        }
    }
}
