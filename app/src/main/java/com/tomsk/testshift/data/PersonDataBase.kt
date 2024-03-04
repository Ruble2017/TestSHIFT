package com.tomsk.testshift.data


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tomsk.testshift.network.Results

@Database(entities = [Results::class], version = 1, exportSchema = false)
@TypeConverters(PersonTypeConvertor::class)
abstract class PersonDataBase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

//    companion object {
//        @Volatile
//        private var Instance: PersonDataBase? = null
//
////        private val MIGRATION_2_3 = object : Migration(2, 3) {
////            override fun migrate(db: SupportSQLiteDatabase) {
////            }
////        }
//
//
//
//        fun getDatabase(context: Context): PersonDataBase {
//            // if the Instance is not null, return it, otherwise create a new database instance.
//            return Instance ?: synchronized(this) {
//                Room.databaseBuilder(context, PersonDataBase::class.java, "persons")
//                    //.addMigrations(MIGRATION_2_3)
//                    .build()
//                    .also { Instance = it
//                    }
//            }
//        }
//    }

}