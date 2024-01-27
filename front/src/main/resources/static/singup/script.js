const host = "http://" + window.location.host.split(':')[0] + ":8002";

function setCookie(name, value, h) {
    const expires = new Date();
    expires.setTime(expires.getTime() + (h * 60 * 60 * 1000));
    document.cookie = `${name}=${encodeURIComponent(value)};expires=${expires.toUTCString()};path=/`;
}

function toLoginPage() {
    document.location.href="/login";
}

async function signUp() {
    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    if(username.length === 0) {
        displayError("Empty username")
        return;
    }

    if(email.length === 0) {
        displayError("Empty username")
        return;
    }

    if(password.length === 0) {
        displayError("Empty username")
        return;
    }

    await postRequestWithsBody("/api/auth/signup", {username: username, email: email, password: password});
    const responseJson = await (await postRequestWithsBody("/api/auth/login", {username: username, password: password})).json();

    setCookie("jwtToken", responseJson.jwtToken, 24)
    setCookie("refreshToken", responseJson.refreshToken, 24 * 30)

    document.location.href="/";
}

function usernameInputKeyPress(event) {
    if(event.key === 'Enter' && event.currentTarget.value.length !== 0)
        document.getElementById("email").focus()

}

function emailInputKeyPress(event) {
    if(event.key === 'Enter' && event.currentTarget.value.length !== 0)
        document.getElementById("password").focus()

}

function passwordInputKeyPress(event) {
    if(event.key === 'Enter' && event.currentTarget.value.length !== 0)
        logIn()

}

async function postRequestWithsBody(url, json) {
    const response = await fetch(host + url, {
        method: 'POST',
        headers: {
            'Access-Control-Allow-Origin': '*',
            'Content-Type': 'application/json',
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


function toErrorDiv(message) {
    return `
        <div class="error_message">
            <p>Error: ${message}</p>
        </div>    
    `
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
    return Date.now() >= exp * 1000
}






























