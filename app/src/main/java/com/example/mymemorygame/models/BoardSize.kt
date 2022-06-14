package com.example.mymemorygame.models

enum class BoardSize(val numCards: Int) {
    EASY(8),
    MEDIUM(18),
    HARD(20);

    // we are determining number of columns
    fun getWidth(): Int {
        // "this" refers to the difficulty level of boardsize
        return when(this) {
            EASY -> 2
            MEDIUM -> 3
            HARD -> 4
        }
    }

    // we are determining number of rows
    fun getHeight(): Int {
        return numCards / getWidth()
    }

    //getting number of pair of cards
    fun getNumPairs(): Int {
        return numCards / 2
    }
}