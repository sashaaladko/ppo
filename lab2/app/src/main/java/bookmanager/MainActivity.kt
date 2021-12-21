package com.example.bookmanager

import AddArgs
import DataAdapter
import DoEnum
import IActivityArgs
import UpdateArgs
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import java.lang.Exception
import android.view.ContextMenu.ContextMenuInfo
import android.view.ContextMenu
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.view.MenuItem
import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import androidx.core.content.ContextCompat
import android.Manifest


class MainActivity : AppCompatActivity() {
    companion object{
        var id = 5636
    }

    private lateinit var mAdapter: DataAdapter
    private lateinit var btn_update: Button
    private lateinit var btn_delete: Button

    private val REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 127
    private val REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 128

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAdapter = DataAdapter(
            applicationContext,
            R.layout.cellgrid
        )


        try {
            var bookArgs = ActivityController.GetArgs<IActivityArgs>(id)
            if (bookArgs.do_with == DoEnum.UPDATE) {
                mAdapter.getItem((bookArgs as UpdateArgs).id).setup(bookArgs.book)
            } else
                mAdapter.addItem(bookArgs.book)
        }catch (e : Exception)
        {

        }

        val g = findViewById<GridView>(R.id.booksGrid)
        g.adapter = mAdapter

        registerForContextMenu(g)

        findViewById<Button>(R.id.add).setOnClickListener {
            var intent = Intent(this, AddUpdateActivity::class.java)
            ActivityController.SetArgs(AddUpdateActivity.id, AddArgs())
            this.startActivity(intent)
        }

        var loadActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val uri = data?.data
                if(uri != null){
                    var inputString= ""

                    val permissionStatus =
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

                    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                        inputString = contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() }?: ""
                    } else {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE
                        )
                    }
                    try {
                        for (i in Book.getBooks(inputString))
                            mAdapter.addItem(i)
                    } catch (e: Exception){

                    }
                }
            }
        }

        var saveActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val uri = data?.data
                if(uri != null){
                    val permissionStatus =
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

                    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                        var outstream = contentResolver.openOutputStream(uri)?.bufferedWriter().use{ it?.write(Book.getJSON(mAdapter.getAll()))}
                    } else {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE
                        )
                    }
                }
            }
        }

        findViewById<Button>(R.id.save).setOnClickListener {
            var books = mAdapter.getAll()
            var myFileIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            myFileIntent.type = "*/*"
            saveActivity.launch(myFileIntent)
        }

        findViewById<Button>(R.id.load).setOnClickListener {
            var myFileIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            myFileIntent.type = "*/*"
            loadActivity.launch(myFileIntent)
        }
    }


    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.getMenuInfo() as AdapterContextMenuInfo
        return when (item.getItemId()) {
            R.id.edit -> {
                var intent = Intent(this, AddUpdateActivity::class.java)
                ActivityController.SetArgs(AddUpdateActivity.id, UpdateArgs(info.position, mAdapter.getItem(info.position)))
                this.startActivity(intent)
                true
            }
            R.id.delete -> {
                mAdapter.deleteItem(info.position)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}