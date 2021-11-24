#!/usr/bin/env python3
import paho.mqtt.publish as publish # pip library paho-mqtt
from subprocess import check_output
from re import findall
import time
import uuid
import random

class Client():
    x = 0
    def __init__(self, x):
        self.x = x
    def get_temp(self):
        temp = random.randint(-10,10)
        temp = temp * 0.01
        self.x = self.x+temp
        return(self.x)
    def publish_message(self, topic, message):
        print("Publishing to MQTT topic: " + topic)
        print("Message: " + message)
        publish.single(topic, message, hostname="192.168.0.40")



client = Client(25)
MessageCounter = 0
while True:
    temp = client.get_temp()
    MessageCounter = MessageCounter + 1
    #client.publish_message("-/pi/cputemp/DEG_C", "{\"TimeOfReading\":"+str(round(time.time()*1000))+",\"Reading\":"+ str(temp)+",\"ReadingId\":\"" + str(uuid.uuid4())+"\",\"MessageCounterId:\"" + str(MessageCounter))
    text = f'{{"TimeOfReading":{str(round(time.time()*1000))},"Reading":{str(temp)},"ReadingId":"{str(uuid.uuid4())}","MessageCounterId":{str(MessageCounter)}}}'
    client.publish_message("-/bsc/temp/DEG_C", text)
    time.sleep(.1)
