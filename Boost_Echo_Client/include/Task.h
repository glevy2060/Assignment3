//
// Created by spl211 on 03/01/2021.
//

#ifndef BOOST_ECHO_CLIENT_TASK_H
#define BOOST_ECHO_CLIENT_TASK_H
#include <iostream>
#include <mutex>
#include <thread>
#include "connectionHandler.h"

using namespace std;
class Task {
private:
    ConnectionHandler &connectionHandler;
    bool shouldterminate;
    bool flag;
    std::vector<string>& vec;

public:
    Task(ConnectionHandler &connectionHandler,std::vector<string> &vec);
    void run ();
    void shouldTerminate();
    void flagChanger();
};


#endif //BOOST_ECHO_CLIENT_TASK_H
