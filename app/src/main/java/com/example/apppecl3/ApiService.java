package com.example.apppecl3;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("Ubicua/GetStreets")   // ruta del endpoint
    Call<List<String>> getStreetIds();

    @GET("Ubicua/GetDataByDate")
    Call<List<DatoSensor>> getItemsByDate(@Query("date") String date);

    @GET("Ubicua/GetSensors")
    Call<List<String>> getSensorsByStreet(@Query("StrId") String streetId);

    @GET("Ubicua/GetSensorData")
    Call<List<SensorData>> getSensorData(@Query("SId") String sensorId);

}
