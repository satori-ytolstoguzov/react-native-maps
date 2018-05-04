package com.airbnb.android.react.maps;

import android.content.Context;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class AirMapPolyline extends AirMapFeature {

  private PolylineOptions polylineOptions;
  private Polyline polyline;

  private List<LatLng> coordinates;
  private int color;
  private float width;
  private boolean geodesic;
  private float zIndex;
  private List<PatternItem> lineDashPattern;

  public AirMapPolyline(Context context) {
    super(context);
  }

  public void setCoordinates(ReadableArray coordinates) {
    this.coordinates = new ArrayList<>(coordinates.size());
    for (int i = 0; i < coordinates.size(); i++) {
      ReadableMap coordinate = coordinates.getMap(i);
      this.coordinates.add(i,
          new LatLng(coordinate.getDouble("latitude"), coordinate.getDouble("longitude")));
    }
    if (polyline != null) {
      polyline.setPoints(this.coordinates);
    }
  }

  public void setColor(int color) {
    this.color = color;
    if (polyline != null) {
      polyline.setColor(color);
    }
  }

  public void setWidth(float width) {
    this.width = width;
    if (polyline != null) {
      polyline.setWidth(width);
    }
  }

  public void setZIndex(float zIndex) {
    this.zIndex = zIndex;
    if (polyline != null) {
      polyline.setZIndex(zIndex);
    }
  }

  public void setGeodesic(boolean geodesic) {
    this.geodesic = geodesic;
    if (polyline != null) {
      polyline.setGeodesic(geodesic);
    }
  }

  public PolylineOptions getPolylineOptions() {
    if (polylineOptions == null) {
      polylineOptions = createPolylineOptions();
    }
    return polylineOptions;
  }

  public void setLineDashPattern(ReadableArray patternArray) {
	if (patternArray != null && patternArray.size() == 2) {
	  int dashLength = patternArray.getInt(0);
	  int gapLength = patternArray.getInt(1);
	  this.lineDashPattern = Arrays.asList(new Dash(dashLength), new Gap(gapLength));
	  if (polyline != null) {
		polyline.setPattern(this.lineDashPattern);
	  }
	}
  }

  private PolylineOptions createPolylineOptions() {
    PolylineOptions options = new PolylineOptions();
    options.addAll(coordinates);
    options.color(color);
    options.width(width);
    options.geodesic(geodesic);
    options.zIndex(zIndex);
    options.pattern(lineDashPattern);
    return options;
  }

  @Override
  public Object getFeature() {
    return polyline;
  }

  @Override
  public void addToMap(GoogleMap map) {
    polyline = map.addPolyline(getPolylineOptions());
    polyline.setClickable(true);
  }

  @Override
  public void removeFromMap(GoogleMap map) {
    polyline.remove();
  }
}
