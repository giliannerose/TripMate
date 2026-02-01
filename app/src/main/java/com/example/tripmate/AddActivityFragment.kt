package com.example.tripmate.ui.itinerary

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tripmate.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import java.util.Calendar

class AddActivityFragment : Fragment(R.layout.fragment_add_activity) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etDate = view.findViewById<EditText>(R.id.etDate)
        val etTime = view.findViewById<EditText>(R.id.etTime)
        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etNotes = view.findViewById<EditText>(R.id.etNotes)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)


        // DATE PICKER
        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val formattedDate = String.format(
                        "%04d-%02d-%02d",
                        year,
                        month + 1,
                        dayOfMonth
                    )
                    etDate.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // PREVENT PAST DATES
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()

            datePickerDialog.show()
        }


        // TIME PICKER
                etTime.setOnClickListener {
                    val calendar = Calendar.getInstance()

                    TimePickerDialog(
                        requireContext(),
                        { _, hour, minute ->
                            val formattedTime = String.format(
                                "%02d:%02d",
                                hour,
                                minute
                            )
                            etTime.setText(formattedTime)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true // 24-hour format
                    ).show()
                }


        btnSave.setOnClickListener {

            val date = etDate.text.toString().trim()
            val time = etTime.text.toString().trim()
            val title = etTitle.text.toString().trim()
            val notes = etNotes.text.toString().trim()

            etDate.error = null
            etTime.error = null
            etTitle.error = null

            if (date.isEmpty()) {
                etDate.error = "Date is required"
                etDate.requestFocus()
                return@setOnClickListener
            }

            if (time.isEmpty()) {
                etTime.error = "Time is required"
                etTime.requestFocus()
                return@setOnClickListener
            }

            if (title.isEmpty()) {
                etTitle.error = "Title is required"
                etTitle.requestFocus()
                return@setOnClickListener
            }

            Toast.makeText(
                requireContext(),
                "Activity added successfully!",
                Toast.LENGTH_SHORT
            ).show()

            findNavController()
                .navigate(R.id.action_addActivityFragment_to_itineraryFragment)
        }

        btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
