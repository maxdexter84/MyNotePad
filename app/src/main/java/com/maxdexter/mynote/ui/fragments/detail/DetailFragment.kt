package com.maxdexter.mynote.ui.fragments.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.*
import androidx.core.content.FileProvider

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.maxdexter.mynote.BuildConfig
import com.maxdexter.mynote.DetailActivity
import com.maxdexter.mynote.R
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.data.PictureUtils
import com.maxdexter.mynote.databinding.FragmentDetailBinding
import com.maxdexter.mynote.extensions.selectItem
import com.maxdexter.mynote.extensions.setDescription
import com.maxdexter.mynote.extensions.setTitle
import java.io.File
import java.io.IOException

class DetailFragment : Fragment() {
    private var share: ImageButton? = null
    private var delete: ImageButton? = null
    private val voice: ImageButton? = null
    private var image: ImageButton? = null
    private var gallery: ImageButton? = null
    private var mNote: Note? = null
    private var mDescriptionField: EditText? = null
    private var title_id: EditText? = null
    private val mButton: FloatingActionButton? = null
//    private var mRadioGroup: RadioGroup? = null
    private var mPhotoFile: File? = null
    private var photo: ImageView? = null
    private var noteId: String? = null
    private lateinit var detailViewModel: DetailFragmentViewModel
    private lateinit var detailViewModelFactory: DetailFragmentViewModelFactory
    var uuid: String? = null
    lateinit var binding: FragmentDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     //   assert(arguments != null)
//        noteId = arguments!!.getString(ARG_NOTE_ID) // Получение идентификатора заметки из аргументов

        //        assert noteId != null;
//        mNote = NotePad.get(getActivity()).getNote(noteId);
//        mPhotoFile = NotePad.get(getActivity()).getPhotoFile(mNote);
    }

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
        detailViewModel.newNote.observe(viewLifecycleOwner, { note ->
            binding.note = note
            binding.titleId.setTitle(note)
            binding.descriptId.setDescription(note)
            binding.radioGroup.selectItem(note)
            mPhotoFile = NotePad.get(activity).getPhotoFile(note)
        })

        initButtonGroup()
//        getTextDescript(binding.root)
//        getTextTitle(binding.root)
//     //   initRadioGroup(view)
//        initButtonGroup(binding.root)
//        initImageButton(binding.root)
//        updatePhotoView()
//        val liveData: LiveData<Int> = NotePad.get(context).liveData
//        liveData.observe(viewLifecycleOwner, Observer { integer ->
//            when (integer) {
//                0 -> {
//                    title_id!!.textSize = 14f
//                    mDescriptionField!!.textSize = 14f
//                    return@Observer
//                }
//                1 -> {
//                    title_id!!.textSize = 18f
//                    mDescriptionField!!.textSize = 18f
//                    return@Observer
//                }
//                2 -> {
//                    title_id!!.textSize = 22f
//                    mDescriptionField!!.textSize = 22f
//                }
//            }
//        })

//        binding.titleId.setText(mNote?.title)
//        binding.descriptId.setText(mNote?.description)
        updateNote()
        return binding.root
    }

    private fun updateNote() {
        binding.titleId.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                detailViewModel.changeTitle(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.descriptId.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                detailViewModel.changeDescription(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }





    private fun initImageButton(view: View) {
        registerForContextMenu(binding.imageViewFragmentDetail)
        binding.imageViewFragmentDetail .setOnClickListener {
            val intent = DetailActivity.newIntent(activity, mNote!!.uuid)
            startActivity(intent)
        }
    }

    private fun updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile!!.exists()) {
            photo!!.visibility = View.INVISIBLE
        } else {
            photo!!.visibility = View.VISIBLE
            Thread {
                val bitmap = PictureUtils.getScaleBitmap(mPhotoFile!!.path, requireActivity())
                photo!!.post { photo!!.setImageBitmap(bitmap) }
            }.start()
        }
    }

    private fun initButtonGroup() {
        binding.shareButton.setOnClickListener{ shareNote() }
        binding.deleteButton.setOnClickListener { v -> deleteNote(v) }
        binding.addImageButton.setOnClickListener { photoIntent() }
        binding.addGalleryButton.setOnClickListener { galleryIntent() }
    }

    private fun shareNote() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plane"
        intent.putExtra(Intent.EXTRA_TITLE, mNote!!.title)
        intent.putExtra(Intent.EXTRA_TEXT, mNote!!.description)
        startActivity(intent)
    }

    private fun deleteNote(v: View) {
        Snackbar.make(v, getString(R.string.snack_bar_delete_note), Snackbar.LENGTH_LONG).setAction(getString(R.string.yes)) {
            detailViewModel.deleteNote()
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToNoteListFragment())
        }.show()
    }

    private fun photoIntent() {
        val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri = FileProvider.getUriForFile(requireActivity(), "com.maxdexter.mynote.fileprovider", mPhotoFile!!)
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        val cameraActivity = requireActivity().packageManager.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
        for (activity in cameraActivity) {
            requireActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        startActivityForResult(captureImage, REQUEST_PHOTO)
    }

    private fun galleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        val uri = FileProvider.getUriForFile(requireActivity(), "com.maxdexter.mynote.fileprovider", mPhotoFile!!)
        galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        val cameraActivity = requireActivity().packageManager.queryIntentActivities(galleryIntent, PackageManager.MATCH_DEFAULT_ONLY)
        for (activity in cameraActivity) {
            requireActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        startActivityForResult(galleryIntent, REQUEST_GALLERY)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PHOTO) {
            val uri = FileProvider.getUriForFile(requireActivity(), "com.maxdexter.mynote.fileprovider", mPhotoFile!!)
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
                    val fos = requireActivity().openFileOutput(mPhotoFile!!.name, Context.MODE_PRIVATE)
                    if (BuildConfig.DEBUG && inputStream == null) {
                        error("Assertion failed")
                    }
                    val image = ByteArray(inputStream!!.available())
                    inputStream.read(image)
                    fos.write(image)
                    updatePhotoView()
                    inputStream.close()
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
            mPhotoFile!!.delete()
            requireActivity().recreate()
        }
        return super.onContextItemSelected(item)
    }

    companion object {
        private const val ARG_NOTE_ID = "note_id"
        const val NOTE_TYPE_SIMPLE = 0
        const val NOTE_TYPE_IMPORTANT = 1
        const val NOTE_TYPE_PASSWORD = 2
        private const val REQUEST_PHOTO = 2
        private const val REQUEST_GALLERY = 3

        //этот метод создает экземпляр фрагмента , упаковывает и задает его аргументы(этот метод вызывается в активносте хосте)
        @JvmStatic
        fun newInstance(noteId: String?): DetailFragment { //Присоединение аргументов к фрагменту
            val args = Bundle()
            args.putSerializable(ARG_NOTE_ID, noteId)
            val detailFragment = DetailFragment()
            detailFragment.arguments = args
            return detailFragment
        }
    }
}