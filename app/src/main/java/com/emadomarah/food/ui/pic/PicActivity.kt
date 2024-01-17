package com.emadomarah.food.ui.pic

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.loader.content.CursorLoader
import com.emadomarah.food.R
import com.emadomarah.food.model.ImageId
import com.emadomarah.food.network.ApiInterface
import com.emadomarah.food.network.Models.Response.Image.ImageResponse
import com.emadomarah.food.network.Models.Response.ImageId.ImageIdResponse
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date
import java.util.concurrent.TimeUnit

class PicActivity : AppCompatActivity() {
    // Defining Permission codes.
    // We can give any value
    // but unique for each permission.
    private val CAMERA_PERMISSION_CODE = 100
    private val STORAGE_PERMISSION_CODE = 101


    private val GALLERY = 1
    private  var CAMERA:Int = 111
    private val REQUEST_WRITE_EXTERNAL_STORAGE = 1
    var IMAGEFILE: String? = null

    //TRUSTED
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSION_STORAGE = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    var cameraPermission = false
    var readPermission = false
    var writePermission = false


    var imageView: ImageView? = null
    var name: TextView? = null
    var calories: TextView? = null
    var fat: TextView? = null
    var carbs: TextView? = null
    var cholesterol: TextView? = null
    var iron: TextView? = null
    var potassium: TextView? = null
    var sodium: TextView? = null
    var protein: TextView? = null
    var sugars: TextView? = null
    var magnesium: TextView? = null
    var apiInterface: ApiInterface? = null
    var myAmount: EditText? = null
    var retrofit: Retrofit? = null
    var Token = "135b46826a0a3d1580d7674e951e71bd0eeaef88"
    var imageUri: Uri? = null

    var convertedPath: String? = null
    var imageResponseId = 0

    var resultName: String? = null
    var resultCal: kotlin.String? = null
    var resultFat: kotlin.String? = null
    var resultCarb: kotlin.String? = null
    var resultProtein: kotlin.String? = null
    var pref: SharedPreferences? = null


    var newFoodAdded = false

    var activity: Activity = this
    var context: Context? = null

