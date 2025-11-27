package com.example.mytarot.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pilju.android.todaytarot.R
import pilju.android.todaytarot.ui.theme.BeigeBackground
import pilju.android.todaytarot.ui.theme.TextDark

@Composable
fun SelectionScreen(onCardClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BeigeBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 카드 뒷면 이미지
            Image(
                painter = painterResource(id = R.drawable.card),
                contentDescription = "Tarot Card Back",
                modifier = Modifier
                    .width(200.dp)
                    .height(340.dp)
                    .clickable { onCardClick() }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Tap the card when you are ready.",
                color = TextDark,
                fontSize = 16.sp
            )
        }
    }
}