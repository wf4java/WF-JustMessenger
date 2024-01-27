async function load() {
    if (jwt == null || jwt === "")
        document.location.href = "/login";

    if (refreshToken == null || refreshToken === "")
        document.location.href = "/login";

    jwt = await getJwtToken();

    if (jwt == null || jwt === "")
        document.location.href = "/login";

    if (refreshToken == null || refreshToken === "")
        document.location.href = "/login";

    id = parseJwt(jwt).id;

    await connect();
    loadMyPerson();
    loadChats();
}

load()











