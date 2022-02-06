package com.ivanhai.suduka

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.ivanhai.suduka.DataClasses.UserRequest
import kotlinx.coroutines.*
import java.lang.Exception
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class EnterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val context = this
        val intent = Intent(context, FindSession::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)

        val button = findViewById<Button>(R.id.button)
        val checkBox = findViewById<CheckBox>(R.id.checkBox)
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        if(sharedPref.contains("1") && sharedPref.contains("2")) {
            val email = AESEncyption.decrypt(sharedPref.getString("1", null) ?: throw(Throwable("null error")) ) ?: throw(Throwable("null error"))
            val password = AESEncyption.decrypt(sharedPref.getString("2", null) ?: throw(Throwable("null error"))) ?: throw(Throwable("null error"))
            MainScope().launch(Dispatchers.IO) {
                val token = userApi.Login(UserRequest(email, password))
                intent.putExtra("token", token.token)
                startActivity(intent)
            }
        }

        button.setOnClickListener {
            val email = findViewById<TextView>(R.id.editTextTextEmailAddress)
            val password = findViewById<TextView>(R.id.editTextTextPassword)
            if(email.text.isEmpty()) email.error = "Empty email" else if(password.text.isEmpty()) password.error = "Empty password" else {
                MainScope().launch(Dispatchers.IO) {
                    val token = userApi.Login(UserRequest(email.text.toString(), password.text.toString()))
                    if(token.token != null && checkBox.isChecked) {
                        with(sharedPref.edit()) {
                            putString("1", AESEncyption.encrypt(email.text.toString()))
                            putString("2", AESEncyption.encrypt(password.text.toString()))
                        }.commit()
                    }
                    intent.putExtra("token", token.token)
                    startActivity(intent)
                }
            }
        }
    }
}

object AESEncyption {

    const val secretKey = "JjVXf3oI2Lx4my8utQWud0hjf3Jrz7zdXMOTnXPlWi=V4BSG"
    const val salt = "bnpHNHRHRm4zekFtTWI2NQ=="
    const val iv = "cU43S0pYSEI2SDRGcVJjbA=="

    fun encrypt(strToEncrypt: String) :  String?
    {
        try
        {
            val ivParameterSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val spec =  PBEKeySpec(secretKey.toCharArray(), Base64.decode(salt, Base64.DEFAULT), 10000, 256)
            val tmp = factory.generateSecret(spec)
            val secretKey =  SecretKeySpec(tmp.encoded, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.toByteArray(Charsets.UTF_8)), Base64.DEFAULT)
        }
        catch (e: Exception)
        {
            println("Error while encrypting: $e")
        }
        return null
    }

    fun decrypt(strToDecrypt : String) : String? {
        try
        {

            val ivParameterSpec =  IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val spec =  PBEKeySpec(secretKey.toCharArray(), Base64.decode(salt, Base64.DEFAULT), 10000, 256)
            val tmp = factory.generateSecret(spec);
            val secretKey =  SecretKeySpec(tmp.encoded, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return  String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)))
        }
        catch (e : Exception) {
            println("Error while decrypting: $e");
        }
        return null
    }
}