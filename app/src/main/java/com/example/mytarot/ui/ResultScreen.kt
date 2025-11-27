package com.example.mytarot.ui

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytarot.model.FortuneResult
import pilju.android.todaytarot.R
import pilju.android.todaytarot.ui.theme.BeigeBackground
import pilju.android.todaytarot.ui.theme.MissionYellow
import pilju.android.todaytarot.ui.theme.TextDark

@Composable
fun ResultScreen(result: FortuneResult) {

    val context = LocalContext.current

    // 1. 이미지 리소스 ID 찾기 (최적화를 위해 remember 사용)
    val imageResId = remember(result.cardName) {
        getCardImageId(context, result.cardName)
    }

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

        // 🎴 타로 카드 이미지 표시 영역
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .width(220.dp)
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = result.cardName,
                contentScale = ContentScale.Crop, // 이미지를 꽉 차게 자름
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 2. 응원 메시지 섹션
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.result_screen_worry_title),
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
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = stringResource(R.string.result_screen_mission_title),
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

/**
 * 🔍 도우미 함수: 문자열 이름으로 drawable ID 찾기
 * 예: "the_moon" -> R.drawable.the_moon (Int)
 */
@SuppressLint("DiscouragedApi")
fun getCardImageId(context: Context, cardName: String): Int {
    // 1. 혹시 모를 공백이나 대문자를 처리 (안전장치)
    val formattedName = cardName.lowercase().replace(" ", "_").trim()

    // 2. 리소스 ID 검색
    val resId = context.resources.getIdentifier(
        formattedName,
        "drawable",
        context.packageName
    )

    // 3. 파일이 있으면 그 ID 반환, 없으면(0) 카드 뒷면 반환 (앱 죽음 방지)
    // ⚠️ 주의: res/drawable 폴더에 'card.jpg' 파일이 꼭 있어야 합니다!
    return if (resId != 0) resId else R.drawable.card
}
