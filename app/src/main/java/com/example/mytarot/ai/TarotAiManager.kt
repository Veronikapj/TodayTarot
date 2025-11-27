package com.example.mytarot.ai

import com.example.mytarot.model.FortuneResult
import kotlinx.coroutines.delay

class TarotAiManager {
    // 실제로는 여기서 Gemini Nano 등을 호출합니다.
    suspend fun getFortune(worry: String): FortuneResult {
        delay(1500) // AI가 고민하는 척 (1.5초 딜레이)

        return FortuneResult(
            cardName = "THE SUN",
            cardDescription = "태양이 세상을 따뜻하게 비추듯, 당신의 삶에도 기쁨과 성공의 시기가 다가오고 있습니다. 긍정적인 에너지를 받아들이고 자신감 있게 목표를 향해 나아가세요. 당신의 고민은 눈 녹듯 사라질 거예요.",
            mission = "좋아하는 신나는 노래를 틀고\n1분 동안 춤을 춰보세요!"
        )
    }
}
