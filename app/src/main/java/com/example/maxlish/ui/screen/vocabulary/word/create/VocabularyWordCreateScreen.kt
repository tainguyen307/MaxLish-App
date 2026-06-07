package com.example.maxlish.ui.screen.vocabulary.word.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maxlish.ui.component.DuoButton
import com.example.maxlish.ui.component.DuoCard
import com.example.maxlish.ui.component.DuoColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VocabularyWordCreateScreen(
    state: VocabularyWordCreateState,
    onEvent: (VocabularyWordCreateEvent) -> Unit
) {
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
                        onClick = { onEvent(VocabularyWordCreateEvent.OnBackClick) },
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
                        text = if (state.isEditMode) "Chỉnh sửa từ vựng" else "Tạo từ vựng mới",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoColors.TextPrimary
                    )
                }
            }

            // Main fields card
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
                    // Word field
                    DuoTextField(
                        value = state.word,
                        onValueChange = { onEvent(VocabularyWordCreateEvent.OnWordChange(it)) },
                        label = "Từ vựng (Tiếng Anh)",
                        placeholder = "Nhập từ tiếng Anh..."
                    )

                    // Pronunciation field
                    DuoTextField(
                        value = state.pronunciation,
                        onValueChange = { onEvent(VocabularyWordCreateEvent.OnPronunciationChange(it)) },
                        label = "Phiên âm",
                        placeholder = "e.g. /haʊ/"
                    )

                    // Meaning field
                    DuoTextField(
                        value = state.meaning,
                        onValueChange = { onEvent(VocabularyWordCreateEvent.OnMeaningChange(it)) },
                        label = "Định nghĩa / Nghĩa tiếng Việt",
                        placeholder = "Nghĩa của từ..."
                    )

                    // Description field
                    DuoTextField(
                        value = state.description,
                        onValueChange = { onEvent(VocabularyWordCreateEvent.OnDescriptionChange(it)) },
                        label = "Định nghĩa tiếng Anh",
                        placeholder = "English definition...",
                        singleLine = false,
                        minLines = 2
                    )

                    // Example field
                    DuoTextField(
                        value = state.example,
                        onValueChange = { onEvent(VocabularyWordCreateEvent.OnExampleChange(it)) },
                        label = "Ví dụ thực tế",
                        placeholder = "Example sentence...",
                        singleLine = false,
                        minLines = 2
                    )
                }
            }

            // Advanced fields card
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
                    // Difficulty Selector
                    DifficultySelector(
                        selectedDifficulty = state.difficulty,
                        onDifficultySelected = { onEvent(VocabularyWordCreateEvent.OnDifficultyChange(it)) }
                    )

                    HorizontalDivider(color = DuoColors.Border.copy(alpha = 0.5f))

                    // Collocations — Inline Chip Input
                    InlineChipTextField(
                        title = "Cụm từ đi kèm (Collocations)",
                        inputValue = state.collocationInput,
                        onInputValueChange = { onEvent(VocabularyWordCreateEvent.OnCollocationInputChange(it)) },
                        onAddChip = { onEvent(VocabularyWordCreateEvent.OnAddCollocation) },
                        items = state.collocations,
                        onRemoveItem = { onEvent(VocabularyWordCreateEvent.OnRemoveCollocation(it)) },
                        placeholder = "Ví dụ: make a decision, take action..."
                    )

                    HorizontalDivider(color = DuoColors.Border.copy(alpha = 0.5f))

                    // Related Words — Inline Chip Input
                    InlineChipTextField(
                        title = "Từ liên quan",
                        inputValue = state.relatedWordInput,
                        onInputValueChange = { onEvent(VocabularyWordCreateEvent.OnRelatedWordInputChange(it)) },
                        onAddChip = { onEvent(VocabularyWordCreateEvent.OnAddRelatedWord) },
                        items = state.relatedWords,
                        onRemoveItem = { onEvent(VocabularyWordCreateEvent.OnRemoveRelatedWord(it)) },
                        placeholder = "Ví dụ: synonym, antonym..."
                    )

                    HorizontalDivider(color = DuoColors.Border.copy(alpha = 0.5f))

                    // Personal Note
                    DuoTextField(
                        value = state.note,
                        onValueChange = { onEvent(VocabularyWordCreateEvent.OnNoteChange(it)) },
                        label = "Ghi chú cá nhân",
                        placeholder = "Nhập ghi chú của bạn...",
                        singleLine = false,
                        minLines = 2
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

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Save DuoButton Green
                DuoButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onEvent(VocabularyWordCreateEvent.OnSaveClick) },
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

                // Back/Cancel DuoButton White
                DuoButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onEvent(VocabularyWordCreateEvent.OnBackClick) },
                    backgroundColor = DuoColors.White,
                    bottomColor = DuoColors.WhiteDark
                ) {
                    Text(
                        text = "Hủy bỏ",
                        color = DuoColors.TextPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun DuoTextField(
    value: String,
    onValueChange: (String) -> Unit,
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

@Composable
fun DifficultySelector(
    selectedDifficulty: String,
    onDifficultySelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Mức độ khó".uppercase(),
            color = DuoColors.TextSecondary,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val options = listOf(
                Triple("Easy", "DỄ", DuoColors.Green),
                Triple("Medium", "TRUNG BÌNH", DuoColors.Orange),
                Triple("Hard", "KHÓ", DuoColors.Red)
            )
            options.forEach { (value, label, color) ->
                val isSelected = selectedDifficulty == value
                val displayColor = if (isSelected) color else DuoColors.White
                val shadowColor = if (isSelected) {
                    when (value) {
                        "Easy" -> DuoColors.GreenDark
                        "Hard" -> DuoColors.RedDark
                        else -> DuoColors.OrangeDark
                    }
                } else DuoColors.Border

                val textColor = if (isSelected) Color.White else DuoColors.TextPrimary

                DuoButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onDifficultySelected(value) },
                    backgroundColor = displayColor,
                    bottomColor = shadowColor,
                    shape = RoundedCornerShape(14.dp),
                    shadowHeight = 3.dp
                ) {
                    Text(
                        text = label,
                        color = textColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

// =============================================================
// INLINE CHIP TEXT FIELD
// Gõ Enter hoặc dấu phẩy để tự động thêm chip — không cần nút
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

        // Hint
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