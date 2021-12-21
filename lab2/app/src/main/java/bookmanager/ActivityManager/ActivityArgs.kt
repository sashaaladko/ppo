import com.example.bookmanager.Book

interface IActivityArgs{
    val do_with: DoEnum
    var book: Book

    companion object {
        fun <T> convert(args: IActivityArgs): T{
            return args as T
        }
    }
}

enum class DoEnum{
    UPDATE, ADD
}

class AddArgs() : IActivityArgs {
    override val do_with: DoEnum = DoEnum.ADD
    override var book: Book = Book.getNullBook()
}

class UpdateArgs(var id: Int, override var book : Book): IActivityArgs {
    override val do_with: DoEnum = DoEnum.UPDATE

}