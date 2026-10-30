package com.example.game.model

enum class PlayerTeamSide {
    HOME,
    AWAY
}

enum class PlayerActionState {
    IDLE,
    RUNNING,
    SPRINTING,
    TACKLING,
    SLIDING,
    SHOOTING,
    PASSING,
    SKILL_MOVE,
    GOAL_CELEBRATION
}

data class GamePlayer(
    val id: String,
    val name: String,
    val jerseyNumber: Int,
    val teamSide: PlayerTeamSide,
    val positionLabel: String,         // GK, CB, LB, RB, CM, CAM, ST, etc.
    var position: Vector2D,            // Current X, Y coordinates on pitch
    var velocity: Vector2D = Vector2D.ZERO,
    var targetPosition: Vector2D = position,
    var facingDirection: Vector2D = if (teamSide == PlayerTeamSide.HOME) Vector2D(1f, 0f) else Vector2D(-1f, 0f),
    var isControlledByUser: Boolean = false,
    var state: PlayerActionState = PlayerActionState.IDLE,
    val overallRating: Int = 78,
    val pace: Float = 75f,             // 50-95
    val shooting: Float = 75f,
    val passing: Float = 75f,
    val defending: Float = 75f,
    val diskiFlavour: Int = 80,        // 60-99 (Kasi trick proficiency)
    var stamina: Float = 100f,
    var actionTimer: Float = 0f,       // e.g. duration of tackle slide or skill move
    var isStunned: Boolean = false,    // Stunned by opponent's Kasi Flavour trick
    var stunTimer: Float = 0f,
    var activeSkillName: String? = null,
    val baseFormationSlot: Vector2D    // Tactical home position on pitch
) {
    val speed: Float
        get() {
            if (isStunned) return 0f
            val baseSpeed = 160f + (pace * 1.5f)
            return when (state) {
                PlayerActionState.SPRINTING -> baseSpeed * 1.35f
                PlayerActionState.SKILL_MOVE -> baseSpeed * 1.15f
                else -> baseSpeed
            }
        }
}
