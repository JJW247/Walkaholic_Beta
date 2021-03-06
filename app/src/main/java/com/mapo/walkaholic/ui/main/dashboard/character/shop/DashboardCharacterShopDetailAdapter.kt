package com.mapo.walkaholic.ui.main.dashboard.character.shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mapo.walkaholic.data.model.ItemInfo
import com.mapo.walkaholic.databinding.ItemSlotShopBinding
import com.mapo.walkaholic.ui.main.dashboard.character.CharacterShopSlotClickListener

class DashboardCharacterShopDetailAdapter(
    private var arrayListItemInfo: ArrayList<ItemInfo>,
    private val listener: CharacterShopSlotClickListener
) : RecyclerView.Adapter<DashboardCharacterShopDetailAdapter.DashboardCharacterShopDetailViewHolder>() {

    private val selectedSlotShopMap = mutableMapOf<Int, Triple<Boolean, ItemInfo, Boolean>>()

    init {
        clearSelectedItem()
    }

    inner class DashboardCharacterShopDetailViewHolder(
        val binding: ItemSlotShopBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setItemInfo(itemInfo: ItemInfo) {
            if(itemInfo != null) {
                binding.itemInfo = itemInfo
                binding.itemShopIvBase.isSelected = isItemSelected(position)
                binding.itemShopIvLowerCorner.isSelected = isItemSelected(position)
            } else { }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DashboardCharacterShopDetailViewHolder {
        val binding = ItemSlotShopBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DashboardCharacterShopDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DashboardCharacterShopDetailViewHolder, position: Int) {
        if(arrayListItemInfo[position].itemName.isNullOrEmpty()) { }
        else {
            holder.setItemInfo(arrayListItemInfo[position])
            holder.binding.itemShopLayout.setOnClickListener {
                toggleItemSelected(position)
                listener.onItemClick(selectedSlotShopMap)
            }
        }
    }

    override fun getItemCount() = arrayListItemInfo.size

    private fun toggleItemSelected(position: Int) {
        if(selectedSlotShopMap[position] != null) {
            for(i in 0 until arrayListItemInfo.size) {
                selectedSlotShopMap[i] = Triple(selectedSlotShopMap[i]!!.first, arrayListItemInfo[i], false)
            }
            if (selectedSlotShopMap[position]!!.first) {
                selectedSlotShopMap[position] = Triple(false, selectedSlotShopMap[position]!!.second, false)
            } else {
                selectedSlotShopMap[position] = Triple(true, selectedSlotShopMap[position]!!.second, true)
            }
            notifyDataSetChanged()
        } else { }
    }

    private fun isItemSelected(position: Int): Boolean {
        return if (selectedSlotShopMap[position] != null) {
            selectedSlotShopMap[position]!!.first
        } else {
            false
        }
    }

    fun clearSelectedItem() {
        for(i in 0 until arrayListItemInfo.size) {
            selectedSlotShopMap[i] = Triple(false, arrayListItemInfo[i], false)
        }
    }

    fun getData() = selectedSlotShopMap
}