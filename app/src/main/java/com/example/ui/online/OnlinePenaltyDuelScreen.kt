package com.example.ui.online

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MatchRoom
import com.example.data.model.TeamEntity
import com.example.game.sound.DiskiAudioAndHaptics
import com.example.ui.components.DiskiClubBadge
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.DiskiGold
import com.example.ui.theme.PitchGreen
import com.example.ui.theme.PitchGreenDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun OnlinePenaltyDuelScreen(
    room: MatchRoom?,
    currentUser: FirebaseUser?,
    allTeams: List<TeamEntity>,
    onTakeShot: (scored: Boolean, commentary: String) -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onExit() }

    val context = LocalContext.current
    val audioHaptics = remember { DiskiAudioAndHaptics(context) }

    val hostTeam = allTeams.find { it.id == room?.hostTeamId } ?: allTeams.firstOrNull()
    val guestTeam = allTeams.find { it.id == room?.guestTeamId } ?: allTeams.lastOrNull()

    val isHost = currentUser?.uid == room?.hostUserId
    val isMyTurn = if (isHost) room?.currentTurn == "HOST_KICKING" else room?.currentTurn == "GUEST_KICKING"
    val isGameOver = room?.status == "COMPLETED"

    var aimX by remember { mutableFloatStateOf(0.5f) }
    var aimY by remember { mutableFloatStateOf(0.5f) }
    var isAimingActive by remember { mutableStateOf(true) }

    var isChargingPower by remember { mutableStateOf(false) }
    var shotPower by remember { mutableFloatStateOf(0.4f) }

    var ballProgress by remember { mutableFloatStateOf(0f) }
    var isBallInFlight by remember { mutableStateOf(false) }
    var ballTargetX by remember { mutableFloatStateOf(0.5f) }
    var ballTargetY by remember { mutableFloatStateOf(0.5f) }

    var gkDiveTargetX by remember { mutableFloatStateOf(0f) }
    var gkDiveCurrentX by remember { mutableFloatStateOf(0f) }
    var gkDiveProgress by remember { mutableFloatStateOf(0f) }

    // Aim reticle movement
    LaunchedEffect(isAimingActive, isMyTurn) {
        if (isAimingActive && isMyTurn) {
            var time = 0f
            while (isAimingActive) {
                withFrameNanos {
                    time += 0.035f
                    aimX = (sin(time.toDouble()).toFloat() + 1f) / 2f
                    aimY = (sin((time * 1.4f).toDouble()).toFloat() + 1f) / 2f
                }
            }
        }
    }

    // Power gauge charge
    LaunchedEffect(isChargingPower) {
        if (isChargingPower) {
            shotPower = 0.35f
            while (isChargingPower && shotPower < 1.0f) {
                delay(16)
                shotPower = (shotPower + 0.04f).coerceAtMost(1.0f)
            }
        }
    }

    // Ball Flight & GK dive animation
    LaunchedEffect(isBallInFlight) {
        if (isBallInFlight) {
            var flightTime = 0f
            while (flightTime < 1.0f) {
                withFrameNanos {
                    flightTime += 0.045f
                    ballProgress = flightTime.coerceAtMost(1.0f)
                    gkDiveProgress = flightTime.coerceAtMost(1.0f)
                    gkDiveCurrentX = gkDiveTargetX * gkDiveProgress
                }
            }

            val goalWidthTolerance = 0.22f
            val gkDist = abs(ballTargetX - (0.5f + gkDiveTargetX * 0.42f))
            val isSaved = gkDist < goalWidthTolerance && ballTargetY in 0.15f..0.85f
            val isMissed = ballTargetX < 0.08f || ballTargetX > 0.92f || ballTargetY < 0.05f

            val scored = !isSaved && !isMissed

            if (scored) {
                audioHaptics.playGoalRoar()
                audioHaptics.hapticGoalScored()
            } else {
                audioHaptics.playPostHitSound()
                audioHaptics.hapticPostHit()
            }

            val shooterName = if (isHost) (room?.hostDisplayName ?: "Host") else (room?.guestDisplayName ?: "Guest")
            val commentary = if (scored) {
                "GOAL! $shooterName scores a sensational penalty over the cloud!"
            } else if (isSaved) {
                "SAVED! Goalkeeper heroics deny $shooterName!"
            } else {
                "OFF TARGET! $shooterName blazes it wide!"
            }

            delay(1500)
            onTakeShot(scored, commentary)

            ballProgress = 0f
            gkDiveProgress = 0f
            gkDiveCurrentX = 0f
            isAimingActive = true
            isChargingPower = false
            shotPower = 0.4f
            isBallInFlight = false
        }
    }

    fun unleashShot() {
        if (!isAimingActive || isBallInFlight || !isMyTurn) return
        isAimingActive = false

        val devX = (Random.nextFloat() - 0.5f) * (shotPower * 0.25f)
        val devY = (Random.nextFloat() - 0.5f) * (shotPower * 0.25f)
        ballTargetX = (aimX + devX).coerceIn(0.05f, 0.95f)
        ballTargetY = (aimY + devY).coerceIn(0.1f, 0.9f)

        val gkChoices = listOf(-1f, -0.6f, 0f, 0.6f, 1f)
        gkDiveTargetX = gkChoices.random()

        audioHaptics.playKickSound(isHardShot = shotPower > 0.7f)
        audioHaptics.hapticBallKick()

        isBallInFlight = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A180E))
            .testTag("online_penalty_duel_screen")
    ) {
        // Perspective 3D Shootout Scene Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawShootoutSceneOnline(
                ballProgress = ballProgress,
                ballTargetX = ballTargetX,
                ballTargetY = ballTargetY,
                gkDiveCurrentX = gkDiveCurrentX,
                isBallInFlight = isBallInFlight,
                aimX = aimX,
                aimY = aimY,
                showAimReticle = (isMyTurn && isAimingActive && !isGameOver)
            )
        }

        // Scoreboard & Room Info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xEE091B0F),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, DiskiGold),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "LIVE 1v1 MATCH", fontSize = 11.sp, fontWeight = FontWeight.Black, color = DiskiGold)
                        Text(text = "ROOM: ${room?.roomCode ?: ""}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    val turnStatus = if (isGameOver) {
                        "MATCH CONCLUDED"
                    } else if (isMyTurn) {
                        "🎮 YOUR TURN TO STRIKE!"
                    } else {
                        "⏳ OPPONENT'S TURN TO STRIKE..."
                    }
                    Text(
                        text = turnStatus,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isMyTurn) DiskiGold else Color(0xFFFF9100)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Host (Home)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (hostTeam != null) {
                                DiskiClubBadge(team = hostTeam, size = 32.dp)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "${room?.hostDisplayName ?: "Host"} ${if (isHost) "(You)" else ""}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                OnlinePenaltyPegsRow(penalties = room?.hostKicks ?: emptyList())
                            }
                        }

                        // Score
                        Text(
                            text = "${room?.hostScore ?: 0} - ${room?.guestScore ?: 0}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = DiskiGold
                        )

                        // Guest (Away)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${room?.guestDisplayName ?: "Guest"} ${if (!isHost) "(You)" else ""}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                OnlinePenaltyPegsRow(penalties = room?.guestKicks ?: emptyList())
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            if (guestTeam != null) {
                                DiskiClubBadge(team = guestTeam, size = 32.dp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Commentary Banner
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xDD000000),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x55FFD200)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = room?.lastAction ?: "Both players connected via cloud! Game on!",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        // Striker Controls for Current Player
        if (isMyTurn && !isBallInFlight && !isGameOver) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isChargingPower) {
                    Box(
                        modifier = Modifier
                            .width(180.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xDD000000))
                            .border(1.dp, DiskiGold, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "SHOT POWER", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text(text = "${(shotPower * 100).toInt()}%", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DiskiGold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { shotPower },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = if (shotPower > 0.85f) Color(0xFFFF3D00) else DiskiGold,
                                trackColor = Color(0x44FFFFFF)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = {
                        if (!isChargingPower) {
                            isChargingPower = true
                        } else {
                            isChargingPower = false
                            unleashShot()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DiskiGold),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .height(52.dp)
                        .width(230.dp)
                        .testTag("btn_online_take_penalty")
                ) {
                    Text(
                        text = if (!isChargingPower) "AIM & CHARGE POWER" else "UNLEASH SPOT KICK!",
                        color = SurfaceDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        } else if (!isMyTurn && !isGameOver) {
            // Waiting Indicator for opponent's turn
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xDD000000),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF9100))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(color = Color(0xFFFF9100), modifier = Modifier.size(20.dp), strokeWidth = 2.5.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Opponent is taking their shot across the network...",
                            color = Color(0xFFFFB74D),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Game Over Dialog
        AnimatedVisibility(
            visible = isGameOver,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            val iWon = currentUser?.uid == room?.winnerUserId
            Card(
                colors = CardDefaults.cardColors(containerColor = PitchGreenDark),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, DiskiGold),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .testTag("dialog_online_duel_over")
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (iWon) "🏆 VICTORY! YOU WIN! 🏆" else "DEFEAT! OPPONENT WINS!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = if (iWon) DiskiGold else Color(0xFFFF5252)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "${room?.hostScore ?: 0} - ${room?.guestScore ?: 0}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (iWon) "Outstanding nerve and penalty execution!" else "Great contest! Challenge them again!",
                        fontSize = 12.sp,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onExit,
                        colors = ButtonDefaults.buttonColors(containerColor = DiskiGold),
                        modifier = Modifier.fillMaxWidth().testTag("btn_online_exit")
                    ) {
                        Text(text = "RETURN TO MAIN MENU", color = SurfaceDark, fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

@Composable
private fun OnlinePenaltyPegsRow(penalties: List<Boolean>) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in 0 until 5) {
            val status = penalties.getOrNull(i)
            val color = when (status) {
                true -> Color(0xFF00E676)
                false -> Color(0xFFFF1744)
                null -> Color(0x44FFFFFF)
            }
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(color)
                    .border(0.5.dp, Color(0x88FFFFFF), RoundedCornerShape(5.dp))
            )
        }
    }
}

private fun DrawScope.drawShootoutSceneOnline(
    ballProgress: Float,
    ballTargetX: Float,
    ballTargetY: Float,
    gkDiveCurrentX: Float,
    isBallInFlight: Boolean,
    aimX: Float,
    aimY: Float,
    showAimReticle: Boolean
) {
    val w = size.width
    val h = size.height

    // Pitch grass
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF04200C), Color(0xFF0D3818), Color(0xFF082610))
        ),
        size = Size(w, h)
    )

    // Goal Frame Dimensions
    val goalLeft = w * 0.16f
    val goalRight = w * 0.84f
    val goalWidth = goalRight - goalLeft
    val goalTop = h * 0.28f
    val goalBottom = h * 0.58f
    val goalHeight = goalBottom - goalTop

    // Net mesh
    drawRect(
        color = Color(0x33FFFFFF),
        topLeft = Offset(goalLeft, goalTop),
        size = Size(goalWidth, goalHeight)
    )

    // Crossbar and posts
    val postColor = Color.White
    val postStroke = 12f

    drawLine(color = postColor, start = Offset(goalLeft, goalBottom), end = Offset(goalLeft, goalTop), strokeWidth = postStroke)
    drawLine(color = postColor, start = Offset(goalRight, goalBottom), end = Offset(goalRight, goalTop), strokeWidth = postStroke)
    drawLine(color = postColor, start = Offset(goalLeft, goalTop), end = Offset(goalRight, goalTop), strokeWidth = postStroke)

    // Goalkeeper
    val gkBaseX = w * 0.5f + (gkDiveCurrentX * goalWidth * 0.38f)
    val gkBaseY = goalBottom - 10f

    // GK Body & Jersey
    drawCircle(color = Color(0xFFFFD200), radius = 24f, center = Offset(gkBaseX, gkBaseY - 60f))
    drawCircle(color = Color(0xFFFFCC80), radius = 14f, center = Offset(gkBaseX, gkBaseY - 95f))

    // Aim Reticle
    if (showAimReticle) {
        val reticlePixelX = goalLeft + (aimX * goalWidth)
        val reticlePixelY = goalTop + (aimY * goalHeight)

        drawCircle(color = Color(0xFFFF1744), radius = 22f, center = Offset(reticlePixelX, reticlePixelY), style = Stroke(width = 3.5f))
        drawLine(color = Color(0xFFFF1744), start = Offset(reticlePixelX - 30f, reticlePixelY), end = Offset(reticlePixelX + 30f, reticlePixelY), strokeWidth = 2.5f)
        drawLine(color = Color(0xFFFF1744), start = Offset(reticlePixelX, reticlePixelY - 30f), end = Offset(reticlePixelX, reticlePixelY + 30f), strokeWidth = 2.5f)
    }

    // Ball
    val penaltySpot = Offset(w * 0.5f, h * 0.82f)
    val targetPos = Offset(goalLeft + (ballTargetX * goalWidth), goalTop + (ballTargetY * goalHeight))

    val currentBallX = penaltySpot.x + (targetPos.x - penaltySpot.x) * ballProgress
    val currentBallY = penaltySpot.y + (targetPos.y - penaltySpot.y) * ballProgress
    val ballRadius = 24f - (ballProgress * 11f)

    drawCircle(color = Color.White, radius = ballRadius, center = Offset(currentBallX, currentBallY))
    drawCircle(color = Color.Black, radius = ballRadius * 0.35f, center = Offset(currentBallX, currentBallY))
}
