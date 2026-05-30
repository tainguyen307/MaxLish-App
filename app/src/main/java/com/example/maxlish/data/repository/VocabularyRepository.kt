package com.example.maxlish.data.repository

import com.example.maxlish.data.model.VocabularySet
import com.example.maxlish.data.model.VocabularyWord

interface VocabularyRepository {

    // ======================
    // SET
    // ======================

    suspend fun getVocabularySets(
        ownerId: String
    ): Result<List<VocabularySet>>

    suspend fun getVocabularySetById(
        setId: String
    ): Result<VocabularySet>

    suspend fun createVocabularySet(
        vocabularySet: VocabularySet
    ): Result<String> // return setId

    suspend fun updateVocabularySet(
        vocabularySet: VocabularySet
    ): Result<Unit>

    suspend fun deleteVocabularySet(
        setId: String
    ): Result<Unit>

    // ======================
    // WORD
    // ======================

    suspend fun getWordsBySetId(
        setId: String
    ): Result<List<VocabularyWord>>

    suspend fun getWordById(
        setId: String,
        wordId: String
    ): Result<VocabularyWord>

    suspend fun createWord(
        setId: String,
        word: VocabularyWord
    ): Result<String> // return wordId

    suspend fun updateWord(
        setId: String,
        word: VocabularyWord
    ): Result<Unit>

    suspend fun deleteWord(
        setId: String,
        wordId: String
    ): Result<Unit>
}