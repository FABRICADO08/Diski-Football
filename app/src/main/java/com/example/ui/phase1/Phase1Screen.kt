package com.example.ui.phase1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.theme.DiskiGold
import com.example.ui.theme.PitchGreen
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardHighlight
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun Phase1Screen(
    viewModel: Phase1ViewModel,
    onBackToMenu: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (onBackToMenu != null) {
        androidx.activity.compose.BackHandler { onBackToMenu() }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pslTeams by viewModel.pslTeams.collectAsStateWithLifecycle()
    val nfdTeams by viewModel.nfdTeams.collectAsStateWithLifecycle()
    val tournaments by viewModel.tournaments.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("phase1_screen"),
        containerColor = SurfaceDark
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header Bar
            Phase1Header(onBackToMenu = onBackToMenu)

            // Tab Row
            val tabs = listOf(
                "PSL (${pslTeams.size})",
                "NFD (${nfdTeams.size})",
                "Cups & Play-offs",
                "DB Schemas & Stats"
            )

            ScrollableTabRow(
                selectedTabIndex = uiState.selectedTab,
                containerColor = SurfaceDark,
                contentColor = DiskiGold,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    if (uiState.selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTab]),
                            color = DiskiGold,
                            height = 3.dp
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = uiState.selectedTab == index,
                        onClick = { viewModel.setTab(index) },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (uiState.selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp,
                                color = if (uiState.selectedTab == index) DiskiGold else TextSecondary
                            )
                        },
                        modifier = Modifier.testTag("tab_$index")
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = DiskiGold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = uiState.statusMessage,
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                when (uiState.selectedTab) {
                    0 -> {
                        // PSL Teams
                        TeamsList(
                            title = "Premier Soccer League (Betway Premiership)",
                            subtitle = "16 Top-Flight South African Clubs • Auto-Relegation & Play-off spots",
                            searchQuery = uiState.searchQuery,
                            onSearchChanged = { viewModel.setSearchQuery(it) },
                            teams = pslTeams.filter {
                                it.name.contains(uiState.searchQuery, ignoreCase = true) ||
                                        it.city.contains(uiState.searchQuery, ignoreCase = true) ||
                                        it.nickname.contains(uiState.searchQuery, ignoreCase = true)
                            },
                            onTeamSelected = { viewModel.selectTeam(it) }
                        )
                    }
                    1 -> {
                        // NFD Teams
                        TeamsList(
                            title = "Motsepe Foundation Championship (NFD)",
                            subtitle = "16 Second-Tier Clubs • 1 Auto-Promotion, 2 Play-off berths",
                            searchQuery = uiState.searchQuery,
                            onSearchChanged = { viewModel.setSearchQuery(it) },
                            teams = nfdTeams.filter {
                                it.name.contains(uiState.searchQuery, ignoreCase = true) ||
                                        it.city.contains(uiState.searchQuery, ignoreCase = true) ||
                                        it.nickname.contains(uiState.searchQuery, ignoreCase = true)
                            },
                            onTeamSelected = { viewModel.selectTeam(it) }
                        )
                    }
                    2 -> {
                        // Cups & Play-offs
                        TournamentsOverview(tournaments = tournaments)
                    }
                    3 -> {
                        // Schema & Room DB Stats
                        SchemaOverview(
                            stats = uiState.stats,
                            onReseed = { viewModel.reseedDatabase() }
                        )
                    }
                }
            }
        }
    }

    // Squad Dialog Modal
    uiState.selectedTeam?.let { team ->
        SquadDialog(
            team = team,
            squad = uiState.selectedTeamSquad,
            onDismiss = { viewModel.selectTeam(null) }
        )
    }
}

@Composable
private fun Phase1Header(onBackToMenu: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onBackToMenu != null) {
                IconButton(onClick = onBackToMenu) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(PitchGreen)
                    .border(2.dp, DiskiGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_diski_logo),
                    contentDescription = "Diski Football Logo",
                    modifier = Modifier.size(36.dp).clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "DISKI FOOTBALL",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ZA",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DiskiGold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SurfaceCardHighlight)
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
                Text(
                    text = "Phase 1: Architecture & Data Layer",
                    style = MaterialTheme.typography.bodySmall,
                    color = DiskiGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceCard)
                .border(1.dp, PitchGreen, RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SportsSoccer,
                    contentDescription = null,
                    tint = DiskiGold,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "32 TEAMS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
private fun TeamsList(
    title: String,
    subtitle: String,
    searchQuery: String,
    onSearchChanged: (String) -> Unit,
    teams: List<com.example.data.model.TeamEntity>,
    onTeamSelected: (com.example.data.model.TeamEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        item {
            // Search field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_teams_input"),
                placeholder = {
                    Text("Search team name, city, or nickname...", color = TextMuted, fontSize = 13.sp)
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = TextMuted)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChanged("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceCard,
                    unfocusedContainerColor = SurfaceCard,
                    focusedBorderColor = DiskiGold,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )
        }

        items(teams, key = { it.id }) { team ->
            TeamCard(
                team = team,
                onClick = { onTeamSelected(team) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
