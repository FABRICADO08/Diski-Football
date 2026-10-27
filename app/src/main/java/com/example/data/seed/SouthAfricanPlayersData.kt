package com.example.data.seed

import com.example.data.model.PlayerEntity
import com.example.data.model.PlayerPosition
import com.example.data.model.TeamEntity

object SouthAfricanPlayersData {

    fun generateAllPlayers(teams: List<TeamEntity>): List<PlayerEntity> {
        val playerList = mutableListOf<PlayerEntity>()

        for (team in teams) {
            when (team.id) {
                "mamelodi_sundowns" -> playerList.addAll(generateSundownsSquad(team.id))
                "orlando_pirates" -> playerList.addAll(generatePiratesSquad(team.id))
                "kaizer_chiefs" -> playerList.addAll(generateChiefsSquad(team.id))
                "stellenbosch_fc" -> playerList.addAll(generateStellenboschSquad(team.id))
                "supersport_united" -> playerList.addAll(generateSuperSportSquad(team.id))
                "amazulu_fc" -> playerList.addAll(generateAmaZuluSquad(team.id))
                "sekhukhune_united" -> playerList.addAll(generateSekhukhuneSquad(team.id))
                "ts_galaxy" -> playerList.addAll(generateTSGalaxySquad(team.id))
                "cape_town_city" -> playerList.addAll(generateCTCitySquad(team.id))
                else -> playerList.addAll(generateGenericSquad(team))
            }
        }

        return playerList
    }

