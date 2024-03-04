package com.tomsk.testshift.screens
import android.content.Context
import androidx.compose.ui.platform.LocalContext
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

