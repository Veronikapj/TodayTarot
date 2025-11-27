package com.example.mytarot.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pilju.android.todaytarot.R
import pilju.android.todaytarot.ui.theme.BeigeBackground
import pilju.android.todaytarot.ui.theme.PrimaryYellow
import pilju.android.todaytarot.ui.theme.TextDark
import pilju.android.todaytarot.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    worry: String,
    onWorryChange: (String) -> Unit,
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BeigeBackground)
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.input_screen_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            lineHeight = 36.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = worry,
            onValueChange = onWorryChange,
            placeholder = { Text(stringResource(R.string.input_screen_placeholder)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(24.dp), // 둥근 모서리
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.5f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                unfocusedBorderColor = TextGray.copy(alpha = 0.5f),
                focusedBorderColor = TextGray
            )
        )

        Text(
            text = stringResource(R.string.input_screen_disclaimer),
            color = TextGray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 12.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryYellow),
            shape = RoundedCornerShape(50) // 완전 둥근 버튼
        ) {
            Text(stringResource(R.string.input_screen_button), color = TextDark, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
