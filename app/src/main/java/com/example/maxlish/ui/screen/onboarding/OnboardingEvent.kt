package com.example.maxlish.ui.screen.onboarding

import com.example.maxlish.data.model.LearningGoal
import com.example.maxlish.data.model.UserLevel

sealed class OnboardingEvent {
    data class GoalSelected(val goal: LearningGoal) : OnboardingEvent()
    data class LevelSelected(val level: UserLevel) : OnboardingEvent()
    data class DailyWordsSelected(val count: Int) : OnboardingEvent()
    object NextStep : OnboardingEvent()
    object PrevStep : OnboardingEvent()
    object Finish : OnboardingEvent()
}
