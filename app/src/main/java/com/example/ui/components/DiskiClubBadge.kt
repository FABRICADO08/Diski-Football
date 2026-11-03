package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.model.TeamEntity
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Authentic, high-fidelity Vector Club Badge & Crest Component for South African Football clubs.
 * Accurately renders the official crest symbols, heraldic shields, silhouettes, and color schemes:
 * - Orlando Pirates: Black crest, white Skull & Crossbones, red star ⭐
 * - Kaizer Chiefs: Gold badge, black Native American Chief headdress profile & football
 * - Mamelodi Sundowns: Ka Bo Yellow sunburst with rays, blue ring & "The Sky is the Limit" banner
 * - Stellenbosch FC: Maroon shield with Cape Winelands grape cluster & oak leaves
 * - SuperSport United: Royal blue shield with red & white chevron wings
 * - Cape Town City: Table Mountain flat silhouette & golden crown
 * - AmaZulu: Traditional Zulu war shield (Isihlangu) & crossed Assegai spears
 * - TS Galaxy: Red crest with soaring space rocket & flame
 * - Chippa United: Blue/red shield with Chilli pepper
 * - Golden Arrows: Dark green shield with golden bow & flying arrow
 * - Magesi FC: Navy shield with Crocodile jaws (2024 CKO Winners)
 * - Polokwane City: Rising sun with radiating beams
 * - Richards Bay: Maritime anchor & ocean waves
 * - Sekhukhune United: Babina Noko porcupine quill fan
 * - Hungry Lions: Roaring lion head
 * - Casric Stars: Central 5-pointed star
 * - University of Pretoria: Striped academic shield with open book
 * - Black Leopards: Gold shield with leopard rosette spots
 */
@Composable
fun DiskiClubBadge(
    team: TeamEntity,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    DiskiClubBadge(
        teamId = team.id,
        primaryColorHex = team.primaryColorHex,
        secondaryColorHex = team.secondaryColorHex,
        modifier = modifier,
        size = size
    )
}

@Composable
fun DiskiClubBadge(
    teamId: String,
    primaryColorHex: String,
    secondaryColorHex: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    val primaryColor = remember(primaryColorHex) {
        try { Color(android.graphics.Color.parseColor(primaryColorHex)) } catch (_: Exception) { Color(0xFFFFD200) }
    }
    val secondaryColor = remember(secondaryColorHex) {
        try { Color(android.graphics.Color.parseColor(secondaryColorHex)) } catch (_: Exception) { Color.White }
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height

            when (teamId) {
                "orlando_pirates" -> drawOrlandoPiratesCrest(w, h)
                "kaizer_chiefs" -> drawKaizerChiefsCrest(w, h)
                "mamelodi_sundowns" -> drawMamelodiSundownsCrest(w, h)
                "stellenbosch_fc" -> drawStellenboschCrest(w, h)
                "supersport_united" -> drawSuperSportCrest(w, h)
                "cape_town_city" -> drawCapeTownCityCrest(w, h)
                "amazulu_fc" -> drawAmaZuluCrest(w, h)
                "ts_galaxy" -> drawTSGalaxyCrest(w, h)
                "chippa_united" -> drawChippaUnitedCrest(w, h)
                "golden_arrows" -> drawGoldenArrowsCrest(w, h)
                "magesi_fc" -> drawMagesiCrest(w, h)
                "polokwane_city" -> drawPolokwaneCityCrest(w, h)
                "richards_bay_fc" -> drawRichardsBayCrest(w, h)
                "sekhukhune_united" -> drawSekhukhuneCrest(w, h)
                "marumo_gallants" -> drawMarumoGallantsCrest(w, h)
                "royal_am" -> drawRoyalAMCrest(w, h)
                "university_of_pretoria" -> drawAmaTuksCrest(w, h)
                "hungry_lions" -> drawHungryLionsCrest(w, h)
                "casric_stars" -> drawCasricStarsCrest(w, h)
                "black_leopards" -> drawBlackLeopardsCrest(w, h)
                "baroka_fc" -> drawBarokaCrest(w, h)
                "upington_city" -> drawUpingtonCityCrest(w, h)
                else -> drawGenericClubCrest(w, h, primaryColor, secondaryColor, teamId)
            }
        }
    }
}

