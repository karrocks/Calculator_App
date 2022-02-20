package academy.learnprogramming.calculatorapp


import academy.learnprogramming.calculatorapp.databinding.ActivityMainBinding
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Variables to hold the operands and type of calculation
    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            binding.newNumber.append(b.text)
        }

        binding.button0.setOnClickListener(listener)
        binding.button1.setOnClickListener(listener)
        binding.button2.setOnClickListener(listener)
        binding.button3.setOnClickListener(listener)
        binding.button4.setOnClickListener(listener)
        binding.button5.setOnClickListener(listener)
        binding.button6.setOnClickListener(listener)
        binding.button7.setOnClickListener(listener)
        binding.button8.setOnClickListener(listener)
        binding.button9.setOnClickListener(listener)
        binding.buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = binding.newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                binding.newNumber.setText("")
            }
            pendingOperation = op
            binding.operation.text = pendingOperation
        }

        binding.buttonEquals.setOnClickListener(opListener)
        binding.buttonDivide.setOnClickListener(opListener)
        binding.buttonMultiply.setOnClickListener(opListener)
        binding.buttonMinus.setOnClickListener(opListener)
        binding.buttonPlus.setOnClickListener(opListener)

        binding.buttonNeg.setOnClickListener { View ->
            val value = binding.newNumber.text.toString()
            if(value.isEmpty()){
                binding.newNumber.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue*= -1
                    binding.newNumber.setText(doubleValue.toString())

                } catch (e: java.lang.NumberFormatException){
                    //newNumber is "-" or "."
                    binding.newNumber.setText("")
                }
            }
        }
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) operand1 = value else {
            if (pendingOperation == "=") pendingOperation = operation

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) Double.NaN else operand1!! / value
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        binding.result.setText(operand1.toString())
        binding.newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.containsKey(STATE_OPERAND1)) {
            operand1 = savedInstanceState.getDouble(STATE_OPERAND1)
        }

        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION, "=")
        binding.operation.text = pendingOperation
    }
}