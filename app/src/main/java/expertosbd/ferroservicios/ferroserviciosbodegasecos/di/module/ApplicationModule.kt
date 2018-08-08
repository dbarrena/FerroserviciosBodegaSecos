package expertosbd.ferroservicios.ferroserviciosbodegasecos.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import expertosbd.ferroservicios.ferroserviciosbodegasecos.BaseApp

@Module
class ApplicationModule(private val baseApp: BaseApp) {

    @Provides
    @Singleton
    fun provideApplication(): Application = baseApp

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = baseApp
}