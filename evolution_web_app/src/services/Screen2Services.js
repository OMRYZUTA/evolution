import * as Utils from "./Utils";

export async function getAll() {
    const url = "/evolution/api/dashboard";
    const method = 'GET';
    return await Utils.fetchWrapper(method, url, null);
}