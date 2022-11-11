package com.example.zhulinaapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterMask extends BaseAdapter {

    private Context mContext;
    List<Mask> maskList;

    public AdapterMask(Context mContext, List<Mask> maskList) {
        this.mContext = mContext;
        this.maskList = maskList;
    }

    @Override
    public int getCount() {
        return maskList.size();
    }

    @Override
    public Object getItem(int i) {
        return maskList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return maskList.get(i).getId();
    }

    private Bitmap getUserImage(String encodedImg) {
        byte[] bytes = Base64.decode(encodedImg, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_mask, null);

        TextView day = v.findViewById(R.id.day);
        TextView wotkout = v.findViewById(R.id.wotkout);
        TextView trainer = v.findViewById(R.id.trainer);
        ImageView Image = v.findViewById(R.id.imageView);

        Mask mask = maskList.get(position);
        day.setText(mask.getday());
        wotkout.setText(mask.getwotkout());
        trainer.setText(mask.gettrainer());
        if (!mask.getImage().equals("null")) {
            Image.setImageBitmap(getUserImage(mask.getImage()));
        }

        return v;
    }
}