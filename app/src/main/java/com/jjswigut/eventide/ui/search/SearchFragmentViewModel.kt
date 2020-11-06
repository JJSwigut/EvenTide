package com.jjswigut.eventide.ui.search

import android.location.Location
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjswigut.eventide.data.repository.Repository


class SearchFragmentViewModel @ViewModelInject constructor(
        private val repo: Repository
) : ViewModel() {

        val tideStations = repo.getStations()

        var userLocation = MutableLiveData<Location>()
}

