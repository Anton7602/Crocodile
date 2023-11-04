package com.crocodile.game.app

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.random.Random

data class Words(val words: List<String>) {
    constructor() : this(emptyList())
}

class MainActivity : AppCompatActivity() {
    private lateinit var front_animation: AnimatorSet
    private lateinit var back_animation: AnimatorSet
    private lateinit var crocodileLayout: ConstraintLayout
    private lateinit var cardFront: CardView
    private lateinit var cardBack: CardView
    private lateinit var firstWordTextView: TextView
    private lateinit var secondWordTextView: TextView
    private lateinit var thirdWordTextView: TextView
    private lateinit var forthWordTextView: TextView
    private lateinit var fifthWordTextView: TextView
    private var easyWordsBank = listOf<String>()
    private var mediumWordsBank = listOf<String>()
    private var hardWordsBank = listOf<String>()
    private var isFront = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
        DeserializeWords()
        setUpAnimation()

        crocodileLayout.setOnClickListener{
            isFront = if (isFront) {
                front_animation.setTarget(cardBack)
                back_animation.setTarget(cardFront)
                front_animation.start()
                back_animation.start()
                false
            } else {
                updateWord()
                front_animation.setTarget(cardFront)
                back_animation.setTarget(cardBack)
                front_animation.start()
                back_animation.start()
                true
            }
        }
    }

    private fun updateWord() {
        firstWordTextView.text = easyWordsBank[Random.nextInt(0, easyWordsBank.size-1)]
        secondWordTextView.text = mediumWordsBank[Random.nextInt(0, mediumWordsBank.size-1)]
        thirdWordTextView.text = mediumWordsBank[Random.nextInt(0, mediumWordsBank.size-1)]
        forthWordTextView.text = mediumWordsBank[Random.nextInt(0, mediumWordsBank.size-1)]
        fifthWordTextView.text = hardWordsBank[Random.nextInt(0, hardWordsBank.size-1)]
    }

    private fun setUpAnimation() {
        val scale = resources.displayMetrics.density
        cardFront.cameraDistance = 8000*scale
        cardBack.cameraDistance=8000*scale
        front_animation = AnimatorInflater.loadAnimator(this, R.animator.front_animator) as AnimatorSet
        back_animation = AnimatorInflater.loadAnimator(this, R.animator.back_animator) as AnimatorSet
        back_animation.addListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                crocodileLayout.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator) {
                crocodileLayout.isEnabled = true
            }

            override fun onAnimationCancel(animation: Animator) {
                crocodileLayout.isEnabled = true
            }

            override fun onAnimationRepeat(animation: Animator) { }
        })
    }

    private fun DeserializeWords() {
        val objectMapper = ObjectMapper()
        var inputStream = resources.openRawResource(R.raw.words_easy)
        var jsonString = inputStream.bufferedReader().use { it.readText() }
        easyWordsBank = objectMapper.readValue(jsonString, Words::class.java).words

        inputStream = resources.openRawResource(R.raw.words_medium)
        jsonString = inputStream.bufferedReader().use { it.readText() }
        mediumWordsBank = objectMapper.readValue(jsonString, Words::class.java).words

        inputStream = resources.openRawResource(R.raw.words_hard)
        jsonString = inputStream.bufferedReader().use { it.readText() }
        hardWordsBank = objectMapper.readValue(jsonString, Words::class.java).words
    }

    private fun bindViews() {
        crocodileLayout = findViewById(R.id.crocodile_act)
        cardFront = findViewById(R.id.back_side_crd)
        cardBack = findViewById(R.id.front_side_card)
        firstWordTextView = findViewById(R.id.first_word_txt)
        secondWordTextView= findViewById(R.id.second_word_txt)
        thirdWordTextView= findViewById(R.id.third_word_txt)
        forthWordTextView= findViewById(R.id.forth_word_txt)
        fifthWordTextView= findViewById(R.id.fifth_word_txt)
    }
}