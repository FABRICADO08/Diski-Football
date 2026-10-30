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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CareerSaveEntity
import com.example.data.model.MatchEntity
import com.example.data.model.MatchStatus
import com.example.data.model.PlayerEntity
import com.example.data.model.TeamEntity
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentRed
import com.example.ui.theme.DiskiGold
import com.example.ui.theme.PitchGreen
import com.example.ui.theme.PitchGreenDark
import com.example.ui.theme.RatingHigh
import com.example.ui.theme.RatingMed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardHighlight
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ManagerDashboardScreen(
    career: CareerSaveEntity,
    team: TeamEntity,
    squad: List<PlayerEntity>,
    nextMatch: MatchEntity?,
    marketPlayers: List<PlayerEntity>,
    onBack: () -> Unit,
    onSimulateNextMatch: () -> Unit,
    onPlayNextMatch: () -> Unit = {},
    onChangeFormation: (String) -> Unit,
    onSwapStarter: (String, String) -> Unit,
    onBuyPlayer: (PlayerEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Overview, 1: Tactics, 2: Squad, 3: Transfers
    var selectedStarterForSwap by remember { mutableStateOf<PlayerEntity?>(null) }
    var showPressConference by remember { mutableStateOf(false) }
    var conferenceBonus by remember { mutableIntStateOf(0) }

    val primaryColor = try {
        Color(android.graphics.Color.parseColor(team.primaryColorHex))
    } catch (e: Exception) {
        DiskiGold
    }

    val secondaryColor = try {
        Color(android.graphics.Color.parseColor(team.secondaryColorHex))
    } catch (e: Exception) {
        Color.White
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("manager_dashboard_screen"),
        containerColor = SurfaceDark
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_manager")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(primaryColor)
                            .border(1.dp, secondaryColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = team.abbreviation.take(3), color = secondaryColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = team.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                        Text(text = "Boss ${career.managerName} • ${team.league.name} 2026", fontSize = 11.sp, color = DiskiGold)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PitchGreen.copy(alpha = 0.3f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PitchGreen)
                ) {
                    Text(
                        text = "GW ${career.currentGameWeek} / 30",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DiskiGold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Manager Mode Sub-Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SurfaceDark,
                contentColor = DiskiGold,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = DiskiGold
                        )
                    }
                }
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Overview", fontSize = 12.sp, fontWeight = FontWeight.Bold) }, modifier = Modifier.testTag("tab_mgr_overview"))
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Tactics", fontSize = 12.sp, fontWeight = FontWeight.Bold) }, modifier = Modifier.testTag("tab_mgr_tactics"))
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Squad", fontSize = 12.sp, fontWeight = FontWeight.Bold) }, modifier = Modifier.testTag("tab_mgr_squad"))
                Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Transfers", fontSize = 12.sp, fontWeight = FontWeight.Bold) }, modifier = Modifier.testTag("tab_mgr_transfers"))
            }

            when (selectedTab) {
                0 -> {
                    // Overview
                    OverviewTabContent(
                        career = career,
                        team = team,
                        nextMatch = nextMatch,
                        conferenceBonus = conferenceBonus,
                        onOpenPressConference = { showPressConference = true },
                        onSimulateMatch = onSimulateNextMatch,
                        onPlayMatch = onPlayNextMatch
                    )
                }
                1 -> {
                    // Tactics Board
                    TacticsTabContent(
                        squad = squad,
                        currentFormation = career.formation,
                        onChangeFormation = onChangeFormation
                    )
                }
                2 -> {
                    // Squad Management
                    SquadTabContent(
                        squad = squad,
                        selectedStarterForSwap = selectedStarterForSwap,
                        onSelectStarter = { selectedStarterForSwap = it },
                        onSelectSubToSwap = { sub ->
                            val starter = selectedStarterForSwap
                            if (starter != null) {
                                onSwapStarter(starter.id, sub.id)
                                selectedStarterForSwap = null
                            }
                        }
                    )
                }
                3 -> {
                    // Transfer Market
                    TransfersTabContent(
                        marketPlayers = marketPlayers,
                        budgetRemaining = career.transferBudgetRemaining,
                        onBuyPlayer = onBuyPlayer
                    )
                }
            }
        if (showPressConference) {
            val oppId = nextMatch?.let {
                if (it.homeTeamId == team.id) it.awayTeamId else it.homeTeamId
            } ?: "rival_fc"
            val oppDisplayName = oppId.replace("_", " ").split(" ").joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
            val mockOpponent = TeamEntity(
                id = oppId,
                name = oppDisplayName,
                shortName = oppDisplayName,
                abbreviation = oppId.take(3).uppercase(),
                nickname = "Rivals",
                league = team.league,
                stadiumName = "Stadium",
                stadiumCapacity = 50000,
                city = "Johannesburg",
                province = "Gauteng",
                primaryColorHex = "#FF0000",
                secondaryColorHex = "#FFFFFF",
                kitHomeJerseyHex = "#FF0000",
                kitHomeShortsHex = "#000000",
                kitAwayJerseyHex = "#FFFFFF",
                kitAwayShortsHex = "#FFFFFF",
                ratingOverall = 78,
                ratingAttack = 77,
                ratingMidfield = 78,
                ratingDefense = 77,
                transferBudget = 10000000L,
                wageBudget = 200000L,
                prestige = 4
            )

            PressConferenceDialog(
                userTeam = team,
                opponentTeam = mockOpponent,
                derbyName = if (team.shortName.contains("Chiefs", true) || team.shortName.contains("Pirates", true)) "Soweto Derby" else "Showdown Match",
                onFinishConference = { bonus, _ ->
                    conferenceBonus = bonus
                    showPressConference = false
                },
                onDismiss = { showPressConference = false }
            )
        }
    }
}
}

