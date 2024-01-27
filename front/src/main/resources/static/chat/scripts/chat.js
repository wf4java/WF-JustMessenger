const chatMap = {};

let selectedChatId;
let selectedChatType;
let selectedChatTypeIsFromSearch = false;
let firstLoadedMessage;
let loadedToEnd = false;



async function getChatByChatIdAndType(chatId, type) {
    let cacheChat = chatMap[chatId];
    if(cacheChat !== undefined) return cacheChat;

    if(type === "single_chat") {
        return getSingleChatByChatId(chatId)
    }
    if (type === "group_chat") {
        return getGroupChatByChatId(chatId);
    }
}

async function getSingleChatMessageByIdAndChatId(id, chatId) {
    const response = await getRequestWithsParams("/api/single_chat/message", {messageId: id, chatId: chatId});
    return Message.fromJson(await response.json());
}

async function getAllSingleChats()  {
    const response = await getRequest("/api/single_chat/all");
    const singleChats = [];

    for (const chatJson of (await response.json())) {
        const singleChat = SingleChat.fromJson(chatJson);
        chatMap[singleChat.chatId] = singleChat;
        singleChats.push(singleChat);
    }

    return singleChats;
}

async function getSingleChatByChatId(chatId) {
    const response = await getRequestWithsParams("/api/single_chat", {chatId: chatId});
    const chat = SingleChat.fromJson(await response.json());
    chatMap[chat.chatId] = chat;
    return chat;
}


async function getAllGroupChats()  {
    const response = await getRequest("/api/group_chat/all");
    const groupChats = [];

    for (const chatJson of (await response.json())) {
        const groupChat = GroupChat.fromJson(chatJson);
        chatMap[groupChat.chatId] = groupChat;
        groupChats.push(groupChat);
    }

    return groupChats;
}

async function getGroupChatByChatId(chatId) {
    const response = await getRequestWithsParams("/api/group_chat", {chatId: chatId});
    const chat = GroupChat.fromJson(await response.json());
    chatMap[chat.chatId] = chat;
    return chat;
}

async function getAllChats() {
    const singleChats = await getAllSingleChats();
    const groupChats = await getAllGroupChats();
    const chats = singleChats.concat(groupChats);

    chats.sort((a, b) => getLastUpdateTimeForChat(b) - getLastUpdateTimeForChat(a))

    return chats;
}

function getLastUpdateTimeForChat(chat) {
    return (chat.lastMessage ? objectIdToTimestamp(chat.lastMessage.id) : objectIdToTimestamp(chat.chatId));
}







const chatListBox = document.getElementById('chat_list_box');
async function loadChats() {
    const chats = await getAllChats();

    const neededPersons = [];
    for (const chat of chats) {
        if(chat instanceof SingleChat) {
            neededPersons.push(chat.othetPersonId);
        } else if(chat instanceof GroupChat) {
            if(chat.lastMessage != null)
                neededPersons.push(chat.lastMessage.senderId);
        }
    }
    await getAllPersonByIds(neededPersons);

    for (const chat of chats) {
        if(chat instanceof SingleChat) {
            chatListBox.appendChild(document.createRange().createContextualFragment(await toSingleChatDiv(chat)));
        } else if (chat instanceof GroupChat) {
            chatListBox.appendChild(document.createRange().createContextualFragment(await toGroupChatDiv(chat)));
        }

    }

    for(const chat of chats) {
        if(chat instanceof SingleChat)
            addStatusHandlerPerson(chat.othetPersonId);
        else if(chat instanceof GroupChat)
            subscribeToGroupChatTopic(chat.chatId);
    }
}

async function addGroupChatToStart(groupChat) {
    chatMap[groupChat.chatId] = groupChat;
    chatListBox.insertBefore(document.createRange().createContextualFragment(await toGroupChatDiv(groupChat)), chatListBox.firstChild);
    subscribeToGroupChatTopic(groupChat.chatId)
}

async function removeGroupChat(groupChatId) {
    for (let element of document.getElementsByClassName("group_chat_by_id_" + groupChatId)) {
        element.remove();
    }
    if(selectedToEditGroupChatId === groupChatId) {
        document.getElementById("group_chat_edit_menu_box").classList.add("hidden");
    }

    if(selectedToShowGroupChatId === groupChatId) {
        document.getElementById("group_chat_show_menu_box").classList.add("hidden");
    }
}


