package com.aryalingga.jakartaairquality

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TipsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewsTips)

        var btnKembali : Button

        val tips = listOf(
            Tip("Kurangi menggunakan kendaraan bermotor", "Berkendara dapat meningkatkan polusi udara, karena kendaraan bermotor menggunakan bahan bakar fosil yang menghasilkan emisi gas. Contohnya, karbon dioksida (CO2), nitrogen oksida (NOx), dan partikulat. \n\n" +
                    "Sebagai solusi pencemaran udara, kamu bisa beralih ke transportasi ramah lingkungan. Misalnya, kendaraan listrik dan mendorong penggunaan transportasi umum, sepeda, atau berjalan kaki."),
            Tip("Matikan mesin mobil", "Mematikan mesin mobil saat sedang berhenti sesaat atau parkir, dapat menjadi solusi mengatasi pencemaran udara. Alasannya, saat mesin mati, tidak ada pembakaran bahan bakar yang terjadi. Hal itu mengurangi emisi polutan seperti karbon dioksida (CO2), nitrogen oksida (NOx), dan partikulat. "),
            Tip("Jangan membakar sampah", "Pembakaran sampah di udara terbuka menghasilkan emisi gas yang mengandung berbagai zat berbahaya. Proses pembakaran ini melepaskan polutan udara, seperti karbon monoksida (CO), senyawa organik volatil (VOCs), dan partikulat ke atmosfer. \n\n" +
                    "Dengan tidak membakar sampah, kamu sudah mengurangi kontribusi emisi polutan tersebut. Jadi sebaiknya pilih alternatif penanganan sampah yang lebih ramah lingkungan. Misalnya, daur ulang, komposting, atau pemrosesan sampah yang aman secara lingkungan"),
            Tip("Menanam dan merawat pohon", "Menanam dan merawat pohon melibatkan proses fotosintesis yang membantu mengurangi konsentrasi karbon dioksida (CO2) di udara. Selain itu, dedaunan pohon dapat menangkap partikel debu dan polutan lain dari udara. \n\n" +
                    "Pohon juga memainkan peran penting dalam menyediakan oksigen, menciptakan lingkungan yang lebih sehat, dan mengurangi pemanasan global."),
            Tip("Kurangi penggunaan energi di rumah", "Mengurangi penggunaan energi memang tidak secara langsung menjadi solusi mengatasi pencemaran udara. Namun, hal ini pada gilirannya mengurangi kebutuhan produksi energi dari sumber yang dapat menciptakan polusi udara. \n\n" +
                    "Beberapa peralatan rumahan yang menjadi sumber polusi udara, yaitu kompor gas, oven, mesin cuci, AC, dan peralatan rumah tangga lain yang membutuhkan listrik agar bisa menjalankan fungsinya.")
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TipsAdapter(tips)

        btnKembali = findViewById(R.id.btnKembali)
        btnKembali.setOnClickListener {
            finish()
        }
    }

}
