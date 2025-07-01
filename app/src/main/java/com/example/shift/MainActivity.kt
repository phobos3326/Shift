package com.example.shift

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.shift.ui.theme.ShiftTheme
import com.example.shift.ui.theme.UserViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShiftTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    /*Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )*/

                    AppNav(
                        modifier = Modifier.padding(innerPadding)
                    )


                }
            }
        }
    }


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(navController: NavController, viewModel: UserViewModel = hiltViewModel()) {
    val users by viewModel.users.collectAsState()
    val context = LocalContext.current
    val snackHost = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            snackHost.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackHost) },
        topBar = { TopAppBar(title = { Text("Пользователи") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.refresh() }) {
                Icon(Icons.Default.Refresh, contentDescription = null)
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(users) { user ->
                ListItem(
                   /* headlineContent = { Text(user.fullName) },
                    supportingContent = { Text(user.address) },*/
                    headlineContent = { Text("user.fullName") },
                    supportingContent = { Text("user.address") },
                    leadingContent = {
                        /*Image(
                            painter = rememberAsyncImagePainter(user.thumbnail),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )*/
                    },
                    modifier = Modifier.clickable {
                        navController.navigate("detail/${user.id}")
                    }
                )
                Divider()
            }
        }
    }
}

    @Composable
    fun AppNav(modifier: Modifier = Modifier) {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "list") {
            composable("list") { UserListScreen(navController) }
            composable("detail/{userId}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("userId") ?: return@composable
                UserDetailScreen(id)
            }
        }
    }

    @Composable
    fun UserDetailScreen(userId: String, viewModel: UserViewModel = hiltViewModel()) {
        val user = viewModel.users.collectAsState().value.find { it.id == userId } ?: return
        val context = LocalContext.current

        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(user.picture),
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(user.fullName, fontSize = 24.sp, modifier = Modifier.padding(vertical = 8.dp))
            Text("Email: ${user.email}", Modifier.clickable {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:${user.email}")
                }
                context.startActivity(intent)
            })
            Text("Телефон: ${user.phone}", Modifier.clickable {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${user.phone}")
                }
                context.startActivity(intent)
            })
            Text("Адрес: ${user.address}", Modifier.clickable {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("geo:0,0?q=${Uri.encode(user.address)}")
                }
                context.startActivity(intent)
            })
        }
    }



    @Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShiftTheme {
        Greeting("Android")
    }
}
}