package com.example.haydaykeyboard.data

import android.content.Context
import org.json.JSONArray

class HayDayRepository private constructor(
    val machines: List<Machine>,
    val products: List<Product>,
) {
    companion object {
        @Volatile private var instance: HayDayRepository? = null

        fun get(context: Context): HayDayRepository {
            return instance ?: synchronized(this) {
                instance ?: loadFromAssets(context.applicationContext).also { instance = it }
            }
        }

        private fun loadFromAssets(context: Context): HayDayRepository {
            val machinesJson = context.assets.open("hayday_machines.json").bufferedReader().use { it.readText() }
            val productsJson = context.assets.open("hayday_products.json").bufferedReader().use { it.readText() }
            return HayDayRepository(
                machines = parseMachines(machinesJson),
                products = parseProducts(productsJson),
            )
        }

        private fun parseMachines(json: String): List<Machine> {
            val array = JSONArray(json)
            return buildList {
                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)
                    add(
                        Machine(
                            id = item.getString("id"),
                            nameEn = item.getString("nameEn"),
                            nameAr = item.getString("nameAr"),
                            unlockLevel = item.getInt("unlockLevel"),
                        )
                    )
                }
            }
        }

        private fun parseProducts(json: String): List<Product> {
            val array = JSONArray(json)
            return buildList {
                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)
                    add(
                        Product(
                            id = item.getString("id"),
                            nameEn = item.getString("nameEn"),
                            nameAr = item.getString("nameAr"),
                            level = item.getInt("level"),
                            machineId = item.getString("machineId"),
                            machineNameEn = item.getString("machineNameEn"),
                            machineNameAr = item.getString("machineNameAr"),
                            imageUrl = item.optString("imageUrl").takeIf { it.isNotBlank() },
                        )
                    )
                }
            }
        }
    }
}
