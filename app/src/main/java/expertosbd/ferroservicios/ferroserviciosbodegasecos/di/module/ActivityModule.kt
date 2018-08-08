package expertosbd.ferroservicios.ferroserviciosbodegasecos.di.module

import android.app.Activity
import dagger.Module
import dagger.Provides
import expertosbd.ferroservicios.ferroserviciosbodegasecos.api.ApiServiceInterface
import expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.login.LoginContract
import expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.login.LoginPresenter
import expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.main.MainContract
import expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.main.MainPresenter

@Module
class ActivityModule(private var activity: Activity) {

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun provideLoginPresenter(): LoginContract.Presenter {
        return LoginPresenter()
    }

    @Provides
    fun provideMainPresenter(): MainContract.Presenter {
        return MainPresenter()
    }

    @Provides
    fun provideApiService(): ApiServiceInterface {
        return ApiServiceInterface.create()
    }
}