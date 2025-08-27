package com.websarva.wings.dostudy_android.model.Room

import androidx.room.TypeConverter

class Converters {
     @TypeConverter
     fun fromListIntToString(list: List<Int>): String {
         return list.joinToString(",")
     }

     @TypeConverter
     fun fromStringToListInt(value: String): List<Int> {
         return if (value.isEmpty()) {
             emptyList() //空文字列の場合は空のリストを返す
         } else {
             value.split(",").map { it.toInt() }
         }
     }
}