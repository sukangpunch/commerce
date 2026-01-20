package com.example.commerce.payment.dto.response

import com.example.commerce.payment.client.Amount
import com.example.commerce.payment.client.KakaoApproveResponse

data class PaymentApproveResponse(
    val aid: String,
    val tid: String,
    val cid: String,
    val sid: String?,
    val partner_order_id: String,
    val partner_user_id: String,
    val payment_method_type: String,
    val amount: Amount,
    val item_name: String,
    val item_code: String?,
    val quantity: Int,
    val created_at: String,
    val approved_at: String,
    val payload: String?,
){
    companion object{
        fun from(kakaoApproveInfo: KakaoApproveResponse):PaymentApproveResponse{
            return PaymentApproveResponse(
                aid = kakaoApproveInfo.aid,
                tid = kakaoApproveInfo.tid,
                cid = kakaoApproveInfo.cid,
                sid = kakaoApproveInfo.sid ?: "",
                partner_order_id = kakaoApproveInfo.partner_order_id,
                partner_user_id = kakaoApproveInfo.partner_user_id,
                payment_method_type = kakaoApproveInfo.payment_method_type,
                amount = kakaoApproveInfo.amount,
                item_name = kakaoApproveInfo.item_name,
                item_code = kakaoApproveInfo.item_code ?: "",
                quantity = kakaoApproveInfo.quantity,
                created_at = kakaoApproveInfo.created_at,
                approved_at = kakaoApproveInfo.approved_at,
                payload = kakaoApproveInfo.payload ?: "",
            )
        }
    }
}
