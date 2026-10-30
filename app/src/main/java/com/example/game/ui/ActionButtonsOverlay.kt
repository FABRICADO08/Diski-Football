package com.example.game.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ActionButtonsOverlay(
    isAttacking: Boolean,
    onPass: () -> Unit,
    onThroughBall: () -> Unit,
    onShoot: (power: Float) -> Unit,
    onLobCross: () -> Unit,
    onKasiSkill: () -> Unit,
    onTackle: () -> Unit,
    onSlideTackle: () -> Unit,
    onSwitchPlayer: () -> Unit,
    onPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Shoot Power Bar state
    var isChargingShot by remember { mutableStateOf(false) }
    var shotPower by remember { mutableFloatStateOf(0.3f) }

    LaunchedEffect(isChargingShot) {
        if (isChargingShot) {
            shotPower = 0.3f
            while (isChargingShot && shotPower < 1.0f) {
                delay(16)
                shotPower = (shotPower + 0.035f).coerceAtMost(1.0f)
            }
        }
    }

    Column(
        modifier = modifier.padding(bottom = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.End
    ) {
        // Power Bar Indicator when charging shot
        if (isChargingShot) {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xDD000000))
                    .border(1.dp, Color(0xFFFFD200), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "SHOT POWER", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(
                            text = "${(shotPower * 100).toInt()}%",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (shotPower > 0.85f) Color(0xFFFF3D00) else Color(0xFFFFD200)
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    LinearProgressIndicator(
                        progress = { shotPower },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (shotPower > 0.85f) Color(0xFFFF3D00) else Color(0xFFFFD200),
                        trackColor = Color(0x55FFFFFF)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Diamond Layout of 4 Main Action Buttons
        Box(
            modifier = Modifier.size(190.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isAttacking) {
                // Top Button: LOB / CROSS
                ActionButtonItem(
                    label = "LOB",
                    subLabel = "Cross",
                    color = Color(0xFF00B0FF),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .testTag("btn_action_lob"),
                    onClick = onLobCross
                )

                // Left Button: THROUGH BALL
                ActionButtonItem(
                    label = "THROUGH",
                    subLabel = "Lead",
                    color = Color(0xFF00E676),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .testTag("btn_action_through"),
                    onClick = onThroughBall
                )

                // Right Button: SHOOT (Hold to charge power)
                ChargeableActionButton(
                    label = "SHOOT",
                    subLabel = "Power",
                    color = Color(0xFFFFD200),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .testTag("btn_action_shoot"),
                    onStartCharging = { isChargingShot = true },
                    onRelease = {
                        isChargingShot = false
                        onShoot(shotPower)
                    }
                )

                // Bottom Button: PASS
                ActionButtonItem(
                    label = "PASS",
                    subLabel = "Ground",
                    color = Color(0xFF2979FF),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .testTag("btn_action_pass"),
                    onClick = onPass
                )

                // Center Button: KASI FLAVOUR (Diski skill move)
                ActionButtonItem(
                    label = "KASI",
                    subLabel = "Flavour",
                    color = Color(0xFFFF007A),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("btn_action_kasi"),
                    onClick = onKasiSkill
                )
            } else {
                // Defending Controls
                // Top Button: PRESS
                ActionButtonItem(
                    label = "PRESS",
                    subLabel = "Team",
                    color = Color(0xFFFF9100),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .testTag("btn_action_press"),
                    onClick = onPress
                )

                // Left Button: SWITCH PLAYER
                ActionButtonItem(
                    label = "SWITCH",
                    subLabel = "Cursor",
                    color = Color(0xFF00B0FF),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .testTag("btn_action_switch"),
                    onClick = onSwitchPlayer
                )

                // Right Button: SLIDE TACKLE
                ActionButtonItem(
                    label = "SLIDE",
                    subLabel = "Hard",
                    color = Color(0xFFFF3D00),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .testTag("btn_action_slide"),
                    onClick = onSlideTackle
                )

                // Bottom Button: STANDING TACKLE
                ActionButtonItem(
                    label = "TACKLE",
                    subLabel = "Clean",
                    color = Color(0xFF00E676),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .testTag("btn_action_tackle"),
                    onClick = onTackle
                )
            }
        }
    }
}

@Composable
private fun ActionButtonItem(
    label: String,
    subLabel: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.9f else 1.0f, label = "button_scale")

    Box(
        modifier = modifier
            .size(58.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = 0.85f),
                        color.copy(alpha = 0.5f)
                    )
                )
            )
            .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = subLabel,
                fontSize = 8.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun ChargeableActionButton(
    label: String,
    subLabel: String,
    color: Color,
    modifier: Modifier = Modifier,
    onStartCharging: () -> Unit,
    onRelease: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.92f else 1.0f, label = "charge_button_scale")

    Box(
        modifier = modifier
            .size(62.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = 0.95f),
                        color.copy(alpha = 0.65f)
                    )
                )
            )
            .border(2.5.dp, Color.White, CircleShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        onStartCharging()
                        tryAwaitRelease()
                        isPressed = false
                        onRelease()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F1E14)
            )
            Text(
                text = subLabel,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F1E14).copy(alpha = 0.85f)
            )
        }
    }
}
