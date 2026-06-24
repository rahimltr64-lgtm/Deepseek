package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val email: String,
    val passwordHash: String,
    val role: String, // "admin" or "merchant"
    val phone: String,
    val storeId: Int? = null,
    val subscriptionStatus: String = "active", // "active", "expired", "suspended"
    val plan: String = "basic", // "basic", "pro"
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "stores")
data class Store(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ownerId: Int,
    val subdomain: String,
    val whatsappNumber: String,
    val themeColor: String = "#FF6200EE", // M3 Primary default
    val fontFamily: String = "SansSerif",
    val logoUrl: String = "",
    val layout: String = "grid", // "grid" or "list"
    val balance: Double = 0.0,
    val predictedMonthlySales: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val storeId: Int,
    val name: String,
    val price: Double,
    val stock: Int = 0,
    val imageUrl: String = "",
    val category: String = "General",
    val reorderPoint: Int = 5,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val storeId: Int,
    val customerName: String,
    val customerPhone: String,
    val itemsJson: String, // JSON array of items: [{"name":"Product A", "price":100.0, "quantity":2}]
    val totalPrice: Double,
    val status: String = "pending", // "pending", "processing", "shipped", "delivered"
    val deliveryAddress: String,
    val paymentMethod: String = "ccp", // "wallet" or "ccp"
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "subscriptions")
data class Subscription(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val plan: String, // "basic" or "pro"
    val price: Double,
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long,
    val status: String = "active" // "active" or "expired"
)
