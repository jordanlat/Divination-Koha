package com.example.divination

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import kotlin.random.Random
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Pour cacher la bar qui contient le titre
        supportActionBar?.hide()

        val msgListe = ArrayList<String>()
        editText = findViewById((R.id.user_input))
        editText.requestFocus()

        editText.setOnKeyListener(object : View.OnKeyListener {
            @SuppressLint("ResourceType")
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {

                val thisText = editText.text
                val layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                val recyclerAdapter = MsgAdapter(msgListe,this@MainActivity)
                val msgRecyclerView = findViewById<RecyclerView>(R.id.msg_recycler_view)

                layoutManager.stackFromEnd = true

                if (event.keyCode == 66 && event.action == KeyEvent.ACTION_UP && thisText.isNotEmpty()) {

                    msgListe.add(thisText.toString())

                    msgRecyclerView.layoutManager = layoutManager
                    msgRecyclerView.adapter = recyclerAdapter

                    sendAnswer()

                    editText.text.clear()
                    return true
                }
                return false
            }

            fun sendAnswer() {
                val isEven = isEven(msgListe.size)
                if(isEven){
                    //Timer().schedule(1000){
                        msgListe.add(generateAnswer())
                    //}

                }
            }
        })
    }


    fun generateAnswer(): String {
        val adverbLists = listOf(
            "Certainement",
            "Sûrement",
            "À mon avis c'est",
            "Pas de doute c'est",
            "Mon petit doigt me dit que c'est",
            "Sans hésiter"
        )
        val randomAdverb = adverbLists[Random.nextInt(adverbLists.size)]
        return "$randomAdverb Koha"
    }


}

class MsgAdapter(private val msgList: ArrayList<String>, private val context: MainActivity) : RecyclerView.Adapter<MsgAdapter.MsgViewHolder>() {

    override fun onBindViewHolder(msgViewHolder: MsgViewHolder, index: Int) {
        var gravityMsg = msgViewHolder.contentTextView.layoutParams as LinearLayout.LayoutParams
        if (!isEven(index)) {
            gravityMsg.gravity = Gravity.RIGHT
        } else {
            gravityMsg.gravity = Gravity.LEFT
        }

        msgViewHolder.contentTextView.text = msgList[index]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgViewHolder {

        return MsgViewHolder(
            LayoutInflater.from(context).inflate(R.layout.message_question_1, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    inner class MsgViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentTextView: TextView = view.findViewById(R.id.msg_content_1)
    }
}


private fun isEven(number: Number): Boolean {
    return number.toInt() % 2 == 0
}