package com.example.bookmanager

import AddArgs
import DataAdapter
import DoEnum
import IActivityArgs
import UpdateArgs
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*

class AddUpdateActivity : AppCompatActivity() {
    companion object{
        var id = 5636
    }

    var notNullEditViews = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update)

        var btn = findViewById<Button>(R.id.refresh)

        btn.isEnabled = false
        btn.isClickable = false

        var reg_num = findViewById<EditText>(R.id.reg_num)
        reg_num.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var str = s?.subSequence(start, start+ count).toString()?: ""
                changeActiveButton(btn, str, "reg")
            }
        })
        notNullEditViews["reg"] = 0

        var author = findViewById<EditText>(R.id.author)

        author.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var str = s?.subSequence(start, start+ count).toString()?: ""
                changeActiveButton(btn, str, "author")
            }
        })
        notNullEditViews["author"] = 0

        var book_name = findViewById<EditText>(R.id.name)
        book_name.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var str = s?.subSequence(start, start+ count).toString()?: ""
                changeActiveButton(btn, str, "book")
            }
        })
        notNullEditViews["book"] = 0

        var year = findViewById<EditText>(R.id.year)

        year.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var str = s?.subSequence(start, start+ count).toString()?: ""
                changeActiveButton(btn, str, "year")
            }
        })
        notNullEditViews["year"] = 0

        var publish = findViewById<EditText>(R.id.publish)

        publish.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var str = s?.subSequence(start, start+ count).toString()?: ""
                changeActiveButton(btn, str, "publish")
            }
        })
        notNullEditViews["publish"] = 0

        var num_of_page = findViewById<EditText>(R.id.num_of_page)

        num_of_page.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var str = s?.subSequence(start, start+ count).toString()?: ""
                changeActiveButton(btn, str, "page")
            }
        })
        notNullEditViews["page"] = 0

        var bookArgs = ActivityController.GetArgs<IActivityArgs>(id)
        if(bookArgs.do_with == DoEnum.UPDATE)
        {
            reg_num.text = bookArgs.book.reg_number.toString().toEditable()
            author.text = bookArgs.book.author.toEditable()
            book_name.text = bookArgs.book.book_name.toEditable()
            year.text = bookArgs.book.year.toString().toEditable()
            publish.text = bookArgs.book.publishing.toEditable()
            num_of_page.text = bookArgs.book.num_of_page.toString().toEditable()
        }


        btn.setOnClickListener {
            var book = Book(
                reg_num.text.toString().toInt(),
                author.text.toString(),
                book_name.text.toString(),
                year.text.toString().toInt(),
                publish.text.toString(),
                num_of_page.text.toString().toInt()
            )
            bookArgs.book.setup(book)
            var intent = Intent(this, MainActivity::class.java)
            ActivityController.SetArgs(MainActivity.id, bookArgs)
            this.startActivity(intent)
        }


    }

    private fun changeActiveButton(btn: Button, str: String, editName: String){
        if(str.isEmpty()) {
            btn.isClickable = false
            btn.isEnabled = false
            notNullEditViews[editName] = 0
        }
        else{
            notNullEditViews[editName] = 1
            if(checkActiveButton()) {
                btn.isClickable = true
                btn.isEnabled = true
            }
        }
    }


    private fun checkActiveButton() : Boolean{
        for((_, value) in notNullEditViews)
        {
            if(value == 0) {
                return false
            }
        }
        return true
    }
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)