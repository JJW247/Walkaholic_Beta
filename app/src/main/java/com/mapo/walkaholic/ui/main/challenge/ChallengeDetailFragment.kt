package com.mapo.walkaholic.ui.main.challenge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mapo.walkaholic.data.model.MissionCondition
import com.mapo.walkaholic.data.network.ApisApi
import com.mapo.walkaholic.data.network.InnerApi
import com.mapo.walkaholic.data.network.Resource
import com.mapo.walkaholic.data.network.SgisApi
import com.mapo.walkaholic.data.repository.MainRepository
import com.mapo.walkaholic.databinding.FragmentDetailChallengeBinding
import com.mapo.walkaholic.ui.base.BaseFragment
import com.mapo.walkaholic.ui.handleApiError
import com.mapo.walkaholic.ui.main.challenge.mission.ChallengeDetailMissionAdapter
import com.mapo.walkaholic.ui.main.challenge.ranking.ChallengeRankingViewPagerAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ChallengeDetailFragment(
    private val position: Int
) : BaseFragment<ChallengeDetailViewModel, FragmentDetailChallengeBinding, MainRepository>() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dummyMission1 =
            MissionCondition("00", "01", "3,000 걸음", "3000", "100")
        val dummyMission2 =
            MissionCondition("00", "02", "5,000 걸음", "5000", "200")
        val dummyMission3 =
            MissionCondition("01", "01", "일일미션 5회", "5", "500")
        val dummyMission4 =
            MissionCondition("01", "02", "일일미션 10회", "10", "1000")
        val dummyArrayList: ArrayList<MissionCondition> = ArrayList()
        dummyArrayList.add(dummyMission1)
        dummyArrayList.add(dummyMission2)
        dummyArrayList.add(dummyMission3)
        dummyArrayList.add(dummyMission4)

        viewModel.userResponse.observe(viewLifecycleOwner, Observer { _userResponse ->
            binding.challengeRVMission.also { _challengeRVMission ->
                _challengeRVMission.layoutManager = LinearLayoutManager(requireContext())
                _challengeRVMission.setHasFixedSize(true)
                when (_userResponse) {
                    is Resource.Success -> {
                        when (_userResponse.value.code) {
                            "200" -> {
                                viewModel.getMissionCondition(
                                    when (position) {
                                        0 -> "00"
                                        1 -> "01"
                                        else -> ""
                                    }
                                )
                                viewModel.missionConditionResponse.observe(
                                    viewLifecycleOwner,
                                    Observer { _missionConditionResponse ->
                                        _challengeRVMission.layoutManager =
                                            LinearLayoutManager(requireContext())
                                        // 동일한 크기의 아이템 항목을 사용자에게 리스트로 보여주기 위해 크기가 변경되지 않음을 명시
                                        _challengeRVMission.setHasFixedSize(true)
                                        when (_missionConditionResponse) {
                                            is Resource.Success -> {
                                                // @TODO GET DATA WHEN REST EXECUTE SUCCESSFUL
                                                Log.e(
                                                    "missionCondition",
                                                    _missionConditionResponse.value.missionCondition.toString()
                                                )
                                            }
                                            is Resource.Loading -> {

                                            }
                                            is Resource.Failure -> {
                                                _missionConditionResponse.errorBody?.let { it1 ->
                                                    Log.e(
                                                        "missionCondition",
                                                        it1.string()
                                                    )
                                                }
                                                handleApiError(_missionConditionResponse)
                                            }
                                        }
                                        when (position) {
                                            0 -> {
                                                binding.challengeTvIntro1.text =
                                                    "일일미션을 완료하고\n포인트를 받으세요!"
                                                binding.challengeTvIntro2.text = "미션은 매일 자정에 갱신되어요"
                                                binding.challengeTvIntro1.visibility = View.VISIBLE
                                                binding.challengeTvIntro2.visibility = View.VISIBLE
                                                binding.challengeLayoutRanking.visibility =
                                                    View.GONE
                                                binding.challengeLayoutRankingIntro.visibility =
                                                    View.GONE
                                                binding.challengeLayoutMission.visibility =
                                                    View.VISIBLE
                                                /*it.adapter = it3.value.missionCondition?.let { it3 ->
                                                    ChallengeDetailMissionAdapter(dummyArrayList)
                                                }*/
                                                _challengeRVMission.adapter =
                                                    ChallengeDetailMissionAdapter(dummyArrayList)
                                            }
                                            1 -> {
                                                binding.challengeTvIntro1.text =
                                                    "주간미션을 완료하고\n포인트를 받으세요!"
                                                binding.challengeTvIntro2.text =
                                                    "미션은 매주 월요일 자정에 갱신되어요"
                                                binding.challengeTvIntro1.visibility = View.VISIBLE
                                                binding.challengeTvIntro2.visibility = View.VISIBLE
                                                binding.challengeLayoutRanking.visibility =
                                                    View.GONE
                                                binding.challengeLayoutRankingIntro.visibility =
                                                    View.GONE
                                                binding.challengeLayoutMission.visibility =
                                                    View.VISIBLE
                                                /*it.adapter = it3.value.missionCondition?.let { it3 ->
                                                    ChallengeDetailMissionAdapter(dummyArrayList)
                                                }*/
                                                _challengeRVMission.adapter =
                                                    ChallengeDetailMissionAdapter(dummyArrayList)
                                            }
                                            2 -> {
                                                binding.challengeTvIntro1.visibility = View.GONE
                                                binding.challengeTvIntro2.visibility = View.GONE
                                                binding.challengeLayoutMission.visibility =
                                                    View.GONE
                                                binding.challengeLayoutRankingIntro.visibility =
                                                    View.VISIBLE
                                                tabLayout = binding.challengeRankingTL
                                                viewPager = binding.challengeRankingVP
                                                val adapter =
                                                    ChallengeRankingViewPagerAdapter(
                                                        childFragmentManager,
                                                        lifecycle,
                                                        2
                                                    )
                                                viewPager.adapter = adapter
                                                val tabName: ArrayList<String> = arrayListOf()
                                                tabName.add("월별포인트")
                                                tabName.add("누적포인트")
                                                TabLayoutMediator(
                                                    tabLayout,
                                                    viewPager
                                                ) { tab, position ->
                                                    tab.text = tabName?.get(position)
                                                }.attach()
                                                tabLayout.addOnTabSelectedListener(object :
                                                    TabLayout.OnTabSelectedListener {
                                                    override fun onTabSelected(tab: TabLayout.Tab?) {
                                                        viewPager.currentItem = tab!!.position
                                                        when (tab!!.position) {
                                                            0 -> {
                                                                binding.challengeRankingTvIntro1.text =
                                                                    "${_userResponse.value.data.first().nickName}님, 월별랭킹"
                                                                // binding. ~~ 순위
                                                                binding.challengeRankingTvIntro3.text =
                                                                    "월별랭킹은 매월 1일 자정에 갱신되어요"
                                                            }
                                                            1 -> {
                                                                binding.challengeRankingTvIntro1.text =
                                                                    "${_userResponse.value.data.first().nickName}님, 누적랭킹"
                                                                // binding. ~~ 순위
                                                                binding.challengeRankingTvIntro3.text =
                                                                    "서비스 시작일(2021년 05월 17일)부터 현재까지"
                                                            }
                                                            else -> {
                                                            }
                                                        }
                                                    }

                                                    override fun onTabUnselected(tab: TabLayout.Tab?) {

                                                    }

                                                    override fun onTabReselected(tab: TabLayout.Tab?) {

                                                    }
                                                })
                                                binding.challengeLayoutRanking.visibility =
                                                    View.VISIBLE
                                            }
                                        }
                                    })
                            }
                            "400" -> {

                            }
                            else -> {

                            }
                        }
                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Failure -> {
                        handleApiError(_userResponse)
                    }
                }
            }
        })
        viewModel.getUser()
    }

    override fun getViewModel() = ChallengeDetailViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDetailChallengeBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): MainRepository {
        val jwtToken = runBlocking { userPreferences.jwtToken.first() }
        val api = remoteDataSource.buildRetrofitInnerApi(InnerApi::class.java, jwtToken)
        val apiWeather = remoteDataSource.buildRetrofitApiWeatherAPI(ApisApi::class.java)
        val apiSGIS = remoteDataSource.buildRetrofitApiSGISAPI(SgisApi::class.java)
        return MainRepository(api, apiWeather, apiSGIS, userPreferences)
    }
}