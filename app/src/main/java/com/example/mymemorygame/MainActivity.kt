package com.example.mymemorygame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import android.renderscript.ScriptGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    //private lateinit var nameButton: Button
    //lateinit var nameId: androidx.appcompat.widget.AppCompatEditText
    //val name: String? =

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //nameId = findViewById(R.id.input)
        //name = input.text.toString()

        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN
        button.setOnClickListener {
            //name = returnName()
            if (input.text.toString().isEmpty()) {
                Snackbar.make(clRoot, "Please enter your Name", Snackbar.LENGTH_LONG).show()

            }
            else {
                //Toast.makeText(this, input.text, Toast.LENGTH_SHORT).show()
                var intent = Intent(this, AskName::class.java)
                //name = returnName("Hadi")
                intent.putExtra("Name", returnName())
                startActivity(intent)
                //Snackbar.make(clRoot, "Enjoy the game ${input.text.toString()}", Snackbar.LENGTH_LONG).show()
                //finish()
            }
        }

    }
    //var hi: String = "Juhaha"

    fun returnName(): String? {
        return input.text.toString()
    }

}