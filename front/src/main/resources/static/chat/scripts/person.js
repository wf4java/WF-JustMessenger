const personMap = {};
const personProfilePhotoMap = {};

async function getPersonById(id) {
    let person = personMap[id];
    if(person !== undefined) return person;

    const response = await getRequestWithsParams("/api/person", {personId: id});
    person = Person.fromJson(await response.json());
    personMap[person.id] = person;
    return person;
}

async function getAllPersonByIds(ids) {
    const missingIds = [];
    const persons = [];

    for (const id of ids) {
        let person = personMap[id];
        if (person !== undefined) {
            persons.push(person);
        } else {
            missingIds.push(id);
        }
    }

    if (missingIds.length > 0) {
        const response = await getRequestWithsParams("/api/person/all", { personIds: missingIds });
        const missingPersons = await response.json();

        for (const personJson of missingPersons) {
            const person = Person.fromJson(personJson);
            persons.push(person);
            personMap[person.id] = person;
        }
    }

    return persons;
}

async function getMyPerson() {
    const response = await getRequest("/api/person/me");
    return Person.fromJson(await response.json());
}

async function loadMyPerson() {
    myPerson = await getMyPerson();
    changeUsernameP.textContent = myPerson.username;
    document.querySelector(".profile_photo_my").src = myPerson.hasProfilePhoto ? (await getPersonProfilePhotoWithUpdate(myPerson.id)).url : `../images/empty_profile_photo.svg`;
    if(myPerson.hasProfilePhoto){
        document.getElementById("delete_profile_photo").classList.add("active");
    } else {
        document.getElementById("delete_profile_photo").classList.add("delete");
    }

    if(document.getElementsByClassName("person_chat_by_id_" + id).length !== 0) {
        if(myPerson.hasProfilePhoto) {
            document.getElementsByClassName("person_chat_by_id_" + id)[0].querySelector(".profile_photo_box img").src
                = (await getPersonProfilePhotoWithUpdate(myPerson.id)).url;
        } else {
            document.getElementsByClassName("person_chat_by_id_" + id)[0].querySelector(".profile_photo_box img").src
                = "../images/empty_profile_photo.svg";
        }
    }

}


async function changeUsername(newUsername) {
    const response = await postRequestWithsBody("/api/person/edit/username", {username: newUsername});
    myPerson.username = newUsername;
}



async function changePersonUsername(personId, username) {
    if(personId === id) return;
    const cachedPerson = personMap[personId];
    if(cachedPerson !== null && cachedPerson !== undefined)
        cachedPerson.username = username;

    let chatDiv = document.getElementsByClassName("person_chat_by_id_" + personId);
    if(chatDiv.length === 0) return;
    chatDiv[0].querySelector(".chat_username").textContent = username;

}

async function getPersonProfilePhoto(personId) {
    const cachedProfilePhoto = personProfilePhotoMap[personId];
    if(cachedProfilePhoto != null) return cachedProfilePhoto;

    const response = await getRequestWithsParams("/api/person/profile_photo", {personId : personId});
    if(!response.ok) throw new Error(await response.json());

    const file = new Blob([new File([await response.blob()], "img.png")], { type: 'image/png' })

    const profilePhoto = new ProfilePhoto(file);
    personProfilePhotoMap[personId] = profilePhoto;

    return profilePhoto;
}

async function getPersonProfilePhotoWithUpdate(personId) {
    const response = await getRequestWithsParams("/api/person/profile_photo", {personId : personId});
    if(!response.ok) throw new Error(await response.json());

    const file = new Blob([new File([await response.blob()], "img.png")], { type: 'image/png' })



    const profilePhoto = new ProfilePhoto(file);
    personProfilePhotoMap[personId] = profilePhoto;

    return profilePhoto;
}



async function changePersonProfilePhotoUpdate(personId) {
    const profilePhoto = await getPersonProfilePhotoWithUpdate(personId);

    if(personId === id) return;
    const cachedPerson = personMap[personId];
    if(cachedPerson != null)
        cachedPerson.hasProfilePhoto = true;

    let chatDiv = document.getElementsByClassName("person_chat_by_id_" + personId);
    if(chatDiv.length === 0) return;
    chatDiv[0].querySelector(".profile_photo_box img").src = profilePhoto.url;
}

async function changePersonProfilePhotoDelete(personId) {
    delete personProfilePhotoMap[personId];

    if(personId === id) return;
    const cachedPerson = personMap[personId];
    if(cachedPerson !== null && cachedPerson !== undefined)
        cachedPerson.hasProfilePhoto = false;

    let chatDiv = document.getElementsByClassName("person_chat_by_id_" + personId);
    if(chatDiv.length === 0) return;
    chatDiv[0].querySelector(".profile_photo_box img").src = "../images/empty_profile_photo.svg";
}

async function uploadProfilePhoto(image) {
    image = await cropImage(image, 512, 512)
    await postRequestWithsFormData("/api/person/profile_photo", {file: image});
}

async function deleteProfilePhoto() {
    const response = await deleteRequest("/api/person/profile_photo");
    if(!response.ok) throw new Error(response.json());
}

async function getPersonStatus(personId) {
    const response = await getRequestWithsParams("/api/person/status/current", {personId: personId})
    return (await response.json()).status
}

async function searchPersonByUsername(username) {
    const response = await getRequestWithsParams("/api/person/search", {username: username});

    const persons = [];

    for (const personJson of await response.json()) {
        const person = Person.fromJson(personJson);
        persons.push(person);
        personMap[person.id] = person;
    }

    return persons;
}





