package com.example.mymemorygame

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemorygame.models.BoardSize
import com.example.mymemorygame.models.MemoryCard

import kotlin.math.min

class MemoryBoardAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val randomizedImages: List<MemoryCard>,
    private val cardClickListener: CardClickListener
) :
    RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {
        /*
         so a viewholder is an object which provides access to all the views of one recyclerview element (so in our case that will represent one memory piece or one memory card in the game)


         */

    //a companion object in kotlin companion objects are singletons where we'll define constants
    //we can access its members directly through the containing class (class doesn't have to be initialized)
    companion object {
        private val marginSize = 10
        private val TAG = "MemoryBoardAdapter"
    }

    /*
    to notify the main activity of this click so that the main activity can then tell the memory game
59:08
class that the user has taken some action and we should update the state appropriately and so the standard pattern for doing
59:14
this is to define an interface

the reason why we're defining this interface is because whoever constructs
59:39
the memory board adapter it will be their responsibility to now pass in an instance of this interface
when an image is clicked we are going to invoke this method on the interface

     */
    interface CardClickListener {
        fun onCardClicked(position: Int)
    }


    // we need to override following methods because it's an abstract class

    //responsible for figuring out how to create one view of our recyclerview

    /*
    we need to measure what is the width and height of the recycler view which is containing all the memory cards and based on that change the width of our card view

    and that turns out not to be that difficult because the recyclerview is actually the parent which is passed in here
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // width / number of columns
        val cardWidth: Int = (parent.width / boardSize.getWidth()) - (2 * marginSize)
        // height divided by number of rows
        val cardHeight: Int = (parent.height / boardSize.getHeight()) - (2 * marginSize)

        /*and we're going to mandate that each memory card in our game is going to be square so we're going to take the smaller of the card with a card height
        so i'll call it card side length that's going to be the minimum of card width and card height
        */
        val cardSideLength: Int = min(cardWidth, cardHeight)

        //returns actual view that was created
        val view = LayoutInflater.from(context).inflate(R.layout.memory_card, parent, false)

        // so now we need to actually grab out the card view from the view that we've inflated and set the width and height of that card view to be cardsidelength
        val layoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = cardSideLength
        layoutParams.height = cardSideLength
        layoutParams.setMargins(marginSize, marginSize, marginSize, marginSize)

        return ViewHolder(view)
    }

    //onbindviewholder is responsible for taking the data which is at this position and binding it to this view holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //
        holder.bind(position)
    }

    // how many elements in recyclerView
    override fun getItemCount() = boardSize.numCards

    // and we're going to define our own view holder which will encapsulate the memory card view
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        // defining the image button
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)

        fun bind(position: Int) {

            // each element in the "randomizedImages" list represent one of images in our game
            // what we want to do is reference these images inside of bind by using an image button

            //in line below, we are setting appearance of card faced down and up
            imageButton.setImageResource(if (randomizedImages[position].isFaceUp) randomizedImages[position].identifiers else R.drawable.coolbackground)

            //want to do is in the check for match method if our logic is correct we will now set the boolean property is matched when cards are indeed matched
            // alpha value refers to the opacity how visible is the the image button
            imageButton.alpha = if (randomizedImages[position].isMatched) .4f else 1.0f

            // grabbing a reference to the image button which is inside that card view
            //changing the state of a memory card when we tap on it
            imageButton.setOnClickListener{
                Log.i(TAG, "Clicked on Position $position")
                //when an image is clicked we are going to invoke this method on the interface
                cardClickListener.onCardClicked(position)
            }

        }
    }
}
