package com.mapo.walkaholic.data.model.response.Deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.mapo.walkaholic.data.model.response.TodayWeatherResponse
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class TodayWeatherResponseDeserializer : JsonDeserializer<TodayWeatherResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): TodayWeatherResponse {
        val currentTimeZone = TimeZone.getTimeZone("Asia/Seoul")
        val timeFormat = SimpleDateFormat("HH00", Locale.KOREAN)
        timeFormat.timeZone = currentTimeZone
        val requireDate = Date()
        requireDate.hours += 1
        val rawWeatherData = ((((json?.asJsonObject
                ?: throw NullPointerException("Response Json String is null"))["response"]?.asJsonObject
                ?: throw NullPointerException("Response Json String is null"))["body"]?.asJsonObject
                ?: throw NullPointerException("Response Json String is null"))["items"]?.asJsonObject
                ?: throw NullPointerException("Response Json String is null")).getAsJsonArray("item")?.asJsonArray
        rawWeatherData?.forEachIndexed { _rawWeatherDataIndex1, _rawWeatherDataElement1 ->
            if ((_rawWeatherDataElement1.asJsonObject["category"].asString.toString().trim() == "T1H") && (_rawWeatherDataElement1.asJsonObject["fcstTime"].asString.toString().trim() == timeFormat.format(requireDate))) {
                rawWeatherData?.forEachIndexed { _rawWeatherDataIndex2, _rawWeatherDataElement2 ->
                    if ((_rawWeatherDataElement2.asJsonObject["category"].asString.toString().trim() == "PTY") && (_rawWeatherDataElement2.asJsonObject["fcstTime"].asString.toString().trim() == "${timeFormat.format(requireDate)}")) {
                        when (_rawWeatherDataElement2.asJsonObject["fcstValue"].asInt) {
                            0 -> {
                                rawWeatherData?.forEachIndexed { _rawWeatherDataIndex3, _rawWeatherDataElement3 ->
                                    if (_rawWeatherDataElement3.asJsonObject["category"].asString.toString().trim() == "SKY" && (_rawWeatherDataElement3.asJsonObject["fcstTime"].asString.toString().trim() == timeFormat.format(requireDate))) {
                                        return TodayWeatherResponse(false,
                                            TodayWeatherResponse.TodayWeather(
                                                _rawWeatherDataElement1.asJsonObject["fcstValue"].asString.toString()
                                                    .trim(),
                                                when (_rawWeatherDataElement3.asJsonObject["fcstValue"].asInt) {
                                                    1 -> "1" // ??????
                                                    2, 3 -> "2" // ??????
                                                    4 -> "3" // ??????
                                                    else -> "7" // ??????
                                                },
                                                when (_rawWeatherDataElement3.asJsonObject["fcstValue"].asInt) {
                                                    1 -> "??????" // ??????
                                                    2, 3 -> "??????" // ??????
                                                    4 -> "??????" // ??????
                                                    else -> "??????" // ??????
                                                }
                                            )
                                        )
                                    } else { }
                                }
                            }
                            1, 4, 5 -> {
                                return TodayWeatherResponse(false,
                                    TodayWeatherResponse.TodayWeather(
                                        _rawWeatherDataElement1.asJsonObject["fcstValue"].asString.toString()
                                            .trim(), "6", "???"
                                    )
                                ) // ???
                            }
                            2, 6 -> {
                                return TodayWeatherResponse(false,
                                    TodayWeatherResponse.TodayWeather(
                                        _rawWeatherDataElement1.asJsonObject["fcstValue"].asString.toString()
                                            .trim(), "4", "????????????"
                                    )
                                ) // ????????????
                            }
                            3, 7 -> {
                                return TodayWeatherResponse(false, TodayWeatherResponse.TodayWeather(_rawWeatherDataElement1.asJsonObject["fcstValue"].asString.toString().trim(), "5", "???")) // ???
                            }
                            else -> {
                                return TodayWeatherResponse(true, TodayWeatherResponse.TodayWeather(_rawWeatherDataElement1.asJsonObject["fcstValue"].asString.toString().trim(), "7", "???")) // ??????
                            }
                        }
                    }
                }
            } else { }
        }
        return TodayWeatherResponse(true, TodayWeatherResponse.TodayWeather("??????", "7", "??????")) // ??????
    }
}