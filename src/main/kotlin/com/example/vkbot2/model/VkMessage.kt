package com.example.vkbot2.model

data class VkMessage(
        val type: String,
        val `object`: VkObject?
) {
    data class VkObject(
            val message: VkMessageObject
    )

    data class VkMessageObject(
            val peer_id: Int,
            val text: String
    )
}
