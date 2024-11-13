package com.example.fake_slink.adapter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateAdapter : JsonDeserializer<Date>, JsonSerializer<Date> {

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a", Locale.ENGLISH)

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date {
        return dateFormat.parse(json?.asString) ?: throw JsonParseException("Lỗi định dạng ngày tháng")
    }

    override fun serialize(
        src: Date?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(dateFormat.format(src))
    }
}