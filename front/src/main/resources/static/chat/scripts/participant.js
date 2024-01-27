let selectedToEditGroupChatId;
let selectedMode;
let selectedToEditGroupChatParticipantsIds = [];

let selectedToShowGroupChatId = [];
let selectedToShowGroupChatParticipantsIds = [];

const participantEditsBox = document.getElementById("group_chat_participant_box");
async function addToParticipantEditsBox(person) {
    participantEditsBox.appendChild(document.createRange().createContextualFragment(await toGroupChatParticipantDiv(person)))
}

async function getAllParticipantById(groupChatId) {
    const response = await getRequestWithsParams("/api/group_chat/participant/all", {chatId: groupChatId})
    const participants = [];

    for (const participantJson of await response.json()) {
        participants.push(Participant.fromJson(participantJson));
    }

    return participants;
}

async function getAllParticipantByIdAsPersons(groupChatId) {
    const participants = await getAllParticipantById(groupChatId);
    return await getAllPersonByIds(participants.map(participant => participant.personId));
}

async function resetGroupChatEditMenu() {
    participantEditsBox.innerHTML = "";
    selectedMode = "remove_mode";
    document.getElementById("group_chat_profile_photo_input_file").value = "";
    document.getElementById("group_chat_upload_profile_photo").classList.remove("active");
    document.getElementById("group_chat_delete_profile_photo").classList.remove("active");
    document.getElementById("change_group_chat_name_accept_button").classList.remove("acceptable");
    changeGroupChatNameP.classList.remove("editable");
    changeGroupChatNameP.setAttribute("contenteditable", false);
    selectedToEditGroupChatId = null;
    selectedToEditGroupChatParticipantsIds = [];
}

async function loadGroupChatEditMenu(groupChat) {
    selectedToEditGroupChatId = groupChat.chatId;
    document.getElementById("group_chat_show_profile_photo").src = await getGroupChatProfilePhotoSrc(groupChat.chatId);
    document.getElementById("change_group_chat_name").textContent = groupChat.name;

    if(groupChat.hasProfilePhoto)
        document.getElementById("group_chat_delete_profile_photo").classList.add("active");

    await addToParticipantEditsBox(myPerson);
    for (const person of (await getAllParticipantByIdAsPersons(groupChat.chatId))) {
        selectedToEditGroupChatParticipantsIds.push(person.id)
        if(person.id === myPerson.id) continue;
        await addToParticipantEditsBox(person);
    }
}

async function openGroupChatMenu(event, groupChatId) {

    let groupChat = await getChatByChatIdAndType(groupChatId, "group_chat")

    if(groupChat.ownerId === myPerson.id) {
        document.getElementById("group_chat_edit_menu_box").classList.remove("hidden")
        await resetGroupChatEditMenu();
        await loadGroupChatEditMenu(await getChatByChatIdAndType(groupChatId, "group_chat"))
    } else {
        document.getElementById("group_chat_show_menu_box").classList.remove("hidden")
        await resetGroupChatShowMenu();
        await loadGroupChatShowMenu(await getChatByChatIdAndType(groupChatId, "group_chat"))
    }
}


async function removeParticipantRequest(personId) {
    await postRequestWithsBody("/api/group_chat/participant/remove", {chatId: selectedToEditGroupChatId, personId: personId})
}

async function changeParticipantMode(mode) {
    if(mode === "remove_mode") {

    } else if(mode === "add_mode") {

    }
}





const participantShowsBox = document.getElementById("group_chat_show_participant_box");
async function resetGroupChatShowMenu() {
    participantShowsBox.innerHTML = "";
    document.getElementById("group_chat_show_show_profile_photo").src = "";
    selectedToShowGroupChatId = null;
    selectedToShowGroupChatParticipantsIds = [];
}


async function loadGroupChatShowMenu(groupChat) {
    selectedToShowGroupChatId = groupChat.chatId;
    document.getElementById("group_chat_show_show_profile_photo").src = await getGroupChatProfilePhotoSrc(groupChat.chatId);
    document.getElementById("change_group_show_chat_name").textContent = groupChat.name;

    await addToParticipantShowBox(myPerson);
    for (const person of (await getAllParticipantByIdAsPersons(groupChat.chatId))) {
        selectedToShowGroupChatParticipantsIds.push(person.id)
        if(person.id === myPerson.id) continue;
        await addToParticipantShowBox(person);
    }
}

async function addToParticipantShowBox(person) {
    participantShowsBox.appendChild(document.createRange().createContextualFragment(await toGroupChaShowParticipantDiv(person)))
}


















