@Composable
private fun OverviewTabContent(
    career: CareerSaveEntity,
    team: TeamEntity,
    nextMatch: MatchEntity?,
    conferenceBonus: Int,
    onOpenPressConference: () -> Unit,
    onSimulateMatch: () -> Unit,
    onPlayMatch: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Next Match Hero Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = PitchGreenDark),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DiskiGold)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(shape = RoundedCornerShape(6.dp), color = DiskiGold) {
                            Text(
                                text = "UPCOMING FIXTURE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SurfaceDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "${nextMatch?.stage ?: "GameWeek ${career.currentGameWeek}"} • ${team.stadiumName}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = team.shortName, fontWeight = FontWeight.Black, fontSize = 16.sp, color = TextPrimary)
                            Text(text = "Rating: ${team.ratingOverall}", fontSize = 11.sp, color = DiskiGold)
                        }

                        Text(text = "VS", fontWeight = FontWeight.Black, fontSize = 20.sp, color = DiskiGold)

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val opponentName = if (nextMatch != null) {
                                if (nextMatch.homeTeamId == team.id) nextMatch.awayTeamId.replace("_", " ").uppercase()
                                else nextMatch.homeTeamId.replace("_", " ").uppercase()
                            } else "RIVALS"
                            Text(text = opponentName, fontWeight = FontWeight.Black, fontSize = 16.sp, color = TextPrimary)
                            Text(text = "Opponent", fontSize = 11.sp, color = TextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onPlayMatch,
                            modifier = Modifier
                                .weight(1.3f)
                                .testTag("btn_play_next_match"),
                            colors = ButtonDefaults.buttonColors(containerColor = DiskiGold),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = SurfaceDark, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Play (2D Pitch)", color = SurfaceDark, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        }

                        Button(
                            onClick = onSimulateMatch,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_simulate_next_match"),
                            colors = ButtonDefaults.buttonColors(containerColor = PitchGreen),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Simulate", color = DiskiGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Finances & Club Status
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "CLUB FINANCES & STATUS", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DiskiGold)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        FinanceItem(label = "Transfer Budget", value = "R ${(career.transferBudgetRemaining / 1_000_000)}M", color = DiskiGold)
                        FinanceItem(label = "Weekly Wage Bill", value = "R ${(team.wageBudget / 1_000)}k/wk", color = TextPrimary)
                        FinanceItem(label = "Job Security", value = "92% (Secure)", color = RatingHigh)
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(text = "Squad Morale", fontSize = 11.sp, color = TextSecondary)
                    LinearProgressIndicator(
                        progress = { career.teamMorale / 100f },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = RatingHigh,
                        trackColor = SurfaceDark
                    )
                }
            }
        }

        // Board Objectives
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "BOARD OBJECTIVES (SEASON 2026)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DiskiGold)

                    ObjectiveRow(text = "Domestic: Finish in Top 4 to qualify for CAF or MTN8", status = "In Progress")
                    ObjectiveRow(text = "Cup: Reach at least the Semi-Finals of the Nedbank Cup", status = "Pending Draw")
                    ObjectiveRow(text = "Financial: Maintain a minimum R 10M transfer reserve", status = "On Track")
                }
            }
        }
    }
}

