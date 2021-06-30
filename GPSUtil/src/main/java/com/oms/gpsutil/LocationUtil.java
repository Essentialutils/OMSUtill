package com.oms.gpsutil;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.Settings;

import java.util.Random;

public class LocationUtil {
    /**
     * The internal name of the provider for the coarse location
     */
    private static final String PROVIDER_COARSE = LocationManager.NETWORK_PROVIDER;
    /**
     * The internal name of the provider for the fine location
     */
    private static final String PROVIDER_FINE = LocationManager.GPS_PROVIDER;
    /**
     * The internal name of the provider for the fine location in passive mode
     */
    private static final String PROVIDER_FINE_PASSIVE = LocationManager.PASSIVE_PROVIDER;
    /**
     * The default interval to receive new location updates after (in milliseconds)
     */
    private static final long INTERVAL_DEFAULT = 10 * 60 * 1000;
    /**
     * The factor for conversion from kilometers to meters
     */
    private static final float KILOMETER_TO_METER = 1000.0f;
    /**
     * The factor for conversion from latitude to kilometers
     */
    private static final float LATITUDE_TO_KILOMETER = 111.133f;
    /**
     * The factor for conversion from longitude to kilometers at zero degree in latitude
     */
    private static final float LONGITUDE_TO_KILOMETER_AT_ZERO_LATITUDE = 111.320f;
    /**
     * The PRNG that is used for location blurring
     */
    private static final Random mRandom = new Random();
    private static final double SQUARE_ROOT_TWO = Math.sqrt(2);
    /**
     * The last location that was internally cached when creating new instances in the same process
     */
    private static Location mCachedPosition;
    /**
     * The LocationManager instance used to query the device location
     */
    private final LocationManager mLocationManager;
    /**
     * Whether a fine location should be required or coarse location can be used
     */
    private final boolean mRequireFine;
    /**
     * Whether passive mode shall be used or not
     */
    private final boolean mPassive;
    /**
     * The internal after which new location updates are requested (in milliseconds) where longer intervals save battery
     */
    private final long mInterval;
    /**
     * Whether to require a new location (`true`) or accept old (last known) locations as well (`false`)
     */
    private final boolean mRequireNewLocation;
    /**
     * The blur radius (in meters) that will be used to blur the location for privacy reasons
     */
    private int mBlurRadius;
    /**
     * The LocationListener instance used internally to listen for location updates
     */
    private LocationListener mLocationListener;
    /**
     * The current location with latitude, longitude, speed and altitude
     */
    private Location mPosition;
    private Listener mListener;

    public LocationUtil(final Context context) {
        this(context, false);
    }

    public LocationUtil(final Context context, final boolean requireFine) {
        this(context, requireFine, false);
    }

    public LocationUtil(final Context context, final boolean requireFine, final boolean passive) {
        this(context, requireFine, passive, INTERVAL_DEFAULT);
    }

    public LocationUtil(final Context context, final boolean requireFine, final boolean passive, final long interval) {
        this(context, requireFine, passive, interval, false);
    }

    public LocationUtil(final Context context, final boolean requireFine, final boolean passive, final long interval, final boolean requireNewLocation) {
        mLocationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        mRequireFine = requireFine;
        mPassive = passive;
        mInterval = interval;
        mRequireNewLocation = requireNewLocation;

        if (!mRequireNewLocation) {
            mPosition = getCachedPosition();
            cachePosition();
        }
    }

    private static int calculateRandomOffset(final int radius) {
        return mRandom.nextInt((radius + 1) * 2) - radius;
    }

