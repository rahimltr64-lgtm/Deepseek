package com.example.data.dao

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsersFlow(): Flow<List<User>>

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}

@Dao
interface StoreDao {
    @Query("SELECT * FROM stores")
    fun getAllStoresFlow(): Flow<List<Store>>

    @Query("SELECT * FROM stores")
    suspend fun getAllStores(): List<Store>

    @Query("SELECT * FROM stores WHERE id = :id LIMIT 1")
    suspend fun getStoreById(id: Int): Store?

    @Query("SELECT * FROM stores WHERE ownerId = :ownerId LIMIT 1")
    suspend fun getStoreByOwnerId(ownerId: Int): Store?

    @Query("SELECT * FROM stores WHERE subdomain = :subdomain LIMIT 1")
    suspend fun getStoreBySubdomain(subdomain: String): Store?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: Store): Long

    @Update
    suspend fun updateStore(store: Store)
}

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE storeId = :storeId ORDER BY createdAt DESC")
    fun getProductsByStoreFlow(storeId: Int): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE storeId = :storeId")
    suspend fun getProductsByStore(storeId: Int): List<Product>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: Int): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE storeId = :storeId ORDER BY createdAt DESC")
    fun getOrdersByStoreFlow(storeId: Int): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE storeId = :storeId")
    suspend fun getOrdersByStore(storeId: Int): List<Order>

    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAllOrdersFlow(): Flow<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    @Update
    suspend fun updateOrder(order: Order)

    @Delete
    suspend fun deleteOrder(order: Order)
}

@Dao
interface SubscriptionDao {
    @Query("SELECT * FROM subscriptions WHERE userId = :userId ORDER BY endDate DESC")
    fun getSubscriptionsByUserFlow(userId: Int): Flow<List<Subscription>>

    @Query("SELECT * FROM subscriptions WHERE userId = :userId LIMIT 1")
    suspend fun getLatestSubscriptionByUser(userId: Int): Subscription?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscription(subscription: Subscription): Long

    @Update
    suspend fun updateSubscription(subscription: Subscription)
}
