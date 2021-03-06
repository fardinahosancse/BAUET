package com.bauet.bauet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;

public class booksViewer extends AppCompatActivity {

    final  String books_01_path ="-MU--vDJWpKl3WyKyhYs";
    final private  String books_02_path ="-MU2KoRCwZjx6mFeMycR";
    final private  String books_03_path ="-MU2Kxf00ZArYEktyCB8";


    private PDFView mPDFView;
    private TextView codV;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference =mDatabase.getReference("PDF ANSWER SHEET");
    DatabaseReference mChild =  mDatabaseReference.child("-MU--vDJWpKl3WyKyhYs");
    DatabaseReference urlRead = mChild.child("url");
    ProgressDialog progressDialog;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_viewer);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mPDFView =(PDFView)findViewById(R.id.pdfView);
        codV = (TextView) findViewById(R.id.textP);

        urlRead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                progressDialog = new ProgressDialog(booksViewer.this);
                progressDialog.setTitle("Downloading");
                progressDialog.setMessage("Core Java by Cay S. Horstmann and Gary Cornell");
                progressDialog.closeOptionsMenu();
                progressDialog.show();


                String value = snapshot.getValue(String.class);
                codV.setText(value);
                Toast.makeText(booksViewer.this, "UPDATED", Toast.LENGTH_SHORT).show();
                String url = codV.getText().toString();
                new booksViewer.RetrivePdfStream().execute(url);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(booksViewer.this, "FAILED TO LOAD", Toast.LENGTH_SHORT).show();

            }
        });}

    class RetrivePdfStream extends AsyncTask<String,Void, InputStream>
    {

        @Override
        protected InputStream doInBackground(String... strings) {


            InputStream inputStream = null;
            try
            {
                URL uRl =  new URL(strings[0]);
                HttpURLConnection urlConnection  = (HttpURLConnection)uRl.openConnection();
                if(urlConnection.getResponseCode()==200)
                {


                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }
            catch (IOException e)
            {
                progressDialog.dismiss();
                return  null;
            }

            return inputStream;
        }



        @Override
        protected void onPostExecute(InputStream inputStream) {

            mPDFView.fromStream(inputStream).load();


        }
    }
}
