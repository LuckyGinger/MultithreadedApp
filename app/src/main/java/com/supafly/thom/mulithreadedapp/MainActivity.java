package com.supafly.thom.mulithreadedapp;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileLockInterruptionException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private String filename = "numbers.txt";
    private FileOutputStream outputStream;
    private FileInputStream inputStream;
    // To view the data from the file
    private ListView myListView = null;
    // Will hold the file data
    private ArrayList<String> results = new ArrayList<>();
    // Not really sure what this does...
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void runCreate(View view) {
        BufferedWriter bufferWriter = null;
        try {
            // Create a file with the filename
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            bufferWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String numbers = null;
            for (int i = 1; i < 11; i++) {
                // convert an int to a string
                numbers = new Integer(i).toString();
                // write the numbers to the file
                bufferWriter.append(numbers);
                bufferWriter.newLine();
                // sleep 250 milliseconds
                Thread.sleep(250);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void runLoad(View view) {
        BufferedReader bufferReader = null;
        try {
            // Open the file
            inputStream = openFileInput(filename);
            bufferReader = new BufferedReader(new InputStreamReader(inputStream));

            // Read line by line
            String line;
            while ((line = bufferReader.readLine()) != null) {
                results.add(line);
                // Sleep 250 milliseconds
                Thread.sleep(250);
            }
            // Send file information to the ListView
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    results);
            myListView = (ListView) this.findViewById(R.id.list);
            myListView.setAdapter(adapter);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void runClear(View view) {
        adapter.clear();
        myListView.setAdapter(adapter);
    }


}
