import {fetchWrapper} from "./Utils";

export async function login(username) {
    const url = "/evolution/api/login";
    const method = 'POST';
    const data = {username};
    try {
        return await fetchWrapper(method, url, data);
    } catch (e) {
        console.error('failed login', e)
    }
}
