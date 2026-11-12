package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.database.DiskiDatabase
import com.example.data.model.CareerSaveEntity
import com.example.data.model.PlayerEntity
import com.example.data.model.PlayerPosition
import com.example.data.model.TeamEntity
import com.example.data.repository.DiskiRepository
import com.example.data.seed.SouthAfricanTeamsData
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class TransferBuySellTest {

    private lateinit var db: DiskiDatabase
    private lateinit var repository: DiskiRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, DiskiDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = DiskiRepository(db)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testBuyAndSellPlayerUpdatesBudgetAndRoster() = runBlocking {
        // Seed teams and players
        val sundowns = SouthAfricanTeamsData.pslTeams.first { it.id == "mamelodi_sundowns" }
        val chiefs = SouthAfricanTeamsData.pslTeams.first { it.id == "kaizer_chiefs" }
        db.teamDao().insertAll(listOf(sundowns, chiefs))

        // Create initial career save managing Chiefs with 50M budget
        val career = CareerSaveEntity(
            id = 1,
            managerName = "Coach Khosi",
            selectedTeamId = chiefs.id,
            difficulty = "Normal",
            transferBudgetRemaining = 50_000_000L,
            currentGameWeek = 1
        )
        db.careerSaveDao().insertSave(career)

        // Player to buy from Sundowns (fee: 15M)
        val targetPlayer = PlayerEntity(
            id = "test_player_target",
            teamId = sundowns.id,
            name = "Scouted Striker",
            position = PlayerPosition.ST,
            jerseyNumber = 9,
            age = 24,
            overall = 80,
            pace = 85,
            shooting = 82,
            passing = 75,
            dribbling = 80,
            defending = 40,
            physical = 78,
            stamina = 85,
            marketValue = 15_000_000L,
            weeklyWage = 80_000L,
            contractYearsRemaining = 3
        )

        // Player to sell from Chiefs (value: 20M)
        val squadPlayer = PlayerEntity(
            id = "test_player_squad",
            teamId = chiefs.id,
            name = "Experienced Midfielder",
            position = PlayerPosition.CM,
            jerseyNumber = 8,
            age = 29,
            overall = 78,
            pace = 70,
            shooting = 74,
            passing = 80,
            dribbling = 76,
            defending = 72,
            physical = 74,
            stamina = 80,
            marketValue = 20_000_000L,
            weeklyWage = 90_000L,
            contractYearsRemaining = 2,
            isStartingEleven = true
        )

        db.playerDao().insertAll(listOf(targetPlayer, squadPlayer))

        // 1. BUY PLAYER TEST
        repository.buyPlayer(targetPlayer.id, chiefs.id)

        val updatedSaveAfterBuy = db.careerSaveDao().getActiveSave()
        assertNotNull(updatedSaveAfterBuy)
        assertEquals(35_000_000L, updatedSaveAfterBuy?.transferBudgetRemaining)

        val boughtPlayer = db.playerDao().getPlayerById(targetPlayer.id)
        assertNotNull(boughtPlayer)
        assertEquals(chiefs.id, boughtPlayer?.teamId)

        // 2. SELL PLAYER TEST (Sell to Sundowns for 23M offer)
        val transferFee = 23_000_000L
        repository.sellPlayer(squadPlayer.id, sundowns.id, transferFee)

        val updatedSaveAfterSell = db.careerSaveDao().getActiveSave()
        assertNotNull(updatedSaveAfterSell)
        assertEquals(58_000_000L, updatedSaveAfterSell?.transferBudgetRemaining)

        val soldPlayer = db.playerDao().getPlayerById(squadPlayer.id)
        assertNotNull(soldPlayer)
        assertEquals(sundowns.id, soldPlayer?.teamId)
        assertEquals(false, soldPlayer?.isStartingEleven)
    }
}
