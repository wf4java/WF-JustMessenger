async function toGroupChatDiv(groupChat) {
    const lastMessageContent = groupChat.lastMessage ? groupChat.lastMessage.text : "Group chat create";
    const lastMessagePersonUsername = groupChat.lastMessage ? (await getPersonById(groupChat.lastMessage.senderId)).username + ":" : "";

    const profilePhoto = groupChat.hasProfilePhoto ?
        `<img src="${(await getGroupChatProfilePhoto(groupChat.chatId)).url}">` :
        `<img src="../images/empty_group_chat_profile_photo.png">`;

    return `
        <div class="chat group_chat group_chat_by_id_${groupChat.chatId} chat_by_id_${groupChat.chatId}" ondblclick="openGroupChatMenu(event, '${groupChat.chatId}')" onclick="selectChat(event, '${groupChat.chatId}', 'group_chat')">
            <div class="profile_photo_box">
                ${profilePhoto}
            </div>
            <div class="chat_username_message">
                <p class="chat_username">${groupChat.name + "(" + groupChat.participantCount +")"} </p>
                <p class="chat_last_message"><span class="chat_last_message_username">${lastMessagePersonUsername} </span>${lastMessageContent}</p>
                <p class="chat_last_message_time">${groupChat.lastMessage ? groupChat.lastMessage.formatedTime : getFormattedTimeFromObjectId(groupChat.chatId)}</p>
            </div>
        </div>
    `
}

async function toSearchPersonChatDiv(person) {
    const profilePhoto = person.hasProfilePhoto ?
        `<img src="${(await getPersonProfilePhoto(person.id)).url}">` :
        `<img src="../images/empty_profile_photo.svg">`;
    const username = person.id === id ? "Favorites(Me)" : person.username;

    return `
        <div class="chat search_chat single_chat person_chat_by_id_${person.id}" onclick="selectChat(event, '${person.id}', 'search_chat')">
            <div class="profile_photo_box">
                ${profilePhoto}
                <div class="status_circle ${person.status.toLowerCase()}"></div>
            </div>
            
            <div class="chat_username_message">
                <p class="chat_username">${username}</p>
                <p class="chat_last_message">New chat</p>
                <p class="chat_last_message_time">--:--</p>
            </div>
        </div>
    `;
}

async function toSingleChatDiv(singleChat) {
    const person = await getPersonById(singleChat.othetPersonId);
    const profilePhoto = person.hasProfilePhoto ?
        `<img src="${(await getPersonProfilePhoto(person.id)).url}">` :
        `<img src="../images/empty_profile_photo.svg">`;

    const username = person.id === id ? "Favorites(Me)" : person.username;
    const lastMessageBy = singleChat.lastMessage ? (singleChat.lastMessage.senderId === id ? "Me" : (person.gender != null ? (person.gender === "FEMALE" ? "She" : "He") : "He")) : "";
    const lastMessageContent = singleChat.lastMessage ? singleChat.lastMessage.text : "";
    return `
        <div class="chat single_chat single_chat_by_id_${singleChat.chatId} chat_by_id_${singleChat.chatId} person_chat_by_id_${singleChat.othetPersonId}" onclick="selectChat(event, '${singleChat.chatId}', 'single_chat')">
            <div class="profile_photo_box">
                ${profilePhoto}
                <div class="status_circle ${person.status.toLowerCase()}"></div>
            </div>
            
            <div class="chat_username_message">
                <p class="chat_username">${username}</p>
                <p class="chat_last_message"><span class="chat_last_message_username">${lastMessageBy.length !== 0 ? lastMessageBy + ":" : ""} </span>${lastMessageContent}</p>
                <p class="chat_last_message_time">${singleChat.lastMessage ? singleChat.lastMessage.formatedTime : "0:00"}</p>
            </div>
        </div>
    `;
}

async function toSingleChatLastMessageDiv(chat, person, message) {
    const username = (await getPersonById(chat.othetPersonId)).username;
    const lastMessageBy = message.senderId === id ? "Me" : (person.gender != null ? (person.gender === "FEMALE" ? "She" : "He") : "He");
    const lastMessageContent = message.text;
    return `
        <div class="chat_username_message">
            <p class="chat_username">${username}</p>
            <p class="chat_last_message"><span class="chat_last_message_username">${lastMessageBy}: </span>${lastMessageContent}</p>
            <p class="chat_last_message_time">${message.formatedTime}</p>
        </div>
    `
}

