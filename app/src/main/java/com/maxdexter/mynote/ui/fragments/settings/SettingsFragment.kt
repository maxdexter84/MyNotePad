package com.maxdexter.mynote.ui.fragments.settings


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import com.maxdexter.mynote.R
import com.maxdexter.mynote.SharedPref
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.databinding.SettingsFragmentBinding
import com.maxdexter.mynote.extensions.checkedRB
import com.maxdexter.mynote.repository.Repository
import com.maxdexter.mynote.utils.SettingsEvent


private const val APPLICATION_NAME = "My Note"
private const val RC_SIGN_IN = 458
private const val DARK_THEME = "DARK_THEME"
class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: SettingsFragmentBinding
    private lateinit var viewModelFactory: SettingsViewModelFactory
    private var isDarkTheme: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
            isDarkTheme = SharedPref(requireActivity()).isDarkTheme
            if (isDarkTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false)
        initViewModel()
        binding.settingsViewModel = viewModel
        binding.lifecycleOwner = this

        binding.auth.setOnClickListener { startLoginActivity() }
        initRadioGroup()
        changeTheme()
        initCloudAuth()
        eventObserve()
        viewModel.logOut.observe(viewLifecycleOwner, { if (it) logout() })
        return binding.root
    }

    private fun initViewModel() {
        val noteDao = context?.let { NotePad.get(it)?.database?.mNoteDao() }
        viewModelFactory = SettingsViewModelFactory(noteDao?.let { Repository }, viewLifecycleOwner, context)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)
    }

    private fun changeTheme() {
        binding.switchAppTheme.isChecked = isDarkTheme
        binding.switchAppTheme.setOnCheckedChangeListener { _, isChecked ->
            SharedPref(requireActivity()).isDarkTheme = isChecked
           requireActivity().recreate()
        }
    }

    private fun eventObserve() {
        viewModel.settingsEvent.observe(viewLifecycleOwner, { event ->
            when (event) {
                SettingsEvent.LOAD_TO_FIRE_STORE -> binding.progressBar.visibility = ProgressBar.VISIBLE
                SettingsEvent.LOAD_FROM_FIRE_STORE -> binding.progressBar.visibility = ProgressBar.VISIBLE
                SettingsEvent.CANCEL_EVENT -> binding.progressBar.visibility = ProgressBar.INVISIBLE
            }
        })
    }

    private fun initCloudAuth() {
        viewModel.isAuth.observe(viewLifecycleOwner, {
            if (it) {
                binding.auth.isEnabled = false
                binding.exit.isEnabled = true
                binding.btnDownloadFireStore.apply { isEnabled = true
                    setColorFilter(R.color.colorAccent)}
                binding.btnSaveFireStore.apply { isEnabled = true
                    setColorFilter(R.color.colorAccent)}
            } else {
                binding.btnDownloadFireStore.apply { isEnabled = false
                    setColorFilter(Color.GRAY)}
                binding.btnSaveFireStore.apply { isEnabled = false
                    setColorFilter(Color.GRAY) }
                binding.auth.isEnabled = true
                binding.exit.isEnabled = false
            }
        })
    }


    private fun startLoginActivity() {
        val providers = listOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.ic_launcher_background)
                        .setTheme(R.style.LoginStyle)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            parentFragmentManager.beginTransaction().replace(R.id.navHostFragment, newInstance()).commit()
        }
    }

    private fun logout() {
        Snackbar.make(binding.root,R.string.logout_dialog_title, Snackbar.LENGTH_LONG).setAction("Yes") {
            context?.let {
                AuthUI.getInstance().signOut(it).addOnCompleteListener {
                    parentFragmentManager.beginTransaction().replace(
                            R.id.navHostFragment,
                            newInstance()
                    ).commit()
                }
            }
        }.show()
    }


    private fun initRadioGroup() {
        binding.rgSelectTextSize.checkedRB(SharedPref(requireActivity()).textSize)
      binding.rgSelectTextSize.setOnCheckedChangeListener { group, checkedId ->
          when(checkedId) {
              R.id.rb_small_text -> viewModel.changeTextSize(0 , requireActivity())
              R.id.rb_medium_text -> viewModel.changeTextSize(1 , requireActivity())
              R.id.rb_large_text -> viewModel.changeTextSize(2, requireActivity())
          }
      }
    }

}