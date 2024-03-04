package com.tomsk.testshift.screens
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object AppViewModelProvider {

    val Factory = viewModelFactory {

        initializer {
            PersonViewModel()
        }

        initializer {
            PersonDetailViewModel(this.createSavedStateHandle())
        }
    }
}

