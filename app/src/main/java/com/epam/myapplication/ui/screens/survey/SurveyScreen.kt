package com.epam.myapplication.ui.screens.survey

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.epam.myapplication.R
import com.epam.myapplication.mock.MockRepository
import com.epam.myapplication.ui.screens.survey.components.QuestionContent
import com.epam.myapplication.ui.screens.survey.components.SubmissionResultSnackbarVisuals
import com.epam.myapplication.ui.screens.survey.components.SubmissionSnackbarHost
import com.epam.myapplication.ui.screens.survey.components.TopBar
import com.epam.myapplication.ui.screens.survey.components.FullscreenLoadingIndicator
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SurveyScreen(
    navController: NavHostController,
    viewModel: SurveyViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.onIntent(SurveyIntent.LoadQuestions)
    }
    val state by viewModel.state.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    val snackbarScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { keyboardController?.hide() },
                )
        ) {
            TopBar(
                currentQuestionIndex = state.currentQuestionIndex,
                totalQuestions = state.questions.size,
                onPrevious = {
                    snackbarScope.coroutineContext.cancelChildren()
                    viewModel.onIntent(SurveyIntent.PreviousQuestion)
                },
                onNext = {
                    snackbarScope.coroutineContext.cancelChildren()
                    viewModel.onIntent(SurveyIntent.NextQuestion)
                },
                isPreviousEnabled = state.currentQuestionIndex > 0,
                isNextEnabled = state.currentQuestionIndex < state.questions.size - 1,
                onBack = navController::popBackStack
            )

            state.submissionResult?.getContentIfNotHandled()?.let {
                snackbarScope.launch {
                    snackbarHostState.showSnackbar(
                        visuals = SubmissionResultSnackbarVisuals(it)
                    )
                }
            }

            Scaffold { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    val currentQuestion = runCatching {
                        state.questions[state.currentQuestionIndex]
                    }.getOrNull()

                    val pagerState = rememberPagerState(pageCount = {
                        state.questions.size
                    }, initialPage = 0)

                    val pagerScope = rememberCoroutineScope()
                    pagerScope.launch {
                        pagerState.animateScrollToPage(state.currentQuestionIndex)
                    }

                    LaunchedEffect(pagerState) {
                        snapshotFlow { pagerState.currentPage }.collect { page ->
                            viewModel.onIntent(
                                SurveyIntent.OnNewQuestionVisible(
                                    page
                                )
                            )
                            keyboardController?.hide()
                            snackbarScope.coroutineContext.cancelChildren()
                        }
                    }

                    if (currentQuestion != null) {
                        Column {
                            Text(
                                text = stringResource(
                                    id = R.string.questions_submitted,
                                    state.submittedAnswersCount
                                ),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.padding(vertical = 20.dp)
                            ) { index ->
                                val question = state.questions[index]
                                QuestionContent(
                                    question = question,
                                    onAnswerChange = { answer ->
                                        viewModel.onIntent(
                                            SurveyIntent.UpdateAnswer(
                                                question.id,
                                                answer
                                            )
                                        )
                                    },
                                    onSubmit = {
                                        keyboardController?.hide()
                                        viewModel.onIntent(
                                            SurveyIntent.SubmitAnswer(
                                                question.id
                                            )
                                        )
                                    },
                                    isSubmitting = state.isLoading
                                )
                            }
                            Button(
                                onClick = {
                                    keyboardController?.hide()
                                    viewModel.onIntent(
                                        SurveyIntent.SubmitAnswer(
                                            currentQuestion.id
                                        )
                                    )
                                },
                                enabled = currentQuestion.answer.isNullOrBlank()
                                    .not() && !currentQuestion.isSubmitted && !state.isLoading,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                val text =
                                    if (currentQuestion.isSubmitted) stringResource(
                                        id = R.string.already_submitted
                                    ) else stringResource(id = R.string.submit)
                                Text(text)
                            }
                        }
                    }

                    SubmissionSnackbarHost(
                        snackbarHostState = snackbarHostState,
                        onRetry = { snackbarScope.coroutineContext.cancelChildren() }
                    )
                }
            }
        }
        if (state.isLoading && state.questions.isEmpty()) {
            FullscreenLoadingIndicator()
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewSurveyScreen() {
    val navController = rememberNavController()
    val viewModel = SurveyViewModel(MockRepository())
    SurveyScreen(navController, viewModel)
}