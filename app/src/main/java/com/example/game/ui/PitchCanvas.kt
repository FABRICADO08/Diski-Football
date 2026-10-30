package com.example.game.ui

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import com.example.game.model.GameBall
import com.example.game.model.GamePlayer
import com.example.game.model.PlayerActionState
import com.example.game.model.PlayerTeamSide
import com.example.game.model.Vector2D
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PitchCanvas(
    players: List<GamePlayer>,
    ball: GameBall,
    cameraPosition: Vector2D,
    homeKitPrimary: Color,
    homeKitSecondary: Color,
    awayKitPrimary: Color,
    awayKitSecondary: Color,
    activeControlledPlayerId: String,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .testTag("pitch_canvas")
    ) {
        val viewWidth = size.width
        val viewHeight = size.height

        // Camera & Scale mapping (World pitch is 1000 x 650)
        // Zoom factor to provide comfortable view of the pitch
        val scale = (viewHeight / 420f).coerceIn(1.1f, 1.8f)
        val viewCenterX = viewWidth / 2f
        val viewCenterY = viewHeight / 2f

        fun toScreen(world: Vector2D): Offset {
            val sx = viewCenterX + (world.x - cameraPosition.x) * scale
            val sy = viewCenterY + (world.y - cameraPosition.y) * scale
            return Offset(sx, sy)
        }

        // 1. Draw Pitch Grass & Stripes
        drawPitchLawn(::toScreen, scale)

        // 2. Draw Pitch Markings (White Lines, Boxes, Circles)
        drawPitchMarkings(::toScreen, scale)

        // 3. Draw Players
        drawPlayers(
            players = players,
            toScreen = ::toScreen,
            scale = scale,
            homeKitPrimary = homeKitPrimary,
            homeKitSecondary = homeKitSecondary,
            awayKitPrimary = awayKitPrimary,
            awayKitSecondary = awayKitSecondary,
            activeControlledPlayerId = activeControlledPlayerId
        )

        // 4. Draw Ball & Ball Shadow
        drawBall(ball, ::toScreen, scale)

        // 5. Mini-map Radar in corner
        drawRadarMiniMap(players, ball, viewWidth)
    }
}

private fun DrawScope.drawPitchLawn(toScreen: (Vector2D) -> Offset, scale: Float) {
    // Pitch dimensions
    val pTopLeft = toScreen(Vector2D(0f, 0f))
    val pBottomRight = toScreen(Vector2D(1000f, 650f))

    val pitchRectWidth = (1000f * scale)
    val pitchRectHeight = (650f * scale)

    // Base pitch green
    drawRect(
        color = Color(0xFF1B6B2F),
        topLeft = pTopLeft,
        size = Size(pitchRectWidth, pitchRectHeight)
    )

    // Alternating lawn mowed stripes (10 stripes across pitch)
    val stripeWidth = 100f * scale
    for (i in 0 until 10) {
        if (i % 2 == 1) {
            val stripeLeft = pTopLeft.x + (i * stripeWidth)
            drawRect(
                color = Color(0xFF175E28),
                topLeft = Offset(stripeLeft, pTopLeft.y),
                size = Size(stripeWidth, pitchRectHeight)
            )
        }
    }
}

