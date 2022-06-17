package cr.ac.baselab

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile

class MainActivity : AppCompatActivity() {
    private var mp3 = 0


    companion object{
        var OPEN_DIRECTORY_REQUEST_CODE = 1
    }
    private lateinit var nombre: TextView
    private lateinit var buttonPlay: Button
    private lateinit var buttonPause: Button
    private lateinit var buttonNext: Button
    private lateinit var buttonPrevious: Button
    private var stop=true

    lateinit var rootTree : DocumentFile

    private lateinit var mediaPlayer: MediaPlayer



    private var Archivos:MutableList<DocumentFile> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)

        buttonPlay = findViewById(R.id.buttonPlay)
        buttonPause = findViewById(R.id.buttonPause)
        buttonNext = findViewById(R.id.buttonNext)
        buttonPrevious = findViewById(R.id.buttonPrev)
        nombre = findViewById(R.id.textView)

        setOnClickListeners(this)
    }
    private fun setOnClickListeners(context: Context) {
        buttonPlay.setOnClickListener {
            if(stop){
                stop=false
                mediaPlayer.start()
                Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show()
                nombre.text = Archivos[mp3].name.toString()
            }

        }
        buttonPause.setOnClickListener {

            stop=true
            mediaPlayer.pause()
            Toast.makeText(context, "Pause", Toast.LENGTH_SHORT).show()
            mediaPlayer = MediaPlayer.create(this,Archivos[mp3].uri )
            nombre.text = Archivos[mp3].name.toString()
        }

        buttonNext.setOnClickListener {
            if(mp3+1>Archivos.size-1){
                mediaPlayer.pause()
                mp3=0
                mediaPlayer = MediaPlayer.create(context,Archivos[mp3].uri )
                mediaPlayer.start()
                nombre.text = Archivos[mp3].name.toString()
            }else{
                mediaPlayer.pause()
                mp3++
                mediaPlayer = MediaPlayer.create(context,Archivos[mp3].uri )
                mediaPlayer.start()
                nombre.text = Archivos[mp3].name.toString()
            }

        }

        buttonPrevious.setOnClickListener {
            if(mp3-1<0){
                mediaPlayer.pause()
                mp3=Archivos.size-1
                Toast.makeText(context, "Anterior", Toast.LENGTH_SHORT).show()
                mediaPlayer = MediaPlayer.create(context,Archivos[mp3].uri )
                mediaPlayer.start()
                nombre.text = Archivos[mp3].name.toString()
            }else{
                mediaPlayer.pause()
                mp3--
                mediaPlayer = MediaPlayer.create(context,Archivos[mp3].uri )
                mediaPlayer.start()
                nombre.text = Archivos[mp3].name.toString()

            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == OPEN_DIRECTORY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                var directoryUri = data?.data ?: return
                //Log.e("Direc" , directoryUri.toString())
               rootTree = DocumentFile.fromTreeUri(this, directoryUri)!!

                for (file in rootTree!!.listFiles()){
                    try {
                        file.name?.let { Log.e("Archivo", it) }
                        Archivos.add(file)
                    }catch (e: Exception){
                        Log.e("Error","No pude ejecutar el archivo"+file.uri)
                    }
                }
                mediaPlayer = MediaPlayer.create(this,Archivos[mp3].uri )
            }
        }
    }
}