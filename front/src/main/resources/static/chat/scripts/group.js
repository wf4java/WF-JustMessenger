const groupChatProfilePhotoMap = {};

let createGroupSelectedPersons = [];
const selectedDiv = document.getElementById("create_group_chat_selected_persons_box");
async function groupChatCreateAddToSelectedList(personId, itsMe) {
    if(itsMe == null) itsMe = (myPerson.id === personId);
    await groupChatCreateRemoveFromSelectiveList(personId);
    if(document.getElementsByClassName("create_group_chat_selected_person_by_id_" + personId).length !== 0) return;
    if(document.getElementsByClassName("create_group_chat_select_person_by_id_" + personId).length !== 0) return;
    selectedDiv.appendChild(document.createRange().createContextualFragment(await toGroupChatSelectedPersonDiv(await getPersonById(personId), itsMe)));
    if (!itsMe)
        createGroupSelectedPersons.push(personId)
}

const selectiveDiv = document.getElementById("create_group_chat_select_persons_search_box");
async function groupChatCreateAddToSelectiveList(personId) {
    if(document.getElementsByClassName("create_group_chat_selected_person_by_id_" + personId).length !== 0) return;
    if(document.getElementsByClassName("create_group_chat_select_person_by_id_" + personId).length !== 0) return;
    selectiveDiv.appendChild(document.createRange().createContextualFragment(await toGroupChatSelectivePersonDiv(await getPersonById(personId))));
}


async function groupChatCreateRemoveFromSelectedList(personId) {
    for (let elementsByClassNameElement of document.getElementsByClassName("create_group_chat_selected_person_by_id_" + personId)) {
        elementsByClassNameElement.remove();
    }
    let index = createGroupSelectedPersons.indexOf(personId);
    if (index !== -1)
        createGroupSelectedPersons.splice(index, 1);

    await groupChatCreateAddToSelectiveList(personId);
}

async function groupChatCreateRemoveFromSelectiveList(personId) {
    for (let elementsByClassNameElement of document.getElementsByClassName("create_group_chat_select_person_by_id_" + personId)) {
        elementsByClassNameElement.remove();
    }
}


async function loadGroupCreateMenu() {
    await groupChatCreateAddToSelectedList(myPerson.id);
    let count = 0;
    for (const id in personMap) {
        if (id === myPerson.id) continue;
        await groupChatCreateAddToSelectiveList(id);
        if (++count >= 99) break;
    }
}

async function resetGroupCreateMenu() {
    selectedDiv.innerHTML = "";
    selectiveDiv.innerHTML = "";
    document.getElementById("create_group_chat_search_input").value = "";
    document.getElementById("group_chat_create_name").value = "";
    createGroupSelectedPersons = [];
}



const createGroupChatSearchInput = document.getElementById("create_group_chat_search_input")
async function groupChatCreateSearchInputKeyUp() {
    const searchValue = createGroupChatSearchInput.value;
    if (searchValue.length === 0) {
        selectiveDiv.innerHTML = "";
        await loadGroupCreateMenu();
    } else {
        let persons = await searchPersonByUsername(searchValue);
        selectiveDiv.innerHTML = "";
        for (const person of persons) {
            if (person.id === myPerson.id) continue;
            await groupChatCreateAddToSelectiveList(person.id);
        }
    }
}

function groupChatCreateSearchInputKeyUpDebounce(func, delay) {
    let timeoutId;
    return function (...args) {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => {
            func.apply(this, args);
        }, delay);
    };
}
createGroupChatSearchInput.addEventListener("keyup", groupChatCreateSearchInputKeyUpDebounce(groupChatCreateSearchInputKeyUp, 300));


let isCreatingGroupChat = false;
async function createGroupChat(event) {
    if(isCreatingGroupChat) return
    try {
        isCreatingGroupChat = true;
        if (!event.currentTarget.classList.contains("active")) return


        const name = document.getElementById("group_chat_create_name").value;
        await postRequestWithsBody("/api/group_chat/create", {name: name, members: createGroupSelectedPersons});
        document.getElementById("create_group_chat_menu_box").classList.add("hidden");
    } finally {
        isCreatingGroupChat = false;
    }
}


async function getGroupChatProfilePhoto(chatId) {
    const cachedProfilePhoto = groupChatProfilePhotoMap[chatId];
    if(cachedProfilePhoto != null) return cachedProfilePhoto;

    const response = await getRequestWithsParams("/api/group_chat/profile_photo", {chatId : chatId});

    const file = new Blob([new File([await response.blob()], "img.png")], { type: 'image/png' })

    const profilePhoto = new ProfilePhoto(file);
    groupChatProfilePhotoMap[chatId] = profilePhoto;

    return profilePhoto;
}

