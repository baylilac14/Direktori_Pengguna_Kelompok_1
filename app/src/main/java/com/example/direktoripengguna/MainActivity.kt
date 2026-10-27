package com.example.direktoripengguna

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.direktoripengguna.data.DataIndonesia
import com.example.direktoripengguna.data.User
import com.example.direktoripengguna.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var progress: ProgressBar
    private lateinit var btnUlang: Button
    private lateinit var listUser: ListView

    private var daftarUser: List<User> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        progress = findViewById(R.id.progress)
        btnUlang = findViewById(R.id.btnUlang)
        listUser = findViewById(R.id.listUser)

        btnUlang.setOnClickListener { muatUsers() }

        listUser.setOnItemClickListener { _, _, position, _ ->
            val user = daftarUser[position]
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra(PostActivity.EXTRA_USER_ID, user.id)
            intent.putExtra(PostActivity.EXTRA_USER_NAME, user.name)
            startActivity(intent)
        }

        muatUsers()
    }

    private fun muatUsers() {
        progress.visibility = View.VISIBLE
        btnUlang.visibility = View.GONE
        tvStatus.text = "Sebentar ya, sedang mengambil data..."

        lifecycleScope.launch {
            try {
                daftarUser = RetrofitClient.api.getUsers().map { DataIndonesia.ubahKeIndonesia(it) }
                val data = daftarUser.map {
                    mapOf(
                        "nama" to it.name,
                        "info" to "@${it.username}  •  ${it.email}"
                    )
                }
                listUser.adapter = SimpleAdapter(
                    this@MainActivity,
                    data,
                    R.layout.item_pengguna,
                    arrayOf("nama", "info"),
                    intArrayOf(R.id.tvNama, R.id.tvInfo)
                )
                tvStatus.text = "${daftarUser.size} orang ditemukan. Ketuk nama untuk melihat tulisannya."
            } catch (e: HttpException) {
                tvStatus.text = "Server sedang bermasalah (kode ${e.code()})"
                btnUlang.visibility = View.VISIBLE
            } catch (e: IOException) {
                tvStatus.text = "Internet tidak tersambung. Cek koneksi, lalu ketuk Muat Ulang."
                btnUlang.visibility = View.VISIBLE
            } finally {
                progress.visibility = View.GONE
            }
        }
    }
}