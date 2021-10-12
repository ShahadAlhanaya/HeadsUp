package com.example.headsup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var floatingActionButton: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var progressBarTextView: TextView

    var celebritiesList = arrayListOf<Celebrities.Celebrity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionButton = findViewById(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            showBottomSheetDialog(this)
        }

        progressBar = findViewById(R.id.progressBar)
        progressBarTextView = findViewById(R.id.progressBarText)
        setProgressBar(false)

        //initialize recyclerView
        recyclerView = findViewById(R.id.rv_celebrities)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CelebritiesAdapter(celebritiesList,this)


        CoroutineScope(Dispatchers.IO).launch {
            getCelebritiesList()
        }
    }

    private fun getCelebritiesList() {

        setProgressBar(true)
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {
            apiInterface.getCelebrities()?.enqueue(object : Callback<List<Celebrities.Celebrity>> {
                override fun onResponse(
                    call: Call<List<Celebrities.Celebrity>>,
                    response: Response<List<Celebrities.Celebrity>>
                ) {
                    setProgressBar(false)
                    Log.d(
                        "GET Response:",
                        response.code().toString() + " " + response.message()
                    )
                    celebritiesList.clear()
                    for (Celebrity in response.body()!!) {
                        celebritiesList.add(Celebrity)
                    }
                    celebritiesList.sortBy{ celebrity: Celebrities.Celebrity -> celebrity.pk}
                    recyclerView.adapter!!.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<List<Celebrities.Celebrity>>, t: Throwable) {
                    setProgressBar(false)
                    Toast.makeText(this@MainActivity, "" + t.message, Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }

    private fun setProgressBar(visibility: Boolean) {
        progressBar.isVisible = visibility
        progressBarTextView.isVisible = visibility
        floatingActionButton.isClickable = !visibility
    }

    private fun showBottomSheetDialog(mainActivity: MainActivity) {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetStyle)
        bottomSheetDialog.setContentView(R.layout.add_celebrity_bottom_sheet)
        val submitButton = bottomSheetDialog.findViewById<Button>(R.id.btn_submit)
        val nameEditText = bottomSheetDialog.findViewById<EditText>(R.id.edt_name)
        val taboo1EditText = bottomSheetDialog.findViewById<EditText>(R.id.edt_taboo1)
        val taboo2EditText = bottomSheetDialog.findViewById<EditText>(R.id.edt_taboo2)
        val taboo3EditText = bottomSheetDialog.findViewById<EditText>(R.id.edt_taboo3)

        submitButton!!.setOnClickListener {
            val name = nameEditText!!.text.toString()
            val taboo1 = taboo1EditText!!.text.toString()
            val taboo2 = taboo2EditText!!.text.toString()
            val taboo3 = taboo3EditText!!.text.toString()
            if (name.trim().length < 2 || taboo1.trim().length < 2 || taboo2.trim().length < 2 || taboo3.trim().length < 2) {
                Toast.makeText(this, "Please enter valid information", Toast.LENGTH_SHORT).show()
            } else {

                var newCelebrity = Celebrities.Celebrity(name, taboo1, taboo2, taboo3)
                postCelebrity(newCelebrity,onResult = {
                    nameEditText.text.clear()
                    taboo1EditText.text.clear()
                    taboo2EditText.text.clear()
                    taboo3EditText.text.clear()
                    Toast.makeText(applicationContext, "Celebrity Added!", Toast.LENGTH_SHORT).show()
                    bottomSheetDialog.dismiss()
                    getCelebritiesList()
                })
            }
        }

        bottomSheetDialog.show()
    }

    private fun postCelebrity(celebrity: Celebrities.Celebrity, onResult: ()-> Unit) {
        setProgressBar(true)
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {
            apiInterface.addCelebrity(celebrity).enqueue(object : Callback<Celebrities.Celebrity> {
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

    override fun onResume() {
        super.onResume()
            getCelebritiesList()
    }
}
