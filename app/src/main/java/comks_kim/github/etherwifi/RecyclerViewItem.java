package comks_kim.github.etherwifi;

import android.graphics.drawable.Drawable;

public class RecyclerViewItem {
    private Drawable icon;
    private String ssid;
    private String price;
    private int rssi;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable iconSignal) {
        this.icon = iconSignal;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

}
