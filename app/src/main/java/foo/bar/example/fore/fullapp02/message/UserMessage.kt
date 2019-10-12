package foo.bar.example.fore.fullapp02.message

import android.os.Parcel
import android.os.Parcelable

import foo.bar.example.fore.fullapp02.App
import foo.bar.example.fore.fullapp02.R


/**
 * As an enum, this value can be passed around the app to indicate various states.
 * If you want to display it to the user you can put it inside a dialog (it implements
 * parcelable). Call getString() for the human readable text.
 *
 * The [co.early.fore.retrofit.CallProcessor] in this app has been set up so that it
 * returns these messages if there are any problems with our network connection,
 * so the business layer knows nothing about HTTP codes etc - it only deals with these UserMessages
 */
enum class UserMessage constructor(val messageResId: Int) : Parcelable {

    ERROR_MISC(R.string.msg_error_misc),
    ERROR_NETWORK(R.string.msg_error_network),
    ERROR_SERVER(R.string.msg_error_server),
    ERROR_CLIENT(R.string.msg_error_client),
    ERROR_SESSION_TIMED_OUT(R.string.msg_error_session_timeout),
    ERROR_BUSY(R.string.msg_error_busy),
    ERROR_PERMISSION_DENIED(R.string.msg_error_permission_denied),
    ERROR_CANCELLED(R.string.msg_error_cancelled);

    private var message: String = ""

    val string: String
        get() {

            if (message == null) {
                message = getString(messageResId)
            }

            return message
        }

    constructor(parcel: Parcel) : this(parcel.readInt()) {
        message = parcel.readString() ?: ""
    }

    //this should only be called when a UserMessage is actually displayed to a user, so not during a JUnit test
    private fun getString(resId: Int): String {
        return App.inst.resources.getString(resId)
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

    companion object CREATOR : Parcelable.Creator<UserMessage> {
        override fun createFromParcel(parcel: Parcel): UserMessage {
            return UserMessage.values()[parcel.readInt()]
        }

        override fun newArray(size: Int): Array<UserMessage?> {
            return arrayOfNulls(size)
        }
    }

}
