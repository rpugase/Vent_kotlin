package org.zapomni.venturers.data.model.response

data class PollResponse(val id: String,
                        val chatId: String,
                        val name: String,
                        val results: Map<String, Int>?,
                        val voted: Boolean,
                        val image: String?)