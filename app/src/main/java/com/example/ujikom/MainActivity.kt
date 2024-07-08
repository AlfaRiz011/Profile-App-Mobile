package com.example.ujikom

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ujikom.databinding.ActivityMainBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var userProfile: UserProfile
    private var selectedImageUri: Uri? = null
    private var isValid: Boolean = true

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.profileImage.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setInput()
        setupImagePicker()
    }

    private fun setInput() {
        val genderItems = listOf("Laki-Laki", "Perempuan")
        val genderAdapter = ArrayAdapter(this, R.layout.dropdown_item, genderItems)
        (binding.editKelaminText as? AutoCompleteTextView)?.setAdapter(genderAdapter)

        binding.editTanggalLahirText.setOnClickListener {
            showDatePickerDialog()
        }

        binding.saveButton.setOnClickListener {
            saveUserProfile()
        }
    }

    private fun setupImagePicker() {
        binding.editButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun saveUserProfile() {
        isValid = true
        binding.apply {
            val name = editNameText.text.toString().trim()
            val nim = editNimText.text.toString().trim()
            val alamat = editAlamatText.text.toString().trim()
            val tanggalLahir = editTanggalLahirText.text.toString().trim()
            val tempatLahir = editTempatLahirText.text.toString().trim()
            val kelamin = editKelaminText.text.toString().trim()

            when {
                name.isBlank() -> {
                    editNameText.error = "Nama harus diisi"
                    isValid = false
                }
                nim.isBlank() -> {
                    editNimText.error = "NIM harus diisi"
                    isValid = false
                }
                alamat.isBlank() -> {
                    editAlamatText.error = "Alamat harus diisi"
                    isValid = false
                }
                tanggalLahir.isBlank() -> {
                    editTanggalLahirText.error = "Tanggal lahir harus diisi"
                    isValid = false
                }
                tempatLahir.isBlank() -> {
                    editTempatLahirText.error = "Tempat lahir harus diisi"
                    isValid = false
                }
                kelamin.isBlank() -> {
                    editKelaminText.error = "Jenis kelamin harus dipilih"
                    isValid = false
                }
            }

            if (isValid) {
                userProfile = UserProfile(
                    name = name,
                    nim = nim,
                    alamat = alamat,
                    tanggalLahir = tanggalLahir,
                    tempatLahir = tempatLahir,
                    kelamin = kelamin,
                    photoUri = selectedImageUri?.toString() ?: ""
                )

                val intent = Intent(this@MainActivity, HasilActivity::class.java).apply {
                    putExtra("USER_PROFILE", userProfile)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "Gagal menyimpan profil", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePickerDialog() {
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Pilih Tanggal")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.show(supportFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = Date(selection)
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = formatter.format(selectedDate)
            binding.editTanggalLahirText.setText(formattedDate)
        }
    }
}
