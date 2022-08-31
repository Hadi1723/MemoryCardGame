package com.example.mymemorygame.models

import com.example.mymemorygame.utils.DEFAULT_ICONS


// and this memory game is going to take in the board size and we want to delegate a
//57:19
//responsibility of creating all the cards into the memory game that shouldn't happen in the main activity that should
//57:24
//actually happen inside of the memory game and so let's add in as a constructor the board size here
//57:33
//[Music] the cards are going to be a member variable in this class
//57:40
//i'm also going to have a couple more which we're going to add over time but to start out with we're going to have one which is


class MemoryGame(private val boardSize: BoardSize) {

    val cards: List<MemoryCard>
    var numPairsFound = 0
    private var numMoves = 0

    //what does that mean that means that there were either zero cards previously flipped over or
    private var indexOfSingleCard: Int? = null

    init {
        // incorporating images into each card
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        // getting 2 copies of each image
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        // create a list of these memory cards and the way we'll do that is we're going to utilize the map  function on randomized images to create a new memory card object
        cards = randomizedImages.map{MemoryCard(it)}
    }

    // defining function that flips over the card
    fun flipCard(position: Int): Boolean {
        val card = cards[position]

        var foundMatch = false

        if(indexOfSingleCard == null) {
            restoreCards()
            indexOfSingleCard = position
        } else {
            //objective is that this function will return to us true or false on whether those two positions on the board are identical images or not
            // the first parameter will be index of single selected card
            // the second will be position and this is going to return to us whether we found a match or not

            // to force null proerty to be treated as non null, we can add two "!!"
                // we think of this idea when we have message "Smart cast to 'Int' is impossible, because 'indexOfSingleCard' is a mutable property"
            foundMatch = checkForMatch(indexOfSingleCard!!, position)

            // after we flip 2 cards, we return to state where no cards are flipped over
            indexOfSingleCard = null
        }

        //increasing number of moves
        numMoves++

        //switiching cards back to face down mode
        card.isFaceUp = !card.isFaceUp

        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifiers != cards[position2].identifiers) {
            return false
        }

        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numMoves
    }
}