package edu.msudenver.kotlinlogin

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.lang.Exception
import java.time.Year

class BikeActivity: AppCompatActivity(), View.OnClickListener {

    var op = CREATE_OP
    lateinit var db: SQLiteDatabase
    lateinit var edtYear:  EditText
    lateinit var edtModel: EditText
    lateinit var edtMake: EditText
    lateinit var edtSize: EditText

    companion object {
        const val CREATE_OP = 0
        const val UPDATE_OP = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike)

        // gets references to the view objects
        edtYear = findViewById(R.id.edtYear)
        edtModel = findViewById(R.id.edtModel)
//        val spnCategory: Spinner = findViewById(R.id.spnCategory)

        edtMake = findViewById(R.id.edtMake)
        edtSize = findViewById(R.id.edtSize)

        // defines the spinner's adapter as an ArrayAdapter of String
        //spnCategory.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Item.CATEGORY_DESCRIPTIONS)

        // gets a reference to the "CREATE/UPDATE" button and sets its listener
        val btnCreateUpdate: Button = findViewById(R.id.btnCreateUpdate)
        btnCreateUpdate.setOnClickListener(this)

        // gets a "writable" db connection
        val dbHelper = BikeDatabaseHelper(this)
        db = dbHelper.writableDatabase

        // gets the operation and updates the view accordingly
        op = intent.getIntExtra("op", CREATE_OP)
        if (op == CREATE_OP)
            btnCreateUpdate.text = "CREATE"
        // TODO #5: write the code for the "update" operation
        else {
            btnCreateUpdate.text = "UPDATE"
            val year = intent.getStringExtra("year") ?: ""
            edtYear.setText(year)
            //edtName.isEnabled = false
            val bike = retrieveItem(year)
            //edtYear.setText(bike.year)
            //spnCategory.setSelection(item.category)
            edtModel.setText(bike.model)
            edtMake.setText(bike.make)
            edtSize.setText(bike.size)
        }
    }

    // returns the item based on the given name
    fun retrieveItem(year: String): Bike {
        val cursor = db.query(
            "bikes",
            null,
            "year = \"${year}\"",
            null,

            null,
            null,
            null
        )
        with (cursor) {
            cursor.moveToNext()
            //val year = cursor.getString(1)
            val make = cursor.getString(1)
            val model = cursor.getString(2)
            val size = cursor.getString(3)
            val bike = Bike(year, make, model, size)
            return bike
        }
    }

    override fun onClick(view: View?) {
        val year = findViewById<EditText>(R.id.edtYear).text.toString()
        val make = findViewById<EditText>(R.id.edtMake).text.toString()
        val model = findViewById<EditText>(R.id.edtModel).text.toString()
        val size = findViewById<EditText>(R.id.edtSize).text.toString()
        if (op == CREATE_OP) {
            try {
                db.execSQL(
                    """
                        INSERT INTO bikes VALUES
                            ("${year}", "${make}", "${model}", "${size}")
                    """
                )
                Toast.makeText(
                    this,
                    "New bicycle is successfully created!",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: Exception) {
                print(ex.toString())
                Toast.makeText(
                    this,
                    "Exception when trying to create a new bicycle!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        // TODO #6: write the code for the "update" operation
        else {
            try {
                db.execSQL(
                    """
                        UPDATE bikes SET
                            make = "${make}", 
                            model = "${model}" 
                            size = "${size}"
                            WHERE year = "${year}"
                        
                    """
                )
                Toast.makeText(
                    this,
                    "Bicycle is successfully updated!",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: Exception) {
                print(ex.toString())
                Toast.makeText(
                    this,
                    "Exception when trying to update the shopping list item!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        finish()
    }
}