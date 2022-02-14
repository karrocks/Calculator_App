package academy.learnprogramming.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
private const val OPERATION = "operation"
private const val OPERAND1 = "operand1"
private const val OPERAND1_STATE = "operand_state"
class MainActivity : AppCompatActivity() {
    private lateinit var result: EditText
    private lateinit var newNumber: EditText
    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }

    //variables for the operands and the type of the calculation
    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById(R.id.result)
        newNumber = findViewById(R.id.newNumber)

        //variables for numbers
        val button0: Button = findViewById(R.id.button0)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)
        val buttonDot: Button = findViewById(R.id.buttonDot)

        //variables for calculation type
        val buttonEquals: Button = findViewById(R.id.buttonEquals)
        val buttonDivide: Button = findViewById(R.id.buttonDivide)
        val buttonMultiply: Button = findViewById(R.id.buttonMultiply)
        val buttonMinus: Button = findViewById(R.id.buttonMinus)
        val buttonPLus: Button = findViewById(R.id.buttonPlus)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try{
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException){
                newNumber.setText("")
            }
            pendingOperation = op
            displayOperation.text = pendingOperation
        }

        buttonDivide.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonPLus.setOnClickListener(opListener)
        buttonEquals.setOnClickListener(opListener)

    }

    private fun performOperation(value: Double, operation: String)
    {
        if(operand1 == null){
            operand1 = value
        } else {


            if(pendingOperation == "="){
                pendingOperation = operation
            }

            when(pendingOperation){
                "=" -> operand1 = value
                "/" -> operand1 = if(value == 0.0){
                    Double.NaN
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
                /* !! is Null Pointer Exception - only use when sure that the entered value would never be null
                   if it is not used then we cannot perform operations between operand1 and operand2 since they are different
                   types of variables one is double? and other is double so null pointer "exception" is used.
                 */
            }
        }

        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(OPERATION,pendingOperation)

        if(operand1 != null){
            outState.putDouble(OPERAND1,operand1!!)
            outState.putBoolean(OPERAND1_STATE,true)
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        pendingOperation = savedInstanceState.getString(OPERATION,"")
        displayOperation.text = pendingOperation

        operand1 = if(savedInstanceState.getBoolean(OPERAND1_STATE,false)){
            savedInstanceState.getDouble(OPERAND1)
        } else {
            null
        }

    }
}