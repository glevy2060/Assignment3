//
// Created by spl211 on 03/01/2021.
//

#ifndef BOOST_ECHO_CLIENT_TASK2_H
#define BOOST_ECHO_CLIENT_TASK2_H
#include <iostream>
#include <mutex>
#include <thread>
#include "connectionHandler.h"
#include <condition_variable>

using namespace std;
class Task {
private:
    ConnectionHandler &connectionHandler;
    std::mutex& mutex;
    condition_variable& cv;
    bool shouldterminate;

public:
    Task(ConnectionHandler &connectionHandler, std::mutex& mutex, condition_variable& cv);
    int twoSpacesCase(string line, char *lineAsChar);
    int fourBytesCase(string line, char* lineAsChar);
    int getCommandInOpcode(string line , char *lineAsChar);
    void run ();
    void shouldTerminate();

};


#endif //BOOST_ECHO_CLIENT_TASK2_H
