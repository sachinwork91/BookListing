package com.udacity.submissions.sachin.booklisting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button searchResultsButton;
    EditText searchQueryetv;
    String query = "";
    private static final String BASEURL = "https://www.googleapis.com/books/v1/volumes?maxResults=10&q=";
    ListView results_list_view;
    ArrayList<BookDetails> bookDetails = new ArrayList<>();

    BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchResultsButton = (Button) findViewById(R.id.searchButton);
        searchQueryetv = (EditText) findViewById(R.id.searchqueryid);
        results_list_view = (ListView) findViewById(R.id.results_list_view);
        results_list_view.setEmptyView(findViewById(R.id.emptyElement));

        results_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BookDetails currentBook =  bookDetails.get(i);
                Intent intent = new Intent(MainActivity.this, DetailedDescription.class);
                intent.putExtra("currentBook", currentBook);
                startActivity(intent);
            }
        });



        //IF the Orientation of the phone is changed
        if (savedInstanceState != null) {

            bookDetails = savedInstanceState.getParcelableArrayList(String.valueOf(R.string.parcalableKeyForBookDetails));
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        bookAdapter = new BookAdapter(this, bookDetails);
        results_list_view.setAdapter(bookAdapter);

        searchResultsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                query = searchQueryetv.getText().toString();



                if (query == null || TextUtils.isEmpty(query)) {
                    Toast.makeText(MainActivity.this, R.string.emptyQueryErrorToast, Toast.LENGTH_SHORT).show();
                }else if (!checkInternetConnection()){

                    Toast.makeText(MainActivity.this, "Internet Connection is not Available. Please Check", Toast.LENGTH_SHORT).show();
                }else {

                    BooksAsyncTask booksAsyncTask = new BooksAsyncTask();
                    URL url = null;
                    try {
                        String finalUrl = BASEURL + query;
                        url = new URL(finalUrl);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    booksAsyncTask.execute(url);

                }
            }
        });

    }

    //Inner Class for Async Network Requests
    private class BooksAsyncTask extends AsyncTask<URL, Void, ArrayList<BookDetails>> {


        @Override
        protected ArrayList<BookDetails> doInBackground(URL... urls) {
            ArrayList<BookDetails> bookDetails = new ArrayList<>();

            try {
                String results = makeHttpRequest(urls[0]);
                bookDetails = parseJSONtoBookDetails(results);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bookDetails;
        }


        @Override
        protected void onPostExecute(ArrayList<BookDetails> bookDetails) {
            super.onPostExecute(bookDetails);

            ArrayList<String> resdata = new ArrayList<>();
            ArrayList<BookDetails> resultBookDetails = new ArrayList<>(bookDetails);


            for (BookDetails book : bookDetails) {
                resdata.add(book.toString());
            }

            updateUI(resultBookDetails);
        }
    }


    //This method is used to update the UI. This happens on the main thread after fetching the data
    private void updateUI(ArrayList<BookDetails> testData) {
        bookAdapter.clear();
        if(testData==null || testData.size()==0 ){
            Toast.makeText(MainActivity.this, "No Results Found", Toast.LENGTH_SHORT).show();
        }
        bookAdapter.addAll(testData);
        bookAdapter.notifyDataSetChanged();
    }


    //This method is used to parse the JSON response into required BookDetails Structure
    private ArrayList<BookDetails> parseJSONtoBookDetails(String jsonResponse) {
        ArrayList<BookDetails> listOfBooks = new ArrayList<BookDetails>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

            //if the returned response has items
            if (itemsArray.length() > 0) {

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonDetails = itemsArray.getJSONObject(i);
                    JSONObject volumeInfo = jsonDetails.getJSONObject("volumeInfo");
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                    String title = volumeInfo.getString("title");
                    String description = volumeInfo.getString("description");
                    String averageRating="Not Available";
                    try {
                         averageRating = volumeInfo.getString("averageRating");
                    }catch(JSONException e){

                    }

                    String imageUrl =   imageLinks.getString("smallThumbnail");

                    JSONArray authorsJSONarray = volumeInfo.getJSONArray("authors");
                    String authorslist = getAuthorsString(authorsJSONarray);
                    listOfBooks.add(new BookDetails(title, authorslist, imageUrl, 5 ,description, averageRating));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listOfBooks;

    }


    //return the Comma separated array of Authors
    String getAuthorsString(JSONArray authorsJSONarray) {
        String allAuthors = "";

        for (int i = 0; i < authorsJSONarray.length(); i++) {
            try {
                allAuthors = authorsJSONarray.getString(i) + ",";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return allAuthors.substring(0, allAuthors.length() - 1);
    }


    // This method makes the HTTP Request to get the details of the books
    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(String.valueOf(R.string.parcalableKeyForBookDetails), bookDetails);
    }


    boolean checkInternetConnection(){

        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();


    }
}
