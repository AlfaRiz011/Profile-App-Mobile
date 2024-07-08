package com.example.ujikom

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ujikom.databinding.ActivityHasilBinding

class HasilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHasilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userProfile: UserProfile? = intent.getParcelableExtra("USER_PROFILE")

        userProfile?.let { profile ->
            binding.textViewName.text = profile.name
            binding.textViewNim.text = profile.nim
            binding.textViewAlamat.text =  profile.alamat
            binding.textViewTanggalLahir.text =  profile.tanggalLahir
            binding.textViewTempatLahir.text =  profile.tempatLahir
            binding.textViewKelamin.text =  profile.kelamin

            val photoUri = Uri.parse(profile.photoUri)
            binding.imageViewProfile.setImageURI(photoUri)
        }

        binding.editBiodata.setOnClickListener {
            val intent = Intent(this@HasilActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}