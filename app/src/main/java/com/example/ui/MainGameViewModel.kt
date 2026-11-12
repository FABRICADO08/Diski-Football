package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.DiskiDatabase
import com.example.data.model.CareerSaveEntity
import com.example.data.model.LeagueType
import com.example.data.model.MatchEntity
import com.example.data.model.PlayerEntity
import com.example.data.model.StandingEntity
import com.example.data.model.TeamEntity
import com.example.data.model.TournamentEntity
import com.example.data.model.TournamentType
import com.example.data.repository.DiskiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.game.engine.MatchStats
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.example.auth.GoogleAuthManager
import com.example.data.model.MatchRoom
import com.example.data.model.UserProfile
import com.example.data.repository.MultiplayerRepository
import com.google.firebase.auth.FirebaseUser
import kotlin.random.Random

enum class ScreenRoute {
    MAIN_MENU,
    CAREER_DASHBOARD,
    NEW_CAREER_SELECT,
    LEAGUE_STANDINGS,
    TOURNAMENT_BRACKETS,
    QUICK_MATCH_SETUP,
    CLUB_DATABASE,
    TACTICS_BOARD,
    TRANSFER_MARKET,
    MATCH_PITCH,
    PENALTY_SHOOTOUT,
    ONLINE_MULTIPLAYER_LOBBY,
    ONLINE_PENALTY_DUEL
}

class MainGameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DiskiRepository(DiskiDatabase.getInstance(application))

    private val _currentScreen = MutableStateFlow(ScreenRoute.MAIN_MENU)
    val currentScreen: StateFlow<ScreenRoute> = _currentScreen.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    val activeCareer: StateFlow<CareerSaveEntity?> = repository.getActiveCareerSave()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allTeams: StateFlow<List<TeamEntity>> = repository.getAllTeams()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pslTeams: StateFlow<List<TeamEntity>> = repository.getPslTeams()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val nfdTeams: StateFlow<List<TeamEntity>> = repository.getNfdTeams()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pslStandings: StateFlow<List<StandingEntity>> = repository.getStandings(TournamentType.PSL)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val nfdStandings: StateFlow<List<StandingEntity>> = repository.getStandings(TournamentType.NFD)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pslMatches: StateFlow<List<MatchEntity>> = repository.getMatches(TournamentType.PSL)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mtn8Matches: StateFlow<List<MatchEntity>> = repository.getMatches(TournamentType.MTN8)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val carlingMatches: StateFlow<List<MatchEntity>> = repository.getMatches(TournamentType.CARLING_CKO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val nedbankMatches: StateFlow<List<MatchEntity>> = repository.getMatches(TournamentType.NEDBANK_CUP)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val playoffMatches: StateFlow<List<MatchEntity>> = repository.getMatches(TournamentType.PLAYOFF)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val playoffStandings: StateFlow<List<StandingEntity>> = repository.getStandings(TournamentType.PLAYOFF)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cblMatches: StateFlow<List<MatchEntity>> = repository.getMatches(TournamentType.CARLING_BLACK_LABEL)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTournaments: StateFlow<List<TournamentEntity>> = repository.getAllTournaments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Career specific reactive state
    private val _userTeam = MutableStateFlow<TeamEntity?>(null)
    val userTeam: StateFlow<TeamEntity?> = _userTeam.asStateFlow()

    private val _userSquad = MutableStateFlow<List<PlayerEntity>>(emptyList())
    val userSquad: StateFlow<List<PlayerEntity>> = _userSquad.asStateFlow()

    private val _nextMatch = MutableStateFlow<MatchEntity?>(null)
    val nextMatch: StateFlow<MatchEntity?> = _nextMatch.asStateFlow()

    private val _transferMarketPlayers = MutableStateFlow<List<PlayerEntity>>(emptyList())
    val transferMarketPlayers: StateFlow<List<PlayerEntity>> = _transferMarketPlayers.asStateFlow()

    // Squad modal state
    private val _selectedTeamForModal = MutableStateFlow<TeamEntity?>(null)
    val selectedTeamForModal: StateFlow<TeamEntity?> = _selectedTeamForModal.asStateFlow()

    private val _selectedTeamSquad = MutableStateFlow<List<PlayerEntity>>(emptyList())
    val selectedTeamSquad: StateFlow<List<PlayerEntity>> = _selectedTeamSquad.asStateFlow()

    // --- ONLINE MULTIPLAYER (1v1 Across Devices) ---
    val googleAuthManager = GoogleAuthManager(application)
    val multiplayerRepository = MultiplayerRepository(application)

    private val _currentUser = MutableStateFlow<FirebaseUser?>(googleAuthManager.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _currentOnlineRoom = MutableStateFlow<MatchRoom?>(null)
    val currentOnlineRoom: StateFlow<MatchRoom?> = _currentOnlineRoom.asStateFlow()

    private val _isOnlineLoading = MutableStateFlow(false)
    val isOnlineLoading: StateFlow<Boolean> = _isOnlineLoading.asStateFlow()

    private val _onlineError = MutableStateFlow<String?>(null)
    val onlineError: StateFlow<String?> = _onlineError.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initialize()
            // Observe active career to update user team and squad
            activeCareer.collect { career ->
                if (career != null) {
                    val team = repository.getTeamDirect(career.selectedTeamId)
                    _userTeam.value = team
                    _nextMatch.value = repository.getNextMatchForTeam(career.selectedTeamId)
                    loadUserSquad(career.selectedTeamId)
                    loadTransferMarket(career.selectedTeamId)
                } else {
                    _userTeam.value = null
                    _userSquad.value = emptyList()
                    _nextMatch.value = null
                }
            }
        }
        refreshAuthState()
    }

    private fun loadUserSquad(teamId: String) {
        viewModelScope.launch {
            repository.getSquadForTeam(teamId).collect {
                _userSquad.value = it
            }
        }
    }

    private fun loadTransferMarket(excludeTeamId: String) {
        viewModelScope.launch {
            repository.getTransferMarketPlayers(excludeTeamId).collect {
                _transferMarketPlayers.value = it
            }
        }
    }

    fun navigateTo(route: ScreenRoute) {
        _currentScreen.value = route
    }

    fun startNewCareer(managerName: String, teamId: String, difficulty: String) {
        viewModelScope.launch {
            val career = repository.createNewCareer(managerName, teamId, difficulty)
            val team = repository.getTeamDirect(teamId)
            _userTeam.value = team
            _nextMatch.value = repository.getNextMatchForTeam(teamId)
            _snackbarMessage.value = "Career started! Welcome, Boss ${managerName}!"
            _currentScreen.value = ScreenRoute.CAREER_DASHBOARD
        }
    }

    fun simulateNextMatch() {
        val match = _nextMatch.value ?: return
        viewModelScope.launch {
            val result = repository.simulateMatch(match.id)
            if (result != null) {
                _snackbarMessage.value = "Match Result: ${result.matchSummary}"
                val career = activeCareer.value
                if (career != null) {
                    _nextMatch.value = repository.getNextMatchForTeam(career.selectedTeamId)
                }
            }
        }
    }

    fun simulateSpecificMatch(matchId: String) {
        viewModelScope.launch {
            val result = repository.simulateMatch(matchId)
            if (result != null) {
                _snackbarMessage.value = result.matchSummary
            }
        }
    }

    fun changeFormation(formation: String) {
        viewModelScope.launch {
            repository.updateCareerFormation(formation)
            _snackbarMessage.value = "Formation updated to $formation"
        }
    }

    fun swapPlayers(starterId: String, subId: String) {
        viewModelScope.launch {
            repository.swapStarterStatus(starterId, subId)
            _snackbarMessage.value = "Starting XI updated!"
        }
    }

    fun buyPlayer(player: PlayerEntity) {
        val career = activeCareer.value ?: return
        if (career.transferBudgetRemaining < player.marketValue) {
            _snackbarMessage.value = "Insufficient transfer funds!"
            return
        }
        viewModelScope.launch {
            repository.buyPlayer(player.id, career.selectedTeamId)
            _snackbarMessage.value = "Signed ${player.name} for R ${(player.marketValue / 1_000_000)}M!"
        }
    }

    fun sellPlayer(player: PlayerEntity, buyerTeamId: String, transferFee: Long) {
        val career = activeCareer.value ?: return
        viewModelScope.launch {
            repository.sellPlayer(player.id, buyerTeamId, transferFee)
            val buyerName = allTeams.value.find { it.id == buyerTeamId }?.shortName ?: "rival club"
            _snackbarMessage.value = "Sold ${player.name} to $buyerName for R ${(transferFee / 1_000_000)}M!"
        }
    }

    fun showTeamSquadModal(team: TeamEntity) {
        viewModelScope.launch {
            _selectedTeamForModal.value = team
            repository.getSquadForTeam(team.id).collect {
                _selectedTeamSquad.value = it
            }
        }
    }

    fun dismissSquadModal() {
        _selectedTeamForModal.value = null
        _selectedTeamSquad.value = emptyList()
    }

    fun generateCarlingDraw() {
        viewModelScope.launch {
            repository.generateCarlingKnockoutTournament()
            _snackbarMessage.value = "Carling Knockout Cup Round of 16 drawn!"
        }
    }

    fun generateNedbankDraw() {
        viewModelScope.launch {
            repository.generateNedbankCupTournament()
            _snackbarMessage.value = "Nedbank Cup 32-team national draw created!"
        }
    }

    fun startPromotionPlayoffs() {
        viewModelScope.launch {
            repository.startPromotionPlayoffTournament()
            _snackbarMessage.value = "Promotion/Relegation Play-off mini-league started!"
        }
    }

    fun finalizeSeasonPromotionRelegation() {
        viewModelScope.launch {
            val result = repository.executeSeasonEndPromotionRelegation()
            _snackbarMessage.value = "Season Finalized! ${result.autoPromotedNfdTeamId} promoted, ${result.autoRelegatedPslTeamId} relegated."
        }
    }

    fun generateCarlingBlackLabelDraw() {
        viewModelScope.launch {
            repository.generateCarlingBlackLabelTournament()
            _snackbarMessage.value = "Carling Black Label Cup pre-season spectacle seeded!"
        }
    }

    fun voteCarlingBlackLabel(teamName: String, playerOrTactic: String) {
        _snackbarMessage.value = "Fan Vote Registered! $teamName supporters voted for $playerOrTactic (+5 Team Morale & Flair)!"
    }

    // 2D Pitch Match State
    private val _pitchMatchHomeTeam = MutableStateFlow<TeamEntity?>(null)
    val pitchMatchHomeTeam: StateFlow<TeamEntity?> = _pitchMatchHomeTeam.asStateFlow()

    private val _pitchMatchAwayTeam = MutableStateFlow<TeamEntity?>(null)
    val pitchMatchAwayTeam: StateFlow<TeamEntity?> = _pitchMatchAwayTeam.asStateFlow()

    private val _pitchMatchHomeSquad = MutableStateFlow<List<PlayerEntity>>(emptyList())
    val pitchMatchHomeSquad: StateFlow<List<PlayerEntity>> = _pitchMatchHomeSquad.asStateFlow()

    private val _pitchMatchAwaySquad = MutableStateFlow<List<PlayerEntity>>(emptyList())
    val pitchMatchAwaySquad: StateFlow<List<PlayerEntity>> = _pitchMatchAwaySquad.asStateFlow()

    private val _pitchMatchTournamentName = MutableStateFlow("Betway Premiership")
    val pitchMatchTournamentName: StateFlow<String> = _pitchMatchTournamentName.asStateFlow()

    private val _pitchMatchDurationSeconds = MutableStateFlow(180f)
    val pitchMatchDurationSeconds: StateFlow<Float> = _pitchMatchDurationSeconds.asStateFlow()

    private val _pitchMatchDifficulty = MutableStateFlow("Medium")
    val pitchMatchDifficulty: StateFlow<String> = _pitchMatchDifficulty.asStateFlow()

    private var activePitchMatchEntityId: String? = null

    fun launchPitchMatch(
        homeTeam: TeamEntity,
        awayTeam: TeamEntity,
        tournamentName: String = "Betway Premiership",
        matchEntityId: String? = null,
        matchDurationSeconds: Float = 180f,
        difficulty: String = "Medium"
    ) {
        viewModelScope.launch {
            _pitchMatchHomeTeam.value = homeTeam
            _pitchMatchAwayTeam.value = awayTeam
            _pitchMatchTournamentName.value = tournamentName
            _pitchMatchDurationSeconds.value = matchDurationSeconds
            _pitchMatchDifficulty.value = difficulty
            activePitchMatchEntityId = matchEntityId

            val homeSq = repository.getSquadForTeam(homeTeam.id).firstOrNull() ?: emptyList()
            val awaySq = repository.getSquadForTeam(awayTeam.id).firstOrNull() ?: emptyList()
            _pitchMatchHomeSquad.value = homeSq
            _pitchMatchAwaySquad.value = awaySq

            _currentScreen.value = ScreenRoute.MATCH_PITCH
        }
    }

    fun completePitchMatch(stats: MatchStats) {
        viewModelScope.launch {
            val matchId = activePitchMatchEntityId
            if (matchId != null) {
                val homeName = _pitchMatchHomeTeam.value?.shortName ?: "Home"
                val awayName = _pitchMatchAwayTeam.value?.shortName ?: "Away"
                val summary = "FT: $homeName ${stats.homeScore} - ${stats.awayScore} $awayName"
                repository.recordPlayedMatchResult(
                    matchId = matchId,
                    homeGoals = stats.homeScore,
                    awayGoals = stats.awayScore,
                    matchSummary = summary
                )
                val career = activeCareer.value
                if (career != null) {
                    _nextMatch.value = repository.getNextMatchForTeam(career.selectedTeamId)
                }
            }

            _snackbarMessage.value = "Match Finished! Score: ${stats.homeScore} - ${stats.awayScore}"
            // Return to career dashboard if career is active, else main menu
            _currentScreen.value = if (activeCareer.value != null) ScreenRoute.CAREER_DASHBOARD else ScreenRoute.MAIN_MENU
        }
    }

    // Penalty Shootout State
    private val _penaltyHomeTeam = MutableStateFlow<TeamEntity?>(null)
    val penaltyHomeTeam: StateFlow<TeamEntity?> = _penaltyHomeTeam.asStateFlow()

    private val _penaltyAwayTeam = MutableStateFlow<TeamEntity?>(null)
    val penaltyAwayTeam: StateFlow<TeamEntity?> = _penaltyAwayTeam.asStateFlow()

    private val _isTwoPlayerPenaltyMode = MutableStateFlow(false)
    val isTwoPlayerPenaltyMode: StateFlow<Boolean> = _isTwoPlayerPenaltyMode.asStateFlow()

    fun launchPenaltyShootout(home: TeamEntity, away: TeamEntity, isTwoPlayer: Boolean = false) {
        _penaltyHomeTeam.value = home
        _penaltyAwayTeam.value = away
        _isTwoPlayerPenaltyMode.value = isTwoPlayer
        _currentScreen.value = ScreenRoute.PENALTY_SHOOTOUT
    }

    fun completePenaltyShootout(homeScore: Int, awayScore: Int) {
        val home = _penaltyHomeTeam.value?.shortName ?: "Home"
        val away = _penaltyAwayTeam.value?.shortName ?: "Away"
        val is2P = _isTwoPlayerPenaltyMode.value
        _snackbarMessage.value = if (is2P) {
            val winner = if (homeScore > awayScore) "Player 1 ($home)" else if (awayScore > homeScore) "Player 2 ($away)" else "Draw"
            "1v1 Shootout Complete! $home $homeScore - $awayScore $away ($winner wins!)"
        } else {
            "Shootout Completed! $home $homeScore - $awayScore $away"
        }
        _currentScreen.value = if (activeCareer.value != null) ScreenRoute.CAREER_DASHBOARD else ScreenRoute.MAIN_MENU
    }

    fun dismissSnackbar() {
        _snackbarMessage.value = null
    }

    fun refreshAuthState() {
        val user = googleAuthManager.currentUser
        _currentUser.value = user
        if (user != null) {
            viewModelScope.launch {
                multiplayerRepository.observeUserProfile(user.uid).collect { profile ->
                    if (profile == null) {
                        val initialProfile = UserProfile(
                            userId = user.uid,
                            displayName = user.displayName ?: "Mzansi Striker",
                            email = user.email,
                            favoriteTeamId = _userTeam.value?.id ?: "kaizer_chiefs",
                            wins = 0L,
                            losses = 0L
                        )
                        multiplayerRepository.saveUserProfile(initialProfile)
                        _userProfile.value = initialProfile
                    } else {
                        _userProfile.value = profile
                    }
                }
            }
        } else {
            _userProfile.value = null
        }
    }

    fun dismissOnlineError() {
        _onlineError.value = null
    }

    fun createOnlineRoom(hostTeam: TeamEntity, gamerTag: String) {
        val user = _currentUser.value
        if (user == null) {
            _onlineError.value = "Please sign in with Google to host an online match."
            return
        }

        viewModelScope.launch {
            _isOnlineLoading.value = true
            _onlineError.value = null
            try {
                val code = "DK-${Random.nextInt(1000, 9999)}"
                val room = MatchRoom(
                    roomId = "",
                    roomCode = code,
                    hostUserId = user.uid,
                    hostDisplayName = gamerTag.ifBlank { user.displayName ?: "Host Mzansi" },
                    hostTeamId = hostTeam.id,
                    status = "WAITING_FOR_PLAYER",
                    currentTurn = "HOST_KICKING",
                    hostScore = 0L,
                    guestScore = 0L,
                    round = 1L,
                    hostKicks = emptyList(),
                    guestKicks = emptyList()
                )
                val newRoomId = multiplayerRepository.createMatchRoom(room)

                // Start observing room
                multiplayerRepository.observeMatchRoom(newRoomId).collect { updated ->
                    _currentOnlineRoom.value = updated
                    if (updated != null && updated.status == "IN_PROGRESS" && _currentScreen.value == ScreenRoute.ONLINE_MULTIPLAYER_LOBBY) {
                        _currentScreen.value = ScreenRoute.ONLINE_PENALTY_DUEL
                    }
                }
            } catch (e: Exception) {
                _onlineError.value = "Failed to create match room: ${e.localizedMessage ?: "Unknown error"}"
            } finally {
                _isOnlineLoading.value = false
            }
        }
    }

    fun joinOnlineRoom(code: String, guestTeam: TeamEntity, gamerTag: String) {
        val user = _currentUser.value
        if (user == null) {
            _onlineError.value = "Please sign in with Google to join an online match."
            return
        }

        viewModelScope.launch {
            _isOnlineLoading.value = true
            _onlineError.value = null
            try {
                val foundRoom = multiplayerRepository.findRoomByCode(code)
                if (foundRoom == null) {
                    _onlineError.value = "No active room found with code '$code'. Check the code and try again."
                    _isOnlineLoading.value = false
                    return@launch
                }

                if (foundRoom.hostUserId == user.uid) {
                    _onlineError.value = "You cannot join your own room as Player 2. Share the code with a friend on another device!"
                    _isOnlineLoading.value = false
                    return@launch
                }

                val guestName = gamerTag.ifBlank { user.displayName ?: "Guest Mzansi" }
                multiplayerRepository.joinMatchRoom(foundRoom.roomId, user.uid, guestName, guestTeam.id)
                _currentScreen.value = ScreenRoute.ONLINE_PENALTY_DUEL

                // Start observing room
                multiplayerRepository.observeMatchRoom(foundRoom.roomId).collect { updated ->
                    _currentOnlineRoom.value = updated
                }
            } catch (e: Exception) {
                _onlineError.value = "Failed to connect to room: ${e.localizedMessage ?: "Unknown error"}"
            } finally {
                _isOnlineLoading.value = false
            }
        }
    }

    fun takeOnlinePenaltyShot(scored: Boolean, commentary: String) {
        val room = _currentOnlineRoom.value ?: return
        val user = _currentUser.value ?: return
        val isHost = user.uid == room.hostUserId

        viewModelScope.launch {
            try {
                // Check if match decided
                val hScore = room.hostScore + if (isHost && scored) 1L else 0L
                val gScore = room.guestScore + if (!isHost && scored) 1L else 0L
                val hAttempts = room.hostKicks.size + if (isHost) 1 else 0
                val gAttempts = room.guestKicks.size + if (!isHost) 1 else 0

                var isGameOver = false
                var winnerUid: String? = null

                if (hAttempts >= 5 && gAttempts >= 5 && hAttempts == gAttempts) {
                    if (hScore > gScore) {
                        isGameOver = true
                        winnerUid = room.hostUserId
                    } else if (gScore > hScore) {
                        isGameOver = true
                        winnerUid = room.guestUserId
                    }
                } else if (hAttempts <= 5 && gAttempts <= 5) {
                    val hRem = 5 - hAttempts
                    val gRem = 5 - gAttempts
                    if (hScore > gScore + gRem) {
                        isGameOver = true
                        winnerUid = room.hostUserId
                    } else if (gScore > hScore + hRem) {
                        isGameOver = true
                        winnerUid = room.guestUserId
                    }
                }

                multiplayerRepository.recordPenaltyKick(
                    roomId = room.roomId,
                    isHostKick = isHost,
                    scored = scored,
                    commentary = commentary,
                    isGameOver = isGameOver,
                    winnerUserId = winnerUid
                )

                if (isGameOver && winnerUid != null) {
                    val won = user.uid == winnerUid
                    multiplayerRepository.recordMatchResult(user.uid, won)
                }
            } catch (e: Exception) {
                _onlineError.value = "Failed to submit spot kick: ${e.localizedMessage ?: "Network error"}"
            }
        }
    }

    fun exitOnlineMatch() {
        val room = _currentOnlineRoom.value
        val user = _currentUser.value
        if (room != null && user != null && room.hostUserId == user.uid) {
            viewModelScope.launch {
                multiplayerRepository.closeRoom(room.roomId)
            }
        }
        _currentOnlineRoom.value = null
        _currentScreen.value = ScreenRoute.MAIN_MENU
    }
}
