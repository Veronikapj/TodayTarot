# 🔮 Today's Tarot (오늘의 타로)

**Google MediaPipe LLM Inference API를 활용한 온디바이스 AI 타로 운세 앱**

이 샘플 앱은 **온디바이스 AI (On-Device AI)**를 Android 앱에서 어떻게 활용하는지 보여주기 위한 데모 프로젝트입니다.  
서버 없이, 사용자의 기기에서 직접 AI 모델을 실행하여 개인화된 타로 운세를 제공합니다.

<br/>

## 📱 앱 소개

사용자가 오늘의 고민을 입력하면, 타로 카드를 뽑아 AI가 그에 맞는 응원 메시지와 행운의 미션을 생성해주는 힐링 앱입니다.

### 주요 화면 흐름

1. **InputScreen**: 고민 입력
2. **SelectionScreen**: 타로 카드 선택
3. **LoadingScreen**: AI 추론 중
4. **ResultScreen**: 카드 해석 & 미션 표시

<br/>

---

## 🎯 이 앱의 목적

> **온디바이스 AI (On-Device AI)**를 실제 앱에서 어떻게 사용하는지 보여주는 샘플입니다.

- ✅ **Google MediaPipe LLM Inference API** 사용법 시연
- ✅ Gemma 2B 모델을 Android 기기에서 직접 실행
- ✅ 서버 없이 완전히 오프라인에서 동작하는 AI 앱

<br/>

---

## 🛠️ 기술 스택

| 분야 | 기술 |
|------|------|
| **언어** | Kotlin |
| **UI** | Jetpack Compose + Material3 |
| **아키텍처** | MVVM (ViewModel + StateFlow) |
| **AI 엔진** | Google MediaPipe Tasks GenAI (`com.google.mediapipe:tasks-genai:0.10.29`) |
| **모델** | Gemma 2B (GPU INT8 quantized) |
| **빌드** | Gradle Kotlin DSL |

<br/>

---

## 📦 프로젝트 구조

```
app/src/main/java/
├── pilju.android.todaytarot/
│   ├── MainActivity.kt                    # 앱 진입점 & 화면 전환 로직
│   └── ui/theme/                          # Compose 테마 (Color, Type, Theme)
│
└── com.example.mytarot/
    ├── ai/
    │   └── TarotAiManager.kt              # 🧠 핵심! AI 모델 로드 & 추론
    ├── model/
    │   ├── FortuneResult.kt               # 운세 결과 데이터 클래스
    │   └── TarotCard.kt                   # 타로 카드 데이터 클래스
    └── ui/
        ├── TarotViewModel.kt              # 상태 관리 (StateFlow)
        ├── InputScreen.kt                 # 고민 입력 화면
        ├── SelectScreen.kt                # 카드 선택 화면
        └── ResultScreen.kt                # 결과 화면
```

<br/>

---

## 🔥 핵심 코드 분석: 온디바이스 AI 사용법

### 1️⃣ AI 엔진 초기화 (`TarotAiManager.kt`)

```kotlin
// MediaPipe LlmInference 객체 생성
val options = LlmInference.LlmInferenceOptions.builder()
    .setModelPath(modelPath)        // 모델 파일 경로 지정
    .setMaxTokens(512)               // 최대 생성 토큰 수
    .build()

llmInference = LlmInference.createFromOptions(context, options)
```

**핵심 포인트:**

- `LlmInference.createFromOptions()` 한 줄로 AI 엔진 생성
- 모델 파일은 기기의 로컬 경로에 저장 (예: `/data/local/tmp/gemma2-2b-it-gpu-int8.bin`)
- 서버 통신 없이 완전히 오프라인 동작

<br/>

### 2️⃣ AI 추론 실행

