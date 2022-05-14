package com.itis.my

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.itis.my.databinding.ActivityMainBinding
import com.itis.my.fragments.*


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    val binding: ActivityMainBinding
        get() = _binding!!

    private val homeFragment = HomeFragment()
    private val notesFragment = NotesFragment()
    private val mediaFragment = MediaFragment()
    private val connectionFragment = ConnectionFragment()
    private val locationFragment = LocationFragment()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bnvMenu.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_fragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fcv_container, homeFragment).commit()
                    true
                }
                R.id.notes_fragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fcv_container, notesFragment).commit()
                    true
                }
                R.id.photo_fragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fcv_container, mediaFragment).commit()
                    true
                }
                R.id.video_fragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fcv_container, connectionFragment).commit()
                    true
                }
                R.id.location_fragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fcv_container, locationFragment).commit()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}