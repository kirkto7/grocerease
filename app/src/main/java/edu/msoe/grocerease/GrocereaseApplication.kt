package edu.msoe.grocerease

import android.app.Application
import edu.msoe.grocerease.database.RecipeRepo
import edu.msoe.grocerease.R

class GrocereaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RecipeRepo.initialize(this)
    }
}