@Composable
private fun FinanceItem(label: String, value: String, color: Color) {
    Column {
        Text(text = label, fontSize = 10.sp, color = TextMuted)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun ObjectiveRow(text: String, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceCardHighlight)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = DiskiGold, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, fontSize = 11.sp, color = TextPrimary)
        }
        Text(text = status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = RatingHigh)
    }
}

@Composable
private fun TacticsTabContent(
    squad: List<PlayerEntity>,
    currentFormation: String,
    onChangeFormation: (String) -> Unit
) {
    val formations = listOf("4-3-3", "4-2-3-1", "3-5-2")
    var playStyle by remember { mutableStateOf("Diski Possession (Flavour)") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(text = "SELECT FORMATION", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DiskiGold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                formations.forEach { formation ->
                    FilterChip(
                        selected = currentFormation == formation,
                        onClick = { onChangeFormation(formation) },
                        label = { Text(formation, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PitchGreen,
                            selectedLabelColor = DiskiGold,
                            containerColor = SurfaceCard,
                            labelColor = TextSecondary
                        )
                    )
                }
            }
        }

        // Pitch Diagram
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PitchGreenDark),
                border = androidx.compose.foundation.BorderStroke(2.dp, PitchGreen)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    // Center Circle & Pitch Lines
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .align(Alignment.Center)
                            .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .align(Alignment.Center)
                            .background(Color.White.copy(alpha = 0.3f))
                    )

                    // Starter player nodes positioned by formation
                    val starters = squad.filter { it.isStartingEleven }.take(11)

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Attack line (ST, LW, RW)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            starters.filter { it.position.group == "Attacker" }.take(3).forEach { p ->
                                PitchPlayerBadge(player = p)
                            }
                        }

                        // Midfield line (CAM, CM, CDM)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            starters.filter { it.position.group == "Midfielder" }.take(4).forEach { p ->
                                PitchPlayerBadge(player = p)
                            }
                        }

                        // Defense line (LB, CB, RB)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            starters.filter { it.position.group == "Defender" }.take(4).forEach { p ->
                                PitchPlayerBadge(player = p)
                            }
                        }

                        // Goalkeeper
                        starters.find { it.position.group == "Goalkeeper" }?.let { gk ->
                            PitchPlayerBadge(player = gk)
                        }
                    }
                }
            }
        }

        // Tactical Style
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "MZANSI PLAY STYLE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DiskiGold)
                    val styles = listOf("Diski Possession (Flavour)", "Direct Counter-Attack", "High Gegenpress")
                    styles.forEach { style ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (playStyle == style) PitchGreen else SurfaceCardHighlight)
                                .clickable { playStyle = style }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = style, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (playStyle == style) DiskiGold else TextPrimary)
                            if (playStyle == style) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = DiskiGold, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PitchPlayerBadge(player: PlayerEntity) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(DiskiGold)
                .border(1.dp, SurfaceDark, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "${player.jerseyNumber}", fontSize = 11.sp, fontWeight = FontWeight.Black, color = SurfaceDark)
        }
        Text(
            text = player.name.split(" ").lastOrNull() ?: player.name,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1
        )
    }
}

