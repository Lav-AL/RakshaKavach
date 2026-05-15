package com.rakshakavach.app.ui.quiz

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.rakshakavach.app.R
import com.rakshakavach.app.data.model.QuizQuestion
import com.rakshakavach.app.data.model.QuizRepository
import com.rakshakavach.app.databinding.ActivitySafetyQuizBinding
import com.rakshakavach.app.viewmodel.MainViewModel

class SafetyQuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySafetyQuizBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var questions: List<QuizQuestion>
    private var currentIndex = 0
    private var score = 0
    private var answered = false
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySafetyQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        questions = QuizRepository.getRandomQuestions(5)
        loadQuestion()
    }

    private fun loadQuestion() {
        if (currentIndex >= questions.size) {
            showResults()
            return
        }

        answered = false
        val question = questions[currentIndex]

        // Reset UI
        resetOptionButtons()
        binding.tvExplanation.visibility = View.GONE
        binding.btnNextQuestion.visibility = View.GONE

        // Display question
        binding.tvQuestionNumber.text = "Question ${currentIndex + 1}/${questions.size}"
        binding.progressQuiz.progress = ((currentIndex.toFloat() / questions.size) * 100).toInt()
        binding.tvQuestion.text = question.question
        binding.tvScore.text = "Score: $score"

        // Set options
        val optionButtons = listOf(binding.btnOption0, binding.btnOption1, binding.btnOption2, binding.btnOption3)
        question.options.forEachIndexed { index, option ->
            optionButtons[index].text = "${('A' + index)}. $option"
            optionButtons[index].setOnClickListener { checkAnswer(index, question) }
        }

        // Start countdown timer (30 seconds)
        startTimer()
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                binding.tvTimer.text = "⏱️ $seconds s"
                binding.tvTimer.setTextColor(
                    if (seconds <= 10) getColor(R.color.risk_high)
                    else getColor(R.color.warning_yellow)
                )
            }
            override fun onFinish() {
                if (!answered) {
                    answered = true
                    val question = questions[currentIndex]
                    showCorrectAnswer(question.correctAnswerIndex, question)
                    binding.tvExplanation.visibility = View.VISIBLE
                    binding.tvExplanation.text = "⏰ Time's Up! ${question.explanation}"
                    binding.btnNextQuestion.visibility = View.VISIBLE
                }
            }
        }.start()
    }

    private fun checkAnswer(selectedIndex: Int, question: QuizQuestion) {
        if (answered) return
        answered = true
        countDownTimer?.cancel()

        val optionButtons = listOf(binding.btnOption0, binding.btnOption1, binding.btnOption2, binding.btnOption3)
        val isCorrect = selectedIndex == question.correctAnswerIndex

        if (isCorrect) {
            score++
            optionButtons[selectedIndex].setBackgroundColor(ContextCompat.getColor(this, R.color.success_green))
            Toast.makeText(this, "✅ Correct!", Toast.LENGTH_SHORT).show()
        } else {
            optionButtons[selectedIndex].setBackgroundColor(ContextCompat.getColor(this, R.color.risk_high))
            showCorrectAnswer(question.correctAnswerIndex, question)
        }

        binding.tvExplanation.text = "💡 ${question.explanation}"
        binding.tvExplanation.visibility = View.VISIBLE
        binding.btnNextQuestion.visibility = View.VISIBLE
        binding.btnNextQuestion.setOnClickListener {
            currentIndex++
            loadQuestion()
        }
    }

    private fun showCorrectAnswer(correctIndex: Int, question: QuizQuestion) {
        val optionButtons = listOf(binding.btnOption0, binding.btnOption1, binding.btnOption2, binding.btnOption3)
        optionButtons[correctIndex].setBackgroundColor(ContextCompat.getColor(this, R.color.success_green))
    }

    private fun resetOptionButtons() {
        val optionButtons = listOf(binding.btnOption0, binding.btnOption1, binding.btnOption2, binding.btnOption3)
        optionButtons.forEach { btn ->
            btn.setBackgroundColor(ContextCompat.getColor(this, R.color.option_default_bg))
            btn.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
    }

    private fun showResults() {
        countDownTimer?.cancel()
        binding.layoutQuiz.visibility = View.GONE
        binding.layoutResults.visibility = View.VISIBLE

        val percentage = (score.toFloat() / questions.size * 100).toInt()
        binding.tvFinalScore.text = "$score/${questions.size}"
        binding.tvFinalPercentage.text = "$percentage%"
        binding.progressFinalScore.progress = percentage

        val (message, color) = when {
            percentage >= 80 -> "🏆 SAFETY CHAMPION! Excellent knowledge!" to R.color.success_green
            percentage >= 60 -> "👍 GOOD JOB! Keep learning safety!" to R.color.warning_yellow
            percentage >= 40 -> "📚 NEEDS IMPROVEMENT! Study PPE rules!" to R.color.risk_medium
            else -> "⚠️ DANGER! Please attend safety training!" to R.color.risk_high
        }

        binding.tvResultMessage.text = message
        binding.tvResultMessage.setTextColor(ContextCompat.getColor(this, color))

        // Award points
        viewModel.addQuizPoints(score, questions.size)
        binding.tvPointsEarned.text = "+${(score.toFloat() / questions.size * 30).toInt()} Safety Points Earned!"

        binding.btnRetakeQuiz.setOnClickListener {
            questions = QuizRepository.getRandomQuestions(5)
            currentIndex = 0
            score = 0
            binding.layoutQuiz.visibility = View.VISIBLE
            binding.layoutResults.visibility = View.GONE
            loadQuestion()
        }

        binding.btnGoHome.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
