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
bool if_parsed = false;
Adafruit_MQTT_Client mqtt(&client, AIO_SERVER, AIO_SERVERPORT, AIO_USERNAME, AIO_USERNAME, AIO_KEY);
Adafruit_MQTT_Subscribe t1 = Adafruit_MQTT_Subscribe(&mqtt, AIO_USERNAME "/feeds/t1",MQTT_QOS_1);
Adafruit_MQTT_Publish button = Adafruit_MQTT_Publish(&mqtt, AIO_USERNAME "/feeds/t2");
void MQTT_connect();


struct config
{
  byte hour;
  byte minute;
  int timeleft;
};
config reminder[4];


/************************** NTP *****************************************/
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "0.asia.pool.ntp.org", 19800, 60000);

/************************** JSON **************************************/
StaticJsonBuffer<1000> jsonBuffer;
String data_json;// = "{t1:[\"dd\"],[\"mm\"],t1:[\"dd\"],[\"mm\"],t1:[\"dd\"],[\"mm\"],t1:[\"dd\"],[\"mm\"]}";

/******************************* Others *****************************************/
const byte interruptPin = 14;
volatile byte interruptCounter = 0;
int numberOfInterrupts = 0;
void handleInterrupt();
bool parse_success = true;
int reminders;
int reps;

void setup() {
// Connect to Wifi & OTA
Serial.begin(9600);
Serial.println("Booting");
pinMode(interruptPin, INPUT_PULLUP);
attachInterrupt(digitalPinToInterrupt(interruptPin), handleInterrupt, FALLING);
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
}

void loop() {
    MQTT_connect();
    Adafruit_MQTT_Subscribe *subscription;
    while ((subscription = mqtt.readSubscription(3000) )) {
    subscription = mqtt.readSubscription(3000);
    data_json = (char*)t1.lastread;
    Serial.print("lastread: ");
    Serial.println(data_json);
 
      JsonObject& data = jsonBuffer.parseObject(data_json);
      // Test if parsing succeeds.
      if (!data.success()) {
        Serial.println("parseObject() failed");
        parse_success = false;
      }
      // Continue to parse if initial parsing succeeds
        if(parse_success){
          //Store Hour and minute in reminder
          //For reminder 1
            Serial.println("\nReminder 1 :-");
            String str = "t1";
            reminder[0].hour[j] = data[str][j];
            reminder[0].minute[j] = data[str][j+1];
            Serial.printf("reminder[0]: %dhour %dmin\n",reminder[0].hour[j],reminder[0].minute[j]);

          if_parsed = true;
        }
        if(if_parsed)
        {
        }
      }

  
    //}


  Serial.print(F("\nPinging..."));
  static int x;
  Serial.print(x);
  Serial.print("...");
  if (! button.publish(x++)) {
    Serial.println(F("Failed"));
  } else {
    Serial.println(F("OK!"));
  }
    
      
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
    return;
  }
  

  Serial.print("Connecting to MQTT... ");

  uint8_t retries = 3;
  while ((ret = mqtt.connect()) != 0) { // connect will return 0 for connected
       Serial.println(mqtt.connectErrorString(ret));
       Serial.println("Retrying MQTT connection in 10 seconds...");
       mqtt.disconnect();
       delay(10000);  // wait 10 seconds
       retries--;
       if (retries == 0) {
         // basically die and wait for WDT to reset me
         while (1);
       }
  }
  Serial.println("MQTT Connected!");
}

void handleInterrupt() {
  Serial.println("Hello");
}