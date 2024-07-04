package com.example.ujikom
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
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
        userProfile = UserProfile(
            name = binding.editNameText.text.toString(),
            nim = binding.editNimText.text.toString(),
            alamat = binding.editAlamatText.text.toString(),
            tanggalLahir = binding.editTanggalLahirText.text.toString(),
            tempatLahir = binding.editTempatLahirText.text.toString(),
            kelamin = binding.editKelaminText.text.toString(),
            photoUri = selectedImageUri.toString()
        )

        val intent = Intent(this, HasilActivity::class.java).apply {
            putExtra("USER_PROFILE", userProfile)
        }
        startActivity(intent)
    }

    private fun showDatePickerDialog() {
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
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
