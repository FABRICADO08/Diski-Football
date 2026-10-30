package com.example.game.ui

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.model.TeamEntity
import com.example.game.sound.DiskiAudioAndHaptics
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.DiskiGold
import com.example.ui.theme.PitchGreen
import com.example.ui.theme.PitchGreenDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

enum class PenaltyTurnType {
    USER_KICKING,
    CPU_KICKING
}

enum class PenaltyShotResult {
    GOAL,
    SAVED,
    MISSED_WIDE
}

@Composable
fun PenaltyShootoutScreen(
    homeTeam: TeamEntity,
    awayTeam: TeamEntity,
    onFinished: (homeScore: Int, awayScore: Int) -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val audioHaptics = remember { DiskiAudioAndHaptics(context) }

    // Penalty rounds state
    val homePenalties = remember { mutableStateListOf<Boolean?>() }
    val awayPenalties = remember { mutableStateListOf<Boolean?>() }

    var currentTurn by remember { mutableStateOf(PenaltyTurnType.USER_KICKING) }
    var currentRound by remember { mutableIntStateOf(1) }
    var homeScore by remember { mutableIntStateOf(0) }
    var awayScore by remember { mutableIntStateOf(0) }

    var isShootoutOver by remember { mutableStateOf(false) }
    var winnerName by remember { mutableStateOf<String?>(null) }
    var commentaryText by remember { mutableStateOf("Welcome to the South African Penalty Shootout! Sudden death pressure!") }

    // Aim Reticle & Ball Animation
    var aimX by remember { mutableFloatStateOf(0.5f) } // 0.0 (left) to 1.0 (right)
    var aimY by remember { mutableFloatStateOf(0.5f) } // 0.0 (top) to 1.0 (bottom)
    var isAimingActive by remember { mutableStateOf(true) }

    var isChargingPower by remember { mutableStateOf(false) }
    var shotPower by remember { mutableFloatStateOf(0.4f) }

    // Ball Animation: 0f (penalty spot) to 1f (goal plane)
    var ballProgress by remember { mutableFloatStateOf(0f) }
    var isBallInFlight by remember { mutableStateOf(false) }
    var ballTargetX by remember { mutableFloatStateOf(0.5f) }
    var ballTargetY by remember { mutableFloatStateOf(0.5f) }

    // Goalkeeper Dive State: -1f (left), 0f (center), +1f (right)
    var gkDiveTargetX by remember { mutableFloatStateOf(0f) }
    var gkDiveCurrentX by remember { mutableFloatStateOf(0f) }
    var gkDiveProgress by remember { mutableFloatStateOf(0f) }

    var lastResult by remember { mutableStateOf<PenaltyShotResult?>(null) }

    // Aim oscillating reticle loop
    LaunchedEffect(isAimingActive, currentTurn) {
        if (isAimingActive && currentTurn == PenaltyTurnType.USER_KICKING) {
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

    // Power Charge Loop
    LaunchedEffect(isChargingPower) {
        if (isChargingPower) {
            shotPower = 0.35f
            while (isChargingPower && shotPower < 1.0f) {
                delay(16)
                shotPower = (shotPower + 0.04f).coerceAtMost(1.0f)
            }
        }
    }

    // Ball Flight & GK Dive Animation Loop
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

            // Determine Outcome
            val goalWidthTolerance = 0.22f
            val gkDist = abs(ballTargetX - (0.5f + gkDiveTargetX * 0.42f))
            val isSaved = gkDist < goalWidthTolerance && ballTargetY in 0.15f..0.85f
            val isMissed = ballTargetX < 0.08f || ballTargetX > 0.92f || ballTargetY < 0.05f

            val result = when {
                isMissed -> PenaltyShotResult.MISSED_WIDE
                isSaved -> PenaltyShotResult.SAVED
                else -> PenaltyShotResult.GOAL
            }
            lastResult = result

            if (currentTurn == PenaltyTurnType.USER_KICKING) {
                val scored = (result == PenaltyShotResult.GOAL)
                homePenalties.add(scored)
                if (scored) {
                    homeScore++
                    audioHaptics.playGoalRoar()
                    audioHaptics.hapticGoalScored()
                    commentaryText = "LADUMA! ${homeTeam.shortName} score! Emphatic penalty strike!"
                } else if (result == PenaltyShotResult.SAVED) {
                    audioHaptics.playPostHitSound()
                    audioHaptics.hapticPostHit()
                    commentaryText = "SAVED! The goalkeeper guesses right and blocks the penalty!"
                } else {
                    audioHaptics.playPostHitSound()
                    audioHaptics.hapticPostHit()
                    commentaryText = "OFF TARGET! Put wide into the stands!"
                }
            } else {
                val scored = (result == PenaltyShotResult.GOAL)
                awayPenalties.add(scored)
                if (scored) {
                    awayScore++
                    audioHaptics.playGoalRoar()
                    audioHaptics.hapticGoalScored()
                    commentaryText = "GOAL! ${awayTeam.shortName} convert their spot kick with ice in their veins!"
                } else if (result == PenaltyShotResult.SAVED) {
                    audioHaptics.playPostHitSound()
                    audioHaptics.hapticGoalScored()
                    commentaryText = "WHAT A SAVE! You denied ${awayTeam.shortName}!"
                } else {
                    audioHaptics.playPostHitSound()
                    commentaryText = "MISSED! ${awayTeam.shortName} send it wide of the post!"
                }
            }

            delay(2200)

            // Check if Shootout Complete
            checkShootoutStatus(
                homeScores = homeScore,
                awayScores = awayScore,
                homeAttempts = homePenalties.size,
                awayAttempts = awayPenalties.size,
                homeTeam = homeTeam,
                awayTeam = awayTeam,
                onGameOver = { winner ->
                    isShootoutOver = true
                    winnerName = winner
                },
                onNextTurn = { nextTurn, nextRound ->
                    currentTurn = nextTurn
                    currentRound = nextRound
                    resetForNextKick(nextTurn)
                }
            )
        }
    }

    fun startUserShot() {
        if (!isAimingActive || isBallInFlight) return
        isAimingActive = false

        // Aim spot with slight deviation based on shot power
        val devX = (Random.nextFloat() - 0.5f) * (shotPower * 0.25f)
        val devY = (Random.nextFloat() - 0.5f) * (shotPower * 0.25f)
        ballTargetX = (aimX + devX).coerceIn(0.05f, 0.95f)
        ballTargetY = (aimY + devY).coerceIn(0.1f, 0.9f)

        // Goalkeeper AI guess: Left (-1), Center (0), Right (+1)
        val gkChoices = listOf(-1f, -0.6f, 0f, 0.6f, 1f)
        gkDiveTargetX = gkChoices.random()

        audioHaptics.playKickSound(isHardShot = shotPower > 0.7f)
        audioHaptics.hapticBallKick()

        isBallInFlight = true
    }

    fun startCpuShot(userGkChoice: Float) {
        if (isBallInFlight) return
        // User picked GK dive direction
        gkDiveTargetX = userGkChoice

        // CPU striker aims towards a corner
        ballTargetX = (listOf(0.2f, 0.35f, 0.5f, 0.65f, 0.8f).random() + (Random.nextFloat() - 0.5f) * 0.1f).coerceIn(0.1f, 0.9f)
        ballTargetY = (listOf(0.3f, 0.5f, 0.7f).random() + (Random.nextFloat() - 0.5f) * 0.1f).coerceIn(0.15f, 0.85f)

        audioHaptics.playKickSound(isHardShot = true)
        audioHaptics.hapticBallKick()

        isBallInFlight = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A180E))
            .testTag("penalty_shootout_screen")
    ) {
        // 1. Perspective 3D Goal Pitch Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawShootoutScene(
                ballProgress = ballProgress,
                ballTargetX = ballTargetX,
                ballTargetY = ballTargetY,
                gkDiveCurrentX = gkDiveCurrentX,
                isBallInFlight = isBallInFlight,
                aimX = aimX,
                aimY = aimY,
                showAimReticle = (currentTurn == PenaltyTurnType.USER_KICKING && isAimingActive)
            )
        }

        // 2. Scoreboard & Round Indicators
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
                    Text(
                        text = "PENALTY SHOOTOUT",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = DiskiGold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Home Team
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(text = homeTeam.shortName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            PenaltyPegsRow(penalties = homePenalties)
                        }

                        // Score
                        Text(
                            text = "$homeScore - $awayScore",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            color = DiskiGold
                        )

                        // Away Team
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = awayTeam.shortName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            PenaltyPegsRow(penalties = awayPenalties)
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
                    text = commentaryText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        // 3. User Controls for Striker Mode (Aim + Shoot with Power Gauge)
        if (currentTurn == PenaltyTurnType.USER_KICKING && !isBallInFlight && !isShootoutOver) {
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
                            startUserShot()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DiskiGold),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .height(52.dp)
                        .width(220.dp)
                        .testTag("btn_take_penalty")
                ) {
                    Text(
                        text = if (!isChargingPower) "AIM & CHARGE POWER" else "UNLEASH PENALTY!",
                        color = SurfaceDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        // 4. User Controls for Goalkeeper Mode (Choose Dive Direction)
        if (currentTurn == PenaltyTurnType.CPU_KICKING && !isBallInFlight && !isShootoutOver) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "PREDICT OPPONENT SHOT: CHOOSE DIVE DIRECTION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = DiskiGold
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Button(
                        onClick = { startCpuShot(-1.0f) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B0FF)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).testTag("btn_gk_dive_left")
                    ) {
                        Text(text = "⬅ DIVE LEFT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Button(
                        onClick = { startCpuShot(0f) },
                        colors = ButtonDefaults.buttonColors(containerColor = PitchGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).testTag("btn_gk_stay_center")
                    ) {
                        Text(text = "⬆ CENTER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Button(
                        onClick = { startCpuShot(1.0f) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B0FF)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).testTag("btn_gk_dive_right")
                    ) {
                        Text(text = "DIVE RIGHT ➡", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // 5. Game Over / Shootout Result Dialog
        AnimatedVisibility(
            visible = isShootoutOver,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = PitchGreenDark),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, DiskiGold),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .testTag("dialog_shootout_over")
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🏆 SHOOTOUT FINISHED 🏆",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = DiskiGold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "${homeTeam.shortName} $homeScore - $awayScore ${awayTeam.shortName}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$winnerName WIN ON PENALTIES!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AccentCyan,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { onFinished(homeScore, awayScore) },
                        colors = ButtonDefaults.buttonColors(containerColor = DiskiGold),
                        modifier = Modifier.fillMaxWidth().testTag("btn_finish_shootout")
                    ) {
                        Text(text = "CONTINUE", color = SurfaceDark, fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

private fun resetForNextKick(nextTurn: PenaltyTurnType) {
    // handled via state resetting inside component
}

private fun checkShootoutStatus(
    homeScores: Int,
    awayScores: Int,
    homeAttempts: Int,
    awayAttempts: Int,
    homeTeam: TeamEntity,
    awayTeam: TeamEntity,
    onGameOver: (winner: String) -> Unit,
    onNextTurn: (nextTurn: PenaltyTurnType, round: Int) -> Unit
) {
    val totalRounds = maxOf(homeAttempts, awayAttempts)

    // Standard 5 rounds rule
    if (homeAttempts <= 5 && awayAttempts <= 5) {
        val homeRemaining = 5 - homeAttempts
        val awayRemaining = 5 - awayAttempts

        if (homeScores > awayScores + awayRemaining) {
            onGameOver(homeTeam.name)
            return
        }
        if (awayScores > homeScores + homeRemaining) {
            onGameOver(awayTeam.name)
            return
        }
    }

    // After equal attempts >= 5, sudden death
    if (homeAttempts >= 5 && awayAttempts >= 5 && homeAttempts == awayAttempts) {
        if (homeScores > awayScores) {
            onGameOver(homeTeam.name)
            return
        }
        if (awayScores > homeScores) {
            onGameOver(awayTeam.name)
            return
        }
    }

    // Continue to next kick
    val nextTurn = if (homeAttempts > awayAttempts) PenaltyTurnType.CPU_KICKING else PenaltyTurnType.USER_KICKING
    val nextRound = if (nextTurn == PenaltyTurnType.USER_KICKING) totalRounds + 1 else totalRounds
    onNextTurn(nextTurn, nextRound)
}

@Composable
private fun PenaltyPegsRow(penalties: List<Boolean?>) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in 0 until 5) {
            val status = penalties.getOrNull(i)
            val color = when (status) {
                true -> Color(0xFF00E676)  // Scored Green
                false -> Color(0xFFFF1744) // Missed Red
                null -> Color(0x44FFFFFF)  // Pending
            }
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape)
            )
        }
    }
}

