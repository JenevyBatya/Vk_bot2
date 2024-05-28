package com.example.vkbot2.controller

import com.example.vkbot2.model.VkMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/callback")
class VkCallbackController {

    @Value("\${vk.confirmation}")
    lateinit var confirmationCode: String

    @Value("\${vk.token}")
    lateinit var token: String

    @Value("\${vk.api.version}")
    lateinit var apiVersion: String

    @Value("\${vk.api.endpoint}")
    lateinit var apiEndpoint: String

    @GetMapping
    fun hi(): String {
        return "hi"
    }

    @PostMapping
    fun handleCallback(@RequestBody vkMessage: VkMessage): String {
        return when (vkMessage.type) {
            "confirmation" -> confirmationCode
            "message_new" -> {
                vkMessage.`object`?.let { vkObject ->
                    vkObject.message.let { message ->
                        sendMessage(message.peer_id, "Вы сказали: ${message.text}")
                    }
                }
                "ok"
            }
            else -> "Unsupported event"
        }
    }

    private fun sendMessage(peerId: Int, message: String) {
        val method = "messages.send"
        val url = "$apiEndpoint$method"
        val params = "peer_id=$peerId&message=$message&access_token=$token&v=$apiVersion&random_id=0"

        val restTemplate = RestTemplate()
        try {
            val response = restTemplate.postForObject("$url?$params", null, String::class.java)
            if (response != null && response.contains("\"response\"")) {
                println("Message sent successfully")
            } else {
                println("Failed to send message, response: $response")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
