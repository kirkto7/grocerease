package edu.msoe.grocerease.database
import androidx.room.TypeConverter
import java.util.UUID

class TypeConverter {
    @TypeConverter
    fun fromListToString(value: List<UUID>?): String? {
        return value?.joinToString(",") { it.toString() }
    }

    @TypeConverter
    fun fromStringToList(value: String?): List<UUID>? {
        return value?.split(",")?.map { UUID.fromString(it) }
    }
}