package com.epam.myapplication

import app.cash.turbine.test
import com.epam.myapplication.data.repository.Repository
import com.epam.myapplication.ui.screens.survey.SubmissionResult
import com.epam.myapplication.ui.screens.survey.SurveyQuestion
import com.epam.myapplication.ui.screens.survey.SurveyViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.test.runTest
import com.epam.myapplication.common.Result
import com.epam.myapplication.ui.screens.survey.SurveyIntent
import ee.mriik.app.rules.MainDispatcherTestListener

class SurveyViewModelTest : StringSpec({
    val dispatcherListener = MainDispatcherTestListener()
    listener(dispatcherListener)
    val repository = mockk<Repository>()

    beforeTest {
        clearMocks(repository)
    }

    "loadQuestions should load questions successfully" {
        val questions = listOf(SurveyQuestion(1, "Question 1"), SurveyQuestion(2, "Question 2"))
        coEvery { repository.getQuestions() } returns Result.Success(questions)
        val viewModel = SurveyViewModel(repository)
        runTest {
            viewModel.state.test {
                viewModel.onIntent(SurveyIntent.LoadQuestions)
                val initial = awaitItem()
                initial.isLoading shouldBe true
                val loaded = awaitItem()
                loaded.questions shouldBe questions
                loaded.isLoading shouldBe false
            }
        }
    }

    "loadQuestions should handle failure" {
        coEvery { repository.getQuestions() } returns Result.Failure(Exception("Error"))
        val viewModel = SurveyViewModel(repository)
        runTest {
            viewModel.state.test {
                viewModel.onIntent(SurveyIntent.LoadQuestions)
                val initial = awaitItem()
                initial.isLoading shouldBe true
                val failed = awaitItem()
                failed.questions shouldBe emptyList()
                failed.isLoading shouldBe false
            }
        }
    }

    "moveToPreviousQuestion should decrease currentQuestionIndex" {
        val questions = listOf(SurveyQuestion(1, "Question 1"), SurveyQuestion(2, "Question 2"))
        coEvery { repository.getQuestions() } returns Result.Success(questions)
        val viewModel = SurveyViewModel(repository)
        runTest {
            viewModel.state.test {
                viewModel.onIntent(SurveyIntent.LoadQuestions)
                skipItems(2) // Skip initial and loaded states
                viewModel.onIntent(SurveyIntent.NextQuestion)
                val firstIndexState = awaitItem()
                firstIndexState.currentQuestionIndex shouldBe 1
                viewModel.onIntent(SurveyIntent.PreviousQuestion)
                val finalState = awaitItem()
                finalState.currentQuestionIndex shouldBe 0
            }
        }
    }

    "moveToNextQuestion should increase currentQuestionIndex" {
        val questions = listOf(SurveyQuestion(1, "Question 1"), SurveyQuestion(2, "Question 2"))
        coEvery { repository.getQuestions() } returns Result.Success(questions)
        val viewModel = SurveyViewModel(repository)
        runTest {
            viewModel.state.test {
                viewModel.onIntent(SurveyIntent.LoadQuestions)
                skipItems(2) // Skip initial and loaded states
                viewModel.onIntent(SurveyIntent.NextQuestion)
                val finalState = awaitItem()
                finalState.currentQuestionIndex shouldBe 1
            }
        }
    }

    "moveToPosition should update currentQuestionIndex to valid position" {
        val questions = listOf(SurveyQuestion(1, "Question 1"), SurveyQuestion(2, "Question 2"))
        coEvery { repository.getQuestions() } returns Result.Success(questions)
        val viewModel = SurveyViewModel(repository)
        runTest {
            viewModel.state.test {
                viewModel.onIntent(SurveyIntent.LoadQuestions)
                skipItems(2) // Skip initial and loaded states
                viewModel.onIntent(SurveyIntent.OnNewQuestionVisible(1))
                val finalState = awaitItem()
                finalState.currentQuestionIndex shouldBe 1
            }
        }
    }

    "updateAnswer should update answer of the given question" {
        val questions = listOf(SurveyQuestion(1, "Question 1"))
        coEvery { repository.getQuestions() } returns Result.Success(questions)
        val viewModel = SurveyViewModel(repository)
        runTest {
            viewModel.state.test {
                viewModel.onIntent(SurveyIntent.LoadQuestions)
                skipItems(2) // Skip initial and loaded states
                viewModel.onIntent(SurveyIntent.UpdateAnswer(1, "New Answer"))
                val finalState = awaitItem()
                finalState.questions[0].answer shouldBe "New Answer"
            }
        }
    }

    "submitAnswer should update question as submitted on success" {
        val question = SurveyQuestion(1, "Question 1", "Answer")
        coEvery { repository.getQuestions() } returns Result.Success(listOf(question))
        coEvery { repository.submitAnswer(any(), any()) } returns Result.Success(Unit)
        val viewModel = SurveyViewModel(repository)
        runTest {
            viewModel.state.test {
                viewModel.onIntent(SurveyIntent.LoadQuestions)
                skipItems(2) // Skip initial and loaded states
                viewModel.onIntent(SurveyIntent.SubmitAnswer(1))
                val submittingState = awaitItem()
                submittingState.isLoading shouldBe true
                val finalState = awaitItem()
                finalState.questions[0].isSubmitted shouldBe true
                finalState.submittedAnswersCount shouldBe 1
                finalState.isLoading shouldBe false
                finalState.submissionResult?.peekContent() shouldBe SubmissionResult.Success
            }
        }
    }

    "submitAnswer should handle failure" {
        val question = SurveyQuestion(1, "Question 1", "Answer")
        coEvery { repository.getQuestions() } returns Result.Success(listOf(question))
        coEvery { repository.submitAnswer(any(), any()) } returns Result.Failure(Exception("Error"))
        val viewModel = SurveyViewModel(repository)
        runTest {
            viewModel.state.test {
                viewModel.onIntent(SurveyIntent.LoadQuestions)
                skipItems(2) // Skip initial and loaded states
                viewModel.onIntent(SurveyIntent.SubmitAnswer(1))
                val submittingState = awaitItem()
                submittingState.isLoading shouldBe true
                val finalState = awaitItem()
                finalState.questions[0].isSubmitted shouldBe false
                finalState.isLoading shouldBe false
                finalState.submissionResult?.peekContent() shouldBe SubmissionResult.Failure
            }
        }
    }
})