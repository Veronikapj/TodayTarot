package com.example.mytarot.model

data class FortuneResult(
    val cardName: String,     // 카드 이름 (예: string resource에서 로드)
    val cardDescription: String, // 카드 해석 (긴 글)
    val mission: String       // 럭키 미션
)