package com.example.tipcalculatortfnew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import android.widget.TimePicker

class MainActivity : AppCompatActivity() {
    private lateinit var baseInput:EditText
    private lateinit var tipPercentTv:TextView
    private lateinit var tipAmountTv:TextView
    private lateinit var totalTv:TextView
    private lateinit var splitAmountTv:TextView
    private lateinit var tipSeekBar: SeekBar
    private lateinit var partyPicker:NumberPicker
    private lateinit var splitSwitch:Switch
    private lateinit var tipQualityTextView: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureUI()
        setListeners()
    }
    private fun configureUI() {
        baseInput = findViewById(R.id.baseInput)
        tipSeekBar = findViewById(R.id.tipSeekBar)
        tipPercentTv = findViewById(R.id.tippercentShowTv)
        tipAmountTv = findViewById(R.id.tipShowTv)
        totalTv = findViewById(R.id.totalShowTv)
        partyPicker = findViewById(R.id.partyPicker)
        splitAmountTv = findViewById(R.id.splitShowTv)
        splitSwitch = findViewById(R.id.splitSwitch)
        tipQualityTextView=findViewById(R.id.tipQualityTextView)

    }

    private fun setListeners(){
        baseInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateAndDisplay()
            }
        }


           /* override fun afterTextChanged(s: Editable?) {
                calculateAndDisplay()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }*/
        )

        tipSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val tipPercentage = progress.toDouble()
                tipPercentTv.text = "$progress"
                updateTipQualityTextView(tipPercentage, tipSeekBar)
                calculateAndDisplay()  //  update the calculations when the SeekBar changes
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })



        partyPicker.minValue=1
        partyPicker.maxValue=20
        partyPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            val numGuest=partyPicker.value
            calculateAndDisplay()
        }

        splitSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Switch is ON, calculate the split amount
                calculateAndDisplay()
                val numGuest = partyPicker.value
                val splitAmount: Double = (totalTv.text.toString().toDouble())/ numGuest

               // splitAmountTv.text = "Each person pays: $" + "${splitAmount.toString()}."*/
                updateSplitAmountTextView(splitAmount)

            } else {
                splitAmountTv.text = ""
            }
        }


    }

    private fun calculateAndDisplay() {
        val baseAmount = baseInput.text.toString().toDouble()
        val tipPercent: Double = tipSeekBar.progress.toDouble()
        val tipAmount: Double = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        tipPercentTv.text = tipPercent.toString()
        tipAmountTv.text = tipAmount.toString()
        totalTv.text = totalAmount.toString()
        if (splitSwitch.isChecked) {
            val numGuest = partyPicker.value
            val splitAmount: Double = totalAmount / numGuest
            updateSplitAmountTextView(splitAmount)
        } else {
            splitAmountTv.visibility = View.GONE
        }
    }

    private fun updateTipQualityTextView(tipPercentage: Double, seekBar: SeekBar) {
        tipQualityTextView.text = when (tipPercentage) {
            in 0.0..10.0 -> "Poor"
            in 10.1..15.0 -> "Good"
            else -> "Amazing"
        }

        // Calculate the horizontal position for the TextView based on the SeekBar progress
        val progressRatio = seekBar.progress.toFloat() / seekBar.max
        val leftOffset = (seekBar.width * progressRatio).toInt() - (tipQualityTextView.width / 2)
        tipQualityTextView.translationX = leftOffset.toFloat()
    }

    private fun updateSplitAmountTextView(splitAmount:Double){
        splitAmountTv.text = getString(R.string.split_amount_format, splitAmount)
        splitAmountTv.visibility = View.VISIBLE

    }

    }

