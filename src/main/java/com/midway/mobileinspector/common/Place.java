package com.midway.mobileinspector.common;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.midway.mobileinspector.service.ControlService;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ilya on 22.02.16.
 */
public class Place {
    private Location location;

    public Place(Location location) {
        this.location = location;
    }

    public Address getAddress(){
        Geocoder gcd = new Geocoder(ControlService.getContext(), Locale.ENGLISH);
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                return addresses.get(0);
            }
        }
        catch (IOException e) {
            HttpHelper.sendMessage("Can't get the address");
        }
        return null;
    }

    public String getStringAddress() {
        Address address = getAddress();
        if (address == null) return "";
        return address.getAddressLine(0);

    }

    public Location getLocation() {
        return location;
    }


    public void setLocation(Location location) {
        this.location = location;
    }

    public String getProvider() {
        return location.getProvider();
    }

    public long getLocationTime() {
        return location.getTime();
    }

    public float getAccuracy() {
        return location.getAccuracy();
    }

}
