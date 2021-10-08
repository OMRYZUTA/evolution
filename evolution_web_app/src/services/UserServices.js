import {fetchWrapper} from "./Utils";

export async function login(username) {
    const url = "/server_Web_exploded/api/login";
    const method = 'POST';
    const data = {username};
    try {
        return await fetchWrapper(method, url, data);
    } catch (e) {
        console.error('failed login', e)
    }
}
