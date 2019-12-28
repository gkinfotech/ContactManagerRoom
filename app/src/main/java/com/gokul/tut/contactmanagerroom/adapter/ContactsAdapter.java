package com.gokul.tut.contactmanagerroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gokul.tut.contactmanagerroom.MainActivity;
import com.gokul.tut.contactmanagerroom.R;
import com.gokul.tut.contactmanagerroom.db.entity.Contact;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Contact> contactsList;
    private MainActivity mainActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView emil;
        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            emil = view.findViewById(R.id.email);

        }
    }

    public ContactsAdapter(Context context, ArrayList<Contact> contacts, MainActivity mainActivity) {
        this.context = context;
        this.contactsList = contacts;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ContactsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.MyViewHolder holder, final int position) {

        final Contact contact = contactsList.get(position);
        holder.name.setText(contact.getName());
        holder.emil.setText(contact.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.addAndEditContacts(true,contact,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }


}
