package com.example.maxlish.ui.screen.vocabulary.set.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

@Composable
fun VocabularySetDetailRoute(
    setId: String,
    onBack: () -> Unit,
    onNavigateToWordList: (String) -> Unit,
    onNavigateToAddWord: (String) -> Unit,
    onNavigateToLearn: (String) -> Unit,
    viewModel: VocabularySetDetailViewModel
) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var csvToExport by remember { mutableStateOf<String?>(null) }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val csvText = inputStream?.bufferedReader()?.use { it.readText() } ?: ""
                viewModel.importFromCsv(csvText)
            } catch (e: Exception) {
                // error is already handled or silent
            }
        }
    }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        if (uri != null && csvToExport != null) {
            try {
                val outputStream = context.contentResolver.openOutputStream(uri)
                outputStream?.bufferedWriter()?.use { it.write(csvToExport!!) }
            } catch (e: Exception) {
                // error is already handled or silent
            }
        }
    }

    LaunchedEffect(setId) {
        viewModel.load(setId)
    }

    VocabularySetDetailScreen(
        state = state,
        onEvent = { event ->
            when (event) {

                VocabularySetDetailEvent.OnBackClick -> {
                    onBack()
                }

                VocabularySetDetailEvent.OnLearnClick -> {

                    state.vocabularySet?.let {
                        onNavigateToLearn(it.setId)
                    }
                }

                VocabularySetDetailEvent.OnViewWordsClick -> {

                    state.vocabularySet?.let {
                        onNavigateToWordList(it.setId)
                    }
                }

                VocabularySetDetailEvent.OnAddWordClick -> {

                    state.vocabularySet?.let {
                        onNavigateToAddWord(it.setId)
                    }
                }

                VocabularySetDetailEvent.OnImportCsvClick -> {
                    importLauncher.launch("*/*")
                }

                VocabularySetDetailEvent.OnExportCsvClick -> {
                    viewModel.exportToCsv { csvString ->
                        if (csvString != null) {
                            csvToExport = csvString
                            val filename = "${state.vocabularySet?.title ?: "vocab_set"}.csv"
                            exportLauncher.launch(filename)
                        }
                    }
                }
            }
        }
    )
}