package com.amadiyawa.feature_quiz.presentation.screen.quiz

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.amadiyawa.feature_base.domain.result.Result
import com.amadiyawa.feature_base.presentation.viewmodel.BaseAction
import com.amadiyawa.feature_base.presentation.viewmodel.BaseState
import com.amadiyawa.feature_base.presentation.viewmodel.BaseViewModel
import com.amadiyawa.feature_quiz.R
import com.amadiyawa.feature_quiz.domain.model.Player
import com.amadiyawa.feature_quiz.domain.model.Question
import com.amadiyawa.feature_quiz.domain.model.Score
import com.amadiyawa.feature_quiz.domain.usecase.CreatePlayerUseCase
import com.amadiyawa.feature_quiz.domain.usecase.GetPlayerByFullNameUseCase
import com.amadiyawa.feature_quiz.domain.usecase.GetQuizUseCase
import com.amadiyawa.feature_quiz.domain.usecase.UpdatePlayerUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

internal class QuizViewModel(
    private val getQuizUseCase: GetQuizUseCase,
    private val getPlayerByFullNameUseCase: GetPlayerByFullNameUseCase,
    private val createPlayerUseCase: CreatePlayerUseCase,
    private val updatePlayerUseCase: UpdatePlayerUseCase
) : BaseViewModel<QuizViewModel.UiState, QuizViewModel.Action>(UiState.Loading) {

    private var job: Job? = null
    private var timerJob: Job? = null
    private var savePlayerJob: Job? = null
    private var songPlayerJob: Job? = null

    private var _questionList = MutableStateFlow(emptyList<Question>())

    private var _playerName = MutableStateFlow("")
    val playerName = _playerName.asStateFlow()

    private var _currentSelectedOption = MutableStateFlow("")
    val currentSelectedOption = _currentSelectedOption.asStateFlow()

    private var _remainingTime = MutableStateFlow(10)
    val remainingTime = _remainingTime.asStateFlow()

    private var _soundToPlay = MutableStateFlow<Int?>(null)

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
                            _questionList.value = result.value
                            Action.QuestionListLoadSuccess
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
        if (currentState is UiState.PlayerName) {
            val action = Action.SetPlayerName(
                playerName = _playerName.value,
                questionList = _questionList.value
            )
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
            nextQuestion(
                isAnswerCorrect = false
            )
        }
    }

    fun onOptionSelected(option: String) {
        _currentSelectedOption.value = option
    }

    fun nextQuestion(
        isAnswerCorrect: Boolean? = null,
        context: Context? = null
    ) {
        if (isAnswerCorrect != null && context != null) {
            _soundToPlay.value = if (isAnswerCorrect) R.raw.correct_answer else R.raw.wrong_answer
            playSound(isAnswerCorrect, context)
        }

        val currentState = getCurrentState()
        if (currentState is UiState.Quiz) {
            val nextQuestionIndex = currentState.currentQuestionIndex + 1

            val action = Action.NextQuestion(
                currentQuestionIndex = nextQuestionIndex,
                currentSelectedOption = _currentSelectedOption.value
            )
            sendAction(action)
            _currentSelectedOption.value = ""
            _remainingTime.value = 10
            startTimer()
        }
    }

    private fun playSound(
        isAnswerCorrect: Boolean,
        context: Context
    ) {
        if (songPlayerJob != null) {
            songPlayerJob?.cancel()
            songPlayerJob = null
        }

        songPlayerJob = viewModelScope.launch {
            val soundResId = if (isAnswerCorrect) R.raw.correct_answer else R.raw.wrong_answer
            val mediaPlayer = MediaPlayer.create(
                context,
                soundResId
            )
            mediaPlayer.start()

            mediaPlayer.setOnCompletionListener { player ->
                player.release()
            }
        }
    }

    fun saveOrUpdatePlayer(playerName: String, points: Int) {
        // Cancel the previous job
        if (savePlayerJob != null) {
            savePlayerJob?.cancel()
            savePlayerJob = null
        }

        savePlayerJob = viewModelScope.launch {
            val playerResult = getPlayerByFullNameUseCase(playerName)
            if (playerResult is Result.Success) {
                val player = playerResult.value
                player.scoreList.add(Score(points))
                updatePlayerUseCase(
                    player.id!!,
                    player.scoreList
                )
            } else {
                val player = Player(
                    fullName = playerName,
                    scoreList = mutableListOf(Score(points))
                )
                createPlayerUseCase(player)
            }
        }

        val action = Action.QuestionListLoadSuccess
        sendAction(action)
    }

    internal sealed interface Action : BaseAction<UiState> {
        data object QuestionListLoadSuccess : Action {
            override fun reduce(state: UiState) = UiState.PlayerName
        }

        data object QuestionListLoadFailure : Action {
            override fun reduce(state: UiState) = UiState.Error
        }

        data class SetPlayerName(
            private val playerName: String,
            private val questionList: List<Question>
        ) : Action {
            override fun reduce(state: UiState): UiState {
                return when (state) {
                    is UiState.Quiz -> state.copy(playerName = playerName)
                    else -> UiState.Quiz(
                        questionList = questionList,
                        playerName = playerName
                    )
                }
            }
        }

        data class NextQuestion(
            private val currentQuestionIndex: Int,
            private val currentSelectedOption: String,
        ) : Action {
            override fun reduce(state: UiState): UiState {
                return when {
                    state !is UiState.Quiz -> state
                    (currentQuestionIndex >= state.questionList.size) ||
                    (state.currentQuestion.level == 1 &&
                    state.questionList[currentQuestionIndex + 1].level == 2 &&
                    state.points < 7) ||
                    (state.currentQuestion.level == 2 &&
                    state.questionList[currentQuestionIndex + 1].level == 3 &&
                    state.points < 17)
                    -> {
                        return UiState.GameOver(
                            playerName = state.playerName,
                            points = state.points,
                            totalPoints = state.questionList.size
                        )
                    }
                    else -> state.copy(
                        currentQuestionIndex = currentQuestionIndex,
                        points = if (currentSelectedOption.isNotEmpty() && state.currentQuestion.answer == currentSelectedOption.first().toString()) {
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
        data class Quiz(
            val questionList: List<Question>,
            val currentQuestionIndex: Int = 0,
            val playerName: String = "",
            val points: Int = 0
        ) : UiState {
            val currentQuestion: Question
                get() = questionList[currentQuestionIndex]
        }
        data class GameOver(
            val playerName: String,
            val points: Int,
            val totalPoints: Int
        ) : UiState
        data object PlayerName : UiState
        data object Loading : UiState
        data object Error : UiState
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        timerJob?.cancel()
        savePlayerJob?.cancel()
    }
}