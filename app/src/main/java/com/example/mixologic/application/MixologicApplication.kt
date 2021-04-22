package com.example.mixologic.application

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MixologicApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

}