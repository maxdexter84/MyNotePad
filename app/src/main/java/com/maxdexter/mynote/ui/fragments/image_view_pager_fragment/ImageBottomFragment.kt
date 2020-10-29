package com.maxdexter.mynote.ui.fragments.image_view_pager_fragment


import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.maxdexter.mynote.R
import com.maxdexter.mynote.data.adapters.ViewPagerAdapter
import com.maxdexter.mynote.databinding.ImageBottomFragmentBinding


const val TAG = "touch"
//три возможные состояния матрици
const val NONE = 0
const val DRAG = 1
const val ZOOM = 2
class ImageBottomFragment : Fragment() {
    //Эти матрици будут использоваться для перемещения и масштабирования изображегия
    var matrix: Matrix = Matrix()
    var savedMatrix: Matrix = Matrix()
    var mode = NONE
    //Данные для масштабирования
    val start: PointF = PointF()
    val mid: PointF = PointF()
    var oldDist = 1F
    lateinit var binding: ImageBottomFragmentBinding

    private lateinit var mViewModel: ImageBottomViewModel
    private lateinit var mViewModelFactory: ImageBottomViewModelFragment
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var viewPager2: ViewPager2
    private var currentPosition: Int? = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.image_bottom_fragment, container, false)
        val path = arguments?.let { ImageBottomFragmentArgs.fromBundle(it) }?.path
        currentPosition = arguments?.let { ImageBottomFragmentArgs.fromBundle(it) }?.itemPosition
        val noteUUID = arguments?.let { ImageBottomFragmentArgs.fromBundle(it) }?.uuid
        if (path != null)
        mViewModelFactory = ImageBottomViewModelFragment(path)
        mViewModel = ViewModelProvider(this, mViewModelFactory).get(ImageBottomViewModel::class.java)
        //binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initViewPager()
        initBackBtn(noteUUID)


        return binding.root
    }

    private fun initBackBtn(noteUUID: String?) {
        binding.materialButton.setOnClickListener {
            if (noteUUID != null) {
               findNavController().navigate(ImageBottomFragmentDirections.actionImageBottomFragmentToDetailFragment(noteUUID))

            }
        }
    }

    private fun initTabLayout(lastIndex:Int) {
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
               in 0..lastIndex -> tab.text ="${position + 1}"
            }
        }.attach()
    }

    private fun initViewPager() {
        viewPager2 = binding.imageViewPager
        mViewModel.imageUriList.observe(viewLifecycleOwner, { list ->
            adapter = ViewPagerAdapter(list)
            viewPager2.adapter = adapter
            initTabLayout(list.lastIndex)
            Log.i("CLICK", "$currentPosition ImageBottom")
        })
    }

    override fun onResume() {
        super.onResume()
        viewPager2.setCurrentItem(currentPosition ?: 0, false)
    }
}