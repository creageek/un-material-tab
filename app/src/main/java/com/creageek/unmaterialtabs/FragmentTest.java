package com.creageek.unmaterialtabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ruslan Kishai aka creageek on 1/3/2017.
 */

public class FragmentTest extends Fragment {
    TextView text;
    String ftext = "empty";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test, container, false);
        text = (TextView) rootView.findViewById(R.id.fragment_text);
        text.setText(ftext);
        return rootView;
    }

    public FragmentTest setFragmentText(String fragmentText) {
        ftext = fragmentText;
        return this;
    }
}
