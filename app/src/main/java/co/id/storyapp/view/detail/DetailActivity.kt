package co.id.storyapp.view.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import co.id.storyapp.R
import co.id.storyapp.data.Result
import co.id.storyapp.data.pref.DetailResponse
import co.id.storyapp.data.pref.ListStoryItem
import co.id.storyapp.data.pref.StoryResponse
import co.id.storyapp.databinding.ActivityDetailBinding
import co.id.storyapp.utils.ViewModelFactory
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id").toString()

        detailViewModel.getDetail(id).observe(this) {detail ->
            if (detail != null) {
                when (detail) {

                    is Result.Loading -> {
                        binding.Progressbar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.Progressbar.visibility = View.GONE
                        setData(detail.data)
                    }

                    is Result.Error -> {
                        binding.Progressbar.visibility = View.GONE
                        Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

    }

    private fun setData(detail: DetailResponse) {
        val context = binding.root.context
        Glide.with(context).load(detail.story.photoUrl).into(binding.imgStory)
        binding.titleStory.text = detail.story.name
        binding.deskripsiStory.text = detail.story.description
    }

    private fun setDataOffline(story: ListStoryItem?) {
        val context = binding.root.context
        Glide.with(context).load(story?.photoUrl).into(binding.imgStory)
        binding.titleStory.text = story?.name
        binding.deskripsiStory.text = story?.description
    }
}