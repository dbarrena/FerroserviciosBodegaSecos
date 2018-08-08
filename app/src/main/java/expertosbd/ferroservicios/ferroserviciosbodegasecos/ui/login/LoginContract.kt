package expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.login

import expertosbd.ferroservicios.ferroserviciosbodegasecos.model.Login
import expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.base.BaseContract

class LoginContract {

    interface View : BaseContract.View {
        fun showProgress(show: Boolean)
        fun loginResult(result: Boolean, login: Login?)
        fun showVersion(localVersion: String, currentVersion: String)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun checkLogin(user: String, password: String)
        fun checkCurrentVersion()
    }
}