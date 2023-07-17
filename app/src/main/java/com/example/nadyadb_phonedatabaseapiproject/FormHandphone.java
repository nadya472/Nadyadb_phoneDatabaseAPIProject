package com.example.nadyadb_phonedatabaseapiproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.example.nadyadb_phonedatabaseapiproject.model.handphone;
import com.example.nadyadb_phonedatabaseapiproject.server.AsyncInvokeURLTask;
import com.example.nadyadb_phonedatabaseapiproject.MainActivity;
import android.util.Pair;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.jar.Attributes;

public class FormHandphone extends AppCompatActivity {
    private EditText textNama, textHarga;
    private handphone handphone;
    public static final String urlSubmit = "submit_phone.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_handphone);
        initView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        handphone = new handphone();
        if(getIntent().hasExtra("id")){
            String id = getIntent().getStringExtra("id");
            String nama = getIntent().getStringExtra("nama");
            String harga = getIntent().getStringExtra("harga");
            textNama.setText(nama);
            textHarga.setText(harga);
            handphone.setId(Integer.valueOf(id));
        }else{
            handphone.setId(0);
        }
    }
    private void initView(){
        textNama = (EditText) findViewById(R.id.add_new_nama);
        textHarga = (EditText) findViewById(R.id.add_new_harga);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_handphone, menu);
        return true;
    }
    private void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                goToMainActivity();
                break;
            case R.id.option_menu_save:
                if(textHarga.getText().toString().trim().isEmpty() ||
                        textNama.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Nama dan Harga tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else{
                    sendData();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void sendData() {
        try {
            String nama = textNama.getText().toString();
            String harga =
                    URLEncoder.encode(textHarga.getText().toString(), "utf-8");
            ArrayList<Pair> nameValuePairs = new
                    ArrayList<Pair>(2);
            nameValuePairs.add(new Pair("nama", nama));
            nameValuePairs.add(new Pair("harga", harga));
            nameValuePairs.add(new Pair("id",String.valueOf(handphone.getId())));
            AsyncInvokeURLTask task = new AsyncInvokeURLTask(new Pair ("nama", nama),
                    new AsyncInvokeURLTask.OnPostExecuteListener() {
                        @Override
                        public void onPostExecute(String result) {
                            // TODO Auto-generated method stub
                            Log.d("TAG", "savedata:" + result);
                            if (result.equals("timeout") ||
                                    result.trim().equalsIgnoreCase("Tidak dapat Terkoneksi ke Data Base")){
                        }else{
                            goToMainActivity();
                        }
                    }
        });
        task.showdialog=true;
        task.message="Proses Submit Data Harap Tunggu..";
        task.applicationContext =FormHandphone.this;
        task.mNoteItWebUrl = urlSubmit;
        task.execute();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
 }

