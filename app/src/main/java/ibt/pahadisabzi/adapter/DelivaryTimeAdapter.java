package ibt.pahadisabzi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ibt.pahadisabzi.R;
import ibt.pahadisabzi.model.delivary_time_responce.Deliverytiming;

public class DelivaryTimeAdapter extends BaseAdapter {
    Context context;
    ArrayList<Deliverytiming> doctorSpecializationData;
    LayoutInflater inflter;

    public DelivaryTimeAdapter(Context applicationContext, ArrayList<Deliverytiming> doctorSpecializationData) {
        this.context = applicationContext;
        this.doctorSpecializationData = doctorSpecializationData;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return doctorSpecializationData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_delivery_time, null);
        TextView title = (TextView) view.findViewById(R.id.tvTimeTitle);
        TextView titleTime = (TextView) view.findViewById(R.id.tvTimeDelivary);
        title.setText("( "+doctorSpecializationData.get(i).getDeliveryTimingTitle()+" )");
        titleTime.setText(doctorSpecializationData.get(i).getDeliveryTimingStartTime() + " - "+ doctorSpecializationData.get(i).getDeliveryTimingEndTime());
        return view;
    }
}