private fun DrawScope.drawShootoutScene(
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

    // 1. Stadium Stands Background (Dark Night Sky + Stadium Crowd)
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF07140B), Color(0xFF132F1A))
        ),
        topLeft = Offset.Zero,
        size = Size(w, h * 0.45f)
    )

    // Stadium Floodlights glow
    drawCircle(color = Color(0x33FFFFDD), radius = 80f, center = Offset(w * 0.15f, h * 0.12f))
    drawCircle(color = Color(0x33FFFFDD), radius = 80f, center = Offset(w * 0.85f, h * 0.12f))

    // 2. Pitch Lawn (Perspective Grass Trapezoid)
    val grassPath = Path().apply {
        moveTo(0f, h * 0.45f)
        lineTo(w, h * 0.45f)
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }
    drawPath(path = grassPath, color = Color(0xFF1B6B2F))

    // Perspective pitch stripes
    for (i in 1..4) {
        val y = h * (0.45f + i * 0.13f)
        drawLine(
            color = Color(0x22FFFFFF),
            start = Offset(0f, y),
            end = Offset(w, y),
            strokeWidth = 3f
        )
    }

    // 3. Regulation Goal Frame & Net (Perspective view from penalty spot)
    val goalLeft = w * 0.14f
    val goalRight = w * 0.86f
    val goalWidth = goalRight - goalLeft
    val goalTop = h * 0.28f
    val goalBottom = h * 0.62f
    val goalHeight = goalBottom - goalTop

    // Net back mesh
    drawRect(
        color = Color(0x28FFFFFF),
        topLeft = Offset(goalLeft, goalTop),
        size = Size(goalWidth, goalHeight)
    )
    for (step in 1..8) {
        val x = goalLeft + (goalWidth * (step / 9f))
        drawLine(color = Color(0x33FFFFFF), start = Offset(x, goalTop), end = Offset(x, goalBottom), strokeWidth = 1f)
    }
    for (step in 1..5) {
        val y = goalTop + (goalHeight * (step / 6f))
        drawLine(color = Color(0x33FFFFFF), start = Offset(goalLeft, y), end = Offset(goalRight, y), strokeWidth = 1f)
    }

    // White Goal Posts and Crossbar
    val postStroke = Stroke(width = 10f)
    // Crossbar
    drawLine(color = Color.White, start = Offset(goalLeft, goalTop), end = Offset(goalRight, goalTop), strokeWidth = 10f)
    // Left Post
    drawLine(color = Color.White, start = Offset(goalLeft, goalTop), end = Offset(goalLeft, goalBottom), strokeWidth = 10f)
    // Right Post
    drawLine(color = Color.White, start = Offset(goalRight, goalTop), end = Offset(goalRight, goalBottom), strokeWidth = 10f)

    // Goal Line on pitch
    drawLine(color = Color(0xEEFFFFFF), start = Offset(goalLeft - 30f, goalBottom), end = Offset(goalRight + 30f, goalBottom), strokeWidth = 6f)

    // 4. Goalkeeper
    val gkBaseX = goalLeft + (goalWidth * 0.5f) + (gkDiveCurrentX * (goalWidth * 0.4f))
    val gkBaseY = goalBottom - 18f
    val gkRadius = 26f

    // GK Drop shadow
    drawCircle(color = Color(0x66000000), radius = gkRadius * 1.1f, center = Offset(gkBaseX, gkBaseY + 12f))
    // GK Jersey (Bright Neon Orange)
    drawCircle(color = Color(0xFFFF6D00), radius = gkRadius, center = Offset(gkBaseX, gkBaseY))
    drawCircle(color = Color.White, radius = gkRadius, center = Offset(gkBaseX, gkBaseY), style = Stroke(width = 3f))
    // GK Gloves
    drawCircle(color = Color(0xFFFFFF00), radius = 8f, center = Offset(gkBaseX - gkRadius * 1.1f, gkBaseY - 6f))
    drawCircle(color = Color(0xFFFFFF00), radius = 8f, center = Offset(gkBaseX + gkRadius * 1.1f, gkBaseY - 6f))

    // 5. Aiming Reticle (When user is aiming)
    if (showAimReticle) {
        val reticleX = goalLeft + (goalWidth * aimX)
        val reticleY = goalTop + (goalHeight * aimY)

        drawCircle(
            color = Color(0xFFFFD200),
            radius = 24f,
            center = Offset(reticleX, reticleY),
            style = Stroke(width = 3.5f)
        )
        // Crosshairs
        drawLine(color = Color(0xFFFFD200), start = Offset(reticleX - 32f, reticleY), end = Offset(reticleX + 32f, reticleY), strokeWidth = 2.5f)
        drawLine(color = Color(0xFFFFD200), start = Offset(reticleX, reticleY - 32f), end = Offset(reticleX, reticleY + 32f), strokeWidth = 2.5f)
    }

    // 6. Penalty Spot (Foreground X=center, Y=h*0.88f)
    val penSpot = Offset(w * 0.5f, h * 0.88f)
    drawCircle(color = Color.White, radius = 7f, center = penSpot)

    // 7. Football
    val startBallPos = penSpot
    val endBallPos = Offset(goalLeft + (goalWidth * ballTargetX), goalTop + (goalHeight * ballTargetY))

    val currentBallX = startBallPos.x + (endBallPos.x - startBallPos.x) * ballProgress
    val currentBallY = startBallPos.y + (endBallPos.y - startBallPos.y) * ballProgress

    // Ball gets smaller as it flies away into the distance
    val baseBallRadius = 18f
    val ballRadius = baseBallRadius * (1f - (ballProgress * 0.55f))

    // Ball shadow
    drawCircle(
        color = Color(0x66000000),
        radius = ballRadius * 1.1f,
        center = Offset(currentBallX, currentBallY + (6f * (1f - ballProgress)))
    )
    // Ball white sphere
    drawCircle(color = Color.White, radius = ballRadius, center = Offset(currentBallX, currentBallY))
    drawCircle(color = Color(0xFF222222), radius = ballRadius, center = Offset(currentBallX, currentBallY), style = Stroke(width = 2f))
    // Pentagon center
    drawCircle(color = Color(0xFF1E1E1E), radius = ballRadius * 0.42f, center = Offset(currentBallX, currentBallY))
}
