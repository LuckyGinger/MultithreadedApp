package com.supafly.thom.mulithreadedapp;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Multithreading App
 *
 * <p>Immplements the workings of threads in an android app
 *
 * @author Thom Allen
 */
public class MainActivity extends ActionBarActivity {
    private static Context context;
    private String filename = "numbers.txt";
    private FileOutputStream outputStream;
    private FileInputStream inputStream;
    // To view the data from the file
    private ListView myListView = null;
    // Will hold the file data
    private List<String> results = new ArrayList<>();
    // Not really sure what this does...
    private ArrayAdapter<String> adapter = null;
    private ProgressBar progress = null;
    private int progressBarStatus1;
    private int progressBarStatus2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = MainActivity.this.getApplicationContext();
        progress = (ProgressBar) findViewById(R.id.progressBar);
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

    /**
     * Creates a file when the Create button is pressed
     *
     * @param view
     */
    public void runCreate(View view) {
        progressBarStatus1 = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedWriter bufferWriter = null;
                try {
                    // Create a file with the filename
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    bufferWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                    String numbers = null;
                    for (int i = 1; i < 11; i++) {
                        // write the numbers to the file
                        bufferWriter.append(String.valueOf(i));
                        bufferWriter.newLine();
                        // sleep 250 milliseconds
                        Thread.sleep(250);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (progressBarStatus1 < 100) {
                                    progressBarStatus1 += 10;
                                }
                                progress.setProgress(progressBarStatus1);
                            }
                        });
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
        }).start();
    }

    /**
     * Loads in the file created in runCreat() and displays the text in a listView.
     *
     * @param view
     */
    public void runLoad(View view) {
        progressBarStatus2 = 0;
        new Thread(new Runnable() {
            public void run() {
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
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (progressBarStatus2 < 100) {
                                    progressBarStatus2 += 10;
                                }
                                progress.setProgress(progressBarStatus2);
                            }
                        });
                    }
                    // Send file information to the ListView
                    adapter = new ArrayAdapter<String>(context, R.layout.list_view,
                            results);
                    myListView = (ListView) findViewById(R.id.list);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            myListView.setAdapter(adapter);
                        }
                    });

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
        }).start();
    }

    /**
     * Checks if there is no file loaded. If there is a file loaded, it will clear it.
     *
     * @param view
     */
    public void runClear(View view) {
        if (adapter != null) {
            adapter.clear();
            myListView.setAdapter(adapter);
            progress.setProgress(0);
            String msg = "Load cleared!";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            String errorMsg = "Nothing to clear";
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }


}
