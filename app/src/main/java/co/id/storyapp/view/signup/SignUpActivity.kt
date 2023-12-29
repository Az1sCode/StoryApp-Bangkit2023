package co.id.storyapp.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import co.id.storyapp.R
import co.id.storyapp.data.Result
import co.id.storyapp.databinding.ActivityMainBinding
import co.id.storyapp.databinding.ActivitySignUpBinding
import co.id.storyapp.utils.ViewModelFactory
import co.id.storyapp.view.login.LoginActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setButtonEnable()
        isLoading(false)
        playAnimation()

        binding.btnSignup.addTextChangedListener(object: TextWatcher {
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
            btnSignup.setOnClickListener {
                if (inputUsernameEditText.length() == 0 && signUpEmailEditText.length() == 0 && inputPasswordEditText.length() == 0) {
                    signUpEmailEditText.error = getString(R.string.error_message)
                    inputUsernameEditText.error = getString(R.string.error_message)
                    inputPasswordEditText.error = getString(R.string.error_message)
                } else if (inputUsernameEditText.length() != 0 && signUpEmailEditText.length() != 0 && inputPasswordEditText.length() != 0) {
                    val username = binding.inputUsernameEditText.text.toString()
                    val email = binding.signUpEmailEditText.text.toString()
                    val password = binding.inputPasswordEditText.text.toString()

                    viewModel.postRegister(username, email, password).observe(this@SignUpActivity) {result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    isLoading(true)
                                }

                                is Result.Success -> {
                                    isLoading(false)
                                    AlertDialog.Builder(this@SignUpActivity).apply {
                                        setTitle("Akun telah terdaftar")
                                        setMessage("Silahkan ke halaman login")
                                        setPositiveButton("Login") {_, _ ->
                                            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                        create()
                                        show()
                                    }
                                }
                                is Result.Error -> {
                                    isLoading(false)
                                    Toast.makeText(this@SignUpActivity, "Gagal membuat akun", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }


                }
            }
        }
    }

    private fun playAnimation() {

        val title = ObjectAnimator.ofFloat(binding.tvTitleLogin, View.ALPHA, 1f).setDuration(150)
        val signup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(150)
        val inputUsername = ObjectAnimator.ofFloat(binding.inputUsernameLayout, View.ALPHA, 1f).setDuration(150)
        val inputEmail = ObjectAnimator.ofFloat(binding.inputEmailLayout, View.ALPHA, 1f).setDuration(150)
        val inputPassword = ObjectAnimator.ofFloat(binding.inputPasswordLayout, View.ALPHA, 1f).setDuration(150)

        AnimatorSet().apply {
            playSequentially(
                title,
                inputUsername,
                inputEmail,
                inputPassword,
                signup
                )
            startDelay = 160
        }.start()
    }

    private fun isLoading(isLoading: Boolean) {
        binding.Progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setButtonEnable() {
        val password = binding.inputPasswordEditText.text.toString()

        binding.btnSignup.isEnabled = password.length > 7
    }
}
