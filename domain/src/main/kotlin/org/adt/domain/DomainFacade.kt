package org.adt.domain

import dagger.Module
import org.adt.domain.modules.DomainModule

@Module(includes = [DomainModule::class])
class DomainFacade