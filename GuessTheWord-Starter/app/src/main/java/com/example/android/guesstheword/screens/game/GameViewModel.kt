package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    private val timer: CountDownTimer
    companion object {
        // Time when the game is over
        private const val DONE = 0L

        //countdown time interval
        private const val ONE_SECOND = 1000L

        //Total time for the game
        private const val  COUNTDOWN_time = 60000L
    }

    //countdown time
    private val _currentTime = MutableLiveData<Long>()
    private val currentTime: LiveData<Long>
        get() = _currentTime

    //String version of the current time
    val currentTimeString: LiveData<String> = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }
    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word


    // The current score
     private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish
    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>
    init {
        _word.value = ""
        _score.value = 0
        resetList()
        nextWord()
        Log.i("GameViewModel", "GameViewModel created!")
        timer = object : CountDownTimer(COUNTDOWN_time, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished/ONE_SECOND
            }

            override fun onFinish() {
               _currentTime.value = DONE
                onGameFinish()
            }

        }
    }

    fun onGameFinish() {
        _eventGameFinish.value = true
    }
    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /** Methods for buttons presses **/

     fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

     fun onCorrect() {
         _score.value = (score.value)?.plus(1)
        nextWord()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        if(wordList.isEmpty()){
            resetList()
        }else{
            _word.value = wordList.removeAt(0)
        }

    }



    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}