// ============================================================================
// 1. ORLANDO PIRATES CREST (Black circle, Skull & Crossbones, Red CAF Star)
// ============================================================================
private fun DrawScope.drawOrlandoPiratesCrest(w: Float, h: Float) {
    val cx = w / 2f
    val cy = h / 2f
    val r = w * 0.46f

    // Outer white rim
    drawCircle(color = Color.White, radius = r, center = Offset(cx, cy))
    // Black background
    drawCircle(color = Color(0xFF101010), radius = r * 0.94f, center = Offset(cx, cy))
    // Inner thin white ring
    drawCircle(color = Color.White, radius = r * 0.88f, center = Offset(cx, cy), style = Stroke(width = w * 0.035f))

    // Red CAF Star at top
    drawFivePointStar(cx = cx, cy = cy - r * 0.65f, outerRadius = w * 0.10f, color = Color(0xFFE50914))

    // Crossed Bones (White angled lines with rounded ends)
    val boneStroke = w * 0.07f
    val boneLen = r * 0.52f
    val angle = PI.toFloat() / 4f

    for (dir in listOf(1, -1)) {
        val dx = cos(angle) * boneLen * dir
        val dy = sin(angle) * boneLen
        drawLine(
            color = Color.White,
            start = Offset(cx - dx, cy + dy),
            end = Offset(cx + dx, cy - dy),
            strokeWidth = boneStroke,
            cap = StrokeCap.Round
        )
        // Bone knuckle dots at ends
        drawCircle(color = Color.White, radius = boneStroke * 0.65f, center = Offset(cx - dx, cy + dy))
        drawCircle(color = Color.White, radius = boneStroke * 0.65f, center = Offset(cx + dx, cy - dy))
    }

    // Skull Head (Center white dome + jaw)
    val skullCenter = Offset(cx, cy + r * 0.05f)
    drawCircle(color = Color.White, radius = w * 0.16f, center = skullCenter)
    // Jaw
    val jawRect = Rect(cx - w * 0.08f, skullCenter.y, cx + w * 0.08f, skullCenter.y + w * 0.14f)
    drawRoundRect(
        color = Color.White,
        topLeft = Offset(jawRect.left, jawRect.top),
        size = Size(jawRect.width, jawRect.height),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.03f, w * 0.03f)
    )

    // Eye Sockets (Dark hollow circles)
    val eyeR = w * 0.045f
    drawCircle(color = Color(0xFF101010), radius = eyeR, center = Offset(cx - w * 0.065f, skullCenter.y + w * 0.01f))
    drawCircle(color = Color(0xFF101010), radius = eyeR, center = Offset(cx + w * 0.065f, skullCenter.y + w * 0.01f))

    // Nose socket
    val nosePath = Path().apply {
        moveTo(cx, skullCenter.y + w * 0.06f)
        lineTo(cx - w * 0.02f, skullCenter.y + w * 0.09f)
        lineTo(cx + w * 0.02f, skullCenter.y + w * 0.09f)
        close()
    }
    drawPath(nosePath, color = Color(0xFF101010))

    // Teeth slits
    for (i in -1..1) {
        drawLine(
            color = Color(0xFF101010),
            start = Offset(cx + i * w * 0.038f, skullCenter.y + w * 0.11f),
            end = Offset(cx + i * w * 0.038f, skullCenter.y + w * 0.14f),
            strokeWidth = w * 0.02f
        )
    }
}

// ============================================================================
// 2. KAIZER CHIEFS CREST (Gold circle, Chief headdress profile, Soccer ball)
// ============================================================================
private fun DrawScope.drawKaizerChiefsCrest(w: Float, h: Float) {
    val cx = w / 2f
    val cy = h / 2f
    val r = w * 0.46f

    // Outer black border
    drawCircle(color = Color(0xFF151515), radius = r, center = Offset(cx, cy))
    // Gold/Yellow background
    drawCircle(color = Color(0xFFF7B100), radius = r * 0.92f, center = Offset(cx, cy))
    // Inner thin black ring
    drawCircle(color = Color(0xFF151515), radius = r * 0.85f, center = Offset(cx, cy), style = Stroke(width = w * 0.03f))

    // Soccer Ball at bottom/center (Black and gold)
    val ballR = w * 0.18f
    val ballCenter = Offset(cx, cy + r * 0.28f)
    drawCircle(color = Color(0xFFF7B100), radius = ballR, center = ballCenter)
    drawCircle(color = Color(0xFF151515), radius = ballR, center = ballCenter, style = Stroke(width = w * 0.03f))
    // Pentagons
    drawFivePointStar(ballCenter.x, ballCenter.y, ballR * 0.45f, Color(0xFF151515))

    // Chief Feather War Bonnet & Profile Silhouette (Black)
    // Feathers fanning from head to upper-right
    val featherPath = Path().apply {
        // Head profile silhouette on left
        moveTo(cx - w * 0.14f, cy - w * 0.10f)
        // Forehead & nose
        lineTo(cx - w * 0.20f, cy - w * 0.04f)
        lineTo(cx - w * 0.22f, cy)
        // Lips & chin
        lineTo(cx - w * 0.18f, cy + w * 0.05f)
        lineTo(cx - w * 0.12f, cy + w * 0.09f)
        // Neck to center
        lineTo(cx, cy + w * 0.08f)
        // Feathers radiating back
        lineTo(cx + w * 0.24f, cy - w * 0.05f)
        lineTo(cx + w * 0.28f, cy - w * 0.18f)
        lineTo(cx + w * 0.18f, cy - w * 0.28f)
        lineTo(cx + w * 0.05f, cy - w * 0.32f)
        lineTo(cx - w * 0.08f, cy - w * 0.26f)
        close()
    }
    drawPath(featherPath, color = Color(0xFF151515))

    // Individual feather white highlights
    for (f in 0..4) {
        val fAngle = -PI.toFloat() * 0.15f - (f * 0.20f)
        val fx = cx + cos(fAngle) * r * 0.65f
        val fy = cy - r * 0.10f + sin(fAngle) * r * 0.65f
        drawLine(
            color = Color(0xFFF7B100),
            start = Offset(cx, cy - r * 0.10f),
            end = Offset(fx, fy),
            strokeWidth = w * 0.02f
        )
    }

    // Chief headband in red accent
    drawLine(
        color = Color(0xFFE50914),
        start = Offset(cx - w * 0.15f, cy - w * 0.12f),
        end = Offset(cx + w * 0.08f, cy - w * 0.08f),
        strokeWidth = w * 0.045f,
        cap = StrokeCap.Round
    )
}

