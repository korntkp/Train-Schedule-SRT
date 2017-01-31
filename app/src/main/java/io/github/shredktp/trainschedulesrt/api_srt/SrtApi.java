package io.github.shredktp.trainschedulesrt.api_srt;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Korshreddern on 23-Jan-17.
 */

public interface SrtApi {
    @FormUrlEncoded
    @POST("checktime.asp")
    Call<String> getSchedule(@Field("StationFirst") String stationFirst, @Field("StationLast") String stationLast);

    @GET("checktime.asp")
    Call<String> getStation();
}