    public static void openSettings(final Context context) {
        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public static double latitudeToKilometer(double latitude) {
        return latitude * LATITUDE_TO_KILOMETER;
    }

    public static double kilometerToLatitude(double kilometer) {
        return kilometer / latitudeToKilometer(1.0f);
    }

    public static double latitudeToMeter(double latitude) {
        return latitudeToKilometer(latitude) * KILOMETER_TO_METER;
    }

    public static double meterToLatitude(double meter) {
        return meter / latitudeToMeter(1.0f);
    }

    public static double longitudeToKilometer(double longitude, double latitude) {
        return longitude * LONGITUDE_TO_KILOMETER_AT_ZERO_LATITUDE * Math.cos(Math.toRadians(latitude));
    }

    public static double kilometerToLongitude(double kilometer, double latitude) {
        return kilometer / longitudeToKilometer(1.0f, latitude);
    }

    public static double longitudeToMeter(double longitude, double latitude) {
        return longitudeToKilometer(longitude, latitude) * KILOMETER_TO_METER;
    }

    public static double meterToLongitude(double meter, double latitude) {
        return meter / longitudeToMeter(1.0f, latitude);
    }

    public static double calculateDistance(Point start, Point end) {
        return calculateDistance(start.latitude, start.longitude, end.latitude, end.longitude);
    }

    public static double calculateDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        float[] results = new float[3];
        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);
        return results[0];
    }

    public void setListener(final Listener listener) {
        mListener = listener;
    }

    public boolean hasLocationEnabled() {
        return hasLocationEnabled(getProviderName());
    }

    private boolean hasLocationEnabled(final String providerName) {
        try {
            return mLocationManager.isProviderEnabled(providerName);
        } catch (Exception e) {
            return false;
        }
    }

    public void beginUpdates() {
        if (mLocationListener != null) {
            endUpdates();
        }

        if (!mRequireNewLocation) {
            mPosition = getCachedPosition();
        }

        mLocationListener = createLocationListener();
        mLocationManager.requestLocationUpdates(getProviderName(), mInterval, 0, mLocationListener);
    }

    public void endUpdates() {
        if (mLocationListener != null) {
            mLocationManager.removeUpdates(mLocationListener);
            mLocationListener = null;
        }
    }

    private Location blurWithRadius(final Location originalLocation) {
        if (mBlurRadius <= 0) {
            return originalLocation;
        } else {
            Location newLocation = new Location(originalLocation);

            double blurMeterLong = calculateRandomOffset(mBlurRadius) / SQUARE_ROOT_TWO;
            double blurMeterLat = calculateRandomOffset(mBlurRadius) / SQUARE_ROOT_TWO;

            newLocation.setLongitude(newLocation.getLongitude() + meterToLongitude(blurMeterLong, newLocation.getLatitude()));
            newLocation.setLatitude(newLocation.getLatitude() + meterToLatitude(blurMeterLat));

            return newLocation;
        }
    }

    public Point getPosition() {
        if (mPosition == null) {
            return null;
        } else {
            Location position = blurWithRadius(mPosition);
            return new Point(position.getLatitude(), position.getLongitude());
        }
    }

    public double getLatitude() {
        if (mPosition == null) {
            return 0.0f;
        } else {
            Location position = blurWithRadius(mPosition);
            return position.getLatitude();
        }
    }

    public double getLongitude() {
        if (mPosition == null) {
            return 0.0f;
        } else {
            Location position = blurWithRadius(mPosition);
            return position.getLongitude();
        }
    }

    public long getTimestampInMilliseconds() {
        if (mPosition == null) {
            return 0L;
        } else {
            return mPosition.getTime();
        }
    }

    public long getElapsedTimeInNanoseconds() {
        if (mPosition == null) {
            return 0L;
        } else {
            if (Build.VERSION.SDK_INT >= 17) {
                return mPosition.getElapsedRealtimeNanos();
            } else {
                return (SystemClock.elapsedRealtime() + getTimestampInMilliseconds() - System.currentTimeMillis()) * 1000000;
            }
        }
    }

    public float getSpeed() {
        if (mPosition == null) {
            return 0.0f;
        } else {
            return mPosition.getSpeed();
        }
    }

    public double getAltitude() {
        if (mPosition == null) {
            return 0.0f;
        } else {
            return mPosition.getAltitude();
        }
    }

    public void setBlurRadius(final int blurRadius) {
        mBlurRadius = blurRadius;
    }

    private LocationListener createLocationListener() {
        return new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                mPosition = location;
                cachePosition();

                if (mListener != null) {
                    mListener.onPositionChanged();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

        };
    }

    private String getProviderName() {
        return getProviderName(mRequireFine);
    }

    private String getProviderName(final boolean requireFine) {
        // if fine location (GPS) is required
        if (requireFine) {
            // we just have to decide between active and passive mode

            if (mPassive) {
                return PROVIDER_FINE_PASSIVE;
            } else {
                return PROVIDER_FINE;
            }
        }
        // if both fine location (GPS) and coarse location (network) are acceptable
        else {
            // if we can use coarse location (network)
            if (hasLocationEnabled(PROVIDER_COARSE)) {
                // if we wanted passive mode
                if (mPassive) {
                    // throw an exception because this is not possible
                    throw new RuntimeException("There is no passive provider for the coarse location");
                }
                // if we wanted active mode
                else {
                    // use coarse location (network)
                    return PROVIDER_COARSE;
                }
            }
            // if coarse location (network) is not available
            else {
                // if we can use fine location (GPS)
                if (hasLocationEnabled(PROVIDER_FINE) || hasLocationEnabled(PROVIDER_FINE_PASSIVE)) {
                    // we have to use fine location (GPS) because coarse location (network) was not available
                    return getProviderName(true);
                }
                // no location is available so return the provider with the minimum permission level
                else {
                    return PROVIDER_COARSE;
                }
            }
        }
    }

    private Location getCachedPosition() {
        if (mCachedPosition != null) {
            return mCachedPosition;
        } else {
            try {
                return mLocationManager.getLastKnownLocation(getProviderName());
            } catch (Exception e) {
                return null;
            }
        }
    }

    private void cachePosition() {
        if (mPosition != null) {
            mCachedPosition = mPosition;
        }
    }

    public interface Listener {
        void onPositionChanged();
    }

    public static class Point implements Parcelable {
        public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator<Point>() {
            @Override
            public Point createFromParcel(Parcel in) {
                return new Point(in);
            }

            @Override
            public Point[] newArray(int size) {
                return new Point[size];
            }

        };
        public final double latitude;
        public final double longitude;

        public Point(double lat, double lon) {
            latitude = lat;
            longitude = lon;
        }

        private Point(Parcel in) {
            latitude = in.readDouble();
            longitude = in.readDouble();
        }

        @Override
        public String toString() {
            return "(" + latitude + ", " + longitude + ")";
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeDouble(latitude);
            out.writeDouble(longitude);
        }

    }

}
