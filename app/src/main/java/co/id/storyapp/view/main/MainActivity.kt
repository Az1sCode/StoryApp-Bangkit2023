package co.id.storyapp.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import co.id.storyapp.R
import co.id.storyapp.data.adapter.ListStoryAdapter
import co.id.storyapp.data.adapter.LoadingStateAdapter
import co.id.storyapp.databinding.ActivityMainBinding
import co.id.storyapp.utils.ViewModelFactory
import co.id.storyapp.view.map.UserMaps
import co.id.storyapp.view.upload.AddStoryActivity
import co.id.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val loginViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding.toolbar.setOnMenuItemClickListener {menuItem ->
            when (menuItem.itemId) {
                R.id.menu1 -> {
                    loginViewModel.logout()
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu2 -> {
                    val intent =  Intent(this, UserMaps::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUserStory.layoutManager = layoutManager

        loginViewModel.getSession().observe(this) {
            binding.toolbar.title = it.email
            if (!it.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            } else {
                getData()
            }
        }
    }

    private fun getData() {
        val adapter = ListStoryAdapter()
        binding.rvUserStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        loginViewModel.story.observe(this, {
            adapter.submitData(lifecycle, it)
        })
    }

}