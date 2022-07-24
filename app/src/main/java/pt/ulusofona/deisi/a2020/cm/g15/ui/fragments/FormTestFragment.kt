package pt.ulusofona.deisi.a2020.cm.g15.ui.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_form_test.*
import kotlinx.android.synthetic.main.fragment_form_test.view.*
import pt.ulusofona.deisi.a2020.cm.g15.R
import pt.ulusofona.deisi.a2020.cm.g15.data.room.entities.TestEnt
import pt.ulusofona.deisi.a2020.cm.g15.ui.viewmodels.TestViewModel
import java.util.*


private const val REQUEST_CODE = 200
class FormTestFragment : Fragment(R.layout.fragment_form_test), DatePickerDialog.OnDateSetListener{

    private var image = 0
    private var result = false
    private var location = ""
    private var date = ""

    private lateinit var testViewModel: TestViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_form_test, container, false)

        testViewModel = ViewModelProvider(this).get(TestViewModel::class.java)

        /*view.imageTest.setOnClickListener {
            imageTest.setImageResource(R.drawable.test_image)
            image = R.drawable.test_image
        }*/

        view.take_picture_button.setOnClickListener{
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            startActivityForResult(takePicture, REQUEST_CODE)
        }

        view.edit_testDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(activity as Context, this, year, month, day).show()
        }

        view.button_submit.setOnClickListener {
            getRadioButtonSelected()
            if (getAndValidateLocality() && getAndValidateDate()) {
                insertDataToDatabase()
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenIamge = data?.extras?.get("data") as Bitmap
            imageTest.setImageBitmap(takenIamge)
            image = R.drawable.ic_default_image
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun insertDataToDatabase() {
        val testIcon = if (result) R.drawable.ic_plus_circle else R.drawable.ic_minus_circle

        val test = TestEnt(result, location, date, testIcon, image)
        testViewModel.addTest(test)

        Toast.makeText(activity as Context, getString(R.string.registeredTest), Toast.LENGTH_SHORT).show()
        val action = FormTestFragmentDirections.actionFormTestFragmentToTestsFragment()
        findNavController().navigate(action)
    }

    private fun getRadioButtonSelected() {
        val selectedId = radioGroupResult.checkedRadioButtonId
        val id = resources.getResourceEntryName(selectedId)

        result = id == "Positive"
    }

    private fun getAndValidateLocality(): Boolean {
        location = edit_testLocality.text.toString().trim()

        if (location.isEmpty()) {
            input_testLocality.error = getString(R.string.field_empty)
            return false
        } else {
            input_testLocality.error = null
            return true
        }
    }

    private fun getAndValidateDate(): Boolean {
        val date = edit_testDate.text.toString().trim()

        if (date.isEmpty()) {
            input_testDate.error = getString(R.string.field_empty)
            return false
        } else {
            input_testDate.error = null
            return true
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        if(dayOfMonth < 10 && month < 10) {
            date = "$year-0${month+1}-0$dayOfMonth"

        } else if(month < 10) {
            date = "$year-0${month+1}-$dayOfMonth"

        }else if(dayOfMonth < 10) {
            date = "$year-${month+1}-0$dayOfMonth"

        }else if(dayOfMonth >= 10 && month >= 10) {
            date = "$year-${month+1}-$dayOfMonth"
        }

        edit_testDate.setText(date)
    }

}