package me.uni.hiker.model

import me.uni.hiker.model.user.User

typealias Subscriber = (user: User?) -> Unit
typealias Unsubscribe = () -> Unit

class Profile {
    var user: User? = null
        private set

    private val subscribers = mutableListOf<Subscriber>()

    fun hasToken(): Boolean {
        return this.user?.token != null
    }

    @Throws(IllegalAccessException::class)
    fun getToken(): String {
        return this.user?.token ?: throw IllegalAccessException("")
    }

    fun setUser(user: User) {
        this.user = user
    }

    fun clear() {
        this.user = null
    }

    fun addSubscriber(subscriber: Subscriber): Unsubscribe {
        subscribers.add(subscriber)

        return fun() {
            subscribers.removeIf { it == subscriber }
        }
    }

    fun notifySubscribers() {
        subscribers.forEach{ subscriber ->
            subscriber(user)
        }
    }
}