// @see: http://stackoverflow.com/a/17051931/605890
String inData;

void setup() {
    Serial.begin(9600);
}

void loop() {
    while (Serial.available() > 0)
    {
        char recieved = Serial.read();
        inData += recieved; 
        if (recieved == '\n')
        {
            Serial.print("Arduino Received: ");
            Serial.print(inData);
            inData = ""; // Clear recieved buffer
        }
    }
}
