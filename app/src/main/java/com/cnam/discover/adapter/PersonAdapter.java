package com.cnam.discover.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cnam.discover.R;
import com.cnam.discover.interfaces.IPerson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    private List<IPerson> personList;

    public class PersonViewHolder extends RecyclerView.ViewHolder{

        private ImageView profilePicture;
        private TextView firstName;
        private TextView lastName;

        public PersonViewHolder(View view) {
            super(view);
            profilePicture = view.findViewById(R.id.profile_picture);
            firstName = view.findViewById(R.id.first_name);
            lastName = view.findViewById(R.id.last_name);
        }
    }

    public PersonAdapter(List<IPerson> personList){
        this.personList = personList;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_row, parent, false);
        return new PersonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        IPerson person = personList.get(position);
        Picasso.get().load(person.getProfilePicUrl()).into(holder.profilePicture);
        holder.firstName.setText(person.getFirstName());
        holder.lastName.setText(person.getLastName());
    }

    @Override
    public int getItemCount() {
        if (personList == null)
            return 0;
        else
            return personList.size();
    }
}