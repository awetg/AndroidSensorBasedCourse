package com.example.arvideo

import android.net.Uri

class DecorItem (val name: String, val modelResource: Uri?, val imgResource: Int)

object DecoreItemList {
    val allItems: List<DecorItem> = listOf(
        DecorItem("Flower", Uri.parse("FlowerPot.sfb"), R.drawable.flower_pot),
        DecorItem("Palm Tree", Uri.parse("palm.sfb"), R.drawable.palm),
        DecorItem("Birch Tree", Uri.parse("birch.sfb"), R.drawable.birch),
        DecorItem("Tree", Uri.parse("quakingAspen.sfb"), R.drawable.quaking_aspen),
        DecorItem("Palm Tree", Uri.parse("funPalm.sfb"), R.drawable.fan_palm),
        DecorItem("Pine Tree", Uri.parse("pine.sfb"), R.drawable.pine),
        DecorItem("Christmas Tree", Uri.parse("christmas_tree.sfb"), R.drawable.christimas_tree),
        DecorItem("Tree", Uri.parse("tree.sfb"), R.drawable.tree),
        DecorItem("Bed", Uri.parse("bed1.sfb"), R.drawable.bed1),
        DecorItem("Barbecue", Uri.parse("barbecue.sfb"), R.drawable.barbecue),
        DecorItem("Bench", Uri.parse("bench.sfb"), R.drawable.bench),
        DecorItem("TV", Uri.parse("samsung_tv.sfb"), R.drawable.samsung_tv),
        DecorItem("Table", Uri.parse("Table1.sfb"), R.drawable.table1),
        DecorItem("Stool", Uri.parse("kitchen_stool.sfb"), R.drawable.kitchen_stool),
        DecorItem("Sofa", Uri.parse("Sofa2.sfb"), R.drawable.sofa2),
        DecorItem("Seat", Uri.parse("Seat.sfb"), R.drawable.seat),
        DecorItem("Fridge", Uri.parse("Fridge.sfb"), R.drawable.bed1),
        DecorItem("Chair", Uri.parse("Chair.sfb"), R.drawable.fridge),
        DecorItem("Night stand", Uri.parse("NightStand1.sfb"), R.drawable.night_stand1),
        DecorItem("Sofa", Uri.parse("Sofa.sfb"), R.drawable.sofa),
        DecorItem("Chair", Uri.parse("OfficChair1.sfb"), R.drawable.offic_chair1),
        DecorItem("Cabinet", Uri.parse("Cabinet.sfb"), R.drawable.cabinet),
        DecorItem("Lamp", Uri.parse("OldLamp1.sfb"), R.drawable.old_lamp1),
        DecorItem("Sofa", Uri.parse("Sofa1.sfb"), R.drawable.sofa1),
        DecorItem("Table", Uri.parse("Wood_Table.sfb"), R.drawable.wood_table),
        DecorItem("Stool", Uri.parse("StoolChair.sfb"), R.drawable.stool_chair),
        DecorItem("Drawer", Uri.parse("Drawer1.sfb"), R.drawable.drawer1),
        DecorItem("ClothHanger", Uri.parse("ClothHanger.sfb"), R.drawable.cloth_hanger),
        DecorItem("Table", Uri.parse("Table3.sfb"), R.drawable.table3),
        DecorItem("Table", Uri.parse("Table4.sfb"), R.drawable.table4),
        DecorItem("Table", Uri.parse("CoffeeTable.sfb"), R.drawable.coffee_table),
        DecorItem("Shelve", Uri.parse("kids_shelves.sfb"), R.drawable.kids_shelves),
        DecorItem("TrashCan", Uri.parse("TrashCan.sfb"), R.drawable.trash_can),
        DecorItem("Traffic Light", Uri.parse("traffic_light_ped.sfb"), R.drawable.traffic_light_ped),
        DecorItem("Glass", Uri.parse("Glass.sfb"), 0)
    )
}
