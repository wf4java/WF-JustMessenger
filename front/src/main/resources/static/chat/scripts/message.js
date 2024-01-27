async function getAllOldMessagesFromSingleChat(chatId, offsetMessageId, limit) {
    const response = await getRequestWithsParams("/api/single_chat/message/old", {chatId: chatId, offsetMessageId: offsetMessageId, limit: limit});
    const messages = [];

    for (const messageJson of await response.json()) {
        const message = Message.fromJson(messageJson);
        messages.push(message);
    }

    messages.reverse();
    return messages;
}

async function getAllOldMessagesFromGroupChat(chatId, offsetMessageId, limit) {
    const response = await getRequestWithsParams("/api/group_chat/message/old", {chatId: chatId, offsetMessageId: offsetMessageId, limit: limit});
    const messages = [];

    for (const messageJson of await response.json()) {
        const message = Message.fromJson(messageJson);
        messages.push(message);
    }

    messages.reverse();
    return messages;
}




const chatMessagesDiv = document.getElementById("chat_messages");
async function addMessage(message, addToEnd) {
    if(document.getElementById(`message_by_id_${message.id}`) != null) return;

    const person = await getPersonById(message.senderId);
    if(addToEnd == null) addToEnd = true;
    if(addToEnd) {
        const lastMessageBox = chatMessagesDiv.children.item(0);
        if(lastMessageBox != null && lastMessageBox.classList.contains("user_message_box_for_" + person.username)) {
            lastMessageBox.insertBefore(document.createRange().createContextualFragment(toMessageDiv(message, person)), lastMessageBox.children.item(1));
        }
        else {
            const messageBox= document.createRange().createContextualFragment(await toMessageBox(message, person));
            messageBox.children.item(0).appendChild(document.createRange().createContextualFragment(toFirstMessageDiv(message, person)));
            chatMessagesDiv.insertBefore(messageBox, chatMessagesDiv.firstChild);
        }
    } else { //Username на последнем сообщении, а не на первом
        const lastMessageBox = chatMessagesDiv.children.item(chatMessagesDiv.children.length - 1);
        if(lastMessageBox != null && lastMessageBox.classList.contains("user_message_box_for_" + person.username)) {
            lastMessageBox.appendChild(document.createRange().createContextualFragment(toMessageDiv(message, person)), lastMessageBox.children.item(lastMessageBox.children.length - 1));
        }
        else {
            const messageBox= document.createRange().createContextualFragment(await toMessageBox(message, person));
            messageBox.children.item(0).appendChild(document.createRange().createContextualFragment(toFirstMessageDiv(message, person)));
            chatMessagesDiv.appendChild(messageBox);
        }
    }
}



async function loadMessages(chatId, type, offset, addToEnd) {
    let messages;
    let chat = await getChatByChatIdAndType(chatId, type);
    if(offset == null) offset = "ffffffffffffffffffffffff";
    if(type === "single_chat") {
        messages = await getAllOldMessagesFromSingleChat(chatId, offset, 100)
    } else if(type === "group_chat") {
        messages = await getAllOldMessagesFromGroupChat(chatId, offset, 100)
    }
    if(messages.length === 0) {
        loadedToEnd = true;
        return;
    }
    firstLoadedMessage = messages[0];

    for (let message of messages) {
        await addMessage(message, addToEnd);
    }
}

async function changeChatLastMessage(message, chatType) {
    //Sort
    let chat = await getChatByChatIdAndType(message.chatId, chatType)
    let chatDiv = document.getElementsByClassName("chat_by_id_" + message.chatId);

    if(chatDiv.length === 0) {
        if(chatType === "single_chat") {
            const chatListBox = document.getElementById("chat_list_box");
            const chatDiv = document.createRange().createContextualFragment(await toSingleChatDiv(await getChatByChatIdAndType(message.chatId, chatType)));

            chatListBox.insertBefore(chatDiv, chatListBox.firstChild);

            addStatusHandlerPerson(selectedChatId);
        }
    } else {
        if(chatType === "single_chat") {
            let person = await getPersonById(message.senderId);
            chatDiv[0].querySelector(".chat_username_message").replaceWith(document.createRange().createContextualFragment(await toSingleChatLastMessageDiv(chat, person, message)));
            const parentElement = chatDiv[0].parentNode;
            parentElement.insertBefore(chatDiv[0], parentElement.firstChild);

        } else if(chatType === "group_chat") {
            let chat = await getChatByChatIdAndType(message.chatId, chatType)
            chatDiv[0].querySelector(".chat_username_message").replaceWith(document.createRange().createContextualFragment(await toGroupChatLastMessageDiv(chat, message)));
            const parentElement = chatDiv[0].parentNode;
            parentElement.insertBefore(chatDiv[0], parentElement.firstChild);

        }
    }

}



























