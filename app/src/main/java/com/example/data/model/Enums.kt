package com.example.data.model

enum class LeagueType {
    PSL,      // Premier Soccer League (Betway Premiership)
    NFD,      // Motsepe Foundation Championship (National First Division)
    PLAYOFF   // PSL/NFD Promotion-Relegation Play-offs
}

enum class TournamentType(val displayName: String, val description: String) {
    PSL("Betway Premiership", "South Africa's elite 16-team top-flight league"),
    NFD("Motsepe Foundation Championship", "16-team National First Division with auto-promotion"),
    MTN8("MTN8 Cup (Wafa Wafa)", "Exclusive knockout cup for the PSL Top 8 from prior season"),
    CARLING_CKO("Carling Knockout Cup", "All 16 PSL teams in high-stakes knockout + All-Star clash"),
    NEDBANK_CUP("Nedbank Cup", "The premier national cup: 32 teams from PSL, NFD & Amateurs"),
    PLAYOFF("PSL/NFD Promotion Play-offs", "3-team round robin: 15th PSL vs 2nd & 3rd NFD"),
    FRIENDLY("Exhibition Match", "Quick match against local rivals")
}

enum class PlayerPosition(val group: String) {
    GK("Goalkeeper"),
    LB("Defender"),
    CB("Defender"),
    RB("Defender"),
    LWB("Defender"),
    RWB("Defender"),
    CDM("Midfielder"),
    CM("Midfielder"),
    CAM("Midfielder"),
    LM("Midfielder"),
    RM("Midfielder"),
    LW("Attacker"),
    RW("Attacker"),
    ST("Attacker"),
    CF("Attacker")
}

enum class MatchStatus {
    SCHEDULED,
    IN_PROGRESS,
    FINISHED,
    POSTPONED
}
