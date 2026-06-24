package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Order
import com.example.data.model.Product
import com.example.data.model.Store
import com.example.data.model.User
import com.example.ui.viewmodel.AppViewModel
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

// --- SCREEN 1: AUTH SCREEN ---
@Composable
fun AuthScreen(viewModel: AppViewModel) {
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var subdomain by remember { mutableStateOf("") }
    var plan by remember { mutableStateOf("basic") } // "basic", "pro"

    var feedbackMsg by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RShopTheme.Background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            // Logo Image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(RShopTheme.PrimaryGradient),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "R-Shop SaaS",
                color = RShopTheme.TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "منصة المتاجر الإلكترونية المتكاملة",
                color = RShopTheme.TextSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Tab Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(RShopTheme.Surface)
                    .padding(4.dp)
            ) {
                Button(
                    onClick = { isLogin = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLogin) RShopTheme.CardBg else Color.Transparent,
                        contentColor = if (isLogin) RShopTheme.Primary else RShopTheme.TextSecondary
                    ),
                    elevation = null,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("تسجيل الدخول", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { isLogin = false },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isLogin) RShopTheme.CardBg else Color.Transparent,
                        contentColor = if (!isLogin) RShopTheme.Primary else RShopTheme.TextSecondary
                    ),
                    elevation = null,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("إنشاء متجر جديد", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form container
            Card(
                colors = CardDefaults.cardColors(containerColor = RShopTheme.Surface),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    if (!isLogin) {
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("الاسم الكامل للتاجر") },
                            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = RShopTheme.Primary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RShopTheme.Primary,
                                unfocusedBorderColor = RShopTheme.TextSecondary.copy(alpha = 0.3f),
                                focusedLabelColor = RShopTheme.Primary,
                                unfocusedLabelColor = RShopTheme.TextSecondary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("رقم الهاتف (الواتساب)") },
                            leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null, tint = RShopTheme.Primary) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RShopTheme.Primary,
                                unfocusedBorderColor = RShopTheme.TextSecondary.copy(alpha = 0.3f),
                                focusedLabelColor = RShopTheme.Primary,
                                unfocusedLabelColor = RShopTheme.TextSecondary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = subdomain,
                            onValueChange = { subdomain = it },
                            label = { Text("اسم النطاق الفرعي (e.g., ahmed-store)") },
                            leadingIcon = { Icon(Icons.Filled.Home, contentDescription = null, tint = RShopTheme.Primary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RShopTheme.Primary,
                                unfocusedBorderColor = RShopTheme.TextSecondary.copy(alpha = 0.3f),
                                focusedLabelColor = RShopTheme.Primary,
                                unfocusedLabelColor = RShopTheme.TextSecondary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Plan picker
                        Text("اختر خطة الاشتراك الشهري:", color = RShopTheme.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .border(
                                        width = 2.dp,
                                        color = if (plan == "basic") RShopTheme.Primary else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .background(RShopTheme.CardBg)
                                    .clickable { plan = "basic" }
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("الأساسية (Basic)", color = RShopTheme.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("1,000 دج / شهر", color = RShopTheme.Primary, fontSize = 11.sp)
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .border(
                                        width = 2.dp,
                                        color = if (plan == "pro") RShopTheme.Primary else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .background(RShopTheme.CardBg)
                                    .clickable { plan = "pro" }
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("المتقدمة (Pro)", color = RShopTheme.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("3,000 دج / شهر", color = RShopTheme.Primary, fontSize = 11.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("البريد الإلكتروني") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = RShopTheme.Primary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RShopTheme.Primary,
                            unfocusedBorderColor = RShopTheme.TextSecondary.copy(alpha = 0.3f),
                            focusedLabelColor = RShopTheme.Primary,
                            unfocusedLabelColor = RShopTheme.TextSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("كلمة المرور") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = RShopTheme.Primary) },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RShopTheme.Primary,
                            unfocusedBorderColor = RShopTheme.TextSecondary.copy(alpha = 0.3f),
                            focusedLabelColor = RShopTheme.Primary,
                            unfocusedLabelColor = RShopTheme.TextSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (isLoading) {
                        CircularProgressIndicator(
                            color = RShopTheme.Primary,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        Button(
                            onClick = {
                                isLoading = true
                                feedbackMsg = null
                                if (isLogin) {
                                    viewModel.login(email, password) { success, msg ->
                                        isLoading = false
                                        feedbackMsg = msg
                                    }
                                } else {
                                    if (fullName.isBlank() || phone.isBlank() || subdomain.isBlank() || email.isBlank() || password.isBlank()) {
                                        isLoading = false
                                        feedbackMsg = "يرجى ملء جميع الحقول المطلوبة!"
                                        return@Button
                                    }
                                    viewModel.register(fullName, email, password, phone, subdomain, plan) { success, msg ->
                                        isLoading = false
                                        feedbackMsg = msg
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RShopTheme.Primary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isLogin) "سجل الدخول" else "ابدأ متجرك الآن 🚀",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    if (feedbackMsg != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = feedbackMsg!!,
                            color = if (feedbackMsg!!.contains("نجاح")) Color(0xFF00E676) else RShopTheme.Danger,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Demo accounts quick-login cards
            Text(
                text = "⚡ تجربة سريعة بدون إدخال بيانات:",
                color = RShopTheme.TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(RShopTheme.Surface)
                        .clickable {
                            email = "ahmed@rshop.com"
                            password = "ahmed123"
                            feedbackMsg = "تم ملء حساب التاجر أحمد (صاحب المتجر). اضغط تسجيل الدخول."
                        }
                        .padding(12.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Icon(imageVector = Icons.Filled.Home, contentDescription = null, tint = RShopTheme.Primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("تاجر تجريبي (أحمد)", color = RShopTheme.TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("ahmed@rshop.com", color = RShopTheme.TextSecondary, fontSize = 9.sp)
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(RShopTheme.Surface)
                        .clickable {
                            email = "admin@rshop.com"
                            password = "admin123"
                            feedbackMsg = "تم ملء حساب مدير المنصة. اضغط تسجيل الدخول للتحكم بالمتاجر."
                        }
                        .padding(12.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Icon(imageVector = Icons.Filled.Settings, contentDescription = null, tint = Color(0xFF00E676))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("مسؤول المنصة (Admin)", color = RShopTheme.TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("admin@rshop.com", color = RShopTheme.TextSecondary, fontSize = 9.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// --- SCREEN 2: MERCHANT DASHBOARD ---
@Composable
fun DashboardScreen(viewModel: AppViewModel) {
    val store by viewModel.merchantStore.collectAsState()
    val products by viewModel.products.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val user by viewModel.currentUser.collectAsState()
    val predictedSales by viewModel.predictedMonthlySales.collectAsState()
    val aiInsight by viewModel.aiInsightText.collectAsState()
    val isAnalyzing by viewModel.isAnalyzingAi.collectAsState()

    val totalSales = orders.filter { it.status == "delivered" }.sumOf { it.totalPrice }
    val totalPending = orders.filter { it.status == "pending" }.size

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(RShopTheme.Background),
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {
        item {
            GradientHeader(
                title = "مرحباً، ${user?.fullName ?: ""}",
                subtitle = "نطاق متجرك: ${store?.subdomain ?: ""}.rshop.com",
                actionIcon = Icons.Filled.Close,
                onActionClick = { viewModel.logout() }
            )
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {
                // Subscription Status Indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (user?.subscriptionStatus == "active") Color(0xFF1B5E20) else Color(0xFFB71C1C)
                        )
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.isShowingCcpWebflow.value = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            Text("تجديد/ترقية الاشتراك 💳", color = RShopTheme.Primary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "الخطة: ${if (user?.plan == "pro") "المتقدمة (Pro)" else "الأساسية"} (${if (user?.subscriptionStatus == "active") "نشط" else "متوقف"})",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                             )
                             Spacer(modifier = Modifier.width(6.dp))
                             Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats grid
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        title = "إجمالي المبيعات",
                        value = "${totalSales.toInt()}",
                        unit = "دج",
                        icon = Icons.Filled.Check,
                        gradient = RShopTheme.AccentGradient,
                        modifier = Modifier.weight(1.5f)
                    )
                    StatCard(
                        title = "الطلبات النشطة",
                        value = "$totalPending",
                        unit = "طلب",
                        icon = Icons.Filled.Warning,
                        gradient = RShopTheme.BlueGradient,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        title = "رصيد العمولات",
                        value = "${store?.balance?.toInt() ?: 0}",
                        unit = "دج",
                        icon = Icons.Filled.Check,
                        gradient = RShopTheme.PurpleGradient,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "عدد المنتجات",
                        value = "${products.size}",
                        icon = Icons.Filled.List,
                        gradient = RShopTheme.PrimaryGradient,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Smart AI Prediction Card
                RShopCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(RShopTheme.PrimaryGradient),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Filled.Settings, contentDescription = null, tint = Color.White)
                        }
                        Text(
                            text = "التحليلات الذكية والتنبؤ بالمبيعات",
                            color = RShopTheme.TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Right
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(RShopTheme.Background)
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "توقعات المبيعات الشهرية المقدرة:",
                                color = RShopTheme.TextSecondary,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "${predictedSales.toInt() ?: store?.predictedMonthlySales?.toInt() ?: 0} دج / شهرياً",
                                color = Color(0xFF00E676),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            Text(
                                text = "من المتوقع أن تربح هذا المبلغ من مبيعاتك الشهر الحالي بناءً على سلوك المشترين والذكاء الاصطناعي.",
                                color = RShopTheme.TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = aiInsight,
                        color = RShopTheme.TextPrimary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isAnalyzing) {
                        CircularProgressIndicator(
                            color = RShopTheme.Primary,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        Button(
                            onClick = { viewModel.runAiSmartAnalytics() },
                            colors = ButtonDefaults.buttonColors(containerColor = RShopTheme.Primary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Filled.Info, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("تحليل ذكي مخصص ومحدث بالذكاء الاصطناعي", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Low Stock Alerts Card
                val lowStockProducts = products.filter { it.stock <= it.reorderPoint }
                if (lowStockProducts.isNotEmpty()) {
                    RShopCard(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Warning, contentDescription = null, tint = RShopTheme.Danger)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "تنبيهات المخزون المنخفض (${lowStockProducts.size})",
                                color = RShopTheme.Danger,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        lowStockProducts.forEach { prod ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(RShopTheme.Danger.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "المتبقي: ${prod.stock} قطع",
                                        color = RShopTheme.Danger,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = prod.name,
                                    color = RShopTheme.TextPrimary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- SCREEN 3: PRODUCTS MANAGEMENT ---
@Composable
fun ProductsScreen(viewModel: AppViewModel) {
    val products by viewModel.products.collectAsState()
    var isAddingProduct by remember { mutableStateOf(false) }

    // Add Product Fields
    var pName by remember { mutableStateOf("") }
    var pPrice by remember { mutableStateOf("") }
    var pStock by remember { mutableStateOf("") }
    var pCategory by remember { mutableStateOf("") }
    var pReorder by remember { mutableStateOf("5") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RShopTheme.Background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GradientHeader(
                title = "إدارة منتجات المتجر",
                subtitle = "أضف، حدّث، أو احذف منتجاتك لتظهر فوراً لعملائك"
            )

            if (products.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = null, tint = RShopTheme.TextSecondary, modifier = Modifier.size(60.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("لا يوجد منتجات حالياً.", color = RShopTheme.TextPrimary, fontWeight = FontWeight.Bold)
                        Text("اضغط على الزر الدائري لإضافة منتجك الأول.", color = RShopTheme.TextSecondary, fontSize = 12.sp, textAlign = TextAlign.Center)
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(products) { product ->
                        val isLow = product.stock <= product.reorderPoint
                        RShopCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = if (isLow) RShopTheme.Danger.copy(alpha = 0.5f) else Color.Transparent,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { viewModel.deleteProduct(product) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(imageVector = Icons.Filled.Delete, contentDescription = null, tint = RShopTheme.Danger, modifier = Modifier.size(16.dp))
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(RShopTheme.Primary.copy(alpha = 0.15f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(product.category, color = RShopTheme.Primary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = product.name,
                                    color = RShopTheme.TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = "${product.price.toInt()} دج",
                                    color = Color(0xFF00E676),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(
                                            onClick = {
                                                viewModel.updateProduct(product.copy(stock = product.stock + 1))
                                            },
                                            modifier = Modifier
                                                .size(22.dp)
                                                .background(RShopTheme.CardBg, CircleShape)
                                        ) {
                                            Icon(imageVector = Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        IconButton(
                                            onClick = {
                                                if (product.stock > 0) {
                                                    viewModel.updateProduct(product.copy(stock = product.stock - 1))
                                                }
                                            },
                                            modifier = Modifier
                                                .size(22.dp)
                                                .background(RShopTheme.CardBg, CircleShape)
                                        ) {
                                            Icon(imageVector = Icons.Filled.Delete, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                        }
                                    }
                                    Text(
                                        text = "المخزن: ${product.stock}",
                                        color = if (isLow) RShopTheme.Danger else RShopTheme.TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Add Product Dialog Trigger Floating Button
        FloatingActionButton(
            onClick = { isAddingProduct = true },
            containerColor = RShopTheme.Primary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "أضف منتج")
        }

        // Add Product Simple Inline Dialog
        if (isAddingProduct) {
            AlertDialog(
                onDismissRequest = { isAddingProduct = false },
                title = { Text("إضافة منتج جديد لمتجرك", textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth()) },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = pName,
                            onValueChange = { pName = it },
                            label = { Text("اسم المنتج") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = pPrice,
                            onValueChange = { pPrice = it },
                            label = { Text("سعر البيع (دج)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = pStock,
                            onValueChange = { pStock = it },
                            label = { Text("كمية المخزون الأولي") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = pCategory,
                            onValueChange = { pCategory = it },
                            label = { Text("الفئة (تصنيف المنتج)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = pReorder,
                            onValueChange = { pReorder = it },
                            label = { Text("حد التنبيه لنفاذ المخزون") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val priceVal = pPrice.toDoubleOrNull() ?: 0.0
                            val stockVal = pStock.toIntOrNull() ?: 0
                            val reorderVal = pReorder.toIntOrNull() ?: 5
                            if (pName.isNotBlank()) {
                                viewModel.addProduct(pName, priceVal, stockVal, pCategory.ifBlank { "عام" }, reorderVal)
                                // Reset fields
                                pName = ""
                                pPrice = ""
                                pStock = ""
                                pCategory = ""
                                pReorder = "5"
                                isAddingProduct = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RShopTheme.Primary)
                    ) {
                        Text("حفظ المنتج")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { isAddingProduct = false }) {
                        Text("إلغاء")
                    }
                },
                containerColor = RShopTheme.Surface,
                titleContentColor = RShopTheme.TextPrimary,
                textContentColor = RShopTheme.TextSecondary
            )
        }
    }
}

// --- SCREEN 4: ORDERS MANAGEMENT ---
@Composable
fun OrdersScreen(viewModel: AppViewModel) {
    val orders by viewModel.orders.collectAsState()
    val products by viewModel.products.collectAsState()

    var isAddingOrder by remember { mutableStateOf(false) }

    // New Order Fields
    var cName by remember { mutableStateOf("") }
    var cPhone by remember { mutableStateOf("") }
    var cAddress by remember { mutableStateOf("") }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var orderQty by remember { mutableStateOf("1") }
    var paymentMethod by remember { mutableStateOf("ccp") } // "ccp" or "wallet"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RShopTheme.Background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GradientHeader(
                title = "إدارة طلبات المشترين",
                subtitle = "تتبع فوري للطلبات، تغيير الحالات، وتوليد إشعارات WhatsApp ذكية"
            )

            if (orders.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Filled.List, contentDescription = null, tint = RShopTheme.TextSecondary, modifier = Modifier.size(60.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("لا توجد طلبات مسجلة بعد.", color = RShopTheme.TextPrimary, fontWeight = FontWeight.Bold)
                        Text("يمكنك الضغط على الزر أدناه لمحاكاة إنشاء طلب عميل جديد وتجربة تدفق الرسائل الذكية.", color = RShopTheme.TextSecondary, fontSize = 11.sp, textAlign = TextAlign.Center)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(orders) { order ->
                        RShopCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Status Pill
                                val (bgStatus, textStatus) = when (order.status) {
                                    "pending" -> Color(0xFFE65100).copy(alpha = 0.15f) to "قيد الانتظار"
                                    "processing" -> Color(0xFF1565C0).copy(alpha = 0.15f) to "قيد المعالجة"
                                    "shipped" -> Color(0xFFAD1457).copy(alpha = 0.15f) to "تم الشحن"
                                    else -> Color(0xFF2E7D32).copy(alpha = 0.15f) to "تم التوصيل"
                                }
                                val textCol = when (order.status) {
                                    "pending" -> Color(0xFFFF9800)
                                    "processing" -> Color(0xFF2196F3)
                                    "shipped" -> Color(0xFFE91E63)
                                    else -> Color(0xFF4CAF50)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(bgStatus)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(textStatus, color = textCol, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }

                                Text(
                                    text = "الطلب #${order.id}",
                                    color = RShopTheme.TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(text = "العميل: ${order.customerName}", color = RShopTheme.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
                            Text(text = "الهاتف: ${order.customerPhone}", color = RShopTheme.TextSecondary, fontSize = 12.sp, textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
                            Text(text = "العنوان: ${order.deliveryAddress}", color = RShopTheme.TextSecondary, fontSize = 11.sp, textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
                            Text(text = "الدفع: ${if (order.paymentMethod == "ccp") "بريدي موب (CCP)" else "المحفظة الإلكترونية"}", color = RShopTheme.Primary, fontSize = 11.sp, textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())

                            Divider(color = RShopTheme.TextSecondary.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 10.dp))

                            // Item List representation
                            val parsedItems = remember(order.itemsJson) {
                                val list = mutableListOf<Pair<String, String>>()
                                try {
                                    val jsonArr = JSONArray(order.itemsJson)
                                    for (i in 0 until jsonArr.length()) {
                                        val obj = jsonArr.getJSONObject(i)
                                        val name = obj.optString("name", "")
                                        val price = obj.optDouble("price", 0.0)
                                        val qty = obj.optInt("quantity", 1)
                                        val calculatedTotal = if (price > 0) "${(price * qty).toInt()} دج" else ""
                                        list.add(calculatedTotal to "$name × $qty")
                                    }
                                } catch (e: Exception) {
                                    list.add("" to order.itemsJson)
                                }
                                list
                            }

                            parsedItems.forEach { (priceStr, nameStr) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = priceStr, color = RShopTheme.TextPrimary, fontSize = 12.sp)
                                    Text(text = nameStr, color = RShopTheme.TextSecondary, fontSize = 12.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "الإجمالي: ${order.totalPrice.toInt()} دج",
                                    color = Color(0xFF00E676),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                // Action Buttons
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    // Status upgrade button
                                    if (order.status != "delivered") {
                                        Button(
                                            onClick = {
                                                val next = when (order.status) {
                                                    "pending" -> "processing"
                                                    "processing" -> "shipped"
                                                    else -> "delivered"
                                                }
                                                viewModel.updateOrderStatus(order, next)
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = RShopTheme.Primary),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = when (order.status) {
                                                    "pending" -> "معالجة الطلب"
                                                    "processing" -> "شحن المنتج"
                                                    else -> "تأكيد التسليم"
                                                },
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    // WhatsApp Alert manual trigger button
                                    Button(
                                        onClick = { viewModel.resendWhatsAppNotificationManual(order) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                                        ) {
                                        Icon(imageVector = Icons.Filled.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("إشعار واتساب", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Add Simulated Customer Order Button
        FloatingActionButton(
            onClick = { isAddingOrder = true },
            containerColor = RShopTheme.Primary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "محاكاة طلب عميل")
        }

        if (isAddingOrder) {
            AlertDialog(
                onDismissRequest = { isAddingOrder = false },
                title = { Text("محاكاة طلب عميل جديد لمتجرك", textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth()) },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = cName,
                            onValueChange = { cName = it },
                            label = { Text("اسم المشتري (العميل)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = cPhone,
                            onValueChange = { cPhone = it },
                            label = { Text("رقم هاتف المشتري") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = cAddress,
                            onValueChange = { cAddress = it },
                            label = { Text("عنوان التوصيل بالكامل") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text("اختر المنتج للطلب:", color = RShopTheme.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        if (products.isEmpty()) {
                            Text("الرجاء إضافة منتجات أولاً قبل إنشاء طلب!", color = RShopTheme.Danger, fontSize = 11.sp)
                        } else {
                            // Simple horizontal list selection
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                products.forEach { p ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (selectedProduct?.id == p.id) RShopTheme.Primary else RShopTheme.CardBg)
                                            .clickable { selectedProduct = p }
                                            .padding(10.dp)
                                    ) {
                                        Text(p.name, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        OutlinedTextField(
                            value = orderQty,
                            onValueChange = { orderQty = it },
                            label = { Text("الكمية المطلوبة") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text("طريقة الدفع:", color = RShopTheme.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .border(
                                        width = 1.5.dp,
                                        color = if (paymentMethod == "ccp") RShopTheme.Primary else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(RShopTheme.CardBg)
                                    .clickable { paymentMethod = "ccp" }
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("بريدي موب (CCP)", color = Color.White, fontSize = 11.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .border(
                                        width = 1.5.dp,
                                        color = if (paymentMethod == "wallet") RShopTheme.Primary else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(RShopTheme.CardBg)
                                    .clickable { paymentMethod = "wallet" }
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("المحفظة الرقمية", color = Color.White, fontSize = 11.sp)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val prod = selectedProduct
                            val qty = orderQty.toIntOrNull() ?: 1
                            if (cName.isNotBlank() && cPhone.isNotBlank() && prod != null) {
                                viewModel.createSampleOrder(cName, cPhone, listOf(prod to qty), cAddress, paymentMethod)
                                // Reset fields
                                cName = ""
                                cPhone = ""
                                cAddress = ""
                                selectedProduct = null
                                orderQty = "1"
                                isAddingOrder = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RShopTheme.Primary)
                    ) {
                        Text("إرسال ومحاكاة الطلب")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { isAddingOrder = false }) {
                        Text("إلغاء")
                    }
                },
                containerColor = RShopTheme.Surface,
                titleContentColor = RShopTheme.TextPrimary,
                textContentColor = RShopTheme.TextSecondary
            )
        }
    }
}

// --- SCREEN 5: STORE DESIGNER ---
@Composable
fun DesignerScreen(viewModel: AppViewModel) {
    val store by viewModel.merchantStore.collectAsState()

    var themeColorSelected by remember { mutableStateOf(store?.themeColor ?: "#FF6200EE") }
    var fontFamilySelected by remember { mutableStateOf(store?.fontFamily ?: "SansSerif") }
    var layoutSelected by remember { mutableStateOf(store?.layout ?: "grid") }
    var logoUrlText by remember { mutableStateOf(store?.logoUrl ?: "") }

    val colorOptions = listOf(
        "#FF6200EE" to "البنفسجي الملكي",
        "#FFFF5722" to "البرتقالي الناري",
        "#FF00E676" to "الأخضر المضيء",
        "#FF2979FF" to "الأزرق المشرق",
        "#FFE040FB" to "الوردي الفاتن"
    )

    val fontOptions = listOf(
        "SansSerif" to "العادي الحديث (Sans)",
        "Serif" to "الكلاسيكي المذيل (Serif)",
        "Monospace" to "الرقمي الدقيق (Mono)"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RShopTheme.Background)
            .verticalScroll(rememberScrollState())
    ) {
        GradientHeader(
            title = "مصمم المتجر المرن (SaaS Builder)",
            subtitle = "قم بسحب وتخصيص هوية متجرك وعاين التغييرات لحظياً"
        )

        Column(modifier = Modifier.padding(16.dp)) {
            // Configuration Controls
            RShopCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "🎨 لوحة أدوات مصمم المتاجر (Designer Console)",
                    color = RShopTheme.TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Theme color selection
                Text("لون المتجر الأساسي:", color = RShopTheme.TextSecondary, fontSize = 12.sp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    colorOptions.forEach { (hex, name) ->
                        val colorParsed = Color(android.graphics.Color.parseColor(hex))
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(colorParsed)
                                .border(
                                    width = if (themeColorSelected == hex) 3.dp else 0.dp,
                                    color = Color.White,
                                    shape = CircleShape
                                )
                                .clickable { themeColorSelected = hex },
                            contentAlignment = Alignment.Center
                        ) {
                            if (themeColorSelected == hex) {
                                Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Font Family Options
                Text("نوع خط متجرك:", color = RShopTheme.TextSecondary, fontSize = 12.sp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    fontOptions.forEach { (fontKey, displayName) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (fontFamilySelected == fontKey) RShopTheme.Primary else RShopTheme.Background)
                                .clickable { fontFamilySelected = fontKey }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(displayName, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Layout Selector
                Text("طريقة عرض المنتجات للعملاء:", color = RShopTheme.TextSecondary, fontSize = 12.sp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (layoutSelected == "grid") RShopTheme.Primary else RShopTheme.Background)
                            .clickable { layoutSelected = "grid" }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.List, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("شبكة مربعات (Grid)", color = Color.White, fontSize = 11.sp)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (layoutSelected == "list") RShopTheme.Primary else RShopTheme.Background)
                            .clickable { layoutSelected = "list" }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.List, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("قائمة متتالية (List)", color = Color.White, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = logoUrlText,
                    onValueChange = { logoUrlText = it },
                    label = { Text("رابط لوجو الشعار الخاص بالمتجر") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RShopTheme.Primary, focusedLabelColor = RShopTheme.Primary),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.saveStoreConfig(themeColorSelected, fontFamilySelected, logoUrlText, layoutSelected)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RShopTheme.Primary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("حفظ ونشر التعديلات فوراً", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Real-time Customer Webapp preview representation ---
            Text(
                text = "📱 معاينة حية لمتجر العميل (Live Preview):",
                color = RShopTheme.TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Web Frame container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 4.dp, color = Color(0xFF2C2F48), shape = RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF0F101A)) // Web preview body
            ) {
                val demoThemeColor = Color(android.graphics.Color.parseColor(themeColorSelected))
                val demoFont = when (fontFamilySelected) {
                    "Serif" -> FontFamily.Serif
                    "Monospace" -> FontFamily.Monospace
                    else -> FontFamily.SansSerif
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Browser Bar representation
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1E2133))
                            .padding(vertical = 6.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.size(8.dp).background(Color(0xFFEF5350), CircleShape))
                            Box(modifier = Modifier.size(8.dp).background(Color(0xFFFFD54F), CircleShape))
                            Box(modifier = Modifier.size(8.dp).background(Color(0xFF69F0AE), CircleShape))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF0C0D17))
                                .padding(vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "https://${store?.subdomain ?: "shop"}.rshop.com",
                                color = RShopTheme.TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Store Customer webapp view
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        // Customer Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = null, tint = demoThemeColor)
                            Text(
                                text = store?.subdomain?.uppercase() ?: "MY STORE",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = demoFont
                            )
                            if (logoUrlText.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(demoThemeColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("L", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Icon(imageVector = Icons.Filled.Menu, contentDescription = null, tint = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Banner Widget
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(demoThemeColor, demoThemeColor.copy(alpha = 0.6f))
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Column {
                                Text(
                                    text = "تنزيلات حصرية ومميزة! 🔥",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = demoFont
                                )
                                Text(
                                    text = "خدمة التوصيل السريع لـ 58 ولاية والدفع عند الاستلام.",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp,
                                    fontFamily = demoFont,
                                    lineHeight = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Product list widget grid simulation
                        Text(
                            text = "منتجاتنا الأكثر طلباً:",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = demoFont,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        if (layoutSelected == "grid") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                repeat(2) { index ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFF191C2D))
                                            .padding(8.dp)
                                    ) {
                                        Column {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(60.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(demoThemeColor.copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(imageVector = Icons.Filled.Home, contentDescription = null, tint = demoThemeColor)
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = if (index == 0) "منتج أ" else "منتج ب",
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = demoFont
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(18.dp)
                                                        .background(demoThemeColor, CircleShape),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(imageVector = Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                                                }
                                                Text("3,500 دج", color = demoThemeColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                repeat(2) { index ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFF191C2D))
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .background(demoThemeColor, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(imageVector = Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("3,500 دج", color = demoThemeColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(if (index == 0) "منتج أ" else "منتج ب", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = demoFont)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(demoThemeColor.copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(imageVector = Icons.Filled.Home, contentDescription = null, tint = demoThemeColor, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Footer
                        Text(
                            text = "حقوق الطبع والنشر © 2026. بدعم من منصة R-Shop",
                            color = RShopTheme.TextSecondary.copy(alpha = 0.5f),
                            fontSize = 8.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = demoFont
                        )
                    }
                }
            }
        }
    }
}

// --- SCREEN 6: PLATFORM MASTER ADMIN PANEL ---
@Composable
fun AdminScreen(viewModel: AppViewModel) {
    val users by viewModel.allUsers.collectAsState()
    val stores by viewModel.allStores.collectAsState()

    val totalActiveStores = users.filter { it.role == "merchant" && it.subscriptionStatus == "active" }.size
    val totalRevenue = users.filter { it.role == "merchant" && it.subscriptionStatus == "active" }.sumOf {
        if (it.plan == "pro") 3000.0 else 1000.0
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(RShopTheme.Background),
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {
        item {
            GradientHeader(
                title = "لوحة التحكم الرئيسية للمنصة (Master Admin)",
                subtitle = "تحكم شامل في المتاجر والمشتركين، مراقبة الإيرادات وحظر الحسابات المخالفة",
                actionIcon = Icons.Filled.Close,
                onActionClick = { viewModel.logout() }
            )
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {
                // Platform KPIs
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        title = "الإيرادات الشهرية المقدرة",
                        value = "${totalRevenue.toInt()}",
                        unit = "دج",
                        icon = Icons.Filled.Home,
                        gradient = RShopTheme.PrimaryGradient,
                        modifier = Modifier.weight(1.3f)
                    )
                    StatCard(
                        title = "المتاجر النشطة",
                        value = "$totalActiveStores",
                        icon = Icons.Filled.Home,
                        gradient = RShopTheme.AccentGradient,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // User list card
                Text(
                    text = "📋 قائمة المتاجر والتجار المسجلين:",
                    color = RShopTheme.TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                if (users.none { it.role == "merchant" }) {
                    RShopCard(modifier = Modifier.fillMaxWidth()) {
                        Text("لا يوجد تجار مسجلين حالياً.", color = RShopTheme.TextSecondary, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                } else {
                    users.filter { it.role == "merchant" }.forEach { u ->
                        val associatedStore = stores.find { it.ownerId == u.id }
                        RShopCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Toggle Suspend Action
                                val isSuspended = u.subscriptionStatus == "suspended"
                                Button(
                                    onClick = { viewModel.toggleUserStoreSubscription(u) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSuspended) Color(0xFF2E7D32) else RShopTheme.Danger
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                                ) {
                                    Text(if (isSuspended) "تفعيل المتجر" else "تجميد الحساب", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = u.fullName, color = RShopTheme.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "النطاق: ${associatedStore?.subdomain ?: ""}.rshop.com", color = RShopTheme.TextSecondary, fontSize = 11.sp)
                                    Text(
                                        text = "الخطة: ${if (u.plan == "pro") "المتقدمة" else "الأساسية"} | الحالة: ${if (isSuspended) "مجمد ❌" else "نشط ✅"}",
                                        color = if (isSuspended) RShopTheme.Danger else Color(0xFF00E676),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
