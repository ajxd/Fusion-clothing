const express = require("express");
const stripe = require("stripe")('sk_test_51Nb96uINWXfLdDEdGjnMi5cNeNW9KPTZW9i8JAGDb9mUe0aeIClRhkL6eb9Dfmck2CCq97q2xNwmV2kOYWrPaMjy00cfwdyXEg');
const admin = require("firebase-admin");
const serviceAccount = require('C:/Users/ajain/OneDrive/Desktop/FINAL PROJECT/ajai-kamaraj-application2-firebase-adminsdk-ydx29-069c96b45c.json');
 // Replace with the path to your downloaded key

const app = express();

// Initialize Firebase Admin with the downloaded service account key
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://ajai-kamaraj-application2-default-rtdb.firebaseio.com'
  
});

const db = admin.database();

// Middleware to serve static files and parse JSON requests
app.use(express.static("public"));
app.use(express.json());

// Fetch cart items from Firebase
const getCartItems = async () => {
  return new Promise((resolve, reject) => {
    const ref = db.ref("carts");
    ref.once("value", (snapshot) => {
      const cartItems = snapshot.val();
      if (!cartItems) reject("Cart is empty");
      resolve(cartItems);
    });
  });
};

const calculateOrderAmount = async () => {
  let total = 0;
  const items = await getCartItems();
  
  for (let itemId in items) {
    const item = items[itemId];
    if (!item.price || !item.quantity) {
      throw new Error("Item missing price or quantity");
    }
    total += item.price * item.quantity;
  }

  if (isNaN(total) || total <= 0) {
    throw new Error("Invalid total amount");
  }

  return Math.round(total * 100);  // Convert to smallest currency unit (cents)
};

app.get("/", (req, res) => {
  res.send("Welcome to the Stripe Server!");
});

app.post("/create-payment-intent", async (req, res) => {
  try {
    const amount = await calculateOrderAmount();

    const paymentIntent = await stripe.paymentIntents.create({
      amount: amount,
      currency: "cad",
      automatic_payment_methods: {
        enabled: true,
      },
    });

    res.send({
      clientSecret: paymentIntent.client_secret,
    });
  } catch (error) {
    console.error("Error creating payment intent:", error);
    res.status(500).send({ error: error.message });
  }
});

app.listen(4242, () => {
  console.log("Node server listening on port 4242!");
});
