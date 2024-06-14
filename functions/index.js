const {onRequest} = require("firebase-functions/v2/https");
const express = require("express");
const fetch = require("node-fetch");
const app = express();

app.get("/auth/instagram/callback", async (req, res) => {
  const authorizationCode = req.query.code;
  if (authorizationCode) {
    try {
      const response = await fetch("https://api.instagram.com/oauth/access_token", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: new URLSearchParams({
          client_id: "7610728705678532",
          client_secret: "4d21385bbe7da1a18b906d527c8d5f0d",
          grant_type: "authorization_code",
          redirect_uri: "https://vinilapp-c328e.web.app/auth/instagram/callback",
          code: authorizationCode,
        }),
      });

      const data = await response.json();

      if (data.access_token) {
        res.json({access_token: data.access_token});
      } else {
        res.status(400).json({
          error: "Failed to get access token",
          details: data,
        });
      }
    } catch (error) {
      res.status(500).json({
        error: "Internal Server Error",
        details: error.message,
      });
    }
  } else {
    res.status(400).send("Authorization code not found");
  }
});

exports.app = onRequest(app);
