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
                "magesi_fc" -> playerList.addAll(generateMagesiSquad(team.id))
                "polokwane_city" -> playerList.addAll(generatePolokwaneCitySquad(team.id))
                "chippa_united" -> playerList.addAll(generateChippaSquad(team.id))
                else -> playerList.addAll(generateGenericSquad(team))
            }
        }

        return playerList
    }

    private fun generateSundownsSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Ronwen Williams", position = PlayerPosition.GK,
            jerseyNumber = 30, age = 34, overall = 86, pace = 65, shooting = 30, passing = 80,
            dribbling = 62, defending = 86, physical = 76, stamina = 86, marketValue = 32_000_000L,
            weeklyWage = 140_000L, contractYearsRemaining = 3, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Khuliso Mudau", position = PlayerPosition.RB,
            jerseyNumber = 25, age = 31, overall = 83, pace = 86, shooting = 64, passing = 77,
            dribbling = 81, defending = 82, physical = 83, stamina = 88, marketValue = 24_000_000L,
            weeklyWage = 105_000L, contractYearsRemaining = 2, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Grant Kekana", position = PlayerPosition.CB,
            jerseyNumber = 20, age = 33, overall = 80, pace = 70, shooting = 45, passing = 73,
            dribbling = 67, defending = 82, physical = 83, stamina = 80, marketValue = 14_000_000L,
            weeklyWage = 90_000L, contractYearsRemaining = 2, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Mothobi Mvala", position = PlayerPosition.CB,
            jerseyNumber = 34, age = 32, overall = 80, pace = 69, shooting = 50, passing = 71,
            dribbling = 65, defending = 83, physical = 86, stamina = 82, marketValue = 13_000_000L,
            weeklyWage = 85_000L, contractYearsRemaining = 2, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Aubrey Modiba", position = PlayerPosition.LB,
            jerseyNumber = 17, age = 31, overall = 82, pace = 82, shooting = 75, passing = 83,
            dribbling = 80, defending = 78, physical = 76, stamina = 85, marketValue = 20_000_000L,
            weeklyWage = 95_000L, contractYearsRemaining = 2, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Teboho Mokoena", position = PlayerPosition.CDM,
            jerseyNumber = 4, age = 29, overall = 86, pace = 79, shooting = 86, passing = 87,
            dribbling = 82, defending = 84, physical = 85, stamina = 92, marketValue = 42_000_000L,
            weeklyWage = 145_000L, contractYearsRemaining = 3, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Marcelo Allende", position = PlayerPosition.CM,
            jerseyNumber = 11, age = 27, nationality = "Chile", overall = 85, pace = 80, shooting = 78, passing = 88,
            dribbling = 86, defending = 73, physical = 75, stamina = 88, marketValue = 35_000_000L,
            weeklyWage = 125_000L, contractYearsRemaining = 3, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Lucas Ribeiro Costa", position = PlayerPosition.RW,
            jerseyNumber = 10, age = 28, nationality = "Brazil", overall = 86, pace = 87, shooting = 85, passing = 84,
            dribbling = 89, defending = 58, physical = 77, stamina = 86, marketValue = 44_000_000L,
            weeklyWage = 140_000L, contractYearsRemaining = 3, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Themba Zwane", position = PlayerPosition.CAM,
            jerseyNumber = 18, age = 37, overall = 80, pace = 72, shooting = 79, passing = 87,
            dribbling = 86, defending = 54, physical = 68, stamina = 74, marketValue = 10_000_000L,
            weeklyWage = 135_000L, contractYearsRemaining = 1, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Iqraam Rayners", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 30, overall = 84, pace = 89, shooting = 85, passing = 77,
            dribbling = 83, defending = 55, physical = 80, stamina = 87, marketValue = 30_000_000L,
            weeklyWage = 110_000L, contractYearsRemaining = 3, isCaptain = false, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Tashreeq Matthews", position = PlayerPosition.LW,
            jerseyNumber = 12, age = 26, overall = 82, pace = 88, shooting = 80, passing = 79,
            dribbling = 84, defending = 53, physical = 73, stamina = 85, marketValue = 24_000_000L,
            weeklyWage = 85_000L, contractYearsRemaining = 3, isCaptain = false, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Denis Onyango", position = PlayerPosition.GK,
            jerseyNumber = 1, age = 41, nationality = "Uganda", overall = 76, pace = 48, shooting = 25, passing = 65,
            dribbling = 48, defending = 77, physical = 79, stamina = 68, marketValue = 3_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Kegan Johannes", position = PlayerPosition.CB,
            jerseyNumber = 2, age = 25, overall = 79, pace = 77, shooting = 52, passing = 74,
            dribbling = 71, defending = 80, physical = 81, stamina = 84, marketValue = 16_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 3, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Sphelele Mkhulise", position = PlayerPosition.CM,
            jerseyNumber = 21, age = 30, overall = 78, pace = 76, shooting = 72, passing = 78,
            dribbling = 79, defending = 71, physical = 73, stamina = 82, marketValue = 11_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Peter Shalulile", position = PlayerPosition.ST,
            jerseyNumber = 38, age = 33, nationality = "Namibia", overall = 83, pace = 86, shooting = 85, passing = 73,
            dribbling = 79, defending = 54, physical = 83, stamina = 86, marketValue = 22_000_000L,
            weeklyWage = 130_000L, contractYearsRemaining = 2, isStartingEleven = false
        )
    )

    private fun generatePiratesSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Sipho Chaine", position = PlayerPosition.GK,
            jerseyNumber = 24, age = 29, overall = 82, pace = 63, shooting = 28, passing = 78,
            dribbling = 59, defending = 82, physical = 78, stamina = 85, marketValue = 20_000_000L,
            weeklyWage = 85_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Thabiso Monyane", position = PlayerPosition.RB,
            jerseyNumber = 2, age = 26, overall = 79, pace = 85, shooting = 62, passing = 75,
            dribbling = 77, defending = 78, physical = 78, stamina = 86, marketValue = 16_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Olisa Ndah", position = PlayerPosition.CB,
            jerseyNumber = 49, age = 28, nationality = "Nigeria", overall = 83, pace = 77, shooting = 49, passing = 76,
            dribbling = 73, defending = 84, physical = 86, stamina = 85, marketValue = 28_000_000L,
            weeklyWage = 105_000L, contractYearsRemaining = 3, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Tapelo Xoki", position = PlayerPosition.CB,
            jerseyNumber = 27, age = 31, overall = 81, pace = 70, shooting = 69, passing = 74,
            dribbling = 66, defending = 82, physical = 84, stamina = 82, marketValue = 17_000_000L,
            weeklyWage = 85_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Deon Hotto", position = PlayerPosition.LB,
            jerseyNumber = 7, age = 35, nationality = "Namibia", overall = 79, pace = 81, shooting = 74, passing = 80,
            dribbling = 77, defending = 76, physical = 77, stamina = 86, marketValue = 9_000_000L,
            weeklyWage = 85_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Makhehlene Makhaula", position = PlayerPosition.CDM,
            jerseyNumber = 16, age = 36, overall = 78, pace = 66, shooting = 59, passing = 76,
            dribbling = 71, defending = 82, physical = 84, stamina = 80, marketValue = 6_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Thalente Mbatha", position = PlayerPosition.CM,
            jerseyNumber = 28, age = 26, overall = 83, pace = 81, shooting = 80, passing = 84,
            dribbling = 83, defending = 80, physical = 82, stamina = 90, marketValue = 28_000_000L,
            weeklyWage = 95_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Patrick Maswanganyi", position = PlayerPosition.CAM,
            jerseyNumber = 10, age = 28, overall = 85, pace = 85, shooting = 83, passing = 87,
            dribbling = 89, defending = 63, physical = 75, stamina = 87, marketValue = 38_000_000L,
            weeklyWage = 125_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Monnapule Saleng", position = PlayerPosition.RW,
            jerseyNumber = 14, age = 28, overall = 83, pace = 89, shooting = 83, passing = 79,
            dribbling = 86, defending = 58, physical = 76, stamina = 86, marketValue = 30_000_000L,
            weeklyWage = 110_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Tshegofatso Mabasa", position = PlayerPosition.ST,
            jerseyNumber = 19, age = 30, overall = 83, pace = 81, shooting = 86, passing = 73,
            dribbling = 78, defending = 50, physical = 85, stamina = 84, marketValue = 25_000_000L,
            weeklyWage = 100_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Relebohile Mofokeng", position = PlayerPosition.LW,
            jerseyNumber = 38, age = 21, overall = 86, pace = 93, shooting = 82, passing = 85,
            dribbling = 92, defending = 55, physical = 72, stamina = 88, marketValue = 48_000_000L,
            weeklyWage = 110_000L, contractYearsRemaining = 4, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Melusi Buthelezi", position = PlayerPosition.GK,
            jerseyNumber = 35, age = 28, overall = 77, pace = 61, shooting = 25, passing = 69,
            dribbling = 55, defending = 77, physical = 77, stamina = 81, marketValue = 9_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Mohau Nkota", position = PlayerPosition.RW,
            jerseyNumber = 34, age = 21, overall = 79, pace = 91, shooting = 76, passing = 76,
            dribbling = 85, defending = 52, physical = 72, stamina = 86, marketValue = 18_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 4, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Kabelo Dlamini", position = PlayerPosition.CAM,
            jerseyNumber = 18, age = 30, overall = 80, pace = 79, shooting = 81, passing = 82,
            dribbling = 82, defending = 56, physical = 72, stamina = 82, marketValue = 15_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Evidence Makgopa", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 26, overall = 81, pace = 83, shooting = 81, passing = 71,
            dribbling = 75, defending = 55, physical = 86, stamina = 85, marketValue = 20_000_000L,
            weeklyWage = 80_000L, contractYearsRemaining = 3, isStartingEleven = false
        )
    )

    private fun generateChiefsSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Fiacre Ntwari", position = PlayerPosition.GK,
            jerseyNumber = 16, age = 27, nationality = "Rwanda", overall = 81, pace = 63, shooting = 26, passing = 72,
            dribbling = 56, defending = 82, physical = 78, stamina = 84, marketValue = 16_000_000L,
            weeklyWage = 80_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Reeve Frosler", position = PlayerPosition.RB,
            jerseyNumber = 39, age = 28, overall = 79, pace = 83, shooting = 65, passing = 77,
            dribbling = 77, defending = 78, physical = 77, stamina = 86, marketValue = 15_000_000L,
            weeklyWage = 80_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Inacio Miguel", position = PlayerPosition.CB,
            jerseyNumber = 84, age = 30, nationality = "Angola", overall = 82, pace = 75, shooting = 52, passing = 76,
            dribbling = 72, defending = 83, physical = 85, stamina = 86, marketValue = 22_000_000L,
            weeklyWage = 95_000L, contractYearsRemaining = 3, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Rushwin Dortley", position = PlayerPosition.CB,
            jerseyNumber = 14, age = 24, overall = 81, pace = 78, shooting = 52, passing = 76,
            dribbling = 72, defending = 82, physical = 83, stamina = 86, marketValue = 22_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 4, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Bradley Cross", position = PlayerPosition.LB,
            jerseyNumber = 48, age = 25, overall = 79, pace = 82, shooting = 60, passing = 76,
            dribbling = 76, defending = 78, physical = 78, stamina = 86, marketValue = 15_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Edson Castillo", position = PlayerPosition.CDM,
            jerseyNumber = 17, age = 32, nationality = "Venezuela", overall = 79, pace = 72, shooting = 74, passing = 79,
            dribbling = 76, defending = 80, physical = 82, stamina = 84, marketValue = 13_000_000L,
            weeklyWage = 85_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Yusuf Maart", position = PlayerPosition.CM,
            jerseyNumber = 8, age = 31, overall = 79, pace = 75, shooting = 77, passing = 81,
            dribbling = 78, defending = 75, physical = 78, stamina = 85, marketValue = 14_000_000L,
            weeklyWage = 80_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Mduduzi Shabalala", position = PlayerPosition.CAM,
            jerseyNumber = 42, age = 22, overall = 83, pace = 87, shooting = 81, passing = 83,
            dribbling = 86, defending = 56, physical = 70, stamina = 86, marketValue = 26_000_000L,
            weeklyWage = 85_000L, contractYearsRemaining = 4, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Mfundo Vilakazi", position = PlayerPosition.RW,
            jerseyNumber = 28, age = 21, overall = 81, pace = 90, shooting = 78, passing = 81,
            dribbling = 92, defending = 52, physical = 68, stamina = 86, marketValue = 22_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 4, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Wandile Duba", position = PlayerPosition.ST,
            jerseyNumber = 36, age = 22, overall = 80, pace = 88, shooting = 81, passing = 72,
            dribbling = 80, defending = 49, physical = 76, stamina = 85, marketValue = 18_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 4, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Gaston Sirino", position = PlayerPosition.LW,
            jerseyNumber = 10, age = 35, nationality = "Uruguay", overall = 79, pace = 79, shooting = 80, passing = 83,
            dribbling = 83, defending = 52, physical = 68, stamina = 78, marketValue = 10_000_000L,
            weeklyWage = 100_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Brandon Petersen", position = PlayerPosition.GK,
            jerseyNumber = 1, age = 32, overall = 76, pace = 57, shooting = 25, passing = 68,
            dribbling = 52, defending = 76, physical = 76, stamina = 80, marketValue = 6_500_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Given Msimango", position = PlayerPosition.CB,
            jerseyNumber = 25, age = 29, overall = 78, pace = 72, shooting = 48, passing = 73,
            dribbling = 68, defending = 79, physical = 80, stamina = 82, marketValue = 12_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Samkelo Zwane", position = PlayerPosition.CM,
            jerseyNumber = 22, age = 24, overall = 78, pace = 76, shooting = 72, passing = 82,
            dribbling = 79, defending = 74, physical = 76, stamina = 84, marketValue = 14_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 3, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Ashley Du Preez", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 29, overall = 79, pace = 90, shooting = 77, passing = 73,
            dribbling = 80, defending = 52, physical = 76, stamina = 84, marketValue = 15_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 2, isStartingEleven = false
        )
    )

    private fun generateStellenboschSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Sage Stephens", position = PlayerPosition.GK,
            jerseyNumber = 17, age = 35, overall = 80, pace = 60, shooting = 26, passing = 73,
            dribbling = 55, defending = 81, physical = 78, stamina = 82, marketValue = 12_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 2, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Kyle Jurgens", position = PlayerPosition.RB,
            jerseyNumber = 26, age = 23, overall = 78, pace = 83, shooting = 59, passing = 75,
            dribbling = 75, defending = 78, physical = 77, stamina = 86, marketValue = 13_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Ismael Toure", position = PlayerPosition.CB,
            jerseyNumber = 45, age = 28, nationality = "Ivory Coast", overall = 81, pace = 76, shooting = 50, passing = 72,
            dribbling = 68, defending = 82, physical = 86, stamina = 84, marketValue = 20_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Athenkosi Mcaba", position = PlayerPosition.CB,
            jerseyNumber = 27, age = 24, overall = 81, pace = 75, shooting = 48, passing = 75,
            dribbling = 71, defending = 82, physical = 80, stamina = 85, marketValue = 20_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Fawaaz Basadien", position = PlayerPosition.LB,
            jerseyNumber = 21, age = 29, overall = 82, pace = 84, shooting = 75, passing = 83,
            dribbling = 80, defending = 79, physical = 78, stamina = 88, marketValue = 24_000_000L,
            weeklyWage = 80_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Sihle Nduli", position = PlayerPosition.CDM,
            jerseyNumber = 8, age = 30, overall = 78, pace = 74, shooting = 64, passing = 77,
            dribbling = 75, defending = 80, physical = 82, stamina = 85, marketValue = 12_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Jayden Adams", position = PlayerPosition.CM,
            jerseyNumber = 23, age = 25, overall = 83, pace = 82, shooting = 79, passing = 85,
            dribbling = 84, defending = 78, physical = 81, stamina = 90, marketValue = 30_000_000L,
            weeklyWage = 90_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Andre de Jong", position = PlayerPosition.CAM,
            jerseyNumber = 18, age = 29, nationality = "New Zealand", overall = 80, pace = 78, shooting = 80, passing = 81,
            dribbling = 80, defending = 60, physical = 78, stamina = 84, marketValue = 15_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Devin Titus", position = PlayerPosition.RW,
            jerseyNumber = 34, age = 25, overall = 82, pace = 90, shooting = 80, passing = 76,
            dribbling = 84, defending = 58, physical = 77, stamina = 90, marketValue = 24_000_000L,
            weeklyWage = 80_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Lehlohonolo Mojela", position = PlayerPosition.ST,
            jerseyNumber = 11, age = 30, overall = 80, pace = 86, shooting = 82, passing = 72,
            dribbling = 79, defending = 50, physical = 81, stamina = 85, marketValue = 16_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Chumani Butsaka", position = PlayerPosition.LW,
            jerseyNumber = 7, age = 25, overall = 80, pace = 87, shooting = 77, passing = 78,
            dribbling = 82, defending = 53, physical = 73, stamina = 85, marketValue = 17_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Oscarine Masuluke", position = PlayerPosition.GK,
            jerseyNumber = 30, age = 33, overall = 75, pace = 54, shooting = 35, passing = 65,
            dribbling = 50, defending = 75, physical = 78, stamina = 77, marketValue = 5_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Omega Mdaka", position = PlayerPosition.LB,
            jerseyNumber = 3, age = 22, overall = 76, pace = 83, shooting = 56, passing = 72,
            dribbling = 74, defending = 75, physical = 73, stamina = 82, marketValue = 8_000_000L,
            weeklyWage = 40_000L, contractYearsRemaining = 3, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Genino Palace", position = PlayerPosition.CM,
            jerseyNumber = 6, age = 27, overall = 77, pace = 76, shooting = 72, passing = 78,
            dribbling = 77, defending = 74, physical = 76, stamina = 82, marketValue = 10_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Ashley Cupido", position = PlayerPosition.ST,
            jerseyNumber = 10, age = 25, overall = 79, pace = 86, shooting = 80, passing = 70,
            dribbling = 78, defending = 49, physical = 79, stamina = 84, marketValue = 15_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 3, isStartingEleven = false
        )
    )

    private fun generateSuperSportSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Ricardo Goss", position = PlayerPosition.GK,
            jerseyNumber = 20, age = 32, overall = 80, pace = 62, shooting = 26, passing = 73,
            dribbling = 55, defending = 81, physical = 77, stamina = 82, marketValue = 14_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Nyiko Mobbie", position = PlayerPosition.RB,
            jerseyNumber = 2, age = 32, overall = 78, pace = 81, shooting = 58, passing = 74,
            dribbling = 73, defending = 78, physical = 78, stamina = 84, marketValue = 10_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Thulani Hlatshwayo", position = PlayerPosition.CB,
            jerseyNumber = 3, age = 36, overall = 78, pace = 65, shooting = 52, passing = 70,
            dribbling = 63, defending = 80, physical = 83, stamina = 78, marketValue = 6_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 1, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Neo Rapoo", position = PlayerPosition.CB,
            jerseyNumber = 25, age = 23, overall = 78, pace = 77, shooting = 46, passing = 73,
            dribbling = 69, defending = 78, physical = 78, stamina = 84, marketValue = 12_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Lyle Lakay", position = PlayerPosition.LB,
            jerseyNumber = 14, age = 35, overall = 78, pace = 78, shooting = 77, passing = 83,
            dribbling = 76, defending = 74, physical = 75, stamina = 80, marketValue = 7_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Grant Margeman", position = PlayerPosition.CM,
            jerseyNumber = 23, age = 28, overall = 80, pace = 77, shooting = 76, passing = 82,
            dribbling = 81, defending = 76, physical = 77, stamina = 86, marketValue = 17_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Siphesihle Ndlovu", position = PlayerPosition.CM,
            jerseyNumber = 8, age = 29, overall = 79, pace = 78, shooting = 73, passing = 79,
            dribbling = 79, defending = 77, physical = 78, stamina = 87, marketValue = 14_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Vincent Pule", position = PlayerPosition.RW,
            jerseyNumber = 11, age = 34, overall = 78, pace = 80, shooting = 78, passing = 79,
            dribbling = 80, defending = 55, physical = 73, stamina = 78, marketValue = 8_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Gape Moralo", position = PlayerPosition.CAM,
            jerseyNumber = 28, age = 26, overall = 77, pace = 78, shooting = 74, passing = 79,
            dribbling = 78, defending = 62, physical = 73, stamina = 83, marketValue = 10_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Terrence Dzvukamanja", position = PlayerPosition.ST,
            jerseyNumber = 10, age = 32, nationality = "Zimbabwe", overall = 79, pace = 81, shooting = 80, passing = 74,
            dribbling = 78, defending = 54, physical = 80, stamina = 82, marketValue = 12_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Bradley Grobler", position = PlayerPosition.ST,
            jerseyNumber = 7, age = 38, overall = 77, pace = 71, shooting = 83, passing = 71,
            dribbling = 73, defending = 47, physical = 80, stamina = 74, marketValue = 5_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Washington Arubi", position = PlayerPosition.GK,
            jerseyNumber = 1, age = 40, nationality = "Zimbabwe", overall = 73, pace = 50, shooting = 24, passing = 65,
            dribbling = 48, defending = 74, physical = 75, stamina = 72, marketValue = 2_500_000L,
            weeklyWage = 40_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Pogiso Sanoka", position = PlayerPosition.CB,
            jerseyNumber = 4, age = 34, overall = 75, pace = 70, shooting = 48, passing = 69,
            dribbling = 65, defending = 76, physical = 79, stamina = 76, marketValue = 5_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Keanin Ayer", position = PlayerPosition.CM,
            jerseyNumber = 16, age = 26, nationality = "Ghana", overall = 76, pace = 76, shooting = 71, passing = 77,
            dribbling = 76, defending = 72, physical = 75, stamina = 81, marketValue = 8_000_000L,
            weeklyWage = 48_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Donay Jansen", position = PlayerPosition.LW,
            jerseyNumber = 35, age = 23, overall = 78, pace = 87, shooting = 76, passing = 74,
            dribbling = 80, defending = 51, physical = 72, stamina = 83, marketValue = 12_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 3, isStartingEleven = false
        )
    )

    private fun generateAmaZuluSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Veli Mothwa", position = PlayerPosition.GK,
            jerseyNumber = 44, age = 35, overall = 78, pace = 61, shooting = 26, passing = 70,
            dribbling = 56, defending = 79, physical = 78, stamina = 81, marketValue = 8_500_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 1, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Wayde Jooste", position = PlayerPosition.RB,
            jerseyNumber = 26, age = 35, overall = 75, pace = 78, shooting = 62, passing = 72,
            dribbling = 73, defending = 74, physical = 74, stamina = 80, marketValue = 5_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Taariq Fielies", position = PlayerPosition.CB,
            jerseyNumber = 5, age = 34, overall = 76, pace = 68, shooting = 50, passing = 71,
            dribbling = 65, defending = 77, physical = 81, stamina = 78, marketValue = 6_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Abbubaker Mobara", position = PlayerPosition.CB,
            jerseyNumber = 2, age = 32, overall = 76, pace = 72, shooting = 54, passing = 72,
            dribbling = 69, defending = 77, physical = 78, stamina = 80, marketValue = 7_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Riaan Hanamub", position = PlayerPosition.LB,
            jerseyNumber = 14, age = 31, nationality = "Namibia", overall = 78, pace = 82, shooting = 64, passing = 78,
            dribbling = 76, defending = 77, physical = 78, stamina = 85, marketValue = 10_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Ethan Brooks", position = PlayerPosition.CM,
            jerseyNumber = 18, age = 25, overall = 78, pace = 79, shooting = 73, passing = 79,
            dribbling = 79, defending = 75, physical = 77, stamina = 86, marketValue = 14_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Ben Motshwari", position = PlayerPosition.CDM,
            jerseyNumber = 22, age = 35, overall = 76, pace = 70, shooting = 62, passing = 75,
            dribbling = 73, defending = 78, physical = 79, stamina = 80, marketValue = 5_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Hendrick Ekstein", position = PlayerPosition.CAM,
            jerseyNumber = 27, age = 35, overall = 76, pace = 75, shooting = 74, passing = 79,
            dribbling = 81, defending = 53, physical = 68, stamina = 76, marketValue = 5_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Augustine Mulenga", position = PlayerPosition.RW,
            jerseyNumber = 17, age = 36, nationality = "Zambia", overall = 76, pace = 79, shooting = 76, passing = 76,
            dribbling = 79, defending = 49, physical = 73, stamina = 76, marketValue = 5_500_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Junior Dion", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 30, nationality = "Ivory Coast", overall = 77, pace = 79, shooting = 79, passing = 68,
            dribbling = 74, defending = 48, physical = 82, stamina = 81, marketValue = 9_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Tshepang Moremi", position = PlayerPosition.LW,
            jerseyNumber = 19, age = 26, overall = 77, pace = 86, shooting = 75, passing = 75,
            dribbling = 80, defending = 52, physical = 73, stamina = 83, marketValue = 11_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Richard Ofori", position = PlayerPosition.GK,
            jerseyNumber = 31, age = 32, nationality = "Ghana", overall = 76, pace = 57, shooting = 24, passing = 68,
            dribbling = 54, defending = 76, physical = 78, stamina = 79, marketValue = 6_500_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Thembela Sikhakhane", position = PlayerPosition.CB,
            jerseyNumber = 24, age = 33, overall = 74, pace = 71, shooting = 47, passing = 69,
            dribbling = 65, defending = 75, physical = 77, stamina = 76, marketValue = 4_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Rally Bwalya", position = PlayerPosition.CM,
            jerseyNumber = 10, age = 31, nationality = "Zambia", overall = 75, pace = 75, shooting = 72, passing = 77,
            dribbling = 76, defending = 62, physical = 72, stamina = 79, marketValue = 5_500_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Bongi Ntuli", position = PlayerPosition.ST,
            jerseyNumber = 7, age = 28, overall = 75, pace = 80, shooting = 76, passing = 67,
            dribbling = 74, defending = 48, physical = 80, stamina = 79, marketValue = 6_500_000L,
            weeklyWage = 48_000L, contractYearsRemaining = 2, isStartingEleven = false
        )
    )

    private fun generateSekhukhuneSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Badra Ali Sangare", position = PlayerPosition.GK,
            jerseyNumber = 1, age = 40, nationality = "Ivory Coast", overall = 76, pace = 56, shooting = 25, passing = 70,
            dribbling = 52, defending = 78, physical = 76, stamina = 74, marketValue = 4_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Njabulo Ngcobo", position = PlayerPosition.CB,
            jerseyNumber = 27, age = 32, overall = 78, pace = 72, shooting = 48, passing = 73,
            dribbling = 68, defending = 79, physical = 81, stamina = 82, marketValue = 10_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Daniel Cardoso", position = PlayerPosition.CB,
            jerseyNumber = 4, age = 37, overall = 76, pace = 63, shooting = 60, passing = 72,
            dribbling = 63, defending = 78, physical = 80, stamina = 75, marketValue = 3_500_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 1, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Vuyo Letlapa", position = PlayerPosition.RB,
            jerseyNumber = 2, age = 27, overall = 77, pace = 82, shooting = 56, passing = 73,
            dribbling = 74, defending = 77, physical = 76, stamina = 85, marketValue = 9_500_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Sikhosana Katlego", position = PlayerPosition.LB,
            jerseyNumber = 3, age = 28, overall = 77, pace = 82, shooting = 58, passing = 74,
            dribbling = 74, defending = 77, physical = 77, stamina = 85, marketValue = 9_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Linda Mntambo", position = PlayerPosition.CM,
            jerseyNumber = 23, age = 37, overall = 76, pace = 71, shooting = 76, passing = 80,
            dribbling = 78, defending = 73, physical = 76, stamina = 76, marketValue = 4_500_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Jamie Webber", position = PlayerPosition.CM,
            jerseyNumber = 12, age = 28, overall = 79, pace = 78, shooting = 79, passing = 81,
            dribbling = 80, defending = 73, physical = 77, stamina = 87, marketValue = 15_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Sipho Mbule", position = PlayerPosition.CAM,
            jerseyNumber = 21, age = 28, overall = 81, pace = 80, shooting = 81, passing = 84,
            dribbling = 85, defending = 66, physical = 78, stamina = 84, marketValue = 24_000_000L,
            weeklyWage = 95_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Shaune Mogaila", position = PlayerPosition.RW,
            jerseyNumber = 14, age = 31, overall = 77, pace = 87, shooting = 74, passing = 74,
            dribbling = 81, defending = 56, physical = 74, stamina = 84, marketValue = 9_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Chibuike Ohizu", position = PlayerPosition.ST,
            jerseyNumber = 34, age = 30, nationality = "Nigeria", overall = 78, pace = 81, shooting = 80, passing = 69,
            dribbling = 75, defending = 50, physical = 84, stamina = 82, marketValue = 11_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Andy Boyeli", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 25, nationality = "DR Congo", overall = 79, pace = 86, shooting = 81, passing = 70,
            dribbling = 79, defending = 48, physical = 82, stamina = 86, marketValue = 16_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Loydt Kazapua", position = PlayerPosition.GK,
            jerseyNumber = 16, age = 37, nationality = "Namibia", overall = 73, pace = 52, shooting = 25, passing = 65,
            dribbling = 48, defending = 74, physical = 75, stamina = 74, marketValue = 2_500_000L,
            weeklyWage = 40_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Tresor Yamba", position = PlayerPosition.CB,
            jerseyNumber = 15, age = 24, nationality = "DR Congo", overall = 76, pace = 75, shooting = 46, passing = 70,
            dribbling = 66, defending = 77, physical = 83, stamina = 82, marketValue = 9_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 3, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Kamohelo Mokotjo", position = PlayerPosition.CDM,
            jerseyNumber = 6, age = 35, overall = 75, pace = 67, shooting = 65, passing = 78,
            dribbling = 74, defending = 76, physical = 75, stamina = 76, marketValue = 3_500_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Tshepo Mokoena", position = PlayerPosition.LW,
            jerseyNumber = 17, age = 26, overall = 76, pace = 85, shooting = 73, passing = 73,
            dribbling = 77, defending = 52, physical = 71, stamina = 82, marketValue = 8_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 2, isStartingEleven = false
        )
    )

    private fun generateTSGalaxySquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Ira Tape", position = PlayerPosition.GK,
            jerseyNumber = 1, age = 29, nationality = "Ivory Coast", overall = 77, pace = 60, shooting = 25, passing = 69,
            dribbling = 54, defending = 78, physical = 77, stamina = 82, marketValue = 9_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Marks Munyai", position = PlayerPosition.RB,
            jerseyNumber = 23, age = 34, overall = 75, pace = 76, shooting = 57, passing = 71,
            dribbling = 71, defending = 75, physical = 75, stamina = 79, marketValue = 4_500_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 1, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "McBeth Mahlangu", position = PlayerPosition.CB,
            jerseyNumber = 4, age = 25, overall = 79, pace = 76, shooting = 47, passing = 72,
            dribbling = 69, defending = 80, physical = 83, stamina = 86, marketValue = 14_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Samukelo Kabini", position = PlayerPosition.LB,
            jerseyNumber = 3, age = 22, overall = 78, pace = 85, shooting = 62, passing = 75,
            dribbling = 78, defending = 77, physical = 76, stamina = 88, marketValue = 13_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Khulumani Ndamane", position = PlayerPosition.CB,
            jerseyNumber = 5, age = 26, overall = 76, pace = 74, shooting = 45, passing = 70,
            dribbling = 67, defending = 77, physical = 81, stamina = 83, marketValue = 8_500_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Mpho Mvelase", position = PlayerPosition.CDM,
            jerseyNumber = 12, age = 33, overall = 75, pace = 71, shooting = 64, passing = 75,
            dribbling = 74, defending = 76, physical = 78, stamina = 80, marketValue = 5_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Sphiwe Mahlangu", position = PlayerPosition.CM,
            jerseyNumber = 8, age = 29, overall = 76, pace = 77, shooting = 74, passing = 77,
            dribbling = 78, defending = 70, physical = 74, stamina = 84, marketValue = 8_500_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Puso Dithejane", position = PlayerPosition.RW,
            jerseyNumber = 17, age = 22, overall = 80, pace = 89, shooting = 77, passing = 77,
            dribbling = 84, defending = 59, physical = 74, stamina = 87, marketValue = 18_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 4, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Thato Khiba", position = PlayerPosition.CAM,
            jerseyNumber = 20, age = 29, overall = 77, pace = 78, shooting = 74, passing = 78,
            dribbling = 78, defending = 68, physical = 75, stamina = 84, marketValue = 9_500_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Samir Nurkovic", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 34, nationality = "Serbia", overall = 77, pace = 71, shooting = 82, passing = 70,
            dribbling = 73, defending = 49, physical = 85, stamina = 74, marketValue = 7_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Dzenan Zajmovic", position = PlayerPosition.ST,
            jerseyNumber = 27, age = 31, nationality = "Bosnia", overall = 77, pace = 78, shooting = 79, passing = 70,
            dribbling = 76, defending = 48, physical = 82, stamina = 80, marketValue = 9_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Wensten van der Linde", position = PlayerPosition.GK,
            jerseyNumber = 32, age = 32, overall = 73, pace = 55, shooting = 22, passing = 64,
            dribbling = 50, defending = 74, physical = 74, stamina = 77, marketValue = 3_000_000L,
            weeklyWage = 35_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Vuyo Mere", position = PlayerPosition.RB,
            jerseyNumber = 2, age = 42, overall = 71, pace = 60, shooting = 48, passing = 71,
            dribbling = 66, defending = 73, physical = 70, stamina = 66, marketValue = 1_000_000L,
            weeklyWage = 35_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Lindokuhle Mbatha", position = PlayerPosition.RW,
            jerseyNumber = 35, age = 40, overall = 73, pace = 74, shooting = 71, passing = 73,
            dribbling = 76, defending = 52, physical = 68, stamina = 70, marketValue = 1_500_000L,
            weeklyWage = 35_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Victor Letsoalo", position = PlayerPosition.ST,
            jerseyNumber = 11, age = 33, overall = 75, pace = 79, shooting = 76, passing = 68,
            dribbling = 73, defending = 48, physical = 77, stamina = 79, marketValue = 5_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 1, isStartingEleven = false
        )
    )

    private fun generateCTCitySquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Darren Keet", position = PlayerPosition.GK,
            jerseyNumber = 16, age = 37, overall = 77, pace = 58, shooting = 26, passing = 73,
            dribbling = 53, defending = 78, physical = 77, stamina = 78, marketValue = 5_000_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 1, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Keanu Cupido", position = PlayerPosition.CB,
            jerseyNumber = 15, age = 28, overall = 78, pace = 75, shooting = 48, passing = 73,
            dribbling = 69, defending = 79, physical = 81, stamina = 84, marketValue = 11_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Lorenzo Gordinho", position = PlayerPosition.CB,
            jerseyNumber = 25, age = 32, overall = 75, pace = 68, shooting = 52, passing = 70,
            dribbling = 65, defending = 76, physical = 81, stamina = 78, marketValue = 5_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Thamsanqa Mkhize", position = PlayerPosition.RB,
            jerseyNumber = 2, age = 38, overall = 75, pace = 73, shooting = 55, passing = 71,
            dribbling = 70, defending = 75, physical = 75, stamina = 75, marketValue = 2_500_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Nathan Fasika", position = PlayerPosition.CB,
            jerseyNumber = 4, age = 27, nationality = "DR Congo", overall = 78, pace = 74, shooting = 46, passing = 71,
            dribbling = 66, defending = 79, physical = 85, stamina = 83, marketValue = 12_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Taahir Goedeman", position = PlayerPosition.CM,
            jerseyNumber = 27, age = 27, overall = 78, pace = 79, shooting = 75, passing = 80,
            dribbling = 81, defending = 73, physical = 75, stamina = 85, marketValue = 13_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Fortune Makaringe", position = PlayerPosition.CAM,
            jerseyNumber = 11, age = 33, overall = 76, pace = 76, shooting = 74, passing = 79,
            dribbling = 81, defending = 63, physical = 73, stamina = 80, marketValue = 7_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Jaedin Rhodes", position = PlayerPosition.RW,
            jerseyNumber = 33, age = 23, overall = 80, pace = 89, shooting = 78, passing = 78,
            dribbling = 85, defending = 55, physical = 73, stamina = 87, marketValue = 18_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 4, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Haashim Domingo", position = PlayerPosition.CM,
            jerseyNumber = 10, age = 31, overall = 78, pace = 76, shooting = 74, passing = 81,
            dribbling = 81, defending = 70, physical = 74, stamina = 82, marketValue = 11_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Amadou Soukouna", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 34, nationality = "France", overall = 76, pace = 77, shooting = 79, passing = 71,
            dribbling = 75, defending = 47, physical = 79, stamina = 78, marketValue = 6_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Prins Tjiueza", position = PlayerPosition.LW,
            jerseyNumber = 7, age = 24, nationality = "Namibia", overall = 79, pace = 88, shooting = 78, passing = 76,
            dribbling = 83, defending = 53, physical = 73, stamina = 85, marketValue = 15_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Bongani Mpandle", position = PlayerPosition.GK,
            jerseyNumber = 1, age = 34, overall = 73, pace = 53, shooting = 22, passing = 64,
            dribbling = 50, defending = 74, physical = 75, stamina = 76, marketValue = 2_500_000L,
            weeklyWage = 35_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Patrick Fisher", position = PlayerPosition.RB,
            jerseyNumber = 22, age = 25, overall = 75, pace = 83, shooting = 55, passing = 71,
            dribbling = 73, defending = 75, physical = 75, stamina = 83, marketValue = 6_500_000L,
            weeklyWage = 35_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Luphumlo Sifumba", position = PlayerPosition.CM,
            jerseyNumber = 24, age = 21, overall = 76, pace = 82, shooting = 72, passing = 76,
            dribbling = 78, defending = 69, physical = 72, stamina = 84, marketValue = 10_000_000L,
            weeklyWage = 35_000L, contractYearsRemaining = 4, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Darwin Gonzalez", position = PlayerPosition.LW,
            jerseyNumber = 20, age = 32, nationality = "Venezuela", overall = 76, pace = 82, shooting = 75, passing = 74,
            dribbling = 78, defending = 51, physical = 71, stamina = 78, marketValue = 6_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 1, isStartingEleven = false
        )
    )

    private fun generateMagesiSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Elvis Chipezeze", position = PlayerPosition.GK,
            jerseyNumber = 1, age = 36, nationality = "Zimbabwe", overall = 80, pace = 60, shooting = 25, passing = 73,
            dribbling = 55, defending = 81, physical = 83, stamina = 85, marketValue = 12_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 2, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Mzwandile Buthelezi", position = PlayerPosition.RB,
            jerseyNumber = 2, age = 28, overall = 76, pace = 80, shooting = 52, passing = 71,
            dribbling = 72, defending = 77, physical = 76, stamina = 84, marketValue = 7_500_000L,
            weeklyWage = 48_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Delano Abrahams", position = PlayerPosition.CB,
            jerseyNumber = 5, age = 27, overall = 78, pace = 74, shooting = 58, passing = 73,
            dribbling = 68, defending = 79, physical = 83, stamina = 85, marketValue = 11_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Limbikani Mzava", position = PlayerPosition.CB,
            jerseyNumber = 4, age = 32, nationality = "Malawi", overall = 77, pace = 69, shooting = 50, passing = 71,
            dribbling = 65, defending = 78, physical = 83, stamina = 79, marketValue = 8_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "John Mokone", position = PlayerPosition.LB,
            jerseyNumber = 3, age = 26, overall = 76, pace = 81, shooting = 55, passing = 72,
            dribbling = 72, defending = 76, physical = 75, stamina = 84, marketValue = 7_500_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Samuel Darpoh", position = PlayerPosition.CDM,
            jerseyNumber = 6, age = 28, nationality = "Ghana", overall = 77, pace = 74, shooting = 63, passing = 77,
            dribbling = 75, defending = 79, physical = 81, stamina = 86, marketValue = 9_500_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Deolin Mekoa", position = PlayerPosition.CM,
            jerseyNumber = 8, age = 33, overall = 76, pace = 75, shooting = 72, passing = 78,
            dribbling = 77, defending = 70, physical = 74, stamina = 80, marketValue = 6_500_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Gift Motupa", position = PlayerPosition.CAM,
            jerseyNumber = 10, age = 32, overall = 77, pace = 77, shooting = 79, passing = 77,
            dribbling = 78, defending = 54, physical = 76, stamina = 78, marketValue = 8_500_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Tshepo Kakora", position = PlayerPosition.RW,
            jerseyNumber = 7, age = 26, overall = 78, pace = 88, shooting = 77, passing = 75,
            dribbling = 83, defending = 54, physical = 73, stamina = 86, marketValue = 12_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Wonderboy Makhubu", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 29, overall = 78, pace = 83, shooting = 80, passing = 70,
            dribbling = 76, defending = 50, physical = 83, stamina = 84, marketValue = 11_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Edmore Chirambadare", position = PlayerPosition.LW,
            jerseyNumber = 11, age = 34, nationality = "Zimbabwe", overall = 76, pace = 82, shooting = 77, passing = 76,
            dribbling = 79, defending = 51, physical = 73, stamina = 78, marketValue = 6_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Bongani Manavhela", position = PlayerPosition.GK,
            jerseyNumber = 16, age = 33, overall = 72, pace = 54, shooting = 20, passing = 62,
            dribbling = 50, defending = 72, physical = 74, stamina = 75, marketValue = 2_500_000L,
            weeklyWage = 30_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Tumelo Bodibe", position = PlayerPosition.CB,
            jerseyNumber = 14, age = 25, overall = 74, pace = 73, shooting = 44, passing = 69,
            dribbling = 65, defending = 75, physical = 77, stamina = 81, marketValue = 5_500_000L,
            weeklyWage = 35_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Kgaogelo Sekgota", position = PlayerPosition.RW,
            jerseyNumber = 17, age = 29, overall = 77, pace = 89, shooting = 74, passing = 74,
            dribbling = 84, defending = 50, physical = 71, stamina = 81, marketValue = 9_500_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Rhulani Manzini", position = PlayerPosition.ST,
            jerseyNumber = 19, age = 37, overall = 73, pace = 71, shooting = 77, passing = 65,
            dribbling = 69, defending = 44, physical = 82, stamina = 72, marketValue = 2_500_000L,
            weeklyWage = 40_000L, contractYearsRemaining = 1, isStartingEleven = false
        )
    )

    private fun generatePolokwaneCitySquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Manuel Sapunga", position = PlayerPosition.GK,
            jerseyNumber = 1, age = 34, nationality = "Equatorial Guinea", overall = 78, pace = 57, shooting = 24, passing = 72,
            dribbling = 52, defending = 79, physical = 78, stamina = 81, marketValue = 8_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Thabang Matuludi", position = PlayerPosition.RB,
            jerseyNumber = 2, age = 27, overall = 79, pace = 86, shooting = 59, passing = 75,
            dribbling = 77, defending = 78, physical = 77, stamina = 87, marketValue = 15_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Bulelani Nikani", position = PlayerPosition.CB,
            jerseyNumber = 4, age = 33, overall = 77, pace = 70, shooting = 46, passing = 70,
            dribbling = 65, defending = 79, physical = 83, stamina = 80, marketValue = 7_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 1, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Lebohang Nkaki", position = PlayerPosition.CB,
            jerseyNumber = 5, age = 32, overall = 76, pace = 69, shooting = 44, passing = 69,
            dribbling = 63, defending = 78, physical = 81, stamina = 79, marketValue = 6_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Langelihle Ndlovu", position = PlayerPosition.LB,
            jerseyNumber = 3, age = 25, overall = 76, pace = 83, shooting = 55, passing = 73,
            dribbling = 74, defending = 76, physical = 75, stamina = 85, marketValue = 8_500_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Manuel Kambala", position = PlayerPosition.CDM,
            jerseyNumber = 6, age = 34, nationality = "Mozambique", overall = 77, pace = 70, shooting = 67, passing = 76,
            dribbling = 73, defending = 78, physical = 83, stamina = 84, marketValue = 7_500_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Ndamulelo Maphangule", position = PlayerPosition.CM,
            jerseyNumber = 8, age = 27, overall = 78, pace = 78, shooting = 73, passing = 79,
            dribbling = 79, defending = 73, physical = 77, stamina = 86, marketValue = 12_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Cole Alexander", position = PlayerPosition.CM,
            jerseyNumber = 17, age = 37, overall = 75, pace = 67, shooting = 69, passing = 77,
            dribbling = 74, defending = 74, physical = 75, stamina = 76, marketValue = 3_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Oswin Appollis", position = PlayerPosition.LW,
            jerseyNumber = 11, age = 24, overall = 84, pace = 90, shooting = 83, passing = 84,
            dribbling = 89, defending = 56, physical = 75, stamina = 89, marketValue = 32_000_000L,
            weeklyWage = 90_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Puleng Marema", position = PlayerPosition.ST,
            jerseyNumber = 10, age = 34, overall = 75, pace = 76, shooting = 77, passing = 72,
            dribbling = 75, defending = 51, physical = 77, stamina = 79, marketValue = 5_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Mokibelo Ramabu", position = PlayerPosition.RW,
            jerseyNumber = 7, age = 28, overall = 76, pace = 85, shooting = 74, passing = 73,
            dribbling = 80, defending = 52, physical = 74, stamina = 83, marketValue = 8_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Lindokuhle Mathebula", position = PlayerPosition.GK,
            jerseyNumber = 16, age = 30, overall = 72, pace = 55, shooting = 20, passing = 64,
            dribbling = 50, defending = 73, physical = 74, stamina = 77, marketValue = 3_000_000L,
            weeklyWage = 30_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Stevens Goovadia", position = PlayerPosition.CAM,
            jerseyNumber = 20, age = 26, overall = 75, pace = 80, shooting = 74, passing = 76,
            dribbling = 78, defending = 54, physical = 73, stamina = 81, marketValue = 7_000_000L,
            weeklyWage = 40_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Hlayisi Chauke", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 27, overall = 76, pace = 82, shooting = 77, passing = 69,
            dribbling = 75, defending = 48, physical = 81, stamina = 83, marketValue = 8_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Simphiwe Tshabalala", position = PlayerPosition.LB,
            jerseyNumber = 12, age = 24, overall = 74, pace = 82, shooting = 50, passing = 71,
            dribbling = 72, defending = 74, physical = 74, stamina = 83, marketValue = 5_500_000L,
            weeklyWage = 35_000L, contractYearsRemaining = 3, isStartingEleven = false
        )
    )

    private fun generateChippaSquad(teamId: String): List<PlayerEntity> = listOf(
        PlayerEntity(
            id = "${teamId}_1", teamId = teamId, name = "Stanley Nwabali", position = PlayerPosition.GK,
            jerseyNumber = 1, age = 30, nationality = "Nigeria", overall = 83, pace = 64, shooting = 28, passing = 79,
            dribbling = 59, defending = 84, physical = 85, stamina = 87, marketValue = 28_000_000L,
            weeklyWage = 100_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_2", teamId = teamId, name = "Craig Martin", position = PlayerPosition.RB,
            jerseyNumber = 2, age = 32, overall = 77, pace = 87, shooting = 66, passing = 74,
            dribbling = 78, defending = 75, physical = 76, stamina = 86, marketValue = 9_000_000L,
            weeklyWage = 60_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_3", teamId = teamId, name = "Justice Chabalala", position = PlayerPosition.CB,
            jerseyNumber = 4, age = 34, overall = 77, pace = 70, shooting = 45, passing = 71,
            dribbling = 64, defending = 79, physical = 85, stamina = 80, marketValue = 7_500_000L,
            weeklyWage = 65_000L, contractYearsRemaining = 1, isCaptain = true, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_4", teamId = teamId, name = "Roscoe Pietersen", position = PlayerPosition.CB,
            jerseyNumber = 5, age = 36, overall = 75, pace = 65, shooting = 50, passing = 70,
            dribbling = 62, defending = 77, physical = 82, stamina = 75, marketValue = 3_500_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_5", teamId = teamId, name = "Malebogo Modise", position = PlayerPosition.LB,
            jerseyNumber = 3, age = 27, overall = 77, pace = 84, shooting = 59, passing = 75,
            dribbling = 77, defending = 76, physical = 76, stamina = 86, marketValue = 10_500_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_6", teamId = teamId, name = "Baraka Majogoro", position = PlayerPosition.CDM,
            jerseyNumber = 6, age = 29, nationality = "Tanzania", overall = 76, pace = 73, shooting = 64, passing = 76,
            dribbling = 75, defending = 78, physical = 82, stamina = 85, marketValue = 8_500_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_7", teamId = teamId, name = "Sinoxolo Kwayiba", position = PlayerPosition.CM,
            jerseyNumber = 8, age = 26, overall = 80, pace = 84, shooting = 79, passing = 80,
            dribbling = 83, defending = 72, physical = 78, stamina = 88, marketValue = 18_000_000L,
            weeklyWage = 70_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_8", teamId = teamId, name = "Ayabulela Konqobe", position = PlayerPosition.CAM,
            jerseyNumber = 10, age = 30, overall = 76, pace = 76, shooting = 74, passing = 79,
            dribbling = 78, defending = 62, physical = 74, stamina = 81, marketValue = 7_500_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 2, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_9", teamId = teamId, name = "Aviwe Mqoqi", position = PlayerPosition.RW,
            jerseyNumber = 7, age = 25, overall = 76, pace = 87, shooting = 73, passing = 73,
            dribbling = 80, defending = 52, physical = 73, stamina = 85, marketValue = 9_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 3, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_10", teamId = teamId, name = "Bienvenu Eva Nga", position = PlayerPosition.ST,
            jerseyNumber = 9, age = 33, nationality = "Cameroon", overall = 78, pace = 81, shooting = 81, passing = 70,
            dribbling = 76, defending = 51, physical = 86, stamina = 80, marketValue = 10_000_000L,
            weeklyWage = 75_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        PlayerEntity(
            id = "${teamId}_11", teamId = teamId, name = "Andile Mbenyane", position = PlayerPosition.LW,
            jerseyNumber = 11, age = 38, overall = 74, pace = 73, shooting = 75, passing = 75,
            dribbling = 76, defending = 53, physical = 73, stamina = 74, marketValue = 2_000_000L,
            weeklyWage = 55_000L, contractYearsRemaining = 1, isStartingEleven = true
        ),
        // Bench
        PlayerEntity(
            id = "${teamId}_12", teamId = teamId, name = "Darren Johnson", position = PlayerPosition.GK,
            jerseyNumber = 16, age = 29, overall = 72, pace = 56, shooting = 22, passing = 66,
            dribbling = 50, defending = 73, physical = 74, stamina = 77, marketValue = 3_000_000L,
            weeklyWage = 30_000L, contractYearsRemaining = 2, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_13", teamId = teamId, name = "Azola Matrose", position = PlayerPosition.LW,
            jerseyNumber = 17, age = 23, overall = 77, pace = 89, shooting = 75, passing = 74,
            dribbling = 83, defending = 51, physical = 71, stamina = 85, marketValue = 12_000_000L,
            weeklyWage = 50_000L, contractYearsRemaining = 3, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_14", teamId = teamId, name = "Elmo Kambindu", position = PlayerPosition.ST,
            jerseyNumber = 18, age = 33, nationality = "Namibia", overall = 75, pace = 77, shooting = 76, passing = 67,
            dribbling = 73, defending = 47, physical = 81, stamina = 78, marketValue = 5_000_000L,
            weeklyWage = 45_000L, contractYearsRemaining = 1, isStartingEleven = false
        ),
        PlayerEntity(
            id = "${teamId}_15", teamId = teamId, name = "Thabang Molaoa", position = PlayerPosition.RB,
            jerseyNumber = 20, age = 25, overall = 74, pace = 82, shooting = 53, passing = 70,
            dribbling = 73, defending = 74, physical = 74, stamina = 83, marketValue = 6_000_000L,
            weeklyWage = 32_000L, contractYearsRemaining = 2, isStartingEleven = false
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
