package com.midway.mobileinspector.provider;

/**
 * Created by ilya on 26.02.16.
 */

import android.location.Location;
import android.location.LocationListener;

import java.util.Collection;

/**
 * Provides a location.
 */
public interface LocationProvider {

    /**
     * @return true if this provider is available for this device.
     */
    boolean isAvailable();

    boolean isNetworkLocationAvailable();

    boolean isGpsLocationAvailable();

    /**
     * @return true if this provider is currently listening.
     */
    boolean isListening();

    /**
     * Add the given listener to be notified when position changes. Should
     * typically be called in Activity's onResume() method.
     *
     * @param listener listener to register
     */
    void registerListener(LocationListener listener);

    /**
     * Remove the given listener from active listeners. Will not be notified
     * anymore when position changes. Should typically be called in Activity's
     * onPause() method.
     *
     * @param listener listener to unregister
     */
    void unregisterListener(LocationListener listener);

    /**
     * Retrieve available listeners.
     * @return the list of listeners
     */
    Collection<LocationListener> getListeners();

    /**
     * Enable listening to new locations
     * @param enabled boolean value
     */
    void setListeningEnabled(boolean enabled);

    /**
     * @return the last known location.
     */
    Location getLastKnownLocation();

}
