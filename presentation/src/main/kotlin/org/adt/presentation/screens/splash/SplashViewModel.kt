package org.adt.presentation.screens.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.adt.domain.abstraction.IDomainRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val _domainRepository: IDomainRepository): ViewModel() {

}