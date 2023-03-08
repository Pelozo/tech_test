package com.example.tmdb_challenge.modules.genericError.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.tmdb_challenge.databinding.DialogErrorBinding

class GenericErrorDialog : DialogFragment() {

    private lateinit var binding: DialogErrorBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =  DialogErrorBinding.inflate(inflater, container, false)

        binding.btOk.setOnClickListener {
            dismiss()
        }
        return binding.root
    }
}