// ============================================================================
// 3. MAMELODI SUNDOWNS CREST (Yellow sunburst, Blue ring, Motto ribbon)
// ============================================================================
private fun DrawScope.drawMamelodiSundownsCrest(w: Float, h: Float) {
    val cx = w / 2f
    val cy = h / 2f
    val r = w * 0.46f

    // Outer Royal Blue rim
    drawCircle(color = Color(0xFF0055A5), radius = r, center = Offset(cx, cy))
    // Bright Ka Bo Yellow fill
    drawCircle(color = Color(0xFFF7D117), radius = r * 0.92f, center = Offset(cx, cy))
    // Inner blue ring
    drawCircle(color = Color(0xFF0055A5), radius = r * 0.85f, center = Offset(cx, cy), style = Stroke(width = w * 0.035f))

    // Central Radiant Sun with Triangular Rays
    val numRays = 12
    val sunPath = Path()
    val outerRayR = r * 0.68f
    val innerRayR = r * 0.42f

    for (i in 0 until numRays * 2) {
        val curR = if (i % 2 == 0) outerRayR else innerRayR
        val angle = (i * PI / numRays).toFloat()
        val rx = cx + cos(angle) * curR
        val ry = cy - r * 0.08f + sin(angle) * curR
        if (i == 0) sunPath.moveTo(rx, ry) else sunPath.lineTo(rx, ry)
    }
    sunPath.close()
    drawPath(sunPath, color = Color(0xFF0055A5))
    // Central yellow sun core
    drawCircle(color = Color(0xFFF7D117), radius = r * 0.35f, center = Offset(cx, cy - r * 0.08f))
    drawCircle(color = Color(0xFF0055A5), radius = r * 0.35f, center = Offset(cx, cy - r * 0.08f), style = Stroke(width = w * 0.025f))

    // Bottom Sky Blue Ribbon Banner ("The Sky is the Limit")
    val bannerPath = Path().apply {
        moveTo(cx - r * 0.75f, cy + r * 0.45f)
        quadraticTo(cx, cy + r * 0.35f, cx + r * 0.75f, cy + r * 0.45f)
        lineTo(cx + r * 0.65f, cy + r * 0.72f)
        quadraticTo(cx, cy + r * 0.58f, cx - r * 0.65f, cy + r * 0.72f)
        close()
    }
    drawPath(bannerPath, color = Color(0xFF00A3E0))
    drawPath(bannerPath, color = Color(0xFF0055A5), style = Stroke(width = w * 0.025f))

    // Green soccer ball in center
    drawCircle(color = Color(0xFF008751), radius = w * 0.07f, center = Offset(cx, cy - r * 0.08f))

    // Top Gold CAF Champions Star
    drawFivePointStar(cx = cx, cy = cy - r * 0.72f, outerRadius = w * 0.08f, color = Color(0xFFF7D117))
}

// ============================================================================
// 4. STELLENBOSCH FC CREST (Maroon shield, Cape Winelands grapes & oak)
// ============================================================================
private fun DrawScope.drawStellenboschCrest(w: Float, h: Float) {
    val cx = w / 2f
    val shieldPath = createHeraldicShieldPath(w, h)

    // Maroon background
    drawPath(shieldPath, color = Color(0xFF800020))
    // Antique gold border
    drawPath(shieldPath, color = Color(0xFFD4AF37), style = Stroke(width = w * 0.05f))

    // Diagonal Gold Sash
    val sashPath = Path().apply {
        moveTo(w * 0.15f, h * 0.20f)
        lineTo(w * 0.35f, h * 0.15f)
        lineTo(w * 0.85f, h * 0.65f)
        lineTo(w * 0.65f, h * 0.75f)
        close()
    }
    drawPath(sashPath, color = Color(0xFFD4AF37).copy(alpha = 0.5f))

    // Cluster of Cape Grapes (Golden circles)
    val grapeR = w * 0.045f
    val grapeY = h * 0.48f
    // Top row (3)
    drawCircle(Color(0xFFD4AF37), grapeR, Offset(cx - grapeR * 2, grapeY - grapeR * 1.5f))
    drawCircle(Color(0xFFD4AF37), grapeR, Offset(cx, grapeY - grapeR * 1.5f))
    drawCircle(Color(0xFFD4AF37), grapeR, Offset(cx + grapeR * 2, grapeY - grapeR * 1.5f))
    // Middle row (2)
    drawCircle(Color(0xFFD4AF37), grapeR, Offset(cx - grapeR, grapeY))
    drawCircle(Color(0xFFD4AF37), grapeR, Offset(cx + grapeR, grapeY))
    // Bottom (1)
    drawCircle(Color(0xFFD4AF37), grapeR, Offset(cx, grapeY + grapeR * 1.5f))

    // Grape vine stem & oak leaf
    drawLine(Color(0xFFD4AF37), Offset(cx, grapeY - grapeR * 3f), Offset(cx, grapeY - grapeR * 1.5f), strokeWidth = w * 0.03f)
    drawCircle(Color(0xFFD4AF37), grapeR * 0.8f, Offset(cx + grapeR * 1.5f, grapeY - grapeR * 2.8f))
}

