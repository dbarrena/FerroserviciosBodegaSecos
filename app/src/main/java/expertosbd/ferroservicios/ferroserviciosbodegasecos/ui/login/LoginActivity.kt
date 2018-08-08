package expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.github.javiersantos.materialstyleddialogs.enums.Style
import expertosbd.ferroservicios.ferroserviciosbodegasecos.R
import expertosbd.ferroservicios.ferroserviciosbodegasecos.di.component.DaggerActivityComponent
import expertosbd.ferroservicios.ferroserviciosbodegasecos.di.module.ActivityModule
import expertosbd.ferroservicios.ferroserviciosbodegasecos.model.Login
import expertosbd.ferroservicios.ferroserviciosbodegasecos.prefs
import expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.base.BaseActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.main.MainActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecos.utils.withColor
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : BaseActivity(), LoginContract.View,
        BaseActivity.OnConnectionAvailableListener {

    @Inject
    lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        rootLayoutID = R.id.main_root_login
        injectDependency()
        onConnectedListener = this

        login_button.setOnClickListener {
            if (isConnected) {
                showProgress(true)
                presenter.checkLogin(user_field.text.toString().toUpperCase(),
                        password_field.text.toString())
            }
        }

        presenter.attach(this)
    }

    override fun loginResult(result: Boolean, login: Login?) {
        showProgress(false)
        if (result) {
            prefs.user = user_field.text.toString()
            prefs.firstName = login!!.nombre
            prefs.lastName = login.apellidoPaterno
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Snackbar.make(
                    main_root_login,
                    "Contraseña no válida",
                    Snackbar.LENGTH_SHORT
            ).withColor(resources.getColor(R.color.colorAccent)).show()
        }
    }

    override fun showErrorMessage(error: String) {
        showMessage(error)
        showProgress(false)
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progress_bar_login.visibility = View.VISIBLE
        } else {
            progress_bar_login.visibility = View.GONE
        }
    }

    override fun showVersion(localVersion: String, currentVersion: String) {
        if (localVersion == currentVersion) {
            version_number.text = "Versión: $localVersion"
        } else {
            MaterialStyledDialog.Builder(this)
                    .setTitle("Versión Incompatible")
                    .setDescription(
                            "Versión local: $localVersion \nVersión actual: $currentVersion " +
                                    "\n\nPor favor actualiza la aplicacion.\n\n")
                    .setStyle(Style.HEADER_WITH_ICON)
                    .setHeaderColor(R.color.colorAccent)
                    .setIcon(ContextCompat.getDrawable(this, R.drawable.alert_icon)!!)
                    .setCancelable(false)
                    .withDialogAnimation(false) //maybe
                    .show()
        }
    }

    override fun onConnectionAvailable() {
        if (version_number.text.isEmpty()) {
            presenter.checkCurrentVersion()
        }
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .build()

        activityComponent.inject(this)
    }
}
