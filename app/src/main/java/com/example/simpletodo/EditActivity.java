package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    // Save Variable names
    EditText edtItem;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edtItem = findViewById(R.id.edtItem);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Editing Selected Item");
        edtItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1: Create an Intent to Contain Results
                Intent intent = new Intent();

                // 2: Pass Data (Results from Editing)
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, edtItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                // 3 Set Result of Intent
                setResult(RESULT_OK, intent);

                // 4: Finish Activity, Close Screen, and Go Back to Main Activity
                finish();
            }
        });
    }
}