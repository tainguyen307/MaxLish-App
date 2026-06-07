package com.example.maxlish.ui.screen.vocabulary.set.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

            // Tags card — InlineChipTextField
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
                    InlineChipTextField(
                        title = "Nhãn (Tags)",
                        inputValue = state.tagInput,
                        onInputValueChange = { onEvent(VocabularySetCreateEvent.OnTagInputChange(it)) },
                        onAddChip = { onEvent(VocabularySetCreateEvent.OnAddTag) },
                        items = state.selectedTags,
                        onRemoveItem = { onEvent(VocabularySetCreateEvent.OnRemoveTag(it)) },
                        placeholder = "Ví dụ: giao tiếp, IELTS, cơ bản..."
                    )
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

// =============================================================
// INLINE CHIP TEXT FIELD — dùng cho Tags, Collocations, Related
// Gõ Enter hoặc dấu phẩy để tự động thêm chip, không cần nút
// =============================================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InlineChipTextField(
    title: String,
    inputValue: String,
    onInputValueChange: (String) -> Unit,
    onAddChip: () -> Unit,
    items: List<String>,
    onRemoveItem: (String) -> Unit,
    placeholder: String = ""
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title.uppercase(),
            color = DuoColors.TextSecondary,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = inputValue,
            onValueChange = { newValue ->
                // Tự động thêm chip khi gõ dấu phẩy
                if (newValue.endsWith(",")) {
                    onInputValueChange(newValue.dropLast(1).trim())
                    onAddChip()
                } else {
                    onInputValueChange(newValue)
                }
            },
            placeholder = {
                Text(
                    text = placeholder,
                    color = DuoColors.TextSecondary.copy(alpha = 0.55f),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { onAddChip() }
            ),
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
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        )

        // Hint text
        Text(
            text = "Nhấn Enter hoặc gõ dấu phẩy để thêm",
            color = DuoColors.TextSecondary.copy(alpha = 0.6f),
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
        )

        // Chips
        if (items.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(DuoColors.Blue.copy(alpha = 0.12f)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item,
                            color = DuoColors.Blue,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 12.dp, top = 6.dp, bottom = 6.dp, end = 4.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Xóa",
                            tint = DuoColors.Blue.copy(alpha = 0.7f),
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .size(14.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .clickable { onRemoveItem(item) }
                        )
                    }
                }
            }
        }
    }
}