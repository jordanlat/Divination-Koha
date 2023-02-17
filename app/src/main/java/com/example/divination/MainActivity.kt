package com.example.divination

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    //Déclaration de constantes
    private val msgListe = ArrayList<String>()
    private val layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
    private val recyclerAdapter = MsgAdapter(msgListe,this@MainActivity)
    private var trigAddMsg = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Déclaration de constante lié aux Layout/visuel
        val editText: EditText = findViewById((R.id.user_input))
        val msgRecyclerView = findViewById<RecyclerView>(R.id.msg_recycler_view)


        // Pour cacher la bar qui contient le titre
        supportActionBar?.hide()


        // On force le focus sur l'entré utilisateur
        editText.requestFocus()

        // À la validation par clavier, on prépare le message et on l'affiche
        editText.setOnKeyListener(object : View.OnKeyListener {
            @SuppressLint("ResourceType")
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                var thisText = editText.text
                layoutManager.stackFromEnd = true

                if (event.keyCode == 66 && event.action == KeyEvent.ACTION_DOWN && thisText.isNotEmpty()) {

                    thisText = editText.text
                    msgListe.add(thisText.toString())

                    msgRecyclerView.layoutManager = layoutManager
                    msgRecyclerView.adapter = recyclerAdapter

                    val deferredResult = GlobalScope.async {
                        //val asyngMsg = Async(Dispatchers.IO) {sendAnswer()}
                        delay(2000)
                        sendAnswer()
                        //recyclerAdapter.notifyDataSetChanged()
                    }
                    editText.text.clear()
                    return true
                }
                return false
            }
            // On enregistre une réponse auto-généré
            suspend fun sendAnswer() {
                delay(2000L)
                msgListe.add(generateAnswer())
            }
        })
        println("this is lifecycle: $lifecycle")
        recyclerAdapter.notifyDataSetChanged()
    }

    // Renvoie une réponse généré
    private fun generateAnswer(): String {
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

//On met en forme les messages
class MsgAdapter(private val msgList: ArrayList<String>, private val context: MainActivity) : RecyclerView.Adapter<MsgAdapter.MsgViewHolder>() {

    @SuppressLint("RtlHardcoded")
    override fun onBindViewHolder(msgViewHolder: MsgViewHolder, index: Int) {
        val gravityMsg = msgViewHolder.contentTextView.layoutParams as LinearLayout.LayoutParams
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

// Détermine si un nombre est pair
private fun isEven(number: Number): Boolean {
    return number.toInt() % 2 == 0
}