package com.example.ui.phase1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.DiskiDatabase
import com.example.data.model.LeagueType
import com.example.data.model.PlayerEntity
import com.example.data.model.TeamEntity
import com.example.data.model.TournamentEntity
import com.example.data.repository.DatabaseStats
import com.example.data.repository.DiskiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class Phase1UiState(
    val isLoading: Boolean = true,
    val selectedTab: Int = 0, // 0: PSL, 1: NFD, 2: Cups, 3: DB Architecture
    val searchQuery: String = "",
    val selectedTeam: TeamEntity? = null,
    val selectedTeamSquad: List<PlayerEntity> = emptyList(),
    val stats: DatabaseStats? = null,
    val statusMessage: String = "Initializing South African Football Database..."
)

class Phase1ViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DiskiRepository(DiskiDatabase.getInstance(application))

    private val _uiState = MutableStateFlow(Phase1UiState())
    val uiState: StateFlow<Phase1UiState> = _uiState.asStateFlow()

    val pslTeams: StateFlow<List<TeamEntity>> = repository.getPslTeams()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val nfdTeams: StateFlow<List<TeamEntity>> = repository.getNfdTeams()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tournaments: StateFlow<List<TournamentEntity>> = repository.getAllTournaments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        initializeDatabase()
    }

    fun initializeDatabase() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, statusMessage = "Seeding PSL & NFD Teams, Squads, and Cups...")
            repository.initialize()
            refreshStats()
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                statusMessage = "Phase 1 Complete: 32 Teams & Cup Schemas Loaded in Room DB"
            )
        }
    }

    fun reseedDatabase() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, statusMessage = "Re-seeding South African football database...")
            repository.reseedDatabase()
            refreshStats()
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                statusMessage = "Database refreshed successfully!"
            )
        }
    }

    private suspend fun refreshStats() {
        val stats = repository.getStats()
        _uiState.value = _uiState.value.copy(stats = stats)
    }

    fun setTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun selectTeam(team: TeamEntity?) {
        if (team == null) {
            _uiState.value = _uiState.value.copy(selectedTeam = null, selectedTeamSquad = emptyList())
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(selectedTeam = team)
            repository.getSquadForTeam(team.id).collect { squad ->
                _uiState.value = _uiState.value.copy(selectedTeamSquad = squad)
            }
        }
    }
}
