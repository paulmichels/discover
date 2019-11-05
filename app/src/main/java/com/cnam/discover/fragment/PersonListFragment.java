package com.cnam.discover.fragment;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cnam.discover.R;
import com.cnam.discover.adapter.PersonAdapter;
import com.cnam.discover.interfaces.IPerson;

import java.util.ArrayList;
import java.util.List;

public class PersonListFragment extends Fragment {

    private static final String PERSON_LIST = "person_list";

    private List<IPerson> personList = new ArrayList<>();

    public static PersonListFragment newInstance(ArrayList<IPerson> personList) {
        PersonListFragment fragment = new PersonListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(PERSON_LIST, personList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            personList = getArguments().getParcelableArrayList(PERSON_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_list, container, false);
        PersonAdapter personAdapter = new PersonAdapter(personList);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(personAdapter);
        return view;
    }

}
