package com.example.nadyadb_phonedatabaseapiproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import com.example.nadyadb_phonedatabaseapiproject.R;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.example.nadyadb_phonedatabaseapiproject.model.handphone;
import com.example.nadyadb_phonedatabaseapiproject.server.AsyncInvokeURLTask;
import com.example.nadyadb_phonedatabaseapiproject.FormHandphone;


import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;


public class DetailHandphone extends AppCompatActivity {
    public static final String urlDelete = "delete_phone.php";
    private EditText textNama, textHarga;
    private handphone handphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_handphone);
        handphone = new handphone();
        initView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void initView(){
        textNama = (EditText) findViewById(R.id.add_new_nama);
        textHarga = (EditText) findViewById(R.id.add_new_harga);
        String id = getIntent().getStringExtra("id");
        String nama = getIntent().getStringExtra("nama");
        String harga = getIntent().getStringExtra("harga");
        textNama.setText(nama);
        textHarga.setText(harga);
        handphone.setId(Integer.valueOf(id));
        handphone.setNama(nama);
        handphone.setHarga(harga);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_action, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                goToMainActivity();
                break;
            case R.id.action_menu_edit:
                Intent in = new Intent(getApplicationContext(), FormHandphone.class);
                in.putExtra("id", handphone.getId().toString());
                in.putExtra("nama", handphone.getNama());
                in.putExtra("harga", handphone.getHarga());
                startActivity(in);
                break;
            case R.id.action_menu_delete:
                delete();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void goToMainActivity(){
        Intent in = new Intent(getApplicationContext(),
                MainActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
    }
    private void delete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete "+handphone.getNama()+" ?");
        builder.setTitle("Delete");
        builder.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData();
                        Toast.makeText(getApplicationContext(), "deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setIcon(android.R.drawable.ic_menu_delete);
        alert.show();
    }
    public void deleteData() {
        try {
            ArrayList<Pair<String, String>> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new Pair<>("id", String.valueOf(handphone.getId())));
            AsyncInvokeURLTask task = new AsyncInvokeURLTask(nameValuePairs,
                    new AsyncInvokeURLTask.OnPostExecuteListener() {
                        @Override
                        public void onPostExecute(String result) {
                            Log.d("TAG", "Delete :" + result);
                            if (result.equals("timeout") ||
                                    result.trim().equalsIgnoreCase("Tidak dapat Terkoneksi ke Data Base")) {
                                Toast.makeText(getBaseContext(), "Tidak Dapat Terkoneksi dengn Server", Toast.LENGTH_SHORT).show();
                            } else {
                                goToMainActivity();
                            }
                        }
                    });
            task.showdialog = true;
            task.message = "Proses Delete Data Harap Tunggu..";
            task.applicationContext = DetailHandphone.this;
            task.mNoteItWebUrl = urlDelete;
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}