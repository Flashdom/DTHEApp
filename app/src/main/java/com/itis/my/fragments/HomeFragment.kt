package com.itis.my.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.itis.my.InfoRepository
import com.itis.my.R
import com.itis.my.databinding.FragmentHomeBinding
import com.itis.my.fragments.adding.AddingNoteFragment
import com.itis.my.model.Connection
import com.itis.my.model.Note
import com.itis.my.utils.getDateString
import com.itis.my.viewmodels.HomeViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.launch
import java.time.Instant
import kotlin.random.Random

class HomeFragment : ViewBindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    private var lastRezContent = ""

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents != null) {
            lastRezContent = result.contents
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.feedback))
                .setMessage("Обратная связь поможет лучше понять детали вашей работы")
                .setPositiveButton("Да") { dialog, _ ->
                    dialog.dismiss()
                    AddingNoteFragment().show(
                        parentFragmentManager,
                        "NOTES_FRAGMENT_TAG"
                    )
                }
                .setNegativeButton("Нет") { dialog, _ ->
                    dialog.dismiss()
                    viewModel.saveQrCode(
                        Connection(
                            Random.nextInt().toString(),
                            Instant.now().toEpochMilli(),
                            lastRezContent,
                            ""
                        )
                    )
                }
                .show()
        } else {
            Toast.makeText(requireContext(), "Ошибка сканирования", Toast.LENGTH_LONG).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserInfo()
        viewModel.userInfo.observe(viewLifecycleOwner) { user ->
            binding.tvFirstName.text = user.firstName
            binding.tvGroup.text = user.group
            binding.tvLastName.text = user.lastName
            binding.tvCreationDate.text = getDateString(user.date)

            binding.ivQrCode.setOnClickListener {
                QRCodeFragment().newInstance(user.id).show(parentFragmentManager, "qwe")
            }

            binding.ivQrCodeScan.setOnClickListener {
                barcodeLauncher.launch(ScanOptions().apply {
                    setBeepEnabled(false)
                    setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                })
            }
            setFragmentResultListener(HOME_REQUEST_KEY) { _, bundle ->
                val note = bundle.getParcelable<Note>(HOME_RESULT_KEY)
                if (note != null) {
                    viewModel.saveQrCode(
                        Connection(
                            Random.nextInt().toString(),
                            Instant.now().toEpochMilli(),
                            lastRezContent,
                            note.text
                        )
                    )
                }
            }
        }
    }

    companion object {
        const val HOME_REQUEST_KEY = "HRK"
        const val HOME_RESULT_KEY = "home_result"
    }


}