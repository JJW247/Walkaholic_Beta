package com.mapo.walkaholic.ui.base

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.mapo.walkaholic.data.UserPreferences
import com.mapo.walkaholic.data.network.RemoteDataSource
import com.mapo.walkaholic.data.repository.BaseRepository
import com.mapo.walkaholic.ui.auth.AuthActivity
import com.mapo.walkaholic.ui.startNewActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseSharedFragment<VM : BaseViewModel, B : ViewBinding, R : BaseRepository> : Fragment() {
    protected lateinit var userPreferences: UserPreferences
    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    protected lateinit var callback: OnBackPressedCallback
    protected val remoteDataSource = RemoteDataSource()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        userPreferences = UserPreferences(requireContext())
        binding = getFragmentBinding(inflater, container)

        /*
        val sharedViewModel : BaseViewModel by viewModels {
            ViewModelFactory(getFragmentRepository())
        }
        viewModel = sharedViewModel as VM
         */

        lifecycleScope.launch { userPreferences.jwtToken.first() }

        /*
        viewModel.showToastEvent.observe(
            viewLifecycleOwner,
            EventObserver(this@BaseSharedFragment::showToastEvent)
        )

        viewModel.showSnackbarEvent.observe(
            viewLifecycleOwner,
            EventObserver(this@BaseSharedFragment::showSnackbarEvent)
        )
         */

        return binding.root
    }

    /*
    private fun showToastEvent(contents: String) {
        when(contents) {
            null -> { }
            "" -> { }
            else -> {
                Toast.makeText(
                    requireContext(),
                    contents,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showSnackbarEvent(contents: String) {
        when(contents) {
            null -> { }
            "" -> { }
            else -> {
                requireView().snackbar(contents)
            }
        }
    }
     */

    fun logout() = lifecycleScope.launch {
        viewModel.logout()
        userPreferences.removeJwtToken()
        requireActivity().startNewActivity(AuthActivity::class.java as Class<Activity>)
    }

    abstract fun getViewModel(): Class<VM>
    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B
    abstract fun getFragmentRepository(): R
}