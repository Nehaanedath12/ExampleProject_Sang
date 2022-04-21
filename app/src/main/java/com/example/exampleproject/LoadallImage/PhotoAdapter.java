package com.example.exampleproject.LoadallImage;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.exampleproject.CaptureImage.ImageCaptureAdapter;
import com.example.exampleproject.R;

import java.io.File;
import java.util.List;

import io.fotoapparat.result.BitmapPhoto;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    final Context context;
    final List<PhotoClass> list;
//    public OnClickListener onClickListener;
//    public void setOnclickListener(OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//
//    }
//    public interface OnClickListener {

//    }


    public PhotoAdapter(Context context, List<PhotoClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PhotoClass photoClass = list.get(position);
        Glide.with(context).load(new File(photoClass.path)).into(holder.image);
        holder.image.setOnClickListener(view -> {
            Intent intent = new Intent(context, NewActivity.class);
            intent.putExtra("positionn", photoClass.path);
            context.startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        });

        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.startDragAndDrop(data, shadowBuilder, view, 0);
                } else {
                    view.startDrag(data, shadowBuilder, view, 0);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
