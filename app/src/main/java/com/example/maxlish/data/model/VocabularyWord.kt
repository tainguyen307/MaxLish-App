package com.example.maxlish.data.model

data class VocabularyWord(

    val wordId: String = "",
    val setId: String = "",
    val ownerId: String = "",

    // Main
    val word: String = "",
    val pronunciation: String = "",
    val meaning: String = "",

    // Details
    val description: String = "",
    val example: String = "",

    // Learning Support
    val collocations: List<String> = emptyList(),
    val relatedWords: List<String> = emptyList(),

    // Extra
    val note: String = "",

    // Media
    val imageUrl: String = "",
    val audioUrl: String = "",

    // Metadata
    val difficulty: String = "Easy",

    // System
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)