package com.bowlofricedev.restaurantqrmenu.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bowlofricedev.restaurantqrmenu.R;
import com.bowlofricedev.restaurantqrmenu.beans.Enlace;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;

public class AdapterEnlacesSwipe extends RecyclerSwipeAdapter<AdapterEnlacesSwipe.SimpleViewHolder> {

    private Context mContext;
    private ArrayList<Enlace> enlaceList;

    public AdapterEnlacesSwipe(Context context, ArrayList<Enlace> objects) {
        this.mContext = context;
        this.enlaceList = objects;
    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_swipe_enlace, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final Enlace item = enlaceList.get(position);

        viewHolder.txtTitle.setText(item.getName());


        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wraper));


        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });

        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, " Click : " + item.getName(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Activity activity = (Activity) mContext;

//                new DialogNuevoEnlace(view.getContext(), fragmentManager, item, "2"); TODO: hacer
            }
        });

        viewHolder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(v.getContext())
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setTitle("Eliminar Enlace")
                        .setMessage("¿Estas seguro de eliminar el enlace: '" + item.getName() + "'?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteEnlace(item, position, viewHolder);
                            }

                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();


            }
        });

        mItemManger.bindView(viewHolder.itemView, position);
    }

    private void deleteEnlace(Enlace enlaceDelete, int position, SimpleViewHolder viewHolder) {

        //TODO: ELIMINAR DE LA BBDD
        mItemManger.removeShownLayouts(viewHolder.swipeLayout);
        enlaceList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, enlaceList.size());
        mItemManger.closeAllItems();

    }

    @Override
    public int getItemCount() {
        return enlaceList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SwipeLayout swipeLayout;
        public TextView txtTitle;
        public ImageButton Delete;
        public ImageButton Edit;

        public SimpleViewHolder(View itemView) {
            super(itemView);

            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            txtTitle = (TextView) itemView.findViewById(R.id.tvTituloEnlace);
            Delete = (ImageButton) itemView.findViewById(R.id.Delete);
            Edit = (ImageButton) itemView.findViewById(R.id.Edit);
        }
    }
}