package com.maxdexter.mynote.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.maxdexter.mynote.R
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.data.PictureUtils
import com.maxdexter.mynote.databinding.FragmentFullscreenBinding
import com.maxdexter.mynote.model.Note
import java.io.File

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenFragment : Fragment() {
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */

    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable { // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        val flags = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        val activity: Activity? = activity
        if (activity != null
                && activity.window != null) {
            activity.window.decorView.systemUiVisibility = flags
        }
        val actionBar = supportActionBar
        actionBar?.hide()
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */

    lateinit var note: Note

    private val mShowPart2Runnable = Runnable { // Delayed display of UI elements
        val actionBar = supportActionBar
        actionBar?.show()
        binding.fullscreenContentControls.visibility = View.VISIBLE
    }
    private var mVisible = false
    private val mHideRunnable = Runnable { hide() }
    private lateinit var binding: FragmentFullscreenBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fullscreen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mVisible = true


        val args = arguments?.let { FullscreenFragmentArgs.fromBundle(it) }
        val noteId = args?.noteUUID
        note = NotePad.get(activity).getNote(noteId)
        val file = NotePad.get(context).getPhotoFile(note)
        val bitmap = PictureUtils.getScaleBitmap(file.path, requireActivity())
        binding.fullscreenContent.setImageBitmap(bitmap)
        // Set up the user interaction to manually show or hide the system UI.
        binding.fullscreenContent.setOnClickListener(View.OnClickListener { toggle() })

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
       // binding.dummyButton.setOnTouchListener(mDelayHideTouchListener)
        binding.dummyButton.setOnClickListener { val uuid = note.uuid
            findNavController().navigate(FullscreenFragmentDirections.actionFullscreenFragmentToDetailFragment(uuid)) }
    }



    override fun onResume() {
        super.onResume()
        if (activity != null && requireActivity().window != null) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    override fun onPause() {
        super.onPause()
        if (activity != null && requireActivity().window != null) {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

            // Clear the systemUiVisibility flag
            requireActivity().window.decorView.systemUiVisibility = 0
        }
        show()
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        val actionBar = supportActionBar
        actionBar?.hide()
        binding.fullscreenContentControls.visibility = View.INVISIBLE
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    @SuppressLint("InlinedApi")
    private fun show() {
        // Show the system bar
        binding.fullscreenContent.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
        val actionBar = supportActionBar
        actionBar?.show()
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    private val supportActionBar: ActionBar?
        private get() {
            var actionBar: ActionBar? = null
            if (activity is AppCompatActivity) {
                val activity = activity as AppCompatActivity?
                actionBar = activity!!.supportActionBar
            }
            return actionBar
        }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [.AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [.AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000
        private const val UI_ANIMATION_DELAY = 300
        private const val ARG_NOTE = "arg_note_id"

        //этот метод создает экземпляр фрагмента , упаковывает и задает его аргументы(этот метод вызывается в активносте хосте)
//        @JvmStatic
//        fun newInstance(noteId: String?): FullscreenFragment { //Присоединение аргументов к фрагменту
//            val args = Bundle()
//            args.putSerializable(ARG_NOTE, noteId)
//            val fullscreenFragment = FullscreenFragment()
//            fullscreenFragment.arguments = args
//            return fullscreenFragment
//        }
    }
}