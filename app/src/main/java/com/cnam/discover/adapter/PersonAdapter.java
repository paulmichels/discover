package com.cnam.discover.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cnam.discover.R;
import com.cnam.discover.IPerson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyViewHolder> {
    private List<IPerson> personList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView lastName;
        public TextView firstName;
        public ImageView profilePic;

        public MyViewHolder(View view) {
            super(view);
            lastName = view.findViewById(R.id.last_name);
            firstName = view.findViewById(R.id.first_name);
            profilePic = view.findViewById(R.id.profile_picture);
        }
    }

    public PersonAdapter(List<IPerson> personList) {
        this.personList = personList;
    }

    @Override
    public PersonAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.lastName.setText(personList.get(position).getLastName());
        holder.firstName.setText(personList.get(position).getFirstName());
        Picasso.get().load(personList.get(position).getProfilePicUrl()).into(holder.profilePic);

    }

    @Override
    public int getItemCount() {
        return personList.size();
    }
}
