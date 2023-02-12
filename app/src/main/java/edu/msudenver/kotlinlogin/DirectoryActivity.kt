package edu.msudenver.kotlinlogin

//package edu.msudenver.kotlinlogin

import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import java.lang.Exception
import java.time.Year


class DirectoryActivity : AppCompatActivity(),View.OnClickListener,View.OnLongClickListener
{ // End DirectoryActivity class
    // Drawer toggle variables
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var recyclerView: RecyclerView
    lateinit var dbHelper: BikeDatabaseHelper

    // creates the ItemHolder inner class
    private inner class ItemHolder(view: View): RecyclerView.ViewHolder(view) {
        val bikeYear: TextView     = view.findViewById(R.id.bikeYear)
        val bikeMake: TextView = view.findViewById(R.id.bikeMake)
        val bikeModel: TextView = view.findViewById(R.id.bikeModel)
        val bikeSize: TextView = view.findViewById(R.id.bikeSize)
    }
    // creates the ItemAdapter inner class
    // TODO #2: add an OnLongClickListener parameter
    private inner class ItemAdapter(var bikes: List<Bike>, var onClickListener: View.OnClickListener
    , var onLongClickListener: View.OnLongClickListener): RecyclerView.Adapter<ItemHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.bikedetail, parent, false)
            return ItemHolder(view)
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            val bike = bikes[position]
            holder.bikeYear.text = bike.year
            holder.bikeMake.text = bike.make
            holder.bikeModel.text = bike.model
            holder.bikeSize.text = bike.size

            // sets the holder's listener
            holder.itemView.setOnClickListener(onClickListener)

            // TODO #3: set the holder's (long click) listener
            holder.itemView.setOnLongClickListener(onLongClickListener)
        }

        override fun getItemCount(): Int {
            return bikes.size
        }
    }
    // populates the recycler view
    fun populateRecyclerView() {
        val db = dbHelper.readableDatabase
        val bikes = mutableListOf<Bike>()
        val cursor = db.query(
            "bikes",
            null,
            null,
            null,
            null,
            null,
            null
        )
        with (cursor) {
            while (moveToNext()) {
                val year    = getString(0)
                val make = getString(1)
                val model = getString(2)
                val size     = getString(3)
                val bike = Bike(year, make, model, size)
                bikes.add(bike)
            }
        }
        recyclerView.adapter = ItemAdapter(bikes, this, this)
    }



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory)
        // creates and populates the recycler view
        dbHelper = BikeDatabaseHelper(this)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        populateRecyclerView()
        // initializes the floating action button
        val fabCreate: FloatingActionButton = findViewById(R.id.fabCreate)
        fabCreate.setOnClickListener {
            // calls CreateUpdateActivity for create
            val intent = Intent(this, BikeActivity::class.java)
            intent.putExtra("op", BikeActivity.CREATE_OP)
            startActivity(intent)
        }

        // Intents
        val intentLogOut = Intent(this, MainActivity::class.java)
        val intentHomeScreen = Intent(this, DirectoryActivity::class.java)

        // Variables to locate and toggle drawer
        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.open,
                R.string.close)

        // Pass Open and Close toggle for drawer layout listener
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // Make Navigation drawer icon always appear on action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Sidebar
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener {
            when (it.itemId)
            {
                // Home Screen
                R.id.nav_home ->
                {
                    startActivity(intentHomeScreen)
                } // End R.id.nav_home

                // Account Page
                R.id.nav_account ->
                {
                    Toast.makeText(this, "Account", Toast.LENGTH_SHORT).show()
                } // End R.id.nav_account

                // Settings
                R.id.nav_settings ->
                {
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                } // End R.id.nav_settings

                // Log out
                R.id.nav_logout ->
                {
                    startActivity(intentLogOut)
                    Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT)
                        .show()
                } // End R.id.nav_logout

            } // End when
            true
        } // End navView.setNavigationItemSelectedListener

//        // Set onclick listener on bike button
//        bikeButton.setOnClickListener { v ->
//            val intent1 = Intent()
//            intent1.setClass(v.context, BikeActivity::class.java)
//            startActivity(intent1)
//        }
    } // End onCreate
    // this method is called when CreateUpdateActivity finishes
    override fun onResume() {
        super.onResume()
        populateRecyclerView()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("dbHelper", dbHelper)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        dbHelper = if (savedInstanceState.containsKey("dbHelper")) savedInstanceState.getSerializable("dbHelper") as BikeDatabaseHelper else BikeDatabaseHelper(this)
    }

    // call CreateUpdateActivity for update
    override fun onClick(view: View?) {
        if (view != null) {
            val year = view.findViewById<TextView>(R.id.bikeYear).text
            val intent = Intent(this, BikeActivity::class.java)
            intent.putExtra("op", BikeActivity.UPDATE_OP)
            intent.putExtra("year", year)
            startActivity(intent)
        }
    }


    // Override function to implement item click listener callback to open
    // and close the navigation drawer when the icon is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            true
        } // End return if
        else super.onOptionsItemSelected(item)
    } // End onOptionsItemSelected

    // TODO #4: implement the onLongClick method
    override fun onLongClick(view: View?): Boolean {

        class MyDialogInterfaceListener(val year: String): DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface?, which: Int) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    try {
                        val db = dbHelper.writableDatabase
                        db.execSQL("""
                            DELETE FROM items
                            WHERE name = "${year}"
                        """)
                        populateRecyclerView()
                    } catch (ex: Exception) {

                    }
                }
            }
        }

        if (view != null) {
            val year = view.findViewById<TextView>(R.id.bikeYear).text.toString()
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("Are you sure you want to delete item named ${year}?")
            alertDialogBuilder.setPositiveButton("Yes", MyDialogInterfaceListener(year))
            alertDialogBuilder.setNegativeButton("No", MyDialogInterfaceListener(year))
            alertDialogBuilder.show()
            return true
        }
        return false
    }


}