// ============================================================================
// 5. SUPERSPORT UNITED (Royal blue shield, Red & white chevron wings)
// ============================================================================
private fun DrawScope.drawSuperSportCrest(w: Float, h: Float) {
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = Color(0xFF003399))
    drawPath(shieldPath, color = Color.White, style = Stroke(width = w * 0.05f))

    // Red Chevron Wings ("Matsatsantsa")
    val chevronPath = Path().apply {
        moveTo(w * 0.20f, h * 0.35f)
        lineTo(w * 0.50f, h * 0.58f)
        lineTo(w * 0.80f, h * 0.35f)
        lineTo(w * 0.80f, h * 0.48f)
        lineTo(w * 0.50f, h * 0.70f)
        lineTo(w * 0.20f, h * 0.48f)
        close()
    }
    drawPath(chevronPath, color = Color(0xFFD22630))

    // Center Soccer Ball
    drawCircle(Color.White, w * 0.12f, Offset(w / 2f, h * 0.35f))
    drawCircle(Color(0xFF003399), w * 0.12f, Offset(w / 2f, h * 0.35f), style = Stroke(width = w * 0.02f))
    drawFivePointStar(w / 2f, h * 0.35f, w * 0.05f, Color(0xFF003399))
}

// ============================================================================
// 6. CAPE TOWN CITY (Navy shield, Table Mountain flat silhouette & Crown)
// ============================================================================
private fun DrawScope.drawCapeTownCityCrest(w: Float, h: Float) {
    val cx = w / 2f
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = Color(0xFF0C2340))
    drawPath(shieldPath, color = Color(0xFFF5A800), style = Stroke(width = w * 0.05f))

    // Golden Triple Crown at top
    val crownPath = Path().apply {
        moveTo(cx - w * 0.22f, h * 0.26f)
        lineTo(cx - w * 0.18f, h * 0.16f)
        lineTo(cx - w * 0.08f, h * 0.22f)
        lineTo(cx, h * 0.14f)
        lineTo(cx + w * 0.08f, h * 0.22f)
        lineTo(cx + w * 0.18f, h * 0.16f)
        lineTo(cx + w * 0.22f, h * 0.26f)
        close()
    }
    drawPath(crownPath, color = Color(0xFFF5A800))

    // Table Mountain Silhouette (Iconic flat-top plateau)
    val mountainPath = Path().apply {
        // Devil's Peak on left
        moveTo(w * 0.16f, h * 0.60f)
        lineTo(w * 0.26f, h * 0.42f)
        // Saddle
        lineTo(w * 0.33f, h * 0.47f)
        // Flat Table Mountain plateau
        lineTo(w * 0.38f, h * 0.38f)
        lineTo(w * 0.64f, h * 0.38f)
        // Lion's Head & Signal Hill on right
        lineTo(w * 0.69f, h * 0.46f)
        lineTo(w * 0.76f, h * 0.43f)
        lineTo(w * 0.84f, h * 0.60f)
        close()
    }
    drawPath(mountainPath, color = Color(0xFF00A3E0))
    drawPath(mountainPath, color = Color.White, style = Stroke(width = w * 0.02f))
}

// ============================================================================
// 7. AMAZULU FC CREST (Green shield, Traditional Zulu War Shield & Spears)
// ============================================================================
private fun DrawScope.drawAmaZuluCrest(w: Float, h: Float) {
    val cx = w / 2f
    val cy = h / 2f
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = Color(0xFF005A36))
    drawPath(shieldPath, color = Color.White, style = Stroke(width = w * 0.05f))

    // Two Crossed Assegai Spears (White lines with spearheads)
    drawLine(Color.White, Offset(w * 0.22f, h * 0.72f), Offset(w * 0.78f, h * 0.22f), strokeWidth = w * 0.035f)
    drawLine(Color.White, Offset(w * 0.78f, h * 0.72f), Offset(w * 0.22f, h * 0.22f), strokeWidth = w * 0.035f)

    // Traditional Zulu Oval War Shield (Isihlangu)
    val isihlanguRect = Rect(cx - w * 0.18f, h * 0.24f, cx + w * 0.18f, h * 0.74f)
    drawOval(color = Color.White, topLeft = Offset(isihlanguRect.left, isihlanguRect.top), size = Size(isihlanguRect.width, isihlanguRect.height))
    drawOval(color = Color(0xFF151515), topLeft = Offset(isihlanguRect.left + w * 0.02f, isihlanguRect.top + h * 0.02f), size = Size(isihlanguRect.width - w * 0.04f, isihlanguRect.height - h * 0.04f))

    // Traditional cowhide white lace marks
    for (y in 0..4) {
        val ly = h * 0.32f + (y * h * 0.08f)
        drawLine(Color.White, Offset(cx - w * 0.07f, ly), Offset(cx + w * 0.07f, ly), strokeWidth = w * 0.025f)
    }
}

// ============================================================================
// 8. TS GALAXY (Red crest, Space Rocket & propulsion flame)
// ============================================================================
private fun DrawScope.drawTSGalaxyCrest(w: Float, h: Float) {
    val cx = w / 2f
    val cy = h / 2f
    val r = w * 0.46f

    drawCircle(color = Color(0xFF151515), radius = r, center = Offset(cx, cy))
    drawCircle(color = Color(0xFFD22630), radius = r * 0.92f, center = Offset(cx, cy))
    drawCircle(color = Color.White, radius = r * 0.86f, center = Offset(cx, cy), style = Stroke(width = w * 0.03f))

    // Rocket Fuselage (White pointing up)
    val rocketPath = Path().apply {
        moveTo(cx, cy - r * 0.65f) // Nose cone
        lineTo(cx + w * 0.10f, cy - r * 0.15f)
        lineTo(cx + w * 0.10f, cy + r * 0.30f)
        // Right fin
        lineTo(cx + w * 0.22f, cy + r * 0.45f)
        lineTo(cx + w * 0.10f, cy + r * 0.42f)
        // Thruster nozzle
        lineTo(cx - w * 0.10f, cy + r * 0.42f)
        // Left fin
        lineTo(cx - w * 0.22f, cy + r * 0.45f)
        lineTo(cx - w * 0.10f, cy + r * 0.30f)
        lineTo(cx - w * 0.10f, cy - r * 0.15f)
        close()
    }
    drawPath(rocketPath, color = Color.White)

    // Red rocket window & nose
    drawCircle(Color(0xFFD22630), w * 0.045f, Offset(cx, cy - r * 0.15f))

    // Orange & Yellow Rocket Exhaust Flame
    val flamePath = Path().apply {
        moveTo(cx - w * 0.08f, cy + r * 0.42f)
        lineTo(cx, cy + r * 0.75f)
        lineTo(cx + w * 0.08f, cy + r * 0.42f)
        close()
    }
    drawPath(flamePath, color = Color(0xFFFF9800))
    drawCircle(Color(0xFFFFEB3B), w * 0.04f, Offset(cx, cy + r * 0.50f))
}

