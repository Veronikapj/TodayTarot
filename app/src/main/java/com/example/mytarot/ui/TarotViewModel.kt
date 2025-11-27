package com.example.mytarot.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytarot.ai.TarotAiManager
import com.example.mytarot.model.FortuneResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 화면 단계 정의
enum class ScreenState { INPUT, SELECTION, LOADING, RESULT }

class TarotViewModel(application: Application) : AndroidViewModel(application) {
    private val aiManager = TarotAiManager(application.applicationContext)

    private val _screenState = MutableStateFlow(ScreenState.INPUT)
    val screenState = _screenState.asStateFlow()

    private val _worryText = MutableStateFlow("")
    val worryText = _worryText.asStateFlow()

    private val _fortuneResult = MutableStateFlow<FortuneResult?>(null)
    val fortuneResult = _fortuneResult.asStateFlow()

    fun updateWorry(text: String) {
        _worryText.value = text
    }

    fun goToSelection() {
        if (_worryText.value.isNotBlank()) {
            _screenState.value = ScreenState.SELECTION
        }
    }

    fun pickCard() {
        viewModelScope.launch {
            _screenState.value = ScreenState.LOADING
            val result = aiManager.getFortune(_worryText.value)
            _fortuneResult.value = result
            _screenState.value = ScreenState.RESULT
        }
    }

    fun reset() {
        _worryText.value = ""
        _screenState.value = ScreenState.INPUT
    }
}