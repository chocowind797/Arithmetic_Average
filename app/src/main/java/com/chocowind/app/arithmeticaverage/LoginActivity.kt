package com.chocowind.app.arithmeticaverage

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import com.chocowind.app.arithmeticaverage.db.PayDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var db: PayDatabase
    var payerAmount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setTitle(R.string.activity_login_title_txt)

        context = this
        db = PayDatabase.getDatabase(context)
    }

    fun option(view: View) {
        if (view.tag == "calc") {
            val editText = EditText(context)
            editText.gravity = EditText.TEXT_ALIGNMENT_CENTER

            GlobalScope.launch {
                payerAmount = db.payDao().getAllPayers().size
            }

            AlertDialog.Builder(context)
                .setTitle(R.string.login_query_alert_title)
                .setView(editText)
                .setPositiveButton(R.string.add_alert_confirm_btn) { _, _ ->
                    if (editText.text.isNotEmpty() && editText.text.toString().isDigitsOnly()) {
                        val amount = editText.text.toString().toInt()
                        if (payerAmount > amount) {
                            Toast.makeText(
                                context,
                                R.string.login_query_wrong_amount,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (payerAmount == 0) {
                            Toast.makeText(
                                context,
                                R.string.login_query_empty_amount,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val intent = Intent(context, CalcActivity::class.java)
                            intent.putExtra("amount", editText.text.toString().toInt())
                            startActivity(intent)
                        }
                    } else
                        Toast.makeText(
                            context,
                            R.string.login_query_null_amount,
                            Toast.LENGTH_SHORT
                        ).show()
                }
                .create()
                .show()
        } else {
            val intent: Intent? = when (view.tag) {
                "add" -> Intent(context, AddActivity::class.java)
                "query" -> Intent(context, QueryActivity::class.java)
                else -> null
            }

            if (intent == null) {
                AlertDialog.Builder(context)
                    .setTitle(R.string.null_alert_title)
                    .setMessage(R.string.null_alert_message)
                    .setPositiveButton(R.string.null_alert_ok, null)
                    .create()
                    .show()
            } else
                startActivity(intent)
        }
    }
}