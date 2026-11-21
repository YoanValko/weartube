const https = require('https');

const testVideos = [
  "dQw4w9WgXcQ", // Never Gonna Give You Up - WORKS
  "jNQXAC9IVRw", // Me at the zoo - NO STREAMING DATA
  "kJQP7kiw5Fk", // Despacito
  "fJ9rUzIMcZQ", // Shape of You
  "kXYiU_JCYtU", // See You Again
  "9bZkp7q19f0", // PSY - GANGNAM STYLE
  "2Vv-BfVoq4g", // Shape of You - Live
  "kffacxfA7G4", // Baby Shark Dance
];

async function checkVideo(videoId) {
  return new Promise((resolve) => {
    const data = JSON.stringify({
      videoId: videoId,
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
          const hasStreaming = !!parsed.streamingData;
          const adaptiveCount = parsed.streamingData?.adaptiveFormats?.length || 0;
          const mp4Count = parsed.streamingData?.adaptiveFormats?.filter(f => f.mimeType?.includes('mp4')).length || 0;
          console.log(`${videoId}: streaming=${hasStreaming}, adaptive=${adaptiveCount}, mp4=${mp4Count}`);
        } catch (e) {
          console.log(`${videoId}: Parse error - ${e.message}`);
        }
        resolve();
      });
    });

    req.on('error', (e) => {
      console.log(`${videoId}: Request error - ${e.message}`);
      resolve();
    });

    req.write(data);
    req.end();
  });
}

async function run() {
  for (const id of testVideos) {
    await checkVideo(id);
    // Small delay between requests
    await new Promise(r => setTimeout(r, 500));
  }
}

run();