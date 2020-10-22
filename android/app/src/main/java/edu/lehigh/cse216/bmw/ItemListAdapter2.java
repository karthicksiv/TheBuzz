package edu.lehigh.cse216.bmw;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

class ItemListAdapter2 extends RecyclerView.Adapter<ItemListAdapter2.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView cMessage;
//        TextView mMessage;
//        TextView mVote;
//        Button upvote;
//        Button downvote;


//        TextView mId;
//        int id=-1;

        ViewHolder(final View itemView) {
            super(itemView);
            this.cMessage = itemView.findViewById(R.id.cMessage);
            //this.mMessage = itemView.findViewById(R.id.listItemMessage);
//            this.mVote = itemView.findViewById(R.id.mvote);
//            this.mId = itemView.findViewById(R.id.mId);
//
//            upvote=  itemView.findViewById(R.id.buttonUpvote);
//            downvote =  itemView.findViewById(R.id.buttonDownvote);
//
//            View.OnClickListener upvoteListen = new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    VolleySingleton vs = VolleySingleton.getInstance(itemView.getContext());
//                    id = Integer.parseInt(mId.getText().toString());
//                    final String URL = String.format(Routes.getMessageUpvoteUrl(),id);
//                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//                                    Toast.makeText(itemView.getContext(), "upvote success!", Toast.LENGTH_SHORT).show();
//                                }
//                            }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.d("BMW", "error while upvote");
//                            Toast.makeText(itemView.getContext(),"upvote failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }); //see how to make a simple request on android
//                    vs.addRequest(stringRequest);
//                }
//
//            };
//
//            upvote.setOnClickListener(upvoteListen);
//
//            View.OnClickListener downvoteListen = new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    VolleySingleton vs = VolleySingleton.getInstance(itemView.getContext());
//                    id = Integer.parseInt(mId.getText().toString());
//                    final String URL = String.format(Routes.getMessageDownvoteUrl(),id);
//                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//                                    Toast.makeText(itemView.getContext(), "downvote success!", Toast.LENGTH_SHORT).show();
//                                }
//                            }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.d("BMW", "error while downvote");
//                        }
//                    });
//                    vs.addRequest(stringRequest);
//                }
//
//            };
//            downvote.setOnClickListener(downvoteListen);
//
//        }

        }
    }

    private ArrayList<Datum> mData;
    private LayoutInflater mLayoutInflater;

    ItemListAdapter2(Context context, ArrayList<Datum> data) {
        mData = data;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_comment, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Datum d = mData.get(holder.getAdapterPosition());
        holder.cMessage.setText(d.getcMessage());
        // holder.mMessage.setText(d.getmMessage());
//        holder.mId.setText(d.getmId());
//        holder.mVote.setText(Integer.toString(d.getmVote()));


        // Attach a click listener to the view we are configuring
        final View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mClickListener.onClick(d);
            }
        };
        holder.cMessage.setOnClickListener(listener);
    }

    //for clicking messages to get to comment page
    interface ClickListener{
        void onClick(Datum d);
    }

    private ClickListener mClickListener;
    ClickListener getClickListener() { return mClickListener;}
    void setClickListener(ClickListener c) { mClickListener = c;}

}