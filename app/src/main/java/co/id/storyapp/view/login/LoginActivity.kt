package co.id.storyapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import co.id.storyapp.R
import co.id.storyapp.data.Result
import co.id.storyapp.data.pref.UserModel
import co.id.storyapp.databinding.ActivityLoginBinding
import co.id.storyapp.utils.ViewModelFactory
import co.id.storyapp.view.main.MainActivity
import co.id.storyapp.view.signup.SignUpViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setButtonEnable()
        isLoading(false)
        playAnimation()

        binding.btnLogin.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun setupAction() {
        binding.apply {
            binding.btnLogin.setOnClickListener {
                if (inputEmailEditText.length() == 0 && inputPasswordEditText.length() == 0) {
                    inputEmailEditText.error = getString(R.string.error_message)
                    inputPasswordEditText.setError(getString(R.string.error_message))
                } else if (inputEmailEditText.length() != 0 && inputPasswordEditText.length() != 0) {
                    postLogin()
                }
            }
        }
    }

    private fun postLogin() {
        val emailText = binding.inputEmailEditText.text.toString()
        val passwordText = binding.inputPasswordEditText.text.toString()

        loginViewModel.postLogin(emailText, passwordText).observe(this) {result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        isLoading(true)
                    }

                    is Result.Success -> {
                        isLoading(false)

                        val token = result.data.loginResult.token
                        saveSession(UserModel(emailText, token ))
                        moveActivity()
                        AlertDialog.Builder(this).apply {
                            setTitle("Selamat!")
                            setMessage("Akun anda terverifikasi!")
                            setPositiveButton("Login") {_, _ ->
                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }

                    is Result.Error -> {
                        isLoading(false)
                        AlertDialog.Builder(this).apply {
                            setTitle("Email atau password salah")
                            setMessage("Mohon masukkan email dan password yang benar!")
                            setPositiveButton("Kembali") {_, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(170)
        val inputEmail = ObjectAnimator.ofFloat(binding.inputEmailLayout, View.ALPHA, 1f).setDuration(170)
        val inputPassword = ObjectAnimator.ofFloat(binding.inputPasswordLayout, View.ALPHA, 1f).setDuration(170)
        val title = ObjectAnimator.ofFloat(binding.tvTitleLogin, View.ALPHA, 1f).setDuration(170)
        val desc = ObjectAnimator.ofFloat(binding.tvTitleSub, View.ALPHA, 1f).setDuration(170)

        AnimatorSet().apply {
            playSequentially(
                title,
                desc,
                inputEmail,
                inputPassword,
                login)
            startDelay = 170
        }.start()
    }

    private fun saveSession(user: UserModel) {
        loginViewModel.saveSession(user)
    }

    private fun moveActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun setButtonEnable() {
        val password = binding.inputPasswordEditText.text.toString()

        binding.btnLogin.isEnabled = password.length > 7
    }

    private fun isLoading(isLoading: Boolean) {
        binding.ProgressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }
}