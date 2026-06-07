package com.example.maxlish.ui.screen.vocabulary.set.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.maxlish.ui.component.DuoButton
import com.example.maxlish.ui.component.DuoCard
import com.example.maxlish.ui.component.DuoColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VocabularySetCreateScreen(
    state: VocabularySetCreateState,
    onEvent: (VocabularySetCreateEvent) -> Unit
) {
    var localTitle by remember { mutableStateOf(TextFieldValue(state.title)) }
    var localDesc by remember { mutableStateOf(TextFieldValue(state.description)) }

    LaunchedEffect(state.title) {
        if (state.title != localTitle.text) {
            localTitle = TextFieldValue(state.title)
        }
    }

    LaunchedEffect(state.description) {
        if (state.description != localDesc.text) {
            localDesc = TextFieldValue(state.description)
        }
    }

    Scaffold(
        containerColor = DuoColors.Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header card
            DuoCard(
                backgroundColor = DuoColors.White,
                borderColor = DuoColors.Border,
                shadowHeight = 4.dp,
                shape = RoundedCornerShape(26.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { onEvent(VocabularySetCreateEvent.OnBackClick) },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = DuoColors.TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = if (state.isEditMode == true) "Chỉnh sửa bộ từ" else "Tạo bộ từ mới",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoColors.TextPrimary
                    )
                }
            }

            // Main info card
            DuoCard(
                backgroundColor = DuoColors.White,
                borderColor = DuoColors.Border,
                shadowHeight = 4.dp,
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Deck name
                    DuoTextField(
                        value = localTitle,
                        onValueChange = {
                            localTitle = it
                            onEvent(VocabularySetCreateEvent.OnTitleChange(it.text))
                        },
                        label = "Tên bộ từ",
                        placeholder = "Ví dụ: Từ vựng giao tiếp cơ bản"
                    )

                    // Description
                    DuoTextField(
                        value = localDesc,
                        onValueChange = {
                            localDesc = it
                            onEvent(VocabularySetCreateEvent.OnDescriptionChange(it.text))
                        },
                        label = "Mô tả",
                        placeholder = "Giải thích những từ vựng bộ từ này bao gồm...",
                        singleLine = false,
                        minLines = 3
                    )
                }
            }

            // Tags card
            DuoCard(
                backgroundColor = DuoColors.White,
                borderColor = DuoColors.Border,
                shadowHeight = 4.dp,
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "NHÃN (TAGS)",
                        color = DuoColors.TextSecondary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = state.tagInput,
                            onValueChange = {
                                onEvent(VocabularySetCreateEvent.OnTagInputChange(it))
                            },
                            placeholder = {
                                Text(
                                    text = "Thêm nhãn...",
                                    color = DuoColors.TextSecondary.copy(alpha = 0.6f),
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DuoColors.Blue,
                                unfocusedBorderColor = DuoColors.Border,
                                focusedContainerColor = DuoColors.White,
                                unfocusedContainerColor = DuoColors.White,
                                cursorColor = DuoColors.Blue,
                                focusedTextColor = DuoColors.TextPrimary,
                                unfocusedTextColor = DuoColors.TextPrimary
                            ),
                            textStyle = LocalTextStyle.current.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )

                        DuoButton(
                            onClick = { onEvent(VocabularySetCreateEvent.OnAddTag) },
                            backgroundColor = DuoColors.Blue,
                            bottomColor = DuoColors.BlueDark,
                            shape = RoundedCornerShape(14.dp),
                            shadowHeight = 3.dp
                        ) {
                            Text(
                                text = "Thêm",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    if (state.selectedTags.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            state.selectedTags.forEach { tag ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(DuoColors.Border.copy(alpha = 0.7f))
                                        .clickable { onEvent(VocabularySetCreateEvent.OnRemoveTag(tag)) }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = tag,
                                            color = DuoColors.TextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Text(
                                            text = "×",
                                            color = DuoColors.TextSecondary,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            state.errorMessage?.let {
                Text(
                    text = it,
                    color = DuoColors.Red,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = DuoColors.Blue)
                }
            }

            // Save button
            DuoButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEvent(VocabularySetCreateEvent.OnCreateClick) },
                backgroundColor = DuoColors.Green,
                bottomColor = DuoColors.GreenDark
            ) {
                Text(
                    text = "Lưu lại",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun DuoTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            color = DuoColors.TextSecondary,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = DuoColors.TextSecondary.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Bold
                )
            },
            singleLine = singleLine,
            minLines = minLines,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DuoColors.Blue,
                unfocusedBorderColor = DuoColors.Border,
                focusedContainerColor = DuoColors.White,
                unfocusedContainerColor = DuoColors.White,
                cursorColor = DuoColors.Blue,
                focusedTextColor = DuoColors.TextPrimary,
                unfocusedTextColor = DuoColors.TextPrimary
            ),
            textStyle = LocalTextStyle.current.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )
    }
}