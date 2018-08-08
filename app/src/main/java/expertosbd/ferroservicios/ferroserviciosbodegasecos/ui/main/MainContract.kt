package expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.main

import expertosbd.ferroservicios.ferroserviciosbodegasecos.model.*
import expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.base.BaseContract
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class MainContract {

    interface View : BaseContract.View {
        fun showProgress(show: Boolean)
        fun onFetchDataSuccess(items: MutableList<Evento>)
        fun onPostSuccesful(message: String)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun fetchData()
        fun postEvento(evento: NuevoEvento)
        fun inicioManiobra(inicioManiobra: InicioManiobra): Single<PostSuccesfulResponse>
        fun finManiobra(finManiobra: FinManiobra): Single<PostSuccesfulResponse>
        fun addSubscription(disposable: Disposable)
    }
}