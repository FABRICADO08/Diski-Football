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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import com.example.data.model.MatchEntity
import com.example.data.model.MatchStatus
import com.example.data.model.StandingEntity
import com.example.data.model.TeamEntity
import com.example.ui.components.DiskiClubBadge
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentRed
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
fun TournamentBracketsScreen(
    mtn8Matches: List<MatchEntity>,
    carlingMatches: List<MatchEntity>,
    nedbankMatches: List<MatchEntity>,
    cblMatches: List<MatchEntity> = emptyList(),
    playoffMatches: List<MatchEntity>,
    playoffStandings: List<StandingEntity>,
    allTeams: List<TeamEntity>,
    onBack: () -> Unit,
    onSimulateMatch: (String) -> Unit,
    onPlayMatch: ((home: TeamEntity, away: TeamEntity, stage: String, matchId: String) -> Unit)? = null,
    onTeamClick: (TeamEntity) -> Unit,
    onGenerateCarlingDraw: () -> Unit,
    onGenerateNedbankDraw: () -> Unit,
    onGenerateCblDraw: () -> Unit = {},
    onVoteCbl: (teamName: String, playerVote: String) -> Unit = { _, _ -> },
    onStartPlayoffs: () -> Unit,
    onFinalizeSeason: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: MTN8, 1: Carling CKO, 2: Nedbank Cup, 3: Play-offs
    val teamsMap = remember(allTeams) { allTeams.associateBy { it.id } }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("tournament_brackets_screen"),
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
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_brackets")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "SOUTH AFRICAN CUP BRACKETS",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Text(
                        text = "MTN8 • Carling Knockout • Nedbank Cup • Promotion Play-offs",
                        style = MaterialTheme.typography.bodySmall,
                        color = DiskiGold,
                        fontSize = 11.sp
                    )
                }
            }

            // Cup Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 12.dp,
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
                    text = { Text("MTN8 Cup", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (selectedTabIndex == 0) DiskiGold else TextSecondary) },
                    modifier = Modifier.testTag("tab_mtn8")
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Carling CKO", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (selectedTabIndex == 1) DiskiGold else TextSecondary) },
                    modifier = Modifier.testTag("tab_carling")
                )
                Tab(
                    selected = selectedTabIndex == 2,
                    onClick = { selectedTabIndex = 2 },
                    text = { Text("Nedbank Cup", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (selectedTabIndex == 2) DiskiGold else TextSecondary) },
                    modifier = Modifier.testTag("tab_nedbank")
                )
                Tab(
                    selected = selectedTabIndex == 3,
                    onClick = { selectedTabIndex = 3 },
                    text = { Text("Play-offs", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (selectedTabIndex == 3) DiskiGold else TextSecondary) },
                    modifier = Modifier.testTag("tab_playoff")
                )
                Tab(
                    selected = selectedTabIndex == 4,
                    onClick = { selectedTabIndex = 4 },
                    text = { Text("Black Label", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (selectedTabIndex == 4) DiskiGold else TextSecondary) },
                    modifier = Modifier.testTag("tab_black_label")
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (selectedTabIndex) {
                    0 -> {
                        // MTN8 Wafa Wafa
                        item {
                            CupHeaderCard(
                                title = "MTN8 Cup (Wafa Wafa)",
                                subtitle = "R10,000,000 Winner-Takes-All • Top 8 PSL Clubs",
                                accentColor = DiskiGold
                            )
                        }

                        item {
                            Text(
                                text = "QUARTER-FINALS (SINGLE KNOCKOUT)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = DiskiGold
                            )
                        }

                        items(mtn8Matches) { match ->
                            val home = teamsMap[match.homeTeamId]
                            val away = teamsMap[match.awayTeamId]
                            BracketMatchCard(
                                match = match,
                                home = home,
                                away = away,
                                onSimulate = { onSimulateMatch(match.id) },
                                onPlay = if (home != null && away != null && onPlayMatch != null) { { onPlayMatch(home, away, match.stage, match.id) } } else null,
                                onTeamClick = onTeamClick
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            SemiFinalsPlaceholderCard(
                                title = "SEMI-FINALS (TWO-LEG AGGREGATE)",
                                desc = "Winners from QF 1 vs QF 4 and QF 2 vs QF 3 meet home and away before the grand final at Moses Mabhida Stadium."
                            )
                        }
                    }
                    1 -> {
                        // Carling Knockout Cup
                        item {
                            CupHeaderCard(
                                title = "Carling Knockout Cup",
                                subtitle = "16 PSL Teams Knockout + Carling All-Star Match",
                                accentColor = AccentRed
                            )
                        }

                        item {
                            CarlingAllStarCard()
                        }

                        if (carlingMatches.isEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Carling Knockout Bracket Pending",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Draw the official Round of 16 featuring all 16 Premier Soccer League clubs.",
                                            fontSize = 11.sp,
                                            color = TextSecondary,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(14.dp))
                                        Button(
                                            onClick = onGenerateCarlingDraw,
                                            colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.testTag("btn_draw_carling")
                                        ) {
                                            Icon(imageVector = Icons.Default.Casino, contentDescription = null, tint = Color.White)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Generate Round of 16 Draw", fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                }
                            }
                        } else {
                            item {
                                Text(
                                    text = "ROUND OF 16 FIXTURES",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DiskiGold
                                )
                            }

                            items(carlingMatches) { match ->
                                val home = teamsMap[match.homeTeamId]
                                val away = teamsMap[match.awayTeamId]
                                BracketMatchCard(
                                    match = match,
                                    home = home,
                                    away = away,
                                    onSimulate = { onSimulateMatch(match.id) },
                                    onPlay = if (home != null && away != null && onPlayMatch != null) { { onPlayMatch(home, away, match.stage, match.id) } } else null,
                                    onTeamClick = onTeamClick
                                )
                            }
                        }
                    }
                    2 -> {
                        // Nedbank Cup
                        item {
                            CupHeaderCard(
                                title = "Nedbank Cup",
                                subtitle = "32-Team National FA Cup (16 PSL + 8 NFD + 8 Amateur Qualifiers)",
                                accentColor = PitchGreen
                            )
                        }

                        if (nedbankMatches.isEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Nedbank Cup 32-Team Draw Pending",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Execute the unseeded national cup draw pairing 16 PSL giants, 8 NFD clubs, and 8 regional amateur underdogs.",
                                            fontSize = 11.sp,
                                            color = TextSecondary,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(14.dp))
                                        Button(
                                            onClick = onGenerateNedbankDraw,
                                            colors = ButtonDefaults.buttonColors(containerColor = PitchGreen),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.testTag("btn_draw_nedbank")
                                        ) {
                                            Icon(imageVector = Icons.Default.Casino, contentDescription = null, tint = DiskiGold)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Generate Round of 32 Draw", fontWeight = FontWeight.Bold, color = DiskiGold)
                                        }
                                    }
                                }
                            }
                        } else {
                            item {
                                Text(
                                    text = "ROUND OF 32 FIXTURES (16 TIES)",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DiskiGold
                                )
                            }

                            items(nedbankMatches) { match ->
                                val home = teamsMap[match.homeTeamId]
                                val away = teamsMap[match.awayTeamId]
                                BracketMatchCard(
                                    match = match,
                                    home = home,
                                    away = away,
                                    onSimulate = { onSimulateMatch(match.id) },
                                    onPlay = if (home != null && away != null && onPlayMatch != null) { { onPlayMatch(home, away, match.stage, match.id) } } else null,
                                    onTeamClick = onTeamClick
                                )
                            }
                        }
                    }
                    3 -> {
                        // Promotion / Relegation Play-offs
                        item {
                            CupHeaderCard(
                                title = "PSL / NFD Promotion Play-off Tournament",
                                subtitle = "3-Team Round-Robin Mini League: 15th PSL vs 2nd & 3rd NFD",
                                accentColor = AccentOrange
                            )
                        }

                        if (playoffMatches.isEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Play-off Tournament Setup",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Generates 6 round-robin fixtures between the 15th place PSL team and the 2nd & 3rd place NFD contenders. Winner claims the final PSL spot.",
                                            fontSize = 11.sp,
                                            color = TextSecondary,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(14.dp))
                                        Button(
                                            onClick = onStartPlayoffs,
                                            colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.testTag("btn_start_playoffs")
                                        ) {
                                            Icon(imageVector = Icons.Default.SwapVert, contentDescription = null, tint = SurfaceDark)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Generate Play-off Mini League", fontWeight = FontWeight.Bold, color = SurfaceDark)
                                        }
                                    }
                                }
                            }
                        } else {
                            // Playoff Standings Table
                            item {
                                Text(
                                    text = "PLAY-OFF MINI LEAGUE STANDINGS",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DiskiGold
                                )
                            }

                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        playoffStandings.forEachIndexed { idx, s ->
                                            val t = teamsMap[s.teamId]
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(text = "${idx + 1}.", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (idx == 0) RatingHigh else TextSecondary)
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(text = t?.name ?: s.teamId, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                                                }
                                                Text(text = "P: ${s.played} | GD: ${s.goalDifference} | PTS: ${s.points}", fontSize = 11.sp, fontWeight = FontWeight.Black, color = DiskiGold)
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                Text(
                                    text = "PLAY-OFF FIXTURES",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DiskiGold
                                )
                            }

                            items(playoffMatches) { match ->
                                val home = teamsMap[match.homeTeamId]
                                val away = teamsMap[match.awayTeamId]
                                BracketMatchCard(
                                    match = match,
                                    home = home,
                                    away = away,
                                    onSimulate = { onSimulateMatch(match.id) },
                                    onPlay = if (home != null && away != null && onPlayMatch != null) { { onPlayMatch(home, away, match.stage, match.id) } } else null,
                                    onTeamClick = onTeamClick
                                )
                            }

                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = PitchGreenDark),
                                    shape = RoundedCornerShape(14.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, DiskiGold)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "SEASON FINALIZATION ENGINE", fontWeight = FontWeight.Black, fontSize = 13.sp, color = DiskiGold)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Executes official end-of-season mechanics:\n• 1st NFD Auto-promoted to PSL\n• 16th PSL Auto-relegated to NFD\n• Play-off Winner awarded PSL spot\n• Next Season Top 8 seeded into MTN8",
                                            fontSize = 11.sp,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Button(
                                            onClick = onFinalizeSeason,
                                            modifier = Modifier.fillMaxWidth().testTag("btn_finalize_season"),
                                            colors = ButtonDefaults.buttonColors(containerColor = PitchGreen),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Finalize Season & Execute Promo/Relegation", fontWeight = FontWeight.Black, color = DiskiGold, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    4 -> {
                        // Carling Black Label Cup (Pre-season Fan-Voted Tournament)
                        item {
                            CupHeaderCard(
                                title = "Carling Black Label Cup",
                                subtitle = "Pre-season Fan-Voted Showdown • Soweto Derby & Mzansi Giants",
                                accentColor = AccentOrange
                            )
                        }

                        // Fan Voting Card
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth().testTag("card_cbl_fan_vote"),
                                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DiskiGold)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier.size(32.dp).clip(CircleShape).background(DiskiGold),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = SurfaceDark, modifier = Modifier.size(18.dp))
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text("BE THE CHAMPION COACH", fontWeight = FontWeight.Black, fontSize = 13.sp, color = DiskiGold)
                                            Text("Cast your fan vote to influence starting line-ups & tactics!", fontSize = 11.sp, color = TextSecondary)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    var selectedClubForVote by remember { mutableStateOf("Kaizer Chiefs") }
                                    var selectedVoteOption by remember { mutableStateOf("Start Ashley Du Preez (Pacey Counter)") }

                                    Text("Select Your Club:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        listOf("Kaizer Chiefs", "Orlando Pirates", "Sundowns").forEach { club ->
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = if (selectedClubForVote == club) DiskiGold else SurfaceCardHighlight,
                                                modifier = Modifier.clickable {
                                                    selectedClubForVote = club
                                                    selectedVoteOption = when (club) {
                                                        "Kaizer Chiefs" -> "Start Ashley Du Preez (Pacey Counter)"
                                                        "Orlando Pirates" -> "Start Patrick Maswanganyi as Playmaker"
                                                        else -> "Start Lucas Ribeiro Costa (Flair Attack)"
                                                    }
                                                }
                                            ) {
                                                Text(
                                                    text = club,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (selectedClubForVote == club) SurfaceDark else TextSecondary,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text("Tactical Fan Choice:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = PitchGreenDark,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "🗳️ $selectedVoteOption",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = DiskiGold,
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = { onVoteCbl(selectedClubForVote, selectedVoteOption) },
                                        modifier = Modifier.fillMaxWidth().testTag("btn_submit_fan_vote"),
                                        colors = ButtonDefaults.buttonColors(containerColor = DiskiGold),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Submit Fan Vote & Boost Morale (+5)", color = SurfaceDark, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                    }
                                }
                            }
                        }

                        if (cblMatches.isEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = "Carling Black Label Cup Pending", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Generate the pre-season showpiece tournament featuring the Soweto Derby (Chiefs vs Pirates) and Tshwane clash (Sundowns vs AmaZulu).",
                                            fontSize = 11.sp,
                                            color = TextSecondary,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(14.dp))
                                        Button(
                                            onClick = onGenerateCblDraw,
                                            colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.testTag("btn_draw_cbl")
                                        ) {
                                            Icon(imageVector = Icons.Default.Casino, contentDescription = null, tint = SurfaceDark)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Seed Black Label Cup Bracket", fontWeight = FontWeight.Bold, color = SurfaceDark)
                                        }
                                    }
                                }
                            }
                        } else {
                            item {
                                Text(
                                    text = "PRE-SEASON FIXTURES & SOWETO DERBY",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DiskiGold
                                )
                            }

                            items(cblMatches) { match ->
                                val home = teamsMap[match.homeTeamId]
                                val away = teamsMap[match.awayTeamId]
                                BracketMatchCard(
                                    match = match,
                                    home = home,
                                    away = away,
                                    onSimulate = { onSimulateMatch(match.id) },
                                    onPlay = if (home != null && away != null && onPlayMatch != null) { { onPlayMatch(home, away, match.stage, match.id) } } else null,
                                    onTeamClick = onTeamClick
                                )
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
}

@Composable
private fun CupHeaderCard(title: String, subtitle: String, accentColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = null, tint = accentColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = accentColor, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun BracketMatchCard(
    match: MatchEntity,
    home: TeamEntity?,
    away: TeamEntity?,
    onSimulate: () -> Unit,
    onPlay: (() -> Unit)? = null,
    onTeamClick: (TeamEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardHighlight)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = match.stage, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DiskiGold)
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (match.status == MatchStatus.FINISHED) RatingHigh.copy(alpha = 0.2f) else SurfaceCardHighlight
                ) {
                    Text(
                        text = if (match.status == MatchStatus.FINISHED) "COMPLETED" else "UPCOMING",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (match.status == MatchStatus.FINISHED) RatingHigh else TextSecondary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Home Team Row
            TeamMatchupRow(
                team = home,
                score = match.homeGoals,
                isWinner = match.status == MatchStatus.FINISHED && (match.homeGoals ?: 0) > (match.awayGoals ?: 0),
                onClick = { home?.let { onTeamClick(it) } }
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Away Team Row
            TeamMatchupRow(
                team = away,
                score = match.awayGoals,
                isWinner = match.status == MatchStatus.FINISHED && (match.awayGoals ?: 0) > (match.homeGoals ?: 0),
                onClick = { away?.let { onTeamClick(it) } }
            )

            if (match.status != MatchStatus.FINISHED) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (onPlay != null) {
                        Button(
                            onClick = onPlay,
                            modifier = Modifier.weight(1.1f).testTag("btn_play_bracket_${match.id}"),
                            colors = ButtonDefaults.buttonColors(containerColor = DiskiGold),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = SurfaceDark, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Play Pitch", color = SurfaceDark, fontWeight = FontWeight.Black, fontSize = 11.sp)
                        }
                    }
                    Button(
                        onClick = onSimulate,
                        modifier = Modifier.weight(0.9f).testTag("btn_simulate_bracket_${match.id}"),
                        colors = ButtonDefaults.buttonColors(containerColor = PitchGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Simulate", color = DiskiGold, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamMatchupRow(
    team: TeamEntity?,
    score: Int?,
    isWinner: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            team?.let {
                DiskiClubBadge(team = it, size = 24.dp)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = team?.name ?: "TBD Club",
                fontSize = 13.sp,
                fontWeight = if (isWinner) FontWeight.Black else FontWeight.SemiBold,
                color = if (isWinner) DiskiGold else TextPrimary
            )
        }

        if (score != null) {
            Text(
                text = "$score",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = if (isWinner) DiskiGold else TextSecondary
            )
        } else {
            Text(text = "-", fontSize = 12.sp, color = TextMuted)
        }
    }
}

@Composable
private fun SemiFinalsPlaceholderCard(title: String, desc: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = desc, fontSize = 11.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun CarlingAllStarCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AccentRed.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AccentRed.copy(alpha = 0.5f))
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = AccentRed, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = "Carling All-Star Exhibition Finale", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                Text(
                    text = "Post-tournament showpiece: The Carling Knockout Champion faces an elite All-Star XI voted exclusively by South African fans.",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }
    }
}
