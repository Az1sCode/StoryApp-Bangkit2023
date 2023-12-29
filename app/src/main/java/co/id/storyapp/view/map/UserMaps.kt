package co.id.storyapp.view.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import co.id.storyapp.R
import co.id.storyapp.data.Result
import co.id.storyapp.data.pref.ListStoryItem
import co.id.storyapp.data.pref.StoryResponse

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import co.id.storyapp.databinding.ActivityUserMapsBinding
import co.id.storyapp.utils.ViewModelFactory
import co.id.storyapp.view.main.MainViewModel

class UserMaps : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityUserMapsBinding

    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        viewModel.getStories().observe(this) {result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        Toast.makeText(this, getString(R.string.loading_data), Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        val listStory = result.data.listStory
                        showMark(listStory)
                    }
                    is Result.Error -> {
                        Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showMark(story: List<ListStoryItem>) {
        story.forEach { mark ->
            val storyMark = LatLng(mark.lat, mark.lon)
            mMap.addMarker(
                MarkerOptions()
                    .position(storyMark)
                    .title(mark.name)
                    .snippet(mark.description)
            )
        }

    }
}