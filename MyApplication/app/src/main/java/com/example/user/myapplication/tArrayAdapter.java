package com.example.user.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.widget.TextView;

import java.util.ArrayList;

public class tArrayAdapter extends ArrayAdapter<WifiInfot> {

    Context context;
    int id;
    ArrayList<WifiInfot> inf = new ArrayList<WifiInfot>();

    public tArrayAdapter(Context context,int id,ArrayList<WifiInfot> info){
        super(context,id,info);
        this.id = id;
        this.context = context;
        this.inf = info;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent){

        View item = convertView;
        textWrapper tw ;

        if (item == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            item = inflater.inflate(id,parent,false);
            tw = new textWrapper();
            //tw.ssid = (TextView)item.findViewById(R.id.text);
            item.setTag(tw);
        }
        else {tw = (textWrapper)item.getTag();}

        WifiInfot winf = inf.get(position);
        tw.ssid.setText(winf.getSsid());

        return item;
    }

    static class textWrapper{
        TextView ssid;
    }

}
