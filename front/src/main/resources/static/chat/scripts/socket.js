let stompClient;

async function connect() {
    const headers = {
        Authorization: ("Bearer " + (await getJwtToken()))
    };

    const socket = new SockJS(host + '/ws');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;
    stompClient.connect(headers, onConnected, onConnectionError);
}


function onConnected() {
    //setConnected(true);


    stompClient.subscribe(`/user/${id}/queue/message`, (payload) => {
        let body = payload.body;
        singleChatNewMessage(Message.fromJson(JSON.parse(body)));
    });

    stompClient.subscribe(`/user/${id}/queue`, (payload) => {
        let body = payload.body;
        let json = JSON.parse(body);

        let t = json.t;
        let object = json.o;

        if(t === "NEW_GROUP_CHAT") {
            getChatByChatIdAndType(object.chatId, "group_chat").then((r) => {
                addGroupChatToStart(r);
            })
        } else if(t === "REMOVE_GROUP_CHAT") {
            removeGroupChat(object.chatId);
        }
    });


    stompClient.subscribe(`/user/${id}/queue/error`, (payload) => {
        onErrorResponse(payload.body);
    });
    // stompClient.subscribe(`/user/${id}/queue/message`, onMessageReceived);
    // stompClient.subscribe(`/user/${id}/queue/reply`, onMessageReceived);
    // stompClient.subscribe(`/user/${id}/queue/error`, onMessageReceived);
}

function addStatusHandlerPerson(personId) {
    stompClient.subscribe(`/topic/user/${personId}/update`, (payload) => {
        let body = payload.body;
        let json = JSON.parse(body);

        let t = json.t;
        let object = json.o;

        if(t === "STATUS") {
            changeSingleChatStatus(object.personId, object.status);
        } else if(t === "USERNAME") {
            changePersonUsername(object.personId, object.username);
        } else if(t === "PROFILE_PHOTO_UPDATE") {
            changePersonProfilePhotoUpdate(personId);
        } else if(t === "PROFILE_PHOTO_DELETE") {
            changePersonProfilePhotoDelete(personId);
        }
    });
}

function subscribeToGroupChatTopic(chatId) {
    stompClient.subscribe(`/topic/group_chat/${chatId}`, (payload) => {
        let body = payload.body;
        let json = JSON.parse(body);

        let t = json.t;
        let object = json.o;

        if(t === "MESSAGE") {
            groupChatNewMessage(Message.fromJson(object))
        } else if(t === "PARTICIPANT_ADD") {
            addParticipant(chatId, object.personId);
        } else if(t === "PARTICIPANT_REMOVE") {
            removeParticipant(chatId, object.personId);
        } else if(t === "PARTICIPANT_LEAVE") {
            removeParticipant(chatId, object.personId);
        } else if(t === "NAME_UPDATE") {
            changeGroupChatNameUpdate(chatId, object.name);
        } else if(t === "PROFILE_PHOTO_UPDATE") {
            changeGroupChatProfilePhotoUpdate(chatId);
        } else if(t === "PROFILE_PHOTO_DELETE") {
            changeGroupChatProfilePhotoDelete(chatId);
        }
    });
}

function onConnectionError(frame) {
    console.log("Connection error: " + frame);
    displayError("WebSocket connection error: " + frame);
}

function onErrorResponse(message) {
    console.log("Error: " + message);
    displayError(message);
}

function disconnect() {
    stompClient.disconnect();
    //setConnected(false);
    console.log("Disconnected");
}



async function sendMessage(topic, message) {
    stompClient.send(topic, {}, JSON.stringify(message));

    //setInterval(sendNameAsync, 15);

    //sendName();
}


window.addEventListener("beforeunload", () => {
    disconnect();
});





































