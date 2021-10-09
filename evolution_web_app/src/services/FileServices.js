import ServiceError from "./ServiceError";
import * as Utils from "./Utils";

export async function uploadFile(file) {
    const url = "/server_Web_exploded/api/dashboard";
    const method = 'POST';
    return await fetchXmlWrapper(method, url, file);
}

const fetchXmlWrapper = async (method, url, file) => {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/xml',
        },
        body: file,
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
    }

    const result = await fetch(url, options);
    return await Utils.buildResponse(result, method, url);
}
