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
Adafruit_MQTT_Publish ping = Adafruit_MQTT_Publish(&mqtt, AIO_USERNAME "/feeds/ping");
Adafruit_MQTT_Publish button = Adafruit_MQTT_Publish(&mqtt, AIO_USERNAME "/feeds/button");
void MQTT_connect();


struct config
{
  byte hour;
  byte minute;
  int timeleft;
  int endtime;
  bool set;
};
config reminder[4];


/************************** NTP *****************************************/
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "0.asia.pool.ntp.org", 19800, 60000);

/************************** JSON **************************************/
StaticJsonBuffer<1000> jsonBuffer;
String data_json;// = "{t1:[\"dd\"],[\"mm\"],t1:[\"dd\"],[\"mm\"],t1:[\"dd\"],[\"mm\"],t1:[\"dd\"],[\"mm\"]}";

/******************************* Others *****************************************/

volatile byte interruptCounter = 0;
int numberOfInterrupts = 0;
void handleInterrupt();
unsigned long prev;
unsigned long now;
unsigned long timer;
bool parse_success = true;
bool calculated;
int reminders;
int reps;
bool button_pressed = false;
const byte interruptPin = 14;
const byte r = 0;
const byte g = 5;
const byte b = 4;
const byte speaker = 13;
bool pushed = false;
bool y[3]={0,0,0};
byte buzz_limit;
byte ring = false;
byte x=0;
int time_in_min();
long time_in_sec();
void trigger(int,int,int);

void setup() {
// Connect to Wifi & OTA
Serial.begin(9600);
Serial.println("Booting");
pinMode(interruptPin, INPUT_PULLUP);
pinMode(r,OUTPUT);
pinMode(g,OUTPUT);
pinMode(b,OUTPUT);
pinMode(speaker,OUTPUT);
digitalWrite(r,0);
digitalWrite(g,0);
digitalWrite(b,0);

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
    x = 0;
    y[0]=0;
    y[1]=0;
    y[2]=0;
    pushed = false;
    ring = false;
    buzz_limit = 0;
    button_pressed = false;
    calculated=false;
    subscription = mqtt.readSubscription(3000);
    data_json = (char*)t1.lastread;
    Serial.print("\nlastread: ");
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
            reminder[0].hour = data[str][0];
            reminder[0].minute = data[str][1];
            Serial.printf("reminder[0]: %dhour %dmin\n",reminder[0].hour,reminder[0].minute);

          //For reminder 2
            Serial.println("\nReminder 2 :-");
            str = "t2";
            reminder[1].hour = data[str][0];
            reminder[1].minute = data[str][1];
            Serial.printf("reminder[1]: %dhour %dmin\n",reminder[1].hour,reminder[1].minute);

          //For reminder 3
            Serial.println("\nReminder 3 :-");
            str = "t3";
            reminder[2].hour = data[str][0];
            reminder[2].minute = data[str][1];
            Serial.printf("reminder[2]: %dhour %dmin\n",reminder[2].hour,reminder[2].minute);
          if_parsed = true;
        }
        
      }
      if(if_parsed)
        {
          
          reminder[0].timeleft = reminder[0].hour*60 + reminder[0].minute- time_in_min();
          reminder[1].timeleft = reminder[1].hour*60 + reminder[1].minute - time_in_min();
          reminder[2].timeleft = reminder[2].hour*60 + reminder[2].minute - time_in_min();
          //if(reminder[0].timeleft<0) reminder[0].timeleft = reminder[0].timeleft*(-1) + 1440;
          //if(reminder[1].timeleft<0) reminder[1].timeleft = reminder[1].timeleft*(-1) + 1440;
          //if(reminder[2].timeleft<0) reminder[2].timeleft = reminder[2].timeleft*(-1) + 1440;
          Serial.println("\nTimeleft");
          Serial.printf("%dmin\n", reminder[0].timeleft);
          Serial.printf("%dmin\n", reminder[1].timeleft);
          Serial.printf("%dmin\n", reminder[2].timeleft);
          reminder[1].set = true;
          reminder[2].set = true;
          reminder[3].set = true;
          if(!calculated){
            for(int i=0;i<=2;i++)
              {
                reminder[i].endtime = reminder[i].hour*60 +  reminder[i].minute + 1;
                Serial.print("End Times");
                Serial.printf("%d %d\n", reminder[i].endtime/60 ,reminder[i].endtime%60);
              }
              
              calculated = true;
          }
          trigger(reminder[0].timeleft,reminder[1].timeleft,reminder[2].timeleft);
        }


  Serial.print(F("\nPinging..."));
  static int x;
  Serial.print(x);
  Serial.print("...");
  if (! ping.publish(x++)) {
    Serial.println(F("Failed"));
  } else {
    Serial.println(F("OK!"));
  }
      
    timeClient.update();
    Serial.printf("NTP Time: ");
    Serial.print(timeClient.getFormattedTime());
    Serial.printf("=%dminutes",time_in_min());
    if(ring)
    {
        digitalWrite(speaker,0);
        ring = 0;
        Serial.println("Buzzer ON\n");
    }
    else
    {
       Serial.println("Buzzer OFF\n");
       digitalWrite(speaker,1);
       ring = false;
    }
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
  button_pressed = true;
  pushed = true;
  Serial.println("INTEEUPT");
}

int time_in_min()
{
 return timeClient.getHours()*60 + timeClient.getMinutes();
}

long time_in_sec()
{
 return timeClient.getHours()*60*60 + timeClient.getMinutes()*60 + timeClient.getSeconds();
}

void trigger(int a, int bx, int c)
{
  /************** De-trigger ***************/
  if( reminder[0].endtime <= time_in_min())
  { 
    Serial.println("R1 De-Triggered");
      digitalWrite(r,0);
      if(!pushed)
      {
        Serial.println("button not Pressed");
        delay(2200);
        button.publish(1);
        pushed = true;
    }
  }

  if( reminder[1].endtime <= time_in_min())
  { 
        Serial.println("R2 De-Triggered");
        digitalWrite(g,0);
        if(!pushed)
        {
          Serial.println("button not Pressed");
          delay(2200);
          button.publish(1);
          pushed = true;
        }
        x++;
  }

  if( reminder[2].endtime <= time_in_min())
  { 

      Serial.println("R3 De-Triggered");
      digitalWrite(b,0);
      if(!pushed)
      {
        Serial.println("button not Pressed");
        delay(2200);
        button.publish(1);
        pushed = true;
    }
    x++;
  }
  if(x == 2) ring = false;

  /***************** TRIGGER **************/
  if(button_pressed) 
  {
    static int i=0;
    Serial.println("Button Pressed");
    Serial.println("R1 De-Triggered");
    digitalWrite(r,0);
    Serial.println("R2 De-Triggered");
    digitalWrite(g,0);
    Serial.println("R3 De-Triggered");
    digitalWrite(b,0);
    return;
  }
  if(a <= 1 && !(a<-1)) 
  {
    Serial.println("R1 Triggered");
    digitalWrite(r,1);
    if(!y[0])
    {
      ring = true;
      y[0] = 1;
    }
  }
  if(bx <= 1  && !(bx<-1) ) 
  {
    Serial.println("R2 Triggered");
    digitalWrite(g,1);
    if(!y[1])
    {
      ring = true;
      y[1] = 1;
    }
  }
  if(c <= 1  && !(c<-1))
  { 
    Serial.println("R3 Triggered");
    digitalWrite(b,1);
    if(!y[2])
    {
      ring = true;
      y[1] = 1;
    }
  }
}