package co.id.storyapp.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import co.id.storyapp.R
import co.id.storyapp.databinding.ActivityWelcomeBinding
import co.id.storyapp.view.login.LoginActivity
import co.id.storyapp.view.signup.SignUpActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showAction()
        playAnimation()
    }

    private fun showAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                val intentLogin = Intent(this@WelcomeActivity, LoginActivity::class.java)
                startActivity(intentLogin)
            }
            btnSignup.setOnClickListener {
                val intentSignup = Intent(this@WelcomeActivity, SignUpActivity::class.java)
                startActivity(intentSignup)
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgWelcome, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(150)
        val signup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(150)
        val title = ObjectAnimator.ofFloat(binding.tvTitleWelcome, View.ALPHA, 1f).setDuration(150)
        val desc = ObjectAnimator.ofFloat(binding.tvTitleDescription, View.ALPHA, 1f).setDuration(150)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, together)
            startDelay = 160
        }.start()
    }
}