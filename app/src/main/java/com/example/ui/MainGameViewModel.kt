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
    PENALTY_SHOOTOUT
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

    private var activePitchMatchEntityId: String? = null

    fun launchPitchMatch(
        homeTeam: TeamEntity,
        awayTeam: TeamEntity,
        tournamentName: String = "Betway Premiership",
        matchEntityId: String? = null
    ) {
        viewModelScope.launch {
            _pitchMatchHomeTeam.value = homeTeam
            _pitchMatchAwayTeam.value = awayTeam
            _pitchMatchTournamentName.value = tournamentName
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

    fun launchPenaltyShootout(home: TeamEntity, away: TeamEntity) {
        _penaltyHomeTeam.value = home
        _penaltyAwayTeam.value = away
        _currentScreen.value = ScreenRoute.PENALTY_SHOOTOUT
    }

    fun completePenaltyShootout(homeScore: Int, awayScore: Int) {
        val home = _penaltyHomeTeam.value?.shortName ?: "Home"
        val away = _penaltyAwayTeam.value?.shortName ?: "Away"
        _snackbarMessage.value = "Shootout Completed! $home $homeScore - $awayScore $away"
        _currentScreen.value = if (activeCareer.value != null) ScreenRoute.CAREER_DASHBOARD else ScreenRoute.MAIN_MENU
    }

    fun dismissSnackbar() {
        _snackbarMessage.value = null
    }
}
