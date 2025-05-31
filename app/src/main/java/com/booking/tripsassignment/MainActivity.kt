package com.booking.tripsassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.booking.tripsassignment.ui.ChainsFragment


// Single Activity Pattern where all features lives inside fragment, This activity is lightweight and has no business logic
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        // Only add the fragment once, on initial creation since savedInstanceState wont be null during screen orientation
        // changes even though activity gets destroyed
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ChainsFragment())
                .commit()
        }
    }
}
