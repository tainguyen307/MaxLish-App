package com.example.maxlish.ui.screen.learning

import com.example.maxlish.data.model.LearningProgress
import com.example.maxlish.data.model.VocabularyWord

data class LearningState(
    val isLoading: Boolean = false,
    val setName: String = "",
    val mode: String = "all", // "all", "new", "review"
    
    // Learning queues
    val queue: List<VocabularyWord> = emptyList(),
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val progressMap: Map<String, LearningProgress> = emptyMap(),
    
    // Session states
    val isFinished: Boolean = false,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val xpEarned: Int = 0,
    val sessionStartTime: Long = 0L,
    val errorMessage: String? = null
) {
    val currentWord: VocabularyWord?
        get() = if (queue.isNotEmpty() && currentIndex in queue.indices) queue[currentIndex] else null
}
