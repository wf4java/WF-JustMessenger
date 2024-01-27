const sendButtonBox = document.getElementById('send_message_send_box');

function sendMessageInputKeyUp(event) {
    if (event.currentTarget.value.length > 0) {
        if(selectedChatId === undefined) return;
        sendButtonBox.classList.remove('unsendable')
    } else {
        sendButtonBox.classList.add('unsendable');
    }
}


const input = document.getElementById('message_text');
function sendMessageInputKeyPress(event) {
    if(event.key === 'Enter') {
        if(selectedChatId === undefined) return;
        if(input.value.length === 0) return;
        sendMessageToChat(input.value)
        input.value = "";
        sendButtonBox.classList.remove('unsendable')
    }
}

function sendMessageButton(event) {
    if(selectedChatId === undefined) return;
    if(input.value.length === 0) return;
    sendMessageToChat(input.value)
    input.value = "";
    sendButtonBox.classList.remove('unsendable')
}



const editProfileMenuDiv = document.getElementById('edit_profile_menu_box');
function openProfileEditMenu(event) {
    if(editProfileMenuDiv.classList.contains("hidden")) {
        editProfileMenuDiv.classList.remove("hidden");
    } else {
        editProfileMenuDiv.classList.add("hidden");
    }
}

const createGroupChatMenuDiv = document.getElementById('create_group_chat_menu_box');
function openCreateGroupChatMenu(event) {
    if(createGroupChatMenuDiv.classList.contains("hidden")) {
        createGroupChatMenuDiv.classList.remove("hidden");
        resetGroupCreateMenu();
        loadGroupCreateMenu();
    } else {
        createGroupChatMenuDiv.classList.add("hidden");
    }
}


function hideOnClick(event) {
    if(event.target === event.currentTarget) {
        event.currentTarget.classList.add("hidden");
    }
}



const optionDiv = document.getElementById('options_box');
function openOptionButton(event) {
    if(event.currentTarget.classList.contains("opened")) {
        optionDiv.classList.add("hidden_v");
        event.currentTarget.classList.remove("opened");
    } else {
        optionDiv.classList.remove("hidden_v");
        event.currentTarget.classList.add("opened");
    }
}



const changeUsernameP = document.getElementById('change_username_p');
function changeUsernameEditable(event) {
    if(changeUsernameP.classList.contains("editable")) {
        changeUsernameP.classList.remove("editable");
        changeUsernameP.setAttribute("contenteditable", false);
    } else {
        changeUsernameP.classList.add("editable");
        changeUsernameP.setAttribute("contenteditable", true);
        changeUsernameP.focus();

    }
}

const changeGroupChatNameP = document.getElementById('change_group_chat_name');
function changeGroupChatNameEditable(event) {
    if(changeGroupChatNameP.classList.contains("editable")) {
        changeGroupChatNameP.classList.remove("editable");
        changeGroupChatNameP.setAttribute("contenteditable", false);
    } else {
        changeGroupChatNameP.classList.add("editable");
        changeGroupChatNameP.setAttribute("contenteditable", true);
        changeGroupChatNameP.focus();
    }
}

const changeUsernameAcceptButton = document.getElementById("change_username_accept_button");
async function changeUsernameButton(event) {
    if(!changeUsernameAcceptButton.classList.contains("acceptable")) return;

    await changeUsername(changeUsernameP.textContent);
    changeUsernameAcceptButton.classList.remove("acceptable")
    changeUsernameP.classList.remove("editable");
    changeUsernameP.setAttribute("contenteditable", false);
}


function changeUsernameOnKeyUp(event) {
    if(changeUsernameP.textContent !== myPerson.username) {
        changeUsernameAcceptButton.classList.add("acceptable")
    } else {
        changeUsernameAcceptButton.classList.remove("acceptable")
    }
}





function onProfilePhotoSelect(event) {
    if(event.currentTarget.value != null){
        document.getElementById("upload_profile_photo").classList.add("active");
    } else {

    }
}

async function onProfilePhotoUpload(event) {
    if(!event.currentTarget.classList.contains("active")) return;

    await uploadProfilePhoto(document.getElementById("profile_photo_input_file").files[0]);

    await loadMyPerson()
    document.getElementById("profile_photo_input_file").value = "";
    document.getElementById("upload_profile_photo").classList.remove("active");
}

