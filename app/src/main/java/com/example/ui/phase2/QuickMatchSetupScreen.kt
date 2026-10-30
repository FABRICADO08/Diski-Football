package com.example.ui.phase2

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stadium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TeamEntity
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.DiskiGold
import com.example.ui.theme.PitchGreen
import com.example.ui.theme.PitchGreenDark
import com.example.ui.theme.RatingHigh
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardHighlight
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun QuickMatchSetupScreen(
    allTeams: List<TeamEntity>,
    onBack: () -> Unit,
    onSimulateQuickMatch: (homeTeamId: String, awayTeamId: String) -> Unit,
    onPlayQuickMatch: (homeTeamId: String, awayTeamId: String) -> Unit = { _, _ -> },
    onPlayPenaltyShootout: (homeTeamId: String, awayTeamId: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var homeTeamId by remember { mutableStateOf("kaizer_chiefs") }
    var awayTeamId by remember { mutableStateOf("orlando_pirates") }
    var halfLength by remember { mutableStateOf("5 min") }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeOfDay by remember { mutableStateOf("Night (Floodlights)") }

    val homeTeam = allTeams.find { it.id == homeTeamId } ?: allTeams.firstOrNull()
    val awayTeam = allTeams.find { it.id == awayTeamId } ?: allTeams.getOrNull(1)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("quick_match_screen"),
        containerColor = SurfaceDark,
        bottomBar = {
            Surface(
                color = SurfaceCard,
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 8.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onPlayQuickMatch(homeTeamId, awayTeamId) },
                            modifier = Modifier
                                .weight(1.2f)
                                .testTag("btn_play_pitch_quick_match"),
                            colors = ButtonDefaults.buttonColors(containerColor = DiskiGold),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = SurfaceDark, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Play Pitch", color = SurfaceDark, fontWeight = FontWeight.Black, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { onPlayPenaltyShootout(homeTeamId, awayTeamId) },
                            modifier = Modifier
                                .weight(1.1f)
                                .testTag("btn_quick_penalties"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B0FF)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Penalties", color = Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { onSimulateQuickMatch(homeTeamId, awayTeamId) },
                            modifier = Modifier
                                .weight(0.9f)
                                .testTag("btn_kickoff_quick_match"),
                            colors = ButtonDefaults.buttonColors(containerColor = PitchGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Simulate", color = DiskiGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
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
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_quick_match")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text("QUICK MATCH / EXHIBITION", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text("Set up your custom South African football clash", fontSize = 11.sp, color = DiskiGold)
                    }
                }
            }

            // Quick Iconic Derbies Chips
            item {
                Text("ICONIC MZANSI RIVALRIES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DiskiGold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DerbyChip(label = "Soweto Derby", isSelected = homeTeamId == "kaizer_chiefs" && awayTeamId == "orlando_pirates") {
                        homeTeamId = "kaizer_chiefs"
                        awayTeamId = "orlando_pirates"
                    }
                    DerbyChip(label = "Tshwane Derby", isSelected = homeTeamId == "mamelodi_sundowns" && awayTeamId == "supersport_united") {
                        homeTeamId = "mamelodi_sundowns"
                        awayTeamId = "supersport_united"
                    }
                    DerbyChip(label = "Cape Town Derby", isSelected = homeTeamId == "cape_town_city" && awayTeamId == "cape_town_spurs") {
                        homeTeamId = "cape_town_city"
                        awayTeamId = "cape_town_spurs"
                    }
                }
            }

            // Matchup Display Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = PitchGreenDark),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DiskiGold)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ClubBadgeBlock(team = homeTeam, label = "HOME")
                            Text("VS", fontWeight = FontWeight.Black, fontSize = 24.sp, color = DiskiGold)
                            ClubBadgeBlock(team = awayTeam, label = "AWAY")
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Stadium, contentDescription = null, tint = DiskiGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Venue: ${homeTeam?.stadiumName ?: "South African Stadium"}",
                                fontSize = 12.sp,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Match Settings
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("MATCH SETTINGS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DiskiGold)

                        Text("Half Length", fontSize = 11.sp, color = TextSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("3 min", "5 min", "10 min").forEach { len ->
                                FilterChip(
                                    selected = halfLength == len,
                                    onClick = { halfLength = len },
                                    label = { Text(len) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PitchGreen,
                                        selectedLabelColor = DiskiGold,
                                        containerColor = SurfaceCardHighlight,
                                        labelColor = TextSecondary
                                    )
                                )
                            }
                        }

                        Text("Match Difficulty", fontSize = 11.sp, color = TextSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Easy", "Medium", "Hard", "Legendary").forEach { diff ->
                                FilterChip(
                                    selected = difficulty == diff,
                                    onClick = { difficulty = diff },
                                    label = { Text(diff) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PitchGreen,
                                        selectedLabelColor = DiskiGold,
                                        containerColor = SurfaceCardHighlight,
                                        labelColor = TextSecondary
                                    )
                                )
                            }
                        }

                        Text("Stadium Atmosphere", fontSize = 11.sp, color = TextSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Night (Floodlights)", "Twilight Sunset", "Sunny Afternoon").forEach { time ->
                                FilterChip(
                                    selected = timeOfDay == time,
                                    onClick = { timeOfDay = time },
                                    label = { Text(time, fontSize = 11.sp) },
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

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun DerbyChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) PitchGreen else SurfaceCard,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, DiskiGold) else null,
        modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) DiskiGold else TextSecondary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun ClubBadgeBlock(team: TeamEntity?, label: String) {
    val pColor = try {
        Color(android.graphics.Color.parseColor(team?.primaryColorHex ?: "#FFD700"))
    } catch (e: Exception) { DiskiGold }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(pColor)
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = team?.abbreviation?.take(3) ?: "ZA", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = team?.shortName ?: "Club", fontWeight = FontWeight.Black, fontSize = 14.sp, color = TextPrimary)
        Text(text = "${team?.ratingOverall ?: 75} OVR", fontSize = 11.sp, color = RatingHigh, fontWeight = FontWeight.Bold)
    }
}
