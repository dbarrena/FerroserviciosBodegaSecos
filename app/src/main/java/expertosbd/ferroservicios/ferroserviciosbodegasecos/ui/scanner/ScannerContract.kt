package expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.scanner

import expertosbd.ferroservicios.ferroserviciosbodegasecos.model.Evento
import expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.base.BaseContract

class ScannerContract {

    interface View : BaseContract.View {
        fun showProgress(show: Boolean)
        fun onPostSuccessful(message: String, paqueteID: Int)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun postScannerResult(evento: Evento)
    }

}