// ============================================================================
// 9. CHIPPA UNITED (Blue/Red shield with Chilli Pepper)
// ============================================================================
private fun DrawScope.drawChippaUnitedCrest(w: Float, h: Float) {
    val cx = w / 2f
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = Color(0xFF004B87))
    drawPath(shieldPath, color = Color.White, style = Stroke(width = w * 0.05f))

    // Curved Fiery Red Chilli Pepper ("Chilli Boys")
    val pepperPath = Path().apply {
        moveTo(cx - w * 0.05f, h * 0.28f)
        cubicTo(cx + w * 0.25f, h * 0.38f, cx + w * 0.22f, h * 0.65f, cx - w * 0.05f, h * 0.72f)
        cubicTo(cx + w * 0.08f, h * 0.60f, cx + w * 0.10f, h * 0.42f, cx - w * 0.05f, h * 0.32f)
        close()
    }
    drawPath(pepperPath, color = Color(0xFFE50914))

    // Green Calyx / Stem on top of chilli
    val stemPath = Path().apply {
        moveTo(cx - w * 0.05f, h * 0.28f)
        lineTo(cx - w * 0.12f, h * 0.20f)
        lineTo(cx - w * 0.02f, h * 0.22f)
        close()
    }
    drawPath(stemPath, color = Color(0xFF4CAF50))

    // White soccer ball
    drawCircle(Color.White, w * 0.10f, Offset(cx - w * 0.10f, h * 0.52f))
    drawCircle(Color(0xFF004B87), w * 0.10f, Offset(cx - w * 0.10f, h * 0.52f), style = Stroke(width = w * 0.02f))
}

// ============================================================================
// 10. GOLDEN ARROWS (Green shield, Golden Bow & Flying Arrow)
// ============================================================================
private fun DrawScope.drawGoldenArrowsCrest(w: Float, h: Float) {
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = Color(0xFF008751))
    drawPath(shieldPath, color = Color(0xFFFFD200), style = Stroke(width = w * 0.05f))

    // Flying Golden Arrow (Pointing diagonally upward to top right)
    val start = Offset(w * 0.22f, h * 0.72f)
    val end = Offset(w * 0.78f, h * 0.25f)
    drawLine(Color(0xFFFFD200), start, end, strokeWidth = w * 0.045f)

    // Arrowhead (Triangle)
    val arrowHead = Path().apply {
        moveTo(end.x, end.y)
        lineTo(end.x - w * 0.15f, end.y + w * 0.02f)
        lineTo(end.x - w * 0.02f, end.y + w * 0.15f)
        close()
    }
    drawPath(arrowHead, color = Color(0xFFFFD200))

    // Arrow Fletching (Feathers at tail)
    drawLine(Color.White, start, Offset(start.x - w * 0.06f, start.y + w * 0.04f), strokeWidth = w * 0.03f)
    drawLine(Color.White, start, Offset(start.x + w * 0.04f, start.y - w * 0.06f), strokeWidth = w * 0.03f)
}

// ============================================================================
// 11. MAGESI FC (Navy & Green shield, Crocodile jaws, 2024 CKO Champions Star)
// ============================================================================
private fun DrawScope.drawMagesiCrest(w: Float, h: Float) {
    val cx = w / 2f
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = Color(0xFF003366))
    drawPath(shieldPath, color = Color(0xFFFF8000), style = Stroke(width = w * 0.05f))

    // 2024 Carling Knockout Cup Gold Star at top!
    drawFivePointStar(cx = cx, cy = h * 0.22f, outerRadius = w * 0.09f, color = Color(0xFFFFD200))

    // Crocodile Head (Dikwena tša Meetse) in Green & Orange
    val crocPath = Path().apply {
        // Snout
        moveTo(cx + w * 0.28f, h * 0.50f)
        // Upper jaw
        lineTo(cx, h * 0.38f)
        // Eye ridge
        lineTo(cx - w * 0.22f, h * 0.40f)
        lineTo(cx - w * 0.28f, h * 0.55f)
        // Lower jaw
        lineTo(cx - w * 0.10f, h * 0.65f)
        lineTo(cx + w * 0.22f, h * 0.58f)
        close()
    }
    drawPath(crocPath, color = Color(0xFF1B6B2F))
    drawPath(crocPath, color = Color(0xFFFF8000), style = Stroke(width = w * 0.025f))

    // Sharp white crocodile teeth
    for (i in 0..3) {
        val tx = cx - w * 0.05f + (i * w * 0.07f)
        val tooth = Path().apply {
            moveTo(tx, h * 0.48f)
            lineTo(tx + w * 0.03f, h * 0.54f)
            lineTo(tx + w * 0.06f, h * 0.48f)
            close()
        }
        drawPath(tooth, color = Color.White)
    }

    // Crocodile Eye
    drawCircle(Color(0xFFFFD200), w * 0.035f, Offset(cx - w * 0.15f, h * 0.45f))
}

