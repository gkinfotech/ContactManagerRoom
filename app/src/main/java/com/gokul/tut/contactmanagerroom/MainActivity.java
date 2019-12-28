package com.gokul.tut.contactmanagerroom;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import com.gokul.tut.contactmanagerroom.adapter.ContactsAdapter;
import com.gokul.tut.contactmanagerroom.db.entity.Contact;
import com.gokul.tut.contactmanagerroom.db.entity.ContactAppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ContactsAdapter contactsAdapter;
    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactAppDatabase contactAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_view_contacts);
        contactAppDatabase = Room.databaseBuilder(getApplicationContext(),ContactAppDatabase.class,"ContactDB").build();

        new GetAllContactAsyncTask().execute();


        contactsAdapter = new ContactsAdapter(this,contactArrayList,MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactsAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndEditContacts(false, null, -1);
            }
        });
    }

    public void addAndEditContacts(final boolean isUpdate,final Contact contact,final int position){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.layout_add_contact,null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        TextView contactTitle = view.findViewById(R.id.new_contact_title);
        final EditText newContact = view.findViewById(R.id.name);
        final EditText contactEmail = view.findViewById(R.id.email);

        contactTitle.setText(!isUpdate?"Add New Contact": "Edit Contact");

        if (isUpdate && contact!=null){
            newContact.setText(contact.getName());
            newContact.setText(contact.getEmail());
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(isUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isUpdate){
                            deleteContact(contact,position);
                        }else {
                            dialog.cancel();
                        }
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(newContact.getText().toString())){
                    Toast.makeText(MainActivity.this, "Enter the Contact Name", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    alertDialog.dismiss();
                }

                if (isUpdate && contact!=null){
                    updateContact(newContact.getText().toString(),contactEmail.getText().toString(),position);
                }else {
                    createContact(newContact.getText().toString(),contactEmail.getText().toString());
                }
            }
        });
    }

    private void deleteContact(Contact contact,int position){
        contactArrayList.remove(position);
        new DeleteAsyncTask().execute(contact);

    }

    private void updateContact(String name,String email,int position){
        Contact contact = contactArrayList.get(position);
        contact.setName(name);
        contact.setEmail(email);

        new UpdateAsyncTask().execute(contact);
        contactArrayList.set(position, contact);

    }

    private void createContact(String name,String email){

        new CreateContactAsyncTask().execute(new Contact(0,name,email));

    }

    private class GetAllContactAsyncTask extends AsyncTask<Void ,Void ,Void >{

        @Override
        protected Void doInBackground(Void... voids) {
            contactArrayList.addAll(contactAppDatabase.getContactDAO().getContact());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            contactsAdapter.notifyDataSetChanged();
        }
    }

    private class CreateContactAsyncTask extends AsyncTask<Contact,Void,Void>{

        @Override
        protected Void doInBackground(Contact... contacts) {
            long id = contactAppDatabase.getContactDAO().addContact(contacts[0]);

            Contact contact = contactAppDatabase.getContactDAO().getContact(id);
            if (contact!=null){
                contactArrayList.add(0,contact);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            contactsAdapter.notifyDataSetChanged();
        }
    }

    private class UpdateAsyncTask extends AsyncTask<Contact,Void,Void>{

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactAppDatabase.getContactDAO().updateContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            contactsAdapter.notifyDataSetChanged();
        }
    }

    private class DeleteAsyncTask extends AsyncTask<Contact,Void,Void>{

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactAppDatabase.getContactDAO().deleteContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            contactsAdapter.notifyDataSetChanged();
        }
    }


}
