package com.tomsk.testshift.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tomsk.testshift.R
import com.tomsk.testshift.navigation.NavigationDestination
import com.tomsk.testshift.network.Coordinates
import com.tomsk.testshift.network.Dob
import com.tomsk.testshift.network.Id
import com.tomsk.testshift.network.Location
import com.tomsk.testshift.network.Login
import com.tomsk.testshift.network.Name
import com.tomsk.testshift.network.Picture
import com.tomsk.testshift.network.Registered
import com.tomsk.testshift.network.Results
import com.tomsk.testshift.network.Street
import com.tomsk.testshift.network.Timezone
import java.io.File


object PersonDetailsDestination : NavigationDestination {
    override val route = "person-details"
    override val titleRes = R.string.person_details
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailsScreen(
    navHostController: NavHostController,
    personDetailViewModel: PersonDetailViewModel
    = viewModel(factory = AppViewModelProvider.Factory)
) {
    //Log.d(TAG, "<<<<<<<<<<<<<<<++++++++++ >>>>>>>>>>>>>>>${personDetailViewModel.itemId}")
    //val itemList by personViewModel.persons.observeAsState(listOf())
    val personForDetails = personDetailViewModel.personForDeatils
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "Person Detail",
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                }
            )
        }) { innerPadding ->
        PersonDetailCard(innerPadding, context, personForDetails)
    }
}

@Composable
private fun PersonDetailCard(
    innerPadding: PaddingValues,
    context: Context,
    personForDetails: Results
) {
    Card(
        modifier = Modifier
            .padding(innerPadding)
    ) {

        Column(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .verticalScroll(rememberScrollState()),

            ) {

            val imgFile = File(personForDetails.picture.thumbnail)
            Log.d(TAG, "imgFile =  $imgFile")
            var imgBitmap: Bitmap? = null
            if (imgFile.exists()) {
                imgBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            }
            Image(
                painter = BitmapPainter(image = imgBitmap!!.asImageBitmap()),
                contentDescription = "Image",
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
            )

//            Box(
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .height(80.dp)
//                    .width(80.dp)
//                    .background(Color.Cyan)
//            ) {}
            Text(
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        top = 5.dp, bottom = 5.dp, end = 10.dp
                    ),
                text = "Gender:  ${personForDetails.gender}"
            )

            Text(
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        top = 5.dp, bottom = 5.dp, end = 10.dp
                    ),
                text = "Name:  ${personForDetails.name.title} " +
                        "${personForDetails.name.first} " +
                        "${personForDetails.name.last}"
            )

            Text(
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        top = 5.dp, bottom = 5.dp, end = 10.dp
                    ),
                text = "Location:\n str. ${personForDetails.location.street.name}\n " +
                        "str. number: ${personForDetails.location.street.number}\n" +
                        " city: ${personForDetails.location.city}\n " +
                        "state: ${personForDetails.location.state}\n " +
                        "country: ${personForDetails.location.country}\n " +
                        "postcode: ${personForDetails.location.postcode}"
            )


            Text(
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        context.map(
                            personForDetails.location.coordinates.latitude,
                            personForDetails.location.coordinates.longitude
                        )
                    }
                    .background(
                        Color.Gray,
                        shape = AbsoluteCutCornerShape(topLeft = 5.dp, bottomRight = 5.dp)
                    )
                    .padding(
                        start = 10.dp,
                        top = 5.dp, bottom = 5.dp, end = 10.dp
                    ),
                text = "Coordinates:\n  " +
                        "latitude:  ${personForDetails.location.coordinates.latitude}\n " +
                        " longitude:  ${personForDetails.location.coordinates.longitude}"
            )

            Text(
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        top = 5.dp, bottom = 5.dp, end = 10.dp
                    ),
                text = "Timezone:\n  " +
                        "offset: ${personForDetails.location.timezone.offset}\n  " +
                        "description: ${personForDetails.location.timezone.description} "

            )


            Text(
                fontSize = 20.sp,
                modifier = Modifier
                    .background(
                        Color.Gray,
                        shape = AbsoluteCutCornerShape(topLeft = 5.dp, bottomRight = 5.dp)
                    )
                    .padding(
                        start = 10.dp,
                        top = 5.dp, bottom = 5.dp, end = 10.dp
                    )
                    .fillMaxWidth()
                    .clickable {
                        context.sendMail(
                            to = personForDetails.email,
                            subject = "subject"
                        )
                    }
                    .padding(
                        start = 10.dp,
                        top = 5.dp, bottom = 5.dp, end = 10.dp
                    ),
                text = "email: ${personForDetails.email}"
            )

            Text(
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        top = 5.dp, bottom = 5.dp, end = 10.dp
                    ),
                text = "Login:\n uuid: ${personForDetails.login.uuid}\n " +
                        "username: ${personForDetails.login.username}\n " +
                        "password: ${personForDetails.login.password}\n " +
                        "salt: ${personForDetails.login.salt}\n " +
                        "md5: ${personForDetails.login.md5}\n " +
                        "sha1: ${personForDetails.login.sha1}\n " +
                        "sha256: ${personForDetails.login.sha256}"

            )


            Text(
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.Gray,
                        shape = AbsoluteCutCornerShape(topLeft = 5.dp, bottomRight = 5.dp)
                    )
                    .padding(
                        start = 10.dp,
                        top = 5.dp, bottom = 5.dp, end = 10.dp
                    )
                    .clickable {
                        context.dial(personForDetails.phone)
                    },
                text = "phone: ${personForDetails.phone}"
            )

            Text(
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        top = 5.dp, bottom = 5.dp, end = 10.dp
                    ),
                text = "Dob:\n  " +
                        "date: ${personForDetails.dob.date}\n  " +
                        "age: ${personForDetails.dob.age} "

            )
            Text(
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        top = 5.dp, bottom = 5.dp, end = 10.dp
                    ),
                text = "Registered:\n  " +
                        "date: ${personForDetails.registered.date}\n  " +
                        "age: ${personForDetails.registered.age} "

            )
            Text(
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        top = 5.dp, bottom = 5.dp, end = 10.dp
                    ),
                text = "Cell: ${personForDetails.cell}"
            )

            Text(
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        top = 5.dp, bottom = 5.dp, end = 10.dp
                    ),
                text = "Id:\n  " +
                        "name: ${personForDetails.id.name}\n  " +
                        "value: ${personForDetails.id.varue} "

            )



            Text(
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        top = 5.dp, bottom = 5.dp, end = 10.dp
                    ),
                text = "Picture:\n large: ${personForDetails.picture.large}\n " +
                        "medium: ${personForDetails.picture.medium}\n " +
                        "thumbnail: ${personForDetails.picture.thumbnail}\n "


            )
            Text(
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        top = 5.dp, bottom = 5.dp, end = 10.dp
                    ),
                text = "Nat: ${personForDetails.nat}"
            )


        }
    }
}

