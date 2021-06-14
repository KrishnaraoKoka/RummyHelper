package com.example.myrhelper

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_settings2.*

class Settings : AppCompatActivity() {

    private val PRIVATE_MODE = 0
    private val PREF_NAME = "KGK"
    val jackPots = arrayOf("Select","All", "None", "Alternate", "First&Last")
    val Stakes = arrayOf("Select","0.1","0.5", "1.0","1.5","2.0")
    lateinit var spnstakes: Spinner
    val arrayList: MutableList<String> = ArrayList()
    lateinit var spnNames: Spinner
    var namePosition: Int =0
    var str:String =""




    override fun onCreate(savedInstanceState: Bundle?) {
        fillArrayList()
       // arrayList.add("KGK")
        //fillNewHands()
        var sharedPref: SharedPreferences =this.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor = sharedPref.edit()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings2)
         spnNames = findViewById(R.id.spnDeleteNames)
        val txt1 = findViewById<TextView>(R.id.txtTitle)
        val spinner: Spinner = findViewById(R.id.spnStakes)
        val jackpotSpinner: Spinner = findViewById(R.id.spnJackpots)
        var jakpot:String
        var stks:String


        //fillNewHands()
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Stakes)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, p2: Int, id: Long) {
                stks  =Stakes[p2]
                if(stks!="Select") {
                    showStakes.text = stks
                    editor.putString(getString(R.string.stakes_key), stks).apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }

//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                TODO("Not yet implemented")
//            }
        }
        val jparrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jackPots)
        jackpotSpinner.adapter = jparrayAdapter
        jackpotSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long

            ) { jakpot  =jackPots[position]
                if(jakpot!="Select") {
                    showJackpots.text = jakpot
                    editor.putString(getString(R.string.jackpot_key), jakpot).apply()
                }

//                editor.putString(getString(R.string.jackpot_key),jackPots[position]).apply()
//                showJackpots.setText(jackPots[position])

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
        fillNewHands()
    }

    fun gotoMain(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun fillArrayList() {
        arrayList.clear()
        var name: String =""
        var sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
         val editor = sharedPref.edit()
        for(i in 0..sharedPref.getInt(getString(R.string.last_no),-1))
       // var no = sharedPref.getInt(getString(R.string.last_no),-1)
        { var key: String = (getString(R.string.new_name))+i
            if(sharedPref.contains(key)) {
                 name = sharedPref.getString(key, "defvalue").toString()
        arrayList.add(name)
         }
        }
    }
    private fun fillNewHands() {

         fillArrayList()
        val namesAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, arrayList
        )
        spnDeleteNames.adapter = namesAdapter
        selectName()
    }

    fun enterNames(view: View){
        var sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
         val editor = sharedPref.edit()
        var no = sharedPref.getInt(getString(R.string.last_no),-1)
        var key: String = (getString(R.string.new_name))+no
        editor.putString(key,edtEnterHands.text.toString()).apply()
        editor .putInt(getString(R.string.last_no),no+1).commit()
        edtEnterHands.text.clear()
        fillNewHands()
        fillArrayList()
    }

//    override fun getSharedPreferences(prefName: String, privateMode: Int): SharedPreferences {
//
//    }

    private fun selectName(){
        spnDeleteNames.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long)
            {str = arrayList[position]
                txtNameToDelete.text=str}
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
    }
    fun deleteHands(view: View){
        var sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor = sharedPref.edit()
        var no = sharedPref.getInt(getString(R.string.last_no),-1)
        for(i in 0..no){
            var strNameKey: String = getString(R.string.new_name)+i
        if(sharedPref.getString(strNameKey,"defValue")==(txtNameToDelete.text).toString())
                editor.remove(strNameKey).apply()}
        txtNameToDelete.text=""
        fillArrayList()
    }
}
