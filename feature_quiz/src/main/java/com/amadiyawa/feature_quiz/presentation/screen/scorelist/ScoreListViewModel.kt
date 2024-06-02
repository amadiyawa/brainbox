package com.amadiyawa.feature_quiz.presentation.screen.scorelist

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_base.presentation.viewmodel.BaseAction
import com.amadiyawa.feature_base.presentation.viewmodel.BaseState
import com.amadiyawa.feature_base.presentation.viewmodel.BaseViewModel
import com.amadiyawa.feature_quiz.domain.model.Player
import com.amadiyawa.feature_quiz.domain.usecase.GetPlayerListUseCase
import kotlinx.coroutines.launch
import com.amadiyawa.feature_base.domain.result.Result
import kotlinx.coroutines.Job

internal class ScoreListViewModel(
    private val getPlayerListUseCase: GetPlayerListUseCase
) : BaseViewModel<ScoreListViewModel.UiState, ScoreListViewModel.Action>(UiState.Loading) {

    private var job: Job? = null

    fun onEnter() {
        getPlayerList()
    }

    private fun getPlayerList() {
        if (job != null) {
            job?.cancel()
            job = null
        }

        job = viewModelScope.launch {
            getPlayerListUseCase().also { result ->
                val action = when (result) {
                    is Result.Success -> {
                        if (result.value.isEmpty()) {
                            Action.PlayerListLoadEmpty
                        } else {
                            Action.PlayerListLoadSuccess(
                                playerList = result.value
                            )
                        }
                    }
                    is Result.Failure -> {
                        Action.PlayerListLoadFailure
                    }
                }
                sendAction(action)
            }
        }
    }

    internal sealed interface Action : BaseAction<UiState> {
        data class PlayerListLoadSuccess(
            val playerList: List<Player>
        ) : Action {
            override fun reduce(state: UiState) = UiState.PlayerList(
                playerList = playerList
            )
        }

        data object PlayerListLoadFailure : Action {
            override fun reduce(state: UiState) = UiState.Error
        }

        data object PlayerListLoadEmpty : Action {
            override fun reduce(state: UiState) = UiState.Empty
        }
    }

    @Immutable
    internal sealed interface UiState : BaseState {
        data class PlayerList(
            val playerList: List<Player>
        ) : UiState
        data object Empty : UiState
        data object Loading : UiState
        data object Error : UiState
    }
}