package com.example.data.repository

import com.example.data.database.AppDatabase
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

class AppRepository(private val db: AppDatabase) {
    val userDao = db.userDao()
    val storeDao = db.storeDao()
    val productDao = db.productDao()
    val orderDao = db.orderDao()
    val subscriptionDao = db.subscriptionDao()

    // --- Users ---
    fun getAllUsersFlow(): Flow<List<User>> = userDao.getAllUsersFlow()
    suspend fun getAllUsers(): List<User> = userDao.getAllUsers()
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    suspend fun getUserById(id: Int): User? = userDao.getUserById(id)
    suspend fun insertUser(user: User): Long = userDao.insertUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    // --- Stores ---
    fun getAllStoresFlow(): Flow<List<Store>> = storeDao.getAllStoresFlow()
    suspend fun getAllStores(): List<Store> = storeDao.getAllStores()
    suspend fun getStoreById(id: Int): Store? = storeDao.getStoreById(id)
    suspend fun getStoreByOwnerId(ownerId: Int): Store? = storeDao.getStoreByOwnerId(ownerId)
    suspend fun getStoreBySubdomain(subdomain: String): Store? = storeDao.getStoreBySubdomain(subdomain)
    suspend fun insertStore(store: Store): Long = storeDao.insertStore(store)
    suspend fun updateStore(store: Store) = storeDao.updateStore(store)

    // --- Products ---
    fun getProductsByStoreFlow(storeId: Int): Flow<List<Product>> = productDao.getProductsByStoreFlow(storeId)
    suspend fun getProductsByStore(storeId: Int): List<Product> = productDao.getProductsByStore(storeId)
    suspend fun getProductById(id: Int): Product? = productDao.getProductById(id)
    suspend fun insertProduct(product: Product): Long = productDao.insertProduct(product)
    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)
    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)

    // --- Orders ---
    fun getOrdersByStoreFlow(storeId: Int): Flow<List<Order>> = orderDao.getOrdersByStoreFlow(storeId)
    suspend fun getOrdersByStore(storeId: Int): List<Order> = orderDao.getOrdersByStore(storeId)
    fun getAllOrdersFlow(): Flow<List<Order>> = orderDao.getAllOrdersFlow()
    suspend fun insertOrder(order: Order): Long = orderDao.insertOrder(order)
    suspend fun updateOrder(order: Order) = orderDao.updateOrder(order)
    suspend fun deleteOrder(order: Order) = orderDao.deleteOrder(order)

    // --- Subscriptions ---
    fun getSubscriptionsByUserFlow(userId: Int): Flow<List<Subscription>> = subscriptionDao.getSubscriptionsByUserFlow(userId)
    suspend fun getLatestSubscriptionByUser(userId: Int): Subscription? = subscriptionDao.getLatestSubscriptionByUser(userId)
    suspend fun insertSubscription(subscription: Subscription): Long = subscriptionDao.insertSubscription(subscription)
    suspend fun updateSubscription(subscription: Subscription) = subscriptionDao.updateSubscription(subscription)
}