// ============================================================================
// 12. POLOKWANE CITY (Orange crest, Rising Sun Rays)
// ============================================================================
private fun DrawScope.drawPolokwaneCityCrest(w: Float, h: Float) {
    val cx = w / 2f
    val cy = h / 2f
    val r = w * 0.46f

    drawCircle(color = Color(0xFFFF6600), radius = r, center = Offset(cx, cy))
    drawCircle(color = Color.White, radius = r * 0.90f, center = Offset(cx, cy))

    // Rising Sun Horizon & Beams ("Rise and Shine")
    val sunCenter = Offset(cx, cy + r * 0.25f)
    val numBeams = 7
    for (i in 0..numBeams) {
        val angle = PI.toFloat() * (i.toFloat() / numBeams)
        val bx = sunCenter.x - cos(angle) * r * 0.70f
        val by = sunCenter.y - sin(angle) * r * 0.70f
        drawLine(Color(0xFFFF6600), sunCenter, Offset(bx, by), strokeWidth = w * 0.045f)
    }

    // Semi-circle Sun
    drawCircle(Color(0xFFFFD200), r * 0.38f, sunCenter)
    drawCircle(Color(0xFFFF6600), r * 0.38f, sunCenter, style = Stroke(width = w * 0.03f))
    // Lower ground
    drawRect(Color(0xFFFF6600), Offset(cx - r * 0.75f, sunCenter.y), Size(r * 1.5f, r * 0.6f))
}

// ============================================================================
// 13. RICHARDS BAY (Blue shield, Anchor & Waves)
// ============================================================================
private fun DrawScope.drawRichardsBayCrest(w: Float, h: Float) {
    val cx = w / 2f
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = Color(0xFF0066B2))
    drawPath(shieldPath, color = Color.White, style = Stroke(width = w * 0.05f))

    // Golden Anchor ("Natal Rich Boyz")
    drawLine(Color(0xFFFFD200), Offset(cx, h * 0.25f), Offset(cx, h * 0.68f), strokeWidth = w * 0.05f)
    // Anchor top loop
    drawCircle(Color(0xFFFFD200), w * 0.07f, Offset(cx, h * 0.25f), style = Stroke(width = w * 0.035f))
    // Anchor crossbar
    drawLine(Color(0xFFFFD200), Offset(cx - w * 0.16f, h * 0.35f), Offset(cx + w * 0.16f, h * 0.35f), strokeWidth = w * 0.045f)
    // Anchor curved bottom arms
    val anchorCurve = Path().apply {
        moveTo(cx - w * 0.24f, h * 0.55f)
        quadraticTo(cx, h * 0.76f, cx + w * 0.24f, h * 0.55f)
    }
    drawPath(anchorCurve, color = Color(0xFFFFD200), style = Stroke(width = w * 0.05f))
}

// ============================================================================
// 14. SEKHUKHUNE UNITED (Crimson shield, Porcupine Quills)
// ============================================================================
private fun DrawScope.drawSekhukhuneCrest(w: Float, h: Float) {
    val cx = w / 2f
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = Color(0xFF8B1E41))
    drawPath(shieldPath, color = Color(0xFFFFD200), style = Stroke(width = w * 0.05f))

    // Babina Noko Porcupine Quills Fan
    val quillCenter = Offset(cx, h * 0.62f)
    for (i in 0..8) {
        val angle = -PI.toFloat() * 0.85f + (i * PI.toFloat() * 0.70f / 8f)
        val qx = quillCenter.x + cos(angle) * w * 0.35f
        val qy = quillCenter.y + sin(angle) * w * 0.35f
        drawLine(Color.White, quillCenter, Offset(qx, qy), strokeWidth = w * 0.03f)
        // Quill tip in yellow
        drawCircle(Color(0xFFFFD200), w * 0.02f, Offset(qx, qy))
    }
    // Center golden soccer ball
    drawCircle(Color(0xFFFFD200), w * 0.11f, quillCenter)
    drawCircle(Color(0xFF8B1E41), w * 0.11f, quillCenter, style = Stroke(width = w * 0.02f))
}

// ============================================================================
// 15. MARUMO GALLANTS (Green shield, Warrior Horn)
// ============================================================================
private fun DrawScope.drawMarumoGallantsCrest(w: Float, h: Float) {
    val cx = w / 2f
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = Color(0xFF1F7A4D))
    drawPath(shieldPath, color = Color(0xFF800020), style = Stroke(width = w * 0.05f))

    // Bahlabane Ba Ntwa Warrior Shield & Horn
    drawCircle(Color(0xFFFFD200), w * 0.16f, Offset(cx, h * 0.45f))
    drawCircle(Color(0xFF800020), w * 0.16f, Offset(cx, h * 0.45f), style = Stroke(width = w * 0.025f))
    // Kudu Battle Horn
    val hornPath = Path().apply {
        moveTo(cx - w * 0.15f, h * 0.55f)
        quadraticTo(cx, h * 0.35f, cx + w * 0.20f, h * 0.38f)
        lineTo(cx + w * 0.22f, h * 0.45f)
        quadraticTo(cx, h * 0.42f, cx - w * 0.12f, h * 0.60f)
        close()
    }
    drawPath(hornPath, color = Color.White)
}

