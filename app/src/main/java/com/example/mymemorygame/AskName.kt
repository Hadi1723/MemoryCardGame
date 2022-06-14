package com.example.mymemorygame

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemorygame.models.BoardSize
import com.example.mymemorygame.models.MemoryGame
import com.google.android.material.snackbar.Snackbar

class AskName : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }



    // declaring late private variables to represent each changable element in our main page of app (which is present in our activity_main.xml file)

    // the lateinit keyword is used for those variables which are initialized after the declaration or we can say that the variable which is late initialized is called a lateinit variable.
    //Initialization is the process of assigning a value to the Variable. Every programming language has its own method of initializing the variable. If the value is not assigned to the Variable, then the process is only called a Declaration.

    // the reason all properties of class should be "private" is because of encapsulation:
    // useful links:
    // https://www.geeksforgeeks.org/encapsulation-in-java/
    // https://www.geeksforgeeks.org/kotlin-visibility-modifiers/#:~:text=In%20Kotlin%2C%20private%20modifiers%20allow,or%20function%20outside%20the%20scope.
    private lateinit var rvBoard: RecyclerView
    private lateinit var tvNumMoves: TextView
    private lateinit var tvNumPairs: TextView
    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var clRoot: ConstraintLayout

    private lateinit var user: String

    //val bundle: Bundle? = intent.extras
    //val string: String? = intent.getString("keyString")

    //private var userName: String = user.name


    //defining variable storing difficulty of level
    private var boardSize: BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_name)

        //setting the late variables to corresponding view (id)  in layout
        clRoot = findViewById(R.id.ConstrainedLayout)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        // NOTE: ideally all elements in "activity_main.xml" must be initialized by their id from that same xml file before changing specific attributes of those elements
        tvNumPairs = findViewById(R.id.tvNumPairs)

        setBoard(boardSize)

        user = intent.getStringExtra("Name").toString()

        Snackbar.make(clRoot, "Enjoy the game ${user}", Snackbar.LENGTH_LONG).show()

    }

    private fun setBoard(level: BoardSize) {
        // constructing the memory game
        memoryGame = MemoryGame(level)

        //tvNumMoves.text = "Moves: 0"
        tvNumMoves.text = "Moves: 0"
        tvNumPairs.text = "Pairs: 0 / ${boardSize.getNumPairs()}"

        /* every recyclerView has two main components:
                1. the adapter: it's responsible for taking in the underlying data set of the recycler view and turning that into or adapting each piece of data into a view
define a new class which has all the logic for the adapter
21:48
called memory board adapter and this will take in two parameters one which is the context
21:54
so i'm going to pass in this and second will be how many elements total are in our grid and also the list of images to be represented by the cards

                2. the layout manager: given some views or screens, it is responsible for measuring and positioning those items
                    . takes in two parameters
                        a) context: which is referencing the main activity class because it is the view that we are referencing
                        b)  span count: indicates how many columns are in your recycler view
         */
        adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards, object: MemoryBoardAdapter.CardClickListener {
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }

        })

        rvBoard.adapter = adapter

        //size of recyclerView will always be same, which improves optimization
        //rvBoard.setHasFixedSize(True)
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    //adding the menu option into the game
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //first parameter is file directory path
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //adding functionality for when menu item is selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        boardSize = when (item.itemId) {
            R.id.easy_refresh -> BoardSize.EASY
            R.id.medium_refresh -> BoardSize.MEDIUM
            R.id.hard_refresh -> BoardSize.HARD
            else -> boardSize
        }

        //warning the user about restarting the game
        if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()) {
            warnRestart("Restart your current Game!", null, View.OnClickListener {
                setBoard(boardSize)
            })
        } else {
            setBoard(boardSize)
        }

        return super.onOptionsItemSelected(item)
    }

    //creating warning screen
    private fun warnRestart(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Ok") {_, _ ->
                positiveClickListener.onClick(null)
            }.show()

    }

    private fun updateGameWithFlip(position: Int) {
        //checking for invalid moves

        // would like to show some ui when either of these funcions are called and the way we can do this is by using a snack bar
        if (memoryGame.haveWonGame()) {
            //Snackbar.make(clRoot, "You already won", Snackbar.LENGTH_LONG).show()
            return
        }
        if (memoryGame.isCardFaceUp(position)) {
            //Snackbar.make(clRoot, "Invalid Move", Snackbar.LENGTH_LONG).show()
            return
        }


        // actions that program executes if move is valid

        if (memoryGame.flipCard(position)) {
            tvNumPairs.text = "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"


            if (memoryGame.haveWonGame()) {
                lateinit var message: String

                var finalNumberOfMoves = memoryGame.getNumMoves()

                if (finalNumberOfMoves <= 30) {
                    message = "${user}, you Won with less than 30 tries. 3 STARS."
                } else if (finalNumberOfMoves > 30 && finalNumberOfMoves <= 40) {
                    message = "${user}, you Won with less than 40 tries. 2 STARS."
                } else {
                    message = "${user}, you Won with less than 50 tries. 1 STAR."
                }
                Snackbar.make(clRoot, message, Snackbar.LENGTH_LONG).show()

            }
        }

        tvNumMoves.text = "Moves: ${memoryGame.getNumMoves()}"

        //we need to tell the recyclerview adapter that the contents of what it's showing has changed and so it should update itself
        adapter.notifyDataSetChanged()
    }
}