    var db = FirebaseFirestore.getInstance()
    var foodMap: MutableMap<String, Any> = HashMap()
    var date: String? = null
    var title: kotlin.String? = null
    var myPersent = 1.0
    var uId: String? = null
    private var OClient: OkHttpClient? = null
    var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pic)
        window.statusBarColor = ContextCompat.getColor(this, R.color.picstatebar)
        context = applicationContext

        pref = getSharedPreferences("Calories", MODE_PRIVATE)
        uId = pref!!.getString("uId", "")

        verifyStoragePermission(this)

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://api.logmeal.es/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OClient)
            .build()



        readPermission = checkPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE
        )
        writePermission = checkPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,REQUEST_WRITE_EXTERNAL_STORAGE
        )
        cameraPermission = checkPermission(
            Manifest.permission.CAMERA,CAMERA_PERMISSION_CODE
        )

        apiInterface = retrofit!!.create(ApiInterface::class.java)

        initViews()
        //get data from home screen (date , meal name)
        //get data from home screen (date , meal name)
        val extras = intent.extras
        if (extras != null) {
            date = extras.getString("date")
            title = extras.getString("title")
        }

        if (!readPermission) {
            readPermission = checkPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE
            )
        } else {
            Log.d("TEST", "We Access read")
        }


        if (!writePermission) {
            writePermission = checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,REQUEST_WRITE_EXTERNAL_STORAGE
            )
        } else {
            Log.d("TEST", "We Access write")
        }


        if (!cameraPermission) {
            cameraPermission = checkPermission(
                Manifest.permission.CAMERA,CAMERA_PERMISSION_CODE
            )
        } else {
            Log.d("TEST", "We Access write")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            imageUri = data!!.data
            if (requestCode == GALLERY) {
                try {
                    val imageStream = contentResolver.openInputStream(imageUri!!)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    val correctedImage = correctOrientation(selectedImage, imageUri)

                    // Resize the image if needed
                    val resizedImage = resizeImage(correctedImage!!, 1024, 1024) // Adjust dimensions as needed

                    imageView!!.setImageBitmap(resizedImage)

                    // Save the resized image to a temporary file
                    val tempFile = saveBitmapToFile(resizedImage)

                    // Upload the resized image
                    uploadFile(tempFile.absolutePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            } else if (requestCode == CAMERA) {
                data?.extras?.getParcelable<Bitmap>("data")?.let { thumbnail ->
                    imageView!!.setImageBitmap(thumbnail)

                    // Resize the image if needed
                    val resizedImage = resizeImage(thumbnail, 1024, 1024) // Adjust dimensions as needed

                    // Save the resized image to a temporary file
                    val tempFile = saveBitmapToFile(resizedImage)

                    // Upload the resized image
                    uploadFile(tempFile.absolutePath)
                }
            } else {
            }
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to resize a Bitmap to the specified dimensions
    private fun resizeImage(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val scaleWidth = minOf(maxWidth.toFloat() / width, 1.0f)
        val scaleHeight = minOf(maxHeight.toFloat() / height, 1.0f)

        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    // Function to save a Bitmap to a temporary file
    private fun saveBitmapToFile(bitmap: Bitmap): File {
        val tempFile = File.createTempFile("temp_image", ".jpg")
        val outputStream = FileOutputStream(tempFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.close()
        return tempFile
    }
    fun getRealPathFromURI(contentUri: Uri?): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)

        //This method was deprecated in API level 11
        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        val cursorLoader = CursorLoader(
            this,
            contentUri!!, proj, null, null, null
        )
        val cursor = cursorLoader.loadInBackground()
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    fun PicOnClick(view: View) {
        when (view.id) {
            R.id.upload_btn -> {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, GALLERY)
            }

            R.id.take_btn -> {
                val photoPickerIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(photoPickerIntent, CAMERA)
            }

            R.id.rec_back_btn -> onBackPressed()
            R.id.add_btn -> if (resultName != null) {
                MakeUserDataToMap()
                db.collection("food/$uId/dates")
                    .document(date!!)
                    .collection(title!!)
                    .document(resultName!!)
                    .set(foodMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Food Added", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun MakeUserDataToMap() {
        foodMap.put("foodName", resultName!!)
        if (!myAmount!!.text.toString().isEmpty()) {
            val a = myAmount!!.text.toString().toDouble()
            myPersent = a / 100
            var cal = java.lang.Double.valueOf(resultCal) * myPersent
            cal = Math.round(cal * 100.0).toDouble() / 100.0
            foodMap.put("calories", cal.toString())
            var car = java.lang.Double.valueOf(resultCarb) * myPersent
            car = Math.round(car * 100.0).toDouble() / 100.0
            foodMap.put("carb", car.toString())
            var prot = java.lang.Double.valueOf(resultProtein) * myPersent
            prot = Math.round(prot * 100.0).toDouble() / 100.0
            foodMap.put("protien", prot.toString())
            var f = java.lang.Double.valueOf(resultFat) * myPersent
            f = Math.round(f * 100.0).toDouble() / 100.0
            foodMap.put("fat", f.toString())
            foodMap.put("amount", myAmount!!.text.toString())
        } else {
            foodMap.put("calories", resultCal!!)
            foodMap.put("carb", resultCarb!!)
            foodMap.put("protien", resultProtein!!)
            foodMap.put("fat", resultFat!!)
            foodMap.put("amount", "100")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode ==STORAGE_PERMISSION_CODE) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(applicationContext, "Storage Permission Granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(applicationContext, "Storage Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    fun initViews() {
        imageView = findViewById(R.id.image_view)
        name = findViewById(R.id.foodName)
        calories = findViewById(R.id.foodCalories)
        carbs = findViewById(R.id.foodCarbs)
        cholesterol = findViewById(R.id.foodCholesterol)
        iron = findViewById(R.id.foodIron)
        sodium = findViewById(R.id.foodSodium)
        protein = findViewById(R.id.foodProtein)
        sugars = findViewById(R.id.foodSugars)
        potassium = findViewById(R.id.foodPotassium)
        fat = findViewById(R.id.foodFat)
        myAmount = findViewById(R.id.input_food_amount)
    }


    private fun uploadFile(path: String?) {
        val file = File(path)
        // create RequestBody instance from file
        val requestFile = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            file
        )
        // MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        //File file = FileUtils.getFile(this, imageUri);
        val call: Call<ImageResponse> = apiInterface!!.uploadImage("Bearer $Token", body)

        call.enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                if (response.isSuccessful()) {
                    imageResponseId = response.body()!!.imageId
                imageRecognition(imageResponseId)
                } else {
                    Toast.makeText(activity.applicationContext, response.message().toString(), Toast.LENGTH_SHORT)
                        .show()
                    // progressDialog.dismiss();
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                println("ERROR :$t")
                Toast.makeText(activity.applicationContext, t.message.toString(), Toast.LENGTH_SHORT).show()
                //progressDialog.dismiss();
            }
        })
    }

    private fun imageRecognition(imageResponseId: Int) {
        val call: Call<ImageIdResponse> =
            apiInterface!!.getImageInfo("Bearer $Token", ImageId(imageResponseId))
        call.enqueue(object : Callback<ImageIdResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ImageIdResponse>,
                response: Response<ImageIdResponse>
            ) {
                if (response.isSuccessful()) {
                    Log.d("TEST","We Have Info" )

                    //calcium,carbs,cholesterol,energy,iron,potassium magnesium,sodium,protein,sugars;
                    resultName = response.body()?.getFoodName().toString()
                    name!!.text = resultName
                    resultCal =
                        java.lang.String.valueOf(response.body()?.getNutritionalInfo()?.getCalories())
                    calories!!.text = resultCal
                    resultCarb = java.lang.String.valueOf(
                        response.body()?.getNutritionalInfo()?.getTotalNutrients()?.getCHOCDF()
                            ?.getQuantity()
                    )
                    carbs!!.text = "$resultCarb g"
                    cholesterol?.setText(
                        java.lang.String.valueOf(
                            response.body()?.getNutritionalInfo()?.getTotalNutrients()?.getCHOLE()
                                ?.getQuantity()
                        ) + " mg"
                    )
                    iron?.setText(
                        java.lang.String.valueOf(
                            response.body()?.getNutritionalInfo()?.getTotalNutrients()?.getFE()
                                ?.getQuantity()
                        ) + " mg"
                    )
                    potassium?.setText(
                        java.lang.String.valueOf(
                            response.body()?.getNutritionalInfo()?.getTotalNutrients()?.getK()
                                ?.getQuantity()
                        ) + " mg"
                    )
                    sodium?.setText(
                        java.lang.String.valueOf(
                            response.body()?.getNutritionalInfo()?.getTotalNutrients()?.getNA()
                                ?.getQuantity()
                        ) + " mg"
                    )
                    resultProtein = java.lang.String.valueOf(
                        response.body()?.getNutritionalInfo()?.getTotalNutrients()?.getPROCNT()
                            ?.getQuantity()
                    )
                    protein!!.text = "$resultProtein g"
                    sugars?.setText(
                        java.lang.String.valueOf(
                            response.body()?.getNutritionalInfo()?.getTotalNutrients()?.getSUGAR()
                                ?.getQuantity()
                        ) + " g"
                    )
                    resultFat = java.lang.String.valueOf(
                        response.body()?.getNutritionalInfo()?.getTotalNutrients()?.getFAT()
                            ?.getQuantity()
                    )
                    fat!!.text = "$resultFat g"
                } else {
                    println("Image ID : $response")
                    Toast.makeText(activity.applicationContext, response.message().toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                // progressDialog.dismiss();
            }

            override fun onFailure(call: Call<ImageIdResponse>, t: Throwable) {
                println("Failed : " + t.message.toString())
                Toast.makeText(activity.applicationContext, t.message.toString(), Toast.LENGTH_SHORT).show()
                // progressDialog.dismiss();
            }
        })
    }

    //TWO  Function to check and request permission.
    fun checkPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION_CODE
            )
        } else {
        }
        return false
    }

    private fun verifyStoragePermission(activity: Activity) {
        val permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION_CODE
            )
        } else {
        }
    }

    fun saveImage(myBitmap: Bitmap?): String {
        val dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/FoodImages"
        val date = Date()
        val format = DateFormat.format("yyyy-MM-dd_HH:mm:ss", date) // Corrected date format
        var imageFilePath = ""

        try {
            val fileDir = File(dirPath)
            if (!fileDir.exists()) {
                fileDir.mkdirs() // Use mkdirs() to create directories recursively
            }

            val path = "$dirPath/food-$format.jpeg"
            val imageFile = File(path)

            myBitmap?.let { bitmap ->
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos)
                val bitmapdata = bos.toByteArray()

                FileOutputStream(imageFile).use { fos ->
                    fos.write(bitmapdata)
                    imageFilePath = imageFile.path
                }
            } ?: run {
                println("Bitmap is null")
            }
        } catch (e1: IOException) {
            e1.printStackTrace()
            println("Error: ${e1.message}")
            // Handle error: Inform user or perform necessary actions
        }
        return imageFilePath
    }

