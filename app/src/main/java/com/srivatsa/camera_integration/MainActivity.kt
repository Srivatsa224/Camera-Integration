package com.srivatsa.camera_integration

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
private const val TAG="MainActivity"
private const val REQUEST_CODE=67
lateinit var photoFile:File
private const val FILE_NAME="photo.jpg"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTakePicture.setOnClickListener {
            val takePictureIntent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            Log.i(TAG,"Pic taken")
            photoFile=getPhotoFile(FILE_NAME)

            //starting with Android 10, this won't work/ API 24
            //Private file uri resources can't be shared b/w
            //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)

            val fileProvider= FileProvider.getUriForFile(this,
                "com.srivatsa.camera_integration.fileprovider",
                photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileProvider)

            if(takePictureIntent.resolveActivity(this.packageManager)!=null){
                startActivityForResult(takePictureIntent, REQUEST_CODE)
                }
                else
                {
                    Toast.makeText(this,"Unable to open the camera", Toast.LENGTH_SHORT).show()
                }





        }



    }

    private fun getPhotoFile(fileName: String): File {
        // Use 'getExternalFileDir on Context to access package-specific directories.
        val storageDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName,".jpg",storageDir)
        Log.i(TAG,"storage")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode== REQUEST_CODE && resultCode==Activity.RESULT_OK){
           //nullable, so ? and casting as bitmap
         //   val imagetaken= data?.extras?.get("data") as Bitmap
            val takenImage=BitmapFactory.decodeFile(photoFile.absolutePath)
            imageView.setImageBitmap(takenImage)
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }


    }
}
