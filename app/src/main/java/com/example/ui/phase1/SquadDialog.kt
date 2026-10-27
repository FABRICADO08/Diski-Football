package com.example.ui.phase1

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.PlayerEntity
import com.example.data.model.TeamEntity
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.DiskiGold
import com.example.ui.theme.PitchGreen
import com.example.ui.theme.RatingHigh
import com.example.ui.theme.RatingLow
import com.example.ui.theme.RatingMed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardHighlight
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SquadDialog(
    team: TeamEntity,
    squad: List<PlayerEntity>,
    onDismiss: () -> Unit
) {
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

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.90f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("squad_dialog"),
            color = SurfaceDark
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header with Team Details & Close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(primaryColor)
                                .border(2.dp, secondaryColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = team.abbreviation.take(3),
                                color = secondaryColor,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = team.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${team.stadiumName} • Capacity: ${team.stadiumCapacity}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_squad_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Kit colors & Budget banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceCardHighlight)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Club Kits:",
                            fontSize = 11.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(primaryColor)
                                .border(1.dp, Color.White, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(secondaryColor)
                                .border(1.dp, Color.White, CircleShape)
                        )
                    }

                    Text(
                        text = "Transfer Budget: R ${(team.transferBudget / 1_000_000)}M",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DiskiGold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "OFFICIAL SQUAD ROSTER (${squad.size} Players)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = DiskiGold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val starters = squad.filter { it.isStartingEleven }
                    val subs = squad.filter { !it.isStartingEleven }

                    item {
                        Text(
                            text = "Starting XI",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    items(starters) { player ->
                        PlayerItemRow(player = player)
                    }

                    if (subs.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Substitutes & Reserves",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        items(subs) { player ->
                            PlayerItemRow(player = player)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PitchGreen)
                ) {
                    Text("Close Roster", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PlayerItemRow(player: PlayerEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Position Badge + Jersey Number
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (player.position.group) {
                        "Goalkeeper" -> AccentOrange.copy(alpha = 0.2f)
                        "Defender" -> Color(0xFF1976D2).copy(alpha = 0.2f)
                        "Midfielder" -> PitchGreen.copy(alpha = 0.4f)
                        else -> AccentRed.copy(alpha = 0.2f)
                    }
                ) {
                    Text(
                        text = player.position.name,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = when (player.position.group) {
                            "Goalkeeper" -> AccentOrange
                            "Defender" -> Color(0xFF42A5F5)
                            "Midfielder" -> DiskiGold
                            else -> Color(0xFFFF5252)
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "#${player.jerseyNumber}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = player.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                        if (player.isCaptain) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Captain",
                                tint = DiskiGold,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                    Text(
                        text = "Age: ${player.age} • ${player.nationality} • Val: R ${(player.marketValue / 1_000_000)}M",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
            }

            // Stats: PAC / SHO / PAS / DEF
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                StatBadge(label = "PAC", value = player.pace)
                StatBadge(label = "SHO", value = player.shooting)
                StatBadge(label = "PAS", value = player.passing)
                StatBadge(label = "DEF", value = player.defending)

                // Overall badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when {
                        player.overall >= 80 -> RatingHigh.copy(alpha = 0.25f)
                        player.overall >= 74 -> RatingMed.copy(alpha = 0.25f)
                        else -> RatingLow.copy(alpha = 0.25f)
                    }
                ) {
                    Text(
                        text = "${player.overall}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = when {
                            player.overall >= 80 -> RatingHigh
                            player.overall >= 74 -> RatingMed
                            else -> RatingLow
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatBadge(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 8.sp, color = TextMuted, fontWeight = FontWeight.Bold)
        Text(text = "$value", fontSize = 10.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
    }
}
