package com.maxdexter.mynote.ui.fragments.settings


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import com.maxdexter.mynote.R
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.databinding.SettingsFragmentBinding
import com.maxdexter.mynote.repository.Repository

private const val RC_SIGN_IN = 458
class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: SettingsFragmentBinding
    private lateinit var viewModelFactory: SettingsViewModelFactory
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false)
        val noteDao = context?.let { NotePad.get(it)?.database?.mNoteDao() }

        viewModelFactory = SettingsViewModelFactory(noteDao?.let { Repository() }, viewLifecycleOwner, context)

        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)

        binding.settingsViewModel = viewModel
        binding.lifecycleOwner = this

        binding.auth.setOnClickListener { startLoginActivity() }

        initCloudAuth()
        viewModel.logOut.observe(viewLifecycleOwner, { if (it) logout() })
        return binding.root
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

}