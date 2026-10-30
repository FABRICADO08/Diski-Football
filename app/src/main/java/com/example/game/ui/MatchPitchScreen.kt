package com.example.game.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.PlayerEntity
import com.example.data.model.TeamEntity
import com.example.game.engine.MatchAudioHapticEvent
import com.example.game.engine.MatchEngine
import com.example.game.engine.MatchStats
import com.example.game.model.GameMatchPhase
import com.example.game.model.PlayerTeamSide
import com.example.game.model.Vector2D
import com.example.game.sound.DiskiAudioAndHaptics
import androidx.compose.ui.platform.LocalContext
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.DiskiGold
import com.example.ui.theme.PitchGreenDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun MatchPitchScreen(
    homeTeam: TeamEntity,
    awayTeam: TeamEntity,
    homeSquad: List<PlayerEntity> = emptyList(),
    awaySquad: List<PlayerEntity> = emptyList(),
    tournamentName: String = "Betway Premiership",
    onMatchCompleted: (MatchStats) -> Unit,
    onExitToMenu: () -> Unit,
    onGoToPenalties: ((MatchStats) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val matchEngine = remember(homeTeam.id, awayTeam.id) {
        MatchEngine(
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeSquad = homeSquad,
            awaySquad = awaySquad,
            tournamentName = tournamentName
        )
    }

    val context = LocalContext.current
    val audioHaptics = remember { DiskiAudioAndHaptics(context) }

    LaunchedEffect(matchEngine) {
        matchEngine.onAudioHapticEvent = { event ->
            when (event) {
                MatchAudioHapticEvent.KICK_NORMAL -> {
                    audioHaptics.playKickSound(isHardShot = false)
                    audioHaptics.hapticBallKick()
                }
                MatchAudioHapticEvent.KICK_HARD -> {
                    audioHaptics.playKickSound(isHardShot = true)
                    audioHaptics.hapticBallKick()
                }
                MatchAudioHapticEvent.TACKLE_COLLISION -> {
                    audioHaptics.playKickSound(isHardShot = false)
                    audioHaptics.hapticTackle()
                }
                MatchAudioHapticEvent.POST_HIT -> {
                    audioHaptics.playPostHitSound()
                    audioHaptics.hapticPostHit()
                }
                MatchAudioHapticEvent.GOAL_SCORED -> {
                    audioHaptics.playGoalRoar()
                    audioHaptics.hapticGoalScored()
                }
                MatchAudioHapticEvent.KASI_SKILL -> {
                    audioHaptics.playKasiFlavourSound()
                    audioHaptics.hapticKasiSkill()
                }
                MatchAudioHapticEvent.WHISTLE_START -> {
                    audioHaptics.playWhistleSound(isDoubleBlast = false)
                }
                MatchAudioHapticEvent.WHISTLE_HALF_TIME,
                MatchAudioHapticEvent.WHISTLE_FULL_TIME -> {
                    audioHaptics.playWhistleSound(isDoubleBlast = true)
                }
            }
        }
    }

    var isPaused by remember { mutableStateOf(false) }
    var showHalfTimeDialog by remember { mutableStateOf(false) }
    var showFullTimeDialog by remember { mutableStateOf(false) }
    var completedStats by remember { mutableStateOf<MatchStats?>(null) }

    // Re-render trigger tick
    var frameTick by remember { mutableFloatStateOf(0f) }

    // Main 60 FPS Game Loop
    LaunchedEffect(isPaused) {
        var lastNanoTime = System.nanoTime()
        while (!isPaused) {
            withFrameNanos { nowNano ->
                val dt = ((nowNano - lastNanoTime) / 1_000_000_000f).coerceIn(0.001f, 0.05f)
                lastNanoTime = nowNano

                matchEngine.update(dt) { stats ->
                    completedStats = stats
                    showFullTimeDialog = true
                }
                frameTick = (frameTick + 1f) % 1000f
            }
        }
    }

    // Monitor Phase Changes
    val phase = matchEngine.gameState.phase
    LaunchedEffect(phase) {
        if (phase == GameMatchPhase.HALF_TIME) {
            showHalfTimeDialog = true
        } else if (phase == GameMatchPhase.FULL_TIME) {
            completedStats = matchEngine.getMatchStats()
            showFullTimeDialog = true
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F1E14))
            .testTag("match_pitch_screen")
    ) {
        // 1. Interactive 2D Pitch Canvas
        PitchCanvas(
            players = matchEngine.players,
            ball = matchEngine.ball,
            cameraPosition = matchEngine.cameraPosition,
            homeKitPrimary = matchEngine.homeKitPrimary,
            homeKitSecondary = matchEngine.homeKitSecondary,
            awayKitPrimary = matchEngine.awayKitPrimary,
            awayKitSecondary = matchEngine.awayKitSecondary,
            activeControlledPlayerId = matchEngine.gameState.activeControlledPlayerId,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Broadcast TV Scoreboard & Commentary Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            // Scoreboard Bar
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xEE0D1F12),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, DiskiGold),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Home Team Info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(matchEngine.homeKitPrimary)
                                .border(1.dp, matchEngine.homeKitSecondary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = homeTeam.abbreviation.take(3),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = matchEngine.homeKitSecondary
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = homeTeam.shortName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    }

                    // Scoreboard Display
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${matchEngine.gameState.homeScore}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = DiskiGold
                        )
                        Text(
                            text = " - ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                        Text(
                            text = "${matchEngine.gameState.awayScore}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = DiskiGold
                        )
                    }

                    // Away Team Info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = awayTeam.shortName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(matchEngine.awayKitPrimary)
                                .border(1.dp, matchEngine.awayKitSecondary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = awayTeam.abbreviation.take(3),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = matchEngine.awayKitSecondary
                            )
                        }
                    }

                    // Clock & Pause
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF1B6B2F)
                        ) {
                            Text(
                                text = "${matchEngine.gameState.displayGameTime} (${if (matchEngine.gameState.currentHalf == 1) "1H" else "2H"})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = { isPaused = true },
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("btn_pause_match")
                        ) {
                            Text(text = "❚❚", color = DiskiGold, fontSize = 14.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Commentary Banner
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xCC000000),
                border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0x66FFD200)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DISKI LIVE",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = DiskiGold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = matchEngine.gameState.commentaryText,
                        fontSize = 10.sp,
                        color = Color.White,
                        maxLines = 1
                    )
                }
            }
        }

        // 3. Goal Scored "LADUMA!" Floating Banner
        AnimatedVisibility(
            visible = matchEngine.gameState.phase == GameMatchPhase.GOAL_SCORED,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xF00D2814)),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(2.5.dp, DiskiGold),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("banner_goal_scored")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "⚡ LADUMA! ⚡",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = DiskiGold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "GOAL SCORED!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = matchEngine.gameState.lastScorerName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AccentCyan
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "${homeTeam.shortName} ${matchEngine.gameState.homeScore} - ${matchEngine.gameState.awayScore} ${awayTeam.shortName}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }

        // 4. Mobile Controls (Left Joystick & Right Dynamic Action Buttons)
        if (matchEngine.gameState.phase == GameMatchPhase.PLAYING || matchEngine.gameState.phase == GameMatchPhase.KICKOFF) {
            // Left Thumb Virtual Joystick
            VirtualJoystick(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 20.dp),
                radiusDp = 65f,
                onMove = { dir, isSprint ->
                    matchEngine.onJoystickMove(dir, isSprint)
                },
                onRelease = {
                    matchEngine.onJoystickMove(Vector2D.ZERO, false)
                }
            )

            // Right Thumb Dynamic Action Buttons
            ActionButtonsOverlay(
                isAttacking = matchEngine.gameState.isUserAttacking,
                onPass = { matchEngine.onPassPressed() },
                onThroughBall = { matchEngine.onThroughBallPressed() },
                onShoot = { power -> matchEngine.onShootPressed(power) },
                onLobCross = { matchEngine.onLobCrossPressed() },
                onKasiSkill = { matchEngine.onKasiSkillPressed() },
                onTackle = { matchEngine.onTacklePressed() },
                onSlideTackle = { matchEngine.onSlideTacklePressed() },
                onSwitchPlayer = { matchEngine.onSwitchPlayerPressed() },
                onPress = { matchEngine.onTacklePressed() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 12.dp)
            )
        }

        // 5. Half-Time Dialog
        if (showHalfTimeDialog) {
            Dialog(onDismissRequest = {}) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = PitchGreenDark),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, DiskiGold),
                    modifier = Modifier.fillMaxWidth().testTag("dialog_half_time")
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "HALF TIME", fontSize = 20.sp, fontWeight = FontWeight.Black, color = DiskiGold)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "${homeTeam.shortName} ${matchEngine.gameState.homeScore} - ${matchEngine.gameState.awayScore} ${awayTeam.shortName}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Stats Summary
                        StatsRow("Possession", "${matchEngine.gameState.homePossessionPercent}%", "${100 - matchEngine.gameState.homePossessionPercent}%")
                        StatsRow("Shots on Target", "${matchEngine.gameState.homeShots}", "${matchEngine.gameState.awayShots}")
                        StatsRow("Tackles Won", "${matchEngine.homeTacklesCount}", "${matchEngine.awayTacklesCount}")

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                showHalfTimeDialog = false
                                matchEngine.startSecondHalf()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DiskiGold),
                            modifier = Modifier.fillMaxWidth().testTag("btn_resume_2nd_half")
                        ) {
                            Text(text = "START 2ND HALF", color = SurfaceDark, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // 6. Full-Time Dialog
        if (showFullTimeDialog && completedStats != null) {
            val stats = completedStats!!
            Dialog(onDismissRequest = {}) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PitchGreenDark),
                    border = androidx.compose.foundation.BorderStroke(2.dp, DiskiGold),
                    modifier = Modifier.fillMaxWidth().testTag("dialog_full_time")
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "FULL TIME", fontSize = 22.sp, fontWeight = FontWeight.Black, color = DiskiGold)
                        Text(text = tournamentName, fontSize = 12.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "${homeTeam.shortName} ${stats.homeScore} - ${stats.awayScore} ${awayTeam.shortName}",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Goal scorers
                        if (stats.scorers.isNotEmpty()) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(text = "Scorers:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DiskiGold)
                                for (scorer in stats.scorers) {
                                    Text(text = "⚽ $scorer", fontSize = 11.sp, color = TextPrimary)
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Match Stats
                        StatsRow("Possession", "${stats.homePossessionPercent}%", "${stats.awayPossessionPercent}%")
                        StatsRow("Total Shots", "${stats.homeShots}", "${stats.awayShots}")
                        StatsRow("Tackles Made", "${stats.homeTackles}", "${stats.awayTackles}")

                        Spacer(modifier = Modifier.height(20.dp))

                        if (onGoToPenalties != null && (stats.homeScore == stats.awayScore || tournamentName.contains("Cup", true) || tournamentName.contains("Knockout", true) || tournamentName.contains("MTN8", true))) {
                            Button(
                                onClick = {
                                    showFullTimeDialog = false
                                    onGoToPenalties(stats)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B0FF)),
                                modifier = Modifier.fillMaxWidth().testTag("btn_goto_penalties")
                            ) {
                                Text(text = "DECIDE ON PENALTIES ⚽", color = Color.White, fontWeight = FontWeight.Black)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Button(
                            onClick = {
                                showFullTimeDialog = false
                                onMatchCompleted(stats)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DiskiGold),
                            modifier = Modifier.fillMaxWidth().testTag("btn_finish_match")
                        ) {
                            Text(text = "FINISH MATCH & SAVE RESULT", color = SurfaceDark, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // 7. Pause Menu Dialog
        if (isPaused) {
            Dialog(onDismissRequest = { isPaused = false }) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, DiskiGold),
                    modifier = Modifier.fillMaxWidth().testTag("dialog_pause_menu")
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "MATCH PAUSED", fontSize = 18.sp, fontWeight = FontWeight.Black, color = DiskiGold)
                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = { isPaused = false },
                            colors = ButtonDefaults.buttonColors(containerColor = DiskiGold),
                            modifier = Modifier.fillMaxWidth().testTag("btn_resume_match")
                        ) {
                            Text(text = "RESUME PLAY", color = SurfaceDark, fontWeight = FontWeight.Black)
                        }

                        Button(
                            onClick = {
                                isPaused = false
                                matchEngine.resetPositionsForKickoff(PlayerTeamSide.HOME)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B6B2F)),
                            modifier = Modifier.fillMaxWidth().testTag("btn_restart_match")
                        ) {
                            Text(text = "RESTART MATCH", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                isPaused = false
                                onExitToMenu()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)),
                            modifier = Modifier.fillMaxWidth().testTag("btn_quit_match")
                        ) {
                            Text(text = "QUIT TO MAIN MENU", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsRow(label: String, homeValue: String, awayValue: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = homeValue, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(text = label, fontSize = 11.sp, color = TextSecondary)
        Text(text = awayValue, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
    }
}
