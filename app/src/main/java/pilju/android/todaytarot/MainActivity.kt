package pilju.android.todaytarot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytarot.ui.InputScreen
import com.example.mytarot.ui.ResultScreen
import com.example.mytarot.ui.ScreenState
import com.example.mytarot.ui.SelectionScreen
import com.example.mytarot.ui.TarotViewModel
import pilju.android.todaytarot.ui.theme.PrimaryYellow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: TarotViewModel = viewModel()
            val screenState by viewModel.screenState.collectAsState()
            val worry by viewModel.worryText.collectAsState()
            val result by viewModel.fortuneResult.collectAsState()

            // 간단한 화면 전환 로직 (Navigation 라이브러리 없이 구현)
            when (screenState) {
                ScreenState.INPUT -> {
                    InputScreen(
                        worry = worry,
                        onWorryChange = viewModel::updateWorry,
                        onNextClick = viewModel::goToSelection
                    )
                }
                ScreenState.SELECTION -> {
                    SelectionScreen(onCardClick = viewModel::pickCard)
                }
                ScreenState.LOADING -> {
                    // 로딩 화면 (간단하게)
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryYellow)
                    }
                }
                ScreenState.RESULT -> {
                    result?.let {
                        ResultScreen(result = it)
                    }
                }
            }
        }
    }
}