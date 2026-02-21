package com.elsharif.hospitalapp

import android.content.Context


import kotlinx.serialization.json.Json

object CheckData {



    var checkListItems: List<CheckList> = listOf()


    fun initList(context: Context): List<CheckList> {
        if (checkListItems.isEmpty()) {
            val products =
                context.assets.open("checkList.json").bufferedReader().use { it.readText() }
            checkListItems = Json.decodeFromString(products)
        }
        return checkListItems
    }
    fun getCheckListByName(name: String): CheckList? {
        return checkListItems.find { it.checkList == name }
    }

}
