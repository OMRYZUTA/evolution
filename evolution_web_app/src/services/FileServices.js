import ServiceError from "./ServiceError";

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
    let responseBody;

    const contentType = result.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
        responseBody = await result.json();
    } else {
        responseBody = await result.text();
    }

    if (!result.ok) {
        throw new ServiceError(method, url, result.statusCode, result.statusText, responseBody)
    }

    return responseBody;
}
