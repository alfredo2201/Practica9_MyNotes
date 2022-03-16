package perez.isai.misnotas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nota_layout.*
import java.io.*

class MainActivity : AppCompatActivity() {
    var notes = ArrayList<Nota>()
    lateinit var adaptador: AdaptadorNotas
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            var intent = Intent(this,AgregarNotaActivity::class.java)
            startActivityForResult(intent,123)
        }
        readNotes()
        adaptador = AdaptadorNotas(this,notes)
        listView.adapter = adaptador
    }
    private fun readNotes(){
        notes.clear()
        var file = File(location())
        
        if (file.exists()){
            var archives = file.listFiles()
            if (archives!= null){
                for (archive in archives){
                    readFile(archive)
                }
            }
        }

    }

    private fun readFile(archive: File?) {
        val fis = FileInputStream(archive)
        val di = DataInputStream(fis)
        val br = BufferedReader(InputStreamReader(di))
        var strLine: String? = br.readLine()
        var myData =""

        while (strLine != null){
            myData += strLine
            strLine = br.readLine()
        }
        br.close()
        di.close()
        fis.close()
        var name = archive?.name?.substring(0,archive.name.length-4)
        var note = name?.let { Nota(it,myData) }
        note?.let { notes.add(it) }
    }

    private fun location():String{
        val folder = File(getExternalFilesDir(null),"notas")
        if (!folder.exists()){
            folder.mkdir()
        }
        return folder.absolutePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 123){
            readNotes()
            adaptador.notifyDataSetChanged()
        }
    }

}

