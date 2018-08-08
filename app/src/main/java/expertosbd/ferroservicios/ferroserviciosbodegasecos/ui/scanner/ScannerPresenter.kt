package expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.scanner

import expertosbd.ferroservicios.ferroserviciosbodegasecos.api.ApiServiceInterface
import expertosbd.ferroservicios.ferroserviciosbodegasecos.model.Evento
import io.reactivex.disposables.CompositeDisposable

class ScannerPresenter : ScannerContract.Presenter {

    private val api: ApiServiceInterface = ApiServiceInterface.create()
    private val subscriptions = CompositeDisposable()
    private lateinit var view: ScannerContract.View

    override fun postScannerResult(evento: Evento) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun subscribe() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unsubscribe() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun attach(view: ScannerContract.View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}