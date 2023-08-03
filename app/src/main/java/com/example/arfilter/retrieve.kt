package com.example.arfilter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.arfilter.databinding.ActivityRetrieveBinding

class retrieve : AppCompatActivity() {

     private lateinit var binding: ActivityRetrieveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRetrieveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.getImage.setOnClickListener {
            val imageUrl = binding.etImageid.text.toString()
            // Load image using Glide library
            Glide.with(this)
                .load(imageUrl)
                .into(binding.ImageView)
            binding.etImageid.setText("")
        }
    }
}