//    fun saveImage(myBitmap: Bitmap?): String {
//        val dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/FoodImages"
//        val date = Date()
//        val format = DateFormat.format("yyyy-MM-dd_HH:mm:ss", date) // Corrected date format
//        var imageFilePath = ""
//
//        try {
//            val fileDir = File(dirPath)
//            if (!fileDir.exists()) {
//                fileDir.mkdirs() // Use mkdirs() to create directories recursively
//            }
//
//            val path = "$dirPath/food-$format.jpeg"
//            val imageFile = File(path)
//
//            myBitmap?.let { bitmap ->
//                val bos = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos)
//                val bitmapdata = bos.toByteArray()
//
//                val fos = FileOutputStream(imageFile)
//                fos.write(bitmapdata)
//                fos.flush()
//                fos.close()
//
//                imageFilePath = imageFile.path
//            } ?: run {
//                println("Bitmap is null")
//            }
//        } catch (e1: IOException) {
//            e1.printStackTrace()
//            println("Error: ${e1.message}")
//        }
//        return imageFilePath
//    }

    private fun correctOrientation(bitmap: Bitmap?, photoUri: Uri?): Bitmap? {
        try {
            val inputStream = contentResolver.openInputStream(photoUri!!)
            val ei = ExifInterface(inputStream!!)
            val orientation: Int = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            val rotatedBitmap: Bitmap?
            rotatedBitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                ExifInterface.ORIENTATION_NORMAL -> bitmap
                else -> bitmap
            }
            inputStream?.close()
            return rotatedBitmap
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    private fun rotateImage(source: Bitmap?, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source!!, 0, 0, source.width, source.height,
            matrix, true
        )
    }


}