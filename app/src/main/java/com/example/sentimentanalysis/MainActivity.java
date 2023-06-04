package com.example.sentimentanalysis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sentimentanalysis.ml.SentimentAnalysis;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.schema.TensorType;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.like  {
    private ImageButton b;
    private ImageButton hi;
    private   TextView tv;
    private   EditText et;
    private RecyclerView recyclerView;
    private RecyclerAdapter chatAdapter;
    private ArrayList<ChatMessage> messageList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_items);
        b= findViewById(R.id.imageButton);
        hi=findViewById(R.id.hi);
        //tv= findViewById(R.id.textView);
        et=findViewById(R.id.editTextTextPersonName);
        recyclerView = findViewById(R.id.Recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageList = new ArrayList<>();
        chatAdapter = new RecyclerAdapter(messageList);
        chatAdapter.getlike(this);
        recyclerView.setAdapter(chatAdapter);
        LinearLayout l =findViewById(R.id.contain);

        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMessage(new ChatMessage("Hello! Lets start analysis", ChatMessage.SENDER_LEFT));
                addMessage(new ChatMessage("Our Model accuracy:\n"+"Validation result for 100000\nfeatures\n" +
                        "null accuracy: 50.55%\n" +
                        "accuracy score: 81.83%\n" +
                        "model is 31.28% more accurate\nthan null accuracy\n" +
                        "train and test time: 309.48s", ChatMessage.SENDER_LEFT));
                l.setVisibility(View.GONE);
            }
        });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SentimentAnalysis model = SentimentAnalysis.newInstance(MainActivity.this);
                    String inputText = et.getText().toString();
                    addMessage(new ChatMessage(inputText, ChatMessage.SENDER_RIGHT));
                    // Converts the preprocessed input text into a numerical representation by hot encoding.
                    int vocabSize = 500;
                    int[] inputIds = new int[500];
                    for (int i = 0; i < inputIds.length; i++) {
                        if (i < inputText.length()) {
                            char c = inputText.charAt(i);
                            if (c < vocabSize) {
                                inputIds[i] = c;
                            } else {
                                inputIds[i] = 0;
                            }
                        }
                        else {
                            inputIds[i] = 0;
                        }
                    }


                    int bufferSize = inputIds.length *4; //bec data type is float32 (4 bytes).
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bufferSize);
                    byteBuffer.order(ByteOrder.nativeOrder());

                    // Loads the input data into the ByteBuffer object.
                    for (int i = 0; i < inputIds.length; i++) {
                        byteBuffer.putFloat((float) inputIds[i]);
                    }

                    // Runs model inference and gets result.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 500}, DataType.FLOAT32);
                    inputFeature0.loadBuffer(byteBuffer);
                    SentimentAnalysis.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    double outputText = outputFeature0.getFloatValue(0);

                     if (outputText >= 0.41){
                        addMessage(new ChatMessage("I think it's Positve", ChatMessage.SENDER_LEFT));
                        et.setText("");
                        l.setVisibility(View.GONE);
                    }
                    else {
                        addMessage(new ChatMessage("I think it's negative", ChatMessage.SENDER_LEFT));
                        et.setText("");
                        l.setVisibility(View.GONE);
                    }
                    model.close();
                } catch (IOException e) {

                }
            }
        });
    }
    private void addMessage(ChatMessage message  ) {
        messageList.add(message);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }

    @Override
    public void click(ChatMessage mess) {
        messageList.add(mess);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }
}
