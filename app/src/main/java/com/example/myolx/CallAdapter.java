package com.example.myolx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sinch.android.rtc.calling.Call;

import java.util.ArrayList;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.CallAdapterViewholder> {

      Call call;
    Context context;
    ArrayList<ModelRegister> list;
    public CallAdapter(Context context, ArrayList<ModelRegister> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CallAdapterViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowcalluser,parent,false);
        return new CallAdapterViewholder(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull CallAdapterViewholder holder, final int position) {
       final ModelRegister Mn = list.get(position);
          holder.textname.setText(Mn.getName());
        final String ns = Mn.getId();


        holder.call.setOnClickListener(new View.OnClickListener() {
              //  context.startActivity(intennt);

            @Override
            public void onClick(View v) {
                ModelRegister MO = list.get(position);
                //  String id =

                String  username = MO.getName();

                /*Intent intennt = new Intent(v.getContext(), CallUser.class);
                intennt.putExtra("id",ns);
               intennt.putExtra("calltoname",username);
                */

                Intent callScreen = new Intent(context, CallScreenActivity.class);
                callScreen.putExtra("remoteUid", ns);
                callScreen.putExtra("Username",username);
                Toast.makeText(context, "id = "+ns, Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Username = "+username, Toast.LENGTH_LONG).show();

                // startActivity(callScreen);
                context.startActivity(callScreen);


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CallAdapterViewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textname;
        Button call;
        public CallAdapterViewholder(@NonNull View itemView) {
            super(itemView);
           call = itemView.findViewById(R.id.cbutton);
           imageView =itemView.findViewById(R.id.cimageuser);
           textname=itemView.findViewById(R.id.ctextuser);
           call.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

               }
           });
        }
    }

}
