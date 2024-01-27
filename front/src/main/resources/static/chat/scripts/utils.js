async function getRequest(url) {
    const response = await fetch(host + url, {
        headers: {
            'Access-Control-Allow-Origin': '*',
            "Authorization": "Bearer " + (await getJwtToken())
        }
    });
    if(!response.ok) {
        let message;
        try {message = (await response.json()).message;}
        catch (e) {message = e.toString();}

        displayError(message)
        throw new Error()
    }
    return response;
}

async function getRequestWithsParams(url, json) {
    const response = await fetch(host + url + jsonToParameters(json), {
        method: 'GET',
        headers: {
            'Access-Control-Allow-Origin': '*',
            'Authorization': 'Bearer ' + (await getJwtToken())
        }
    });
    if(!response.ok) {
        let message;
        try {message = (await response.json()).message;}
        catch (e) {message = e.toString();}

        displayError(message)
        throw new Error()
    }
    return response;
}

async function postRequest(url) {
    const response = await fetch(host + url, {
        method: 'POST',
        headers: {
            'Access-Control-Allow-Origin': '*',
            'Authorization': 'Bearer ' + (await getJwtToken())
        }
    });
    if(!response.ok) {
        let message;
        try {message = (await response.json()).message;}
        catch (e) {message = e.toString();}

        displayError(message)
        throw new Error()
    }
    return response;
}


async function postRequestWithsBody(url, json) {
    const response = await fetch(host + url, {
        method: 'POST',
        headers: {
            'Access-Control-Allow-Origin': '*',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + (await getJwtToken())
        },
        body: JSON.stringify(json)
    });
    if(!response.ok) {
        let message;
        try {message = (await response.json()).message;}
        catch (e) {message = e.toString();}

        displayError(message)
        throw new Error()
    }
    return response;
}

async function postRequestWithsBodyWithoutJwt(url, json) {
    return await fetch(host + url, {
        method: 'POST',
        headers: {
            'Access-Control-Allow-Origin': '*',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(json)
    });
}

async function postRequestWithsParams(url, json) {
    const response = await fetch(host + url + jsonToParameters(json), {
        method: 'POST',
        headers: {
            'Access-Control-Allow-Origin': '*',
            'Authorization': 'Bearer ' + (await getJwtToken())
        }
    });
    if(!response.ok) {
        let message;
        try {message = (await response.json()).message;}
        catch (e) {message = e.toString();}

        displayError(message)
        throw new Error()
    }
    return response;
}



async function postRequestWithsFormData(url, json) {
    const formData = new FormData();
    for (const key in json) {
        if(key === null || key === undefined) continue;
        let value = json[key];
        formData.append(key, value);
    }


    const response = await fetch(host + url, {
        method: 'POST',
        headers: {
            'Access-Control-Allow-Origin': '*',
            'Authorization': 'Bearer ' + (await getJwtToken())
        },
        body: formData
    });
    if(!response.ok) {
        let message;
        try {message = (await response.json()).message;}
        catch (e) {message = e.toString();}

        displayError(message)
        throw new Error()
    }
    return response;
}



async function deleteRequest(url) {
    const response = await fetch(host + url, {
        method: 'DELETE',
        headers: {
            'Access-Control-Allow-Origin': '*',
            'Authorization': 'Bearer ' + (await getJwtToken())
        }
    });
    if(!response.ok) {
        let message;
        try {message = (await response.json()).message;}
        catch (e) {message = e.toString();}

        displayError(message)
        throw new Error()
    }
    return response;
}

async function deleteRequestWithBody(url, json) {
    const response = await fetch(host + url, {
        method: 'DELETE',
        headers: {
            'Access-Control-Allow-Origin': '*',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + (await getJwtToken())
        },
        body: JSON.stringify(json)
    });
    if(!response.ok) {
        let message;
        try {message = (await response.json()).message;}
        catch (e) {message = e.toString();}

        displayError(message)
        throw new Error()
    }
    return response;
}

function objectIdToTimestamp(objectId) {
    // if(objectId === null || objectId === undefined)
    //     return new Date(0);

    const timestamp = objectId.substring(0, 8);
    return new Date(parseInt(timestamp, 16) * 1000);
}

function parseJwt (token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

function setCookie(name, value, days) {
    const expires = new Date();
    expires.setTime(expires.getTime() + (days * 24 * 60 * 60 * 1000));
    document.cookie = `${name}=${encodeURIComponent(value)};expires=${expires.toUTCString()};path=/`;
}

function getCookie(name) {
    const cookieValue = document.cookie.match('(^|;)\\s*' + name + '\\s*=\\s*([^;]+)');
    return cookieValue ? cookieValue.pop() : '';
}

function clearCookie(name) {
    document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}

function getRandomElementOfList(list) {
    const randomIndex = Math.floor(Math.random() * list.length);
    return list[randomIndex];
}


function jsonToParameters(json) {
    const searchParams = new URLSearchParams();

    for (const key in json) {
        if(key === null || key === undefined) continue;
        let value = json[key];
        if(Array.isArray(value)) {
            for (const value2 of value) {
                searchParams.append(key, value2);
            }
        } else {
            searchParams.append(key, value);
        }
    }
    return "?" + searchParams.toString();
}


function cropImage(file, width, height) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = function (event) {
            const img = new Image();
            img.onload = function () {
                const canvas = document.createElement('canvas');
                const ctx = canvas.getContext('2d');
                const aspectRatio = img.width / img.height;

                let newWidth, newHeight, offsetX, offsetY;

                if (aspectRatio > width / height) {
                    newWidth = img.height * (width / height);
                    newHeight = img.height;
                    offsetX = (img.width - newWidth) / 2;
                    offsetY = 0;
                } else {
                    newWidth = img.width;
                    newHeight = img.width * (height / width);
                    offsetX = 0;
                    offsetY = (img.height - newHeight) / 2;
                }

                canvas.width = width;
                canvas.height = height;
                ctx.drawImage(img, offsetX, offsetY, newWidth, newHeight, 0, 0, width, height);
                canvas.toBlob((blob) => {
                    resolve(blob);
                }, file.type);
            };
            img.src = event.target.result;
        };
        reader.readAsDataURL(file);
    });
}



