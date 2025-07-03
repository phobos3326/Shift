package com.example.shift

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

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

import dagger.hilt.android.AndroidEntryPoint
import androidx.core.net.toUri

import androidx.compose.runtime.getValue
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.shift.ui.ViewModel



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShiftTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


                    AppNav(
                        modifier = Modifier.padding(innerPadding)
                    )


                }
            }
        }
    }





    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UserListScreen(navController: NavController, viewModel: ViewModel = hiltViewModel()) {
        val lazyPagingItems = viewModel.users.collectAsLazyPagingItems()
        val context = LocalContext.current
        val snackHost = remember { SnackbarHostState() }



        Scaffold(
            snackbarHost = { SnackbarHost(snackHost) },
            topBar = {
                TopAppBar(
                    title = { Text("Пользователи") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { lazyPagingItems.refresh() }) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                }
            }
        ) { paddingValues ->

            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(lazyPagingItems.itemCount) { index ->

                    val user = lazyPagingItems[index]

                    if (user != null) {
                        ListItem(
                            /*headlineContent = { Text("user.fullName") },
                            supportingContent = { Text("user.address") },*/

                            headlineContent = { Text(user.fullName) },
                            supportingContent = { Text(user.address) },

                            leadingContent = {
                                Image(
                                    painter = rememberAsyncImagePainter(user.thumbnail),
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp)
                                )
                            },
                            modifier = Modifier.clickable {
                                navController.navigate("detail/${user.id}")
                            }
                        )
                        Divider()
                    }
                }

                lazyPagingItems.apply {
                    when (loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                            }
                        }
                        is LoadState.Error -> {
                            val e = loadState.append as LoadState.Error
                            item {
                                Text(
                                    "Ошибка загрузки: ${e.error.localizedMessage}",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    @Composable
    fun AppNav(modifier: Modifier = Modifier) {
        val navController = rememberNavController()
        val viewModel: ViewModel = hiltViewModel()
        NavHost(navController, startDestination = "list") {
            composable("list") { UserListScreen(navController) }
            composable("detail/{userId}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("userId") ?: return@composable
                UserDetailScreen(id, viewModel, navController )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UserDetailScreen(
        userId: String,
        viewModel: ViewModel = hiltViewModel(),
        navController: NavController
    ) {
        val user by viewModel.getUserById(userId).collectAsState(initial = null)
        val context = LocalContext.current

        if (user == null) {
            // Можно показать загрузку или плейсхолдер
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(user!!.fullName, fontSize = 24.sp, modifier = Modifier.padding(vertical = 8.dp)) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.TopCenter
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(user?.picture),
                            contentDescription = null,
                            modifier = Modifier
                                .size(128.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Text("Email: ${user?.email}", Modifier.clickable {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = "mailto:${user?.email}".toUri()
                            }
                            context.startActivity(intent)
                        })
                        Text("Телефон: ${user?.phone}", Modifier.clickable {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = "tel:${user?.phone}".toUri()
                            }
                            context.startActivity(intent)
                        })
                        Text(
                            "Адрес: ${user?.address}",
                            Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        data = "geo:0,0?q=${Uri.encode(user?.address)}".toUri()
                                    }
                                    context.startActivity(intent)
                                }
                        )
                    }
                }
            }
        }
    }



    @Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShiftTheme {

    }
}
}