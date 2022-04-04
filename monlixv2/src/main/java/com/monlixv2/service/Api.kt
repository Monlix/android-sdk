package com.monlixv2.service

import com.monlix.service.models.Offer
import com.monlix.service.models.OfferResponse
import com.monlix.service.models.Transaction
import com.monlix.service.models.TransactionResponse
import org.json.JSONArray
import org.json.JSONObject


object Api {
    const val ENDPOINT = "https://api.monlix.com/api"

    private fun getOffersByType(array: JSONArray): ArrayList<Offer> {
        val outArray: ArrayList<Offer> = arrayListOf()
        for (i in 0 until array.length()) {
            val offerObject = array.getJSONObject(i)
            val offer = Offer(
                offerObject.getInt("id"),
                offerObject.getString("payout"),
                offerObject.getString("currency"),
                offerObject.getString("link"),
                offerObject.getString("name"),
                offerObject.getString("description"),
                offerObject.getString("logo"),
                if (offerObject.has("provider")) { offerObject.getString("provider") } else "",
            )
            outArray.add(offer)
        }
        return outArray
    }

    fun parseOffers(responseString: String): OfferResponse {
        val root = JSONObject(responseString);

        val parsedSurveys = getOffersByType(root.getJSONArray("surveys"))
        val parsedOffers = getOffersByType(root.getJSONArray("offers"))
        val parsedAds = getOffersByType(root.getJSONArray("ads"))

        val offerResponse = OfferResponse(parsedSurveys, parsedOffers, parsedAds);

        return offerResponse;
    }

    fun parseTransactions(responseString: String): TransactionResponse {
        val root = JSONObject(responseString);

        val rawTransactionArray = root.getJSONArray("transactions");
        val transactionArray: ArrayList<Transaction> = arrayListOf()
        for (i in 0 until rawTransactionArray.length()) {
            val transactionObject = rawTransactionArray.getJSONObject(i);
            transactionArray.add(
                Transaction(
                    transactionObject.getString("id"),
                    transactionObject.getString("currency"),
                    transactionObject.getString("name"),
                    transactionObject.getInt("reward"),
                    transactionObject.getString("status"),
                    transactionObject.getString("time")
                )
            )
        }

        return TransactionResponse(
            root.getInt("ptcClicks"),
            root.getInt("ptcEarnings"),
            transactionArray
        );
    }
}
