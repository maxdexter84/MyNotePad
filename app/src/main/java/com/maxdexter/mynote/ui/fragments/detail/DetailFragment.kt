package com.maxdexter.mynote.ui.fragments.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.view.*
import android.view.ContextMenu.ContextMenuInfo

import androidx.core.content.FileProvider

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.maxdexter.mynote.BuildConfig
import com.maxdexter.mynote.R
import com.maxdexter.mynote.SharedPref
import com.maxdexter.mynote.databinding.FragmentDetailBinding
import com.maxdexter.mynote.extensions.*
import com.maxdexter.mynote.repository.Repository
import com.maxdexter.mynote.utils.DetailEvent
import java.io.File
import java.io.IOException


const val REQUEST_PHOTO = 2
private const val REQUEST_GALLERY = 3
class DetailFragment : Fragment() {
    private lateinit var photoFile: File
    private lateinit var detailViewModel: DetailFragmentViewModel
    private lateinit var detailViewModelFactory: DetailFragmentViewModelFactory
    var uuid: String? = null
    lateinit var binding: FragmentDetailBinding
    lateinit var repository: Repository

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
       val args = arguments?.let { DetailFragmentArgs.fromBundle(it) }
        val context = context
        if(context != null && args != null) {
            detailViewModelFactory = DetailFragmentViewModelFactory(args.noteId,context)
        }

        detailViewModel = ViewModelProvider(this, detailViewModelFactory).get(DetailFragmentViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = detailViewModel
        noteObserve()
        eventObserve()
        updateNote()
        registerForContextMenu(binding.imageViewFragmentDetail)
        return binding.root
    }

    private fun noteObserve() {
        detailViewModel.newNote.observe(viewLifecycleOwner, { note ->
            binding.note = note
            binding.titleId.apply { setTitle(note)
                                    setTextSize(SharedPref(requireActivity()).textSize)}
            binding.descriptId.apply { setDescription(note)
                                    setTextSize(SharedPref(requireActivity()).textSize)}
            binding.radioGroup.selectItem(note)
            photoFile = detailViewModel.getPhotoFile()
            updatePhotoView()
        })
    }

    private fun eventObserve() {
        detailViewModel.eventType.observe(viewLifecycleOwner, {
            if (it != null) {
                when (it.first) {
                    DetailEvent.SHARE -> startActivity(it.second)
                    DetailEvent.PHOTO -> startActivityForResult(it.second, REQUEST_PHOTO)
                    DetailEvent.DELETE -> view?.let { view -> deleteNote(view) }
                    DetailEvent.SAVE -> findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToNoteListFragment())
                    DetailEvent.ZOOM_IMAGE -> findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToImageDialogFragment(photoFile.path))
                    else -> startActivityForResult(it.second, REQUEST_GALLERY)
                }
            }
        })
    }

    private fun updateNote() {
        binding.titleId.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                detailViewModel.changeTitle(p0.toString())
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.descriptId.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                detailViewModel.changeDescription(p0.toString())
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
    }


    private fun  updatePhotoView( ) {
        if (!photoFile.exists()) {
            binding.imageViewFragmentDetail.visibility = View.INVISIBLE
        } else {
            binding.imageViewFragmentDetail.visibility = View.VISIBLE
            binding.imageViewFragmentDetail.setImage(requireContext(),photoFile.path)
        }
    }


    private fun deleteNote(v: View) {
        Snackbar.make(v, getString(R.string.snack_bar_delete_note), Snackbar.LENGTH_SHORT).setAction(getString(R.string.yes)) {
            detailViewModel.deleteNote()
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToNoteListFragment())
        }.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PHOTO) {
            val uri = photoFile.let { FileProvider.getUriForFile(requireActivity(), "com.maxdexter.mynote.fileprovider", it) }
            requireActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            updatePhotoView()
        }
        if (requestCode == REQUEST_GALLERY && data != null) {
            // Получаем URI изображения
            val imageUri = data.data
            if (imageUri != null) {
                try {
                    // Получаем InputStream, из которого будем декодировать Bitmap
                    val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                    val fos = requireActivity().openFileOutput(photoFile.name, Context.MODE_PRIVATE)
                    if (BuildConfig.DEBUG && inputStream == null) {
                        error("Assertion failed")
                    }
                    val image = inputStream?.available()?.let { ByteArray(it) }
                    inputStream?.read(image)
                    fos.write(image)
                    updatePhotoView()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = requireActivity().menuInflater
        inflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete) {
            photoFile.absoluteFile.delete()
            updatePhotoView()
        }
        return super.onContextItemSelected(item)
    }



}