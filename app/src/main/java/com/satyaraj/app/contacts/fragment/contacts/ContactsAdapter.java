package com.satyaraj.app.contacts.fragment.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.satyaraj.app.contacts.R;
import com.satyaraj.app.contacts.custom.CircleImageTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Contact> mContactList = new ArrayList<>(0);

    ClickListener mClickListener;
    Context mContext;

    @Inject
    ContactsAdapter (ContactsFragment contactsFragment){
        this.mClickListener = contactsFragment;
        this.mContext = contactsFragment.getContext();
    }

    void updateList(List<Contact> messageList){
        mContactList.clear();
        mContactList.addAll(messageList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_contact, parent, false);
            viewHolder = new ViewHeader(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Contact contact = mContactList.get(position);
        ((ViewHeader) holder).onBind(contact);
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    private class ViewHeader extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        ImageView imageView;
        Contact contact;

        ViewHeader(View view) {
            super(view);
            textView = view.findViewById(R.id.contact_name);
            imageView = view.findViewById(R.id.imageView);
            view.setOnClickListener(this);
        }

        void onBind(Contact contact){
            this.contact = contact;
            textView.setText(contact.getName());
            Picasso.with(mContext).load(contact.getPicUrl())
                    .placeholder(R.drawable.ic_person)
                    .fit()
                    .transform(new CircleImageTransform())
                    .into(imageView);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClicked(contact);
        }
    }
}
