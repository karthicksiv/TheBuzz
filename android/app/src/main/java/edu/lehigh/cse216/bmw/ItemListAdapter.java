package edu.lehigh.cse216.bmw;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.List;
import java.util.ArrayList;

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> implements Filterable{
    private ItemFilter mFilter = new ItemFilter();
    private List<Datum>originalData = null;
    private List<Datum>filteredData = null;
    private ArrayList<Datum> mData;
    private LayoutInflater mLayoutInflater;


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mIndex;
        TextView mMessage;
        TextView mVote;
        Button upvote;
        Button downvote;


        TextView mId;
        int id=-1;

        ViewHolder(final View itemView) {
            super(itemView);
            this.mIndex = itemView.findViewById(R.id.listItemIndex); //I know this is misleading but mIndex here is a title
            //this.mMessage = itemView.findViewById(R.id.listItemMessage);
            this.mVote = itemView.findViewById(R.id.mvote);
            this.mId = itemView.findViewById(R.id.mId);

            upvote=  itemView.findViewById(R.id.buttonUpvote);
            downvote =  itemView.findViewById(R.id.buttonDownvote);

            View.OnClickListener upvoteListen = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VolleySingleton vs = VolleySingleton.getInstance(itemView.getContext());
                    id = Integer.parseInt(mId.getText().toString());
                    final String URL = String.format(Routes.getMessageUpvoteUrl(),id);
                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(itemView.getContext(), "upvote success!", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("BMW", "error while upvote");
                            Toast.makeText(itemView.getContext(),"upvote failed", Toast.LENGTH_SHORT).show();
                        }
                    }); //see how to make a simple request on android
                    vs.addRequest(stringRequest);
                }

            };

            upvote.setOnClickListener(upvoteListen);

            View.OnClickListener downvoteListen = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VolleySingleton vs = VolleySingleton.getInstance(itemView.getContext());
                    id = Integer.parseInt(mId.getText().toString());
                    final String URL = String.format(Routes.getMessageDownvoteUrl(),id);
                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(itemView.getContext(), "downvote success!", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("BMW", "error while downvote");
                        }
                    });
                    vs.addRequest(stringRequest);
                }

            };
            downvote.setOnClickListener(downvoteListen);

        }

    }



    public ItemListAdapter(Context context, ArrayList<Datum> data) {
        mData = data;
        this.filteredData = data ;
        this.originalData = data ;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Datum> list = originalData;

            int count = list.size();
            final ArrayList<Datum> nlist = new ArrayList<Datum>(count);

            Datum filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                //if (filterableString.toLowerCase().contains(filterString)) {
                if (filterableString.contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Datum>) results.values;
            notifyDataSetChanged();
        }
    }

        @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Datum d = mData.get(holder.getAdapterPosition());
        holder.mIndex.setText(d.getmTitle());
       // holder.mMessage.setText(d.getmMessage());
        holder.mId.setText(d.getmId());
        holder.mVote.setText(Integer.toString(d.getmVote()));


        // Attach a click listener to the view we are configuring
        final View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mClickListener.onClick(d);
            }
        };
        holder.mIndex.setOnClickListener(listener);
    }

    //for clicking messages to get to comment page
    interface ClickListener{
        void onClick(Datum d);
    }

    private ClickListener mClickListener;
    ClickListener getClickListener() { return mClickListener;}
    void setClickListener(ClickListener c) { mClickListener = c;}
}