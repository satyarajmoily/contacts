package com.satyaraj.app.contacts.fragment.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.satyaraj.app.contacts.MainActivity;
import com.satyaraj.app.contacts.R;
import com.satyaraj.app.contacts.application.ContactApplication;
import com.satyaraj.app.contacts.base.BaseFragment;
import com.satyaraj.app.contacts.fragment.contactdetails.ContactDetailsFragment;
import com.satyaraj.app.contacts.fragment.contacts.di.ContactListComponent;
import com.satyaraj.app.contacts.fragment.contacts.di.ContactListModule;
import com.satyaraj.app.contacts.fragment.contacts.di.DaggerContactListComponent;
import com.satyaraj.app.contacts.prefs.SharedPrefsHelper;
import com.satyaraj.app.contacts.prefs.SharedPrefsKey;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsFragment extends BaseFragment<MainActivity> implements ContactContract.View, ClickListener{

    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;
    @BindView(R.id.new_contacts_recycler_view)
    public RecyclerView mNewContactsRecyclerView;
    @BindView(R.id.view)
    View mView;
    @BindView(R.id.new_contact_text)
    TextView newContactText;
    @BindView(R.id.progress_circular)
    ProgressBar progressBar;

    private SharedPrefsHelper mSharedPrefsHelper;

    @Inject
    ContactsPresenter mainPresenter;
    @Inject
    ContactsAdapter adapter;
    @Inject
    ContactsAdapter newAdapter;

    public ContactsFragment() {
    }

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachParent((MainActivity) getActivity());
        initViews(view);
    }

    private void initViews(View view) {
        ButterKnife.bind(this,view);

        progressBar.setVisibility(View.VISIBLE);
        mSharedPrefsHelper = ContactApplication.getSharedPrefsHelper();

        ContactListComponent contactListComponent = DaggerContactListComponent.builder()
                .contactListModule(new ContactListModule(this))
                .build();

        contactListComponent.injectContactFragment(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

        mNewContactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mNewContactsRecyclerView.setAdapter(newAdapter);
    }

    @Override
    public void displayContacts(List<Contact> contactList) {
        adapter.updateList(contactList);

        int count = mSharedPrefsHelper.get(SharedPrefsKey.PREF_KEY_CONTACTS_ADDED,0);
        if (count != 0) {
            newContactText.setVisibility(View.VISIBLE);
            mNewContactsRecyclerView.setVisibility(View.VISIBLE);
            mView.setVisibility(View.VISIBLE);
            mainPresenter.getNewContacts(contactList, count);
        }else {
            newContactText.setVisibility(View.GONE);
            mNewContactsRecyclerView.setVisibility(View.GONE);
            mView.setVisibility(View.GONE);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemClicked(Contact contact) {
        getParentActivity().switchFragment(ContactDetailsFragment.newInstance(contact), false);
    }

    @Override
    public void displayNewContacts(List<Contact> contactList){
        newAdapter.updateList(contactList);
        mSharedPrefsHelper.put(SharedPrefsKey.PREF_KEY_CONTACTS_ADDED, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainPresenter.getContacts();
    }
}
