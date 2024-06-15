package com.epam.myapplication.ui.screens.survey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.epam.myapplication.common.Result
import com.epam.myapplication.common.UiDataEvent
import com.epam.myapplication.data.repository.Repository
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    private val _state = MutableStateFlow(
        SurveyState(
            questions = listOf(),
            isLoading = true
        )
    )

    val state: StateFlow<SurveyState> = _state.asStateFlow()

    fun onIntent(intent: SurveyIntent) {
        viewModelScope.launch {
            when (intent) {
                is SurveyIntent.LoadQuestions -> loadQuestions()
                is SurveyIntent.PreviousQuestion -> moveToPreviousQuestion()
                is SurveyIntent.NextQuestion -> moveToNextQuestion()
                is SurveyIntent.SubmitAnswer -> submitAnswer(intent.questionId)
                is SurveyIntent.UpdateAnswer -> updateAnswer(
                    intent.questionId,
                    intent.newAnswer
                )

                is SurveyIntent.OnNewQuestionVisible -> moveToPosition(intent.position)
            }
        }
    }

    private fun loadQuestions(){
        viewModelScope.launch {
            when (val result = repository.getQuestions()) {
                is Result.Success -> {
                    _state.update {
                        it.copy(questions = result.data, isLoading = false)
                    }
                }

                is Result.Failure -> {
                    _state.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }

    private fun moveToPreviousQuestion() {
        _state.update { state ->
            if (state.currentQuestionIndex > 0) {
                state.copy(currentQuestionIndex = state.currentQuestionIndex - 1)
            } else state
        }
    }

    private fun moveToNextQuestion() {
        _state.update { state ->
            if (state.currentQuestionIndex < state.questions.size - 1) {
                state.copy(currentQuestionIndex = state.currentQuestionIndex + 1)
            } else state
        }
    }

    private fun moveToPosition(position: Int) {
        if (position < 0 || position >= state.value.questions.size || position == state.value.currentQuestionIndex) return
        _state.update { state ->
            state.copy(currentQuestionIndex = position)
        }
    }

    private fun updateAnswer(questionId: Int, newAnswer: String) {
        _state.update { state ->
            val oldQuestion = state.questions.find { it.id == questionId }
            val position = state.questions.indexOf(oldQuestion)
            val newQuestion = oldQuestion?.copy(answer = newAnswer) ?: return
            val newQuestionList = state.questions.toMutableList().apply {
                remove(oldQuestion)
                add(position, newQuestion)
            }
            state.copy(questions = newQuestionList)
        }
    }

    private fun submitAnswer(questionId: Int) {
        val question =
            _state.value.questions.find { it.id == questionId } ?: return
        val currentQuestion =
            _state.value.questions[_state.value.currentQuestionIndex]
        if (currentQuestion.isSubmitted || question.answer.isNullOrBlank()) return

        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result =
                repository.submitAnswer(currentQuestion.id, question.answer)
            when (result) {
                is Result.Success -> {
                    _state.update { state ->
                        val updatedQuestions = state.questions.toMutableList()
                        updatedQuestions[state.currentQuestionIndex] =
                            currentQuestion.copy(isSubmitted = true)
                        state.copy(
                            questions = updatedQuestions,
                            submittedAnswersCount = state.submittedAnswersCount + 1,
                            isLoading = false,
                            submissionResult = UiDataEvent(SubmissionResult.Success)
                        )
                    }
                }

                is Result.Failure -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            submissionResult = UiDataEvent(SubmissionResult.Failure)
                        )
                    }
                }
            }
        }
    }
}