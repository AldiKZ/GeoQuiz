package com.app.android.geoquiz

import Question
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    // Ленивая инициализация (см. заметки)
    private val quizViewModel: QuizViewModel by lazy {
        // Связываем текущую активность и ViewModel
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var backButton: Button
    private lateinit var nextButton: Button
    private lateinit var replayButton: ImageButton
    private lateinit var questionTextView: TextView


    private var numbResponse = 0
    private var correctResponse = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate(Bundle?) called")

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        backButton = findViewById(R.id.back_button)
        nextButton = findViewById(R.id.next_button)
        replayButton = findViewById(R.id.replay_button)
        questionTextView = findViewById(R.id.question_text_view)

        replayButton.setVisibility(View.INVISIBLE)
        updateQuestion()
        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            quizViewModel.changeResolvedStatus(true)
            isResolved()
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            quizViewModel.changeResolvedStatus(true)
            isResolved()
        }
        backButton.setOnClickListener {
            view: View ->
            quizViewModel.currentIndex = (quizViewModel.currentIndex - 1)
            if (quizViewModel.currentIndex < 0)
                quizViewModel.currentIndex = quizViewModel.questionBank.size - 1
            else
                quizViewModel.currentIndex % quizViewModel.questionBank.size
            isResolved()
            updateQuestion()
        }
        nextButton.setOnClickListener {
            view: View ->
            quizViewModel.moveToNext()
            isResolved()
            updateQuestion()
        }
        replayButton.setOnClickListener {
            view: View ->
            quizViewModel.currentIndex = 0
            numbResponse = 0
            correctResponse = 0
            nextButton.setEnabled(true)
            backButton.setEnabled(true)
            for (question in quizViewModel.questionBank) {
                question.resolved = false
            }
            isResolved()
            updateQuestion()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun isResolved() {
        if (quizViewModel.currentQuestionResolved == true) {
            trueButton.setEnabled(false)
            falseButton.setEnabled(false)
            numbResponse++
            if (numbResponse == quizViewModel.questionBank.size){
                nextButton.setEnabled(false)
                backButton.setEnabled(false)
                val text:String = "Game over. You answered correctly $correctResponse/$numbResponse"
                questionTextView.setText(text)
                replayButton.setVisibility(View.VISIBLE)
            }
        } else {
            falseButton.setEnabled(true)
            trueButton.setEnabled(true)
        }

    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId: Int
        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            correctResponse++
        } else {
            messageResId = R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        quizViewModel.changeResolvedStatus(true)
    }

}