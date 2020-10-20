package com.jjswigut.eventide.ui.search


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jjswigut.eventide.databinding.FragmentSearchBinding
import com.jjswigut.eventide.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}