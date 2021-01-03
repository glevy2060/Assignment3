//
// Created by spl211 on 03/01/2021.
//

#ifndef BOOST_ECHO_CLIENT_TASK_H
#define BOOST_ECHO_CLIENT_TASK_H
#include <iostream>
#include <mutex>
#include <thread>
using namespace std;
class Task {
private:
    std::mutex & _mutex;
    string _host;
    short _port;

public:
    Task(mutex &mutex, basic_string<char> host, short port);
    int twoSpacesCase(string line, char *lineAsChar);
    int fourBytesCase(string line, char* lineAsChar);
    int getCommandInOpcode(string line , char *lineAsChar);
    void run ();

};


#endif //BOOST_ECHO_CLIENT_TASK_H
