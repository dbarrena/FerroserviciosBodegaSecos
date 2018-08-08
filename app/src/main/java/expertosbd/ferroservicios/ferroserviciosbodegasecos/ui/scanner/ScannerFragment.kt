package expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.scanner


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.Result
import expertosbd.ferroservicios.ferroserviciosbodegasecos.model.Evento
import expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.main.MainActivity
import me.dm7.barcodescanner.zxing.ZXingScannerView

private const val EVENTO = "evento"

class ScannerFragment : Fragment(), ZXingScannerView.ResultHandler, ScannerContract.View {

    private lateinit var activity: MainActivity
    private var evento: Evento? = null
    private var listener: OnScannedListener? = null
    lateinit var scannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            evento = it.getSerializable(EVENTO) as Evento
        }

        activity = getActivity() as MainActivity
        activity.setActionBarTitle("Evento ${evento?.eventoBodegaID}")
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        scannerView = ZXingScannerView(activity)
        return scannerView
    }

    override fun handleResult(barcode: Result?) {
        barcode?.text?.let { listener?.onScanned(it) }
    }

    override fun showProgress(show: Boolean) {
        activity.showProgress(true)
    }

    override fun onPostSuccessful(message: String, paqueteID: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun resumeCamera(){
        scannerView.resumeCameraPreview(this)
    }

    override fun showErrorMessage(error: String) {
        activity.showErrorMessage(error)
    }

    interface OnScannedListener {
        fun onScanned(barcode: String)
    }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnScannedListener) {
            listener = context
        } else {
            throw RuntimeException(
                    context.toString() + " must implement OnScannedListener")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity.setActionBarTitle("Eventos")
    }

    companion object {
        @JvmStatic
        fun newInstance(evento: Evento) =
                ScannerFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(EVENTO, evento)
                    }
                }
    }
}
