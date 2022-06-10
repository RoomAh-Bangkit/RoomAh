package com.bangkit.roomah.data.remote.response

import com.google.gson.annotations.SerializedName

data class ClassifyResponses(

	@field:SerializedName("result")
	val result: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
