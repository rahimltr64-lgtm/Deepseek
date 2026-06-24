const mongoose = require('mongoose');

// User Schema (Merchant / Admin)
const UserSchema = new mongoose.Schema({
  fullName: { type: String, required: true },
  email: { type: String, required: true, unique: true, index: true },
  password: { type: String, required: true },
  phone: { type: String, required: true },
  role: { type: String, enum: ['merchant', 'admin'], default: 'merchant' },
  plan: { type: String, enum: ['basic', 'pro'], default: 'basic' },
  status: { type: String, enum: ['active', 'suspended'], default: 'active' },
  createdAt: { type: Date, default: Date.now }
});

// Store Schema
const StoreSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, index: true },
  subdomain: { type: String, required: true, unique: true, index: true },
  storeName: { type: String, required: true },
  bannerUrl: { type: String, default: '' },
  whatsappNumber: { type: String, default: '' },
  themeColor: { type: String, default: '#E31C25' }, // Custom brand styling
  layoutGrid: { type: String, default: 'grid' }, // 'grid' or 'list'
  isCustomDomainEnabled: { type: Boolean, default: false },
  createdAt: { type: Date, default: Date.now }
});

// Product Schema
const ProductSchema = new mongoose.Schema({
  storeId: { type: mongoose.Schema.Types.ObjectId, ref: 'Store', required: true, index: true },
  name: { type: String, required: true },
  price: { type: Number, required: true },
  originalPrice: { type: Number, default: 0 },
  image: { type: String, default: '' },
  description: { type: String, default: '' },
  inventory: { type: Number, default: 10 },
  category: { type: String, default: 'عام' },
  isFeatured: { type: Boolean, default: false },
  createdAt: { type: Date, default: Date.now }
});

// Order Schema
const OrderSchema = new mongoose.Schema({
  storeId: { type: mongoose.Schema.Types.ObjectId, ref: 'Store', required: true, index: true },
  customerName: { type: String, required: true },
  customerPhone: { type: String, required: true },
  customerAddress: { type: String, required: true },
  state: { type: String, required: true }, // Algerian Wilaya (e.g. Algiers, Oran)
  items: [{
    productId: { type: mongoose.Schema.Types.ObjectId, ref: 'Product' },
    name: { type: String, required: true },
    price: { type: Number, required: true },
    quantity: { type: Number, required: true }
  }],
  totalAmount: { type: Number, required: true },
  status: { type: String, enum: ['pending', 'confirmed', 'shipped', 'delivered', 'cancelled'], default: 'pending' },
  aiPredictionFeedback: { type: String, default: '' }, // Prediction of purchase seriousness
  createdAt: { type: Date, default: Date.now }
});

// Subscription Schema (Payments Log)
const SubscriptionSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, index: true },
  plan: { type: String, enum: ['basic', 'pro'], required: true },
  price: { type: Number, required: true },
  paymentMethod: { type: String, enum: ['ccp', 'baridimob', 'admin_override'], default: 'ccp' },
  receiptImageUrl: { type: String, default: '' }, // CCP Receipt Proof
  status: { type: String, enum: ['pending', 'approved', 'rejected'], default: 'pending' },
  expiresAt: { type: Date, required: true },
  createdAt: { type: Date, default: Date.now }
});

module.exports = {
  User: mongoose.model('User', UserSchema),
  Store: mongoose.model('Store', StoreSchema),
  Product: mongoose.model('Product', ProductSchema),
  Order: mongoose.model('Order', OrderSchema),
  Subscription: mongoose.model('Subscription', SubscriptionSchema)
};
