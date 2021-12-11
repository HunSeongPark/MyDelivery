package com.hunseong.delivery.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat

@Parcelize
data class TrackingInfoCompany(
    val info: TrackingInformation? = null,
    val company: Company? = null,
    var isChecked: Boolean = false
) : Parcelable {

    // 마지막 업데이트 mm.dd 변환
    fun getLastUpdate(): String {
        val formatter = SimpleDateFormat("MM.dd")
        return formatter.format(info!!.lastDetail!!.time)
    }

    // 운송장번호 | 택배사 문자열 변환
    fun getInvoiceAndCompany(): String {
        return "${info!!.invoiceNo} | ${company!!.name}"
    }

    // 상품 이름 존재 여부에 따른 대체 텍스트
    fun getExistItemName(): String {
        return if (info!!.itemName.isNullOrBlank()) {
            "상품 정보 없음"
        } else {
            info.itemName!!
        }
    }
}