    private fun generateSundownsSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Ronwen Williams", position = PlayerPosition.GK,
            jerseyNumber = 30, age = 32, overall = 85, pace = 65, shooting = 30, passing = 78,
            dribbling = 60, defending = 85, physical = 76, stamina = 85, marketValue = 28_000_000L,
            weeklyWage = 120_000L, contractYearsRemaining = 3, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Khuliso Mudau", position = PlayerPosition.RB,
            jerseyNumber = 25, age = 29, overall = 82, pace = 86, shooting = 62, passing = 76,
            dribbling = 80, defending = 81, physical = 82, stamina = 88, marketValue = 22_000_000L,
            weeklyWage = 95_000L, contractYearsRemaining = 2, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Grant Kekana", position = PlayerPosition.CB,
            jerseyNumber = 20, age = 31, overall = 80, pace = 72, shooting = 45, passing = 73,
            dribbling = 68, defending = 82, physical = 83, stamina = 82, marketValue = 16_000_000L,
            weeklyWage = 85_000L, contractYearsRemaining = 2, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Mothobi Mvala", position = PlayerPosition.CB,
            jerseyNumber = 34, age = 30, overall = 80, pace = 70, shooting = 50, passing = 71,
            dribbling = 66, defending = 83, physical = 86, stamina = 84, marketValue = 15_000_000L,
            weeklyWage = 80_000L, contractYearsRemaining = 2, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Aubrey Modiba", position = PlayerPosition.LB,
            jerseyNumber = 17, age = 29, overall = 81, pace = 82, shooting = 74, passing = 82,
            dribbling = 79, defending = 77, physical = 75, stamina = 85, marketValue = 18_000_000L,
            weeklyWage = 90_000L, contractYearsRemaining = 3, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Teboho Mokoena", position = PlayerPosition.CDM,
            jerseyNumber = 4, age = 27, overall = 84, pace = 78, shooting = 83, passing = 85,
            dribbling = 81, defending = 83, physical = 84, stamina = 90, marketValue = 35_000_000L,
            weeklyWage = 130_000L, contractYearsRemaining = 3, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Marcelo Allende", position = PlayerPosition.CM,
            jerseyNumber = 11, age = 25, nationality = "Chile", overall = 83, pace = 80, shooting = 77, passing = 86,
            dribbling = 84, defending = 72, physical = 74, stamina = 87, marketValue = 30_000_000L,
            weeklyWage = 115_000L, contractYearsRemaining = 2, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Themba Zwane", position = PlayerPosition.CAM,
            jerseyNumber = 18, age = 35, overall = 82, pace = 75, shooting = 80, passing = 86,
            dribbling = 85, defending = 55, physical = 70, stamina = 78, marketValue = 14_000_000L,
            weeklyWage = 140_000L, contractYearsRemaining = 1, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Lucas Ribeiro Costa", position = PlayerPosition.RW,
            jerseyNumber = 10, age = 26, nationality = "Brazil", overall = 84, pace = 86, shooting = 84, passing = 82,
            dribbling = 87, defending = 58, physical = 76, stamina = 84, marketValue = 38_000_000L,
            weeklyWage = 125_000L, contractYearsRemaining = 3, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Peter Shalulile", position = PlayerPosition.ST,
            jerseyNumber = 38, age = 31, nationality = "Namibia", overall = 84, pace = 88, shooting = 86, passing = 74,
            dribbling = 80, defending = 55, physical = 84, stamina = 89, marketValue = 32_000_000L,
            weeklyWage = 135_000L, contractYearsRemaining = 2, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Tashreeq Matthews", position = PlayerPosition.LW,
            jerseyNumber = 12, age = 24, overall = 80, pace = 87, shooting = 78, passing = 77,
            dribbling = 83, defending = 52, physical = 72, stamina = 83, marketValue = 20_000_000L,
            weeklyWage = 80_000L, contractYearsRemaining = 3, isCaptain = false, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Denis Onyango", position = PlayerPosition.GK,
            jerseyNumber = 1, age = 39, nationality = "Uganda", overall = 77, pace = 50, shooting = 25, passing = 65,
            dribbling = 50, defending = 78, physical = 80, stamina = 70, marketValue = 5_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Divine Lunga", position = PlayerPosition.LB,
            jerseyNumber = 3, age = 29, nationality = "Zimbabwe", overall = 77, pace = 80, shooting = 60, passing = 72,
            dribbling = 74, defending = 76, physical = 78, stamina = 80, marketValue = 10_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Sphelele Mkhulise", position = PlayerPosition.CM,
            jerseyNumber = 21, age = 28, overall = 78, pace = 77, shooting = 72, passing = 78,
            dribbling = 79, defending = 71, physical = 73, stamina = 82, marketValue = 12_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Iqraam Rayners", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 28, overall = 81, pace = 88, shooting = 82, passing = 75,
            dribbling = 81, defending = 54, physical = 79, stamina = 85, marketValue = 24_000_000L,
            weeklyWage = 90_000L, contractYearsRemaining = 3, isStartingEleven = false
        )
    )

    private fun generatePiratesSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Sipho Chaine", position = PlayerPosition.GK,
            jerseyNumber = 24, age = 27, overall = 81, pace = 62, shooting = 28, passing = 76,
            dribbling = 58, defending = 81, physical = 77, stamina = 84, marketValue = 18_000_000L,
            weeklyWage = 80_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Thabiso Monyane", position = PlayerPosition.RB,
            jerseyNumber = 2, age = 24, overall = 78, pace = 84, shooting = 60, passing = 74,
            dribbling = 76, defending = 77, physical = 77, stamina = 85, marketValue = 14_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Olisa Ndah", position = PlayerPosition.CB,
            jerseyNumber = 49, age = 26, nationality = "Nigeria", overall = 82, pace = 76, shooting = 48, passing = 75,
            dribbling = 72, defending = 83, physical = 85, stamina = 84, marketValue = 24_000_000L,
            weeklyWage = 95_000L, contractYearsRemaining = 2, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Tapelo Xoki", position = PlayerPosition.CB,
            jerseyNumber = 27, age = 29, overall = 80, pace = 70, shooting = 68, passing = 73,
            dribbling = 65, defending = 81, physical = 84, stamina = 82, marketValue = 16_000_000L,
            weeklyWage = 80_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Deon Hotto", position = PlayerPosition.LB,
            jerseyNumber = 7, age = 33, nationality = "Namibia", overall = 80, pace = 83, shooting = 75, passing = 80,
            dribbling = 78, defending = 76, physical = 78, stamina = 88, marketValue = 12_000_000L,
            weeklyWage = 85_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Makhehlene Makhaula", position = PlayerPosition.CDM,
            jerseyNumber = 16, age = 34, overall = 79, pace = 68, shooting = 60, passing = 76,
            dribbling = 72, defending = 82, physical = 85, stamina = 84, marketValue = 9_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Thalente Mbatha", position = PlayerPosition.CM,
            jerseyNumber = 28, age = 24, overall = 81, pace = 80, shooting = 78, passing = 82,
            dribbling = 82, defending = 78, physical = 81, stamina = 88, marketValue = 22_000_000L,
            weeklyWage = 85_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Patrick Maswanganyi", position = PlayerPosition.CAM,
            jerseyNumber = 28, age = 26, overall = 84, pace = 84, shooting = 82, passing = 86,
            dribbling = 88, defending = 62, physical = 74, stamina = 86, marketValue = 34_000_000L,
            weeklyWage = 115_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Monnapule Saleng", position = PlayerPosition.RW,
            jerseyNumber = 14, age = 26, overall = 83, pace = 89, shooting = 83, passing = 78,
            dribbling = 86, defending = 58, physical = 75, stamina = 86, marketValue = 30_000_000L,
            weeklyWage = 110_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Tshegofatso Mabasa", position = PlayerPosition.ST,
            jerseyNumber = 19, age = 28, overall = 82, pace = 81, shooting = 85, passing = 72,
            dribbling = 77, defending = 50, physical = 84, stamina = 84, marketValue = 24_000_000L,
            weeklyWage = 95_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Relebohile Mofokeng", position = PlayerPosition.LW,
            jerseyNumber = 38, age = 19, overall = 83, pace = 91, shooting = 80, passing = 82,
            dribbling = 89, defending = 54, physical = 70, stamina = 86, marketValue = 36_000_000L,
            weeklyWage = 85_000L, contractYearsRemaining = 4, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Melusi Buthelezi", position = PlayerPosition.GK,
            jerseyNumber = 35, age = 26, overall = 76, pace = 60, shooting = 25, passing = 68,
            dribbling = 54, defending = 76, physical = 76, stamina = 80, marketValue = 8_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Innocent Maela", position = PlayerPosition.CB,
            jerseyNumber = 23, age = 32, overall = 77, pace = 72, shooting = 52, passing = 72,
            dribbling = 68, defending = 78, physical = 78, stamina = 78, marketValue = 7_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Kabelo Dlamini", position = PlayerPosition.CAM,
            jerseyNumber = 18, age = 28, overall = 79, pace = 79, shooting = 80, passing = 81,
            dribbling = 82, defending = 56, physical = 72, stamina = 82, marketValue = 14_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Evidence Makgopa", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 24, overall = 79, pace = 82, shooting = 80, passing = 70,
            dribbling = 74, defending = 55, physical = 85, stamina = 84, marketValue = 16_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 3, isStartingEleven = false
        )
    )

    private fun generateChiefsSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Fiacre Ntwari", position = PlayerPosition.GK,
            jerseyNumber = 16, age = 25, nationality = "Rwanda", overall = 78, pace = 62, shooting = 26, passing = 70,
            dribbling = 55, defending = 79, physical = 76, stamina = 82, marketValue = 12_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Reeve Frosler", position = PlayerPosition.RB,
            jerseyNumber = 39, age = 26, overall = 78, pace = 82, shooting = 64, passing = 76,
            dribbling = 76, defending = 77, physical = 76, stamina = 85, marketValue = 13_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Inacio Miguel", position = PlayerPosition.CB,
            jerseyNumber = 84, age = 28, nationality = "Angola", overall = 80, pace = 74, shooting = 50, passing = 74,
            dribbling = 70, defending = 81, physical = 84, stamina = 84, marketValue = 18_000_000L,
            weeklyWage = 85_000L, contractYearsRemaining = 3, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Given Msimango", position = PlayerPosition.CB,
            jerseyNumber = 25, age = 27, overall = 78, pace = 72, shooting = 48, passing = 73,
            dribbling = 68, defending = 79, physical = 80, stamina = 82, marketValue = 14_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Bradley Cross", position = PlayerPosition.LB,
            jerseyNumber = 48, age = 23, overall = 77, pace = 81, shooting = 58, passing = 75,
            dribbling = 74, defending = 76, physical = 76, stamina = 84, marketValue = 11_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Edson Castillo", position = PlayerPosition.CDM,
            jerseyNumber = 17, age = 30, nationality = "Venezuela", overall = 79, pace = 73, shooting = 74, passing = 79,
            dribbling = 77, defending = 80, physical = 82, stamina = 86, marketValue = 15_000_000L,
            weeklyWage = 85_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Yusuf Maart", position = PlayerPosition.CM,
            jerseyNumber = 8, age = 29, overall = 79, pace = 76, shooting = 77, passing = 81,
            dribbling = 78, defending = 75, physical = 78, stamina = 86, marketValue = 15_000_000L,
            weeklyWage = 80_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Mduduzi Shabalala", position = PlayerPosition.CAM,
            jerseyNumber = 42, age = 20, overall = 80, pace = 86, shooting = 78, passing = 80,
            dribbling = 84, defending = 55, physical = 68, stamina = 84, marketValue = 22_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 4, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Ashley Du Preez", position = PlayerPosition.RW,
            jerseyNumber = 9, age = 27, overall = 79, pace = 90, shooting = 77, passing = 73,
            dribbling = 80, defending = 52, physical = 76, stamina = 84, marketValue = 16_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Ranga Chivaviro", position = PlayerPosition.ST,
            jerseyNumber = 7, age = 31, overall = 78, pace = 77, shooting = 81, passing = 68,
            dribbling = 73, defending = 48, physical = 84, stamina = 80, marketValue = 12_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Gaston Sirino", position = PlayerPosition.LW,
            jerseyNumber = 10, age = 33, nationality = "Uruguay", overall = 81, pace = 82, shooting = 82, passing = 84,
            dribbling = 85, defending = 54, physical = 70, stamina = 80, marketValue = 18_000_000L,
            weeklyWage = 110_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Brandon Petersen", position = PlayerPosition.GK,
            jerseyNumber = 1, age = 30, overall = 76, pace = 58, shooting = 25, passing = 68,
            dribbling = 52, defending = 76, physical = 76, stamina = 80, marketValue = 8_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Zitha Kwinika", position = PlayerPosition.CB,
            jerseyNumber = 4, age = 30, overall = 76, pace = 72, shooting = 46, passing = 70,
            dribbling = 66, defending = 77, physical = 78, stamina = 80, marketValue = 8_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Sibongiseni Mthethwa", position = PlayerPosition.CDM,
            jerseyNumber = 5, age = 30, overall = 77, pace = 74, shooting = 62, passing = 75,
            dribbling = 73, defending = 78, physical = 82, stamina = 84, marketValue = 10_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Wandile Duba", position = PlayerPosition.ST,
            jerseyNumber = 36, age = 20, overall = 76, pace = 86, shooting = 76, passing = 69,
            dribbling = 78, defending = 48, physical = 72, stamina = 82, marketValue = 10_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 3, isStartingEleven = false
        )
    )

    private fun generateStellenboschSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Sage Stephens", position = PlayerPosition.GK,
            jerseyNumber = 17, age = 33, overall = 79, pace = 60, shooting = 26, passing = 72,
            dribbling = 55, defending = 80, physical = 78, stamina = 82, marketValue = 12_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 2, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Kyle Jurgens", position = PlayerPosition.RB,
            jerseyNumber = 26, age = 21, overall = 77, pace = 82, shooting = 58, passing = 74,
            dribbling = 74, defending = 77, physical = 76, stamina = 85, marketValue = 11_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Ismael Toure", position = PlayerPosition.CB,
            jerseyNumber = 45, age = 26, nationality = "Ivory Coast", overall = 81, pace = 76, shooting = 50, passing = 72,
            dribbling = 68, defending = 82, physical = 86, stamina = 84, marketValue = 20_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Athenkosi Mcaba", position = PlayerPosition.CB,
            jerseyNumber = 27, age = 22, overall = 79, pace = 74, shooting = 46, passing = 74,
            dribbling = 70, defending = 80, physical = 78, stamina = 84, marketValue = 16_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Fawaaz Basadien", position = PlayerPosition.LB,
            jerseyNumber = 21, age = 27, overall = 81, pace = 84, shooting = 74, passing = 82,
            dribbling = 79, defending = 78, physical = 78, stamina = 87, marketValue = 22_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Sihle Nduli", position = PlayerPosition.CDM,
            jerseyNumber = 8, age = 28, overall = 78, pace = 75, shooting = 64, passing = 77,
            dribbling = 75, defending = 80, physical = 82, stamina = 86, marketValue = 12_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Jayden Adams", position = PlayerPosition.CM,
            jerseyNumber = 23, age = 23, overall = 82, pace = 81, shooting = 78, passing = 84,
            dribbling = 83, defending = 77, physical = 80, stamina = 89, marketValue = 26_000_000L,
            weeklyWage = 80_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Andre de Jong", position = PlayerPosition.CAM,
            jerseyNumber = 18, age = 27, nationality = "New Zealand", overall = 79, pace = 78, shooting = 80, passing = 80,
            dribbling = 79, defending = 60, physical = 78, stamina = 84, marketValue = 14_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Devin Titus", position = PlayerPosition.RW,
            jerseyNumber = 34, age = 23, overall = 80, pace = 89, shooting = 78, passing = 74,
            dribbling = 82, defending = 58, physical = 76, stamina = 88, marketValue = 18_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Lehlohonolo Mojela", position = PlayerPosition.ST,
            jerseyNumber = 11, age = 28, overall = 79, pace = 86, shooting = 81, passing = 71,
            dribbling = 78, defending = 50, physical = 80, stamina = 85, marketValue = 15_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Chumani Butsaka", position = PlayerPosition.LW,
            jerseyNumber = 7, age = 23, overall = 78, pace = 86, shooting = 75, passing = 76,
            dribbling = 81, defending = 52, physical = 72, stamina = 83, marketValue = 14_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Oscarine Masuluke", position = PlayerPosition.GK,
            jerseyNumber = 30, age = 31, overall = 75, pace = 55, shooting = 35, passing = 65,
            dribbling = 50, defending = 75, physical = 78, stamina = 78, marketValue = 6_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Omega Mdaka", position = PlayerPosition.LB,
            jerseyNumber = 3, age = 20, overall = 74, pace = 82, shooting = 55, passing = 71,
            dribbling = 72, defending = 74, physical = 72, stamina = 80, marketValue = 6_000_000L,
            weeklyWage = 35_000L, contractYearsRemaining = 3, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Genino Palace", position = PlayerPosition.CM,
            jerseyNumber = 6, age = 25, overall = 76, pace = 76, shooting = 71, passing = 77,
            dribbling = 76, defending = 74, physical = 76, stamina = 82, marketValue = 9_000_000L,
            weeklyWage = 48_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Ashley Cupido", position = PlayerPosition.ST,
            jerseyNumber = 10, age = 23, overall = 77, pace = 85, shooting = 79, passing = 69,
            dribbling = 76, defending = 48, physical = 78, stamina = 83, marketValue = 12_000_000L,
            weeklyWage = 52_000L, contractYearsRemaining = 3, isStartingEleven = false
        )
    )

    private fun generateSuperSportSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Ricardo Goss", position = PlayerPosition.GK,
            jerseyNumber = 20, age = 30, overall = 79, pace = 62, shooting = 26, passing = 72,
            dribbling = 55, defending = 80, physical = 77, stamina = 82, marketValue = 13_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Nyiko Mobbie", position = PlayerPosition.RB,
            jerseyNumber = 2, age = 30, overall = 78, pace = 82, shooting = 58, passing = 74,
            dribbling = 74, defending = 78, physical = 79, stamina = 85, marketValue = 12_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Thulani Hlatshwayo", position = PlayerPosition.CB,
            jerseyNumber = 3, age = 34, overall = 79, pace = 68, shooting = 52, passing = 71,
            dribbling = 64, defending = 81, physical = 84, stamina = 80, marketValue = 8_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 1, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Neo Rapoo", position = PlayerPosition.CB,
            jerseyNumber = 25, age = 21, overall = 76, pace = 76, shooting = 45, passing = 72,
            dribbling = 68, defending = 77, physical = 76, stamina = 82, marketValue = 9_000_000L,
            weeklyWage = 40_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Lyle Lakay", position = PlayerPosition.LB,
            jerseyNumber = 14, age = 33, overall = 79, pace = 80, shooting = 78, passing = 83,
            dribbling = 77, defending = 75, physical = 76, stamina = 82, marketValue = 9_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Grant Margeman", position = PlayerPosition.CM,
            jerseyNumber = 23, age = 26, overall = 79, pace = 77, shooting = 75, passing = 81,
            dribbling = 80, defending = 76, physical = 77, stamina = 86, marketValue = 15_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Siphesihle Ndlovu", position = PlayerPosition.CM,
            jerseyNumber = 8, age = 27, overall = 78, pace = 79, shooting = 73, passing = 79,
            dribbling = 79, defending = 77, physical = 78, stamina = 88, marketValue = 13_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Vincent Pule", position = PlayerPosition.RW,
            jerseyNumber = 11, age = 32, overall = 78, pace = 82, shooting = 78, passing = 79,
            dribbling = 81, defending = 56, physical = 74, stamina = 80, marketValue = 10_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Gape Moralo", position = PlayerPosition.CAM,
            jerseyNumber = 28, age = 24, overall = 76, pace = 78, shooting = 74, passing = 78,
            dribbling = 78, defending = 62, physical = 72, stamina = 82, marketValue = 8_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Terrence Dzvukamanja", position = PlayerPosition.ST,
            jerseyNumber = 10, age = 30, nationality = "Zimbabwe", overall = 79, pace = 82, shooting = 80, passing = 74,
            dribbling = 78, defending = 54, physical = 80, stamina = 82, marketValue = 13_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Bradley Grobler", position = PlayerPosition.ST,
            jerseyNumber = 7, age = 36, overall = 79, pace = 74, shooting = 84, passing = 72,
            dribbling = 74, defending = 48, physical = 82, stamina = 76, marketValue = 7_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Washington Arubi", position = PlayerPosition.GK,
            jerseyNumber = 1, age = 38, nationality = "Zimbabwe", overall = 74, pace = 52, shooting = 24, passing = 65,
            dribbling = 50, defending = 75, physical = 76, stamina = 75, marketValue = 4_000_000L,
            weeklyWage = 40_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Pogiso Sanoka", position = PlayerPosition.CB,
            jerseyNumber = 4, age = 32, overall = 76, pace = 72, shooting = 48, passing = 70,
            dribbling = 66, defending = 77, physical = 80, stamina = 78, marketValue = 6_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Keanin Ayer", position = PlayerPosition.CM,
            jerseyNumber = 16, age = 24, nationality = "Ghana", overall = 75, pace = 76, shooting = 70, passing = 76,
            dribbling = 76, defending = 72, physical = 75, stamina = 80, marketValue = 7_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Donay Jansen", position = PlayerPosition.LW,
            jerseyNumber = 35, age = 21, overall = 75, pace = 85, shooting = 74, passing = 72,
            dribbling = 77, defending = 50, physical = 70, stamina = 80, marketValue = 8_000_000L,
            weeklyWage = 35_000L, contractYearsRemaining = 3, isStartingEleven = false
        )
    )

    private fun generateAmaZuluSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Veli Mothwa", position = PlayerPosition.GK,
            jerseyNumber = 44, age = 33, overall = 78, pace = 62, shooting = 26, passing = 70,
            dribbling = 56, defending = 79, physical = 78, stamina = 82, marketValue = 10_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 2, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Wayde Jooste", position = PlayerPosition.RB,
            jerseyNumber = 26, age = 33, overall = 76, pace = 80, shooting = 62, passing = 73,
            dribbling = 74, defending = 75, physical = 75, stamina = 82, marketValue = 7_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Taariq Fielies", position = PlayerPosition.CB,
            jerseyNumber = 5, age = 32, overall = 77, pace = 70, shooting = 50, passing = 72,
            dribbling = 66, defending = 78, physical = 82, stamina = 80, marketValue = 8_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Abbubaker Mobara", position = PlayerPosition.CB,
            jerseyNumber = 2, age = 30, overall = 77, pace = 74, shooting = 54, passing = 73,
            dribbling = 70, defending = 78, physical = 79, stamina = 82, marketValue = 8_500_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Riaan Hanamub", position = PlayerPosition.LB,
            jerseyNumber = 14, age = 29, nationality = "Namibia", overall = 78, pace = 83, shooting = 64, passing = 78,
            dribbling = 76, defending = 77, physical = 78, stamina = 86, marketValue = 12_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Ethan Brooks", position = PlayerPosition.CM,
            jerseyNumber = 18, age = 23, overall = 77, pace = 78, shooting = 72, passing = 78,
            dribbling = 78, defending = 75, physical = 76, stamina = 85, marketValue = 12_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Ben Motshwari", position = PlayerPosition.CDM,
            jerseyNumber = 22, age = 33, overall = 77, pace = 72, shooting = 62, passing = 76,
            dribbling = 74, defending = 79, physical = 80, stamina = 82, marketValue = 7_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Hendrick Ekstein", position = PlayerPosition.CAM,
            jerseyNumber = 27, age = 33, overall = 77, pace = 77, shooting = 75, passing = 80,
            dribbling = 82, defending = 54, physical = 70, stamina = 78, marketValue = 7_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Augustine Mulenga", position = PlayerPosition.RW,
            jerseyNumber = 17, age = 34, nationality = "Zambia", overall = 77, pace = 81, shooting = 77, passing = 77,
            dribbling = 80, defending = 50, physical = 74, stamina = 78, marketValue = 7_500_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Junior Dion", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 28, nationality = "Ivory Coast", overall = 77, pace = 80, shooting = 79, passing = 68,
            dribbling = 74, defending = 48, physical = 82, stamina = 82, marketValue = 10_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Tshepang Moremi", position = PlayerPosition.LW,
            jerseyNumber = 19, age = 24, overall = 76, pace = 85, shooting = 74, passing = 74,
            dribbling = 79, defending = 52, physical = 72, stamina = 82, marketValue = 9_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Richard Ofori", position = PlayerPosition.GK,
            jerseyNumber = 31, age = 30, nationality = "Ghana", overall = 76, pace = 58, shooting = 24, passing = 68,
            dribbling = 54, defending = 76, physical = 78, stamina = 80, marketValue = 8_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Thembela Sikhakhane", position = PlayerPosition.CB,
            jerseyNumber = 24, age = 31, overall = 75, pace = 73, shooting = 48, passing = 70,
            dribbling = 66, defending = 76, physical = 78, stamina = 78, marketValue = 5_500_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Rally Bwalya", position = PlayerPosition.CM,
            jerseyNumber = 10, age = 29, nationality = "Zambia", overall = 75, pace = 76, shooting = 72, passing = 77,
            dribbling = 77, defending = 62, physical = 72, stamina = 80, marketValue = 7_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Bongi Ntuli", position = PlayerPosition.ST,
            jerseyNumber = 7, age = 26, overall = 75, pace = 81, shooting = 76, passing = 67,
            dribbling = 74, defending = 48, physical = 80, stamina = 80, marketValue = 7_500_000L,
            weeklyWage = 48_000L, contractYearsRemaining = 2, isStartingEleven = false
        )
    )

    private fun generateSekhukhuneSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Badra Ali Sangare", position = PlayerPosition.GK,
            jerseyNumber = 1, age = 38, nationality = "Ivory Coast", overall = 78, pace = 58, shooting = 25, passing = 70,
            dribbling = 54, defending = 79, physical = 77, stamina = 78, marketValue = 6_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Njabulo Ngcobo", position = PlayerPosition.CB,
            jerseyNumber = 27, age = 30, overall = 78, pace = 73, shooting = 48, passing = 73,
            dribbling = 68, defending = 79, physical = 81, stamina = 82, marketValue = 10_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Daniel Cardoso", position = PlayerPosition.CB,
            jerseyNumber = 4, age = 35, overall = 77, pace = 66, shooting = 62, passing = 72,
            dribbling = 64, defending = 79, physical = 82, stamina = 78, marketValue = 5_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 1, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Vuyo Letlapa", position = PlayerPosition.RB,
            jerseyNumber = 2, age = 25, overall = 76, pace = 81, shooting = 56, passing = 72,
            dribbling = 73, defending = 76, physical = 76, stamina = 84, marketValue = 8_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Sikhosana Katlego", position = PlayerPosition.LB,
            jerseyNumber = 3, age = 26, overall = 76, pace = 82, shooting = 58, passing = 73,
            dribbling = 74, defending = 76, physical = 77, stamina = 84, marketValue = 8_500_000L,
            weeklyWage = 48_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Linda Mntambo", position = PlayerPosition.CM,
            jerseyNumber = 23, age = 35, overall = 78, pace = 74, shooting = 77, passing = 80,
            dribbling = 79, defending = 74, physical = 78, stamina = 80, marketValue = 7_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Jamie Webber", position = PlayerPosition.CM,
            jerseyNumber = 12, age = 26, overall = 78, pace = 78, shooting = 78, passing = 80,
            dribbling = 79, defending = 73, physical = 76, stamina = 86, marketValue = 13_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Sipho Mbule", position = PlayerPosition.CAM,
            jerseyNumber = 21, age = 26, overall = 80, pace = 79, shooting = 80, passing = 83,
            dribbling = 84, defending = 66, physical = 78, stamina = 82, marketValue = 20_000_000L,
            weeklyWage = 85_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Shaune Mogaila", position = PlayerPosition.RW,
            jerseyNumber = 14, age = 29, overall = 77, pace = 88, shooting = 74, passing = 74,
            dribbling = 81, defending = 56, physical = 74, stamina = 85, marketValue = 10_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Chibuike Ohizu", position = PlayerPosition.ST,
            jerseyNumber = 34, age = 28, nationality = "Nigeria", overall = 78, pace = 82, shooting = 80, passing = 69,
            dribbling = 75, defending = 50, physical = 84, stamina = 83, marketValue = 12_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Andy Boyeli", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 23, nationality = "DR Congo", overall = 77, pace = 85, shooting = 79, passing = 68,
            dribbling = 77, defending = 48, physical = 81, stamina = 84, marketValue = 11_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Loydt Kazapua", position = PlayerPosition.GK,
            jerseyNumber = 16, age = 35, nationality = "Namibia", overall = 74, pace = 55, shooting = 25, passing = 66,
            dribbling = 50, defending = 75, physical = 76, stamina = 78, marketValue = 4_000_000L,
            weeklyWage = 40_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Tresor Yamba", position = PlayerPosition.CB,
            jerseyNumber = 15, age = 22, nationality = "DR Congo", overall = 75, pace = 74, shooting = 46, passing = 69,
            dribbling = 65, defending = 76, physical = 82, stamina = 80, marketValue = 7_000_000L,
            weeklyWage = 40_000L, contractYearsRemaining = 3, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Kamohelo Mokotjo", position = PlayerPosition.CDM,
            jerseyNumber = 6, age = 33, overall = 76, pace = 70, shooting = 66, passing = 78,
            dribbling = 75, defending = 77, physical = 76, stamina = 80, marketValue = 5_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Tshepo Mokoena", position = PlayerPosition.LW,
            jerseyNumber = 17, age = 24, overall = 75, pace = 84, shooting = 72, passing = 72,
            dribbling = 76, defending = 52, physical = 70, stamina = 80, marketValue = 6_500_000L,
            weeklyWage = 38_000L, contractYearsRemaining = 2, isStartingEleven = false
        )
    )

    private fun generateTSGalaxySquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Ira Tape", position = PlayerPosition.GK,
            jerseyNumber = 1, age = 27, nationality = "Ivory Coast", overall = 76, pace = 60, shooting = 25, passing = 68,
            dribbling = 54, defending = 77, physical = 76, stamina = 82, marketValue = 8_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Marks Munyai", position = PlayerPosition.RB,
            jerseyNumber = 23, age = 32, overall = 76, pace = 79, shooting = 58, passing = 72,
            dribbling = 73, defending = 76, physical = 76, stamina = 82, marketValue = 6_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 1, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "McBeth Mahlangu", position = PlayerPosition.CB,
            jerseyNumber = 4, age = 23, overall = 77, pace = 75, shooting = 46, passing = 71,
            dribbling = 68, defending = 78, physical = 82, stamina = 84, marketValue = 10_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Samukelo Kabini", position = PlayerPosition.LB,
            jerseyNumber = 3, age = 20, overall = 76, pace = 84, shooting = 60, passing = 74,
            dribbling = 76, defending = 75, physical = 75, stamina = 86, marketValue = 9_000_000L,
            weeklyWage = 35_000L, contractYearsRemaining = 4, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Khulumani Ndamane", position = PlayerPosition.CB,
            jerseyNumber = 5, age = 24, overall = 75, pace = 73, shooting = 45, passing = 69,
            dribbling = 66, defending = 76, physical = 80, stamina = 82, marketValue = 7_000_000L,
            weeklyWage = 40_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Mpho Mvelase", position = PlayerPosition.CDM,
            jerseyNumber = 12, age = 31, overall = 76, pace = 74, shooting = 65, passing = 76,
            dribbling = 75, defending = 77, physical = 79, stamina = 84, marketValue = 7_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Sphiwe Mahlangu", position = PlayerPosition.CM,
            jerseyNumber = 8, age = 27, overall = 76, pace = 78, shooting = 74, passing = 77,
            dribbling = 78, defending = 70, physical = 74, stamina = 84, marketValue = 8_500_000L,
            weeklyWage = 48_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Puso Dithejane", position = PlayerPosition.RW,
            jerseyNumber = 17, age = 20, overall = 77, pace = 88, shooting = 75, passing = 75,
            dribbling = 82, defending = 58, physical = 72, stamina = 85, marketValue = 12_000_000L,
            weeklyWage = 40_000L, contractYearsRemaining = 4, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Thato Khiba", position = PlayerPosition.CAM,
            jerseyNumber = 20, age = 27, overall = 76, pace = 78, shooting = 73, passing = 77,
            dribbling = 78, defending = 68, physical = 75, stamina = 84, marketValue = 8_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Samir Nurkovic", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 32, nationality = "Serbia", overall = 78, pace = 74, shooting = 83, passing = 70,
            dribbling = 74, defending = 50, physical = 86, stamina = 78, marketValue = 10_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Dzenan Zajmovic", position = PlayerPosition.ST,
            jerseyNumber = 27, age = 29, nationality = "Bosnia", overall = 76, pace = 78, shooting = 78, passing = 69,
            dribbling = 75, defending = 48, physical = 82, stamina = 80, marketValue = 8_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Wensten van der Linde", position = PlayerPosition.GK,
            jerseyNumber = 32, age = 30, overall = 73, pace = 56, shooting = 22, passing = 64,
            dribbling = 50, defending = 74, physical = 74, stamina = 78, marketValue = 4_000_000L,
            weeklyWage = 35_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Vuyo Mere", position = PlayerPosition.RB,
            jerseyNumber = 2, age = 40, overall = 72, pace = 65, shooting = 50, passing = 72,
            dribbling = 68, defending = 74, physical = 72, stamina = 70, marketValue = 1_500_000L,
            weeklyWage = 35_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Lindokuhle Mbatha", position = PlayerPosition.RW,
            jerseyNumber = 35, age = 38, overall = 74, pace = 78, shooting = 72, passing = 74,
            dribbling = 78, defending = 54, physical = 70, stamina = 74, marketValue = 3_000_000L,
            weeklyWage = 40_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Victor Letsoalo", position = PlayerPosition.ST,
            jerseyNumber = 11, age = 31, overall = 75, pace = 80, shooting = 77, passing = 68,
            dribbling = 74, defending = 48, physical = 78, stamina = 80, marketValue = 6_500_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 1, isStartingEleven = false
        )
    )

    private fun generateCTCitySquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Darren Keet", position = PlayerPosition.GK,
            jerseyNumber = 16, age = 35, overall = 78, pace = 60, shooting = 26, passing = 73,
            dribbling = 54, defending = 79, physical = 78, stamina = 80, marketValue = 8_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 1, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Keanu Cupido", position = PlayerPosition.CB,
            jerseyNumber = 15, age = 26, overall = 77, pace = 75, shooting = 48, passing = 73,
            dribbling = 69, defending = 78, physical = 80, stamina = 84, marketValue = 10_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Lorenzo Gordinho", position = PlayerPosition.CB,
            jerseyNumber = 25, age = 30, overall = 76, pace = 70, shooting = 52, passing = 71,
            dribbling = 66, defending = 77, physical = 82, stamina = 80, marketValue = 7_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Thamsanqa Mkhize", position = PlayerPosition.RB,
            jerseyNumber = 2, age = 36, overall = 76, pace = 76, shooting = 56, passing = 72,
            dribbling = 72, defending = 76, physical = 76, stamina = 78, marketValue = 4_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Nathan Fasika", position = PlayerPosition.CB,
            jerseyNumber = 4, age = 25, nationality = "DR Congo", overall = 77, pace = 74, shooting = 46, passing = 70,
            dribbling = 66, defending = 78, physical = 84, stamina = 82, marketValue = 10_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Taahir Goedeman", position = PlayerPosition.CM,
            jerseyNumber = 27, age = 25, overall = 77, pace = 79, shooting = 74, passing = 79,
            dribbling = 80, defending = 73, physical = 75, stamina = 85, marketValue = 11_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Fortune Makaringe", position = PlayerPosition.CAM,
            jerseyNumber = 11, age = 31, overall = 77, pace = 78, shooting = 75, passing = 80,
            dribbling = 82, defending = 64, physical = 74, stamina = 82, marketValue = 9_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Jaedin Rhodes", position = PlayerPosition.RW,
            jerseyNumber = 33, age = 21, overall = 78, pace = 88, shooting = 76, passing = 77,
            dribbling = 83, defending = 54, physical = 72, stamina = 86, marketValue = 15_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 4, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Haashim Domingo", position = PlayerPosition.CM,
            jerseyNumber = 10, age = 29, overall = 77, pace = 77, shooting = 74, passing = 80,
            dribbling = 80, defending = 70, physical = 74, stamina = 82, marketValue = 10_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Amadou Soukouna", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 32, nationality = "France", overall = 77, pace = 80, shooting = 80, passing = 72,
            dribbling = 76, defending = 48, physical = 80, stamina = 80, marketValue = 8_500_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Prins Tjiueza", position = PlayerPosition.LW,
            jerseyNumber = 7, age = 22, nationality = "Namibia", overall = 77, pace = 87, shooting = 76, passing = 75,
            dribbling = 81, defending = 52, physical = 72, stamina = 84, marketValue = 12_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Bongani Mpandle", position = PlayerPosition.GK,
            jerseyNumber = 1, age = 32, overall = 73, pace = 54, shooting = 22, passing = 64,
            dribbling = 50, defending = 74, physical = 75, stamina = 78, marketValue = 3_500_000L,
            weeklyWage = 35_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Patrick Fisher", position = PlayerPosition.RB,
            jerseyNumber = 22, age = 23, overall = 74, pace = 82, shooting = 54, passing = 70,
            dribbling = 72, defending = 74, physical = 74, stamina = 82, marketValue = 5_000_000L,
            weeklyWage = 32_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Luphumlo Sifumba", position = PlayerPosition.CM,
            jerseyNumber = 24, age = 19, overall = 74, pace = 80, shooting = 70, passing = 74,
            dribbling = 76, defending = 68, physical = 70, stamina = 82, marketValue = 7_000_000L,
            weeklyWage = 28_000L, contractYearsRemaining = 4, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Darwin Gonzalez", position = PlayerPosition.LW,
            jerseyNumber = 20, age = 30, nationality = "Venezuela", overall = 76, pace = 84, shooting = 75, passing = 74,
            dribbling = 79, defending = 52, physical = 72, stamina = 80, marketValue = 7_500_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 2, isStartingEleven = false
        )
    )

    private fun generateGenericSquad(team: TeamEntity): List<PlayerEntity> {
        val baseOvr = team.ratingOverall
        val namesByPos = listOf(
            Triple(PlayerPosition.GK, 1, listOf("Bafana Mokoena", "Themba Khumalo")),
            Triple(PlayerPosition.RB, 2, listOf("Sipho Sithole", "Bongani Dlamini")),
            Triple(PlayerPosition.CB, 3, listOf("Mandla Nkosi", "Vusumuzi Zulu")),
            Triple(PlayerPosition.CB, 4, listOf("Nhlanhla Ndlovu", "Sifiso Gumede")),
            Triple(PlayerPosition.LB, 5, listOf("Mthokozisi Cele", "Thabo Mbatha")),
            Triple(PlayerPosition.CDM, 6, listOf("Nkululeko Mthembu", "Jabulani Shandu")),
            Triple(PlayerPosition.CM, 8, listOf("Kagiso Maluleke", "Bonginkosi Khoza")),
            Triple(PlayerPosition.CAM, 10, listOf("Sibusiso Radebe", "Lungelo Maseko")),
            Triple(PlayerPosition.RW, 7, listOf("Katlego Modise", "Ayanda Mabaso")),
            Triple(PlayerPosition.ST, 9, listOf("Tshepo Tau", "Dumisani Hadebe")),
            Triple(PlayerPosition.LW, 11, listOf("Thabang Mabena", "Mlungisi Zungu")),
            // 4 substitutes
            Triple(PlayerPosition.GK, 16, listOf("Lebohang Mashego", "Sfiso Mngomezulu")),
            Triple(PlayerPosition.CB, 14, listOf("Thokozani Vilakazi", "Gcina Buthelezi")),
            Triple(PlayerPosition.CM, 15, listOf("Nhlakanipho Ntuli", "Phumlani Mthembu")),
            Triple(PlayerPosition.ST, 17, listOf("Sphiwe Mokoena", "Luyanda Gwala"))
        )

        return namesByPos.mapIndexed { index, (pos, shirtNo, names) ->
            val isStarter = index < 11
            val ratingVariance = if (isStarter) (-2..2).random() else (-4..-1).random()
            val ovr = (baseOvr + ratingVariance).coerceIn(60, 92)
            val name = names[index % names.size]

            PlayerEntity(
                id = "${team.id}_p_${index + 1}",
                teamId = team.id,
                name = name,
                position = pos,
                jerseyNumber = shirtNo,
                age = (19..34).random(),
                overall = ovr,
                pace = (ovr + (-5..5).random()).coerceIn(55, 95),
                shooting = if (pos == PlayerPosition.ST || pos == PlayerPosition.LW || pos == PlayerPosition.RW || pos == PlayerPosition.CAM)
                    (ovr + (-3..4).random()).coerceIn(60, 92) else (ovr - 20).coerceAtLeast(30),
                passing = (ovr + (-4..3).random()).coerceIn(50, 90),
                dribbling = (ovr + (-4..4).random()).coerceIn(50, 92),
                defending = if (pos == PlayerPosition.CB || pos == PlayerPosition.RB || pos == PlayerPosition.LB || pos == PlayerPosition.CDM)
                    (ovr + (-2..4).random()).coerceIn(65, 90) else (ovr - 22).coerceAtLeast(35),
                physical = (ovr + (-3..4).random()).coerceIn(60, 92),
                stamina = (75..92).random(),
                marketValue = (ovr * 150_000L).coerceAtLeast(1_500_000L),
                weeklyWage = (ovr * 600L).coerceAtLeast(15_000L),
                contractYearsRemaining = (1..3).random(),
                isCaptain = index == 2,
                isStartingEleven = isStarter
            )
        }
    }
}
