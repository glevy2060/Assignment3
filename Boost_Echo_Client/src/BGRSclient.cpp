#include <stdlib.h>
#include "../include/connectionHandler.h"

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
using namespace std;
int twoSpacesCase(string line, char *lineAsChar){
    line = line.substr(line.find(" ") + 1, line.length());
    for(int i=2;i<line.length()+2;i++){
        if(line[i-2]!=' ')
            lineAsChar[i]=line[i-2];
        else
            lineAsChar[i]='\0';
    }
    lineAsChar[line.length()+2] = '\0';
    return line.length()+3;
}

int fourBytesCase(string line, char* lineAsChar){
    line = line.substr(line.find(" ")+1);
    for(int i = 2; i < line.length()+2; i++){
        lineAsChar[i]=line[i-2];
    }
    return 4;
}

//this method changes the user input to the required command;
int getCommandInOpcode(string line , char *lineAsChar){
    string op = line.substr(0, line.find(" "));

    string toReturn = "";
    if(op.compare("ADMINREG") == 0) {
        lineAsChar[0] = 0;
        lineAsChar[1] = 1;
        return twoSpacesCase(line,lineAsChar);
    }
    if(op.compare("STUDENTREG") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 2;
        return twoSpacesCase(line,lineAsChar);
    }
    if(op.compare("LOGIN") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 3;
        return twoSpacesCase(line,lineAsChar);
    }if(op.compare("LOGOUT") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 4;
        return 2;
    }
    if(op.compare("COURSEREG") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 5;
        return fourBytesCase(line, lineAsChar);
    }
    if(op.compare("KDAMCHECK") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 6;
        return fourBytesCase(line, lineAsChar);
    }
    if(op.compare("COURSESTAT") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 7;
        return fourBytesCase(line, lineAsChar);
    }
    if(op.compare("STUDENTSTAT") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 8;
        line = line.substr(line.find(" ") + 1);
        for(int i= 2; i< line.length() +2; i++){
            lineAsChar[i]=line[i-2];
        }
        lineAsChar[line.length()+2] = '\0';
        return line.length()+3;
    }
    if(op.compare("ISREGISTERED") == 0){
        lineAsChar[0] = 0;
        lineAsChar[1] = 9;
        return fourBytesCase(line, lineAsChar);
    }
    if(op.compare("UNREGISTER") == 0){
        lineAsChar[0] = 1;
        lineAsChar[1] = 0;
        return fourBytesCase(line, lineAsChar);
    }
    if(op.compare("MYCOURSES") == 0){
        lineAsChar[0] = 1;
        lineAsChar[1] = 1;
        return 2;
    }

    return -1;

}
int main (int argc, char *argv[]) {
/*    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }*/
    std::string host = "0.0.0.0"; // argv[1];
    short port = atoi("7777");//(argv[2]); //server port

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
        std::cout << line << std::endl;
        //decode the lint to bytes[]

        char lineAsChar[line.length()];
        int len= getCommandInOpcode(line,lineAsChar);
        std::cout << line << std::endl;

        if (!connectionHandler.sendBytes(lineAsChar, len)) { //change sendLine to sendByte()
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        // connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
        std::cout << line << std::endl;
        std::cout << "Sent " << len << " bytes to server" << std::endl;


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
        std::cout << answer << std::endl;
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


