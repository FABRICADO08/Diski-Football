package com.example.ui.phase2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.TeamEntity
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.DiskiGold
import com.example.ui.theme.PitchGreenDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class PressQuestion(
    val mediaOutlet: String,
    val journalistName: String,
    val question: String,
    val options: List<PressResponseOption>
)

data class PressResponseOption(
    val answer: String,
    val effectLabel: String,
    val moraleBonus: Int
)

@Composable
fun PressConferenceDialog(
    userTeam: TeamEntity,
    opponentTeam: TeamEntity,
    derbyName: String,
    onFinishConference: (totalMoraleBonus: Int, summary: String) -> Unit,
    onDismiss: () -> Unit
) {
    val questions = remember(userTeam.id, opponentTeam.id) {
        listOf(
            PressQuestion(
                mediaOutlet = "Soccer Laduma",
                journalistName = "Sipho Mthembu",
                question = "Coach, the whole nation is buzzing for this titanic $derbyName encounter! How will you approach ${opponentTeam.shortName} today?",
                options = listOf(
                    PressResponseOption(
                        answer = "We will play authentic 'shoe-shine and piano' football! Diski magic will conquer them.",
                        effectLabel = "+5 Team Morale & Diski Flavour boost",
                        moraleBonus = 5
                    ),
                    PressResponseOption(
                        answer = "Relentless high-press from the first whistle. We won't give them a second on the ball.",
                        effectLabel = "+4 Attack sharpness",
                        moraleBonus = 4
                    ),
                    PressResponseOption(
                        answer = "Derbies are won with tactical discipline and grit. We will shut them down completely.",
                        effectLabel = "+4 Defensive resilience",
                        moraleBonus = 4
                    )
                )
            ),
            PressQuestion(
                mediaOutlet = "SuperSport Football",
                journalistName = "Carol Tshabalala",
                question = "The stadium is packed to capacity with 80,000 passionate fans. How are your players dealing with the derby pressure?",
                options = listOf(
                    PressResponseOption(
                        answer = "Pressure is a privilege. Our boys thrive in the electric stadium atmosphere!",
                        effectLabel = "+5 Confidence & Stamina resilience",
                        moraleBonus = 5
                    ),
                    PressResponseOption(
                        answer = "We respect ${opponentTeam.shortName}, but our tactical preparation has been flawless all week.",
                        effectLabel = "+3 Tactical composure",
                        moraleBonus = 3
                    ),
                    PressResponseOption(
                        answer = "We let our football do the talking. The fans will leave the stadium celebrating!",
                        effectLabel = "+4 Fan excitement & Morale",
                        moraleBonus = 4
                    )
                )
            )
        )
    }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var cumulativeBonus by remember { mutableIntStateOf(0) }
    var conferenceFinished by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            border = androidx.compose.foundation.BorderStroke(2.dp, DiskiGold),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("dialog_press_conference")
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(DiskiGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Mic, contentDescription = "Mic", tint = SurfaceDark, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "PRE-MATCH PRESS ROOM", fontSize = 12.sp, fontWeight = FontWeight.Black, color = DiskiGold)
                            Text(text = derbyName.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF1B6B2F)
                    ) {
                        Text(
                            text = "${userTeam.shortName} vs ${opponentTeam.shortName}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (!conferenceFinished && currentQuestionIndex < questions.size) {
                    val q = questions[currentQuestionIndex]

                    // Media badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0x33FFFFFF),
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = q.mediaOutlet, fontSize = 10.sp, fontWeight = FontWeight.Black, color = DiskiGold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "• ${q.journalistName}", fontSize = 9.sp, color = TextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Question prompt
                    Text(
                        text = "\"${q.question}\"",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "SELECT YOUR RESPONSE AS MANAGER:",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = TextSecondary,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Options
                    for (opt in q.options) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PitchGreenDark,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x66FFD200)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    cumulativeBonus += opt.moraleBonus
                                    if (currentQuestionIndex + 1 < questions.size) {
                                        currentQuestionIndex++
                                    } else {
                                        conferenceFinished = true
                                    }
                                }
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = opt.answer,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "⚡ Effect: ${opt.effectLabel}",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentCyan
                                )
                            }
                        }
                    }
                } else {
                    // Summary Screen
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(text = "PRESS CONFERENCE CONCLUDED", fontSize = 15.sp, fontWeight = FontWeight.Black, color = DiskiGold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Your inspiring statements have rallied the squad and ignited the fanbase ahead of the derby showdown!",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF1B6B2F),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Star, contentDescription = "Star", tint = DiskiGold, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "+$cumulativeBonus Squad Morale & Derby Bonus Active",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                onFinishConference(cumulativeBonus, "Derby morale boosted by +$cumulativeBonus!")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DiskiGold),
                            modifier = Modifier.fillMaxWidth().testTag("btn_lead_team_out")
                        ) {
                            Text(text = "LEAD SQUAD ONTO THE PITCH", color = SurfaceDark, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}
