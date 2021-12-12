package com.hunseong.delivery.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat

@Parcelize
data class TrackingDetail(
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("kind")
    val kind: String? = null,
    @SerializedName("level")
    val level: Level? = null,
    @SerializedName("manName")
    val manName: String? = null,
    @SerializedName("manPic")
    val manPic: String? = null,
    @SerializedName("remark")
    val remark: String? = null,
    @SerializedName("telno")
    val telno: String? = null,
    @SerializedName("telno2")
    val telno2: String? = null,
    @SerializedName("time")
    val time: Long? = null,
    @SerializedName("timeString")
    val timeString: String? = null,
    @SerializedName("where")
    val `where`: String? = null,
) : Parcelable {

    // 위치 | 상태 문자열 변환
    fun getPlaceAndState(): String {
        return "$where | ${kind!!.replace("\n", "")}"
    }

    // 2021-01-01 11:11:11 문자열 시간 변환
    fun getTimeFormat(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.format(time)
    }

    // Last Item에 대한 "위치 | " 문자열 변환
    fun getLastPlace(): String {
        return "$where | "
    }

    // 배송 상태 string 줄바꿈 제거
    fun getReplaceState() : String {
        return kind!!.replace("\n", "")
    }
}
