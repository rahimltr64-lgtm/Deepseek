# 🌐 دليل نشر وتشغيل منصة R-Shop SaaS على الخادم (Production Deployment Guide)

يرحب بك هذا الدليل كمهندس أول، ليشرح لك بالتفصيل كيفية نشر وتشغيل التطبيق بالكامل (Node.js API & React Web Client) على خادم VPS خاص بك (مثل DigitalOcean أو Hetzner أو AWS).

---

## 🛠️ المتطلبات الأساسية (Prerequisites)

قبل البدء، تأكد من تثبيت الأدوات التالية على الخادم (نظام Ubuntu 22.04 LTS أو أحدث):

1. **Node.js** (إصدار v18 أو v20)
2. **MongoDB** (نسخة مجتمعية أو سحابية مثل MongoDB Atlas)
3. **Nginx** (لإدارة توجيه الروابط وتثبيت شهادات SSL)
4. **PM2** (لإبقاء السيرفر نشطاً في الخلفية بشكل دائم)

---

## 📂 أولاً: إعداد السيرفر (Backend Setup)

1. **نقل الكود المصدري**:
   قم بنقل محتويات مجلد `/r-shop-saas/backend` إلى المسار المطلوبة على سيرفرك:
   ```bash
   mkdir -p /var/www/r-shop-saas/backend
   # ثم انسخ الملفات إلى هناك
   ```

2. **تثبيت الحزم (Dependencies)**:
   ```bash
   cd /var/www/r-shop-saas/backend
   npm install --production
   ```

3. **إعداد ملف البيئة (.env)**:
   قم بإنشاء وتعديل ملف الإعدادات:
   ```bash
   nano .env
   ```
   املأ البيانات التالية ببياناتك الحقيقية:
   ```env
   PORT=5000
   MONGODB_URI=mongodb+srv://admin:secure_password@cluster0.mongodb.net/rshop_prod
   JWT_SECRET=your_jwt_production_secret_382891
   GEMINI_API_KEY=AIzaSyA... (مفتاح جوجل جيميناي الفعلي)
   ```

4. **تشغيل السيرفر بالخلفية عبر PM2**:
   ```bash
   npm install -g pm2
   pm2 start server.js --name "rshop-backend"
   pm2 save
   pm2 startup
   ```

---

## 🎨 ثانياً: إعداد واجهة المستخدم (React Frontend)

1. **بناء النسخة النهائية (Production Build)**:
   على جهازك المحلي أو داخل سيرفر البناء، توجه إلى مجلد الواجهة:
   ```bash
   cd /r-shop-saas/frontend
   npm install
   npm run build
   ```
   سيقوم المترجم بإنشاء مجلد باسم `dist` يحتوي على ملفات HTML/CSS/JS جاهزة وسريعة للغاية للعملاء.

2. **رفع الملفات لخادم الويب**:
   انسخ محتويات مجلد `dist` إلى المسار المخصص له في الخادم:
   ```bash
   mkdir -p /var/www/r-shop-saas/frontend/dist
   # ارفع الملفات إلى هذا المجلد
   ```

---

## 🔒 ثالثاً: إعداد Nginx والتوجيه الذكي وشهادة SSL

1. **إنشاء ملف إعدادات جديد في Nginx**:
   ```bash
   sudo nano /etc/nginx/sites-available/r-shop.com
   ```

2. **الصق الإعداد التالي (مع تغيير اسم الدومين بـ الدومين الخاص بك)**:
   ```nginx
   server {
       listen 80;
       server_name r-shop.com *.r-shop.com; # دعم النطاقات الفرعية تلقائياً (Wildcard Subdomains)

       # واجهة المستخدم الرئيسية (Vite Production Build)
       location / {
           root /var/www/r-shop-saas/frontend/dist;
           try_files $uri $uri/ /index.html;
       }

       # توجيه طلبات الـ API إلى سيرفر Node.js
       location /api/ {
           proxy_pass http://127.0.0.1:5000/;
           proxy_http_version 1.1;
           proxy_set_header Upgrade $http_upgrade;
           proxy_set_header Connection 'upgrade';
           proxy_set_header Host $host;
           proxy_cache_bypass $http_upgrade;
       }
   }
   ```

3. **تفعيل الموقع وإعادة تشغيل Nginx**:
   ```bash
   sudo ln -s /etc/nginx/sites-available/r-shop.com /etc/nginx/sites-enabled/
   sudo nginx -t
   sudo systemctl restart nginx
   ```

4. **تثبيت شهادة حماية مجانية SSL عبر Let's Encrypt**:
   ```bash
   sudo apt install certbot python3-certbot-nginx
   sudo certbot --nginx -d r-shop.com -d *.r-shop.com
   ```

---

## 📈 رابعاً: مراقبة الأداء وقاعدة البيانات

- **لمراقبة سجل السيرفر والطلبات لحظياً**:
  ```bash
  pm2 logs rshop-backend
  ```
- **لمشاهدة استهلاك المعالج والذاكرة**:
  ```bash
  pm2 monit
  ```

🎉 مبارك! الآن موقع SaaS الخاص بك جاهز للعمل بشكل متكامل، ويمكن للتجار إنشاء متاجرهم في ثوانٍ والحصول على لوحات تحكم متطورة مرتبطة بواتساب والذكاء الاصطناعي مباشرة من متصفحاتهم.
