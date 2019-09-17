package com.arafat.cachesample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<Users> list;

    public UserAdapter(Context context, List<Users> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_item_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {


        Users users = list.get(position);

        holder.userName.setText("Name: "+users.getUserName());
        holder.userAge.setText("Age: "+String.valueOf(users.getUserEmail()));
        holder.userSalary.setText("Salary: "+String.valueOf(users.getUserPhone())+" $");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, userAge, userSalary;

        public ViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            userAge = itemView.findViewById(R.id.userAge);
            userSalary = itemView.findViewById(R.id.userSalary);
        }
    }
}
