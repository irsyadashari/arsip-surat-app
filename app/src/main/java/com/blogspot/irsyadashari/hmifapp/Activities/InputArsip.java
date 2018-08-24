package com.blogspot.irsyadashari.hmifapp.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.irsyadashari.hmifapp.R;
import com.blogspot.irsyadashari.hmifapp.model.Surat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InputArsip extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Button setDateBtn;
    private Button browseFileBtn;
    private Button saveFileBtn;
    private TextView keteranganTambahanTextView,perihalSuratTextView,jenisSuratValueText;
    public TextView displayDate;
    private TextView fileName,fileSize;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Spinner spinnerJenisSurat;

    private StorageReference mStorage;
    private DatabaseReference mPostDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog mProgress;
    private Uri mDocumentUri;

    private String displayName ;
    private String fileSizeValue ;

    private static final int REQUEST_CODE = 43; // sembarangji kodenya


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_arsip);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();

        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("MSurat");

        //value dari tanggal sekaligus display
        displayDate = findViewById(R.id.displayDate);

        //file ref
        fileName = findViewById(R.id.fileName);
        fileSize = findViewById(R.id.fileSize);

        //buttons ref
        browseFileBtn = findViewById(R.id.browseFileBtn);
        saveFileBtn = findViewById(R.id.saveFileBtn);
        setDateBtn = findViewById(R.id.datePickerBtn);

        //jenis surat ref
        jenisSuratValueText = findViewById(R.id.jenisSuratValueText);

        //keterangan tambahan
        keteranganTambahanTextView = findViewById(R.id.keteranganTambahanTextView);

        //perihal surat
        perihalSuratTextView = findViewById(R.id.perihalSuratTextView);

        //setOnclickListener on the BrowseFileBtn
        browseFileBtn.setOnClickListener(this);

        //setOnclickListener on the SaveFileBtn
        saveFileBtn.setOnClickListener(this);

        //setOnclickListener on the datepicker button
        setDateBtn.setOnClickListener(this);

        //display date
        displayDate = findViewById(R.id.displayDate);

        //setting the default date with the current date when the activity start
        long currentDateTimeString = java.lang.System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");
        Date date = new Date(currentDateTimeString);
        displayDate.setText(sdf.format(date));


        //datePicker init
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override //override method when the date is set
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                int tahun = year-1900;

                long currentDateTimeString = java.lang.System.currentTimeMillis();
                Date currentDate = new Date(tahun,month,dayOfMonth);
                Date currentTime = new Date(currentDateTimeString);

                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                String pickedDate = sdf.format(currentDate)+" , "+sdf2.format(currentTime);
                displayDate.setText(pickedDate);
            }
        };

        //Spinner sebagai pemilih jenis surat
        spinnerJenisSurat = findViewById(R.id.jenisSuratSpinnerInputActivity);
        ArrayAdapter<CharSequence> adapterJenisSurat = ArrayAdapter.createFromResource(this,
                R.array.jenis_surat,
                android.R.layout.simple_spinner_item);
        adapterJenisSurat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisSurat.setAdapter(adapterJenisSurat);
        spinnerJenisSurat.setOnItemSelectedListener(this);

    }

    //Spinner Method 1
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //what to do when jenisSurat is selected
        jenisSuratValueText.setText(spinnerJenisSurat.getSelectedItem().toString());//setting text value spinner ke jenisSuratValueText
    }

    //Spinner Method 2
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //onClick button function
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.datePickerBtn:
                //popup datepicker & set the date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        InputArsip.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            break;

            case R.id.saveFileBtn:
                //save the data
                SaveDataSurat();
            break;

            case R.id.browseFileBtn:
                //browse the file
                StartFileSearch();
            break;
        }

    }

    private void SaveDataSurat() {

        if(mDocumentUri != null)
        {
            mProgress.setMessage("Menyimpan surat...");
            mProgress.show();

            final String jenisSurat = jenisSuratValueText.getText().toString();
            final String perihalSurat = perihalSuratTextView.getText().toString();
            final String tanggal = displayDate.getText().toString();
            final String keteranganTambahan = keteranganTambahanTextView.getText().toString();
            String filePicked = "file yang dipilih";

            try {

                if (!TextUtils.isEmpty(jenisSurat) && !TextUtils.isEmpty(perihalSurat)
                        && !TextUtils.isEmpty(tanggal)&& !TextUtils.isEmpty(keteranganTambahan))
                {
                    //start the uploading...
                    final StorageReference filepath = mStorage.child("MSurat_file").
                            child(mDocumentUri.getLastPathSegment());  //mDocumentUri.getLastPathSegment() == /folder/document.doc"

                    filepath.putFile(mDocumentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Uri downloadurl = taskSnapshot.getDownloadUrl();

                            DatabaseReference newPost = mPostDatabase.push();

                            Map<String, String> dataToSave = new HashMap<>();
                            dataToSave.put("jenisSurat", jenisSurat);
                            dataToSave.put("perihalSurat", perihalSurat);
                            dataToSave.put("tanggalSurat", tanggal);// use dataToSave.put("tanggalSurat", String.valueOf(java.lang.System.currentTimeMillis())); to get the time when the surat posted
                            dataToSave.put("keteranganSurat", keteranganTambahan);
                            dataToSave.put("userId", mUser.getUid());
                            dataToSave.put("displayName",displayName);
                            dataToSave.put("fileSizeValue",fileSizeValue);

                            try{
                                newPost.setValue(dataToSave); // save the data to firebase database
                                mProgress.dismiss();

                                startActivity(new Intent(InputArsip.this, LihatArsip.class));
                                finish();

                            }catch (Exception e){
                                Toast.makeText(InputArsip.this,
                                        "Failed to save data\n" +
                                                "Check Internet Connection",
                                        Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                            }
                        }
                    });
                }else{
                    // tell the user to fill the blank
                    Toast.makeText(InputArsip.this,"Fill the blank",Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(InputArsip.this,"Pick file surat first",Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void StartFileSearch()// metode untuk memilih file
    {
        Intent startSearchFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        startSearchFileIntent.setType("*/*"); // All File's Type
        startActivityForResult(startSearchFileIntent,REQUEST_CODE);
    }

    @Override // method dieksekusi setelah memilih file dan menyetor file dalam bentuk intent bernama data
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            if(data != null)
            {
                mDocumentUri = data.getData(); // getting the document uri
                String uriString = mDocumentUri.toString();
                File myFile = new File(uriString);
                String path = myFile.getAbsolutePath(); // to get the file's path. currently not used
                displayName = null;
                fileSizeValue = null;


                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getContentResolver().query(mDocumentUri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            fileSizeValue = cursor.getString(cursor.getColumnIndex(OpenableColumns.SIZE));
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    displayName = myFile.getName();
                    fileSizeValue = String.valueOf(myFile.length()) ;
                }
                fileName.setText(displayName);
                fileSize.setText(fileSizeValue +" bytes");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
