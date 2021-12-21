import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import android.view.LayoutInflater
import com.example.flappybirds.Model.UserInfo
import com.example.flappybirds.R


class DataAdapter
    (var mContext: Context, textViewResourceId: Int) :
    ArrayAdapter<UserInfo>(mContext, textViewResourceId, Users) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var grid: View

        if (convertView == null) {
            grid = View(mContext)
            val inflater =
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            grid = inflater.inflate(
                R.layout.grid,
                null,
                false
            )
        } else {
            grid = convertView
        }

        setupTextView(grid, R.id.name, Users[position].name)
        setupTextView(grid, R.id.nick_name, Users[position].nickname)
        setupTextView(grid, R.id.score, Users[position].score.toString())

        return grid
    }

    private fun setupTextView(grid: View, viewId: Int, str: kotlin.String) {
        val view  = grid.findViewById<TextView>(viewId)
        view.text = str
    }

    // возвращает содержимое выделенного элемента списка
    override fun getItem(position: Int): UserInfo {
        return Users[position]
    }

    companion object {
        var Users = mutableListOf<UserInfo>()
    }
}