package com.example.maxlish.ui.screen.vocabulary.set.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maxlish.ui.component.DuoButton
import com.example.maxlish.ui.component.DuoCard
import com.example.maxlish.ui.component.DuoColors
import com.example.maxlish.ui.component.DuoProgressBar
import com.example.maxlish.ui.screen.home.model.VocabularySetUiModel

@Composable
fun VocabularySetListScreen(
    state: VocabularySetListState,
    onEvent: (VocabularySetListEvent) -> Unit
) {
    var deleteSetId by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(
        containerColor = DuoColors.Background,
        floatingActionButton = {
            // Nút bấm thêm bộ từ vựng dạng 3D tròn
            DuoCard(
                modifier = Modifier.size(56.dp),
                backgroundColor = DuoColors.Blue,
                borderColor = DuoColors.BlueDark,
                shadowHeight = 4.dp,
                shape = CircleShape,
                onClick = {
                    onEvent(VocabularySetListEvent.OnCreateSetClick)
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Thêm bộ từ",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = "Bộ Từ Vựng",
                        color = DuoColors.TextPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Bạn đang sở hữu ${state.vocabularySets.size} bộ từ vựng",
                        color = DuoColors.TextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                // Search field bo tròn lớn kiểu Duolingo
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = {
                        onEvent(VocabularySetListEvent.OnSearchChange(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Tìm kiếm bộ từ vựng...", fontWeight = FontWeight.Bold)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = DuoColors.TextSecondary
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DuoColors.White,
                        unfocusedContainerColor = DuoColors.White,
                        focusedBorderColor = DuoColors.Blue,
                        unfocusedBorderColor = DuoColors.Border
                    )
                )
            }

            items(state.vocabularySets) { set ->
                VocabularySetCard3D(
                    set = set,
                    onClick = {
                        onEvent(VocabularySetListEvent.OnSetClick(set.id))
                    },
                    onLearnClick = {
                        onEvent(VocabularySetListEvent.OnLearnClick(set.id))
                    },
                    onEditClick = {
                        onEvent(VocabularySetListEvent.OnEditClick(set.id))
                    },
                    onDeleteClick = {
                        deleteSetId = set.id
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        deleteSetId?.let { setId ->
            AlertDialog(
                onDismissRequest = {
                    deleteSetId = null
                },
                title = {
                    Text("Xóa bộ từ vựng?", fontWeight = FontWeight.ExtraBold, color = DuoColors.TextPrimary)
                },
                text = {
                    Text("Hành động này sẽ xóa vĩnh viễn bộ từ vựng của bạn và không thể hoàn tác.", fontWeight = FontWeight.Bold, color = DuoColors.TextSecondary)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onEvent(VocabularySetListEvent.OnDeleteClick(setId))
                            deleteSetId = null
                        }
                    ) {
                        Text("Xóa bỏ", color = DuoColors.Red, fontWeight = FontWeight.ExtraBold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            deleteSetId = null
                        }
                    ) {
                        Text("Hủy", color = DuoColors.TextSecondary, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

@Composable
fun VocabularySetCard3D(
    set: VocabularySetUiModel,
    onClick: () -> Unit,
    onLearnClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    // Phân bổ màu sắc sinh động dựa trên tag hoặc tên bộ từ vựng
    val (primaryColor, darkColor) = when {
        set.title.contains("IELTS", ignoreCase = true) -> 
            Pair(DuoColors.Purple, DuoColors.PurpleDark)
        set.title.contains("TOEIC", ignoreCase = true) -> 
            Pair(DuoColors.Green, DuoColors.GreenDark)
        else -> 
            Pair(DuoColors.Blue, DuoColors.BlueDark)
    }

    DuoCard(
        backgroundColor = DuoColors.White,
        borderColor = DuoColors.Border,
        shadowHeight = 4.dp,
        shape = RoundedCornerShape(24.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // HEADER ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(primaryColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = primaryColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // LEARN DuoButton 3D
                    DuoButton(
                        onClick = onLearnClick,
                        backgroundColor = primaryColor,
                        bottomColor = darkColor,
                        shape = RoundedCornerShape(12.dp),
                        shadowHeight = 3.dp,
                        modifier = Modifier
                            .width(80.dp)
                            .height(38.dp)
                    ) {
                        Text(
                            "Học",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // MORE MENU
                    Box {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable { showMenu = true }
                                .size(24.dp),
                            tint = DuoColors.TextSecondary
                        )

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Chỉnh sửa bộ từ", fontWeight = FontWeight.Bold, color = DuoColors.TextPrimary) },
                                onClick = {
                                    showMenu = false
                                    onEditClick()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Xóa bộ từ", fontWeight = FontWeight.Bold, color = DuoColors.Red) },
                                onClick = {
                                    showMenu = false
                                    onDeleteClick()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = set.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DuoColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${set.totalWords} từ vựng",
                color = DuoColors.TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3D Progress Bar
            DuoProgressBar(
                progress = set.progress,
                color = primaryColor,
                trackColor = DuoColors.Border.copy(alpha = 0.7f),
                height = 10.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Hoàn thành ${(set.progress * 100).toInt()}%",
                color = primaryColor,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp
            )
        }
    }
}