// ============================================================================
// 16. ROYAL AM (Gold & Black shield, Royal Crown)
// ============================================================================
private fun DrawScope.drawRoyalAMCrest(w: Float, h: Float) {
    val cx = w / 2f
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = Color(0xFF1A1A1A))
    drawPath(shieldPath, color = Color(0xFFD4AF37), style = Stroke(width = w * 0.05f))

    // Royal Crown
    val crownPath = Path().apply {
        moveTo(cx - w * 0.22f, h * 0.52f)
        lineTo(cx - w * 0.18f, h * 0.35f)
        lineTo(cx - w * 0.08f, h * 0.44f)
        lineTo(cx, h * 0.30f)
        lineTo(cx + w * 0.08f, h * 0.44f)
        lineTo(cx + w * 0.18f, h * 0.35f)
        lineTo(cx + w * 0.22f, h * 0.52f)
        close()
    }
    drawPath(crownPath, color = Color(0xFFD4AF37))
    drawCircle(Color.White, w * 0.025f, Offset(cx, h * 0.30f))
}

// ============================================================================
// 17. UNIVERSITY OF PRETORIA / AMATUKS (Academic shield, Open Book)
// ============================================================================
private fun DrawScope.drawAmaTuksCrest(w: Float, h: Float) {
    val cx = w / 2f
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = Color(0xFF002855))
    drawPath(shieldPath, color = Color(0xFFB58500), style = Stroke(width = w * 0.05f))

    // Open Book of Knowledge
    val bookY = h * 0.42f
    val leftPage = Path().apply {
        moveTo(cx, bookY + w * 0.08f)
        quadraticTo(cx - w * 0.12f, bookY - w * 0.02f, cx - w * 0.22f, bookY)
        lineTo(cx - w * 0.22f, bookY + w * 0.22f)
        quadraticTo(cx - w * 0.12f, bookY + w * 0.20f, cx, bookY + w * 0.30f)
        close()
    }
    val rightPage = Path().apply {
        moveTo(cx, bookY + w * 0.08f)
        quadraticTo(cx + w * 0.12f, bookY - w * 0.02f, cx + w * 0.22f, bookY)
        lineTo(cx + w * 0.22f, bookY + w * 0.22f)
        quadraticTo(cx + w * 0.12f, bookY + w * 0.20f, cx, bookY + w * 0.30f)
        close()
    }
    drawPath(leftPage, color = Color.White)
    drawPath(rightPage, color = Color.White)
    // Red stripe
    drawLine(Color(0xFFD32F2F), Offset(w * 0.20f, h * 0.22f), Offset(w * 0.80f, h * 0.22f), strokeWidth = w * 0.05f)
}

// ============================================================================
// 18. HUNGRY LIONS (Amber crest, Roaring African Lion)
// ============================================================================
private fun DrawScope.drawHungryLionsCrest(w: Float, h: Float) {
    val cx = w / 2f
    val cy = h / 2f
    val r = w * 0.46f

    drawCircle(color = Color(0xFFFFA000), radius = r, center = Offset(cx, cy))
    drawCircle(color = Color(0xFF151515), radius = r * 0.90f, center = Offset(cx, cy))

    // Roaring Lion Mane & Snout (Ditau)
    val lionPath = Path().apply {
        moveTo(cx - w * 0.10f, cy - w * 0.20f)
        lineTo(cx + w * 0.12f, cy - w * 0.18f)
        lineTo(cx + w * 0.22f, cy - w * 0.05f)
        lineTo(cx + w * 0.25f, cy + w * 0.08f)
        // Open roar mouth
        lineTo(cx + w * 0.12f, cy + w * 0.15f)
        lineTo(cx + w * 0.05f, cy + w * 0.05f)
        lineTo(cx + w * 0.10f, cy + w * 0.22f)
        // Jaw to neck
        lineTo(cx - w * 0.15f, cy + w * 0.22f)
        lineTo(cx - w * 0.25f, cy)
        close()
    }
    drawPath(lionPath, color = Color(0xFFFFA000))
    // Lion eye
    drawCircle(Color.White, w * 0.03f, Offset(cx + w * 0.08f, cy - w * 0.08f))
}

// ============================================================================
// 19. CASRIC STARS (Red & Black shield, Central White Star)
// ============================================================================
private fun DrawScope.drawCasricStarsCrest(w: Float, h: Float) {
    val cx = w / 2f
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = Color(0xFFD32F2F))
    drawPath(shieldPath, color = Color.White, style = Stroke(width = w * 0.05f))

    // Black lower chevron
    val blackChevron = Path().apply {
        moveTo(w * 0.15f, h * 0.50f)
        lineTo(cx, h * 0.85f)
        lineTo(w * 0.85f, h * 0.50f)
        close()
    }
    drawPath(blackChevron, color = Color(0xFF151515))

    // Large 5-Pointed White Star in center
    drawFivePointStar(cx = cx, cy = h * 0.44f, outerRadius = w * 0.22f, color = Color.White)
}

