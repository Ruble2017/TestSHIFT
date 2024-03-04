package com.tomsk.testshift.screens


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tomsk.testshift.R
import com.tomsk.testshift.navigation.NavigationDestination
import com.tomsk.testshift.network.Coordinates
import com.tomsk.testshift.network.Dob
import com.tomsk.testshift.network.Id
import com.tomsk.testshift.network.LoadingAnimation
import com.tomsk.testshift.network.Location
import com.tomsk.testshift.network.Login
import com.tomsk.testshift.network.Name
import com.tomsk.testshift.network.Picture
import com.tomsk.testshift.network.Registered
import com.tomsk.testshift.network.Results
import com.tomsk.testshift.network.Street
import com.tomsk.testshift.network.Timezone
import java.io.File

object MainDestination : NavigationDestination {
    override val route = "main"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MainScreen(
    navigateToDetailScreen: (Int) -> Unit,
    personViewModel: PersonViewModel = viewModel(factory = AppViewModelProvider.Factory),
    personeUiState: PersoneUiState = personViewModel.personUiState
) {
    val itemList by personViewModel.persons.observeAsState(listOf())
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(MainDestination.titleRes))
                }
            )
        },
        floatingActionButton = {
            Column {
                ExtendedFloatingActionButton(onClick = {
                    personViewModel.deleteJpg()
                    personViewModel.clearDataBase()
                    //personViewModel.loadingPersonUiState()
                    personViewModel.loadingPersonUiState()
                    Log.d (TAG, "$personeUiState")
                    personViewModel.getPersonsInfoFromNetwork()

                }) {
                    Text(text = "Обновить сисок из сети")
                }
//                Spacer(modifier = Modifier.height(20.dp))
//                ExtendedFloatingActionButton(onClick = { personViewModel.deleteJpg() }) {
//                    Text(text = "Delete files")
//                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column {
            when (personeUiState) {
                is PersoneUiState.Loading -> LoadingScreen()
                is PersoneUiState.Success -> PersonsListScreen(
                    padding,
                    itemList, navigateToDetailScreen
                )
                is PersoneUiState.Error -> ErrorScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    LoadingAnimation()
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

@Composable
private fun PersonsListScreen(
    padding: PaddingValues,
    itemList: List<Results>,
    navigateToDetailScreen: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(items = itemList, key = { it.idd }) { item ->
            Spacer(modifier = Modifier.height(10.dp))
            PersonItem(
                modifier = Modifier.clickable {
                    navigateToDetailScreen(item.idd)
                    Log.d(TAG, " PersonItem  click  ${item.idd}")

                },
                item = item
            )
        }
    }
}

@Composable
fun PersonItem(
    item: Results, modifier: Modifier
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        //.height(50.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(modifier = modifier) {
            Row(modifier = modifier,
                verticalAlignment = Alignment.CenterVertically) {
                val imgFile = File(item.picture.thumbnail)
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

//                Box(
//                    modifier = Modifier
//                        .height(60.dp)
//                        .width(60.dp)
//                        .background(Color.Cyan)
//                ){}

                Column(modifier = Modifier) {
                    Text(
                        modifier = Modifier.padding(1.dp),
                        text ="ФИО : ${item.name.first}  ${item.name.last}"
                    )
                    Text(
                        modifier = Modifier.padding(1.dp),
                        text ="Адрес : ${item.location.country}," +
                                " г. ${item.location.city}, ул. ${item.location.street.name}, " +
                                " д. ${item.location.street.number}"
                    )
                    Text(
                        modifier = Modifier.padding(1.dp),
                        text ="Телефон : ${item.phone}"
                    )

                }


            }

        }
    }
}

@Preview
@Composable
fun PersonItemPreview() {

//    val context = LocalContext.current
//    context
    val name = Name("", "Alina", "Malina")
    val coordinates = Coordinates("", "")
    val timezone = Timezone("", "")
    val street = Street("10", "Салтыкова - Щедрина")
    val location = Location(street, "Новосибирск", "", "Россия","",coordinates, timezone)
    val login = Login("", "", "", "", "", "", "")
    val dob = Dob("01-01-2001", "23")
    val registered = Registered("", "")
    val id = Id("", "")
    val picture = Picture("", "", "/data/user/0/com.tomsk.testshift/64.jpg")
    PersonItem(
        item = Results(
            1, "", name, location, "example@gmail.com",
            login, dob, registered, "+79138133133", "", id, picture, ""
        ), modifier = Modifier
    )
}



