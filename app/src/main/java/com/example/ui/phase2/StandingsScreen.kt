package com.example.ui.phase2

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.StandingEntity
import com.example.data.model.TeamEntity
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentRed
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
fun StandingsScreen(
    pslStandings: List<StandingEntity>,
    nfdStandings: List<StandingEntity>,
    allTeams: List<TeamEntity>,
    fixtures: List<MatchEntity>,
    onBack: () -> Unit,
    onTeamClick: (TeamEntity) -> Unit,
    onSimulateMatch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: PSL, 1: NFD
    val currentStandings = if (selectedTabIndex == 0) pslStandings else nfdStandings
    val teamsMap = remember(allTeams) { allTeams.associateBy { it.id } }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("standings_screen"),
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
                IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_standings")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "LEAGUE STANDINGS & STATS",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Text(
                        text = "Season 2026 • Live Standings & Promotion / Relegation",
                        style = MaterialTheme.typography.bodySmall,
                        color = DiskiGold,
                        fontSize = 11.sp
                    )
                }
            }

            // Tab Selector
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
                    text = {
                        Text(
                            text = "Betway Premiership (PSL)",
                            fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = if (selectedTabIndex == 0) DiskiGold else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("tab_psl_standings")
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Text(
                            text = "Motsepe Championship (NFD)",
                            fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = if (selectedTabIndex == 1) DiskiGold else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("tab_nfd_standings")
                )
            }

            // Content List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    ZoneLegendCard(isPsl = selectedTabIndex == 0)
                }

                item {
                    Text(
                        text = if (selectedTabIndex == 0) "BETWAY PREMIERSHIP TABLE" else "MOTSEPE FOUNDATION CHAMPIONSHIP TABLE",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = DiskiGold
                    )
                }

                // Table Header
                item {
                    TableHeaderRow()
                }

                // Standings Rows
                itemsIndexed(currentStandings, key = { _, s -> s.id }) { index, standing ->
                    val team = teamsMap[standing.teamId]
                    val position = index + 1
                    val zoneColor = getZoneColor(position, isPsl = selectedTabIndex == 0)

                    TableRow(
                        position = position,
                        team = team,
                        standing = standing,
                        zoneColor = zoneColor,
                        onClick = { team?.let { onTeamClick(it) } }
                    )
                }

                // Upcoming League Fixtures with Simulator
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "GAMEWEEK 1 FIXTURES & LIVE SIMULATOR",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = DiskiGold
                    )
                }

                val leagueFixtures = fixtures.filter {
                    if (selectedTabIndex == 0) it.tournamentType == com.example.data.model.TournamentType.PSL
                    else it.tournamentType == com.example.data.model.TournamentType.NFD
                }

                if (leagueFixtures.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                        ) {
                            Text(
                                text = "All GameWeek 1 matches completed! Standings reflected above.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                } else {
                    itemsIndexed(leagueFixtures) { _, match ->
                        val home = teamsMap[match.homeTeamId]
                        val away = teamsMap[match.awayTeamId]
                        MatchFixtureRow(
                            match = match,
                            home = home,
                            away = away,
                            onSimulate = { onSimulateMatch(match.id) }
                        )
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
private fun TableHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceCardHighlight)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "#", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
        Text(text = "CLUB", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.weight(1f))
        Text(text = "P", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
        Text(text = "W", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
        Text(text = "D", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
        Text(text = "L", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
        Text(text = "GD", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.width(30.dp), textAlign = TextAlign.Center)
        Text(text = "PTS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = DiskiGold, modifier = Modifier.width(34.dp), textAlign = TextAlign.Center)
    }
}

@Composable
private fun TableRow(
    position: Int,
    team: TeamEntity?,
    standing: StandingEntity,
    zoneColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .testTag("standing_row_${standing.teamId}"),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Position indicator with zone color border/indicator
            Box(
                modifier = Modifier
                    .width(26.dp)
                    .height(26.dp)
                    .clip(CircleShape)
                    .background(zoneColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$position",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = zoneColor
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Team Name & Badge
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                team?.let {
                    val pColor = try { Color(android.graphics.Color.parseColor(it.primaryColorHex)) } catch (e: Exception) { DiskiGold }
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(pColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = team?.shortName ?: standing.teamId,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1
                )
            }

            // Stats columns
            Text(text = "${standing.played}", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
            Text(text = "${standing.won}", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
            Text(text = "${standing.drawn}", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
            Text(text = "${standing.lost}", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.width(26.dp), textAlign = TextAlign.Center)
            Text(
                text = (if (standing.goalDifference > 0) "+${standing.goalDifference}" else "${standing.goalDifference}"),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (standing.goalDifference > 0) RatingHigh else if (standing.goalDifference < 0) AccentRed else TextSecondary,
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "${standing.points}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = DiskiGold,
                modifier = Modifier.width(34.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MatchFixtureRow(
    match: MatchEntity,
    home: TeamEntity?,
    away: TeamEntity?,
    onSimulate: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${home?.shortName ?: "Home"} vs ${away?.shortName ?: "Away"}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary
                )
                Text(
                    text = if (match.status == com.example.data.model.MatchStatus.FINISHED)
                        "Result: ${match.homeGoals} - ${match.awayGoals} (Final)"
                    else "${match.stage} • ${home?.stadiumName ?: "Stadium"}",
                    fontSize = 11.sp,
                    color = if (match.status == com.example.data.model.MatchStatus.FINISHED) RatingHigh else TextSecondary
                )
            }

            if (match.status != com.example.data.model.MatchStatus.FINISHED) {
                Button(
                    onClick = onSimulate,
                    colors = ButtonDefaults.buttonColors(containerColor = PitchGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("simulate_match_${match.id}")
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = DiskiGold, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Simulate", color = DiskiGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = RatingHigh.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "FT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RatingHigh,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ZoneLegendCard(isPsl: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = "QUALIFICATION & RELEGATION ZONES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DiskiGold)
            if (isPsl) {
                LegendItem(color = Color(0xFF00E676), label = "1st - 2nd: CAF Champions League")
                LegendItem(color = Color(0xFF29B6F6), label = "3rd: CAF Confederation Cup")
                LegendItem(color = DiskiGold, label = "1st - 8th: MTN8 Cup Qualification")
                LegendItem(color = AccentOrange, label = "15th: PSL Promotion/Relegation Play-offs")
                LegendItem(color = AccentRed, label = "16th: Automatic Relegation to NFD")
            } else {
                LegendItem(color = Color(0xFF00E676), label = "1st: Automatic Promotion to PSL")
                LegendItem(color = AccentOrange, label = "2nd - 3rd: Promotion Play-offs (with 15th PSL)")
                LegendItem(color = AccentRed, label = "15th - 16th: Relegation to ABC Motsepe League")
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, fontSize = 11.sp, color = TextSecondary)
    }
}

private fun getZoneColor(pos: Int, isPsl: Boolean): Color {
    return if (isPsl) {
        when {
            pos <= 2 -> Color(0xFF00E676)  // CAF CL
            pos == 3 -> Color(0xFF29B6F6)  // CAF CC
            pos <= 8 -> DiskiGold          // MTN8
            pos == 15 -> AccentOrange      // Playoff
            pos == 16 -> AccentRed         // Relegation
            else -> TextSecondary
        }
    } else {
        when {
            pos == 1 -> Color(0xFF00E676)  // Auto promo
            pos in 2..3 -> AccentOrange   // Playoff
            pos >= 15 -> AccentRed        // Relegation
            else -> TextSecondary
        }
    }
}
