package comks_kim.github.etherwifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class WiFiListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private WifiListAdapter adapter;
    private List<ScanResult> scanDatas;
    private WifiManager wifiManager;
    private SwipeRefreshLayout pullToRefresh;

    // RecyclerView를 Fragment에 추가하기 위한 코드
    private RecyclerView rvwifilist;
    private RecyclerView.LayoutManager mLayoutManager;

    private Context main_context = getActivity();

    //MainActivity로 뺀다.
//    @Override
//    public void onCreate(@Nullable Bundle savedInsatanceState) {
//        super.onCreate(savedInsatanceState);
//        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        if (!wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 최초에 Fragment가 호출되었을 때 와이파이가 꺼져있으면 강제로 켜도록 함
        // 우리는 wifi를 굳이 킬 필요가 있을까?

        View view = inflater.inflate(R.layout.fragment1_wifi_list, container, false);

        // RecyclerView를 Fragment에 추가하기 위한 코드
        rvwifilist = view.findViewById(R.id.wifilist);
        rvwifilist.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvwifilist.setLayoutManager(mLayoutManager);
        rvwifilist.scrollToPosition(0);
        adapter = new WifiListAdapter(main_context);
        rvwifilist.setAdapter(adapter);
        //rvwifilist.setItemAnimator(new DefaultItemAnimator());

        // pull to Refresh
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(this);


        // 왜 여기서 멈추지???
        // wifiScan();


        // Touch 이벤트
        // getContext()와 getActivity()의 차이
        final GestureDetector gestureDetector = new GestureDetector(this.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        rvwifilist.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                //손으로 터치한 곳의 좌표를 토대로 해당 Item의 View를 가져옴
                View childView = rv.findChildViewUnder(e.getX(), e.getY());

                //터치한 곳의 View가 RecyclerView 안의 아이템이고 그 아이템의 View가 null이 아니라
                //정확한 Item의 View를 가져왔고, gestureDetector에서 한번만 누르면 true를 넘기게 구현했으니
                //한번만 눌려서 그 값이 true가 넘어왔다면
                if (childView != null && gestureDetector.onTouchEvent(e)) {

                    //현재 터치된 곳의 position을 가져오고
                    int currentPosition = rv.getChildAdapterPosition(childView);

                    // itemlist의 item 변수 생성
                    RecyclerViewItem item = adapter.getObject(currentPosition);

                    String ssid = item.getSsid();

                    String price = item.getPrice();
                    Drawable icon = item.getIcon();

                    // TODO: use item data.
                    Toast.makeText(getContext(), ssid, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), price, Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        return view;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                scanDatas = wifiManager.getScanResults();


                HashMap<String, Integer> wifimap = new HashMap<>();
                int count = 0;
                //scanDatas는 어떤형식이지? 배열?
                for (ScanResult select : scanDatas) {
                    String SSID = select.SSID;
                    String BSSID = select.BSSID;
                    int RSSI = select.level;
                    // select.level로 신호세기(rssi) 측정가능

                    if (SSID.isEmpty()) continue;
                    ++count;
                    //수정개선사항: wifi list 찾을 때 중복되는 것 때문에 시간이 좀 걸린다
                    // 이것 때문에 하나씩 계속 delay되며 추가되었던 것
                    // 왜 receiver가 두번 작동하지?
                    // HashMap에서 같은 Value값 or null값의 ssid를 걸러냄
                    if (!wifimap.containsKey(SSID)) {
                        wifimap.put(SSID, RSSI);
                        if (RSSI >= -60) {
                            adapter.addItem(SSID, BSSID, RSSI, ContextCompat.getDrawable(getActivity(), R.drawable.wifi_strength_3));
                        } else if (-75 <= RSSI && RSSI < -60) {
                            adapter.addItem(SSID, BSSID, RSSI, ContextCompat.getDrawable(getActivity(), R.drawable.wifi_strength_2));
                        } else if (-85 <= RSSI && RSSI < -75) {
                            adapter.addItem(SSID, BSSID, RSSI, ContextCompat.getDrawable(getActivity(), R.drawable.wifi_strength_1));
                        }

                        // adapter.notifyDataSetChanged();를 add할 때 마다 하면 기존에 것에서 계속 덧붙여져서 문제가 된다.

                    } else {
                        --count;
                    }
                }
                adapter.itemSort();
            } else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                getActivity().sendBroadcast(new Intent("wifi.ON_NETWORK_STATE_CHANGED"));
            }
        }
    };


    public void wifiScan() {
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(wifiManager.NETWORK_STATE_CHANGED_ACTION);
        getContext().registerReceiver(receiver, intentFilter);
        wifiManager.startScan(); //여기서 살짝 멈춤
    }

    @Override
    public void onResume() {
        super.onResume();
        wifiScan();
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(receiver);
    }


    @Override
    public void onRefresh() {
        pullToRefresh.setRefreshing(true);
        wifiScan();
        pullToRefresh.setRefreshing(false);
    }


    public void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 4000);
    }


}








