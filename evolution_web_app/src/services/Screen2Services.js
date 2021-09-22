import * as Utils from "./Utils";

export async function getAll() {
    const payload = {};
    const url = "/server_Web_exploded/screen2";
    const method = 'GET';
    const result = await Utils.fetchWrapper(method, payload, url);

    console.log(result);
    return result;
}