private fun DrawScope.drawPitchMarkings(toScreen: (Vector2D) -> Offset, scale: Float) {
    val lineWidth = 2.5f * scale
    val white = Color(0xEEFFFFFF)
    val strokeStyle = Stroke(width = lineWidth)

    val pTopLeft = toScreen(Vector2D(0f, 0f))
    val pitchRectWidth = 1000f * scale
    val pitchRectHeight = 650f * scale

    // Boundary Rect
    drawRect(
        color = white,
        topLeft = pTopLeft,
        size = Size(pitchRectWidth, pitchRectHeight),
        style = strokeStyle
    )

    // Halfway Line
    val halfTop = toScreen(Vector2D(500f, 0f))
    val halfBottom = toScreen(Vector2D(500f, 650f))
    drawLine(color = white, start = halfTop, end = halfBottom, strokeWidth = lineWidth)

    // Center Circle & Spot
    val center = toScreen(Vector2D(500f, 325f))
    drawCircle(color = white, radius = 68f * scale, center = center, style = strokeStyle)
    drawCircle(color = white, radius = 3f * scale, center = center)

    // Left Penalty Box (165 x 350)
    val lBoxTopLeft = toScreen(Vector2D(0f, 150f))
    drawRect(color = white, topLeft = lBoxTopLeft, size = Size(165f * scale, 350f * scale), style = strokeStyle)

    // Left 6-Yard Box (55 x 180)
    val l6TopLeft = toScreen(Vector2D(0f, 235f))
    drawRect(color = white, topLeft = l6TopLeft, size = Size(55f * scale, 180f * scale), style = strokeStyle)

    // Left Penalty Spot
    val lPenSpot = toScreen(Vector2D(110f, 325f))
    drawCircle(color = white, radius = 2.5f * scale, center = lPenSpot)

    // Right Penalty Box (165 x 350)
    val rBoxTopLeft = toScreen(Vector2D(835f, 150f))
    drawRect(color = white, topLeft = rBoxTopLeft, size = Size(165f * scale, 350f * scale), style = strokeStyle)

    // Right 6-Yard Box (55 x 180)
    val r6TopLeft = toScreen(Vector2D(945f, 235f))
    drawRect(color = white, topLeft = r6TopLeft, size = Size(55f * scale, 180f * scale), style = strokeStyle)

    // Right Penalty Spot
    val rPenSpot = toScreen(Vector2D(890f, 325f))
    drawCircle(color = white, radius = 2.5f * scale, center = rPenSpot)

    // Left Goal Net (X=-22 to 0, Y=275 to 375)
    val lGoalTopLeft = toScreen(Vector2D(-22f, 275f))
    drawRect(
        color = Color(0x77FFFFFF),
        topLeft = lGoalTopLeft,
        size = Size(22f * scale, 100f * scale),
        style = Stroke(width = 1.5f * scale)
    )
    drawRect(
        color = Color(0x22FFFFFF),
        topLeft = lGoalTopLeft,
        size = Size(22f * scale, 100f * scale)
    )

    // Right Goal Net (X=1000 to 1022, Y=275 to 375)
    val rGoalTopLeft = toScreen(Vector2D(1000f, 275f))
    drawRect(
        color = Color(0x77FFFFFF),
        topLeft = rGoalTopLeft,
        size = Size(22f * scale, 100f * scale),
        style = Stroke(width = 1.5f * scale)
    )
    drawRect(
        color = Color(0x22FFFFFF),
        topLeft = rGoalTopLeft,
        size = Size(22f * scale, 100f * scale)
    )

    // Corner Arcs
    drawCircle(color = white, radius = 12f * scale, center = toScreen(Vector2D(0f, 0f)), style = strokeStyle)
    drawCircle(color = white, radius = 12f * scale, center = toScreen(Vector2D(0f, 650f)), style = strokeStyle)
    drawCircle(color = white, radius = 12f * scale, center = toScreen(Vector2D(1000f, 0f)), style = strokeStyle)
    drawCircle(color = white, radius = 12f * scale, center = toScreen(Vector2D(1000f, 650f)), style = strokeStyle)
}

private fun DrawScope.drawPlayers(
    players: List<GamePlayer>,
    toScreen: (Vector2D) -> Offset,
    scale: Float,
    homeKitPrimary: Color,
    homeKitSecondary: Color,
    awayKitPrimary: Color,
    awayKitSecondary: Color,
    activeControlledPlayerId: String
) {
    val playerRadius = 12.5f * scale

    val textPaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 9f * scale
        typeface = Typeface.DEFAULT_BOLD
    }

    val badgePaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.YELLOW
        textAlign = Paint.Align.CENTER
        textSize = 10f * scale
        typeface = Typeface.DEFAULT_BOLD
    }

    val skillPaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.parseColor("#00E5FF")
        textAlign = Paint.Align.CENTER
        textSize = 11f * scale
        typeface = Typeface.DEFAULT_BOLD
        setShadowLayer(4f, 0f, 0f, android.graphics.Color.BLACK)
    }

    val stunPaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.parseColor("#FF5252")
        textAlign = Paint.Align.CENTER
        textSize = 10f * scale
        typeface = Typeface.DEFAULT_BOLD
        setShadowLayer(3f, 0f, 0f, android.graphics.Color.BLACK)
    }

    for (player in players) {
        val screenPos = toScreen(player.position)

        val isHome = (player.teamSide == PlayerTeamSide.HOME)
        val primaryColor = if (isHome) homeKitPrimary else awayKitPrimary
        val secondaryColor = if (isHome) homeKitSecondary else awayKitSecondary

        // 1. Sliding grass trail effect
        if (player.state == PlayerActionState.SLIDING) {
            val backOffset = player.facingDirection.normalized() * (-18f * scale)
            drawCircle(
                color = Color(0x66FFFFFF),
                radius = playerRadius * 1.3f,
                center = screenPos + Offset(backOffset.x, backOffset.y)
            )
        }

        // 1b. Kasi Flavour Skill Move Glow
        if (player.state == PlayerActionState.SKILL_MOVE) {
            drawCircle(
                color = Color(0x88FFD700),
                radius = playerRadius * 2.2f,
                center = screenPos
            )
            drawCircle(
                color = Color(0x5500E5FF),
                radius = playerRadius * 1.7f,
                center = screenPos
            )
        }

        // 2. Drop Shadow
        drawCircle(
            color = Color(0x55000000),
            radius = playerRadius * 1.05f,
            center = screenPos + Offset(0f, 3f * scale)
        )

        // 3. User Selection Indicator Ring
        if (player.id == activeControlledPlayerId) {
            drawCircle(
                color = Color(0xFFFFD200),
                radius = playerRadius * 1.65f,
                center = screenPos,
                style = Stroke(width = 2.5f * scale)
            )

            // Overhead floating badge with name and position
            val badgeY = screenPos.y - (playerRadius * 2.2f)
            drawContext.canvas.nativeCanvas.drawText(
                "${player.positionLabel} ${player.name.take(10)}",
                screenPos.x,
                badgeY,
                badgePaint
            )

            if (player.state == PlayerActionState.SKILL_MOVE && player.activeSkillName != null) {
                drawContext.canvas.nativeCanvas.drawText(
                    "✨ ${player.activeSkillName} ✨",
                    screenPos.x,
                    badgeY - (13f * scale),
                    skillPaint
                )
            }
        }

        // 3b. Stunned indicator for defending AI
        if (player.isStunned) {
            drawContext.canvas.nativeCanvas.drawText(
                "💫 BAMBOOZLED!",
                screenPos.x,
                screenPos.y - (playerRadius * 2.0f),
                stunPaint
            )
        }

        // 4. Jersey Circle
        drawCircle(
            color = primaryColor,
            radius = playerRadius,
            center = screenPos
        )

        // Secondary color collar/rim
        drawCircle(
            color = secondaryColor,
            radius = playerRadius,
            center = screenPos,
            style = Stroke(width = 2.2f * scale)
        )

        // 5. Facing Direction pointer
        val dir = player.facingDirection.normalized()
        val pointerTip = screenPos + Offset(dir.x * playerRadius * 1.3f, dir.y * playerRadius * 1.3f)
        drawCircle(
            color = Color.White,
            radius = 2.2f * scale,
            center = pointerTip
        )

        // 6. Jersey Number
        val textY = screenPos.y + (3.5f * scale)
        drawContext.canvas.nativeCanvas.drawText(
            player.jerseyNumber.toString(),
            screenPos.x,
            textY,
            textPaint
        )
    }
}

