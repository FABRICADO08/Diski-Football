package com.example.ui.online

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MatchRoom
import com.example.data.model.TeamEntity
import com.example.ui.components.DiskiClubBadge
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.DiskiGold
import com.example.ui.theme.PitchGreen
import com.example.ui.theme.PitchGreenDark
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardHighlight
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.google.firebase.auth.FirebaseUser

import com.example.data.model.UserProfile

@Composable
fun OnlineMultiplayerLobbyScreen(
    currentUser: FirebaseUser?,
    userProfile: UserProfile? = null,
    currentRoom: MatchRoom?,
    isLoading: Boolean,
    errorMessage: String?,
    allTeams: List<TeamEntity>,
    onSignInWithGoogle: () -> Unit,
    onCreateRoom: (hostTeam: TeamEntity, gamerTag: String) -> Unit,
    onJoinRoom: (roomCode: String, guestTeam: TeamEntity, gamerTag: String) -> Unit,
    onCancelRoom: () -> Unit,
    onBack: () -> Unit,
    onDismissError: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        if (currentRoom != null) {
            onCancelRoom()
        } else {
            onBack()
        }
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0 = Host, 1 = Join
    var selectedTeamId by remember { mutableStateOf("kaizer_chiefs") }
    var gamerTag by remember { mutableStateOf(currentUser?.displayName ?: "Mzansi Striker") }
    var inputRoomCode by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current

    val selectedTeam = allTeams.find { it.id == selectedTeamId } ?: allTeams.firstOrNull()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("online_multiplayer_lobby"),
        containerColor = SurfaceDark
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_online_lobby")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Wifi, contentDescription = null, tint = DiskiGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ONLINE 1v1 MULTIPLAYER",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "Play real-time penalty shootouts across 2 separate Android phones",
                            fontSize = 11.sp,
                            color = DiskiGold
                        )
                    }
                }
            }

            // Error banner if any
            if (errorMessage != null) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0x33FF1744),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF1744)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = errorMessage, color = Color.White, fontSize = 12.sp, modifier = Modifier.weight(1f))
                            Button(
                                onClick = onDismissError,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x66FF1744)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Dismiss", fontSize = 10.sp, color = Color.White)
                            }
                        }
                    }
                }
            }

            // Google Auth Status Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(if (currentUser != null) PitchGreen else SurfaceCardHighlight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = if (currentUser != null) DiskiGold else TextMuted
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (currentUser != null) (currentUser.displayName ?: "Authenticated Player") else "Guest Mode (Offline)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = if (currentUser != null) "Ready for Online Cloud Matchmaking" else "Sign in with Google to play across 2 phones",
                                    fontSize = 10.sp,
                                    color = if (currentUser != null) AccentCyan else TextSecondary
                                )
                                if (currentUser != null && userProfile != null) {
                                    val total = userProfile.wins + userProfile.losses
                                    val winRate = if (total > 0) ((userProfile.wins.toFloat() / total) * 100).toInt() else 0
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = PitchGreenDark,
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Text(
                                            text = "🏆 ${userProfile.wins}W - ${userProfile.losses}L ($winRate%)",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DiskiGold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }

                        if (currentUser == null) {
                            Button(
                                onClick = onSignInWithGoogle,
                                colors = ButtonDefaults.buttonColors(containerColor = DiskiGold),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("btn_google_signin")
                            ) {
                                Text("Sign In", color = SurfaceDark, fontWeight = FontWeight.Black, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Active Room Hosting Status View
            if (currentRoom != null && currentRoom.status == "WAITING_FOR_PLAYER") {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = PitchGreenDark),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(2.dp, DiskiGold)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "MATCH ROOM CREATED!", fontSize = 16.sp, fontWeight = FontWeight.Black, color = DiskiGold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Share this 6-character code with your friend on Phone 2:", fontSize = 12.sp, color = TextPrimary, textAlign = TextAlign.Center)

                            Spacer(modifier = Modifier.height(14.dp))

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SurfaceDark,
                                border = androidx.compose.foundation.BorderStroke(1.dp, DiskiGold),
                                modifier = Modifier.clickable {
                                    clipboardManager.setText(AnnotatedString(currentRoom.roomCode))
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = currentRoom.roomCode,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Black,
                                        color = DiskiGold,
                                        letterSpacing = 4.sp
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = AccentCyan, modifier = Modifier.size(20.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(color = DiskiGold, modifier = Modifier.size(20.dp), strokeWidth = 2.5.dp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Waiting for Player 2 to join with code...",
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = onCancelRoom,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FF1744)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().testTag("btn_cancel_room")
                            ) {
                                Text("Cancel Room", color = Color(0xFFFF5252), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                // Host / Join Match Tabs
                item {
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = SurfaceCard,
                        contentColor = DiskiGold,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = DiskiGold,
                                height = 3.dp
                            )
                        }
                    ) {
                        Tab(
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 },
                            text = { Text("HOST MATCH (PHONE 1)", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                        )
                        Tab(
                            selected = selectedTabIndex == 1,
                            onClick = { selectedTabIndex = 1 },
                            text = { Text("JOIN MATCH (PHONE 2)", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                        )
                    }
                }

                // Choose Club Section
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(text = "1. CHOOSE YOUR SOUTH AFRICAN CLUB", fontSize = 12.sp, fontWeight = FontWeight.Black, color = DiskiGold)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (selectedTeam != null) {
                                        DiskiClubBadge(team = selectedTeam, size = 44.dp)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(text = selectedTeam?.name ?: "", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = "Venue: ${selectedTeam?.stadiumName ?: ""}", fontSize = 10.sp, color = TextSecondary)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Quick Select:", fontSize = 11.sp, color = TextSecondary)

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("kaizer_chiefs", "orlando_pirates", "mamelodi_sundowns", "cape_town_city").forEach { id ->
                                    val team = allTeams.find { it.id == id }
                                    FilterChip(
                                        selected = selectedTeamId == id,
                                        onClick = { selectedTeamId = id },
                                        label = { Text(team?.shortName ?: id, fontSize = 10.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = PitchGreen,
                                            selectedLabelColor = DiskiGold,
                                            containerColor = SurfaceCardHighlight,
                                            labelColor = TextSecondary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // GamerTag and Action Section
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(text = "2. PLAYER DETAILS", fontSize = 12.sp, fontWeight = FontWeight.Black, color = DiskiGold)

                            OutlinedTextField(
                                value = gamerTag,
                                onValueChange = { gamerTag = it },
                                label = { Text("Your GamerTag") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("input_gamertag"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = DiskiGold,
                                    unfocusedBorderColor = SurfaceCardHighlight,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            if (selectedTabIndex == 1) {
                                OutlinedTextField(
                                    value = inputRoomCode,
                                    onValueChange = { inputRoomCode = it.uppercase() },
                                    label = { Text("Match Room Code (e.g. DK-1234)") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth().testTag("input_room_code"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = AccentCyan,
                                        unfocusedBorderColor = SurfaceCardHighlight,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            if (selectedTabIndex == 0) {
                                Button(
                                    onClick = {
                                        if (selectedTeam != null) {
                                            onCreateRoom(selectedTeam, gamerTag)
                                        }
                                    },
                                    enabled = !isLoading,
                                    colors = ButtonDefaults.buttonColors(containerColor = DiskiGold),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .testTag("btn_host_create_room")
                                ) {
                                    if (isLoading) {
                                        CircularProgressIndicator(color = SurfaceDark, modifier = Modifier.size(20.dp))
                                    } else {
                                        Icon(imageVector = Icons.Default.SportsSoccer, contentDescription = null, tint = SurfaceDark)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("CREATE 1v1 MATCH ROOM", color = SurfaceDark, fontWeight = FontWeight.Black)
                                    }
                                }
                            } else {
                                Button(
                                    onClick = {
                                        if (selectedTeam != null && inputRoomCode.isNotBlank()) {
                                            onJoinRoom(inputRoomCode, selectedTeam, gamerTag)
                                        }
                                    },
                                    enabled = !isLoading && inputRoomCode.isNotBlank(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B0FF)),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .testTag("btn_join_room_submit")
                                ) {
                                    if (isLoading) {
                                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                                    } else {
                                        Text("CONNECT & KICK OFF", color = Color.White, fontWeight = FontWeight.Black)
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
