package com.example.libconsumer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import com.example.libconsumer.databinding.ActivityMainBinding
import com.grupocasasbahia.libreconhecimentofacial.VoucherActivity


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        callActivityLib()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun callActivityLib(){
        val intent = Intent(this, VoucherActivity::class.java)
        intent.putExtra("operacao","Voucher_Digital");
        intent.putExtra("empresaFilial",1);
        intent.putExtra("filial",1000);
        intent.putExtra("empresaFuncionario",21);
        intent.putExtra("matricula","361798");
        intent.putExtra("cpfCliente","60756201250");
        resultLauncher.launch(intent);

    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            Log.e("vaiii", "SUCESSO---> " + (data?.getLongExtra("codigoAutorizacaoCompra", 0).toString()))

            binding.fab.setOnClickListener { view ->
                Snackbar.make(view, data?.getLongExtra("codigoAutorizacaoCompra", 0).toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.fab).show()
            }

        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            val data: Intent? = result.data
            Log.e("vaiii", "Error---> " + (data?.getStringExtra("MSG") ?: ""))

            binding.fab.setOnClickListener { view ->
                Snackbar.make(view, "Deu Ruim", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.fab).show()
            }
        }
    }
}