private fun DrawScope.drawBall(
    ball: GameBall,
    toScreen: (Vector2D) -> Offset,
    scale: Float
) {
    val groundPos = toScreen(ball.position)
    val elevationOffset = ball.heightZ * scale * 0.7f
    val ballDrawPos = Offset(groundPos.x, groundPos.y - elevationOffset)

    val baseBallRadius = 5.5f * scale
    // Ball scales slightly up when high in air
    val airborneScale = 1.0f + (ball.heightZ / 300f).coerceIn(0f, 0.4f)
    val ballRadius = baseBallRadius * airborneScale

    // 1. Ground Shadow (Fades and grows when airborne)
    val shadowAlpha = (0.5f - (ball.heightZ / 600f)).coerceIn(0.15f, 0.5f)
    val shadowRadius = (baseBallRadius + (ball.heightZ * 0.05f)) * scale
    drawCircle(
        color = Color.Black.copy(alpha = shadowAlpha),
        radius = shadowRadius,
        center = groundPos + Offset(0f, 1.5f * scale)
    )

    // 2. Ball Body
    drawCircle(
        color = Color.White,
        radius = ballRadius,
        center = ballDrawPos
    )
    drawCircle(
        color = Color(0xFF222222),
        radius = ballRadius,
        center = ballDrawPos,
        style = Stroke(width = 1f * scale)
    )

    // 3. Pentagon center patch for realistic soccer ball look
    drawCircle(
        color = Color(0xFF1E1E1E),
        radius = ballRadius * 0.4f,
        center = ballDrawPos
    )
}

private fun DrawScope.drawRadarMiniMap(
    players: List<GamePlayer>,
    ball: GameBall,
    viewWidth: Float
) {
    val radarWidth = 110f
    val radarHeight = 72f
    val margin = 16f
    val radarTopLeft = Offset(viewWidth - radarWidth - margin, margin + 48f)

    // Semi-transparent dark green pitch box
    drawRect(
        color = Color(0xCC0D2814),
        topLeft = radarTopLeft,
        size = Size(radarWidth, radarHeight)
    )
    drawRect(
        color = Color(0x88FFD200),
        topLeft = radarTopLeft,
        size = Size(radarWidth, radarHeight),
        style = Stroke(width = 1.5f)
    )

    // Halfway line in radar
    drawLine(
        color = Color(0x55FFFFFF),
        start = Offset(radarTopLeft.x + (radarWidth / 2f), radarTopLeft.y),
        end = Offset(radarTopLeft.x + (radarWidth / 2f), radarTopLeft.y + radarHeight),
        strokeWidth = 1f
    )

    fun toRadar(world: Vector2D): Offset {
        val rx = radarTopLeft.x + (world.x / 1000f) * radarWidth
        val ry = radarTopLeft.y + (world.y / 650f) * radarHeight
        return Offset(rx, ry)
    }

    // Players dots
    for (p in players) {
        val dotColor = if (p.teamSide == PlayerTeamSide.HOME) Color(0xFFFFD200) else Color(0xFFFFFFFF)
        val rPos = toRadar(p.position)
        drawCircle(color = dotColor, radius = 2.2f, center = rPos)
    }

    // Ball dot (Pulsing bright cyan)
    val bPos = toRadar(ball.position)
    drawCircle(color = Color(0xFF00E5FF), radius = 3.2f, center = bPos)
}
