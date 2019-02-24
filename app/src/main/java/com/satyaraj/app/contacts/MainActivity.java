package com.satyaraj.app.contacts;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.satyaraj.app.contacts.base.BaseActivity;
import com.satyaraj.app.contacts.custom.FragmentTransactionManager;
import com.satyaraj.app.contacts.fragment.contacts.ContactsFragment;
import com.satyaraj.app.contacts.observer.ContactWatchService;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            requestContactsPermission();
        }
    }

    public void switchFragment(Fragment fragment, boolean shouldPop){
        FragmentTransactionManager.addFragment(getSupportFragmentManager(), fragment, shouldPop, R.id.container);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                switchFragment(ContactsFragment.newInstance(), false);
                Intent intent = new Intent(MainActivity.this, ContactWatchService.class);
                startService(intent);
            } else {
                Toast.makeText(MainActivity.this,"Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestContactsPermission() {
        String permission = Manifest.permission.READ_CONTACTS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }else {
            switchFragment(ContactsFragment.newInstance(), false);
            Intent intent = new Intent(MainActivity.this, ContactWatchService.class);
            startService(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
