var http = require("http");
var socketIO = require('socket.io');
var my_define = require('./define.js');
var filename = './data.json';
var fs = require('fs');
var io;
var port = 3456;
var ip = "169.254.171.159";

var server = http.createServer().listen(port,ip, () => {
    console.log(`Server started at %s:%s`, ip, port);
});

io = socketIO.listen(server);

function convertToUTF8(data) {
    return data.toString(my_define.Ascii);
}

// Process

// When someone socket connect to Server
io.on(my_define.Connect, (socket) => {
    var idUser, my_db;
    console.log(socket.request.connection.remoteAddress + " is connect to Server!\n");

    // Setting event listen

    // Login
    socket.on("login", (dataClient) => {
        var packageLogin = JSON.parse(dataClient.toString());
        console.log(`Server receive data:
        \t- id: ${packageLogin.id}
        \t- pass: ${packageLogin.pass}\n`);
        // id : "", pass : ""
        // Get list GV

        my_db = JSON.parse(fs.readFileSync(filename, 'utf8'));

        var result = false;
        var data = convertToUTF8("Tài khoản hoặc mật khẩu không đúng. Vui lòng kiểm tra lại!");
        for (let i = 0; i < my_db.list_giaovien.length; ++i) {
            // Check id and pass
            if (my_db.list_giaovien[i].username == packageLogin.id && my_db.list_giaovien[i].password == packageLogin.pass) {
                result = true;
                idUser = my_db.list_giaovien[i].id;
                var userName = convertToUTF8(my_db.list_giaovien[i].name);
                data = {
                    id: idUser,
                    name: userName
                };
                break;
            }
        }
        var clientReceive = {
            TYPE: "ResultLogin",
            RESULT: result,
            DATA: data
        };
        console.log(`Server send data to client:
        \t- Name Event: Login 
        \t- Type: ${clientReceive.TYPE}
        \t- Result: ${clientReceive.RESULT}
        \t- Data: ${clientReceive.DATA}\n`
        );

        socket.emit(my_define.Message, clientReceive);
        console.log("Server send success!\n");
    });

    // Main

    // Get User Timetable
    socket.on("getTimeTable", (dataClient) => {
        var data = convertToUTF8("Hiện tại không chưa có thời khoá biểu!");
        var result = false;
        for (let i = 0; i < my_db.list_giaovien.length; ++i) {
            if (my_db.list_giaovien[i].id == idUser) {
                result = true;
                data = my_db.list_giaovien[i].timetable;
                //data = new Buffer(my_db.list_giaovien[i].timetable, 'binary').toString('base64');
                break;
            }
        }
        var clientReceive = {
            TYPE: "ResultTimeTable",
            RESULT: result,
            DATA: data
        }
        console.log(`Server send data to client: 
        \t- Name Event: GetTimeTable
        \t- Type: ${clientReceive.TYPE}
        \t- Result: ${clientReceive.RESULT}
        \t- Data: ${clientReceive.DATA}\n`);
        socket.emit(my_define.Message, clientReceive);
        console.log("Server send success!");
    });

    // Update data
    socket.on("updateData", (dataClient) => {
        let packageClient = JSON.parse(dataClient.toString());
        console.log(`Server receive data: 
        \t- day: ${packageClient.day}
        \t- name: ${packageClient.name}
        \t- idStudent: ${packageClient.id}
        \t- date: ${packageClient.date}\n`);
        // day, name of list_monhoc, id of list_student, date
        for (let i = 0; i < my_db.list_giaovien.length; ++i) {
            // loop list GV to get id_User
            if (my_db.list_giaovien[i].id == idUser) {
                let list_TB = my_db.list_giaovien[i].timetable;
                for (let j = 0; j < list_TB.length; ++j) {
                    // loop list timeTable get day
                    if (list_TB[j].day == packageClient.day) {
                        let list_monhoc = list_TB[j].list_monhoc;
                        for (let n = 0; n < list_monhoc.length; ++n) {
                            // Loop list monhoc get name
                            if (list_monhoc[n].name == packageClient.name) {
                                let list_Student = list_monhoc[n].list_student;
                                for (let m = 0; m < list_Student.length; ++m) {
                                    // Loop list student get id
                                    if (list_Student[m].id == packageClient.id) {
                                        let dateUser = packageClient.date;

                                        let item = {
                                            date: dateUser,
                                            value: true
                                        }
                                        list_Student[m].check.push(item);

                                        fs.writeFile(filename, JSON.stringify(my_db), 'utf8');
                                        console.log(`Server update success!`);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    });
});