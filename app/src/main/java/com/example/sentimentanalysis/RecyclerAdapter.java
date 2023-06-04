package com.example.sentimentanalysis;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    interface like{
        void click (ChatMessage mess);
    }
    like love;
    void getlike(like like) {
        love=like;

    }
    private ArrayList<ChatMessage> messageList;
    public RecyclerAdapter(ArrayList<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mess_adapter, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textLeft;
        private TextView textRight;
        private ImageView img;
        private ImageButton like;
        private RelativeLayout send;
        private RelativeLayout rec;
        private TextView textViewTime;
        private TextView textViewTime2;
        private RecyclerView recyclerView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textLeft = itemView.findViewById(R.id.response);
            textRight = itemView.findViewById(R.id.sender);
            img =itemView.findViewById(R.id.imageView);
            send= itemView.findViewById(R.id.send);
            rec= itemView.findViewById(R.id.rec);
            textViewTime = itemView.findViewById(R.id.time1);
            textViewTime2 = itemView.findViewById(R.id.time2);
            like= itemView.findViewById(R.id.imageButton2);
            recyclerView = itemView.findViewById(R.id.Recycler);


        }

        public void bind(ChatMessage message) {
            final boolean[] Like = {false};
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Like[0]) {
                        // If already liked, set the background drawable to the first drawable
                        like.setBackgroundResource(R.drawable.unchecked);

                        Like[0] = false;
                    } else {
                        // If not liked, set the background drawable to the second drawable
                        like.setBackgroundResource(R.drawable.checked);

                        love.click(new ChatMessage("Glade that u liked the analysis", ChatMessage.SENDER_LEFT));

                        Like[0] = true;

                    }
                }




            });

            if (message.getSender() == ChatMessage.SENDER_LEFT) {
                // Set the message text on the left TextView
                textLeft.setVisibility(View.VISIBLE);
                send.setVisibility(View.GONE);
                textLeft.setText(message.getText());
            }
            else if (message.getSender() == ChatMessage.SENDER_RIGHT) {
                // Set the message text on the right TextView
                rec.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
                like.setVisibility(View.GONE);
                textRight.setVisibility(View.VISIBLE);
                textRight.setText(message.getText());

            }

            String currentTime = getCurrentTime();

            // Set the current time to the TextView
            textViewTime.setText(currentTime);
            textViewTime2.setText(currentTime);

        }
        private String getCurrentTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            return sdf.format(new Date());
        }
    }

}

