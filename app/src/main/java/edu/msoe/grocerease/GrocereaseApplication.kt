package edu.msoe.grocerease

import android.app.Application
import edu.msoe.grocerease.database.RecipeRepo

class GrocereaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RecipeRepo.initialize(this)
    }
}