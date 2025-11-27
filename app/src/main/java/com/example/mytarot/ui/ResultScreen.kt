package com.example.mytarot.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytarot.model.FortuneResult
import pilju.android.todaytarot.ui.theme.BeigeBackground
import pilju.android.todaytarot.ui.theme.MissionYellow
import pilju.android.todaytarot.ui.theme.TextDark

@Composable
fun ResultScreen(result: FortuneResult) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BeigeBackground)
            .verticalScroll(rememberScrollState()) // 스크롤 가능하게
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // 1. 타로 카드 이미지 (The Sun)
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.width(220.dp).height(360.dp)
        ) {
            // res/drawable 에 'tarot_sun.jpg' 같은 이미지가 있어야 합니다.
            // 임시로 배경색만 채웁니다. 이미지가 있다면 Image 컴포넌트 사용하세요.
            Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
                // Image(painter = painterResource(id = R.drawable.the_sun), ...)
                Text(text = "THE SUN", modifier = Modifier.align(Alignment.Center))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 2. 응원 메시지 섹션
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "For your worry...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = result.cardDescription,
                fontSize = 16.sp,
                color = TextDark,
                lineHeight = 24.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 3. 오늘의 미션 (노란색 포스트잇)
        Card(
            colors = CardDefaults.cardColors(containerColor = MissionYellow),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "⭐ Today's Lucky Mission",
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = result.mission,
                    fontSize = 20.sp,
                    color = TextDark,
                    fontFamily = FontFamily.Cursive // 필기체 느낌 (기본 제공 폰트)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
