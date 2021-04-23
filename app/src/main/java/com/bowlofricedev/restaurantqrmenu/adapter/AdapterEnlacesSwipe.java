package com.bowlofricedev.restaurantqrmenu.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.AndroidRuntimeException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bowlofricedev.restaurantqrmenu.DialogEditEnlace;
import com.bowlofricedev.restaurantqrmenu.R;
import com.bowlofricedev.restaurantqrmenu.WebviewActivity;
import com.bowlofricedev.restaurantqrmenu.beans.Enlace;
import com.bowlofricedev.restaurantqrmenu.tools.DatabaseHelper;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.ArrayList;

public class AdapterEnlacesSwipe extends RecyclerSwipeAdapter<AdapterEnlacesSwipe.SimpleViewHolder> implements View.OnLongClickListener {

    private Context mContext;
    private ArrayList<Enlace> enlaceList;
    DatabaseHelper mDatabaseHelper;

    public AdapterEnlacesSwipe(Context context, ArrayList<Enlace> objects) {
        this.mContext = context;
        this.enlaceList = objects;
        mDatabaseHelper = new DatabaseHelper(context);
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

        if (item.getType().equals("PDF")) {
            viewHolder.enlaceIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_pdfviewpager));
        }


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
                Intent intentWB = new Intent(mContext, WebviewActivity.class);
                intentWB.putExtra("url", item.getUrl());
                try {
                    mContext.startActivity(intentWB);
                } catch (AndroidRuntimeException e) {
                    intentWB.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intentWB);
                }
            }
        });

        viewHolder.swipeLayout.getSurfaceView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

//                new DialogEditEnlace(v.getContext(), item);

                try{
                    //copiamos enlace al portapapeles
                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", item.getUrl());
                    clipboard.setPrimaryClip(clip);

                    Toast toast1 = Toast.makeText(mContext, String.valueOf(R.string.copied_to_clipboard), Toast.LENGTH_SHORT);
                    toast1.show();

                }catch (Exception ex){

                }


                return false;
            }
        });



        //BOTON FAV

        viewHolder.favoriteButton.setFavorite(item.getFav().equals("s"));
        viewHolder.favoriteButton.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        item.setFav(favorite ? "s" : "n");
                        mDatabaseHelper.updateEnlace(item.getId(), item);
                    }
                });


        //BOTON EDIT

        viewHolder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DialogEditEnlace(view.getContext(), item);
            }
        });

        //BOTON SHARE

        viewHolder.Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //copiamos enlace al portapapeles
                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", item.getUrl());
                    clipboard.setPrimaryClip(clip);

                    Toast toast1 = Toast.makeText(mContext, String.valueOf(R.string.copied_to_clipboard), Toast.LENGTH_SHORT);
                    toast1.show();

                    shareUrl(item.getUrl());

                }catch (Exception ex){

                    String h = ex.getMessage();
                }
            }
        });

        //BOTON DELETE

        viewHolder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cuerpo = mContext.getResources().getString(R.string.eliminar_confirmacion_cuerpo);
                cuerpo = cuerpo + "\n\n"+item.getName();
                String si = mContext.getResources().getString(R.string.si);
                String no = mContext.getResources().getString(R.string.no);

                new AlertDialog.Builder(v.getContext())
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setTitle(R.string.eliminar_confirmacion_titulo)
                        .setMessage(cuerpo)
                        .setPositiveButton(si, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteEnlace(item, position, viewHolder);
                            }

                        })
                        .setNegativeButton(no, new DialogInterface.OnClickListener() {
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

    private void shareUrl(String url) {

        //package name (será en nombre en google play)
        final String appPackageName = mContext.getPackageName(); // getPackageName() from Context or Activity object


        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, url +" \nShared by Restaurant QR Menu\nhttps://play.google.com/store/apps/details?id=\\" + appPackageName);
        sendIntent.setType("text/plain");
        mContext.startActivity(Intent.createChooser(sendIntent, String.valueOf(R.string.shareOn))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


    }

    private void deleteEnlace(Enlace enlaceDelete, int position, SimpleViewHolder viewHolder) {

        //TODO: ELIMINAR DE LA BBDD

        mDatabaseHelper.deleteEnlace(enlaceDelete.getId());
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

    @Override
    public boolean onLongClick(View v) {



        return false;
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SwipeLayout swipeLayout;
        public TextView txtTitle;
        public ImageButton Delete;
        public ImageButton Edit;
        public ImageButton Share;
        public ImageView enlaceIcon;
        public MaterialFavoriteButton favoriteButton;

        public SimpleViewHolder(View itemView) {
            super(itemView);

            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            txtTitle = (TextView) itemView.findViewById(R.id.tvTituloEnlace);
            Delete = (ImageButton) itemView.findViewById(R.id.Delete);
            Edit = (ImageButton) itemView.findViewById(R.id.Edit);
            Share = (ImageButton) itemView.findViewById(R.id.Share);
            enlaceIcon = (ImageView) itemView.findViewById(R.id.enlaceIcon);

            //Boton favorito
            favoriteButton = (MaterialFavoriteButton) itemView.findViewById(R.id.favoriteButton);

        }
    }
}