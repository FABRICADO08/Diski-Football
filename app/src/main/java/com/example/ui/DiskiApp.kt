package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.game.ui.MatchPitchScreen
import com.example.game.ui.PenaltyShootoutScreen
import androidx.compose.runtime.rememberCoroutineScope
import com.example.ui.online.OnlineMultiplayerLobbyScreen
import com.example.ui.online.OnlinePenaltyDuelScreen
import com.example.ui.phase1.Phase1Screen
import com.example.ui.phase1.Phase1ViewModel
import com.example.ui.phase1.SquadDialog
import com.example.ui.phase2.MainMenuScreen
import com.example.ui.phase2.ManagerDashboardScreen
import com.example.ui.phase2.QuickMatchSetupScreen
import com.example.ui.phase2.StandingsScreen
import com.example.ui.phase2.TeamSelectScreen
import com.example.ui.phase2.TournamentBracketsScreen
import com.example.ui.theme.SurfaceDark
import kotlinx.coroutines.launch

@Composable
fun DiskiApp(
    viewModel: MainGameViewModel,
    phase1ViewModel: Phase1ViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val activeCareer by viewModel.activeCareer.collectAsStateWithLifecycle()
    val userTeam by viewModel.userTeam.collectAsStateWithLifecycle()
    val userSquad by viewModel.userSquad.collectAsStateWithLifecycle()
    val nextMatch by viewModel.nextMatch.collectAsStateWithLifecycle()
    val allTeams by viewModel.allTeams.collectAsStateWithLifecycle()
    val pslTeams by viewModel.pslTeams.collectAsStateWithLifecycle()
    val nfdTeams by viewModel.nfdTeams.collectAsStateWithLifecycle()
    val pslStandings by viewModel.pslStandings.collectAsStateWithLifecycle()
    val nfdStandings by viewModel.nfdStandings.collectAsStateWithLifecycle()
    val pslMatches by viewModel.pslMatches.collectAsStateWithLifecycle()
    val mtn8Matches by viewModel.mtn8Matches.collectAsStateWithLifecycle()
    val carlingMatches by viewModel.carlingMatches.collectAsStateWithLifecycle()
    val nedbankMatches by viewModel.nedbankMatches.collectAsStateWithLifecycle()
    val playoffMatches by viewModel.playoffMatches.collectAsStateWithLifecycle()
    val playoffStandings by viewModel.playoffStandings.collectAsStateWithLifecycle()
    val cblMatches by viewModel.cblMatches.collectAsStateWithLifecycle()
    val marketPlayers by viewModel.transferMarketPlayers.collectAsStateWithLifecycle()
    val selectedTeamForModal by viewModel.selectedTeamForModal.collectAsStateWithLifecycle()
    val selectedTeamSquad by viewModel.selectedTeamSquad.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    val pitchHomeTeam by viewModel.pitchMatchHomeTeam.collectAsStateWithLifecycle()
    val pitchAwayTeam by viewModel.pitchMatchAwayTeam.collectAsStateWithLifecycle()
    val pitchHomeSquad by viewModel.pitchMatchHomeSquad.collectAsStateWithLifecycle()
    val pitchAwaySquad by viewModel.pitchMatchAwaySquad.collectAsStateWithLifecycle()
    val pitchTournamentName by viewModel.pitchMatchTournamentName.collectAsStateWithLifecycle()
    val pitchMatchDurationSeconds by viewModel.pitchMatchDurationSeconds.collectAsStateWithLifecycle()
    val pitchMatchDifficulty by viewModel.pitchMatchDifficulty.collectAsStateWithLifecycle()
    val penaltyHomeTeam by viewModel.penaltyHomeTeam.collectAsStateWithLifecycle()
    val penaltyAwayTeam by viewModel.penaltyAwayTeam.collectAsStateWithLifecycle()
    val isTwoPlayerPenaltyMode by viewModel.isTwoPlayerPenaltyMode.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val currentOnlineRoom by viewModel.currentOnlineRoom.collectAsStateWithLifecycle()
    val isOnlineLoading by viewModel.isOnlineLoading.collectAsStateWithLifecycle()
    val onlineError by viewModel.onlineError.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissSnackbar()
        }
    }

    if (selectedTeamForModal != null) {
        BackHandler {
            viewModel.dismissSquadModal()
        }
    } else if (currentScreen != ScreenRoute.MAIN_MENU) {
        BackHandler {
            when (currentScreen) {
                ScreenRoute.PENALTY_SHOOTOUT -> {
                    viewModel.navigateTo(if (activeCareer != null) ScreenRoute.CAREER_DASHBOARD else ScreenRoute.MAIN_MENU)
                }
                ScreenRoute.ONLINE_MULTIPLAYER_LOBBY, ScreenRoute.ONLINE_PENALTY_DUEL -> {
                    viewModel.exitOnlineMatch()
                }
                else -> {
                    viewModel.navigateTo(ScreenRoute.MAIN_MENU)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SurfaceDark,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                ScreenRoute.MAIN_MENU -> {
                    MainMenuScreen(
                        activeCareer = activeCareer,
                        userTeam = userTeam,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
                ScreenRoute.CAREER_DASHBOARD -> {
                    val career = activeCareer
                    val team = userTeam
                    if (career != null && team != null) {
                        ManagerDashboardScreen(
                            career = career,
                            team = team,
                            squad = userSquad,
                            nextMatch = nextMatch,
                            marketPlayers = marketPlayers,
                            onBack = { viewModel.navigateTo(ScreenRoute.MAIN_MENU) },
                            onSimulateNextMatch = { viewModel.simulateNextMatch() },
                            onPlayNextMatch = {
                                val m = nextMatch
                                val t = userTeam
                                if (m != null && t != null) {
                                    val opponentId = if (m.homeTeamId == t.id) m.awayTeamId else m.homeTeamId
                                    val opponent = allTeams.find { it.id == opponentId }
                                    if (opponent != null) {
                                        val home = if (m.homeTeamId == t.id) t else opponent
                                        val away = if (m.homeTeamId == t.id) opponent else t
                                        viewModel.launchPitchMatch(home, away, m.stage, m.id)
                                    }
                                }
                            },
                            onChangeFormation = { viewModel.changeFormation(it) },
                            onSwapStarter = { starterId, subId -> viewModel.swapPlayers(starterId, subId) },
                            onBuyPlayer = { viewModel.buyPlayer(it) }
                        )
                    } else {
                        // Fallback to team select if no career
                        TeamSelectScreen(
                            pslTeams = pslTeams,
                            nfdTeams = nfdTeams,
                            onBack = { viewModel.navigateTo(ScreenRoute.MAIN_MENU) },
                            onStartCareer = { name, teamId, diff ->
                                viewModel.startNewCareer(name, teamId, diff)
                            }
                        )
                    }
                }
                ScreenRoute.NEW_CAREER_SELECT -> {
                    TeamSelectScreen(
                        pslTeams = pslTeams,
                        nfdTeams = nfdTeams,
                        onBack = { viewModel.navigateTo(ScreenRoute.MAIN_MENU) },
                        onStartCareer = { name, teamId, diff ->
                            viewModel.startNewCareer(name, teamId, diff)
                        }
                    )
                }
                ScreenRoute.LEAGUE_STANDINGS -> {
                    StandingsScreen(
                        pslStandings = pslStandings,
                        nfdStandings = nfdStandings,
                        allTeams = allTeams,
                        fixtures = pslMatches,
                        onBack = { viewModel.navigateTo(ScreenRoute.MAIN_MENU) },
                        onTeamClick = { viewModel.showTeamSquadModal(it) },
                        onSimulateMatch = { viewModel.simulateSpecificMatch(it) }
                    )
                }
                ScreenRoute.TOURNAMENT_BRACKETS -> {
                    TournamentBracketsScreen(
                        mtn8Matches = mtn8Matches,
                        carlingMatches = carlingMatches,
                        nedbankMatches = nedbankMatches,
                        cblMatches = cblMatches,
                        playoffMatches = playoffMatches,
                        playoffStandings = playoffStandings,
                        allTeams = allTeams,
                        onBack = { viewModel.navigateTo(ScreenRoute.MAIN_MENU) },
                        onSimulateMatch = { viewModel.simulateSpecificMatch(it) },
                        onPlayMatch = { home, away, stage, matchId ->
                            viewModel.launchPitchMatch(home, away, stage, matchId)
                        },
                        onTeamClick = { viewModel.showTeamSquadModal(it) },
                        onGenerateCarlingDraw = { viewModel.generateCarlingDraw() },
                        onGenerateNedbankDraw = { viewModel.generateNedbankDraw() },
                        onGenerateCblDraw = { viewModel.generateCarlingBlackLabelDraw() },
                        onVoteCbl = { team, vote -> viewModel.voteCarlingBlackLabel(team, vote) },
                        onStartPlayoffs = { viewModel.startPromotionPlayoffs() },
                        onFinalizeSeason = { viewModel.finalizeSeasonPromotionRelegation() }
                    )
                }
                ScreenRoute.QUICK_MATCH_SETUP -> {
                    QuickMatchSetupScreen(
                        allTeams = allTeams,
                        onBack = { viewModel.navigateTo(ScreenRoute.MAIN_MENU) },
                        onSimulateQuickMatch = { home, away ->
                            viewModel.simulateNextMatch()
                            viewModel.navigateTo(ScreenRoute.MAIN_MENU)
                        },
                        onPlayQuickMatch = { homeId, awayId, durationSecs, difficulty ->
                            val home = allTeams.find { it.id == homeId } ?: allTeams.firstOrNull()
                            val away = allTeams.find { it.id == awayId } ?: allTeams.lastOrNull()
                            if (home != null && away != null) {
                                viewModel.launchPitchMatch(home, away, "Exhibition Match", null, durationSecs, difficulty)
                            }
                        },
                        onPlayPenaltyShootout = { homeId, awayId, is2P ->
                            val home = allTeams.find { it.id == homeId } ?: allTeams.firstOrNull()
                            val away = allTeams.find { it.id == awayId } ?: allTeams.lastOrNull()
                            if (home != null && away != null) {
                                viewModel.launchPenaltyShootout(home, away, is2P)
                            }
                        }
                    )
                }
                ScreenRoute.MATCH_PITCH -> {
                    val hTeam = pitchHomeTeam
                    val aTeam = pitchAwayTeam
                    if (hTeam != null && aTeam != null) {
                        MatchPitchScreen(
                            homeTeam = hTeam,
                            awayTeam = aTeam,
                            homeSquad = pitchHomeSquad,
                            awaySquad = pitchAwaySquad,
                            tournamentName = pitchTournamentName,
                            matchDurationSeconds = pitchMatchDurationSeconds,
                            difficulty = pitchMatchDifficulty,
                            onMatchCompleted = { stats -> viewModel.completePitchMatch(stats) },
                            onExitToMenu = { viewModel.navigateTo(ScreenRoute.MAIN_MENU) },
                            onGoToPenalties = {
                                viewModel.launchPenaltyShootout(hTeam, aTeam)
                            }
                        )
                    } else {
                        viewModel.navigateTo(ScreenRoute.MAIN_MENU)
                    }
                }
                ScreenRoute.PENALTY_SHOOTOUT -> {
                    val hTeam = penaltyHomeTeam ?: userTeam ?: allTeams.firstOrNull { it.id.contains("chiefs") } ?: allTeams.firstOrNull()
                    val aTeam = penaltyAwayTeam ?: allTeams.firstOrNull { it.id.contains("pirates") } ?: allTeams.getOrNull(1) ?: allTeams.lastOrNull()
                    if (hTeam != null && aTeam != null) {
                        PenaltyShootoutScreen(
                            homeTeam = hTeam,
                            awayTeam = aTeam,
                            isTwoPlayerMode = isTwoPlayerPenaltyMode,
                            onFinished = { homeScore, awayScore ->
                                viewModel.completePenaltyShootout(homeScore, awayScore)
                            },
                            onExit = {
                                viewModel.navigateTo(if (activeCareer != null) ScreenRoute.CAREER_DASHBOARD else ScreenRoute.MAIN_MENU)
                            }
                        )
                    } else {
                        viewModel.navigateTo(ScreenRoute.MAIN_MENU)
                    }
                }
                ScreenRoute.CLUB_DATABASE -> {
                    Phase1Screen(
                        viewModel = phase1ViewModel,
                        onBackToMenu = { viewModel.navigateTo(ScreenRoute.MAIN_MENU) }
                    )
                }
                ScreenRoute.ONLINE_MULTIPLAYER_LOBBY -> {
                    OnlineMultiplayerLobbyScreen(
                        currentUser = currentUser,
                        userProfile = userProfile,
                        currentRoom = currentOnlineRoom,
                        isLoading = isOnlineLoading,
                        errorMessage = onlineError,
                        allTeams = allTeams,
                        onSignInWithGoogle = {
                            coroutineScope.launch {
                                viewModel.googleAuthManager.signInWithGoogle()
                                viewModel.refreshAuthState()
                            }
                        },
                        onCreateRoom = { hostTeam, gamerTag ->
                            viewModel.createOnlineRoom(hostTeam, gamerTag)
                        },
                        onJoinRoom = { code, guestTeam, gamerTag ->
                            viewModel.joinOnlineRoom(code, guestTeam, gamerTag)
                        },
                        onCancelRoom = {
                            viewModel.exitOnlineMatch()
                        },
                        onBack = {
                            viewModel.navigateTo(ScreenRoute.MAIN_MENU)
                        },
                        onDismissError = {
                            viewModel.dismissOnlineError()
                        }
                    )
                }
                ScreenRoute.ONLINE_PENALTY_DUEL -> {
                    OnlinePenaltyDuelScreen(
                        room = currentOnlineRoom,
                        currentUser = currentUser,
                        allTeams = allTeams,
                        onTakeShot = { scored, commentary ->
                            viewModel.takeOnlinePenaltyShot(scored, commentary)
                        },
                        onExit = {
                            viewModel.exitOnlineMatch()
                        }
                    )
                }
                else -> {
                    MainMenuScreen(
                        activeCareer = activeCareer,
                        userTeam = userTeam,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
            }

            // Global Team Squad Dialog
            selectedTeamForModal?.let { team ->
                SquadDialog(
                    team = team,
                    squad = selectedTeamSquad,
                    onDismiss = { viewModel.dismissSquadModal() }
                )
            }
        }
    }
}
