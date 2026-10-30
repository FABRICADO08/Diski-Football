package com.example.ui.phase2

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CareerSaveEntity
import com.example.data.model.TeamEntity
import com.example.ui.ScreenRoute
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
fun MainMenuScreen(
    activeCareer: CareerSaveEntity?,
    userTeam: TeamEntity?,
    onNavigate: (ScreenRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Top Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(PitchGreen)
                            .border(2.dp, DiskiGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_diski_logo),
                            contentDescription = "Diski Logo",
                            modifier = Modifier.size(38.dp).clip(CircleShape)
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
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = DiskiGold
                            ) {
                                Text(
                                    text = "2026",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SurfaceDark,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = "South Africa's Premier Diski Simulation",
                            style = MaterialTheme.typography.bodySmall,
                            color = DiskiGold,
                            fontSize = 11.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PitchGreen)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsSoccer,
                            contentDescription = null,
                            tint = DiskiGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "PSL & NFD",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        // Hero Stadium Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.stadium_hero_banner),
                        contentDescription = "South African Stadium",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, SurfaceDark.copy(alpha = 0.95f)),
                                    startY = 60f
                                )
                            )
                    )

                    // Overlay Content
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = PitchGreen
                        ) {
                            Text(
                                text = "BETWAY PREMIERSHIP & MOTSEPE FOUNDATION CHAMPIONSHIP",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DiskiGold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Experience Mzansi Football",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "32 Clubs • MTN8 • Carling Knockout • Nedbank Cup",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Active Career Resume Banner (if save exists)
        if (activeCareer != null && userTeam != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onNavigate(ScreenRoute.CAREER_DASHBOARD) }
                        .testTag("resume_career_banner"),
                    colors = CardDefaults.cardColors(containerColor = PitchGreenDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DiskiGold)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(DiskiGold),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    tint = SurfaceDark,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "CONTINUE CAREER: ${userTeam.shortName.uppercase()}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    color = DiskiGold
                                )
                                Text(
                                    text = "Boss ${activeCareer.managerName} • GameWeek ${activeCareer.currentGameWeek} • R ${(activeCareer.transferBudgetRemaining / 1_000_000)}M Budget",
                                    fontSize = 12.sp,
                                    color = TextPrimary
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Resume",
                            tint = DiskiGold
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "GAME MODES",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = DiskiGold
            )
        }

        // Manager Mode Card
        item {
            MenuModeCard(
                title = "Manager Career Mode",
                subtitle = "Take charge of any PSL or NFD club. Lead transfers, manage player morale, tactics, and battle for the League & Cups.",
                badge = if (activeCareer != null) "In Progress" else "Popular",
                badgeColor = if (activeCareer != null) RatingHigh else DiskiGold,
                icon = Icons.Default.Star,
                iconTint = DiskiGold,
                testTag = "btn_career_mode",
                onClick = {
                    if (activeCareer != null) {
                        onNavigate(ScreenRoute.CAREER_DASHBOARD)
                    } else {
                        onNavigate(ScreenRoute.NEW_CAREER_SELECT)
                    }
                }
            )
        }

        // Quick Match Exhibition
        item {
            MenuModeCard(
                title = "Quick Match / Exhibition",
                subtitle = "Pick any two South African clubs (e.g. Soweto Derby, Tshwane Derby), choose your iconic venue and match length.",
                badge = "Exhibition",
                badgeColor = AccentOrange,
                icon = Icons.Default.PlayArrow,
                iconTint = AccentOrange,
                testTag = "btn_quick_match",
                onClick = { onNavigate(ScreenRoute.QUICK_MATCH_SETUP) }
            )
        }

        // Penalty Shootout Mini-Game
        item {
            MenuModeCard(
                title = "Penalty Shootout Showdown",
                subtitle = "Sudden death spot kicks! Aim with power gauge, dive as goalkeeper to make heroic saves, and celebrate with the crowd.",
                badge = "Mini-Game",
                badgeColor = DiskiGold,
                icon = Icons.Default.SportsSoccer,
                iconTint = DiskiGold,
                testTag = "btn_penalty_shootout",
                onClick = { onNavigate(ScreenRoute.PENALTY_SHOOTOUT) }
            )
        }

        // League Standings & Tables Hub
        item {
            MenuModeCard(
                title = "League Tables & Standings Hub",
                subtitle = "Live Betway Premiership & Motsepe Foundation Championship tables, promotion spots, play-off race, and relegation zone.",
                badge = "Live Tables",
                badgeColor = PitchGreen,
                icon = Icons.Default.FormatListNumbered,
                iconTint = Color(0xFF4CAF50),
                testTag = "btn_standings",
                onClick = { onNavigate(ScreenRoute.LEAGUE_STANDINGS) }
            )
        }

        // Tournament Brackets
        item {
            MenuModeCard(
                title = "South African Cup Brackets",
                subtitle = "MTN8 (Wafa Wafa), Carling Knockout Cup, and Nedbank Cup knockout trees with round fixtures and progress.",
                badge = "Cup Brackets",
                badgeColor = AccentRed,
                icon = Icons.Default.EmojiEvents,
                iconTint = DiskiGold,
                testTag = "btn_brackets",
                onClick = { onNavigate(ScreenRoute.TOURNAMENT_BRACKETS) }
            )
        }

        // Club Database & Rosters
        item {
            MenuModeCard(
                title = "Club Database & Player Rosters",
                subtitle = "Browse all 32 South African teams, squad cards, attributes, jersey numbers, and market values.",
                badge = "32 Clubs",
                badgeColor = DiskiGold,
                icon = Icons.Default.Group,
                iconTint = DiskiGold,
                testTag = "btn_club_database",
                onClick = { onNavigate(ScreenRoute.CLUB_DATABASE) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MenuModeCard(
    title: String,
    subtitle: String,
    badge: String,
    badgeColor: Color,
    icon: ImageVector,
    iconTint: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceCardHighlight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = badgeColor.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open",
                tint = TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
