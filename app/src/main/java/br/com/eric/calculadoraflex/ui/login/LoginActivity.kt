package br.com.eric.calculadoraflex.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.eric.calculadoraflex.R
import br.com.eric.calculadoraflex.ui.form.FormActivity
import br.com.eric.calculadoraflex.ui.signup.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val NEW_USER_REQUEST = 1

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        //refresh do usuario - caso o usuário esteja inativo, não vai conseguir acessar
        mAuth.currentUser?.reload()

        if(mAuth.currentUser != null){
            goToHome()
        }

        btLogin.setOnClickListener {
            mAuth.signInWithEmailAndPassword(
                inputLoginEmail.text.toString(),
                inputLoginPassword.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    goToHome()
                } else {
                    Toast.makeText(this@LoginActivity, it.exception?.message,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }


        btSignup.setOnClickListener {
            val criarConta = Intent(this, SignUpActivity::class.java)
            startActivityForResult(criarConta, NEW_USER_REQUEST)
        }

    }

    private fun goToHome() {
        val intent = Intent(this, FormActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //funciona como o switch-case
        when(requestCode){
            NEW_USER_REQUEST -> {
                when(resultCode){
                    Activity.RESULT_OK -> { inputLoginEmail.setText(data?.getStringExtra("email"))}
                }
            }
            else  -> {}
        }
    }
}