async function getGroupChatPhotoWithUpdate(chatId) {
    const response = await getRequestWithsParams("/api/group_chat/profile_photo", {chatId : chatId});
    if(!response.ok) throw new Error(await response.json());

    const file = new Blob([new File([await response.blob()], "img.png")], { type: 'image/png' })

    const profilePhoto = new ProfilePhoto(file);
    groupChatProfilePhotoMap[chatId] = profilePhoto;

    return profilePhoto;
}


async function changeGroupChatProfilePhotoUpdate(chatId) {
    const profilePhoto = await getGroupChatPhotoWithUpdate(chatId);

    const groupChat = await getChatByChatIdAndType(chatId, "group_chat");
    if(groupChat != null)
        groupChat.hasProfilePhoto = true;

    let chatDiv = document.getElementsByClassName("group_chat_by_id_" + chatId);
    if(chatDiv.length === 0) return;
    chatDiv[0].querySelector(".profile_photo_box img").src = profilePhoto.url;

    if(selectedToEditGroupChatId === chatId) {
        document.getElementById("group_chat_show_profile_photo").src = profilePhoto.url
        document.getElementById("group_chat_delete_profile_photo").classList.add("active")
    }

    if(selectedToShowGroupChatId === chatId)
        document.getElementById("group_chat_show_show_profile_photo").src = profilePhoto.url
}

async function changeGroupChatNameUpdate(chatId, name) {
    let groupChat = await getChatByChatIdAndType(chatId, "group_chat");
    groupChat.name = name;

    let chatDiv = document.getElementsByClassName("group_chat_by_id_" + chatId);
    chatDiv[0].querySelector(".chat_username").textContent = name + "(" + groupChat.participantCount +")"

    if(selectedToEditGroupChatId === chatId)
        document.getElementById("change_group_chat_name").textContent = name;

    if(selectedToShowGroupChatId === chatId)
        document.getElementById("change_group_show_chat_name").textContent = name;
}

async function changeGroupChatProfilePhotoDelete(chatId) {
    const groupChat = await getChatByChatIdAndType(chatId, "group_chat");
    if(groupChat != null)
        groupChat.hasProfilePhoto = false;

    let chatDiv = document.getElementsByClassName("group_chat_by_id_" + chatId);
    if(chatDiv.length === 0) return;
    chatDiv[0].querySelector(".profile_photo_box img").src = "../images/empty_group_chat_profile_photo.png";

    if(selectedToEditGroupChatId === chatId){
        document.getElementById("group_chat_show_profile_photo").src = "../images/empty_group_chat_profile_photo.png";
        document.getElementById("delete_profile_photo").classList.remove("active")
    }

    if(selectedToShowGroupChatId === chatId)
        document.getElementById("group_chat_show_show_profile_photo").src = "../images/empty_group_chat_profile_photo.png";

}

async function removeParticipant(chatId, personId) {
    if(selectedToEditGroupChatId === chatId){
        let participantDiv = document.getElementsByClassName("group_chat_participant_by_id_" + personId);
        if(participantDiv.length !== 0)
            participantDiv[0].remove()
    }

    if(selectedToShowGroupChatId === chatId){
        let participantDiv = document.getElementsByClassName("group_chat_show_participant_by_id_" + personId);
        if(participantDiv.length !== 0)
            participantDiv[0].remove()
    }

    let groupChat = await getChatByChatIdAndType(chatId, "group_chat");
    groupChat.participantCount = groupChat.participantCount - 1;

    let chatDiv = document.getElementsByClassName("group_chat_by_id_" + chatId);
    chatDiv[0].querySelector(".chat_username").textContent = groupChat.name + "(" + groupChat.participantCount +")"
}

async function addParticipant(chatId, personId) {
    if(selectedToEditGroupChatId === chatId){
        await addToParticipantEditsBox(await getPersonById(personId))
    }

    if(selectedToShowGroupChatId === chatId){
        await addToParticipantShowBox(await getPersonById(personId))
    }

    let groupChat = await getChatByChatIdAndType(chatId, "group_chat");
    groupChat.participantCount = groupChat.participantCount + 1;

    let chatDiv = document.getElementsByClassName("group_chat_by_id_" + chatId);
    chatDiv[0].querySelector(".chat_username").textContent = groupChat.name + "(" + groupChat.participantCount +")"
}

















































































