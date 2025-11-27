package com.example.mytarot.model

data class FortuneResult(
    val cardName: String,     // 예: "THE SUN"
    val cardDescription: String, // 카드 해석 (긴 글)
    val mission: String       // 럭키 미션
)