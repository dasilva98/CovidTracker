package pt.ulusofona.deisi.a2020.cm.g15.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_splash_screen.*
import pt.ulusofona.deisi.a2020.cm.g15.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        logotipoSplashScreen.alpha = 0f
        textSplashScreen.alpha = 0f
        textSplashScreen.animate().setDuration(1500).alpha(1f)
        logotipoSplashScreen.animate().setDuration(1500).alpha(1f).withEndAction{
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }

    }
}