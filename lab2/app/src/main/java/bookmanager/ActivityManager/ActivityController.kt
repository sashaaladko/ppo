
class ActivityController
{
    companion object{

        private var args = mutableMapOf<Int, IActivityArgs>()

        fun SetArgs(activityId: Int, arg: IActivityArgs) {
            args[activityId] = arg
        }


        fun <T> GetArgs(activityId: Int) : T{
            return args.getValue(activityId) as T
        }
    }
}