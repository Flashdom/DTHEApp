package com.itis.my.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.itis.my.databinding.FragmentQrCodeBinding
import com.journeyapps.barcodescanner.BarcodeEncoder


class QRCodeFragment : BottomSheetDialogFragment() {


    private var _binding: FragmentQrCodeBinding? = null
    private val binding: FragmentQrCodeBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrCodeBinding.inflate(inflater, container, false)
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
                if (BottomSheetBehavior.from(bottomSheet).state == BottomSheetBehavior.STATE_EXPANDED) {
                    BottomSheetBehavior.from(bottomSheet).state =
                        BottomSheetBehavior.STATE_COLLAPSED
                } else {
                    BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

        }
        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivQrCode.apply {
            try {
                setImageBitmap(
                    BarcodeEncoder().encodeBitmap(
                        arguments?.getString("data", ""),
                        BarcodeFormat.QR_CODE,
                        400,
                        400
                    )
                )
            } catch (e: Exception) {
                Log.d("a", "b")
            }

        }
    }

    fun newInstance(data: String): BottomSheetDialogFragment {
        return QRCodeFragment().apply {
            arguments = Bundle().apply {
                putString("data", data)
            }
        }
    }
}