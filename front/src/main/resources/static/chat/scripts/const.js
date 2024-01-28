//const host = "http://" + window.location.host.split(':')[0] + ":8002";
const host = "http://194.169.160.253:8002"
let jwt = getCookie("jwtToken");
let refreshToken = getCookie("refreshToken");

let id = jwt ? parseJwt(jwt).id : null;
let myPerson;