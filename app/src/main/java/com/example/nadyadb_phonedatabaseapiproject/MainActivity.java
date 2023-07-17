package com.example.nadyadb_phonedatabaseapiproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
private lateinit var listView: ListView
private var actionMode: ActionMode? = null
private lateinit var amCallback: ActionMode.Callback
private var listhp: MutableList<Handphone> = ArrayList()
private lateinit var adapter: ListAdapterHandphone
private lateinit var selectedList: Handphone

        companion object {
private const val TAG = "MainActivity"
        }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listview_main)

        amCallback = object : ActionMode.Callback {
        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.activity_main_action, menu)
        return true
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when (item?.itemId) {
        R.id.action_menu_edit -> showUpdateForm()
        R.id.action_menu_delete -> delete()
        }
        mode?.finish()
        return false
        }
        }

        loadDataHP()
        }

private fun showUpdateForm() {
        val intent = Intent(applicationContext, FormHandphone::class.java)
        intent.putExtra("id", selectedList.id.toString())
        intent.putExtra("nama", selectedList.nama)
        intent.putExtra("harga", selectedList.harga)
        startActivity(intent)
        }

private fun delete() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete ${selectedList.nama} ?")
        builder.setTitle("Delete")
        builder.setPositiveButton("Yes") { dialog, _ ->
        listhp.remove(selectedList)
        Toast.makeText(applicationContext, "Deleted", Toast.LENGTH_SHORT).show()
        dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
        dialog.cancel()
        }
        val alert = builder.create()
        alert.setIcon(android.R.drawable.ic_menu_delete)
        alert.show()
        }

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.option_menu_search)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        if (searchView != null) {
        MenuItemCompat.setShowAsAction(
        searchItem,
        MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_ALWAYS
        )
        val id =
        searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val textView = searchView.findViewById<TextView>(id)
        textView.setTextColor(Color.WHITE)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false)
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = "nama"
        }
        return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        R.id.option_menu_new -> {
        val intent = Intent(applicationContext, FormHandphone::class.java)
        startActivity(intent)
        }
        }
        return super.onOptionsItemSelected(item)
        }

private fun processResponse(response: String) {
        try {
        val jsonObj = JSONObject(response)
        val jsonArray = jsonObj.getJSONArray("handphone")
        Log.d(TAG, "data length: ${jsonArray.length()}")
        var handphone: Handphone?
        for (i in 0 until jsonArray.length()) {
        val obj = jsonArray.getJSONObject(i)
        handphone = Handphone()
        handphone.id = obj.getInt("id")
        handphone.nama = obj.getString("nama")
        handphone.harga = obj.getString("harga")
        listhp.add(handphone)
        }
        } catch (e: JSONException) {
        Log.d(TAG, e.message)
        }
        }

private fun populateListView() {
        adapter = ListAdapterHandphone(applicationContext, listhp)
        listView.adapter = adapter

        listView.setOnItemLongClickListener { _, v, pos, _ ->
        if (actionMode != null) {
        return@setOnItemLongClickListener false
        }
        actionMode = startActionMode(amCallback)
        v.isSelected = true
        selectedList = adapter.getItem(pos)
        true
        }

        listView.setOnItemClickListener { _, v, pos, _ ->
        selectedList = adapter.getItem(pos)
        val intent = Intent(applicationContext, DetailHandphone::class.java)
        intent.putExtra("id", selectedList.id.toString())
        intent.putExtra("nama", selectedList.nama)
        intent.putExtra("harga", selectedList.harga)
        startActivity(intent)
        }
        }

private fun loadDataHP() {
        try {
        val nameValuePairs = ArrayList<Pair<String, String>>()
        val task = AsyncInvokeURLTask(nameValuePairs,
        object : AsyncInvokeURLTask.OnPostExecuteListener {
        override fun onPostExecute(result: String) {
        Log.d("TAG", "Login: $result")
        if (result.equals("timeout", ignoreCase = true) ||
        result.trim().equals("Tidak dapat Terkoneksi ke Data Base", ignoreCase = true)
        ) {
        Toast.makeText(
        baseContext,
        "Tidak Dapat Terkoneksi dengan Server",
        Toast.LENGTH_SHORT
        ).show()
        } else {
        processResponse(result)
        populateListView()
        }
        }
        })
        task.showdialog = true
        task.message = "Load Data HP Please Wait..."
        task.applicationContext = this@MainActivity
            task.mNoteItWebUrl = "list_phone.php"
                    task.execute()
                    } catch (e: Exception) {
                    e.printStackTrace()
                    }
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                    adapter.filter.filter(newText)
                    return true
                    }

                    override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                    }
                    }