```kotlin
suspend fun getFortune(worry: String): FortuneResult = withContext(Dispatchers.IO) {
    // 1. 랜덤으로 타로 카드 선택
    val selectedCardKey = tarotDeck.random()
    
    // 2. 프롬프트 작성 (한국어 응답 유도)
    val prompt = """
        <start_of_turn>user
        Role: Tarot Reader.
        Task: Create a NEW Korean response based on the user's card and worry.
        Input Worry: "$worry"
        Input Card: "$selectedCardKey"
        Output:
        <end_of_turn>
        <start_of_turn>model
    """.trimIndent()
    
    // 3. AI에게 추론 요청 (단 한 줄!)
    val response = llmInference?.generateResponse(prompt) ?: ""
    
    // 4. 결과 반환
    FortuneResult(
        cardName = selectedCardKey,
        cardDescription = response.trim(),
        mission = fallbackMissions.random()
    )
}
```

**핵심 포인트:**

- `generateResponse(prompt)` 호출만으로 AI 응답 생성
- Coroutine (`withContext(Dispatchers.IO)`)으로 백그라운드 처리
- 프롬프트 엔지니어링으로 한국어 응답 유도

<br/>

### 3️⃣ ViewModel에서 AI 호출

```kotlin
class TarotViewModel(application: Application) : AndroidViewModel(application) {
    private val aiManager = TarotAiManager(application.applicationContext)
    
    fun pickCard() {
        viewModelScope.launch {
            _screenState.value = ScreenState.LOADING
            val result = aiManager.getFortune(_worryText.value)  // AI 호출
            _fortuneResult.value = result
            _screenState.value = ScreenState.RESULT
        }
    }
}
```

**핵심 포인트:**

- `viewModelScope.launch`로 비동기 실행
- StateFlow로 UI 상태 관리
- 로딩 → AI 추론 → 결과 표시 흐름

<br/>

---

## 🚀 빌드 & 실행 가이드

### 1. 사전 요구사항

- Android Studio Hedgehog (2023.1.1) 이상
- Android SDK 24 이상
- 물리 디바이스 (에뮬레이터는 AI 성능 저하 가능)

### 2. AI 모델 파일 다운로드

