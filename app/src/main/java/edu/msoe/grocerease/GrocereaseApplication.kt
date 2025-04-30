package edu.msoe.grocerease

import android.app.Application
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import edu.msoe.grocerease.database.RecipeRepo
import edu.msoe.grocerease.R
import kotlinx.coroutines.launch

class GrocereaseApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        RecipeRepo.initialize(this)
    }
}