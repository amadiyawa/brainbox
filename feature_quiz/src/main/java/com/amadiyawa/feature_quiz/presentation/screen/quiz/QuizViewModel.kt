package com.amadiyawa.feature_quiz.presentation.screen.quiz

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_base.presentation.viewmodel.BaseAction
import com.amadiyawa.feature_base.presentation.viewmodel.BaseState
import com.amadiyawa.feature_base.presentation.viewmodel.BaseViewModel
import com.amadiyawa.feature_quiz.domain.model.Question
import com.amadiyawa.feature_quiz.domain.usecase.GetQuestionListUseCase
import com.amadiyawa.feature_base.domain.result.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class QuizViewModel(
    private val getQuestionListUseCase: GetQuestionListUseCase
) : BaseViewModel<QuizViewModel.UiState, QuizViewModel.Action>(UiState.Loading) {

    private var job: Job? = null

    fun onEnter() {
        getQuestionList()
    }

    private fun getQuestionList() {
        if (job != null) {
            job?.cancel()
            job = null
        }

        job = viewModelScope.launch {
            getQuestionListUseCase().also { result ->
                val action = when (result) {
                    is Result.Success -> {
                        if (result.value.isEmpty()) {
                            Action.QuestionListLoadFailure
                        } else {
                            Action.QuestionListLoadSuccess(result.value)
                        }
                    }
                    is Result.Failure -> {
                        Action.QuestionListLoadFailure
                    }
                }
                sendAction(action)
            }
        }
    }

    internal sealed interface Action : BaseAction<UiState> {
        data class QuestionListLoadSuccess(private val questionList: List<Question>) : Action {
            override fun reduce(state: UiState) = UiState.Content(questionList)
        }

        data object QuestionListLoadFailure : Action {
            override fun reduce(state: UiState) = UiState.Error
        }
    }


    @Immutable
    internal sealed interface UiState : BaseState {
        data class Content(val questionList: List<Question>) : UiState
        data object Loading : UiState
        data object Error : UiState
    }
}