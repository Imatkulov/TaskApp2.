package com.taskapp2;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BoardFragment extends Fragment {


    public BoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        TextView textTitle = view.findViewById(R.id.textTitle);
        TextView textDesc = view.findViewById(R.id.textDesc);
        ImageView imageView = view.findViewById(R.id.imageView);
        Button button = view.findViewById(R.id.button);
        int pos =getArguments().getInt("pos");
        switch (pos){
            case 0:
                textTitle.setText("Привет");
                textDesc.setText("Добро пожаловать в Андроид мир");
                imageView.setImageResource(R.drawable.smile1);
                view.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                button.setVisibility(view.INVISIBLE);
                break;
            case 1:
                textTitle.setText("Как дела?");
                textDesc.setText("Second page");
                imageView.setImageResource(R.drawable.smile2);
                view.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                button.setVisibility(view.INVISIBLE);
                break;
            case 2:
                textTitle.setText("Что делаешь?");
                textDesc.setText("Что делаешь мир ?");
                imageView.setImageResource(R.drawable.smile3);
                view.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                button.setVisibility(view.VISIBLE);
                break;
        }
        return view;
    }

}
