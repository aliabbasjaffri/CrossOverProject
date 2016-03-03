package com.crossoverproject.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import com.crossoverproject.R;
import android.widget.EditText;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import com.crossoverproject.activity.RegistrationLoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment
{
    TextView headerText;
    EditText userName;
    EditText password;
    Button loginButton;

    String sUsername;
    String sPassword;

    RegistrationLoginActivity obj = (RegistrationLoginActivity) getActivity();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;


    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        //obj.setToolbarHomeAccess(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        headerText = (TextView) view.findViewById(R.id.signInFragmentHeaderTextView);
        userName = (EditText) view.findViewById(R.id.signInFragmentUsernameEditText);
        password = (EditText) view.findViewById(R.id.signInFragmentPasswordEditText);
        loginButton = (Button) view.findViewById(R.id.signInFragmentLoginButton);

        if(mParam1 == getContext().getString(R.string.admin))
            headerText.setText("Admin Login");
        else
            headerText.setText("Doctor Login");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sUsername = userName.getText().toString();
                sPassword = password.getText().toString();
            }
        });

        return view;
    }

}
