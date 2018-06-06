package comks_kim.github.etherwifi;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.ViewHolder> {
    Context getclass;
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    public ArrayList<RecyclerViewItem> Recycleritemlist = new ArrayList<>();

    // ListViewAdapter의 생성자
    public WifiListAdapter(Context getclass) {
        this.getclass = getclass;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.f1_listview_custom, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // Data Set(listViewItemRecycler)에서 position에 위치한 데이터 참조 획득
        RecyclerViewItem recyclerViewItem = Recycleritemlist.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        viewHolder.iconImageView.setImageDrawable(recyclerViewItem.getIcon());
        viewHolder.titleTextView.setText(recyclerViewItem.getSsid());
        viewHolder.descTextView.setText(recyclerViewItem.getSsid());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView descTextView;
        public ImageView iconImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.ssid);
            descTextView = itemView.findViewById(R.id.price);
            iconImageView = itemView.findViewById(R.id.icon);
        }

    }


    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    public int getItemCount() {
        return Recycleritemlist.size();
    }


    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    public RecyclerViewItem getObject(int position) {
        return Recycleritemlist.get(position);
    }


    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능. (와이파이 상태)

    public void addItem(String ssid, String bssid, int rssi, Drawable icon) {
        RecyclerViewItem item = new RecyclerViewItem();
        boolean available = true;
        for (int i = 0; i < Recycleritemlist.size(); i++) {
            RecyclerViewItem items = Recycleritemlist.get(i);
            if (items.getSsid() != null) {
                if (items.getSsid().equals(ssid)) {
                    available = false;
                }
                // ssid는 같지만 rssi가 다를 때
                if (items.getSsid().equals(ssid) && items.getRssi() != rssi) {
                    available = true;
                    Recycleritemlist.remove(i);
                }
            }
        }
        if (available) {
            item.setRssi(rssi);
            item.setSsid(ssid);
            item.setPrice(bssid);
            item.setIcon(icon);
            Recycleritemlist.add(item);
        }
    }

    public void itemSort() {
        Collections.sort(Recycleritemlist, new Comparator<RecyclerViewItem>() {
            @Override
            public int compare(RecyclerViewItem o1, RecyclerViewItem o2) {
                if (o1.getRssi() > o2.getRssi()) {
                    return 1;
                } else if (o1.getRssi() < o2.getRssi()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        Collections.reverse(Recycleritemlist);
        this.notifyDataSetChanged();
    }


    public void itemDelete() {

        if (!Recycleritemlist.isEmpty()) {
            for (int i = 0; i < Recycleritemlist.size(); i++) {
                Recycleritemlist.remove(i);
                i = i - 1;
                //머야 제대로 안지워지네???
                // remove()함수는 공간까지 지운다...
            }
        }
//        this.listViewItemRecycler.clear();
        this.notifyDataSetChanged();
    }


}
