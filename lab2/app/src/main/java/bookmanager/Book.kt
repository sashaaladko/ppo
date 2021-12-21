package com.example.bookmanager
import org.json.*

class Book(var reg_number : Int, var author : String,
           var book_name : String, var year : Int,
           var publishing : String, var num_of_page : Int) {

    constructor(jsonObject: JSONObject) :
            this(jsonObject.getInt("rn"), jsonObject.getString("au"),
                jsonObject.getString("bn"), jsonObject.getInt("ye"),
                jsonObject.getString("pu"), jsonObject.getInt("np")){ }

    companion object{
        fun getNullBook(): Book{
            return Book(-1, "", "", -1, "", -1)
        }


        fun getBooks(str: String): List<Book>{
            var books = mutableListOf<Book>()
            var json_arr = JSONArray(str)
            for(i in 0 until json_arr.length())
            {
                books.add(Book(json_arr.getJSONObject(i)))
            }

            return books
        }

        fun getJSON(books: List<Book>): String{
            val json_arr = JSONArray()
            for(i in books) {
                var book = JSONObject()
                book.put("rn", i.reg_number).put("au", i.author)
                    .put("bn", i.book_name).put("ye", i.year)
                    .put("pu", i.publishing).put("np", i.num_of_page)
                json_arr.put(book)
            }
            var str = json_arr.toString()
            return json_arr.toString()
        }
    }

    fun setup(book: Book){
        reg_number = book.reg_number
        author = book.author
        book_name = book.book_name
        year = book.year
        publishing = book.publishing
        num_of_page = book.num_of_page
    }
}