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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TeamEntity
import com.example.ui.theme.DiskiGold
import com.example.ui.theme.PitchGreen
import com.example.ui.theme.RatingHigh
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardHighlight
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TeamSelectScreen(
    pslTeams: List<TeamEntity>,
    nfdTeams: List<TeamEntity>,
    onBack: () -> Unit,
    onStartCareer: (managerName: String, teamId: String, difficulty: String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: PSL, 1: NFD
    var managerName by remember { mutableStateOf("Coach Mzansi") }
    var selectedDifficulty by remember { mutableStateOf("Medium") }
    var selectedTeamId by remember { mutableStateOf(pslTeams.firstOrNull()?.id ?: "mamelodi_sundowns") }

    val currentTeams = if (selectedTabIndex == 0) pslTeams else nfdTeams

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("team_select_screen"),
        containerColor = SurfaceDark,
        bottomBar = {
            Surface(
                color = SurfaceCard,
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 8.dp
            ) {
                Button(
                    onClick = {
                        onStartCareer(managerName.ifBlank { "Coach Mzansi" }, selectedTeamId, selectedDifficulty)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .testTag("btn_confirm_career_start"),
                    colors = ButtonDefaults.buttonColors(containerColor = PitchGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Begin Manager Career", color = DiskiGold, fontWeight = FontWeight.Black, fontSize = 14.sp)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_team_select")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text("NEW MANAGER CAREER", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text("Select your club and configure manager profile", fontSize = 11.sp, color = DiskiGold)
                    }
                }
            }

            // Manager Setup Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(text = "MANAGER PROFILE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DiskiGold)

                        OutlinedTextField(
                            value = managerName,
                            onValueChange = { managerName = it },
                            label = { Text("Manager Name", color = TextSecondary) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("input_manager_name"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = DiskiGold,
                                unfocusedBorderColor = SurfaceCardHighlight
                            )
                        )

                        Text(text = "GAMEPLAY DIFFICULTY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Easy", "Medium", "Hard", "Legendary").forEach { diff ->
                                FilterChip(
                                    selected = selectedDifficulty == diff,
                                    onClick = { selectedDifficulty = diff },
                                    label = { Text(diff, fontSize = 11.sp) },
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

            // League Selector
            item {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = SurfaceDark,
                    contentColor = DiskiGold,
                    indicator = { tabPositions ->
                        if (selectedTabIndex < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = DiskiGold
                            )
                        }
                    }
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = { Text("Betway Premiership (PSL)", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = { Text("Motsepe Championship (NFD)", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            item {
                Text(text = "CHOOSE YOUR CLUB", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DiskiGold)
            }

            items(currentTeams, key = { it.id }) { team ->
                val isSelected = selectedTeamId == team.id
                val pColor = try { Color(android.graphics.Color.parseColor(team.primaryColorHex)) } catch (e: Exception) { DiskiGold }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { selectedTeamId = team.id }
                        .testTag("select_team_${team.id}"),
                    colors = CardDefaults.cardColors(containerColor = if (isSelected) PitchGreen else SurfaceCard),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, DiskiGold) else null
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(pColor)
                                    .border(1.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = team.abbreviation.take(3), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = team.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                                Text(
                                    text = "${team.stadiumName} • Budget: R ${(team.transferBudget / 1_000_000)}M",
                                    fontSize = 11.sp,
                                    color = if (isSelected) DiskiGold else TextSecondary
                                )
                            }
                        }

                        Surface(shape = RoundedCornerShape(8.dp), color = RatingHigh.copy(alpha = 0.2f)) {
                            Text(
                                text = "${team.ratingOverall} OVR",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = RatingHigh,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
