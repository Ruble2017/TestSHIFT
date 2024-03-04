package com.tomsk.testshift

import android.app.Application
import com.tomsk.testshift.data.MainRepo

class TestShiftApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MainRepo.initialize(this)
    }


}