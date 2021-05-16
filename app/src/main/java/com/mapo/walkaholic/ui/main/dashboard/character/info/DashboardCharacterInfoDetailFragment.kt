package com.mapo.walkaholic.ui.main.dashboard.character.info

import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mapo.walkaholic.R
import com.mapo.walkaholic.data.model.ItemInfo
import com.mapo.walkaholic.data.network.ApisApi
import com.mapo.walkaholic.data.network.InnerApi
import com.mapo.walkaholic.data.network.SgisApi
import com.mapo.walkaholic.data.repository.MainRepository
import com.mapo.walkaholic.databinding.FragmentDetailCharacterInfoBinding
import com.mapo.walkaholic.ui.base.BaseFragment
import com.mapo.walkaholic.ui.base.EventObserver
import com.mapo.walkaholic.ui.main.dashboard.character.CharacterItemSlotClickListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DashboardCharacterInfoDetailFragment(
    private val position: Int
) : BaseFragment<DashboardCharacterInfoDetailViewModel, FragmentDetailCharacterInfoBinding, MainRepository>(),
    CharacterItemSlotClickListener {

    val arrayListInventoryItem = arrayListOf<ItemInfo>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrayListInventoryItem.add(ItemInfo("face", "0", "진한눈썹", "3000"))
        /*
        arrayListInventoryItem.add(ItemInfo("face", "1", "속눈썹펌", "3000"))
        arrayListInventoryItem.add(ItemInfo("face", "2", "수줍은볼", "3000"))
        arrayListInventoryItem.add(ItemInfo("face", "3", "발그레볼", "3000"))
        arrayListInventoryItem.add(ItemInfo("hair", "0", "똑딱이핀", "3000"))
        arrayListInventoryItem.add(ItemInfo("hair", "1", "나뭇잎컷", "3000"))
        arrayListInventoryItem.add(ItemInfo("hair", "2", "최준머리", "3000"))
         */
        viewModel.onClickEvent.observe(
            viewLifecycleOwner,
            EventObserver(this@DashboardCharacterInfoDetailFragment::onClickEvent)
        )
        binding.dashCharacterInfoDetailRV.also {
            val linearLayoutManager = LinearLayoutManager(requireContext())
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            it.layoutManager = linearLayoutManager
            it.setHasFixedSize(true)
            when (position) {
                0 -> {
                    val filterResult =
                        arrayListInventoryItem.filter { itemInfo -> itemInfo.itemType == "face" } as ArrayList<ItemInfo>
                    when (filterResult.size) {
                        0 -> {
                            filterResult.add(ItemInfo(null, null, null, null))
                            filterResult.add(ItemInfo(null, null, null, null))
                            filterResult.add(ItemInfo(null, null, null, null))
                        }
                        1 -> {
                            filterResult.add(ItemInfo(null, null, null, null))
                            filterResult.add(ItemInfo(null, null, null, null))
                        }
                        2 -> {
                            filterResult.add(ItemInfo(null, null, null, null))
                        }
                        else -> {
                        }
                    }
                    it.adapter = DashboardCharacterInfoDetailAdapter(filterResult, this)
                }
                1 -> {
                    val filterResult =
                        arrayListInventoryItem.filter { itemInfo -> itemInfo.itemType == "hair" } as ArrayList<ItemInfo>
                    when (filterResult.size) {
                        0 -> {
                            filterResult.add(ItemInfo(null, null, null, null))
                            filterResult.add(ItemInfo(null, null, null, null))
                            filterResult.add(ItemInfo(null, null, null, null))
                        }
                        1 -> {
                            filterResult.add(ItemInfo(null, null, null, null))
                            filterResult.add(ItemInfo(null, null, null, null))
                        }
                        2 -> {
                            filterResult.add(ItemInfo(null, null, null, null))
                        }
                        else -> {
                        }
                    }
                    it.adapter = DashboardCharacterInfoDetailAdapter(filterResult, this)
                }
                else -> {
                }
            }
        }
    }

    private fun onClickEvent(name: String) {
        when (name) {
            "walk_record" -> {
            }
            else -> {
                null
            }
        }
    }

    override fun getViewModel() = DashboardCharacterInfoDetailViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDetailCharacterInfoBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): MainRepository {
        val jwtToken = runBlocking { userPreferences.jwtToken.first() }
        val api = remoteDataSource.buildRetrofitInnerApi(InnerApi::class.java, jwtToken)
        val apiWeather = remoteDataSource.buildRetrofitApiWeatherAPI(ApisApi::class.java)
        val apiSGIS = remoteDataSource.buildRetrofitApiSGISAPI(SgisApi::class.java)
        return MainRepository.getInstance(api, apiWeather, apiSGIS, userPreferences)
    }

    override fun onRecyclerViewItemClick(
        view: View,
        position: Int,
        arrayListItemInfo: ArrayList<ItemInfo>,
        selectedItems: SparseBooleanArray,
        selectedTotalPrice: Int
    ) {
        when (view.id) {
            R.id.itemInventoryLayout -> {

            }
            R.id.itemInventoryIvDiscard -> {

            }
        }
    }
}