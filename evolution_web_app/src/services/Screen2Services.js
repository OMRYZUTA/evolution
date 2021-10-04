import * as Utils from "./Utils";

export async function getAll() {
    const url = "/server_Web_exploded/api/dashboard";
    const method = 'GET';
    const result = await Utils.fetchWrapper(method, url, null);
    return result;
}