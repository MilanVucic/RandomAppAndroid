package com.example.vucic.randomapp.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.vucic.randomapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class LockScreenFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    List<CheckBox> checkBoxes = new ArrayList<>();
    StringBuilder sequence = new StringBuilder();

    StringBuilder correctSequence = new StringBuilder();
    boolean inEditMode = false;

    public static LockScreenFragment newInstance() {
        LockScreenFragment fragment = new LockScreenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lock_screen, container, false);

        CheckBox cb1 = (CheckBox) view.findViewById(R.id.cb1);
        CheckBox cb2 = (CheckBox) view.findViewById(R.id.cb2);
        CheckBox cb3 = (CheckBox) view.findViewById(R.id.cb3);
        CheckBox cb4 = (CheckBox) view.findViewById(R.id.cb4);
        CheckBox cb5 = (CheckBox) view.findViewById(R.id.cb5);
        CheckBox cb6 = (CheckBox) view.findViewById(R.id.cb6);
        checkBoxes.add(cb1);
        checkBoxes.add(cb2);
        checkBoxes.add(cb3);
        checkBoxes.add(cb4);
        checkBoxes.add(cb5);
        checkBoxes.add(cb6);

        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
        cb3.setOnCheckedChangeListener(this);
        cb4.setOnCheckedChangeListener(this);
        cb5.setOnCheckedChangeListener(this);
        cb6.setOnCheckedChangeListener(this);

        return view;
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String tag = (String) buttonView.getTag();

        if (isChecked) {
            if(inEditMode){
                if(correctSequence.length()==6) correctSequence = new StringBuilder();
                correctSequence.append(tag);
            } else {
                sequence.append(tag);
            }
        } else {
            clearSequence();
            uncheckAll();
        }

        if (isLastCheckBox((CheckBox) buttonView)) {
            Log.i("Uso sam u petlju", "yeah");
            checkSequence();
        }
    }

    @Subscribe
    public void onPasswordModeChange(Boolean changePasswordModeOn){
        inEditMode = changePasswordModeOn;
        if (changePasswordModeOn){
            setCheckboxMode(R.drawable.custom_checkbox_password_change_mode);
            uncheckAll();
            return;
        }
        setCheckboxMode(R.drawable.custom_checkbox);
        uncheckAll();
    }

    private void setCheckboxMode(int drawable){
        for (CheckBox cb : checkBoxes) {
            cb.setButtonDrawable(drawable);
            cb.setEnabled(true);
        }
    }

    private boolean isLastCheckBox(CheckBox checkBox) {
        for (CheckBox cb : checkBoxes) {
            if (cb.equals(checkBox)) continue;
            if (!cb.isChecked()) return false;
        }
        return true;
    }

    private void checkSequence() {
        if(inEditMode){
            Toast.makeText(getActivity(), "Nova sifra je postavljena", Toast.LENGTH_SHORT).show();
        } else {
            if (sequence.toString().equals(correctSequence.toString())) {
                Toast.makeText(getActivity(), "Tacna sifra", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Ne valja sifra", Toast.LENGTH_SHORT).show();
            }
        }
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                uncheckAll();
            }
        }, 500);
        clearSequence();
    }

    private void uncheckAll() {
        for (final CheckBox cb : checkBoxes) {
            cb.setChecked(false);
        }
    }

    private void clearSequence() {
        sequence = new StringBuilder();
    }
}
