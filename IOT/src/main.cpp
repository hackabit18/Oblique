#include <ESP8266WiFi.h>
#include "Adafruit_MQTT.h"
#include "Adafruit_MQTT_Client.h"
#include <ESP8266mDNS.h>
#include <WiFiUdp.h>
#include <ArduinoOTA.h>
#include <NTPClient.h>
#include <ArduinoJson.h>

/************************* WiFi Access Point *********************************/
#define WLAN_SSID       "#Aakash"
#define WLAN_PASS       "pez-1024"
WiFiClient client;

/************************* MQTT Setup *********************************/
#define AIO_SERVER      "io.adafruit.com"
#define AIO_SERVERPORT  1883
#define AIO_USERNAME    "aakashk_kvjp58"
#define AIO_KEY         "46f406136c5e4e5f874e283f95ed3dfc"
Adafruit_MQTT_Client mqtt(&client, AIO_SERVER, AIO_SERVERPORT, AIO_USERNAME, AIO_USERNAME, AIO_KEY);
void MQTT_connect();

struct config
{
  byte hour;
  byte minute;
  byte color[3];
};
config reminder[4];

//Subscribe Topics
Adafruit_MQTT_Subscribe t1 = Adafruit_MQTT_Subscribe(&mqtt, AIO_USERNAME "/f/t1");
Adafruit_MQTT_Subscribe t2 = Adafruit_MQTT_Subscribe(&mqtt, AIO_USERNAME "/f/t2");
Adafruit_MQTT_Subscribe t3 = Adafruit_MQTT_Subscribe(&mqtt, AIO_USERNAME "/f/t3");
Adafruit_MQTT_Subscribe t4 = Adafruit_MQTT_Subscribe(&mqtt, AIO_USERNAME "/f/t4");
//Publish Topics
Adafruit_MQTT_Publish button = Adafruit_MQTT_Publish(&mqtt, AIO_USERNAME "/f/button");
Adafruit_MQTT_Publish ping = Adafruit_MQTT_Publish(&mqtt, AIO_USERNAME "/f/ping");

/************************** NTP *****************************************/
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "0.asia.pool.ntp.org", 19800, 60000);

/************************** JSON **************************************/
StaticJsonBuffer<200> jsonBuffer;
String data_json = "{t1:[\"dd\"],[\"mm\"],t1:[\"dd\"],[\"mm\"],t1:[\"dd\"],[\"mm\"],t1:[\"dd\"],[\"mm\"]}";

void setup() {
// Connect to Wifi & OTA
Serial.begin(9600);
Serial.println("Booting");
pinMode(D4,INPUT);
WiFi.begin(WLAN_SSID, WLAN_PASS);
while (WiFi.waitForConnectResult() != WL_CONNECTED) {
    Serial.println("Connection Failed! Rebooting...");
    delay(1000);
    ESP.restart();
    }

ArduinoOTA.onStart([]() {
    String type;
    if (ArduinoOTA.getCommand() == U_FLASH) {
      type = "sketch";
    } else { // U_SPIFFS
      type = "filesystem";
    }
    Serial.println("Start updating " + type);
  });
  ArduinoOTA.onEnd([]() {
    Serial.println("\nEnd");
  });
  ArduinoOTA.onProgress([](unsigned int progress, unsigned int total) {
    Serial.printf("Progress: %u%%\n", (progress / (total / 100)));
  });
  ArduinoOTA.onError([](ota_error_t error) {
    Serial.printf("Error[%u]: ", error);
    if (error == OTA_AUTH_ERROR) {
      Serial.println("Auth Failed");
    } else if (error == OTA_BEGIN_ERROR) {
      Serial.println("Begin Failed");
    } else if (error == OTA_CONNECT_ERROR) {
      Serial.println("Connect Failed");
    } else if (error == OTA_RECEIVE_ERROR) {
      Serial.println("Receive Failed");
    } else if (error == OTA_END_ERROR) {
      Serial.println("End Failed");
    }
  });
  ArduinoOTA.setPassword("admin404");
  ArduinoOTA.begin();
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  // End of Wifi Connection Seq
  timeClient.begin();
  //Sub Topics
  mqtt.subscribe(&t1);
  mqtt.subscribe(&t2);
  mqtt.subscribe(&t3);
  mqtt.subscribe(&t4);
}

void loop() {
    MQTT_connect();
    Adafruit_MQTT_Subscribe *subscription;
    data_json = (char*)t1.lastread;
    JsonObject& data = jsonBuffer.parseObject(data_json);
    // Test if parsing succeeds.
    if (!data.success()) {
      Serial.println("parseObject() failed");
    }
      //Store Hour and minute in reminder
      for(int i=1 ; i<5 ; i++) 
      {
        String str = "t";
        str += (char)i;
        reminder[i].hour = data[str][0];
        reminder[i].minute = data[str][1];
        Serial.printf("reminder[%d]: %dhour %dmin ",i,reminder[i].hour,reminder[i].minute);
        Serial.printf("%d:%d:%d color\n",reminder[0].color,reminder[1].color,reminder[2].color);
      }
      
      /*/Store Colors in reminder
      {
        String str = "c";
        str += (char)i;
        reminder[i].hour = data[str][0];
        reminder[i].minute = data["t%d"][1];
        
      }
      */
    timeClient.update();
    Serial.printf("NTP Time: ");
    Serial.println(timeClient.getFormattedTime());
    ArduinoOTA.handle();
    delay(2500);
    
}

/****************************** FUNCTIONS *************************************/
void MQTT_connect() {
  int8_t ret;

  // Stop if already connected.
  if (mqtt.connected()) {
    static unsigned int x = 0;
    return;
  }

  Serial.print("Connecting to MQTT... ");

  uint8_t retries = 4;
  while ((ret = mqtt.connect()) != 0) {
       Serial.println(mqtt.connectErrorString(ret));
       Serial.println("Retrying MQTT connection in 5 seconds...");
       mqtt.disconnect();
       delay(5000);
       retries--;
       if (retries == 0) {
         Serial.println("Can't Cpnnect after 4 retries, Restart!");
       }
  }
  Serial.println("MQTT Connected!");
}