package com.example.enviarmensajetexto

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var editTextMsj: EditText
    private lateinit var textContacto: TextView
    private lateinit var textNumero: TextView
    private lateinit var buttonBuscar: Button
    private lateinit var buttonEnviar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissionSend()

        editTextMsj = findViewById(R.id.editTextTextPersonName)
        textContacto = findViewById(R.id.textViewContacto)
        textNumero = findViewById(R.id.textViewNumero)
        buttonBuscar = findViewById(R.id.buttonBuscar)
        buttonEnviar = findViewById(R.id.buttonEnviar)

        buttonBuscar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            startActivityForResult(intent, 1)
        }

        buttonEnviar.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, editTextMsj.text.toString())
            intent.setPackage("com.whatsapp")
            intent.type = "text/plain"
            startActivity(intent)
        }
    }

    private fun checkPermissionSend() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val contacto: Uri? = data.data
            val cursor: Cursor? = contacto?.let { contentResolver.query(it, null, null, null, null) }
            if (cursor != null && cursor.moveToFirst()) {
                val indiceName: Int = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val indiceNumero: Int = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                val nombre: String = cursor.getString(indiceName)
                var numero: String = cursor.getString(indiceNumero)

                numero = numero.replace("(", "")
                    .replace(")", "")
                    .replace(" ", "")
                    .replace("-", "")
                textContacto.text = nombre
                textNumero.text = numero
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}