async function selectChat(event, chatId, type) {
    if(event.currentTarget.classList.contains("selected_chat")) return;
    if(event.currentTarget.classList.contains("un_search") && type === "search_chat") return;

    for (const element of document.getElementsByClassName("selected_chat")) {
        element.classList.remove("selected_chat");
    }
    event.currentTarget.classList.add("selected_chat");
    if(type === "search_chat") {
        selectedChatTypeIsFromSearch = true;
        selectedChatId = chatId;
        selectedChatType = "single_chat";
        firstLoadedMessage = null;
        loadedToEnd = false;

        document.getElementById("chat_messages").innerHTML = '';

        document.getElementById("search_input").value = "";
        await deleteAllSearchChat();
        await showAllChats();
    } else {
        selectedChatTypeIsFromSearch = false;
        selectedChatId = chatId;
        selectedChatType = type;
        firstLoadedMessage = null;
        loadedToEnd = false;

        document.getElementById("chat_messages").innerHTML = '';
        await loadMessages(chatId, type)
    }
}

async function singleChatNewMessage(message) {
    if(selectedChatId === message.chatId) {
        await addMessage(message);
    } else if(selectedChatTypeIsFromSearch && (id === message.senderId || selectedChatId === message.senderId)) {
        const searchChatDiv = document.getElementsByClassName("person_chat_by_id_" + selectedChatId)[0];

        searchChatDiv.classList.remove("search_chat");
        searchChatDiv.classList.add("single_chat_by_id_" + message.chatId);
        searchChatDiv.classList.add("chat_by_id_" + message.chatId);
        searchChatDiv.classList.add("un_search");

        searchChatDiv.addEventListener("onclick", () => selectChat(event, `${message.chatId}`, "single_chat"))


        getPersonStatus(selectedChatId).then((s) => {
            changeSingleChatStatus(selectedChatId, s)
        })
        addStatusHandlerPerson(selectedChatId);

        selectedChatId = message.chatId;
        selectedChatTypeIsFromSearch = false;

        await addMessage(message);
    }
    await changeChatLastMessage(message, "single_chat")
}

async function groupChatNewMessage(message) {
    if(selectedChatId === message.chatId) {
        await addMessage(message);
    } else {

    }
    await changeChatLastMessage(message, "group_chat")
}

async function changeSingleChatStatus(personId, status) {
    let chatDiv = document.getElementsByClassName("person_chat_by_id_" + personId);
    if(chatDiv.length === 0) return;

    let statusCircle = chatDiv[0].querySelector(".status_circle");

    statusCircle.classList.remove("online");
    statusCircle.classList.remove("invisible");
    statusCircle.classList.remove("not_disturb");
    statusCircle.classList.remove("not_active");

    statusCircle.classList.add(status.toLowerCase());
}


async function sendMessageToChat(text) {
    if(selectedChatId === null) return;

    if(selectedChatType === "single_chat") {
        if(selectedChatTypeIsFromSearch) {
            sendMessage("/messenger/single_chat/message", {receiverId: selectedChatId, text: text})
        } else {
            let chat = await getChatByChatIdAndType(selectedChatId, "single_chat");
            sendMessage("/messenger/single_chat/message", {receiverId: chat.othetPersonId, text: text})
        }
    } else if(selectedChatType === "group_chat") {
        sendMessage("/messenger/group_chat/message", {receiverId: selectedChatId, text: text} )
    }

}

async function addToSearchChat(person) {
    if(document.getElementsByClassName("person_chat_by_id_" + person.id).length !== 0) {
        document.getElementsByClassName("person_chat_by_id_" + person.id)[0].style.display = "flex";
    } else {
        chatListBox.insertBefore(document.createRange().createContextualFragment(await toSearchPersonChatDiv(person)), chatListBox.firstChild);
    }
}


async function hideAllChats() {
    for (let chatElement of document.getElementsByClassName("single_chat")) {
        chatElement.style.display = "none"
    }
    for (let chatElement of document.getElementsByClassName("group_chat")) {
        chatElement.style.display = "none"
    }
}
async function showAllChats() {
    for (let chatElement of document.getElementsByClassName("single_chat")) {
        chatElement.style.display = "flex"
    }
    for (let chatElement of document.getElementsByClassName("group_chat")) {
        chatElement.style.display = "flex"
    }
}

async function deleteAllSearchChat() {
    const searchChats = document.getElementsByClassName("search_chat");

    for (let i = searchChats.length - 1; i >= 0; i--) {
        const chat = searchChats[i];
        if (!chat.classList.contains("selected_chat")) {
            chat.remove();
        }
    }
}





















