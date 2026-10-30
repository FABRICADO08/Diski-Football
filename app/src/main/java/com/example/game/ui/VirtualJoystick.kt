package com.example.game.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.game.model.Vector2D
import kotlin.math.sqrt

@Composable
fun VirtualJoystick(
    modifier: Modifier = Modifier,
    radiusDp: Float = 65f,
    onMove: (vector: Vector2D, isSprinting: Boolean) -> Unit,
    onRelease: () -> Unit
) {
    var thumbOffset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .size((radiusDp * 2).dp)
            .testTag("virtual_joystick")
            .pointerInput(Unit) {
                val maxRadiusPx = radiusDp * density
                detectDragGestures(
                    onDragStart = { offset ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val delta = offset - center
                        val dist = sqrt(delta.x * delta.x + delta.y * delta.y)
                        val clampedDist = dist.coerceAtMost(maxRadiusPx)
                        val angle = delta / if (dist > 0.001f) dist else 1f
                        thumbOffset = angle * clampedDist

                        val normX = thumbOffset.x / maxRadiusPx
                        val normY = thumbOffset.y / maxRadiusPx
                        val isSprinting = (clampedDist / maxRadiusPx) > 0.75f
                        onMove(Vector2D(normX, normY), isSprinting)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val delta = change.position - center
                        val dist = sqrt(delta.x * delta.x + delta.y * delta.y)
                        val clampedDist = dist.coerceAtMost(maxRadiusPx)
                        val angle = delta / if (dist > 0.001f) dist else 1f
                        thumbOffset = angle * clampedDist

                        val normX = thumbOffset.x / maxRadiusPx
                        val normY = thumbOffset.y / maxRadiusPx
                        val isSprinting = (clampedDist / maxRadiusPx) > 0.75f
                        onMove(Vector2D(normX, normY), isSprinting)
                    },
                    onDragEnd = {
                        thumbOffset = Offset.Zero
                        onRelease()
                    },
                    onDragCancel = {
                        thumbOffset = Offset.Zero
                        onRelease()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size((radiusDp * 2).dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val baseRadius = radiusDp * density

            // Outer Base Ring
            drawCircle(
                color = Color(0x66000000),
                radius = baseRadius,
                center = center
            )
            drawCircle(
                color = Color(0xFFFFD200).copy(alpha = 0.45f),
                radius = baseRadius,
                center = center,
                style = Stroke(width = 2.5.dp.toPx())
            )

            // Inner Sprint Threshold Ring
            drawCircle(
                color = Color(0xFF00E676).copy(alpha = 0.25f),
                radius = baseRadius * 0.75f,
                center = center,
                style = Stroke(width = 1.dp.toPx())
            )

            // Movable Thumb Stick Knob
            val thumbCenter = center + thumbOffset
            val thumbRadius = 24.dp.toPx()

            // Thumb shadow
            drawCircle(
                color = Color(0x88000000),
                radius = thumbRadius + 2.dp.toPx(),
                center = thumbCenter + Offset(0f, 2.dp.toPx())
            )

            // Thumb fill
            drawCircle(
                color = Color(0xFFFFD200),
                radius = thumbRadius,
                center = thumbCenter
            )

            // Thumb inner accent
            drawCircle(
                color = Color(0xFF003399),
                radius = thumbRadius * 0.45f,
                center = thumbCenter
            )
        }
    }
}
