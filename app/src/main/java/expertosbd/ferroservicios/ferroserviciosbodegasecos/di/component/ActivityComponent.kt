package expertosbd.ferroservicios.ferroserviciosbodegasecos.di.component

import dagger.Component
import expertosbd.ferroservicios.ferroserviciosbodegasecos.di.module.ActivityModule
import expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.login.LoginActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.main.MainActivity

@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(loginActivity: LoginActivity)

    fun inject(mainActivity: MainActivity)
}