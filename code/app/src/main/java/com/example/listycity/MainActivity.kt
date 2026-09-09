package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.listycity.ui.theme.ListyCityTheme
// Bunch of imports because it wasn't working: TAs told students to import them!
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository() // Step 6
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen( // Step 7
                        cities = cityRepository.cities, // Step 7
                        onAddCity = { cityRepository.addCity(it) }, // Step 10 b
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Step 5
class CityRepository {
    // Keep mutable app data private so other classes cannot change it directly
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow",
        "Sydney", "Berlin", "Vienna",
        "Tokyo", "Beijing", "Osaka",
        "New Delhi"
    )

    // Get a read-only list for the UI to display
    val cities: List<String>
        get() = _cities

    // Function to add a city
    fun addCity(city: String) {
        _cities.add(city)
    }
}

// Step 8 a
//@Composable means this function describes part of the app's UI
@Composable
fun CityListScreen(
    // cities: List<String> is the list of city names that this screen receives from MainActivity
    cities: List<String>,
    // onAddCity means that CityListScreen receives a function as a parameter that takes a string (city).
    //The function returns Unit, which means it performs an action but does not return a value.
    onAddCity: (String) -> Unit, // Step 10 a
    // modifier: Modifier = Modifier allows layout info, such as padding, to be passed into this screen
    modifier: Modifier = Modifier
) {

    var newCityName by remember { mutableStateOf(value = "") } // Step 10 c

    // Step 10 d
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(all = 16.dp)) {
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text("City name") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newCityName.isNotBlank()) {
                        onAddCity(newCityName)
                        newCityName = ""
                    }
                }
            ) {
                Text("Add City")
            }
        }

        // Step 8 b
        // LazyColumn is the Compose for a basic scrolling ListView
        LazyColumn(modifier = modifier.fillMaxSize()) {
            // items(cities) loops through the city list and
            // creates one UI row for each city
            items(cities) { city ->
                CityRow(
                    city = city
                )
            }
        }
    }
}

// Step 9
@Composable
fun CityRow(city: String) {
    Text(
        text = city,
        fontSize = 28.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp)
    )
}