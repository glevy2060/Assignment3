#include <stdlib.h>
#include "../include/connectionHandler.h"

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
using namespace std;
string twoSpacesCase(string line){
    line = line.substr(line.find(" ") + 1);
    line = line.replace(line.begin(), line.end(), " ", "/0");
    line += "/0";
    return line;
}

string fourBytesCase(string line){
    line = line.substr(line.find(" ")+1);
    return line;
}

//this method changes the user input to the required command;
string getCommandInOpcode(string line){
    string op = line.substr(0, line.find(" "));
    string toReturn = "";
    if(op.compare("ADMINREG")) {
        toReturn += "01";
        toReturn += twoSpacesCase(line);
    }
    if(op.compare("STUDENTREG")){
        toReturn += "02";
        toReturn += twoSpacesCase(line);
    }
    if(op.compare("LOGIN")){
        toReturn += "03";
        toReturn += twoSpacesCase(line);
    }if(op.compare("LOGOUT")){
        toReturn = "04";
    }
    if(op.compare("COURSEREG")){
        toReturn = "05";
        toReturn += fourBytesCase(line);
    }
    if(op.compare("KDAMCHECK")){
        toReturn = "o6";
        toReturn += fourBytesCase(line);
    }
    if(op.compare("COURSESTAT")){
        toReturn = "07";
        toReturn += fourBytesCase(line);
    }
    if(op.compare("STUDENTSTAT")){
        toReturn = "08";
        toReturn += line.substr(line.find(" ") + 1) + "/0";
    }
    if(op.compare("ISREGISTERED")){
        toReturn = "09";
        toReturn += fourBytesCase(line);
    }
    if(op.compare("UNREGISTER")){
        toReturn = "10";
        toReturn += fourBytesCase(line);
    }
    if(op.compare("MYCOURSES")){
        toReturn = "11";
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
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    //From here we will see the rest of the ehco client implementation:
    while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        //decode the lint to bytes[]
        line = getCommandInOpcode(line);
        int len=line.length();
        if (!connectionHandler.sendBytes(line.c_str(), len)) { //change sendLine to sendByte()
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        // connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
        std::cout << "Sent " << len+1 << " bytes to server" << std::endl;


        // We can use one of three options to read data from the server:
        // 1. Read a fixed number of characters
        // 2. Read a line (up to the newline character using the getline() buffered reader
        // 3. Read up to the null character
        std::string answer; //

        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
        if (!connectionHandler.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }

        len=answer.length();
        // A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
        // we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
        answer.resize(len-1);
        std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
        if (answer == "bye") {
            std::cout << "Exiting...\n" << std::endl;
            break;
        }
    }
    return 0;
}