fun Context.sendMail(to: String, subject: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "vnd.android.cursor.item/email" // or "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {

        Toast.makeText(
            this,
            "Похоже нет ниодного приложения, способного отправить email! ",
            Toast.LENGTH_LONG
        ).show()
    } catch (t: Throwable) {
        Toast.makeText(
            this,
            "Похоже что то не так!\n $t ",
            Toast.LENGTH_LONG
        ).show()
    }
}

fun Context.dial(phone: String) {
    try {
        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.fromParts("tel", phone, null)
        )
        startActivity(intent)
    } catch (t: Throwable) {
        Toast.makeText(
            this,
            "Похоже что то не так!\n $t ",
            Toast.LENGTH_LONG
        ).show()
    }
}


fun Context.map(latitude: String, longitude: String) {

    //val gmmIntentUri = Uri.parse("geo:37.7749,-122.4194")

    //Log.d(TAG, "lati =$latitude,   longi =$longitude   ")

    try {
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        ContextCompat.startActivity(this, mapIntent, null)
    } catch (t: Throwable) {
        Toast.makeText(
            this,
            "Похоже что то не так!\n $t ",
            Toast.LENGTH_LONG
        ).show()
    }

}

@Preview
@Composable
fun CardPreview() {
    val context = LocalContext.current
    val name = Name("Miss", "Alina", "Malina")
    val coordinates = Coordinates("-69.8246", "134.8719")
    val timezone = Timezone("+9:30", "Adelaide, Darwin")
    val street = Street("8929", "Valwood Pkwy")
    val location = Location(
        street, "Billings", "Michigan",
        "United States", "63104", coordinates, timezone
    )
    val login = Login(
        "7a0eed16-9430-4d68-901f-c0d4c1c3bf00",
        "yellowpeacock117", "addison", "sld1yGtd",
        "ab54ac4c0be9480ae8fa5e9e2a5196a3", "edcf2ce613cbdea349133c52dc2f3b83168dc51b",
        "48df5229235ada28389b91e60a935e4f9b73eb4bdb855ef9258a1751f10bdc5d"
    )
    val dob = Dob("1992-03-08T15:13:16.688Z", "30")
    val registered = Registered("2007-07-09T05:51:59.390Z", "14")
    val id = Id("SSN", "405-88-3636")
    val picture = Picture(
        "https://randomuser.me/api/portraits/men/75.jpg",
        "https://randomuser.me/api/portraits/med/men/75.jpg",
        "/data/user/0/com.tomsk.testshift/64.jpg"
    )
    PersonDetailCard(
        innerPadding = PaddingValues(5.dp), context = context, personForDetails = Results(
            1, "female", name, location, "example@gmail.com",
            login, dob, registered, "+79138133133", "(489) 330-2385", id, picture, "US"
        )

    )

}
