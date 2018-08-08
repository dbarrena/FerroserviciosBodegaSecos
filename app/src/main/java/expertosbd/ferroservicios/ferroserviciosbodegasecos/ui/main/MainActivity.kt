package expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.main

import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import expertosbd.ferroservicios.ferroserviciosbodegasecos.R
import expertosbd.ferroservicios.ferroserviciosbodegasecos.di.component.DaggerActivityComponent
import expertosbd.ferroservicios.ferroserviciosbodegasecos.di.module.ActivityModule
import expertosbd.ferroservicios.ferroserviciosbodegasecos.model.*
import expertosbd.ferroservicios.ferroserviciosbodegasecos.prefs
import expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.base.BaseActivity
import expertosbd.ferroservicios.ferroserviciosbodegasecos.ui.scanner.ScannerFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View, SearchView.OnQueryTextListener,
        EventosAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
        ScannerFragment.OnScannedListener {
    @Inject
    lateinit var presenter: MainContract.Presenter
    private lateinit var adapter: EventosAdapter
    var selectedEvent: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rootLayoutID = R.id.main_root
        setSupportActionBar(toolbar_main)
        injectDependency()
        supportActionBar?.title = "Eventos"
        setSearchView(eventos_search)
        swipe_container.setOnRefreshListener(this)
        presenter.attach(this)
        presenter.subscribe()
    }

    override fun showErrorMessage(error: String) {
        showMessage(error)

        val fragment = supportFragmentManager.findFragmentByTag("scanner_fragment")
        if (fragment != null) {
            fragment as ScannerFragment
            fragment.resumeCamera()
        }
    }

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun setSearchView(searchView: SearchView) {
        searchView.setOnQueryTextListener(this)
        searchView.isSubmitButtonEnabled = true
        searchView.queryHint = "Busqueda"
        searchView.isFocusable = false
        searchView.isIconified = false
        searchView.clearFocus()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (::adapter.isInitialized) {
            adapter.filter.filter(query)
        }
        return false
    }

    override fun onItemClicked(item: Evento) {
        selectedEvent = item.eventoBodegaID
        val fragment = ScannerFragment.newInstance(item)
        supportFragmentManager.beginTransaction()
                .add(R.id.main_content, fragment, "scanner_fragment")
                .addToBackStack("main")
                .commit()
    }

    override fun onRefresh() {
        presenter.fetchData()
    }

    override fun showProgress(show: Boolean) {
        val progressBar = findViewById<FrameLayout>(R.id.progress_bar_main)
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE

            if (swipe_container.isRefreshing) {
                swipe_container.isRefreshing = false
            }
        }
    }

    override fun onFetchDataSuccess(items: MutableList<Evento>) {
        adapter = EventosAdapter(this, items, this)
        eventos_list.adapter = adapter
        eventos_list.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        eventos_list.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        showProgress(false)
    }

    override fun onScanned(barcode: String) {
        showScannerDialog(barcode)
    }

    private fun showScannerDialog(barcode: String) {
        MaterialDialog.Builder(this)
                .title(barcode)
                .customView(R.layout.dialog_scanner, false)
                .positiveText(R.string.positive)
                .negativeText(R.string.negative)
                .onAny { dialog, which ->
                    when (which) {
                        DialogAction.POSITIVE -> {
                            val pesoNeto = dialog.findViewById(R.id.peso_neto) as EditText
                            val datosAdicionales =
                                    dialog.findViewById(R.id.datos_adicionales) as EditText
                            val nuevoEvento = NuevoEvento(barcode, pesoNeto.text.toString(),
                                    datosAdicionales.text.toString(), selectedEvent)
                            presenter.postEvento(nuevoEvento)
                            showProgress(true)
                        }
                        DialogAction.NEGATIVE -> {
                            dialog.dismiss()
                            //scannerFragment.resumeCamera()
                        }
                    }
                }
                .show()
    }

    override fun OnMoreOptionsClicked(evento: Evento) {
        showMoreOptionsDialog(evento)
    }

    private fun showMoreOptionsDialog(evento: Evento) {
        val dialog = MaterialDialog.Builder(this)
                .title("Más opciones")
                .titleColorRes(R.color.colorPrimary)
                .dividerColorRes(R.color.colorPrimary)
                .customView(R.layout.more_options_dialog, false)
                .negativeText(R.string.negative)
                .onAny { dialog, which ->
                    when (which) {
                        DialogAction.NEGATIVE -> {
                            dialog.dismiss()
                        }
                    }
                }
                .show()

        val progressBar = dialog.findViewById(R.id.progress_bar) as ProgressBar
        val buttons = dialog.findViewById(R.id.buttons) as LinearLayout

        val inicioManiobraBtn = dialog.findViewById(R.id.inicio_maniobra_btn) as Button
        val finManiobraBtn = dialog.findViewById(R.id.fin_maniobra_btn) as Button

        val format = SimpleDateFormat("dd/MM/yyyy,HH:mm")

        inicioManiobraBtn.setOnClickListener {
            buttons.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            val date = format.format(Calendar.getInstance().time)
            val inicioManiobra = InicioManiobra(date, prefs.user, evento.eventoBodegaID)

            presenter.addSubscription(
                    presenter.inicioManiobra(inicioManiobra).subscribe(
                            {
                                showMessage("Inicio Maniobra Registrado")
                                dialog.dismiss()
                            },
                            { error ->
                                showErrorMessage(error.localizedMessage)
                                buttons.visibility = View.VISIBLE
                                progressBar.visibility = View.GONE
                            }
                    ))
        }

        finManiobraBtn.setOnClickListener {
            buttons.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            val date = format.format(Calendar.getInstance().time)
            val finManiobra = FinManiobra(date, prefs.user, evento.eventoBodegaID)

            presenter.addSubscription(
                    presenter.finManiobra(finManiobra).subscribe(
                            {
                                showMessage("Fin Maniobra Registrado")
                                dialog.dismiss()
                            },
                            { error ->
                                showErrorMessage(error.localizedMessage)
                                buttons.visibility = View.VISIBLE
                                progressBar.visibility = View.GONE
                            }
                    ))
        }

    }

    override fun onPostSuccesful(message: String) {
        showMessage(message)
        showProgress(false)
        val fragment =
                supportFragmentManager.findFragmentByTag("scanner_fragment") as ScannerFragment
        fragment.resumeCamera()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFragmentManager.popBackStack()
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .build()

        activityComponent.inject(this)
    }
}
