package com.itis.my.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.net.toUri
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.itis.my.databinding.FragmentPhotoDetailedBinding


class PhotoDetailedDialogFragment : BottomSheetDialogFragment() {


    private var _binding: FragmentPhotoDetailedBinding? = null
    private val binding: FragmentPhotoDetailedBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {

        val bottomSheet =
            dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        (bottomSheet?.layoutParams as ViewGroup.MarginLayoutParams).topMargin =
            16
        object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {


            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (BottomSheetBehavior.from(bottomSheet).state == BottomSheetBehavior.STATE_EXPANDED)
                {
                    BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
                }
                else
                {
                    BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

        }
        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivPhotoDetailed.setImageURI(arguments?.getString("uri", "")?.toUri())

    }

    fun newInstance(uri: String): BottomSheetDialogFragment {
        return PhotoDetailedDialogFragment().apply {
            arguments = Bundle().apply {
                putString("uri", uri)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}