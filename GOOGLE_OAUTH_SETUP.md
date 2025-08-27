# Google OAuth Setup Instructions

## 🔧 **Required Configuration Steps:**

### 1. **Google Cloud Console Setup:**
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable **Google Identity Services API**
4. Go to **"Credentials"** → **"Create Credentials"** → **"OAuth 2.0 Client IDs"**
5. Choose **"Web application"**

### 2. **OAuth Configuration:**
**Application type:** Web application

**Authorized JavaScript origins:**
```
http://localhost:5173
```

**Authorized redirect URIs:**
```
http://localhost:5173
```

### 3. **Get Your Credentials:**
After creating, you'll get:
- **Client ID** (looks like: `1234567890-abcdefghijklmnopqrstuvwxyz.apps.googleusercontent.com`)
- **Client Secret** (looks like: `GOCSPX-abcdefghijklmnopqrstuvwxyz`)

### 4. **Update Backend Configuration:**
Replace in `application.yml`:
```yaml
google:
  oauth:
    client-id: YOUR_ACTUAL_GOOGLE_CLIENT_ID
    client-secret: YOUR_ACTUAL_GOOGLE_CLIENT_SECRET
```

### 5. **Update Frontend Configuration:**
Replace in `GoogleAuthButton.tsx` line 35:
```typescript
client_id: 'YOUR_ACTUAL_GOOGLE_CLIENT_ID'
```

## 🚀 **Testing:**
1. Start backend: `mvn spring-boot:run`
2. Start frontend: `npm run dev`
3. Go to `http://localhost:5173`
4. Try Google login on Login or Register page

## 🔍 **Troubleshooting:**
- Make sure both Client ID and Secret are correctly configured
- Ensure the origins match exactly: `http://localhost:5173`
- Check browser console for any JavaScript errors
- Verify the backend endpoint is accessible: `http://localhost:8083/api/auth/google`

## 📍 **Current Status:**
- ✅ Backend Google OAuth endpoint: `/api/auth/google`
- ✅ Frontend Google OAuth button component
- ⚠️ **NEEDS CONFIGURATION:** Replace placeholder Client ID and Secret