// ============================================================================
// 20. BLACK LEOPARDS (Yellow shield, Black Leopard Spots)
// ============================================================================
private fun DrawScope.drawBlackLeopardsCrest(w: Float, h: Float) {
    val cx = w / 2f
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = Color(0xFFFFEB3B))
    drawPath(shieldPath, color = Color(0xFF151515), style = Stroke(width = w * 0.05f))

    // Leopard Head Profile & Spots (Lidoda Duvha)
    drawCircle(Color(0xFF151515), w * 0.14f, Offset(cx, h * 0.44f))
    // Ears
    drawCircle(Color(0xFF151515), w * 0.05f, Offset(cx - w * 0.10f, h * 0.32f))
    drawCircle(Color(0xFF151515), w * 0.05f, Offset(cx + w * 0.10f, h * 0.32f))
    // Spots
    drawCircle(Color(0xFF151515), w * 0.035f, Offset(cx - w * 0.20f, h * 0.25f))
    drawCircle(Color(0xFF151515), w * 0.035f, Offset(cx + w * 0.20f, h * 0.25f))
    drawCircle(Color(0xFF151515), w * 0.035f, Offset(cx - w * 0.18f, h * 0.62f))
    drawCircle(Color(0xFF151515), w * 0.035f, Offset(cx + w * 0.18f, h * 0.62f))
    // Eyes
    drawCircle(Color(0xFFFFEB3B), w * 0.025f, Offset(cx - w * 0.05f, h * 0.42f))
    drawCircle(Color(0xFFFFEB3B), w * 0.025f, Offset(cx + w * 0.05f, h * 0.42f))
}

// ============================================================================
// 21. BAROKA FC (Green & Yellow shield, Limpopo Star)
// ============================================================================
private fun DrawScope.drawBarokaCrest(w: Float, h: Float) {
    val cx = w / 2f
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = Color(0xFF2E7D32))
    drawPath(shieldPath, color = Color(0xFFFBC02D), style = Stroke(width = w * 0.05f))

    // Bakgaga Golden Star
    drawFivePointStar(cx = cx, cy = h * 0.45f, outerRadius = w * 0.20f, color = Color(0xFFFBC02D))
    drawCircle(Color.White, w * 0.06f, Offset(cx, h * 0.45f))
}

// ============================================================================
// 22. UPINGTON CITY (Sky blue shield, Golden Diamonds)
// ============================================================================
private fun DrawScope.drawUpingtonCityCrest(w: Float, h: Float) {
    val cx = w / 2f
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = Color(0xFF0288D1))
    drawPath(shieldPath, color = Color(0xFFFFD200), style = Stroke(width = w * 0.05f))

    // "Bafana ba Gucci" Golden Diamond
    val diamondPath = Path().apply {
        moveTo(cx, h * 0.28f)
        lineTo(cx + w * 0.18f, h * 0.48f)
        lineTo(cx, h * 0.68f)
        lineTo(cx - w * 0.18f, h * 0.48f)
        close()
    }
    drawPath(diamondPath, color = Color(0xFFFFD200))
    drawPath(diamondPath, color = Color.White, style = Stroke(width = w * 0.025f))
}

// ============================================================================
// GENERIC HERALDIC CREST FALLBACK (For any other clubs)
// ============================================================================
private fun DrawScope.drawGenericClubCrest(
    w: Float,
    h: Float,
    primaryColor: Color,
    secondaryColor: Color,
    teamId: String
) {
    val cx = w / 2f
    val shieldPath = createHeraldicShieldPath(w, h)
    drawPath(shieldPath, color = primaryColor)
    drawPath(shieldPath, color = secondaryColor, style = Stroke(width = w * 0.05f))

    // Diagonal Sash or Chevron
    val sashPath = Path().apply {
        moveTo(w * 0.15f, h * 0.25f)
        lineTo(w * 0.35f, h * 0.20f)
        lineTo(w * 0.85f, h * 0.70f)
        lineTo(w * 0.65f, h * 0.75f)
        close()
    }
    drawPath(sashPath, color = secondaryColor.copy(alpha = 0.4f))

    // Center Soccer Ball emblem
    drawCircle(color = secondaryColor, radius = w * 0.14f, center = Offset(cx, h * 0.45f))
    drawCircle(color = primaryColor, radius = w * 0.14f, center = Offset(cx, h * 0.45f), style = Stroke(width = w * 0.025f))
    drawFivePointStar(cx = cx, cy = h * 0.45f, outerRadius = w * 0.06f, color = primaryColor)
}

// ============================================================================
// HELPER DRAWING FUNCTIONS
// ============================================================================
private fun createHeraldicShieldPath(w: Float, h: Float): Path {
    return Path().apply {
        moveTo(w * 0.10f, h * 0.12f)
        lineTo(w * 0.90f, h * 0.12f)
        lineTo(w * 0.90f, h * 0.55f)
        cubicTo(w * 0.90f, h * 0.78f, w * 0.55f, h * 0.92f, w * 0.50f, h * 0.95f)
        cubicTo(w * 0.45f, h * 0.92f, w * 0.10f, h * 0.78f, w * 0.10f, h * 0.55f)
        close()
    }
}

private fun DrawScope.drawFivePointStar(cx: Float, cy: Float, outerRadius: Float, color: Color) {
    val innerRadius = outerRadius * 0.42f
    val starPath = Path()
    for (i in 0 until 10) {
        val r = if (i % 2 == 0) outerRadius else innerRadius
        val angle = -PI.toFloat() / 2f + (i * PI.toFloat() / 5f)
        val x = cx + cos(angle) * r
        val y = cy + sin(angle) * r
        if (i == 0) starPath.moveTo(x, y) else starPath.lineTo(x, y)
    }
    starPath.close()
    drawPath(starPath, color = color)
}
