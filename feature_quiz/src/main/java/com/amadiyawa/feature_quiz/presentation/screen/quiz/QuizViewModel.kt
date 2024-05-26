package com.amadiyawa.feature_quiz.presentation.screen.quiz

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_base.domain.result.Result
import com.amadiyawa.feature_base.presentation.viewmodel.BaseAction
import com.amadiyawa.feature_base.presentation.viewmodel.BaseState
import com.amadiyawa.feature_base.presentation.viewmodel.BaseViewModel
import com.amadiyawa.feature_quiz.domain.enum.GameStatus
import com.amadiyawa.feature_quiz.domain.model.Player
import com.amadiyawa.feature_quiz.domain.model.Question
import com.amadiyawa.feature_quiz.domain.model.Score
import com.amadiyawa.feature_quiz.domain.usecase.GetPlayerByFullNameUseCase
import com.amadiyawa.feature_quiz.domain.usecase.GetQuizUseCase
import com.amadiyawa.feature_quiz.domain.usecase.SavePlayerUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

internal class QuizViewModel(
    private val getQuizUseCase: GetQuizUseCase,
    private val getPlayerByFullNameUseCase: GetPlayerByFullNameUseCase,
    private val savePlayerUseCase: SavePlayerUseCase
) : BaseViewModel<QuizViewModel.UiState, QuizViewModel.Action>(UiState.Loading) {

    private var job: Job? = null
    private var timerJob: Job? = null
    private var savePlayerJob: Job? = null

    private var _playerName = MutableStateFlow("")
    val playerName = _playerName.asStateFlow()

    private var _currentSelectedOption = MutableStateFlow("")
    val currentSelectedOption = _currentSelectedOption.asStateFlow()

    private var _remainingTime = MutableStateFlow(15)
    val remainingTime = _remainingTime.asStateFlow()

    fun onEnter() {
        getQuestionList()
    }

    private fun getQuestionList() {
        if (job != null) {
            job?.cancel()
            job = null
        }

        job = viewModelScope.launch {
            getQuizUseCase().also { result ->
                Timber.d("getQuestionListUseCase result: $result")
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

    fun onPlayerNameChanged(value: String) {
        _playerName.value = value
    }

    fun setPlayerName() {
        val currentState = getCurrentState()
        if (currentState is UiState.Content) {
            val action = Action.SetPlayerName(_playerName.value)
            sendAction(action)
        }
    }

    fun startTimer() {
        // Cancel the previous timer
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value--
            }
            nextQuestion()
        }
    }

    fun onOptionSelected(option: String) {
        _currentSelectedOption.value = option
    }

    fun nextQuestion() {
        val currentState = getCurrentState()
        if (currentState is UiState.Content) {
            val nextQuestionIndex = currentState.currentQuestionIndex + 1

            val action = Action.NextQuestion(
                currentQuestionIndex = nextQuestionIndex,
                currentSelectedOption = _currentSelectedOption.value
            )
            sendAction(action)
            _currentSelectedOption.value = ""
            _remainingTime.value = 15
            startTimer()

            if (currentState.gameStatus == GameStatus.FINISHED) {
                savePlayerJob = viewModelScope.launch {
                    saveOrUpdatePlayer(_playerName.value, currentState.points)
                }
            }
        }
    }

    private suspend fun saveOrUpdatePlayer(playerName: String, points: Int) {
        val playerResult = getPlayerByFullNameUseCase(playerName)
        if (playerResult is Result.Success) {
            // Update player's score list
            val updatedScoreList = playerResult.value.scoreList.toMutableList()
            updatedScoreList.add(Score(points)) // Assuming Score is a data class that takes an Int parameter
            val updatedPlayer = playerResult.value.copy(scoreList = updatedScoreList)
            savePlayerUseCase(updatedPlayer)
        } else {
            // Save new player
            val newPlayer = Player(fullName = playerName, scoreList = listOf(Score(points)))
            savePlayerUseCase(newPlayer)
        }
    }

    internal sealed interface Action : BaseAction<UiState> {
        data class QuestionListLoadSuccess(private val questionList: List<Question>) : Action {
            override fun reduce(state: UiState) = UiState.Content(
                questionList = questionList,
                gameStatus = GameStatus.ONGOING
            )
        }

        data object QuestionListLoadFailure : Action {
            override fun reduce(state: UiState) = UiState.Error
        }

        data class SetPlayerName(private val playerName: String) : Action {
            override fun reduce(state: UiState): UiState {
                return when (state) {
                    is UiState.Content -> state.copy(playerName = playerName)
                    else -> state
                }
            }
        }

        data class NextQuestion(
            private val currentQuestionIndex: Int,
            private val currentSelectedOption: String,
        ) : Action {
            override fun reduce(state: UiState): UiState {
                return when {
                    state !is UiState.Content -> state
                    (currentQuestionIndex >= state.questionList.size) ||
                    (state.currentQuestion.level == 1 &&
                    state.questionList[currentQuestionIndex + 1].level == 2 &&
                    state.points < 7) ||
                    (state.currentQuestion.level == 2 &&
                    state.questionList[currentQuestionIndex + 1].level == 3 &&
                    state.points < 17)
                    -> {
                        state.copy(
                            playerName = "",
                            currentQuestionIndex = 0,
                            gameStatus = GameStatus.FINISHED
                        )
                    }
                    else -> state.copy(
                        currentQuestionIndex = currentQuestionIndex,
                        points = if (state.currentQuestion.answer == currentSelectedOption) {
                            state.points + 1
                        } else {
                            state.points
                        }
                    )
                }
            }
        }
    }


    @Immutable
    internal sealed interface UiState : BaseState {
        data class Content(
            val questionList: List<Question>,
            val currentQuestionIndex: Int = 0,
            val playerName: String = "",
            val points: Int = 0,
            val gameStatus: GameStatus = GameStatus.ONGOING
        ) : UiState {
            val currentQuestion: Question
                get() = questionList[currentQuestionIndex]
        }
        data object Loading : UiState
        data object Error : UiState
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}