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
// Lab Participation: Additional Libraries
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color

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
                        onDeleteCity = { cityRepository.deleteCity(it) }, // Lab Participation: Following same process as above
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

    // Lab Participation : Function to delete a city
    fun deleteCity(city: String) {
        _cities.remove(city)
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
    // Lab Participation: Following same format as onAddCity for onDeleteCity
    onDeleteCity: (String) -> Unit,
    // modifier: Modifier = Modifier allows layout info, such as padding, to be passed into this screen
    modifier: Modifier = Modifier
) {

    var newCityName by remember { mutableStateOf(value = "") } // Step 10 c

    // Lab Participation: I needed another variable to hide and show text (in requirements)
    var isTextVisible by remember { mutableStateOf(false) }

    // Lab Participation: I needed to make parts of the city list "selectable"
        // The following line was made with the help of CMPUT 301 GitHub for Lab 3, Compose Hints
        // By: Raj Prasad and Michelle Deng
        // I visited the site on 2026-09-07, it was published 2026-06-30
        // https://ualberta-cmput301.github.io/labs/lab3_inst.html
    var selectCityName by remember { mutableStateOf<String?>(null)}


    // Step 10 d
    Column(modifier = modifier.fillMaxSize()) {
        // Lab Participation: Not required, but for my viewing
        Row(modifier = Modifier.padding(all = 16.dp)){
            Text(
                text = "ListyCity",
                fontSize = 60.sp
            )
        }

        Row(modifier = Modifier.padding(all = 4.dp)) { // Lab Participation: Updated the padding to 4.dp, for visuals
            Spacer(modifier = Modifier.width(60.dp)) // Lab Participation: Updated the spacer to 60.dp, for visuals

            // Lab Participation: Had to move the entering of the city to show only when the Add City button is clicked (see later Row)

            Button(
                onClick = {
                    // Lab Participation: Had to update the functionalities of the Add City button (now all moved to the Confirm button)
                    if (!isTextVisible) {
                        isTextVisible = true
                        selectCityName = null
                    } else {
                        isTextVisible = false
                    }
                }
            ) {
                Text("Add City")
            }

            Spacer(modifier = Modifier.width(60.dp)) // Lab Participation: Added a spacer, for visuals

            // Lab Participation: Adding a Delete City Button (following similar format to above)
            Button(
                onClick = {
                    // Used this to help me find out how to check if a string is null (I was having 'safety' errors?)
                    // https://www.baeldung.com/kotlin/check-empty-string
                    isTextVisible = false
                    if (!(selectCityName.isNullOrBlank())) {
                        // I was having an error: Smart cast to 'String' is impossible, because 'selectCityName' is a delegated property.
                        // When I searched it up, I got the below link. This seemed like the simplest fix.
                        // Even though I thought it gets checked right before, Android Studio didn't like it. But now does?
                        // https://www.baeldung.com/kotlin/smart-cast-to-type-is-impossible
                        // What I learned:
                        // !! = Not null assertion operator, to make sure it's not null
                        onDeleteCity(selectCityName!!)
                        selectCityName = null // Because "" is still a string
                    }
                }
            ) {
                Text("Delete City")
            }
        }

        // Lab Participation: Actually showing the text when Add City is pushed
        if (isTextVisible) {
            Row(modifier = Modifier.padding(all = 5.dp)) {
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("City name") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        if (newCityName.isNotBlank()) {
                            onAddCity(newCityName)
                            newCityName = ""
                        }
                    }
                ) {
                    Text("Confirm")
                }
            }
        }

        // Step 8 b
        // LazyColumn is the Compose for a basic scrolling ListView
        LazyColumn(modifier = modifier.fillMaxSize()) {
            // items(cities) loops through the city list and
            // creates one UI row for each city
            items(cities) { city ->
                CityRow(
                    city = city,
                    // Lab Participation: Have to actually make the cities in the list clickable
                        // The following code was created using the help of StackOverFlow
                        // Author: Asked By user18082994, Answered by nglauber
                        // Title: How to select only one item in a list (LazyColumn)?
                        // Answer: https://stackoverflow.com/questions/72531840/how-to-select-only-one-item-in-a-list-lazycolumn
                        // Date of Post: 2022-07-07
                        // License: CC BY-SA 4.0
                        // Date I Accessed: 2026-09-08
                    selected = (city == selectCityName),
                    onClick = {
                        if (selectCityName == city) {
                            selectCityName = null
                        }
                        else {
                            selectCityName = city
                        }
                    }
                )
            }
        }
    }
}

// Step 9
@Composable
fun CityRow(city: String,
            selected: Boolean = false, // Lab Participation: I want to be able to see which city is selected
            onClick: () -> Unit = {} // Lab Participation: See Above
) {
    Text(
        text = city,
        fontSize = 28.sp,
        modifier = Modifier
            .fillMaxWidth()
            // Lab Participation: To be able to see if a city is selected, I need to change the background.
                // I just typed the words 'background' and 'color', and Android Studio's suggested library
                // came up so I hope this is actually the way to do it. I mean it works?
            .background(
                if (selected){
                    Color.Gray
                }
                else {
                    Color.White
                }
            )
            .padding(horizontal = 18.dp, vertical = 14.dp)
            // Lab Participation: Getting the items in a list to be clickable
                // The following code created by following through https://developer.android.com/develop/ui/compose/modifiers
                // I visited the site on 2026-09-08
            .clickable { onClick() }
    )
}