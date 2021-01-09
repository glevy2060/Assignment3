#include <stdlib.h>
#include <mutex>
#include <Task.h>
#include <connectionHandler.h>
#include <thread>
#include <condition_variable>


/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
using namespace std;


string answerReader(string answer){
        string toReturn="";
        string op=answer.substr(0,2);
        string messageOp=answer.substr(2,2);
        if(messageOp[0]=='0')
            messageOp=messageOp[1];
        if(op.compare("12") == 0){
            toReturn+="ACK "+messageOp+answer.substr(4);

        } else if(op.compare("13") == 0){
            toReturn+="ERROR "+messageOp;
        }
    return toReturn;
}

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]); //server port


    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        return 1;
    }

    std::mutex mutex;
    std::condition_variable cv;
    Task task(connectionHandler, mutex, cv);
    thread taskThread(&Task::run, &task);


    while (1) {
        std::string answer;
        if (!connectionHandler.getLine(answer)) {
            break;
        };

        //unlock
        answer = answerReader(answer);
        std::cout << answer << std::endl;

        if (answer[4] == '4') {
            task.shouldTerminate();
            {std::lock_guard<std::mutex>lk(mutex);}
            cv.notify_all();
            //taskThread.detach();
            break;
        }else if(answer == "ERROR 4"){
            {std::lock_guard<std::mutex>lk(mutex);}
            cv.notify_all();
        }

    }
    taskThread.join();
    return 0;
}


