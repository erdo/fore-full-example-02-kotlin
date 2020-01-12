package foo.bar.example.fore.fullapp02.message

import android.os.Parcel
import android.os.Parcelable
import foo.bar.example.fore.fullapp02.App
import foo.bar.example.fore.fullapp02.R


/**
 * As an enum, this value can be passed around the app to indicate various states.
 * If you want to display it to the user you can put it inside a dialog (it implements
 * parcelable). Call localisedMessage() for the human readable text.
 */
enum class UserMessage constructor(val messageResId: Int) : Parcelable {

    ERROR_MISC(R.string.msg_error_misc),
    ERROR_NETWORK(R.string.msg_error_network),
    ERROR_SECURITY_UNKNOWN(R.string.msg_error_network),
    ERROR_SERVER(R.string.msg_error_server),
    ERROR_CLIENT(R.string.msg_error_client),
    ERROR_SESSION_TIMED_OUT(R.string.msg_error_session_timeout),
    ERROR_BUSY(R.string.msg_error_busy),
    ERROR_PERMISSION_DENIED(R.string.msg_error_permission_denied),
    ERROR_CANCELLED(R.string.msg_error_cancelled);

    val localisedMessage: String by lazy {
        getString(messageResId)
    }

    constructor(parcel: Parcel) : this(parcel.readInt())

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
            return values()[parcel.readInt()]
        }

        override fun newArray(size: Int): Array<UserMessage?> {
            return arrayOfNulls(size)
        }
    }

}
