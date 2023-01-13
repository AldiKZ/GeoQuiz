package com.app.android.geoquiz

import Question
import androidx.lifecycle.ViewModel

private const val TAG = "QuizVeiwModel"
class QuizViewModel: ViewModel() {
    val questionBank = listOf(
        Question(R.string.question_africa, true, false),
        Question(R.string.question_australia, true, false),
        Question(R.string.question_mideast, false, false),
        Question(R.string.question_oceans, false, false)
    )
    var currentIndex = 0
    val currentQuestionAnswer: Boolean get() = questionBank[currentIndex].answer
    val currentQuestionText: Int get() = questionBank[currentIndex].textResId
    val currentQuestionResolved: Boolean get() = questionBank[currentIndex].resolved

    fun changeResolvedStatus(isResolved: Boolean) {
        questionBank[currentIndex].resolved = isResolved
    }
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
    fun moveToBack() {
        currentIndex = (currentIndex - 1)
        if (currentIndex < 0)
            currentIndex = questionBank.size - 1
        else
            currentIndex % questionBank.size
    }
}