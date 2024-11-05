package com.example.e_carefinal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddDisease extends AppCompatActivity {
    EditText disease, symptoms;
    Button Upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_disease);

        // Initialize UI elements
        disease = findViewById(R.id.diseaseId);
        symptoms = findViewById(R.id.symptomId);
        Upload = findViewById(R.id.bUploadD);

        // Set onClick listener for the upload button
        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String d = disease.getText().toString().trim();
                String s = symptoms.getText().toString().trim();

                // Validate inputs
                if (d.isEmpty()) {
                    disease.setError("Please fill the details");
                    return;
                } else if (s.isEmpty()) {
                    symptoms.setError("Please fill the details");
                    return;
                } else {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("DiseaseName", d);
                    hashMap.put("Symptoms", s);

                    // Upload data to Firebase
                    FirebaseDatabase.getInstance().getReference().child("DiseaseAndSymptoms").push().setValue(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddDisease.this, "Disease and Symptoms uploaded successfully", Toast.LENGTH_SHORT).show();
                                    disease.setText("");
                                    symptoms.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddDisease.this, "Error uploading data", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}
