const https = require('https');

const data = JSON.stringify({
  videoId: "dQw4w9WgXcQ",
  context: {
    client: {
      clientName: "ANDROID_VR",
      clientVersion: "1.57.29",
      hl: "en"
    }
  }
});

const options = {
  hostname: 'www.youtube.com',
  path: '/youtubei/v1/player?key=AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'User-Agent': 'com.google.android.apps.youtube.vr.oculus/1.57.29 (Linux; U; Android 12L; eureka-user Build/SQ3A.220605.009.A1) gzip',
    'Content-Length': Buffer.byteLength(data)
  }
};

const req = https.request(options, (res) => {
  let data = '';
  res.on('data', (chunk) => { data += chunk; });
  res.on('end', () => {
    try {
      const parsed = JSON.parse(data);
      console.log('Status:', res.statusCode);
      console.log('Has streamingData:', !!parsed.streamingData);
      if (parsed.streamingData) {
        console.log('Adaptive formats:', parsed.streamingData.adaptiveFormats?.length || 0);
        if (parsed.streamingData.adaptiveFormats?.length > 0) {
          const fmt = parsed.streamingData.adaptiveFormats[0];
          console.log('First format:', fmt.mimeType, fmt.url ? 'HAS URL' : 'NO URL', fmt.signatureCipher ? 'HAS CIPHER' : 'NO CIPHER');
        }
      }
      if (parsed.videoDetails) {
        console.log('Title:', parsed.videoDetails.title);
      }
    } catch (e) {
      console.log('Parse error:', e.message);
      console.log('Raw:', data.substring(0, 500));
    }
  });
});

req.on('error', (e) => {
  console.error('Request error:', e.message);
});

req.write(data);
req.end();