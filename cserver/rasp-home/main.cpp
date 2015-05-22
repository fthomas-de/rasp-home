#include <iostream>
#include <string>
#include <stdio.h>
#include "RCSwitch.h"

using namespace std;

int main(int argc, char *argv[]) {
    int PIN = 0; // equals Pin 11 or GPIO 17
    char* systemCode = argv[1];
    int unitCode = atoi(argv[2]);
    int command  = atoi(argv[3]);

    if (wiringPiSetup () == -1) return 1;
    printf("sending systemCode[%s] unitCode[%i] command[%i]\n", systemCode, unitCode, command);
    RCSwitch mySwitch = RCSwitch();
    mySwitch.enableTransmit(PIN);

    switch(command) {
        case 1:
            mySwitch.switchOn(systemCode, unitCode);
            break;
        case 0:
            mySwitch.switchOff(systemCode, unitCode);
            break;
        default:
            printf("command[%i] is unsupported\n", command);
            return -1;
    }

    return 0;
}