async function onProfilePhotoDelete(event) {
    if(!event.currentTarget.classList.contains("active")) return;

    await deleteProfilePhoto();

    await loadMyPerson()
    document.getElementById("delete_profile_photo").classList.remove("active");
}



async function chatScroll(event) {
    if (!(event.currentTarget.offsetHeight + Math.abs(event.currentTarget.scrollTop) >= event.currentTarget.scrollHeight)) return
    if (loadedToEnd) return;
    if (firstLoadedMessage == null) return;

    await loadMessages(selectedChatId, selectedChatType, firstLoadedMessage.id, false);
}








const groupCreateActiveButton = document.getElementById('create_group_chat_button');
function onChangeGroupChatCreateKeyUp(event) {
    if (event.currentTarget.value.length > 0) {
        groupCreateActiveButton.classList.add('active')
    } else {
        groupCreateActiveButton.classList.remove('active');
    }
}



const changeGroupChatNameAcceptButton = document.getElementById("change_group_chat_name_accept_button");
async function changeGroupChatNameButton(event) {
    if(!changeGroupChatNameAcceptButton.classList.contains("acceptable")) return;

    await postRequestWithsBody("/api/group_chat/edit/name", {chatId: selectedToEditGroupChatId, name: changeGroupChatNameP.textContent})

    changeGroupChatNameAcceptButton.classList.remove("acceptable")
    changeGroupChatNameP.classList.remove("editable");
    changeGroupChatNameP.setAttribute("contenteditable", false);
}




function onGroupChatProfilePhotoSelect(event) {
    if(event.currentTarget.value != null){
        document.getElementById("group_chat_upload_profile_photo").classList.add("active");
    } else {

    }
}



async function onGroupChatProfilePhotoUpload(event) {
    if(!event.currentTarget.classList.contains("active")) return;

    let image = await cropImage(document.getElementById("group_chat_profile_photo_input_file").files[0], 512, 512)
    await postRequestWithsFormData("/api/group_chat/profile_photo", {file: image, chatId: selectedToEditGroupChatId});

    document.getElementById("group_chat_profile_photo_input_file").value = "";
    document.getElementById("group_chat_upload_profile_photo").classList.remove("active");
    document.getElementById("group_chat_delete_profile_photo").classList.add("active");
}


async function onGroupChatProfilePhotoDelete(event) {
    if(!event.currentTarget.classList.contains("active")) return;

    await deleteRequestWithBody("/api/group_chat/profile_photo", {chatId: selectedToEditGroupChatId});

    document.getElementById("group_chat_delete_profile_photo").classList.remove("active");
}

async function onGroupChatDelete(event) {
    await deleteRequestWithBody("/api/group_chat", {chatId: selectedToEditGroupChatId})
}

async function onGroupChatLeave(event) {
    await postRequestWithsBody("/api/group_chat/leave", {chatId: selectedToShowGroupChatId})
}

async function onChangeParticipantMode(event) {
    if(event.currentTarget.classList.contains("add_mode")) {
        event.currentTarget.classList.remove("add_mode");
        await changeParticipantMode("remove_mode");
    } else {
        event.currentTarget.classList.add("add_mode");
        await changeParticipantMode("add_mode");
    }
}





function changeGroupChatNameOnKeyUp(event) {
    if(changeGroupChatNameP.textContent !== getChatByChatIdAndType(selectedToEditGroupChatId, "group_chat")) {
        document.getElementById("change_group_chat_name_accept_button").classList.add("acceptable")
    } else {
        document.getElementById("change_group_chat_name_accept_button").classList.remove("acceptable")
    }
}



const searchInput = document.getElementById("search_input");
async function searchInputOnKeyUp() {
    if(searchInput.value.length === 0) {
        await deleteAllSearchChat();
        await showAllChats();
    } else {
        await deleteAllSearchChat();
        await hideAllChats();
        (await searchPersonByUsername(searchInput.value)).reverse().forEach((p) => {
            addToSearchChat(p)
        })
    }
}

function searchInputKeyUpDebounce(func, delay) {
    let timeoutId;
    return function (...args) {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => {
            func.apply(this, args);
        }, delay);
    };
}
searchInput.addEventListener("keyup", searchInputKeyUpDebounce(searchInputOnKeyUp, 300));

















