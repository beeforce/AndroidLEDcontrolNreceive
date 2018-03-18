int value[20] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19}; //array that contain the value from 20 sensors
void setup() {
  Serial.begin(9600);
  pinMode(8, OUTPUT);
  pinMode(7, OUTPUT);
  pinMode(6, OUTPUT);
  pinMode(5, OUTPUT);
  pinMode(4, OUTPUT);
}

void loop() {
  sendValuesToAndroid();
  if (Serial.available() > 0)
  {
    char data = (byte)Serial.read(); // reading the data received from the bluetooth module
    switch (data)
    {
      // check for each character that receive from android and control
      case 'a': digitalWrite(8, HIGH); value[0] = 11; break;
      case 'b': digitalWrite(8, LOW); value[0] = 10; break;
      case 'c': digitalWrite(7, HIGH); value[1] = 21; break;
      case 'd': digitalWrite(7, LOW); value[1] = 20; break;
      case 'e': digitalWrite(6, HIGH); value[2] = 31; break;
      case 'f': digitalWrite(6, LOW); value[2] = 30; break;
      case 'g': digitalWrite(5, HIGH); value[3] = 41; break;
      case 'h': digitalWrite(5, LOW); value[3] = 40; break;
      case 'i': digitalWrite(4, HIGH); value[4] = 51; break;
      case 'j': digitalWrite(4, LOW); value[4] = 500; break;
      default : break;
    }
  }
  delay(2000);
}

void sendValuesToAndroid()
{
  //sends # before the values, to tell the android handles the data
  Serial.print('#');
  //for loop cycles through 20 values via serial
  for (int i = 0; i < 20; i++)
  {
    Serial.print(value[i]);
    Serial.print('+');
  }
  Serial.print('~'); //used as an end of transmission character - used in app for string length
  Serial.println();
  delay(10);        //added a delay to eliminate missed transmissions
}
