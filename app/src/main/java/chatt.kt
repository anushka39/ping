class chatt(toString: String, displayName: String?) {
    var messageText: String? = null
    var messageUser: String? = null

    fun ChatMessage(messageText: String?, messageUser: String?) {
        this.messageText = messageText
        this.messageUser = messageUser
        // Initialize to current time
    }

    fun ChatMessage() {}

}