package expertosbd.ferroservicios.ferroserviciosbodegasecos.utils

import android.support.design.widget.Snackbar
import android.view.View
import android.widget.TextView

class Utils {

    fun showSnackBar(view: View, text: String, backgroundColor: Int): Snackbar {
        return Snackbar.make(
                view,
                text,
                Snackbar.LENGTH_SHORT
        ).withColor(backgroundColor)
    }

    fun showSnackBar(view: View, text: String, backgroundColor: Int, duration: Int): Snackbar {
        val snackbar = Snackbar.make(
                view,
                text,
                duration
        ).withColor(backgroundColor)

        val snackview = snackbar.getView()
        val tv = snackview.findViewById(android.support.design.R.id.snackbar_text) as TextView
        //tv.setTextColor(Color.BLACK)

        return snackbar
    }
}