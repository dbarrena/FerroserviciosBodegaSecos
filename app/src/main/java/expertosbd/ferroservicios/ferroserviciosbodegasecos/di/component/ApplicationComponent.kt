package expertosbd.ferroservicios.ferroserviciosbodegasecos.di.component

import dagger.Component
import expertosbd.ferroservicios.ferroserviciosbodegasecos.BaseApp
import expertosbd.ferroservicios.ferroserviciosbodegasecos.di.module.ApplicationModule

@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun inject(application: BaseApp)
}