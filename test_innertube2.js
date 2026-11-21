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
      if (parsed.streamingData) {
        console.log('\n=== ADAPTIVE FORMATS ===');
        parsed.streamingData.adaptiveFormats?.forEach((fmt, i) => {
          console.log(`${i}: ${fmt.mimeType} | url: ${fmt.url ? 'YES' : 'NO'} | cipher: ${fmt.signatureCipher ? 'YES' : 'NO'} | bitrate: ${fmt.bitrate} | quality: ${fmt.qualityLabel || 'N/A'}`);
        });
        
        console.log('\n=== COMBINED FORMATS ===');
        parsed.streamingData.formats?.forEach((fmt, i) => {
          console.log(`${i}: ${fmt.mimeType} | url: ${fmt.url ? 'YES' : 'NO'} | cipher: ${fmt.signatureCipher ? 'YES' : 'NO'}`);
        });
        
        // Check for MP4
        const mp4Formats = parsed.streamingData.adaptiveFormats?.filter(f => f.mimeType?.includes('mp4')) || [];
        console.log('\n=== MP4 FORMATS ===');
        mp4Formats.forEach(f => console.log(f.mimeType, f.url ? 'URL' : 'NO URL'));
        
        // Check for HLS
        const hlsFormats = parsed.streamingData.adaptiveFormats?.filter(f => f.mimeType?.includes('m3u8') || f.url?.includes('.m3u8')) || [];
        console.log('\n=== HLS FORMATS ===');
        hlsFormats.forEach(f => console.log(f.mimeType, f.url));
        
      } else {
        console.log('No streamingData');
      }
    } catch (e) {
      console.log('Parse error:', e.message);
    }
  });
});

req.on('error', (e) => {
  console.error('Request error:', e.message);
});

req.write(data);
req.end();