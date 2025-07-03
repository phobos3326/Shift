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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
                    title = { Text(stringResource(R.string.users)) },
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
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.user_info), fontSize = 24.sp) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.size(140.dp),
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(user!!.picture),
                        contentDescription = stringResource(R.string.user_photo),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = user!!.fullName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))


                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(stringResource(R.string.contact_info), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow(icon = Icons.Default.Email, label = "Email", value = user!!.email) {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = "mailto:${user!!.email}".toUri()
                            }
                            context.startActivity(intent)
                        }

                        InfoRow(icon = Icons.Default.Phone, label = stringResource(R.string.phone), value = user!!.phone) {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = getString(R.string.tel, user!!.phone).toUri()
                            }
                            context.startActivity(intent)
                        }

                        InfoRow(
                            icon = Icons.Default.Phone,
                            label = stringResource(R.string.mobile), value = user!!.cell) {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = getString(R.string.tel, user!!.cell).toUri()
                            }
                            context.startActivity(intent)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Address Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(stringResource(R.string.address), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = "geo:0,0?q=${Uri.encode(user!!.address)}".toUri()
                            }
                            context.startActivity(intent)
                        }) {
                            Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${user!!.address}, ${user!!.postcode}," +
                                       "${user!!.state}, ${user!!.country}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Additional info card
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(stringResource(R.string.more_info), style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow(label = stringResource(R.string.gender), value = user!!.gender)
                        InfoRow(label = stringResource(R.string.age), value = user!!.dobAge.toString())
                        InfoRow(label = stringResource(R.string.date_of_birth), value = user!!.dobDate)
                        InfoRow(label = stringResource(R.string.date_of_registration), value = user!!.registeredDate)
                        InfoRow(label = stringResource(R.string.time_zone), value = "${user!!.timezoneOffset} (${user!!.timezoneDescription})")
                        InfoRow(label = stringResource(R.string.id), value = "${user!!.idName ?: "-"} - ${user!!.idValue ?: "-"}")
                        InfoRow(label = stringResource(R.string.nationality), value = user!!.nat)
                        InfoRow(label = stringResource(R.string.coordinates), value = "${user!!.latitude}, ${user!!.longitude}")
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }




    @Composable
    fun InfoRow(
        icon: ImageVector? = null,
        label: String,
        value: String,
        onClick: (() -> Unit)? = null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .let {
                    if (onClick != null) it.clickable { onClick() } else it
                }
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(text = "$label:", fontWeight = FontWeight.Medium, modifier = Modifier.width(110.dp))
            Text(text = value, style = MaterialTheme.typography.bodyMedium)
        }
    }



    @Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShiftTheme {

    }
}
}