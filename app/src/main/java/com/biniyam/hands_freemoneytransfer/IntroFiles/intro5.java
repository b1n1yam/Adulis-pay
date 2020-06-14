package com.biniyam.hands_freemoneytransfer.IntroFiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.biniyam.hands_freemoneytransfer.R;

import java.util.Locale;

public class intro5 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RadioButton en, am;
    RadioGroup lang;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private View rootView;

    public intro5() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment intro2.
     */
    // TODO: Rename and change types and number of parameters
    public static intro5 newInstance(String param1, String param2) {
        intro5 fragment = new intro5();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_intro_five, container, false);
        en = rootView.findViewById(R.id.en);
        am = rootView.findViewById(R.id.am);

        en.setChecked(true);

        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioClick(v);
            }
        });

        am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioClick(v);
            }
        });


        return rootView;
    }


    public void onRadioClick(View view) {
        Boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.en:
                if (checked)
                    setLocale("en");

                break;
            case R.id.am:
                if (checked)

                    setLocale("am");

                break;
        }
    }

    public void setLocale(String localeName) {
        try {
            Locale myLocale;

            //get cuttent language
            SharedPreferences sp = getContext().getSharedPreferences("com.biniyam.hands_freemoneytransfer", Context.MODE_PRIVATE);
            String currentLanguage = sp.getString("lang", "");

            //set language
            if (!localeName.equals(currentLanguage)) {

                myLocale = new Locale(localeName);
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
                //actualy updating it
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("lang", localeName);
                editor.apply();


            }
        } catch (Exception e) {
            Log.e("err", e.toString());
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

