import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.bookmanager.Book

import android.view.LayoutInflater
import com.example.bookmanager.R


class DataAdapter
    (var mContext: Context, textViewResourceId: Int) :
    ArrayAdapter<Book>(mContext, textViewResourceId, books) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var grid: View

        if (convertView == null) {
            grid = View(mContext)
            val inflater =
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            grid = inflater.inflate(
                R.layout.cellgrid,
                null,
                false
            )
        } else {
            grid = convertView
        }

        setupTextView(grid, R.id.reg_num, books[position].reg_number.toString())
        setupTextView(grid, R.id.author, books[position].author)
        setupTextView(grid, R.id.name, books[position].book_name)
        setupTextView(grid, R.id.year, books[position].year.toString())
        setupTextView(grid, R.id.publish, books[position].publishing)
        setupTextView(grid, R.id.num_of_page, books[position].num_of_page.toString())

        return grid
    }

    private fun setupTextView(grid: View, viewId: Int, str: kotlin.String) {
        val view  = grid.findViewById<TextView>(viewId)
        view.text = str
    }

    // возвращает содержимое выделенного элемента списка
    override fun getItem(position: Int): Book {
        return books[position]
    }

    fun addItem(book: Book) {
        books.add(book)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        books.removeAt(position)
        notifyDataSetChanged()
    }

    fun getAll() : List<Book> {
        return books
    }

    companion object {
        private var books = mutableListOf<Book>()
    }
}