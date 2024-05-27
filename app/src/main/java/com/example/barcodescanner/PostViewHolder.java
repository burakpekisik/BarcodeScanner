package com.example.barcodescanner;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class PostViewHolder extends RecyclerView.ViewHolder {
    private TextView orderId_text;
    private TextView letter_name_text;
    private TextView customerId_text;
    private TextView date_for_transport_text;
    private TextView order_price_text;
    private TextView order_date_text;
    private TextView phone_number_text;
    private TextView customer_name_text;
    private TextView email_text;
    private TextView track_text;

    public PostViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
        super(itemView);

        orderId_text = itemView.findViewById(R.id.orderId);
        letter_name_text = itemView.findViewById(R.id.letter_name);
        customerId_text = itemView.findViewById(R.id.customerId);
        date_for_transport_text = itemView.findViewById(R.id.date_for_transport);
        order_price_text = itemView.findViewById(R.id.order_price);
        order_date_text = itemView.findViewById(R.id.order_date);
        phone_number_text = itemView.findViewById(R.id.phone_number);
        customer_name_text = itemView.findViewById(R.id.customer_name);
        email_text = itemView.findViewById(R.id.email);
        track_text = itemView.findViewById(R.id.track_id);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewInterface != null) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemOnClick(pos);
                    }
                }
            }
        });
    }

    public void bindView(PostModel postModel) {
        orderId_text.setText("Sipariş ID: " + String.valueOf(postModel.getID()));
        letter_name_text.setText("Mektup Adı: " + String.valueOf(postModel.getLetterName()));
        customerId_text.setText("Müşteri ID: " + String.valueOf(postModel.getCustomerId()));
        date_for_transport_text.setText("Postaya Verilme Tarihi: " + String.valueOf(postModel.getDateForTransport()));
        order_price_text.setText("Sipariş Tutarı: " + String.valueOf(postModel.getOrderPrice()));
        order_date_text.setText("Sipariş Tarihi: " + String.valueOf(postModel.getOrderDate()));
        phone_number_text.setText("Telefon Numarası: " + String.valueOf(postModel.getPhoneNumber()));
        customer_name_text.setText("Müşteri Adı: " + String.valueOf(postModel.getCustomerName()));
        email_text.setText("Email: " + String.valueOf(postModel.getEmail()));
        if (postModel.getTrackId() != null) {
            track_text.setVisibility(View.VISIBLE);
            track_text.setText("Track ID: " + String.valueOf(postModel.getTrackId()));
        } else {
            track_text.setVisibility(View.GONE);
        }
    }
}
