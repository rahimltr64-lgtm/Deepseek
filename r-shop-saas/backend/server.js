const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 5000;

// Middleware
app.use(helmet());
app.use(cors());
app.use(express.json());

// Basic Rate Limiter
const limiter = rateLimit({
  windowMs: 15 * 60 * 1000, // 15 minutes
  max: 100 // limit each IP to 100 requests per windowMs
});
app.use(limiter);

// DB Connection
mongoose.connect(process.env.MONGODB_URI)
  .then(() => console.log('Successfully connected to MongoDB Database.'))
  .catch(err => console.error('Database connection error:', err));

// Routes
app.get('/', (req, res) => {
  res.json({
    status: 'success',
    message: 'Welcome to R-Shop SaaS Production Backend API',
    version: '1.0.0',
    timestamp: new Date()
  });
});

// Import controllers and models
// In production, these are routed as separate files:
// app.use('/api/auth', require('./routes/auth'));
// app.use('/api/stores', require('./routes/stores'));
// app.use('/api/products', require('./routes/products'));
// app.use('/api/orders', require('./routes/orders'));
// app.use('/api/subscriptions', require('./routes/subscriptions'));

app.listen(PORT, () => {
  console.log(`R-Shop SaaS Server running on port ${PORT}`);
});
