package com.example.aninterface.task.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aninterface.task.MapsActivity;
import com.example.aninterface.task.Model.ItemModel;
import com.example.aninterface.task.R;
import com.example.aninterface.task.Utils.Network;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    private Context context;
    private List<ItemModel.Result> list;
    private com.example.aninterface.task.Adapter.Callback callback;

    public ItemAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, int position) {
        ItemModel.Result model = list.get(position);
        View view;
        holder.date.setText(model.getOrderDate());
        Log.e("r", "main");
        holder.food.removeAllViews();
        for (ItemModel.Product product : model.getProducts()) {

            view = LayoutInflater.from(context).inflate(R.layout.sub_item, holder.food, false);
            Log.e("r", "for");
            RatingBar ratingItem = view.findViewById(R.id.rating_item);
            final ProgressBar progressBar = view.findViewById(R.id.imageProgress);
            TextView itemName = view.findViewById(R.id.name_item);
            TextView orderUserName = view.findViewById(R.id.customer_name);
            TextView phoneNumber = view.findViewById(R.id.customer_phone);

            final ImageView itmeImage = view.findViewById(R.id.image_item);
            itemName.setText(product.getProductName());
            orderUserName.setText(product.getUserName());
            phoneNumber.setText(product.getUserMobile());
            ratingItem.setTag(product.getProductRate());
            Picasso.with(context).load(product.getProductImage()).into(itmeImage, new Callback() {
                @Override
                public void onSuccess() {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                        itmeImage.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onError() {

                }
            });
            holder.food.addView(view);
        }

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
    public void setCallback(com.example.aninterface.task.Adapter.Callback callback){
        this.callback=callback;
    }
    public void addlist(List<ItemModel.Result> newList) {
        list.addAll(newList);
        notifyItemRangeInserted(getItemCount(), newList.size() - 1);
    }
    public void deleteItem(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }
    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView delivered, proplem, date;
        private ImageView delete, place;
        private LinearLayout food;

        public ItemHolder(View itemView) {
            super(itemView);
            food = itemView.findViewById(R.id.food);
            delivered = itemView.findViewById(R.id.delivered_item);
            proplem = itemView.findViewById(R.id.proplem_item);
            date = itemView.findViewById(R.id.time_item);
            delete = itemView.findViewById(R.id.delete_item);
            place = itemView.findViewById(R.id.place_item);

            proplem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            place.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Network.isConeccted(context)) {
                        Intent i = new Intent(context, MapsActivity.class);
                        i.putExtra("result", list.get(getAdapterPosition()));
                        i.putExtra("product", list.get(getAdapterPosition()).getProducts().get(0));
                        context.startActivity(i);
                    } else {
                        final Snackbar snackbar = Snackbar.make(view, "No internet conecction", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("Tray again", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (Network.isConeccted(context)) {
                                    if (Network.isConeccted(context)) {
                                        Intent i = new Intent(context, MapsActivity.class);
                                        i.putExtra("result", list.get(getAdapterPosition()));
                                        i.putExtra("product", list.get(getAdapterPosition()).getProducts().get(0));
                                        context.startActivity(i);
                                    }else {

                                    }
                                }

                            }
                        });
                        snackbar.show();
                    }

                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onClick(view,getAdapterPosition());
                }
            });
            proplem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendProplem();
                }
            });
        }

        private void sendProplem() {
            final EditText editText = new EditText(context);
            editText.setHint("Write a proplem");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
            editText.setLayoutParams(lp);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Proplem")
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!editText.getText().toString().isEmpty()) {
                                editText.getText().toString();
                                Toast.makeText(context, "Send Proplem", Toast.LENGTH_LONG).show();
                            }

                            //where?!!!!!!
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setView(editText);

            final AlertDialog dialog = builder.create();

            dialog.show();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (TextUtils.isEmpty(charSequence)) {
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                    } else {
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (TextUtils.isEmpty(editable)) {
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                    } else {
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                    }
                }
            });
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                }
            });
        }
    }


}
