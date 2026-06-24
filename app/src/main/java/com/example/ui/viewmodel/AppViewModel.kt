package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.*
import com.example.data.repository.AppRepository
import com.example.data.api.GeminiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AppRepository

    // Screen navigation state
    var currentScreen = MutableStateFlow("auth") // "auth", "dashboard", "products", "orders", "designer", "admin", "ccp_payment"

    // Session user
    val currentUser = MutableStateFlow<User?>(null)
    val merchantStore = MutableStateFlow<Store?>(null)

    // Reactive states
    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    val allUsers: StateFlow<List<User>> = _allUsers.asStateFlow()

    private val _allStores = MutableStateFlow<List<Store>>(emptyList())
    val allStores: StateFlow<List<Store>> = _allStores.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    // Smart Analytics predictions state
    val predictedMonthlySales = MutableStateFlow<Double>(0.0)
    val aiInsightText = MutableStateFlow<String>("اضغط على 'تحليل بالذكاء الاصطناعي' للحصول على توقعات مبيعات دقيقة وتوصيات مخصصة لمتجرك.")
    val isAnalyzingAi = MutableStateFlow(false)

    // Simulation states
    val lastSimulatedWhatsappMessage = MutableStateFlow<String?>(null)
    val isShowingWhatsappNotification = MutableStateFlow(false)
    val isShowingCcpWebflow = MutableStateFlow(false)

    init {
        val database = AppDatabase.getDatabase(application)
        repository = AppRepository(database)

        // Pre-fill default accounts to make testing amazing
        viewModelScope.launch(Dispatchers.IO) {
            initDefaultAccounts()
            observeDatabase()
        }
    }

    private suspend fun observeDatabase() {
        // Collect users and stores globally
        repository.getAllUsersFlow().collect { list ->
            _allUsers.value = list
        }
    }

    private fun startObservingMerchantData(storeId: Int, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            // Collect merchant products
            repository.getProductsByStoreFlow(storeId).collect { list ->
                _products.value = list
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            // Collect merchant orders
            repository.getOrdersByStoreFlow(storeId).collect { list ->
                _orders.value = list
            }
        }
    }

    private suspend fun initDefaultAccounts() {
        val existingUsers = repository.getAllUsers()
        if (existingUsers.isEmpty()) {
            // Create Admin
            val adminUser = User(
                fullName = "مدير المنصة",
                email = "admin@rshop.com",
                passwordHash = "admin123", // Simplified for prototype
                role = "admin",
                phone = "0555123456",
                subscriptionStatus = "active",
                plan = "pro"
            )
            repository.insertUser(adminUser)

            // Create default Merchant
            val merchantUser = User(
                fullName = "أحمد لبيع الإلكترونيات",
                email = "ahmed@rshop.com",
                passwordHash = "ahmed123",
                role = "merchant",
                phone = "0661987654",
                subscriptionStatus = "active",
                plan = "basic"
            )
            val merchantId = repository.insertUser(merchantUser).toInt()

            // Create default Store
            val defaultStore = Store(
                ownerId = merchantId,
                subdomain = "ahmed-tech",
                whatsappNumber = "213661987654",
                themeColor = "#FFFF5722", // Deep Orange Accent
                fontFamily = "Serif",
                layout = "grid",
                balance = 12500.0,
                predictedMonthlySales = 45000.0
            )
            val storeId = repository.insertStore(defaultStore).toInt()

            // Update user with storeId
            val updatedUser = merchantUser.copy(id = merchantId, storeId = storeId)
            repository.updateUser(updatedUser)

            // Add default Products
            val products = listOf(
                Product(storeId = storeId, name = "سماعات لاسلكية Pro", price = 4500.0, stock = 12, category = "إلكترونيات", reorderPoint = 5),
                Product(storeId = storeId, name = "شاحن سريع 65W", price = 2500.0, stock = 3, category = "ملحقات", reorderPoint = 5),
                Product(storeId = storeId, name = "ساعة ذكية SmartWatch v2", price = 8500.0, stock = 8, category = "إلكترونيات", reorderPoint = 3),
                Product(storeId = storeId, name = "حامل هاتف للسيارة مغناطيسي", price = 1200.0, stock = 15, category = "ملحقات", reorderPoint = 4)
            )
            products.forEach { repository.insertProduct(it) }

            // Add default Orders
            val order1Items = JSONArray().apply {
                put(JSONObject().apply {
                    put("name", "سماعات لاسلكية Pro")
                    put("price", 4500.0)
                    put("quantity", 1)
                })
                put(JSONObject().apply {
                    put("name", "شاحن سريع 65W")
                    put("price", 2500.0)
                    put("quantity", 2)
                })
            }.toString()

            val order2Items = JSONArray().apply {
                put(JSONObject().apply {
                    put("name", "ساعة ذكية SmartWatch v2")
                    put("price", 8500.0)
                    put("quantity", 1)
                })
            }.toString()

            repository.insertOrder(
                Order(
                    storeId = storeId,
                    customerName = "عبد القادر بن عودة",
                    customerPhone = "0771234567",
                    itemsJson = order1Items,
                    totalPrice = 9500.0,
                    status = "pending",
                    deliveryAddress = "حي الموز، باب الزوار، الجزائر",
                    paymentMethod = "ccp"
                )
            )

            repository.insertOrder(
                Order(
                    storeId = storeId,
                    customerName = "ياسمين بلعيد",
                    customerPhone = "0550987654",
                    itemsJson = order2Items,
                    totalPrice = 8500.0,
                    status = "delivered",
                    deliveryAddress = "شارع ديدوش مراد، الجزائر الوسطى",
                    paymentMethod = "wallet"
                )
            )
        }
        
        // Always populate platform stores list
        repository.getAllStoresFlow().collect { list ->
            _allStores.value = list
        }
    }

    // --- Authentication Actions ---
    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.getUserByEmail(email)
            if (user != null && user.passwordHash == password) {
                currentUser.value = user
                if (user.role == "merchant") {
                    val store = repository.getStoreByOwnerId(user.id)
                    merchantStore.value = store
                    if (store != null) {
                        startObservingMerchantData(store.id, user.id)
                    }
                    currentScreen.value = "dashboard"
                } else {
                    currentScreen.value = "admin"
                }
                onResult(true, "تم تسجيل الدخول بنجاح")
            } else {
                onResult(false, "البريد الإلكتروني أو كلمة المرور غير صحيحة")
            }
        }
    }

    fun register(
        fullName: String,
        email: String,
        passwordHash: String,
        phone: String,
        subdomain: String,
        plan: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            // Check email uniqueness
            if (repository.getUserByEmail(email) != null) {
                onResult(false, "البريد الإلكتروني مسجل بالفعل")
                return@launch
            }
            // Check subdomain uniqueness
            if (repository.getStoreBySubdomain(subdomain) != null) {
                onResult(false, "اسم المتجر الفرعي مستخدم بالفعل")
                return@launch
            }

            // Create user
            val newUser = User(
                fullName = fullName,
                email = email,
                passwordHash = passwordHash,
                role = "merchant",
                phone = phone,
                subscriptionStatus = "active", // Activate instantly for test trial
                plan = plan
            )
            val userId = repository.insertUser(newUser).toInt()

            // Create associated store
            val newStore = Store(
                ownerId = userId,
                subdomain = subdomain,
                whatsappNumber = if (phone.startsWith("0")) "213" + phone.substring(1) else phone,
                themeColor = "#FF6200EE",
                fontFamily = "SansSerif"
            )
            val storeId = repository.insertStore(newStore).toInt()

            // Update user storeId reference
            val updatedUser = newUser.copy(id = userId, storeId = storeId)
            repository.updateUser(updatedUser)

            // Create initial Subscription entry
            val subscriptionPrice = if (plan == "pro") 3000.0 else 1000.0
            val newSubscription = Subscription(
                userId = userId,
                plan = plan,
                price = subscriptionPrice,
                endDate = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000), // 30 Days
                status = "active"
            )
            repository.insertSubscription(newSubscription)

            // Insert sample products for a beautiful initial experience
            val defaultProducts = listOf(
                Product(storeId = storeId, name = "عينة منتج 1", price = 1500.0, stock = 10, category = "عام", reorderPoint = 3),
                Product(storeId = storeId, name = "عينة منتج 2", price = 3000.0, stock = 2, category = "عام", reorderPoint = 4)
            )
            defaultProducts.forEach { repository.insertProduct(it) }

            // Log user in
            currentUser.value = updatedUser
            merchantStore.value = newStore
            startObservingMerchantData(storeId, userId)
            
            currentScreen.value = "dashboard"
            onResult(true, "تم إنشاء المتجر وتفعيله بنجاح!")
        }
    }

    fun logout() {
        currentUser.value = null
        merchantStore.value = null
        _products.value = emptyList()
        _orders.value = emptyList()
        currentScreen.value = "auth"
    }

    // --- Product Management ---
    fun addProduct(name: String, price: Double, stock: Int, category: String, reorderPoint: Int) {
        val store = merchantStore.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val product = Product(
                storeId = store.id,
                name = name,
                price = price,
                stock = stock,
                category = category,
                reorderPoint = reorderPoint
            )
            repository.insertProduct(product)
            
            // Check if stock is low instantly on creation and trigger notification simulator
            if (stock <= reorderPoint) {
                triggerLowStockAlert(product)
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProduct(product)
            if (product.stock <= product.reorderPoint) {
                triggerLowStockAlert(product)
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProduct(product)
        }
    }

    // --- Order Management ---
    fun createSampleOrder(customerName: String, customerPhone: String, items: List<Pair<Product, Int>>, address: String, paymentMethod: String) {
        val store = merchantStore.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val itemsJsonArray = JSONArray()
            var total = 0.0
            items.forEach { (product, qty) ->
                val price = product.price
                total += price * qty
                itemsJsonArray.put(JSONObject().apply {
                    put("name", product.name)
                    put("price", price)
                    put("quantity", qty)
                })

                // Decrease stock
                val updatedProduct = product.copy(stock = (product.stock - qty).coerceAtLeast(0))
                repository.updateProduct(updatedProduct)
            }

            val order = Order(
                storeId = store.id,
                customerName = customerName,
                customerPhone = customerPhone,
                itemsJson = itemsJsonArray.toString(),
                totalPrice = total,
                status = "pending",
                deliveryAddress = address,
                paymentMethod = paymentMethod
            )
            repository.insertOrder(order)
            
            // Auto generate customized WhatsApp alert message via Gemini API or simulation
            generateWhatsAppAlertAndShow(order)
        }
    }

    fun updateOrderStatus(order: Order, newStatus: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedOrder = order.copy(status = newStatus)
            repository.updateOrder(updatedOrder)
        }
    }

    // --- Store Designer config ---
    fun saveStoreConfig(themeColor: String, fontFamily: String, logoUrl: String, layout: String) {
        val store = merchantStore.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val updatedStore = store.copy(
                themeColor = themeColor,
                fontFamily = fontFamily,
                logoUrl = logoUrl,
                layout = layout
            )
            repository.updateStore(updatedStore)
            merchantStore.value = updatedStore
        }
    }

    // --- Admin Platform Actions ---
    fun toggleUserStoreSubscription(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val newStatus = if (user.subscriptionStatus == "suspended") "active" else "suspended"
            val updatedUser = user.copy(subscriptionStatus = newStatus)
            repository.updateUser(updatedUser)
            
            // Also notify stores
            val store = repository.getStoreByOwnerId(user.id)
            if (store != null) {
                // Just sync state
                if (currentUser.value?.id == user.id) {
                    currentUser.value = updatedUser
                }
            }
            // Update global lists
            _allUsers.value = repository.getAllUsers()
        }
    }

    // --- Simulated Subscription Webhook / CCP payment ---
    fun renewSubscriptionViaCcp(plan: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            // Simulated delay for Baridimob verification webhook
            val updatedUser = user.copy(subscriptionStatus = "active", plan = plan)
            repository.updateUser(updatedUser)
            
            val price = if (plan == "pro") 3000.0 else 1000.0
            val subscription = Subscription(
                userId = user.id,
                plan = plan,
                price = price,
                endDate = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000),
                status = "active"
            )
            repository.insertSubscription(subscription)
            
            currentUser.value = updatedUser
            isShowingCcpWebflow.value = false
        }
    }

    // --- Intelligent AI Analytics ---
    fun runAiSmartAnalytics() {
        val store = merchantStore.value ?: return
        val currentProducts = _products.value
        val currentOrders = _orders.value
        
        isAnalyzingAi.value = true
        
        viewModelScope.launch(Dispatchers.IO) {
            val productsString = currentProducts.joinToString("\n") { "- ${it.name} (${it.category}): السعر ${it.price} دج، المخزن الحالي: ${it.stock}" }
            val ordersString = currentOrders.joinToString("\n") { "- طلب من ${it.customerName} بقيمة ${it.totalPrice} دج وحالته ${it.status}" }

            val prompt = """
            أنت خبير ذكاء اصطناعي ومستشار أعمال لمنصة التجارة الإلكترونية R-Shop.
            قم بإجراء تحليل شامل وتنبؤ ذكي للمبيعات وتدفق المخزون للمتجر الفرعي التالي:
            اسم المتجر الفرعي: ${store.subdomain}
            
            المنتجات الحالية المتوفرة في المتجر:
            $productsString
            
            سجل المبيعات والطلبات الأخير:
            $ordersString
            
            المطلوب بلغة عربية احترافية وجميلة وموجهة للتاجر مباشرة:
            1. تنبؤ تقريبي بالمبيعات المتوقعة للشهر القادم بناءً على معدل الطلبات.
            2. تقييم سريع للمخزون ومن هو المنتج الأكثر عرضة للنفاد وما هي نسبة النفاذ.
            3. نصيحتين تسويقيتين أو تشغيليتين ذكيتين مخصصة بالكامل لزيادة المبيعات بنسبة 20% هذا الشهر.
            """.trimIndent()

            val response = GeminiHelper.generateContent(prompt)
            
            // Calculate a beautiful mathematical expected monthly sales forecast
            val averageOrderValue = if (currentOrders.isNotEmpty()) currentOrders.map { it.totalPrice }.average() else 2500.0
            val estimatedMonthlyCount = (currentOrders.size * 5).coerceAtLeast(8)
            val expectedValue = averageOrderValue * estimatedMonthlyCount

            predictedMonthlySales.value = expectedValue
            aiInsightText.value = response
            isAnalyzingAi.value = false
            
            // Update store predictedMonthlySales in Database
            repository.updateStore(store.copy(predictedMonthlySales = expectedValue))
        }
    }

    // --- Low Stock WhatsApp Alarm Simulator ---
    private fun triggerLowStockAlert(product: Product) {
        val store = merchantStore.value ?: return
        val msg = """
        🚨 *R-Shop - تنبيه المخزون المنخفض!* 🚨
        
        مرحباً يا صاحب متجر (*${store.subdomain}*)،
        نود تنبيهك بأن مخزون المنتج:
        📦 *"${product.name}"*
        قد وصل إلى: *${product.stock}* قطع فقط! ⚠️
        وهو تحت حد إعادة الطلب المحدد بـ (${product.reorderPoint} قطع).
        
        يرجى شحن المخزون سريعاً لضمان استمرار مبيعاتك وعدم تفويت طلبات العملاء. 🚀
        """.trimIndent()

        lastSimulatedWhatsappMessage.value = msg
        isShowingWhatsappNotification.value = true
    }

    // --- Order WhatsApp Notification Generator ---
    private fun generateWhatsAppAlertAndShow(order: Order) {
        val store = merchantStore.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val prompt = """
            أنت بوت المبيعات التلقائي لمنصة R-Shop.
            قم بصياغة رسالة تأكيد طلب احترافية وممتازة باللغة العربية والرموز التعبيرية لإرسالها عبر WhatsApp للعميل:
            اسم العميل: ${order.customerName}
            رقم الهاتف: ${order.customerPhone}
            القيمة الإجمالية للطلب: ${order.totalPrice} دج
            طريقة الدفع: ${order.paymentMethod}
            عنوان التوصيل: ${order.deliveryAddress}
            اسم المتجر: ${store.subdomain}
            
            اجعل الرسالة ترحيبية، منظمة، تحتوي على تفاصيل الطلب، ومؤكدة على أن خدمة العملاء ستتصل به لتأكيد الشحن.
            """.trimIndent()

            val message = GeminiHelper.generateContent(prompt)
            lastSimulatedWhatsappMessage.value = message
            isShowingWhatsappNotification.value = true
        }
    }

    fun resendWhatsAppNotificationManual(order: Order) {
        generateWhatsAppAlertAndShow(order)
    }
}