function displayError(message) {
    document.getElementById("error_message_box").classList.remove("hidden");
    const errorsListDiv = document.getElementById("error_messages");
    let errorDiv = document.createRange().createContextualFragment(toErrorDiv(message));
    errorsListDiv.appendChild(errorDiv);
    errorDiv = errorsListDiv.children.item(errorsListDiv.children.length - 1);


    setTimeout(() => {
        errorDiv.classList.add("hidden_o")
    }, 9500);

    setTimeout(() => {
        errorsListDiv.removeChild(errorDiv);
        if(errorsListDiv.children.length === 0) {
            document.getElementById("error_message_box").classList.add("hidden");
        }
    }, 10000);
}


async function getPersonProfilePhotoSrc(personId) {
    let person = await getPersonById(personId)
    if(person == null || !person.hasProfilePhoto) return "../images/empty_profile_photo.svg";
    return (await getPersonProfilePhoto(personId)).url;
}

async function getGroupChatProfilePhotoSrc(groupChatId) {
    let groupChat = await getChatByChatIdAndType(groupChatId, "group_chat");
    if(groupChat == null || !groupChat.hasProfilePhoto) return "../images/empty_group_chat_profile_photo.png";
    return (await getGroupChatProfilePhoto(groupChat.chatId)).url;
}



function getFormattedTimeFromObjectId(objectId) {
    return  objectIdToTimestamp(objectId).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}


function isTokenExpired(token) {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
        atob(base64)
            .split("")
            .map(function (c) {
                return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
            })
            .join("")
    );

    const { exp } = JSON.parse(jsonPayload);
    return Date.now() >= (exp * 1000 )
}


async function getJwtToken() {
    if(isTokenExpired(jwt)) {
        const response = await postRequestWithsBodyWithoutJwt("/api/auth/refresh", {refreshToken: refreshToken})
        if(response.status === 401) {
            clearCookie("jwtToken")
            clearCookie("refreshToken")
            document.location.href="/";
        }
        if(!response.ok) {
            let message;
            try {message = (await response.json()).message;}
            catch (e) {message = e.toString();}

            displayError(message)
            throw new Error()
        }

        const responseJson = await response.json()

        setCookie("jwtToken", responseJson.jwtToken, 1)
        setCookie("refreshToken", responseJson.refreshToken, 24 * 30)

        jwt = responseJson.jwtToken;
        refreshToken = responseJson.refreshToken;
        id = parseJwt(jwt).id;

        return jwt;
    } else {
        return jwt;
    }
}







