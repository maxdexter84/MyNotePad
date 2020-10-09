package com.maxdexter.mynote.ui.fragments.image_dialog


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Matrix
import android.graphics.PointF
import android.os.Bundle
import android.util.FloatMath
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ablanco.zoomy.Zoomy
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maxdexter.mynote.R
import com.maxdexter.mynote.databinding.ImageDialogFragmentBinding
import com.maxdexter.mynote.extensions.setImage
import java.lang.Math.sqrt
import java.lang.StrictMath.sqrt
import kotlin.math.sqrt

const val TAG = "touch"
//три возможные состояния матрици
const val NONE = 0
const val DRAG = 1
const val ZOOM = 2
class ImageDialogFragment : BottomSheetDialogFragment() {
    //Эти матрици будут использоваться для перемещения и масштабирования изображегия
    var matrix: Matrix = Matrix()
    var savedMatrix: Matrix = Matrix()
    var mode = NONE
    //Данные для масштабирования
    val start: PointF = PointF()
    val mid: PointF = PointF()
    var oldDist = 1F
    lateinit var binding: ImageDialogFragmentBinding

    private lateinit var viewModel: ImageDialogViewModel
    private lateinit var viewModelFactory: ImageDialogViewModelFragment

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.image_dialog_fragment, container, false)
        val args = arguments?.let { ImageDialogFragmentArgs.fromBundle(it) }?.path
        if (args != null)
        viewModelFactory = ImageDialogViewModelFragment(args)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ImageDialogViewModel::class.java)
        //binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.ivImageDialogFragment.setImage(requireContext(), args)



        return binding.root
    }
}