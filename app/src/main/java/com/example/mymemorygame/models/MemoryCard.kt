package com.example.mymemorygame.models

/*
 every memory card will have an associated state so for example a memory card can either be face up which means that the image is showing or it can be face down which is the default state in how the game starts
 */

data class MemoryCard(
    // this represents the uniqueness of the memory icon which is the underlying resource id integer of the memory card and this is going to be of type int because the actual identifier is going to be a drawable resource that we defined earlier
    val identifiers: Int,

    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false
)

