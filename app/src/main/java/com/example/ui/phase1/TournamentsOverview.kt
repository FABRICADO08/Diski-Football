package com.example.ui.phase1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SwapVert
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TournamentEntity
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentRed
import com.example.ui.theme.DiskiGold
import com.example.ui.theme.PitchGreen
import com.example.ui.theme.RatingHigh
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardHighlight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TournamentsOverview(
    tournaments: List<TournamentEntity>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PromotionRelegationCard()
        }

        item {
            Text(
                text = "SOUTH AFRICAN DOMESTIC CUP COMPETITIONS",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = DiskiGold
            )
        }

        item {
            TournamentCard(
                title = "MTN8 Cup (Wafa Wafa)",
                tagline = "Winner Takes All • Top 8 PSL Clash",
                color = DiskiGold,
                teamsCount = "8 Elite Clubs",
                prize = "R 10,000,000",
                format = "Single Knockout QFs, Two-Leg Semi-Finals, Grand Final",
                qualification = "Strictly top 8 finishers from the preceding PSL campaign.",
                status = "Ready in DB • Quarter-Finals Populated"
            )
        }

        item {
            TournamentCard(
                title = "Carling Knockout Cup",
                tagline = "The League Cup of Mzansi",
                color = AccentRed,
                teamsCount = "16 PSL Teams",
                prize = "R 6,850,000",
                format = "Straight knockout: Round of 16, Quarter-Finals, Semi-Finals, and Final.",
                qualification = "All 16 Betway Premiership clubs.",
                extraFeature = "Carling All-Star Finale: Fan-voted exhibition clash against the Champion.",
                status = "Ready in DB • Round of 16 Bracket"
            )
        }

        item {
            TournamentCard(
                title = "Nedbank Cup",
                tagline = "South Africa's Premier FA Cup",
                color = PitchGreen,
                teamsCount = "32 Teams (PSL + NFD + Amateurs)",
                prize = "R 7,000,000",
                format = "Unseeded national knockout from Round of 32 down to the Final.",
                qualification = "16 PSL teams + 8 NFD clubs + 8 SAFA regional amateur qualifiers.",
                extraFeature = "Giant-killing potential with underdog qualification rules.",
                status = "Ready in DB • 32-Team Format"
            )
        }

        item {
            TournamentCard(
                title = "Carling Black Label Cup",
                tagline = "Pre-season Special Derby Event",
                color = AccentOrange,
                teamsCount = "Top Rivalry Clash (e.g. Soweto Derby)",
                prize = "Pre-season Glory & Fan Engagement",
                format = "Fan-selected starting line-ups and substitutions during match.",
                qualification = "South Africa's most popular clubs.",
                status = "Exhibition Tournament Schema"
            )
        }
    }
}

@Composable
private fun PromotionRelegationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(DiskiGold.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = null,
                        tint = DiskiGold,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Promotion & Relegation Mechanics",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "South African League Pyramid System",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Rule 1: Auto Promotion & Relegation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceCardHighlight)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = RatingHigh.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "AUTO",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RatingHigh,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "NFD 1st place champion gains automatic promotion to PSL. 16th PSL team is automatically relegated to NFD.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Rule 2: 3-Team Promotion Play-off Mini League
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceCardHighlight)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = DiskiGold.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "PLAY-OFF",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DiskiGold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "3-Team Play-off Tournament: 15th PSL finisher + 2nd & 3rd NFD teams play a home-and-away round-robin. Winner claims the final PSL spot.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
private fun TournamentCard(
    title: String,
    tagline: String,
    color: Color,
    teamsCount: String,
    prize: String,
    format: String,
    qualification: String,
    status: String,
    extraFeature: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = tagline,
                            style = MaterialTheme.typography.bodySmall,
                            color = color,
                            fontSize = 11.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PitchGreen.copy(alpha = 0.3f)
                ) {
                    Text(
                        text = teamsCount,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceCardHighlight)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TournamentDetailRow(label = "Format", value = format)
                TournamentDetailRow(label = "Prize Pool", value = prize)
                TournamentDetailRow(label = "Entry Rules", value = qualification)
                if (extraFeature != null) {
                    TournamentDetailRow(label = "Special Feature", value = extraFeature)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = DiskiGold,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodySmall,
                    color = DiskiGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun TournamentDetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$label: ",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            modifier = Modifier.width(90.dp)
        )
        Text(
            text = value,
            fontSize = 11.sp,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}
