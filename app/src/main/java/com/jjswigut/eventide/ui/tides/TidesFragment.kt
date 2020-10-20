package com.jjswigut.eventide.ui.tides


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jjswigut.eventide.databinding.FragmentTidesBinding
import com.jjswigut.eventide.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TidesFragment : BaseFragment() {

    private var _binding: FragmentTidesBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTidesBinding.inflate(inflater, container, false)
        val view = binding.root

        return view

    }
}