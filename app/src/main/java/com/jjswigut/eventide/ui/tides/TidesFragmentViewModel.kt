package com.jjswigut.eventide.ui.tides

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.jjswigut.eventide.data.repository.Repository

class TidesFragmentViewModel @ViewModelInject constructor(
    repo: Repository
) : ViewModel()