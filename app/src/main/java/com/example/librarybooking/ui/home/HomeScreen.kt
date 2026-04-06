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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

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
                        .verticalScroll(rememberScrollState())
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
                            text = buildAnnotatedString {

                                fun bold(text: String) {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(text)
                                    }
                                }

                                fun header(text: String) {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    ) {
                                        append(text)
                                    }
                                }

                                header("How to Book Your Booth\n\n")

                                bold("1.")
                                append(" Choose the booth you want to book from the home screen.\n\n")

                                bold("2.")
                                append(" Tap on the booth card to open the booking page.\n\n")

                                bold("3.")
                                append(" Select a weekday from the available dates.\n\n")

                                bold("4.")
                                append(" Choose an available time slot for your booking.\n\n")

                                bold("5.")
                                append(" Press the Confirm Booking button.\n\n")

                                bold("6.")
                                append(" If the booking is successful, it will be saved and you will see it in your profile.\n\n")

                                header("Managing Your Bookings\n\n")

                                bold("1.")
                                append(" Open the menu and go to your Profile page.\n\n")

                                bold("2.")
                                append(" View your current bookings in the bookings section.\n\n")

                                bold("3.")
                                append(" To change a booking, press the Edit Booking button.\n\n")

                                bold("4.")
                                append(" To remove a booking, press the Cancel Booking button. If it disappeared from your profile, you successfully canceled it.\n\n")

                            },
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
                            text = buildAnnotatedString {

                                fun bold(text: String) {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(text)
                                    }
                                }
                                fun header(text: String) {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    ) {
                                        append(text)
                                    }
                                }


                                header("Booking a booth.\n")
                                bold("You")
                                append(" must book a booth before use.\n\n")

                                bold("You")
                                append(" can only book a booth when the Library is open.\n\n")

                                bold("You")
                                append(" can only book a booth for 1 session of up to 2 hours in a day.\n\n")

                                bold("You")
                                append(" can book up to a week in advance.\n\n")

                                bold("You")
                                append(" should arrive on time for your booking, if you do not arrive within the first fifteen minutes of your booking it may be cancelled to allow others to use the booth.\n\n")

                                append("If you want to extend your booking, and the booth is available, you can extend it at the Library Desk.\n\n")

                                append("If you are banned from using a booth your booking will be cancelled.\n\n")

                                header("Using a booth.\n")

                                append("Everyone using the booth must have a booth card issued to them.\n\n")

                                bold("You")
                                append(" must not give your booth card to anyone else and must return it at the end of a booking.\n\n")

                                bold("You")
                                append(" must have the booth pass on display in the booth.\n\n")

                                bold("You")
                                append(" must only use the booth for study purpose and behave appropriately.\n\n")

                                bold("You")
                                append(" must leave or extend your session when your booking ends.\n\n")

                                bold("You")
                                append(" must make sure the booth is left tidy, clean and fit for use.\n\n")

                                bold("You")
                                append(" must not eat in the booth.\n\n")

                                bold("You")
                                append(" must ensure all drinks are in containers with lids.\n\n")

                                bold("You")
                                append(" will show your College card when asked.\n\n")

                                bold("You")
                                append(" will not exceed the maximum number of users allowed in a booth.\n\n")

                                bold("You")
                                append(" will not leave a booked booth unoccupied for an extended length of time.\n\n")

                                bold("You")
                                append(" take responsibility for anything left unattended in a booth.\n\n")

                                bold("You")
                                append(" will return the Booth Pass and Booth Cards at the end of a booking.\n\n")

                                header("Being banned from a booth.\n")

                                append("If you do not follow the rules above, you may be banned from using the booths.\n\n")

                                append("If you are banned, a suspension is usually for one calendar month.\n\n")

                                bold("You")
                                append(" will receive a further ban of one calendar month if you use a booth while serving a ban.\n\n")

                                bold("You")
                                append(" will receive a further ban if you have been previously banned.\n\n")

                                bold("You")
                                append(" will be suspended for the rest of the academic year if you receive a third ban.\n\n")

                                bold("You")
                                append(" will also be banned if you have any item overdue from the Library.\n\n")

                                append("The Library reserves the right to impose a longer ban depending on behaviour.\n\n\n")

                                bold("You can appeal a ban by emailing")
                                append(" askalibrarian@bradfordcollege.ac.uk")
                            },
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