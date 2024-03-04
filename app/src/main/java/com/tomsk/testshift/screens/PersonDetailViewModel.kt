package com.tomsk.testshift.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tomsk.testshift.data.MainRepo
import com.tomsk.testshift.network.Results

class PersonDetailViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val repo = MainRepo.get()
    val itemId: Int = checkNotNull(savedStateHandle[PersonDetailsDestination.itemIdArg])
    var personForDeatils: Results = repo.getPersonForDetails(itemId)!!

}
