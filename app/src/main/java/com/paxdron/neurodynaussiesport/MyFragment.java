package com.paxdron.neurodynaussiesport;

/**
 * Created by Antonio on 27/03/2016.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyFragment extends Fragment {

    public static Fragment newInstance(Control context, int pos,
                                       float scale)
    {
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        b.putFloat("scale", scale);
        return Fragment.instantiate(context, MyFragment.class.getName(), b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        LinearLayout l = (LinearLayout)
                inflater.inflate(R.layout.mf, container, false);

        int pos = this.getArguments().getInt("pos");
        TextView tv = (TextView) l.findViewById(R.id.text);

        WheelView wva = (WheelView) l.findViewById(R.id.lista);

        wva.setOffset(1);
        List<String> list = new ArrayList<String>();
        String [] elementos;
        String Titulo=null;
        switch(pos){
            case 0:
                Titulo=getString(R.string.tab_stim_mode);
                elementos=getResources().getStringArray(R.array.stimm_mode);
                for (String s : elementos) {
                    list.add(s);
                }
                break;
            case 1:
                Titulo=getString(R.string.tab_carrier);
                elementos=getResources().getStringArray(R.array.carrier);
                for (String s : elementos) {
                    list.add(s);
                }
                break;
            case 2:
                Titulo=getString(R.string.tab_duracion_burst);
                elementos=getResources().getStringArray(R.array.duration);
                for (String s : elementos) {
                    list.add(s);
                }
                break;
            case 3:
                Titulo=getString(R.string.tab_frecuencia_burst);
                for (int i=1;i<=120;i++) {
                    list.add(Integer.toString(i)+" "+getString(R.string.hz));
                }
                break;
            case 4:
                Titulo=getString(R.string.tab_rise);
                for (int i=1;i<=20;i++) {
                    list.add(Integer.toString(i)+" "+getString(R.string.seg));
                }
                break;
            case 5:
                Titulo=getString(R.string.tab_on);
                for (int i=1;i<=60;i++) {
                    list.add(Integer.toString(i)+" "+getString(R.string.seg));
                }
                break;
            case 6:
                Titulo=getString(R.string.tab_decay);
                for (int i=1;i<=20;i++) {
                    list.add(Integer.toString(i)+" "+getString(R.string.seg));
                }
                break;
            case 7:

                Titulo=getString(R.string.tab_off);
                for (int i=1;i<=60;i++) {
                    list.add(Integer.toString(i)+" "+getString(R.string.seg));
                }
                break;
            case 8:
                Titulo=getString(R.string.tab_tiempo);
                for (int i=1;i<=60;i++) {
                    list.add(Integer.toString(i)+" "+getString(R.string.min));
                }
                break;
            default:
                Titulo=getString(R.string.tab_tiempo);
                for (int i=1;i<=60;i++) {
                    list.add(Integer.toString(i)+" "+getString(R.string.seg));
                }
                break;
        }
        tv.setText(Titulo);
        wva.setItems(list);

        MyLinearLayout root = (MyLinearLayout) l.findViewById(R.id.root);
        float scale = this.getArguments().getFloat("scale");
        root.setScaleBoth(scale);

        return l;
    }
}