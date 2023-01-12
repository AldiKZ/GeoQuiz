package com.app.android.geoquiz

import Question
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var backButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_africa, true, false),
        Question(R.string.question_australia, true, false),
        Question(R.string.question_mideast, false, false),
        Question(R.string.question_oceans, false, false)
    )
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate(Bundle?) called")
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        backButton = findViewById(R.id.back_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)

        updateQuestion()
        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            questionBank[currentIndex].resolved = true
            trueButton.setEnabled(false)
            falseButton.setEnabled(false)
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            questionBank[currentIndex].resolved = true
            trueButton.setEnabled(false)
            falseButton.setEnabled(false)
        }
        backButton.setOnClickListener {
            view: View ->
            currentIndex = (currentIndex - 1)
            if (currentIndex < 0)
                currentIndex = questionBank.size - 1
            else
                currentIndex % questionBank.size
            updateQuestion()
        }
        nextButton.setOnClickListener {
            view: View ->
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
    }
    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
        if (questionBank[currentIndex].resolved == true) {
            trueButton.setEnabled(false)
            falseButton.setEnabled(false)
        }
    }
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        questionBank[currentIndex].resolved = true
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart(Bundle?) called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume(Bundle?) called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause(Bundle?) called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop(Bundle?) called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy(Bundle?) called")
    }
}