Gemma 모델을 [Kaggle](https://www.kaggle.com/models/google/gemma/tfLite/)
또는 [AI Edge](https://ai.google.dev/edge/mediapipe/solutions/genai/llm_inference) 페이지에서 다운로드하세요.

**추천 모델:**

- `gemma2-2b-it-gpu-int8.bin` (약 2.5GB)
- `gemma-3-270m-it-int8.task` (더 가벼운 버전, 약 400MB)

### 3. 모델 파일을 기기에 업로드

```bash
# 1. 모델 파일을 기기의 임시 디렉토리로 push
adb push gemma2-2b-it-gpu-int8.bin /data/local/tmp/

# 2. 파일 권한 설정
adb shell chmod 644 /data/local/tmp/gemma2-2b-it-gpu-int8.bin

# 3. 파일이 잘 들어갔는지 확인
adb shell ls -lh /data/local/tmp/
```

> **⚠️ 주의:** `/data/local/tmp/` 경로는 디버그 빌드에서만 접근 가능합니다.  
> 실제 배포 시에는 앱의 내부 저장소 (`context.filesDir`)로 복사하는 로직이 필요합니다.

### 4. 앱 빌드 & 실행

```bash
# Gradle 빌드
./gradlew assembleDebug

# 앱 설치
adb install -r app/build/outputs/apk/debug/app-debug.apk

# 실행
adb shell am start -n pilju.android.todaytarot/.MainActivity
```

또는 Android Studio에서 `Run` 버튼을 클릭하세요!

<br/>

---

## 📂 주요 파일 설명

### `TarotAiManager.kt` - AI 엔진 핵심

- MediaPipe LLM Inference API 래퍼 클래스
- 모델 초기화, 프롬프트 생성, 추론 실행 담당
- 78장의 타로 카드 덱 관리 (Major Arcana + 4 Suits)

### `TarotViewModel.kt` - 상태 관리

- `StateFlow`로 화면 상태 관리
- `INPUT` → `SELECTION` → `LOADING` → `RESULT` 플로우 제어
- AI 호출을 Coroutine으로 비동기 처리

### `MainActivity.kt` - 화면 전환

- Jetpack Compose로 UI 구성
- Navigation 라이브러리 없이 간단한 `when` 분기로 화면 전환
- Material3 테마 적용

### `InputScreen.kt` / `SelectScreen.kt` / `ResultScreen.kt`

- Compose UI 컴포넌트
- Material3 디자인 (둥근 버튼, 카드 그림자, 베이지 테마)
- 78장의 타로 카드 이미지를 동적으로 로드

<br/>

---

## 🎨 UI/UX 특징

- **컬러 테마**: 따뜻한 베이지 & 골드 컬러 (힐링 컨셉)
- **타이포그래피**: Material3 기본 폰트 + 미션 섹션에 Cursive 폰트
- **애니메이션**: 로딩 시 CircularProgressIndicator
- **다국어 지원**: 한국어(`values-ko`) 및 영어(`values`) 리소스 제공

<br/>

---

## 🧪 테스트 시나리오

### 정상 동작 테스트

1. 앱 실행 → "취업이 될까?" 입력
2. 카드 선택 → AI 추론 대기 (3~10초)
3. 결과 화면에서 카드 이미지, 해석, 미션 확인
4. "다시하기" 버튼 클릭 → 처음 화면으로 돌아가기

### 에러 핸들링 테스트

- **모델 파일 없음**: 더미 메시지 표시 (앱 크래시 방지)
- **AI 추론 실패**: Try-Catch로 에러 메시지 반환
- **빈 입력**: 버튼 비활성화 (실제로는 `isNotBlank()` 체크)

<br/>

---

## 🤔 FAQ

### Q1. 왜 `/data/local/tmp/`에 모델을 넣나요?

**A.** 샘플 앱이므로 간편하게 테스트하기 위한 방법입니다. 실제 배포 시에는:

- APK의 `assets/` 폴더에 모델 추가 필요함 (용량 주의!)
- 또는 앱 최초 실행 시 다운로드 후 내부 저장소에 저장

### Q2. 에뮬레이터에서도 동작하나요?

**A.** 동작은 하지만 **매우 느립니다**. GPU 가속이 필요한 모델이므로 실제 기기 권장.

### Q3. iOS 버전도 있나요?

**A.** 이 샘플은 Android 전용입니다. iOS에서는 Core ML이나 TensorFlow Lite를 사용해야 합니다.

### Q4. 다른 AI 모델로 교체 가능한가요?

**A.** MediaPipe가 지원하는 모델(Gemma, Phi-2 등)이라면 모델 경로만 바꾸면 됩니다.  
`TarotAiManager.kt`의 `modelPath` 변수를 수정하세요.

### Q5. 상용 앱에 사용해도 되나요?

**A.** 예! 단, 다음 사항을 고려하세요:

- Gemma 모델의 라이선스 확인 (Apache 2.0, 상업적 사용 가능)
- 모델 파일 크기로 인한 APK 용량 증가
- 기기 성능에 따른 추론 속도 차이

<br/>

---

## 📚 참고 자료

- [Google AI Edge - MediaPipe LLM Inference](https://ai.google.dev/edge/mediapipe/solutions/genai/llm_inference/android)
- [MediaPipe Tasks GenAI API Docs](https://developers.google.com/mediapipe/api/solutions/java/com/google/mediapipe/tasks/genai)
- [Gemma Model on Kaggle](https://www.kaggle.com/models/google/gemma)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)

<br/>

---

## 📄 라이선스

이 프로젝트는 **Apache 2.0 License** 하에 배포됩니다.  
타로 카드 이미지는 퍼블릭 도메인(Rider-Waite 덱)을 사용했습니다.

<br/>

---

## 👨‍💻 개발자

**pilju.bae**  
이 샘플 앱에 대한 질문이나 피드백은 이슈로 남겨주세요!

---

## 🌟 더 알아보기

### 온디바이스 AI의 장점

- ✅ **개인정보 보호**: 서버에 데이터를 보내지 않음
- ✅ **오프라인 동작**: 인터넷 연결 불필요
- ✅ **빠른 응답**: 네트워크 지연 없음
- ✅ **비용 절감**: 서버 운영 비용 제로

### 이 기술을 활용할 수 있는 분야

- 📝 일기 앱 (감정 분석)
- 🎓 언어 학습 앱 (문법 교정)
- 💬 채팅봇 (고객 지원)
- 🖼️ 이미지 캡션 생성
- 🎵 음악 추천 시스템

---

**Happy Coding! 🚀**
