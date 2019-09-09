package foo.bar.example.fore.fullapp02.ui.launch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import foo.bar.example.fore.fullapp02.R

/**
 * Nothing to sync here, all this does is show a spinner while we check for the log in status of the
 * user, this work is done in [LaunchView]
 */
class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(layoutInflater.inflate(R.layout.activity_launch, null))
    }
}
