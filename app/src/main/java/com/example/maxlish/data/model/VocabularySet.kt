package com.example.maxlish.data.model

data class VocabularySet(

    val setId: String = "",
    val ownerId: String = "",

    val title: String = "",
    val description: String = "",

    val tags: List<String> = emptyList(),

    val wordCount: Int = 0,

    val isPublic: Boolean = true,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)