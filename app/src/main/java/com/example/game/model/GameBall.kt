package com.example.game.model

data class GameBall(
    var position: Vector2D = Vector2D(500f, 325f), // Center spot
    var heightZ: Float = 0f,                       // 0 = ground level, > 0 = airborne
    var velocity: Vector2D = Vector2D.ZERO,
    var velocityZ: Float = 0f,
    var ownerPlayerId: String? = null,             // Player currently in possession
    var lastTouchTeam: PlayerTeamSide = PlayerTeamSide.HOME,
    var spin: Float = 0f
) {
    val isAirborne: Boolean
        get() = heightZ > 0.5f

    fun kick(direction: Vector2D, power: Float, loftAngle: Float = 0f) {
        ownerPlayerId = null
        val dir = direction.normalized()
        velocity = dir * power
        velocityZ = loftAngle
    }
}
