import * as Utils from "./Utils";

export async function login(username) {
    const userObject = {"username":username};
    const url ="/server_Web_exploded/login";
    const method = 'POST';
    const result = await Utils.fetchWrapper(method, url, userObject);
    return result ;
}
