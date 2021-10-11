package com.example.headsup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditActivity : AppCompatActivity() {

    var name = ""
    var taboo1 = ""
    var taboo2 = ""
    var taboo3 = ""
    var pk = 0

    lateinit var saveButton: Button
    lateinit var deleteButton: Button
    lateinit var nameEditText: EditText
    lateinit var taboo1EditText: EditText
    lateinit var taboo2EditText: EditText
    lateinit var taboo3EditText: EditText

    lateinit var progressBar: ProgressBar
    lateinit var progressBarTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        //actionbar
        val actionbar = supportActionBar!!
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        //set actionbar title
        actionbar.title = "Edit entry"


        saveButton = findViewById(R.id.btn_save)
        deleteButton = findViewById(R.id.btn_delete)
        nameEditText = findViewById(R.id.edt_nameEdit)
        taboo1EditText = findViewById(R.id.edt_taboo1Edit)
        taboo2EditText = findViewById(R.id.edt_taboo2Edit)
        taboo3EditText = findViewById(R.id.edt_taboo3Edit)

        progressBar = findViewById(R.id.progressBar3)
        progressBarTextView = findViewById(R.id.progressBarText3)
        setProgressBar(false)



        //getting info from previous activity
        name = intent.extras?.getString("celebrityName").toString()
        taboo1 = intent.extras?.getString("taboo1").toString()
        taboo2 = intent.extras?.getString("taboo2").toString()
        taboo3 = intent.extras?.getString("taboo3").toString()
        pk = intent.extras?.getInt("pk")!!

        nameEditText.setText(name)
        taboo1EditText.setText(taboo1)
        taboo2EditText.setText(taboo2)
        taboo3EditText.setText(taboo3)

        saveButton.setOnClickListener {
            val updatedName = nameEditText!!.text.toString()
            val updatedTaboo1 = taboo1EditText!!.text.toString()
            val updatedTaboo2 = taboo2EditText!!.text.toString()
            val updatedTaboo3 = taboo3EditText!!.text.toString()
            if (updatedName != name || updatedTaboo1 != taboo1 || updatedTaboo2 != taboo2 || updatedTaboo3 != taboo3) {
                if (name.trim().length < 2 || taboo1.trim().length < 2 || taboo2.trim().length < 2 || taboo3.trim().length < 2) {
                    Toast.makeText(this, "Please enter valid information", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    var newCelebrity = Celebrities.Celebrity(updatedName, updatedTaboo1, updatedTaboo2, updatedTaboo3)
                    updateCelebrity(newCelebrity, onResult = {
                        nameEditText.clearFocus()
                        taboo1EditText.clearFocus()
                        taboo2EditText.clearFocus()
                        taboo3EditText.clearFocus()
                        Toast.makeText(applicationContext, "Saved Successfully!", Toast.LENGTH_SHORT)
                            .show()
                    })
                }
            }
        }

        deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(this@EditActivity)
            builder.setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Delete") { _, _ ->
                    deleteCelebrity(onResult = {
                        Toast.makeText(applicationContext, "Deleted Successfully!", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    })
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.RED));
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    private fun updateCelebrity(celebrity: Celebrities.Celebrity, onResult: () -> Unit) {
        setProgressBar(true)
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {
            apiInterface.updateCelebrity(pk, celebrity).enqueue(object : Callback<Celebrities.Celebrity> {
                override fun onResponse(call: Call<Celebrities.Celebrity>, response: Response<Celebrities.Celebrity>) {
                    setProgressBar(false)
                    onResult()
                }

                override fun onFailure(call: Call<Celebrities.Celebrity>, t: Throwable) {
                    setProgressBar(false)
                    onResult()
                }
            })
        }

    }

    private fun deleteCelebrity(onResult: () -> Unit) {
        setProgressBar(true)
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {
            apiInterface.deleteCelebrity(pk).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    setProgressBar(false)
                    onResult()
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    setProgressBar(false)
                    onResult()
                }
            })
        }

    }

    private fun setProgressBar(visibility: Boolean) {
        progressBar.isVisible = visibility
        progressBarTextView.isVisible = visibility
        taboo1EditText.isEnabled = !visibility
        taboo2EditText.isEnabled = !visibility
        taboo3EditText.isEnabled = !visibility
        nameEditText.isEnabled = !visibility
        saveButton.isClickable = !visibility
        deleteButton.isClickable = !visibility
    }
}