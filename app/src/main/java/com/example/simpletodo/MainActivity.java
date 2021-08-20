package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;     // don't forget to import List

    Button btnAdd;          // identify the id from design (member variable)
    EditText editItem;
    RecyclerView recItems;
    ItemAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find the id of each member variable
        btnAdd = findViewById(R.id.btnAdd);
        editItem = findViewById(R.id.editItem);
        recItems = findViewById(R.id.recItems);

        loadItems();

        ItemAdapter.OnLongClickListener onLongClickListener = new ItemAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // 1: Deletes item from the model
                items.remove(position);
                // 2: Notify adapter when item is deleted
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was successfully removed.", Toast.LENGTH_SHORT).show();
                saveItems();    // save changes
            }
        };

        ItemAdapter.OnClickListener onClickListener= new ItemAdapter.OnClickListener() {
            @Override
            public void OnItemClicked(int position) {
                // 1: Check that a Single Click is Registered
                Log.d("MainActivity", "Single Click at Position " + position);

                // 2: Create a New Activity (create intent)
                Intent i = new Intent( MainActivity.this, EditActivity.class);

                // 3: Pass Data to Edit in New Activity
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);

                // 4: Display Activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        // construct an adapter with items
        itemsAdapter = new ItemAdapter(items, onLongClickListener, onClickListener);
        recItems.setAdapter(itemsAdapter);
        recItems.setLayoutManager(new LinearLayoutManager(this));

        // add logic to button click
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toDo = editItem.getText().toString();
                // 1: Add Item to Model
                items.add(toDo);

                // 2: Notify adapter when item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                editItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was successfully added.", Toast.LENGTH_SHORT).show();
                saveItems();    // save changes
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == EDIT_TEXT_CODE && resultCode == RESULT_OK) {
            // 1: Retrieve Updated Text Value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            // 2: Extract Original Position of Edited Text from Position Key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // 3: Update Model with New Item at the Specified Position
            items.set(position, itemText);

            // 4: Notify the Adapter
            itemsAdapter.notifyItemChanged(position);

            // 5: Persist the Changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown Call to onActivityResult");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // Load by Reading Through File
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("Main Activity", "Error While Reading Items", e);
            items = new ArrayList<>();
        }
    }

    // Save by Writing the File
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("Main Activity", "Error While Writing Items", e);
        }
    }
}