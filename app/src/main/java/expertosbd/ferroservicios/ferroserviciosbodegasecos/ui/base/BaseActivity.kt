package expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.base

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecos.R
import expertosbd.ferroservicios.ferroserviciosbodegasecos.utils.ConnectivityReceiver
import expertosbd.ferroservicios.ferroserviciosbodegasecos.utils.Utils

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {
    private var snackBar: Snackbar? = null
    var rootLayoutID: Int? = null
    var isConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(ConnectivityReceiver(),
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun showMessage(isConnected: Boolean) {
        if (!isConnected) {
            val messageToUser = "No se detecta conexión a Internet"
            onDisconnectedListener?.onDisconnected()
            val utils = Utils()
            if (rootLayoutID != null) {
                snackBar = utils.showSnackBar(findViewById(rootLayoutID!!), messageToUser,
                        resources.getColor(R.color.colorAccent), Snackbar.LENGTH_INDEFINITE)
                snackBar?.show()
            }

        } else {
            snackBar?.dismiss()
            onConnectedListener?.onConnectionAvailable()
        }
    }

    fun showMessage(message: String) {
        val utils = Utils()
        if (rootLayoutID != null) {
            snackBar = utils.showSnackBar(findViewById(rootLayoutID!!), message,
                    resources.getColor(R.color.colorAccent), Snackbar.LENGTH_LONG)
            snackBar?.show()
        }
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
        this.isConnected = isConnected
    }

    interface OnConnectionAvailableListener {
        fun onConnectionAvailable()
    }

    interface OnDisconnectedListener {
        fun onDisconnected()
    }

    companion object {
        var onConnectedListener: OnConnectionAvailableListener? = null
        var onDisconnectedListener: OnDisconnectedListener? = null
    }
}