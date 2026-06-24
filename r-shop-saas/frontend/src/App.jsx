import React, { useState } from 'react';
import { 
  ShoppingBag, Users, Layers, TrendingUp, Settings, LogOut, 
  Plus, Edit2, Trash2, Check, RefreshCw, Smartphone, Monitor, Globe
} from 'lucide-react';

export default function App() {
  const [currentTab, setCurrentTab] = useState('dashboard');
  const [storeName, setStoreName] = useState('متجر أحمد التجريبي');
  const [themeColor, setThemeColor] = useState('#E31C25');
  const [layout, setLayout] = useState('grid');
  
  const [products, setProducts] = useState([
    { id: 1, name: 'هاتف ذكي برو 5G', price: 89000, stock: 15, category: 'إلكترونيات' },
    { id: 2, name: 'سماعة رأس لاسلكية إلغاء الضوضاء', price: 12500, stock: 4, category: 'ملحقات' },
    { id: 3, name: 'ساعة يد ذكية مقاومة للماء', price: 18000, stock: 20, category: 'إلكترونيات' }
  ]);

  const [orders, setOrders] = useState([
    { id: 'ORD-5821', customer: 'جمال قاسم', phone: '0555123456', amount: 101500, status: 'pending', Wilaya: 'الجزائر العاصمة' },
    { id: 'ORD-9831', customer: 'أمينة بن يوسف', phone: '0661987654', amount: 18000, status: 'confirmed', Wilaya: 'وهران' }
  ]);

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 font-sans flex" dir="rtl">
      {/* Sidebar Navigation */}
      <aside className="w-64 bg-slate-900 border-l border-slate-800 flex flex-col justify-between p-6">
        <div>
          <div className="flex items-center gap-3 mb-8">
            <div className="w-10 h-10 rounded-xl bg-red-600 flex items-center justify-center font-bold text-white text-xl shadow-lg shadow-red-500/20">
              R
            </div>
            <div>
              <h1 className="text-lg font-black tracking-tight text-white">R-Shop SaaS</h1>
              <p className="text-xs text-slate-400">لوحة تحكم التاجر المحدثة</p>
            </div>
          </div>

          <nav className="space-y-2">
            {[
              { id: 'dashboard', label: 'الرئيسية والإحصائيات', icon: TrendingUp },
              { id: 'products', label: 'إدارة المنتجات', icon: ShoppingBag },
              { id: 'orders', label: 'الطلبات النشطة', icon: Layers },
              { id: 'designer', label: 'المصمم المرئي', icon: Settings }
            ].map((tab) => {
              const Icon = tab.icon;
              return (
                <button
                  key={tab.id}
                  onClick={() => setCurrentTab(tab.id)}
                  className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-bold transition-all ${
                    currentTab === tab.id 
                      ? 'bg-red-600 text-white shadow-lg shadow-red-500/20' 
                      : 'text-slate-400 hover:bg-slate-800 hover:text-white'
                  }`}
                >
                  <Icon className="w-5 h-5" />
                  {tab.label}
                </button>
              );
            })}
          </nav>
        </div>

        <button className="flex items-center gap-3 px-4 py-3 text-slate-400 hover:text-red-500 rounded-xl text-sm font-bold hover:bg-red-500/10 transition-all">
          <LogOut className="w-5 h-5" />
          تسجيل الخروج
        </button>
      </aside>

      {/* Main Content Area */}
      <main className="flex-1 p-8 overflow-y-auto">
        <header className="flex justify-between items-center mb-8 pb-6 border-b border-slate-800">
          <div>
            <h2 className="text-2xl font-extrabold text-white">
              {currentTab === 'dashboard' && 'لوحة المعلومات والتحليلات'}
              {currentTab === 'products' && 'إدارة المنتجات والمخزون'}
              {currentTab === 'orders' && 'طلبات العملاء وإشعارات التوصيل'}
              {currentTab === 'designer' && 'المصمم المرئي للمتجر'}
            </h2>
            <p className="text-slate-400 text-sm mt-1">نطاق متجرك الحالي: <span className="text-red-500 underline font-mono">ahmed.rshop.com</span></p>
          </div>
          
          <div className="flex items-center gap-4">
            <span className="px-3 py-1.5 rounded-full bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-xs font-bold flex items-center gap-1.5">
              <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
              الاشتراك نشط (خطة Pro)
            </span>
          </div>
        </header>

        {/* Dashboard Tab */}
        {currentTab === 'dashboard' && (
          <div className="space-y-8">
            <div className="grid grid-cols-4 gap-6">
              {[
                { label: 'إجمالي المبيعات', value: '119,500 دج', color: 'border-emerald-500/30 text-emerald-400' },
                { label: 'الطلبات النشطة', value: '2 طلب', color: 'border-blue-500/30 text-blue-400' },
                { label: 'المنتجات المعروضة', value: '3 منتجات', color: 'border-purple-500/30 text-purple-400' },
                { label: 'معدل التحويل (AI)', value: '87.4%', color: 'border-amber-500/30 text-amber-400' }
              ].map((stat, idx) => (
                <div key={idx} className={`bg-slate-900 border ${stat.color} p-6 rounded-2xl`}>
                  <p className="text-slate-400 text-xs font-bold">{stat.label}</p>
                  <p className="text-2xl font-black mt-2">{stat.value}</p>
                </div>
              ))}
            </div>

            {/* AI Sales Forecast Card */}
            <div className="bg-gradient-to-r from-red-950/40 to-slate-900 border border-red-500/20 p-6 rounded-2xl flex items-center justify-between">
              <div>
                <span className="px-2.5 py-1 rounded bg-red-500/10 border border-red-500/20 text-red-400 text-xs font-bold">تنبؤ جيميناي الذكي AI 🧠</span>
                <h3 className="text-lg font-bold text-white mt-3">توقع الطلبات المرتفعة لعطلة نهاية الأسبوع</h3>
                <p className="text-slate-400 text-sm mt-1 max-w-xl">استناداً لبيانات المبيعات التاريخية في الجزائر، ننصحك برفع مخزون منتج "سماعة رأس لاسلكية" بمقدار 5 قطع لتلبية الطلب المتزايد المتوقع بنسبة 45%.</p>
              </div>
              <button className="bg-red-600 hover:bg-red-500 text-white font-bold text-sm px-6 py-3 rounded-xl shadow-lg shadow-red-600/20 transition-all">تحديث المخزن الآن</button>
            </div>
          </div>
        )}

        {/* Products Tab */}
        {currentTab === 'products' && (
          <div className="bg-slate-900 rounded-2xl border border-slate-800 overflow-hidden">
            <div className="p-6 border-b border-slate-800 flex justify-between items-center">
              <h3 className="font-bold text-white">قائمة المنتجات المتاحة</h3>
              <button className="bg-red-600 hover:bg-red-500 text-white font-bold text-sm px-4 py-2 rounded-lg flex items-center gap-2">
                <Plus className="w-4 h-4" /> إضافة منتج جديد
              </button>
            </div>
            <table className="w-full text-right">
              <thead>
                <tr className="border-b border-slate-800 text-slate-400 text-xs">
                  <th className="p-4">اسم المنتج</th>
                  <th className="p-4">الفئة</th>
                  <th className="p-4">السعر</th>
                  <th className="p-4">المخزون المتبقي</th>
                  <th className="p-4">الإجراءات</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-800 text-sm">
                {products.map((p) => (
                  <tr key={p.id} className="hover:bg-slate-800/30">
                    <td className="p-4 font-bold text-white">{p.name}</td>
                    <td className="p-4 text-slate-400">{p.category}</td>
                    <td className="p-4 font-bold text-red-500">{p.price.toLocaleString()} دج</td>
                    <td className="p-4">
                      <span className={`px-2.5 py-1 rounded text-xs font-bold ${p.stock <= 5 ? 'bg-amber-500/10 border border-amber-500/25 text-amber-400' : 'bg-slate-800 text-slate-300'}`}>
                        {p.stock} قطعة {p.stock <= 5 && '(مخزون منخفض!)'}
                      </span>
                    </td>
                    <td className="p-4 flex gap-2">
                      <button className="p-2 bg-slate-800 hover:bg-slate-700 rounded"><Edit2 className="w-4 h-4 text-slate-300" /></button>
                      <button className="p-2 bg-slate-800 hover:bg-red-500/20 rounded"><Trash2 className="w-4 h-4 text-red-500" /></button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* Orders Tab */}
        {currentTab === 'orders' && (
          <div className="bg-slate-900 rounded-2xl border border-slate-800 overflow-hidden">
            <div className="p-6 border-b border-slate-800 flex justify-between items-center">
              <h3 className="font-bold text-white">تتبع طلبات التجارة الإلكترونية</h3>
              <button className="bg-slate-800 hover:bg-slate-700 text-slate-300 font-bold text-sm px-4 py-2 rounded-lg flex items-center gap-2">
                <RefreshCw className="w-4 h-4" /> تحديث الطلبات
              </button>
            </div>
            <table className="w-full text-right">
              <thead>
                <tr className="border-b border-slate-800 text-slate-400 text-xs">
                  <th className="p-4">رقم الطلب</th>
                  <th className="p-4">اسم الزبون</th>
                  <th className="p-4">الولاية</th>
                  <th className="p-4">الإجمالي</th>
                  <th className="p-4">الحالة</th>
                  <th className="p-4">إرسال تنبيه واتساب</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-800 text-sm">
                {orders.map((o) => (
                  <tr key={o.id} className="hover:bg-slate-800/30">
                    <td className="p-4 font-mono font-bold text-white">{o.id}</td>
                    <td className="p-4">
                      <div>
                        <p className="font-bold text-white">{o.customer}</p>
                        <p className="text-xs text-slate-400">{o.phone}</p>
                      </div>
                    </td>
                    <td className="p-4 text-slate-300">{o.Wilaya}</td>
                    <td className="p-4 font-bold text-red-500">{o.amount.toLocaleString()} دج</td>
                    <td className="p-4">
                      <span className={`px-2.5 py-1 rounded text-xs font-bold ${o.status === 'pending' ? 'bg-amber-500/10 text-amber-400 border border-amber-500/20' : 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20'}`}>
                        {o.status === 'pending' ? 'قيد الانتظار' : 'مؤكد'}
                      </span>
                    </td>
                    <td className="p-4">
                      <button className="px-3 py-1.5 bg-emerald-600 hover:bg-emerald-500 text-white rounded-lg text-xs font-bold flex items-center gap-1.5">
                        إرسال إشعار تأكيد 💬
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* Designer Tab */}
        {currentTab === 'designer' && (
          <div className="grid grid-cols-3 gap-8">
            <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 space-y-6">
              <h3 className="font-black text-lg text-white mb-4">أدوات تعديل مظهر المتجر</h3>
              
              <div>
                <label className="text-xs text-slate-400 font-bold block mb-2">اسم المتجر</label>
                <input 
                  type="text" 
                  value={storeName} 
                  onChange={(e) => setStoreName(e.target.value)} 
                  className="w-full bg-slate-950 border border-slate-800 rounded-xl px-4 py-2.5 text-sm text-white focus:outline-none focus:border-red-500"
                />
              </div>

              <div>
                <label className="text-xs text-slate-400 font-bold block mb-2">لون السمة الأساسية (Theme Color)</label>
                <div className="flex gap-2">
                  {['#E31C25', '#2563EB', '#059669', '#7C3AED', '#D97706'].map((color) => (
                    <button 
                      key={color}
                      onClick={() => setThemeColor(color)}
                      className={`w-8 h-8 rounded-full border-2 transition-transform ${themeColor === color ? 'border-white scale-110' : 'border-transparent'}`}
                      style={{ backgroundColor: color }}
                    />
                  ))}
                </div>
              </div>

              <div>
                <label className="text-xs text-slate-400 font-bold block mb-2">طريقة عرض المنتجات</label>
                <div className="grid grid-cols-2 gap-3">
                  <button 
                    onClick={() => setLayout('grid')} 
                    className={`p-3 border rounded-xl text-xs font-bold ${layout === 'grid' ? 'border-red-500 bg-red-500/10 text-white' : 'border-slate-800 text-slate-400'}`}
                  >
                    شبكة مربعات
                  </button>
                  <button 
                    onClick={() => setLayout('list')} 
                    className={`p-3 border rounded-xl text-xs font-bold ${layout === 'list' ? 'border-red-500 bg-red-500/10 text-white' : 'border-slate-800 text-slate-400'}`}
                  >
                    قائمة متتالية
                  </button>
                </div>
              </div>

              <button className="w-full bg-red-600 hover:bg-red-500 text-white font-bold py-3 rounded-xl shadow-lg transition-all">
                حفظ ونشر التعديلات فوراً
              </button>
            </div>

            {/* Simulated Desktop Preview */}
            <div className="col-span-2 bg-slate-950 border border-slate-800 rounded-2xl overflow-hidden shadow-2xl flex flex-col h-[500px]">
              <div className="bg-slate-900 px-4 py-2 flex items-center justify-between border-b border-slate-800 text-xs text-slate-400">
                <div className="flex gap-1.5">
                  <span className="w-2.5 h-2.5 rounded-full bg-red-500"></span>
                  <span className="w-2.5 h-2.5 rounded-full bg-amber-500"></span>
                  <span className="w-2.5 h-2.5 rounded-full bg-green-500"></span>
                </div>
                <div className="bg-slate-950 px-4 py-1.5 rounded-md w-72 text-center font-mono select-none">
                  https://ahmed-store.rshop.com
                </div>
                <div className="flex gap-2">
                  <Smartphone className="w-4 h-4 cursor-pointer hover:text-white" />
                  <Monitor className="w-4 h-4 cursor-pointer text-red-500" />
                </div>
              </div>

              {/* Live Render Preview */}
              <div className="flex-1 bg-slate-900 p-8 flex flex-col justify-between overflow-y-auto">
                <div>
                  <div className="flex items-center justify-between mb-8 pb-4 border-b border-slate-800">
                    <span className="text-xl font-black text-white" style={{ fontFamily: 'sans-serif' }}>
                      {storeName}
                    </span>
                    <ShoppingBag className="w-6 h-6" style={{ color: themeColor }} />
                  </div>

                  <div className={`grid gap-4 ${layout === 'grid' ? 'grid-cols-2' : 'grid-cols-1'}`}>
                    {products.map((p) => (
                      <div key={p.id} className="bg-slate-950 border border-slate-800/60 p-4 rounded-xl flex justify-between items-center">
                        <div>
                          <h4 className="font-bold text-sm text-slate-100">{p.name}</h4>
                          <span className="text-xs font-bold mt-2 inline-block" style={{ color: themeColor }}>
                            {p.price.toLocaleString()} دج
                          </span>
                        </div>
                        <button 
                          className="px-3 py-1.5 rounded-lg text-xs font-bold text-white flex items-center gap-1"
                          style={{ backgroundColor: themeColor }}
                        >
                          شراء الآن
                        </button>
                      </div>
                    ))}
                  </div>
                </div>

                <p className="text-center text-[10px] text-slate-500 mt-12">صنع بحب باستخدام R-Shop SaaS</p>
              </div>
            </div>
          </div>
        )}
      </main>
    </div>
  );
}
