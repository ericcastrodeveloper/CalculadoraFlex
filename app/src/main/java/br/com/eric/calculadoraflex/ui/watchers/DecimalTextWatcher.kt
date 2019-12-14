package br.com.eric.calculadoraflex.ui.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.text.DecimalFormat

class DecimalTextWatcher(editText: EditText, val totalDecimalNumber: Int = 2) :
    TextWatcher {

    private val editTextWeakReference: WeakReference<EditText> =
        WeakReference(editText)
    init {
        formatNumber(editTextWeakReference.get()!!.text)
    }

    override fun afterTextChanged(editable: Editable) {

        formatNumber(editable)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    private fun getTotalDecimalNumber(): String {
        val decimalNumber = StringBuilder()
        for (i in 1..totalDecimalNumber) {
            decimalNumber.append("0")
        }
        return decimalNumber.toString()
    }

    private fun formatNumber(editable: Editable) {
        val editText = editTextWeakReference.get() ?: return
        val cleanString = editable.toString().trim().replace(" ", "")
        editText.removeTextChangedListener(this)
        val number = Math.pow(10.toDouble(), totalDecimalNumber.toDouble())
        val parsed = when (cleanString) {
            null -> BigDecimal(0)
            "" -> BigDecimal(0)
            "null" -> BigDecimal(0)
            else -> BigDecimal(cleanString.replace("\\D+".toRegex(), ""))
                .setScale(totalDecimalNumber, BigDecimal.ROUND_FLOOR)
                .divide(
                    BigDecimal(number.toInt()),
                    BigDecimal.ROUND_FLOOR
                )
        }
        val dfnd = DecimalFormat("#,##0.${getTotalDecimalNumber()}")
        val formatted = dfnd.format(parsed)
        editText.setText(formatted.replace(',', '.'))
        editText.setSelection(formatted.length)
        editText.addTextChangedListener(this)
    }



}