async function toGroupChatLastMessageDiv(groupChat, message) {
    const lastMessageContent = message.text;
    const lastMessagePersonUsername = (await getPersonById(message.senderId)).username + ":"

    return `
        <div class="chat_username_message">
            <p class="chat_username">${groupChat.name + "(" + groupChat.participantCount +")"}</p>
            <p class="chat_last_message"><span class="chat_last_message_username">${lastMessagePersonUsername} </span>${lastMessageContent}</p>
            <p class="chat_last_message_time">${groupChat.lastMessage ? groupChat.lastMessage.formatedTime : getFormattedTimeFromObjectId(groupChat.chatId)}</p>
        </div>
    `
}


async function toMessageBox(message, person) {
    const profilePhoto = person.hasProfilePhoto ?
        `<img src="${(await getPersonProfilePhoto(person.id)).url}" class="in_message_profile_photo">` :
        `<img src="../images/empty_profile_photo.svg" class="in_message_profile_photo">`;

    return `
        <div class="user_message_box user_message_box_for_${person.username}">
            ${profilePhoto}
            <div class="messages_in_box">

             </div>
        </div>
    `
}


function toFirstMessageDiv(message, person) {
    return `
        <div class="message" id="message_by_id_${message.id}">
            <div class="in_message_item_box">
                <p class="in_message_username">${person.username}</p>
                <p class="in_message_text">${message.text}</p>
                <p class="in_message_time">${message.formatedTime}</p>
            </div>
        </div>
    `;
}

function toMessageDiv(message) {
    return `
        <div class="message" id="message_by_id_${message.id}">
            <div class="in_message_item_box">
                <p class="in_message_text">${message.text}</p>
                <p class="in_message_time">${message.formatedTime}</p>
            </div>
        </div>
    `;
}

function toErrorDiv(message) {
    return `
        <div class="error_message">
            <p>Error: ${message}</p>
         </div>    
    `
}



async function toGroupChatSelectedPersonDiv(person, itsMe) {
    return `
        <div class="create_group_chat_selected_person create_group_chat_selected_person_by_id_${person.id}"">
            <img class="profile_photo" src="${await getPersonProfilePhotoSrc(person.id)}" alt="Profile photo">
            <p class="username">${person.username}</p>
            <img class="delete ${itsMe ? "hidden" : ""}" src="../images/icons/delete.png" alt="Remove from list" onclick="groupChatCreateRemoveFromSelectedList('${person.id}')">
        </div>
    `
}

async function toGroupChatSelectivePersonDiv(person) {
    return `
        <div class="create_group_chat_selected_person create_group_chat_select_person_by_id_${person.id}">
            <img class="profile_photo" src="${await getPersonProfilePhotoSrc(person.id)}" alt="Profile photo">
            <p class="username">${person.username}</p>
            <img class="add" src="../images/icons/check.png" alt="Remove from list" onclick="groupChatCreateAddToSelectedList('${person.id}')">
        </div>
    `
}

async function toGroupChatParticipantDiv(person) {
    let img = `<img class="kick" src="../images/icons/delete.png" onclick="removeParticipantRequest('${person.id}')" alt="Remove from group chat">`;
    return `
        <div class="group_chat_participant group_chat_participant_by_id_${person.id}">
            <img class="profile_photo" src="${await getPersonProfilePhotoSrc(person.id)}" alt="Profile photo">
            <p class="username">${person.username}</p>
            ${person.id === myPerson.id ? "" : img}
        </div>
    `
}

async function toGroupChaShowParticipantDiv(person) {
    let img = `<img class="kick" src="../images/icons/delete.png" onclick="removeParticipantRequest('${person.id}')" alt="Remove from group chat">`;
    return `
        <div class="group_chat_show_participant group_chat_show_participant_by_id_${person.id}">
            <img class="profile_photo" src="${await getPersonProfilePhotoSrc(person.id)}" alt="Profile photo">
            <p class="username">${person.username}</p>
        </div>
    `
}














































