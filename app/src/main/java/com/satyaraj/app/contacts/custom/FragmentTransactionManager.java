package com.satyaraj.app.contacts.custom;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentTransactionManager {

    public static synchronized void addFragment(FragmentManager mFragmentManager,
                                                                         Fragment fragment,
                                                                         boolean popStack,
                                                                         int container) {
        if (!mFragmentManager.isDestroyed()) {
            if (popStack) {
                mFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.add(container, fragment);
            ft.commitAllowingStateLoss();
            ft.addToBackStack(null);
            mFragmentManager.executePendingTransactions();
        }
    }
}
