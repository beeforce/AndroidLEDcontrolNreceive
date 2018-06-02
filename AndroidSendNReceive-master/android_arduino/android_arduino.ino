double value[30] = {1/*Seat lock 1-2  value1*/,
                 1 /*Battery lock 1-2 value2*/,
                 1/*BMS status 1-5 value3*/,
                 1/*Ready to drive signal 1-2 value4*/,
                 1/*Override key 1-2 value5*/,
                 1/*RFID 1-3 value6*/,
                 0 /*NA 0 value7*/,
                 1/*High Beam value8*/,
                 1/*Turning signal value9*/,
                 1/*Horn 1-2 value10*/,
                 0 /*NA 0 value11*/,
                 0 /*NA 0 value12*/,
                 0 /*NA 0 value13*/,
                 0 /*NA 0 value14*/,
                 0 /*NA 0 value15*/,
                 999/*Speedometer value16*/,
                 0 /*NA 0 value17*/,
                 0 /*NA 0 value18*/,
                 99/*Battery Voltage 00.00 - 99.99 value19*/,
                 100/*Battery percentage 0-100 value20*/,
                 999/*Battery Current -999 - 999 value21*/,
                 9.999/*Battery off balance 0.000 - 9.999 value22*/,
                 99.99/*Battery temperature 00.00 - 99.99 value23*/,
                 999/*Range 0 - 999 value24*/,
                 0 /*NA 0 value25*/,
                 999/*Battery serial 00000000 - 99999999 value26*/,
                 9999/*Battery cycle 0 - 9999 value27*/,
                 999/*VIN number 1000000000 - 9999999999 value28*/,
                 999/*Vehicle mileage 0-999 value29*/,
                 0 /*NA 0 value30*/
                }; //array that contain the value from 20 sensors
void setup() {
  Serial.begin(9600);
}

void loop() {
  sendValuesToAndroid();
  if (Serial.available() > 0)
  {
    char data = (byte)Serial.read(); // reading the data received from the bluetooth module
    switch (data)
    {
      // check for each character that receive from android and control
      case 'a':
        if (value[0] == 0) {
          value[0] = 1;
        } else {
          value[0] = 0;
        }
        break;
      case 'b':
        if (value[1] == 0) {
          value[1] = 1;
        } else {
          value[1] = 0;
        }
        break;
      case 'c':
        if (value[5] == 0) {
          value[5] = 1;
        } else {
          value[5] = 0;
        }
        break;
      case 'd':
        if (value[7] == 0) {
          value[7] = 1;
        } else {
          value[7] = 0;
        }
        break;
      case 'e':
        value[20] = 1;
        value[21] = 0;

        break;
      case 'f':
        value[20] = 0;
        value[21] = 1;
        break;
      case 'g':
        value[19] = 0;
        value[19] = 1;
        break;
      case 'h':
        value[19] = 1;
        value[19] = 0;
        break;
      default : break;
    }
  }
}

void sendValuesToAndroid()
{
  //sends # before the values, to tell the android handles the data
  Serial.print('#');
  //for loop cycles through 20 values via serial
  for (int i = 0; i < 30; i++)
  {
    Serial.print(value[i]);
    Serial.print('+');
  }
  Serial.print('~'); //used as an end of transmission character - used in app for string length
  Serial.println();
  delay(10);        //added a delay to eliminate missed transmissions
}

//#include <ArduinoJson.h>
//StaticJsonBuffer<200> jsonBuffer;
//void setup() {
//Serial.begin(9600);
//}
//
//void loop() {
//  sendValuesToAndroid();
//}
//
//void sendValuesToAndroid()
//{
//  JsonObject& root = jsonBuffer.createObject();
//  root["seatLock"] = "1-2";
//  root["batteryLock"] = "1-2";
//  root["bmsStatus"] = "1-5";
//  root["readyToDriveSignal"] = "1-2";
//  root["RFID"] = "1-3";
//  root["hignBeam"] = "1-2";
//  root["turningSignal"] = "1-3";
//  root["horn"] = "1-2";
//  root["speedmoter"] = "0-999";
//  root["batteryVoltage"] = "00.00-99.99";
//  root["batteryPercentage"] = "0-100";
//  root["batteryCurrent"] = "-999-999";
//  root["batteryOffBalance"] = "0.000-9.999";
//  root["batteryTempurature"] = "0.00-99.99";
//  root["range"] = "0-999";
//  root["batterySerial"] = "00000000-99999999";
//  root["batteryCycle"] = "0-9999";
//  root["VINNumber"] = "1000000000-9999999999";
//  root["vehicleMileage"] = "0-999";
//  Serial.print("+");
//  root.printTo(Serial);
//  Serial.print("!");
//  jsonBuffer.clear();
//  delay(100);        //added a delay to eliminate missed transmissions
//}
