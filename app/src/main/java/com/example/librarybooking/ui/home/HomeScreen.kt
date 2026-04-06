package com.example.librarybooking.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.librarybooking.R
import com.example.librarybooking.State
import com.example.librarybooking.models.Booth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenProfile: () -> Unit,
    onLogout: () -> Unit,
    onOpenBooking: (String) -> Unit
) {
    val homeView: HomeView = viewModel()
    val boothState by homeView.boothState.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showGuide by remember { mutableStateOf(false) }
    var showTerms by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Menu",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NavigationDrawerItem(
                        label = { Text("Profile") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            onOpenProfile()
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    NavigationDrawerItem(
                        label = { Text("Guide") },
                        selected = false,
                        onClick = {
                            showGuide = !showGuide
                        },
                        badge = {
                            Icon(
                                imageVector = if (showGuide) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (showGuide) "Collapse guide" else "Expand guide"
                            )
                        }
                    )

                    if (showGuide) {
                        Text(
                            text = "Select a booth, choose a valid date and time slot, and confirm your booking.",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    NavigationDrawerItem(
                        label = { Text("Terms and Conditions") },
                        selected = false,
                        onClick = {
                            showTerms = !showTerms
                        },
                        badge = {
                            Icon(
                                imageVector = if (showTerms) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (showTerms) "Collapse terms" else "Expand terms"
                            )
                        }
                    )

                    if (showTerms) {
                        Text(
                            text = "Here will be the terms and conditions.",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    NavigationDrawerItem(
                        label = { Text("Logout") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            onLogout()
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Library Booking") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open menu"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Choose a booth or study space",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                when (boothState) {
                    is State.Loading -> {
                        CircularProgressIndicator()
                    }

                    is State.Error -> {
                        Text(
                            text = (boothState as State.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    is State.Success -> {
                        val booths = (boothState as State.Success<List<Booth>>).data

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 24.dp)
                        ) {
                            items(booths) { booth ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { onOpenBooking(booth.name) }
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        val imageRes = when (booth.name) {
                                            "DHB A" -> R.drawable.dhb_a
                                            "DHB B" -> R.drawable.dhb_b
                                            "DHB C" -> R.drawable.dhb_c
                                            "DHB D" -> R.drawable.dhb_d
                                            "TG 1" -> R.drawable.tg_1
                                            "TG 2" -> R.drawable.tg_2
                                            "TG 3" -> R.drawable.tg_3
                                            else -> R.drawable.error
                                        }

                                        Image(
                                            painter = painterResource(id = imageRes),
                                            contentDescription = booth.name,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(150.dp)
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(
                                            text = booth.name,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text("Building: ${booth.building}")
                                        Text("Type: ${booth.type}")
                                        Text("Capacity: ${booth.capacity}")

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = "Tap to view availability",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }

                    else -> Unit
                }
            }
        }
    }
}