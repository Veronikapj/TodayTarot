package com.example.mytarot.ai

import android.content.Context
import com.example.mytarot.model.FortuneResult
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pilju.android.todaytarot.R
import java.io.File

class TarotAiManager(private val context: Context) {

    private var llmInference: LlmInference? = null

    // 모델 파일이 저장된 경로 (Device File Explorer로 넣은 경로)
    // 실제 배포시에는 assets에서 내부 저장소로 복사하는 로직이 필요하지만, 샘플앱에선 절대경로 사용 추천
    private val modelPath = "/data/local/tmp/gemma-2b-it-gpu-int4.bin"

    // AI 엔진 초기화 (앱 시작시 혹은 최초 실행시 호출 필요)
    suspend fun initialize() {
        withContext(Dispatchers.IO) {
            if (llmInference != null) return@withContext

            val file = File(modelPath)
            if (!file.exists()) {
                // 모델 파일이 없으면 더미 모드로 동작하거나 에러 처리
                println(context.getString(R.string.ai_model_not_found, modelPath))
                return@withContext
            }

            val options = LlmInference.LlmInferenceOptions.builder()
                .setModelPath(modelPath)
                .setMaxTokens(512) // 답변 길이 제한
                // .setTemperature(0.7f) // setTemperature는 현재 API에서 지원되지 않음
                .build()

            llmInference = LlmInference.createFromOptions(context, options)
        }
    }

    suspend fun getFortune(worry: String): FortuneResult = withContext(Dispatchers.IO) {
        // 모델이 로드되지 않았으면 초기화 시도
        if (llmInference == null) initialize()

        // 모델 파일이 없거나 초기화 실패시 더미 데이터 반환 (앱 죽음 방지)
        if (llmInference == null) {
            return@withContext FortuneResult(
                cardName = context.getString(R.string.ai_demo_card_name),
                cardDescription = context.getString(R.string.ai_demo_description),
                mission = context.getString(R.string.ai_demo_mission)
            )
        }

        // 1. 프롬프트 엔지니어링 (한국어로 답변 유도 + 포맷 지정)
        val prompt = """
            You are a warm and mystical Tarot reader.
            User's worry: "$worry"
            Card: "The Sun" (Symbolizes Success, Joy, Positivity)
            
            Please respond in Korean.
            Format your response exactly like this:
            [Cheering Message] @ [Funny Lucky Mission]
            
            Do not include any other text.
            Example:
            걱정하지 마세요, 태양이 당신을 비추고 있어요. @ 좋아하는 노래 듣기
            
            Response:
        """.trimIndent()

        try {
            // 2. AI 추론 실행
            val response = llmInference?.generateResponse(prompt) ?: ""

            // 3. 결과 파싱 ("@" 문자로 메시지와 미션 분리)
            val parts = response.split("@")

            val message = parts.getOrNull(0)?.trim()
                ?: context.getString(R.string.ai_default_message)
            val mission = parts.getOrNull(1)?.trim()
                ?: context.getString(R.string.ai_default_mission)

            FortuneResult(
                cardName = context.getString(R.string.ai_card_name_sun), // 실제 앱에선 뽑은 카드 이름을 넣어야 함
                cardDescription = message,
                mission = mission
            )
        } catch (e: Exception) {
            e.printStackTrace()
            FortuneResult(
                cardName = context.getString(R.string.ai_error_card_name),
                cardDescription = context.getString(R.string.ai_error_description),
                mission = context.getString(R.string.ai_error_mission)
            )
        }
    }
}
