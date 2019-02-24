package com.satyaraj.app.contacts.fragment.contactdetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.satyaraj.app.contacts.MainActivity;
import com.satyaraj.app.contacts.R;
import com.satyaraj.app.contacts.base.BaseFragment;
import com.satyaraj.app.contacts.custom.CircleImageTransform;
import com.satyaraj.app.contacts.fragment.contacts.Contact;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactDetailsFragment extends BaseFragment<MainActivity>{

    private static String CONTACT_DETAILS = "contact_details";

    @BindView(R.id.contact_name)
    TextView contactName;
    @BindView(R.id.contact_image)
    ImageView contactImage;
    @BindView(R.id.text_phone)
    TextView phoneNumber;
    @BindView(R.id.img_msg)
    ImageView messageButton;
    @BindView(R.id.img_call)
    ImageView callButton;

    private String mPhoneNumber;

    public static ContactDetailsFragment newInstance(Contact contact) {
        ContactDetailsFragment contactDetailsFragment = new ContactDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CONTACT_DETAILS, contact);
        contactDetailsFragment.setArguments(bundle);
        return contactDetailsFragment;
    }

    public ContactDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachParent(getParentActivity());
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            Contact contact = (Contact) getArguments().getSerializable(CONTACT_DETAILS);
            if (contact != null) {
                initViews(contact);
            }
        }
    }

    private void initViews(Contact contact) {
        contactName.setText(contact.getName());

        Picasso.with(getContext()).load(contact.getPicUrl())
                .placeholder(R.drawable.ic_person)
                .fit()
                .transform(new CircleImageTransform())
                .into(contactImage);

        if (contact.getHasPhoneNumber() > 0) {
            phoneNumber.setText(contact.getPhoneNumber());
            mPhoneNumber = contact.getPhoneNumber();
            messageButton.setVisibility(View.VISIBLE);
            callButton.setVisibility(View.VISIBLE);
        }else{
            messageButton.setVisibility(View.GONE);
            callButton.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.img_call)
    public void onCallClicked(){
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:"+ mPhoneNumber));
        startActivity(dialIntent);

    }
    @OnClick(R.id.img_msg)
    public void onMessageClicked(){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"+ mPhoneNumber));
        startActivity(sendIntent);
    }

    @OnClick(R.id.root_layout)
    public void onRootClicked(){}
}