@Composable
private fun SquadTabContent(
    squad: List<PlayerEntity>,
    selectedStarterForSwap: PlayerEntity?,
    onSelectStarter: (PlayerEntity) -> Unit,
    onSelectSubToSwap: (PlayerEntity) -> Unit
) {
    val starters = squad.filter { it.isStartingEleven }
    val subs = squad.filter { !it.isStartingEleven }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (selectedStarterForSwap != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DiskiGold.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DiskiGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null, tint = DiskiGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Selected ${selectedStarterForSwap.name}. Tap any substitute below to swap into the Starting XI!",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DiskiGold
                        )
                    }
                }
            }
        }

        item {
            Text(text = "STARTING XI (${starters.size} / 11) • Tap to Swap", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DiskiGold)
        }

        items(starters) { player ->
            SquadPlayerRow(
                player = player,
                isSelected = selectedStarterForSwap?.id == player.id,
                onClick = { onSelectStarter(player) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "SUBSTITUTES & RESERVES (${subs.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        }

        items(subs) { player ->
            SquadPlayerRow(
                player = player,
                isSelected = false,
                onClick = {
                    if (selectedStarterForSwap != null) {
                        onSelectSubToSwap(player)
                    }
                }
            )
        }
    }
}

@Composable
private fun SquadPlayerRow(
    player: PlayerEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("squad_player_${player.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PitchGreen else SurfaceCard
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, DiskiGold) else null
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = SurfaceCardHighlight
                ) {
                    Text(
                        text = player.position.name,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = DiskiGold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "#${player.jerseyNumber}", fontSize = 11.sp, color = TextMuted)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = player.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "PAC: ${player.pace} • SHO: ${player.shooting} • PAS: ${player.passing}", fontSize = 10.sp, color = TextSecondary)
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = RatingHigh.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "${player.overall}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = RatingHigh,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun TransfersTabContent(
    marketPlayers: List<PlayerEntity>,
    budgetRemaining: Long,
    onBuyPlayer: (PlayerEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = PitchGreenDark),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "TRANSFER BUDGET REMAINING", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                        Text(text = "R ${(budgetRemaining / 1_000_000)}M", fontSize = 18.sp, fontWeight = FontWeight.Black, color = DiskiGold)
                    }
                    Icon(imageVector = Icons.Default.AttachMoney, contentDescription = null, tint = DiskiGold, modifier = Modifier.size(32.dp))
                }
            }
        }

        item {
            Text(text = "SCOUTED TARGETS (AVAILABLE PLAYERS)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DiskiGold)
        }

        items(marketPlayers) { player ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Surface(shape = RoundedCornerShape(6.dp), color = SurfaceCardHighlight) {
                            Text(text = player.position.name, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DiskiGold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = player.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                            Text(text = "OVR: ${player.overall} • Age: ${player.age} • Fee: R ${(player.marketValue / 1_000_000)}M", fontSize = 11.sp, color = TextSecondary)
                        }
                    }

                    Button(
                        onClick = { onBuyPlayer(player) },
                        enabled = budgetRemaining >= player.marketValue,
                        colors = ButtonDefaults.buttonColors(containerColor = PitchGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("buy_player_${player.id}")
                    ) {
                        Text("Sign Player", fontSize = 11.sp, color = if (budgetRemaining >= player.marketValue) DiskiGold else TextMuted, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
