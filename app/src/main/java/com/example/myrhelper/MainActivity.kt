

package com.example.myrhelper

import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.Toast.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity() : AppCompatActivity(), Parcelable {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "KGK"
    var names: ArrayList<TextView> = arrayListOf()
    var slNos: ArrayList<EditText> = arrayListOf()
    var jackpots: ArrayList<CheckBox> = arrayListOf()
    var deposits: ArrayList<EditText> = arrayListOf()
    var winners: ArrayList<RadioButton> = arrayListOf()
    var points: ArrayList<EditText> = arrayListOf()
    var balences: ArrayList<TextView> = arrayListOf()
    private var stake: String? = ""
    var jackpot: String? = ""
    var strNames: String? = ""
    val nameBalence = arrayOf(
        arrayOf("", ""), arrayOf("", ""), arrayOf("", ""), arrayOf("", ""),
        arrayOf("", ""), arrayOf("", "")
    )
    val arrayList: MutableList<String> = ArrayList()
    val jpotArray = intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1)
    val arrangeName = Array(9) { "" }
    val arrangeBal = Array<String>(9) { "" }
    val selectedNames: MutableList<String> = ArrayList()
    var gameNo = 0
    var Winner = 0
    var nameNo: Int = 0
    var blNo: Int = 0
    var roundOn: Boolean = false
    var slNo =0
   // var hNo =  getHandsNo()

    // val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    constructor(parcel: Parcel) : this() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor = sharedPref.edit()
        if (sharedPref.getInt("lastno", -1) == -1) {
            editor.putInt("lastno", 0).commit()
        }
        findByids()
        disableEditTexts()
        stake = sharedPref.getString(getString(R.string.stakes_key), "defvalue")
        jackpot = sharedPref.getString(getString(R.string.jackpot_key), "defvalue")
        fillNewHands()
        defaultSettings()
        txtName1.setOnLongClickListener(View.OnLongClickListener {
            alertDialogBuild(0)
            true
        })
        txtName2.setOnLongClickListener(View.OnLongClickListener {
            alertDialogBuild(1)
            true
        })
        txtName3.setOnLongClickListener(View.OnLongClickListener {
            alertDialogBuild(2)
            true
        })
        txtName4.setOnLongClickListener(View.OnLongClickListener {
            alertDialogBuild(3)
            true
        })
        txtName5.setOnLongClickListener(View.OnLongClickListener {
            alertDialogBuild(4)
            true
        })
        txtName6.setOnLongClickListener(View.OnLongClickListener {
            alertDialogBuild(5)
            true
        })
        txtName7.setOnLongClickListener(View.OnLongClickListener {
            alertDialogBuild(6)
            true
        })
        txtName8.setOnLongClickListener(View.OnLongClickListener {
            alertDialogBuild(7)
            true
        })
        txtName9.setOnLongClickListener(View.OnLongClickListener {
            alertDialogBuild(8)
            true
        })

    }//onCreate
    override fun onBackPressed() {
        
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
    }

    override fun describeContents(): Int {
        return 0
    }

    fun longClicked(n: Int) {
        makeText(baseContext, " $n Long Clicked", LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.all -> {
                jackpot = "all"
                setJackpots()
                Toast.makeText(this, "$jackpot", Toast.LENGTH_LONG).show()
                true
            }
            R.id.alternate -> {
                jackpot = "alternate"
                setJackpots()
                true
            }
            R.id.none -> {
                jackpot = "none"
                setJackpots()
                true
            }
            R.id.firstlast -> {
                jackpot = "firstLast"
                setJackpots()
                true
            }
            R.id.pointOne -> {
                stake = "0.1"
                true
            }
            R.id.pointFive -> {
                stake = "0.5"
                true
            }
            R.id.one -> {
                stake = "1.0"
                true
            }
            R.id.onePointFive -> {
                stake = "1.5"
                true
            }
            R.id.two -> {
                stake = "2.0"
                true
            }
            R.id.defalt -> {
                defaultSettings()
                true
            }
            R.id.crntSet -> {
                currentSettings()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    companion object CREATOR : Parcelable.Creator<MainActivity> {
        override fun createFromParcel(parcel: Parcel): MainActivity {
            return MainActivity(parcel)
        }

        override fun newArray(size: Int): Array<MainActivity?> {
            return arrayOfNulls(size)
        }
    }


    public fun selectWinner(view: View) {

        for (i in 0 until getHandsNo())
            if (view.id != winners[i].id) {
                winners[i].isChecked = false
                points[i].isEnabled=true
            } else {
                points[i].text.clear()
                points[i].isEnabled=false
                Winner = i
            }
    }

    fun alertDialogBuild(no: Int) {
        if (no>=gameNo) {
            Toast.makeText(
                this, "You can't quit or re enter", Toast.LENGTH_LONG
            ).show()
            return
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Quit/Re Enter")
        builder.setMessage("${names[no].text} wants to Quit or Re Enter")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("ReEnter")
        { dialog: DialogInterface?, which: Int ->
            rejoin(no)

        }
        builder.setPositiveButton("Cancel")
        { dialog: DialogInterface?, which: Int ->
            makeText(this, "Clicked Cancel", LENGTH_LONG).show()
        }
        builder.setNeutralButton("Quit")
        { dialog: DialogInterface?, which: Int ->
            Quit(no)

        }

        builder.setNegativeButton("Re Enter")
        { dialog: DialogInterface?, which: Int ->
            rejoin(no)
            makeText(this, "${nameBalence[0][0]},${nameBalence[0][1]}", LENGTH_LONG).show()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun createPendingResult(requestCode: Int, data: Intent, flags: Int): PendingIntent {
        return super.createPendingResult(requestCode, data, flags)
    }

    override fun closeContextMenu() {
        super.closeContextMenu()
    }

    fun enterDeposit(view: View) {
        for (i in 0..8) {
            var bal: Int = 0
            var dep: Int = 0
            if (!balences[i].text.isEmpty()) bal = balences[i].text.toString().toInt()
            if (!deposits[i].text.isEmpty()) dep = deposits[i].text.toString().toInt()
            bal += dep
            if (bal > 0) balences[i].text = bal.toString()
            deposits[i].text.clear()
        }
    }

    private fun Quit(no: Int) {
        val sharedPref: SharedPreferences =
            getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor =sharedPref.edit()
        if(sharedPref.contains(names[no].text.toString())){
            editor.remove(names[no].text.toString()).apply()
        }
        var hnds: Int = getHandsNo()
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Pay Back")
        builder.setMessage("Pay back ${balences[no].text.toString()}\n     to ${names[no].text.toString()}")
        builder.setPositiveButton("OK", null)
        var dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window?.setLayout(600, 500)

        dialog.window?.setGravity(1000)
      //  Toast.makeText(this,"Pay back ${balences[no].text} to ${names[no].text}",Toast.LENGTH_LONG).show()
        for (i in no until hnds) {
            names[i].text = names[i + 1].text.toString()
            balences[i].text = balences[i + 1].text.toString()
            jpotArray[i]=jpotArray[i + 1]
            jackpots[i].isChecked =jackpots[i + 1].isChecked
            nameNo--
          //  gameNo--
            slNos[hnds - 1].isEnabled=false
        }
        names[hnds].text = ""
        balences[hnds].text = ""
        points[hnds - 1].isEnabled=false
        deposits[hnds - 1].isEnabled=false
        names[gameNo].setTextColor(resources.getColor(R.color.colorBlack))
        gameNo--
        nameNo--
        blNo++
        names[gameNo].setTextColor(resources.getColor(R.color.colorAccent))


    }


    fun rejoin(no: Int) {
        val sharedPref: SharedPreferences =
            getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor =sharedPref.edit()
        editor.putString(
            names[no].text.toString(),
            balences[no].text.toString()
        ).apply()
        nameBalence[blNo][0] = names[no].text.toString()
        nameBalence[blNo][1] = balences[no].text.toString()
        var hnds: Int = getHandsNo()
        Toast.makeText(
            this,
            " ${balences[no].text} of ${names[no].text} sent to memory",
            Toast.LENGTH_LONG
        ).show()
        for (i in no until hnds) {
            names[i].text = names[i + 1].text.toString()
            balences[i].text = balences[i + 1].text.toString()
            jpotArray[i]=jpotArray[i + 1]
            jackpots[i].isChecked =jackpots[i + 1].isChecked
            nameNo--
            //  gameNo--
            slNos[hnds - 1].isEnabled=false
        }
        names[hnds].text = ""
        balences[hnds].text = ""
        points[hnds - 1].isEnabled=false
        deposits[hnds - 1].isEnabled=false
        names[gameNo].setTextColor(resources.getColor(R.color.colorBlack))
        gameNo--
        nameNo--
        blNo++
        names[gameNo].setTextColor(resources.getColor(R.color.colorAccent))

    }

    private fun getHandsNo(): Int {
        var no: Int = 0
        for (i in 0..8)
            if (!names[i].text.isBlank()) no++
        return no
    }

    fun onLongclicked(no: Int) {
        for (i in no until getHandsNo()) {
            makeText(baseContext, "${names[no].text} long Clicked", LENGTH_SHORT).show()
        }
    }

    public fun findByids() {
        names = arrayListOf(
            findViewById(R.id.txtName1),
            findViewById(R.id.txtName2),
            findViewById(R.id.txtName3),
            findViewById(R.id.txtName4),
            findViewById(R.id.txtName5),
            findViewById(R.id.txtName6),
            findViewById(R.id.txtName7),
            findViewById(R.id.txtName8),
            findViewById(R.id.txtName9)
        )
        slNos = arrayListOf(
            findViewById(R.id.edtSl1),
            findViewById(R.id.edtSl2),
            findViewById(R.id.edtSl3),
            findViewById(R.id.edtSl4),
            findViewById(R.id.edtSl5),
            findViewById(R.id.edtSl6),
            findViewById(R.id.edtSl7),
            findViewById(R.id.edtSl8),
            findViewById(R.id.edtSl9)
        )
        jackpots = arrayListOf(
            findViewById(R.id.chk1),
            findViewById(R.id.chk2),
            findViewById(R.id.chk3),
            findViewById(R.id.chk4),
            findViewById(R.id.chk5),
            findViewById(R.id.chk6),
            findViewById(R.id.chk7),
            findViewById(R.id.chk8),
            findViewById(R.id.chk9)
        )
        deposits = arrayListOf(
            findViewById(R.id.edtDep1),
            findViewById(R.id.edtDep2),
            findViewById(R.id.edtDep3),
            findViewById(R.id.edtDep4),
            findViewById(R.id.edtDep5),
            findViewById(R.id.edtDep6),
            findViewById(R.id.edtDep7),
            findViewById(R.id.edtDep8),
            findViewById(R.id.edtDep9)
        )
        winners = arrayListOf(
            findViewById(R.id.rdo1),
            findViewById(R.id.rdo2),
            findViewById(R.id.rdo3),
            findViewById(R.id.rdo4),
            findViewById(R.id.rdo5),
            findViewById(R.id.rdo6),
            findViewById(R.id.rdo7),
            findViewById(R.id.rdo8),
            findViewById(R.id.rdo9)
        )
        points = arrayListOf(
            findViewById(R.id.edtPts1),
            findViewById(R.id.edtPts2),
            findViewById(R.id.edtPts3),
            findViewById(R.id.edtPts4),
            findViewById(R.id.edtPts5),
            findViewById(R.id.edtPts6),
            findViewById(R.id.edtPts7),
            findViewById(R.id.edtPts8),
            findViewById(R.id.edtPts9)
        )
        balences = arrayListOf(
            findViewById(R.id.txtBal1),
            findViewById(R.id.txtBal2),
            findViewById(R.id.txtBal3),
            findViewById(R.id.txtBal4),
            findViewById(R.id.txtBal5),
            findViewById(R.id.txtBal6),
            findViewById(R.id.txtBal7),
            findViewById(R.id.txtBal8),
            findViewById(R.id.txtBal9)
        )
    }

    fun gotoSettings(view: View) {
        val intent = Intent(this, Settings::class.java)
        startActivity(intent)
    }
    fun getBalNo(): Int {
        var no: Int = 0
        for (i in 0..8)
            if (!balences[i].text.isEmpty()) no++
        return no
    }
    private fun fillArrayList() {
        arrayList.clear()
        arrayList.add("")
        var name: String = ""
        var sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor = sharedPref.edit()
        for (i in 0..sharedPref.getInt(getString(R.string.last_no), -1))
        // var no = sharedPref.getInt(getString(R.string.last_no),-1)
        {
            var key: String = (getString(R.string.new_name)) + i
            if (sharedPref.contains(key)) {
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
        spn1.adapter = namesAdapter
        spn1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            //var nameNo:Int =0
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (nameNo == 9) return
                var hand: String = parent?.getItemAtPosition(position).toString()
                if (!isAlreadySelected(hand)) {
                    if(roundOn)insertName(hand)
                    else {
                        names[nameNo].text = hand
                        balences[nameNo].text = getBalance(hand)
                       slNos[nameNo].isEnabled=true
                        nameNo++
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
    }

    fun isAlreadySelected(name: String): Boolean {
        var hasName: Boolean = false

        for (i in 0..getHandsNo()) {
            if (names[i].text == name) hasName = true
        }
        return hasName
    }

    fun getBalance(name: String): String {
        var bal = ""
        var sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor = sharedPref.edit()
        if(sharedPref.contains(name)) {bal=sharedPref.getString(name, "defvalue").toString()
            editor.remove(name)
        }
//        for (i in nameBalence.indices) {
//            if (name == nameBalence[i][0]) bal = nameBalence[i][1]
//        }
        return bal
    }

    fun setJackpots() {
        when (jackpot) {
            "All" -> allJpots()
            "None" -> noneJpots()
            "Alternate" -> alternateJpots()
            "First&Last" -> firstandLast()
        }
    }

    private fun allJpots() {
        for (i in 0 until getHandsNo()) {
            jackpots[i].isChecked = true
            jpotArray[i] = 2
        }
    }

    private fun noneJpots() {
        for (i in 0 until getHandsNo()) {
            jackpots[i].isChecked = false
            jpotArray[i] = 1
        }
    }

    private fun alternateJpots() {
        noneJpots()
        for (i in 0 until getHandsNo() step 2) {
            jackpots[i].isChecked = true
            jpotArray[i] = 2
        }
    }

    private fun firstandLast() {
        noneJpots()
        jpotArray[0] = 2
        jpotArray[getHandsNo() - 1] = 2
        jackpots[0].isChecked=true
        jackpots[getHandsNo() - 1].isChecked=true
    }

   private  fun arrange() {
        for (i in 0 until getHandsNo()) {
            var no: Int = slNos[i].text.toString().toInt()
            if (no == getHandsNo()) {
                arrangeName[0] = names[i].text.toString()
                arrangeBal[0] = balences[i].text.toString()
            } else {
                arrangeName[no] = names[i].text.toString()
                arrangeBal[no] = balences[i].text.toString()
            }
        }
        for (i in 0 until getHandsNo()) {
            names[i].text = arrangeName[i]
            this.balences[i].text = arrangeBal[i]
            points[i].isEnabled=true
            deposits[i].isEnabled=true
        }
        gameNo =0
        winners[0].isChecked=true
        points[0].isEnabled=false
        names[this.gameNo].setTextColor(resources.getColor(R.color.colorAccent))
        for (i in 0..8) slNos[i].text.clear()
        roundOn =true
    }

    fun isValidSlnos(): Boolean {
        var ok: Boolean = true
        for (i in 0 until getHandsNo())
            if (slNos[i].text.isEmpty()) ok = false
        for (i in 1..getHandsNo()) {
            var no = 0
            for (j in 0 until getHandsNo()) {
                if (slNos[j].text.toString().toInt() == i) no++
            }
            if (no > 1) ok = false
        }
        for (i in 0 until getHandsNo())
            if (slNos[i].text.toString().toInt() > getHandsNo() || slNos[i].text.toString()
                    .toInt() < 1
            )
                ok = false
        return ok

    }

    fun arrangeHands(view: View) {

        if (!isValidSlnos())Toast.makeText(this, "check Entries in SlNos", LENGTH_LONG)
            .show()

        else {
            arrange()
            setJackpots()
            gameNo = 0

        }
    }

    fun caliculatePoints(view: View) {
   //     depositsEnter()
        if(! isAllPointsEntered()){
            val customPositionToast =
                Toast.makeText(
                    applicationContext,
                    "enter points to all\n except winner",
                    LENGTH_SHORT
                )
            customPositionToast.setGravity(Gravity.TOP, 100, 1000)

            customPositionToast.show()
            return
        }

        if (gameNo == getHandsNo()) {
            val customPositionToast =
                Toast.makeText(
                    applicationContext,
                    "End of this Round. Start a new Round",
                    LENGTH_SHORT
                )
            customPositionToast.setGravity(Gravity.TOP, 100, 100)

            customPositionToast.show()
            return
        }

        var winBal: Int = if(balences[Winner].text.isEmpty()){0}
        else{ balences[Winner].text.toString().toInt()}

        for (i in 0 until getHandsNo()) {
            if (i != Winner) {

                /* var bal: Int */
                var pts = points[i].text.toString().toInt()
                var stk: Double = stake!!.toDouble()
                var jpt: Int = jpotArray[gameNo]
                var bal = if(balences[i].text.isEmpty()){0}
                   else{ balences[i].text.toString().toInt()}
                var loss = (stk * jpt * pts + 0.5).toInt()
                winBal += loss
                bal -= loss
                if (!deposits[i].text.isEmpty()) {
                    bal += deposits[i].text.toString().toInt()
                }
                balences[i].text =
                    bal.toString()
                points[i].text.clear()
                deposits[i].text.clear()
            }
        }
            if (!deposits[Winner].text.isEmpty()) {
                winBal += deposits[Winner].text.toString().toInt()
            }

            balences[Winner].text = winBal.toString()

            winners[Winner].isChecked = false
            deposits[Winner].text.clear()
            points[Winner].isEnabled=true


        names[gameNo].setTextColor(resources.getColor(R.color.colorBlack))
        gameNo++
        if(gameNo==getHandsNo()) {
            Toast.makeText(this, "Round is over Start new Round", Toast.LENGTH_LONG).show()
            for(i in 0 until getHandsNo()){
                points[i].isEnabled = false
                deposits[i].isEnabled=false
                slNos[i].isEnabled=true

            }
        }
        else
        names[gameNo].setTextColor(resources.getColor(R.color.colorAccent))
    }


    fun enterDeposits(view: View) {
        var netBal: Int =0
        for (i in 0 until getHandsNo()) {
            var bal = balences[i].text.toString().toInt()
            if (!deposits[i].text.isEmpty()) {
                 netBal = deposits[i].text.toString().toInt() + bal

               { netBal = bal }

            }
            balences[i].text=netBal.toString()
        }
    }


    private fun currentSettings() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Current Settings")
        builder.setMessage("Stake:      $stake \nJackpots: $jackpot")
        builder.setPositiveButton("OK", null)
        var dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window?.setLayout(500, 400)

        dialog.window?.setGravity(1000)

    }
    fun defaultSettings(){
        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Default Settings")
        builder.setMessage(
            "Stake:   ${
                sharedPref.getString(
                    getString(R.string.stakes_key),
                    "defValue"
                )
            }" +
                    "\nJackpots: ${
                        sharedPref.getString(
                            getString(R.string.jackpot_key),
                            "devValue"
                        )
                    }"
        )
        builder.setPositiveButton("OK", null)
        var dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window?.setLayout(600, 500)

        dialog.window?.setGravity(1000)

    }

    private fun namesOnLongClick() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Select Quit or Join Later")
        builder.setMessage("Choose to Quit or Rejoin")
        builder.setPositiveButton("Quit") { _, which ->
            makeText(this, "Quit clicked", LENGTH_SHORT).show()
        }
        builder.setNeutralButton("Re Enter") { _, which ->
            makeText(this, "Re Enter clicked", LENGTH_SHORT).show()
            builder.setNegativeButton("Cancel") { _, which ->
                makeText(this, "no action", LENGTH_SHORT).show()
                builder.create()
                builder.show()
            }
        }
    }
    private fun insertName(name: String) {
        for (i in getHandsNo()downTo gameNo-1) {
            names[i].text = names[i - 1].text
            balences[i].text = balences[i - 1].text
            jackpots[i].isChecked=jackpots[i - 1].isChecked
            names[i].setTextColor(names[i - 1].textColors)
            jpotArray[i]=jpotArray[i - 1]
        }
        names[gameNo].text = name
        names[gameNo].setTextColor(resources.getColor(R.color.colorBlack))
        balences[gameNo].text = getBalance(name)
        jackpots[gameNo].isChecked=false
        gameNo++
        slNos[getHandsNo() - 1].isEnabled=true
        winners[0].isChecked=true
        slNos[0].isEnabled=false
    }
    fun isAllPointsEntered():Boolean{
        var entered:Boolean = true
        for(i in 0 until getHandsNo()){
            if(i !=Winner){
                if(points[i].text.isEmpty())
                 entered = false
            }
        }
        return entered
    }
    fun onClickDeal(view: View){
        for(i in 0..8){
            if(jackpots[i].isChecked){
                jpotArray[i]=2
            }
            else jpotArray[i]=1
        }
    }
    fun disableEditTexts(){
        for(i in 0..8){
            slNos[i].isEnabled=false
            points[i].isEnabled=false
            deposits[i].isEnabled=false
        }
    }
    fun winnerMarked():Boolean{
        var marked = true
        for (i in 0 until getHandsNo()){
            if(!winners[i].isChecked)
                marked= false
        }
        return marked
    }
    fun saveBalances(view: View){
        var sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor = sharedPref.edit()
        for(i in 0 until getHandsNo()){
        editor.putString(names[i].text.toString(), balences[i].text.toString())
            names[i].text =""
            balences[i].text=""
        }
    }

}//class


