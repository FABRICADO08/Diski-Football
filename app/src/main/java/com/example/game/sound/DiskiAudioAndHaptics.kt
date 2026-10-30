package com.example.game.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

/**
 * Procedural Audio & Haptic Feedback Manager for Diski Football.
 * Synthesizes dynamic match sounds (whistle, kick, post ping, goal roar, vuvuzela buzz, kasi whoosh)
 * and triggers tactile feedback for physical on-pitch collisions.
 */
class DiskiAudioAndHaptics(private val context: Context) {

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    private val audioScope = CoroutineScope(Dispatchers.Default)
    private val sampleRate = 22050

    // --- Haptic Feedback Methods ---

    fun hapticBallKick() {
        try {
            if (vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(28, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(28)
                }
            }
        } catch (_: Exception) {}
    }

    fun hapticTackle() {
        try {
            if (vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(55, 220))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(55)
                }
            }
        } catch (_: Exception) {}
    }

    fun hapticPostHit() {
        try {
            if (vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val timings = longArrayOf(0, 35, 40, 45)
                    val amplitudes = intArrayOf(0, 200, 0, 255)
                    vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(80)
                }
            }
        } catch (_: Exception) {}
    }

    fun hapticGoalScored() {
        try {
            if (vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Celebration rhythmic pulse
                    val timings = longArrayOf(0, 100, 70, 120, 60, 200)
                    val amplitudes = intArrayOf(0, 240, 0, 255, 0, 255)
                    vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(400)
                }
            }
        } catch (_: Exception) {}
    }

    fun hapticKasiSkill() {
        try {
            if (vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val timings = longArrayOf(0, 25, 30, 35)
                    val amplitudes = intArrayOf(0, 160, 0, 220)
                    vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(60)
                }
            }
        } catch (_: Exception) {}
    }

    // --- Procedural Sound Synthesis Methods ---

    fun playKickSound(isHardShot: Boolean = false) {
        audioScope.launch {
            val durationMs = if (isHardShot) 90 else 60
            val numSamples = (sampleRate * (durationMs / 1000f)).toInt()
            val buffer = ShortArray(numSamples)
            val startFreq = if (isHardShot) 180f else 130f
            val endFreq = 45f

            for (i in 0 until numSamples) {
                val t = i.toFloat() / numSamples
                val currentFreq = startFreq + (endFreq - startFreq) * t
                val phase = 2.0 * PI * currentFreq * (i.toDouble() / sampleRate)
                val envelope = 1f - t // Decay
                buffer[i] = (sin(phase) * envelope * Short.MAX_VALUE * 0.75f).toInt().toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    fun playWhistleSound(isDoubleBlast: Boolean = false) {
        audioScope.launch {
            val blastDurationMs = if (isDoubleBlast) 180 else 240
            val numSamples = (sampleRate * (blastDurationMs / 1000f)).toInt()
            val buffer = ShortArray(numSamples)
            val freq1 = 2650.0
            val freq2 = 2950.0

            for (i in 0 until numSamples) {
                val t = i.toFloat() / numSamples
                val envelope = when {
                    t < 0.1f -> t / 0.1f
                    t > 0.8f -> (1f - t) / 0.2f
                    else -> 1f
                }
                // Tremolo/trill effect
                val trill = 1.0 + 0.15 * sin(2.0 * PI * 35.0 * (i.toDouble() / sampleRate))
                val sample1 = sin(2.0 * PI * freq1 * trill * (i.toDouble() / sampleRate))
                val sample2 = sin(2.0 * PI * freq2 * trill * (i.toDouble() / sampleRate))
                buffer[i] = ((sample1 + sample2) * 0.45 * envelope * Short.MAX_VALUE).toInt().toShort()
            }
            playPcmBuffer(buffer)

            if (isDoubleBlast) {
                kotlinx.coroutines.delay(100)
                playPcmBuffer(buffer)
            }
        }
    }

    fun playPostHitSound() {
        audioScope.launch {
            val durationMs = 150
            val numSamples = (sampleRate * (durationMs / 1000f)).toInt()
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toFloat() / numSamples
                val envelope = (1f - t) * (1f - t)
                // Metallic harmonic ping (880Hz + 1760Hz + 2640Hz)
                val s1 = sin(2.0 * PI * 880.0 * (i.toDouble() / sampleRate))
                val s2 = sin(2.0 * PI * 1760.0 * (i.toDouble() / sampleRate)) * 0.4
                val s3 = sin(2.0 * PI * 2640.0 * (i.toDouble() / sampleRate)) * 0.2
                buffer[i] = ((s1 + s2 + s3) * envelope * Short.MAX_VALUE * 0.7f).toInt().toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    fun playKasiFlavourSound() {
        audioScope.launch {
            val durationMs = 160
            val numSamples = (sampleRate * (durationMs / 1000f)).toInt()
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toFloat() / numSamples
                // Frequency sweep upwards 300Hz -> 950Hz
                val freq = 300.0 + (650.0 * t * t)
                val envelope = sin(t * PI)
                val sample = sin(2.0 * PI * freq * (i.toDouble() / sampleRate))
                buffer[i] = (sample * envelope * Short.MAX_VALUE * 0.6f).toInt().toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    fun playGoalRoar() {
        audioScope.launch {
            val durationMs = 600
            val numSamples = (sampleRate * (durationMs / 1000f)).toInt()
            val buffer = ShortArray(numSamples)

            // Vuvuzela stadium tone (approx 233Hz B♭3 + harmonic 466Hz) + celebratory cheer rise
            for (i in 0 until numSamples) {
                val t = i.toFloat() / numSamples
                val envelope = when {
                    t < 0.2f -> t / 0.2f
                    else -> 1f - (t - 0.2f) * 0.9f
                }
                val vuvu = sin(2.0 * PI * 233.0 * (i.toDouble() / sampleRate)) * 0.5 +
                        sin(2.0 * PI * 466.0 * (i.toDouble() / sampleRate)) * 0.35 +
                        sin(2.0 * PI * 699.0 * (i.toDouble() / sampleRate)) * 0.15
                buffer[i] = (vuvu * envelope * Short.MAX_VALUE * 0.65f).toInt().toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    private fun playPcmBuffer(buffer: ShortArray) {
        var track: AudioTrack? = null
        try {
            val minBuf = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val bufferSize = maxOf(minBuf, buffer.size * 2)

            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            val audioFormat = AudioFormat.Builder()
                .setSampleRate(sampleRate)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build()

            track = AudioTrack.Builder()
                .setAudioAttributes(audioAttributes)
                .setAudioFormat(audioFormat)
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(buffer, 0, buffer.size)
            track.play()
        } catch (_: Exception) {
            // Graceful fallback if device audio track is unavailable
        } finally {
            // Note: track will be collected when finished
        }
    }
}
