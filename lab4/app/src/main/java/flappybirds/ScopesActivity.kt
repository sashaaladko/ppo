package com.example.flappybirds

import DatabaseHandler
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flappybirds.Model.UserInfo
import android.view.View
import DataAdapter
import android.view.MenuItem
import android.widget.*


class ScopesActivity : AppCompatActivity() {

    private lateinit var mAdapter: DataAdapter
    companion object{
        var load_times = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mAdapter = DataAdapter(
            applicationContext,
            R.layout.grid
        )

        val db = DatabaseHandler(this)

        val g = findViewById<ListView>(R.id.list_item)
        g.adapter = mAdapter


        DataAdapter.Users = db.allUsers.toMutableList()
        mAdapter.notifyDataSetChanged()

        var name = findViewById<EditText>(R.id.name_text)
        var nickname = findViewById<EditText>(R.id.nickname_text)

        val score = intent.getIntExtra("score", -1)

        findViewById<Button>(R.id.add).setOnClickListener {
            if(nickname.text.isNotEmpty() && name.text.isNotEmpty())
                addUser(nickname.text.toString(), name.text.toString(), score)
        }

        findViewById<Button>(R.id.load).setOnClickListener {
            change()
        }

        findViewById<Button>(R.id.button).setOnClickListener(viewClickListener)

        if(load_times != 2){
            var intent = Intent(this, ScopesActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra("score", score)
            this.startActivity(intent)
            load_times += 1
        }
        else
            load_times = 0

    }

    var viewClickListener =
        View.OnClickListener { v -> showPopupMenu(v) }

    private fun showPopupMenu(v: View) {
        val popupMenu = PopupMenu(this, v)
        popupMenu.inflate(R.menu.popupmenu)
        popupMenu
            .setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    return when (item.getItemId()) {
                        R.id.nickname_menu -> {
                            DataAdapter.Users.sortBy { userInfo -> userInfo.nickname }
                            mAdapter.notifyDataSetChanged()
                            true
                        }
                        R.id.name_menu -> {
                            DataAdapter.Users.sortBy { userInfo -> userInfo.name }
                            mAdapter.notifyDataSetChanged()
                            true
                        }
                        R.id.score_menu -> {
                            DataAdapter.Users.sortBy { userInfo -> -userInfo.score }
                            mAdapter.notifyDataSetChanged()
                            true
                        }
                        else -> false
                    }
                }
            })
        popupMenu.setOnDismissListener(object : PopupMenu.OnDismissListener {
            override fun onDismiss(menu: PopupMenu?) {
                Toast.makeText(applicationContext, "onDismiss",
                    Toast.LENGTH_SHORT).show()
            }
        })
        popupMenu.show()
    }



    private fun change(){
        var intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        this.startActivity(intent)
        finish()
    }


    private fun addUser(nickName: String, name: String, score: Int) {
        try {
            val db = DatabaseHandler(this)
            var user = UserInfo(nickName, name, score)
            db.addUser(user)
            var size = DataAdapter.Users.size
            DataAdapter.Users = db.allUsers.toMutableList()
            mAdapter.notifyDataSetChanged()

            if(DataAdapter.Users.size == size)
                return

            mAdapter.add(user)

            var button =  findViewById<Button>(R.id.add)
            button.isClickable = false
            button.isActivated = false
        } catch (e: Exception) {
        }
    }
}