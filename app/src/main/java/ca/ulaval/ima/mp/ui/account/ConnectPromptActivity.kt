package ca.ulaval.ima.mp.ui.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.utilities.AccountLogin
import ca.ulaval.ima.mp.utilities.getInstanceAPI

class ConnectPromptActivity : AppCompatActivity() {

    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_prompt)

        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("Connectez-vous")

        initComponent()

    }

    fun initComponent() {
        email = findViewById(R.id.input_courriel)
        password = findViewById(R.id.input_mot_de_passe)
        button = findViewById(R.id.btn_connecter)
        button.setOnClickListener {
            connect(email.text.toString(), password.text.toString())
        }
    }

    fun connect(email : String, pwd : String) {
        val login = AccountLogin(email,pwd)
        val api = getInstanceAPI()
        api.loginAccount(login)
        if (!api.accountDoesntExist && api.connected) {
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
        else if (api.accountDoesntExist) {
            Toast.makeText(this, "Le compte n'existe pas", Toast.LENGTH_SHORT).show()
            api.accountDoesntExist = false
        }
        else if (!api.connected) {
            Toast.makeText(this, "Problème de connexion", Toast.LENGTH_SHORT).show()
        }
    }
}